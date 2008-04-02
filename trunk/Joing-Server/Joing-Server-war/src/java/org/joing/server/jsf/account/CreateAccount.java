package org.joing.server.jsf.account;

import org.joing.server.jsf.ManagedBean;

/**
 *
 * @author Fernando
 */
public class CreateAccount extends ManagedBean {

    private CreateAccountForm createAccount() {
	return el("CreateAccountForm");
    }
    
    private String account = null;
    private String email = null;
    
    public String gotoForm() {
	
	CreateAccountForm ca = createAccount();
	
	ca.setAccount(account);
	ca.setEmail(email);
	
	ca.setConfirmPassword(null);
	ca.setFirstName(null);
	ca.setMale(null);
	ca.setPassword(null);
	ca.setSecondName(null);
	
	return "createaccount";

    }

    
    public void setAccount(String account) {
	this.account = account;
    }
    public String getAccount() {
	return account;
    }
    public void setEmail(String email) {
	this.email = email;
    }
    public String getEmail() {
	return email;
    }
    
}
