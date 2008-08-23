/*
 * Copyright (C) 2007, 2008 Join'g Team Members. All Rights Reserved.
 * Join'g Team Members are listed at project's home page. By the time of 
 * writting this at: https://joing.dev.java.net/servlets/ProjectMemberList.
 *
 * This file is part of Join'g project: www.joing.org
 *
 * GNU Classpath is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the free
 * Software Foundation; either version 3, or (at your option) any later version.
 * 
 * GNU Classpath is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * GNU Classpath; see the file COPYING.  If not, write to the Free Software 
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */
package org.joing.server.jsf.account;

import org.joing.server.ejb.Constant;
import org.joing.server.ejb.session.SessionManagerLocal;
import org.joing.server.ejb.user.UserManagerLocal;
import java.util.logging.Level;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import org.joing.common.dto.user.User;
import org.joing.common.exception.JoingServerException;
import org.joing.server.jsf.ManagedBean;

/**
 *
 * @author Fernando José Ortigosa
 */
public class CreateAccountForm extends ManagedBean {

    private String account = null;
    private String password = null;
    private String email = null;
    private String confirmPassword = null;
    private String firstName = null;
    private String secondName = null;
    private Boolean male = null;
    
    /** Default quota space for user accounts */
    final private int DEFAULT_QUOTA = 0; // Quota in megabytes, 0 for no limit

    //TODO make it configurable
    final private Integer quota = Integer.valueOf(DEFAULT_QUOTA);

    private @EJB UserManagerLocal userManager;
    private @EJB SessionManagerLocal sessionManager;
 
/*
    final private SessionManagerLocal sessionManager = new SessionManagerMock();
    final private UserManagerLocal userManager = new UserManagerMock();
*/
 
    private boolean testEmpty(FacesContext ctx, String var, String name) {
	
	boolean empty = var == null || var.trim().length() == 0;
	
	if(empty) {
	    ctx.addMessage(null, messageErrorMissing(name));
	}
	
	return empty;
    }
    
    private boolean testEmpty(FacesContext ctx, Boolean var, String name) {
	
	boolean empty = var == null;
	
	if(empty) {
	    ctx.addMessage(null, messageErrorMissing(name));
	}
	
	return empty;
    }
    
    private boolean testAccount(FacesContext ctx, String name) {
	
	boolean ok = !testEmpty(ctx, name, "account name");
	
	if(ok && !userManager.isValidAccount(name)) {
	    
	    ok = false;
	    
//TODO This message will not be valid if UserManagerBean.isValidAccount changes
	    
	    ctx.addMessage(null, new FacesMessage(
		FacesMessage.SEVERITY_ERROR,
		"Invalid account name format",
		"Account names must be between 4 and 32 characters long and "+
		"can only contain lowercase letters, numbers, dots and "+
		"underscores (low line)"));
	}
	
	if(ok && !sessionManager.isAccountAvailable(getJoingAccount())) {

	    ok = false;
	    
	    ctx.addMessage(null, new FacesMessage(
		FacesMessage.SEVERITY_ERROR,
		"Account name not available",
		"There is already a different account with that name, enter "+
		"a different account name and try again."));
	
	}
	
	return ok;
    }
    
    public String getJoingAccount() {
	return sessionManager.composeAccount(account);
    }
    
    private boolean testPassword(FacesContext ctx, String password) {
	
	boolean ok = !testEmpty(ctx, password, "password");
	
	if(ok && !userManager.isValidPassword(password)) {
	    
	    ok = false;
	    
//TODO This message will not be valid if UserManagerBean.isValidPassword changes
	    
	    ctx.addMessage(null, new FacesMessage(
		FacesMessage.SEVERITY_ERROR,
		"Invalid password format",
		"Passwords must be between 6 and 32 characters long"));

	}
	
	return ok;
	
    }
    
