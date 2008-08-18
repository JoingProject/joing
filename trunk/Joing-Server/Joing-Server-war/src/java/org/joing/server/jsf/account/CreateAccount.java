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
