package org.joing.server.jsf;

import ejb.session.SessionManagerLocal;
import ejb.user.UserManagerLocal;
import java.util.logging.Level;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import org.joing.common.Constant;
import org.joing.common.dto.user.User;
import org.joing.common.exception.JoingServerException;

/**
 *
 * @author Fernando José Ortigosa
 */
public class CreateAccount {

    private String newUsername = null;

    private String newEmail = null;
    
    private String username = null;
    private String password = null;
    private String email = null;
    private String confirmPassword = null;
    private String firstName = null;
    private String secondName = null;
    private Boolean male = null;
    
    //TODO make it configurable
    final private Integer quota = Integer.valueOf(10);

    private @EJB UserManagerLocal userManager;
    private @EJB SessionManagerLocal sessionManager;
 
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
    
    private boolean testUsername(FacesContext ctx, String name) {
	
	boolean ok = !testEmpty(ctx, name, "account name");
	
	if(ok && !userManager.isValidAccount(name)) {
	    
	    ok = false;
	    
//TODO This message will not be valid if UserManagerBean.isValidAccount changes
	    
	    ctx.addMessage(null, new FacesMessage(
		FacesMessage.SEVERITY_ERROR,
		"Invalid account name format",
		"Account names must be between 6 and 32 characters long and "+
		"can only contain lowercase letters, numbers, dots and "+
		"underscores (low line)"));
	}
	
	String account = sessionManager.composeAccount(name);
	
	if(ok && !sessionManager.isAccountAvailable(account)) {

	    ok = false;
	    
	    ctx.addMessage(null, new FacesMessage(
		FacesMessage.SEVERITY_ERROR,
		"Account name not available",
		"There is already a different account with that name, enter "+
		"a different account name and try again."));
	
	}
	
	return ok;
    }
    
    public String getAccount() {
	return sessionManager.composeAccount(username);
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
	missing = testEmpty(ctx, confirmPassword, "password confirmation") || missing;;
		
	boolean ok = !missing;
	
	ok = testUsername(ctx, username) && ok;
	

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
    
    public String newAccountForm() {
	
	FacesContext ctx = FacesContext.getCurrentInstance();
	
	username = newUsername;
	email = newEmail;

	newUsername = null;
	newEmail = null;

	password = null;
	confirmPassword = null;
	firstName = null;
	secondName = null;
	male = null;
	
	testUsername(ctx, username);
	testEmpty(ctx, email, "email address");
	
	return "createaccount";
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
		User user =  userManager.createUser(username, password, email,
			firstName, secondName, male.booleanValue(),
			ctx.getViewRoot().getLocale(),
		    10);// 10 Megas (ponerlo en la web sólo para informar)
		
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
	String detail = String.format(
	    "You need to enter a valid %s in order to create a new account",
	    itemName);

	return new FacesMessage(FacesMessage.SEVERITY_ERROR, summary, detail);

    }
    
    public void setUsername(String username) {
	this.username = username;
    }

    public String getUsername() {
	return username;
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
 
    public String getNewEmail() {
	return newEmail;
    }

    public void setNewEmail(String newEmail) {
	this.newEmail = newEmail;
    }

    public String getNewUsername() {
	return newUsername;
    }

    public void setNewUsername(String newUsername) {
	this.newUsername = newUsername;
    }
    
    public Integer getQuota() {
	return quota;
    }
}