package org.joing.server.jsf;

import ejb.user.UserManagerLocal;
import java.util.logging.Level;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import org.joing.common.Constant;
import org.joing.common.exception.JoingServerUserException;

/**
 *
 * @author Fernando José Ortigosa
 */
public class CreateAccount {

    private String username = null;
    private String password = null;
    private String email = null;
    private String confirmPassword = null;
    
    private @EJB UserManagerLocal userManager;
    
    /**
     * <p>This method is invoked by the JSF framework (from the create-account 
     * form in index.jsp.</p>
     * 
     * <p>It should test the account info provided by the property setters and
     * try to create a new account in the system</p>
     */
    public String createNewAccount() {
	
	FacesContext ctx = FacesContext.getCurrentInstance();
	
	boolean ok = true;
	
	if(username == null || "".equals(username.trim())) {
	    ok = false;
	    ctx.addMessage(null, messageErrorMissing("user name"));
	}

	if(email == null || "".equals(email.trim())) {
	    ok = false;
	    ctx.addMessage(null, messageErrorMissing("email address"));
	}

	if(password == null || "".equals(password.trim())) {
	    ok = false;
	    ctx.addMessage(null, messageErrorMissing("password"));
	}

	if(confirmPassword == null || "".equals(confirmPassword.trim())) {
	    ok = false;
	    ctx.addMessage(null, messageErrorMissing("password confirmation"));
	}
	
	if(!ok) {
	    return null;
	}

	if(!password.equals(confirmPassword)) {
	    ok = false;
	    ctx.addMessage(null, new FacesMessage(
		FacesMessage.SEVERITY_ERROR,
		"Passwords do not match",
		"The password and password confirmation did not match, they " +
		"must match in order to create a new account"));
	}
	
	if(!ok) {
	    return null;
	}

/*
    TODO We need to choose the default values for this form or add them.
    In my oppinion the user quota one is very important
*/
	try {
	    userManager.createUser(
		username,
		password,
		email,
		username,
		username,
		true,
		ctx.getViewRoot().getLocale(),
		0);

	} catch(JoingServerUserException ex) {

	    ok = false;
	    
	    ctx.addMessage(null, new FacesMessage(
		    FacesMessage.SEVERITY_ERROR,
		    ex.getMessage(),
		    ex.getMessage()));

	} catch (EJBException ex) {
	    
	    ok = false;
	    
	    Constant.getLogger().log(Level.SEVERE,
		    "Unable to create new user account", ex);
	    
	    String msg = "An error occurred while processing your" +
		    " request, the server couldn't create a new account.";
	    
	    ctx.addMessage(null, new FacesMessage(
		    FacesMessage.SEVERITY_FATAL, msg, msg));
	}
	
	if(ok) {
	    //Account successfully created
	    ctx.addMessage(null, new FacesMessage(
		    FacesMessage.SEVERITY_INFO,
		    "User account successfully created",
		    "User account successfully created"));
	}
	
	return null;
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

}