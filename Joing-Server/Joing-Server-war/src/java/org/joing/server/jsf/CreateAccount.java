package org.joing.server.jsf;

import ejb.session.SessionManagerLocal;
import ejb.user.UserManagerLocal;
import java.util.logging.Level;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import org.joing.common.Constant;
import org.joing.common.exception.JoingServerException;

/**
 *
 * @author Fernando José Ortigosa
 */
public class CreateAccount {
    
    private boolean creatingAccount = false;
    private boolean accountCreated = false;

    private String username = null;
    private String password = null;
    private String email = null;
    private String confirmPassword = null;
    private String firstName = null;
    private String secondName = null;
    private Boolean male = null;


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
	
//TODO The account name is calculated the same as in other methods, it could be
// safer to calculate it in one method instead and call it from everywhere where
// you need a full account name from a simple account name.

	String account = getAccount(name);
	
	if(ok && !sessionManager.isAccountAvailable(account)) {

	    ctx.addMessage(null, new FacesMessage(
		FacesMessage.SEVERITY_ERROR,
		"Account name not available",
		"There is already a different account with that name, enter "+
		"a different account name and try again."));
	
	}
	
	return ok;
    }
    
    private String getAccount(String name) {
	return name + "@" + Constant.getSystemName();
    }
    
    public String getAccount() {
	return getAccount(username);
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
	missing = testEmpty(ctx, password, "password") || missing;
	missing = testEmpty(ctx, male, "gender") || missing;
	missing = testEmpty(ctx, confirmPassword, "password confirmation") || missing;;
		
	boolean ok = !missing;
	
	ok = ok && testUsername(ctx, username);
	

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
    
    public String testUsername() {
	testUsername(FacesContext.getCurrentInstance(), username);
	return null;
    }
    
    public String newAccountForm() {
	
	FacesContext ctx = FacesContext.getCurrentInstance();

	testUsername(ctx, username);
	testEmpty(ctx, email, "email address");
	
	creatingAccount = true;
	
	return "createaccount";
    }
    
    public boolean isCreatingAccount() {
	return creatingAccount;
    }
    
    public boolean isAccountCreated() {
	return accountCreated;
    }
    
    public String reset() {
	
	confirmPassword = null;
	email = null;
	firstName = null;
	male = null;
	password = null;
	secondName = null;
	username = null;
	
	creatingAccount = false;
	accountCreated = false;
	
	return "home";
    }
    
/*
    Avisar de que se está en pruebas y que las cuentas se purgarán  cuando
    dejen de estar en pruebas
*/
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
		userManager.createUser(username, password, email, firstName,
		    secondName, male.booleanValue(), ctx.getViewRoot().getLocale(),
		    10);// 10 Megas (ponerlo en la web sólo para informar)
		
		ok = true;
		
		reset();

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
	String detail = null;
	
	if(itemName.matches("(?i)[aeiouy].*")) {
	    detail = String.format(
		"You must provide an %s in order to create a new account",
		itemName);
	} else {
	    detail = String.format(
		"You must provide a %s in order to create a new account",
		itemName);
	}
	
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
    
}