    private boolean inputDataIsValid(FacesContext ctx) {
	
	boolean missing = testEmpty(ctx, email, "email address");
	missing = testEmpty(ctx, firstName, "first name") || missing;
	missing = testEmpty(ctx, secondName, "second name") || missing;
	missing = testEmpty(ctx, male, "gender") || missing;
	missing = testEmpty(ctx, password, "password") || missing;
	missing = testEmpty(ctx, confirmPassword, "password confirmation") || missing;
		
	boolean ok = !missing;
	
	ok = testAccount(ctx, account) && ok;
	

	if(ok && !password.equals(confirmPassword)) {
    	    ok = false;
	    ctx.addMessage(null, new FacesMessage(
		FacesMessage.SEVERITY_ERROR,
		"Passwords do not match",
		"The password and password confirmation did not match, they " +
		"must match in order to create a new account"));
	}

	ok = ok && testPassword(ctx, password);
	
	return ok;
    }
    
    public String testInput() {

	FacesContext ctx = FacesContext.getCurrentInstance();
	
	return inputDataIsValid(ctx) ? "success": "error";

    }
    
    /**
     * <p>This method is invoked by the JSF framework (from the create-account 
     * form in index.jsp.</p>
     * 
     * <p>It should test the account info provided by the property setters and
     * try to create a new account in the system</p>
     */
    public String createAccount() {

	FacesContext ctx = FacesContext.getCurrentInstance();

	boolean ok = inputDataIsValid(ctx);

	if(ok) {

	    ok = false;
	    
	    try {
		User user =  userManager.createUser(account, password, email,
			firstName, secondName, male.booleanValue(),
			ctx.getViewRoot().getLocale(),
                        quota == null ? DEFAULT_QUOTA : quota.intValue());
		
		ok = user != null;
		
		if(user == null) {
		    ctx.addMessage(null, new FacesMessage(
			    FacesMessage.SEVERITY_ERROR,
			    "Unable to create user",
			    "The server was unable to create the user account "+
			    "you applied for"));
  
		}
		
	    } catch(JoingServerException ex) {

		ctx.addMessage(null, new FacesMessage(
			FacesMessage.SEVERITY_ERROR,
			ex.getMessage(),
			ex.getMessage()));

	    } catch (EJBException ex) {

		Constant.getLogger().log(Level.SEVERE,
			"Unable to create new user account", ex);

		String msg = "An error occurred while processing your" +
			" request, the server couldn't create a new account.";

		ctx.addMessage(null, new FacesMessage(
			FacesMessage.SEVERITY_FATAL, msg, msg));
	    }

	}

	return ok ? "success" : "error";

    }
    
    private FacesMessage messageErrorMissing(String itemName) {
	
	String summary = "Missing " + itemName;
	
	String template = "A %s is needed in order to create a new account";
	
	if(itemName.substring(0, 1).matches("(?i)[aeiou]")) {
	    template = "An %s is needed in order to create a new account";
	}

	String detail = String.format(template, itemName);

	return new FacesMessage(FacesMessage.SEVERITY_ERROR, summary, detail);

    }
    
    public void setAccount(String account) {
	this.account = account;
    }

    public String getAccount() {
	return account;
    }

    public void setPassword(String password) {
	this.password = password;
    }

    public String getPassword() {
	return password;
    }

    public void setConfirmPassword(String confirmPassword) {
	this.confirmPassword = confirmPassword;
    }

    public String getConfirmPassword() {
	return confirmPassword;
    }

    public void setEmail(String email) {
	this.email = email;
    }

    public String getEmail() {
	return email;
    }
    
    public void setFirstName(String firstName) {
	this.firstName = firstName;
    }
    
    public String getFirstName() {
	return this.firstName;
    }

    public void setSecondName(String secondName) {
	this.secondName = secondName;
    }
    
    public String getSecondName() {
	return this.secondName;
    }
 
    public void setMale(Boolean male) {
	this.male = male;
    }
    
    public Boolean getMale() {
	return this.male;
    }
    
    public Integer getQuota() {
        return quota;
    }
}