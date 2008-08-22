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

import org.joing.server.ejb.user.UserManagerLocal;
import java.util.List;
import java.util.Locale;
import org.joing.common.dto.user.Local;
import org.joing.common.dto.user.User;
import org.joing.common.exception.JoingServerUserException;

class UserManagerMock implements UserManagerLocal {

    public User createUser(String sAccount, String sPassword, String sEmail, String sFirstName, String sSecondName, boolean bIsMale, Locale locale, int nQuota) throws JoingServerUserException {
	User user = new User();

	user.setAccount(sAccount);
	user.setEmail(sEmail);
	user.setFirstName(sFirstName);
	user.setMale(bIsMale);
	user.setSecondName(sSecondName);
	user.setTotalSpace(nQuota);
	user.setUsedSpace(0);

	return user;
    }

    public void removeUser(User user) throws JoingServerUserException {
	throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean isValidAccount(String sAccount) {
	return sAccount.matches("[a-z][a-z1-9\\.\\_]{5,31}");
    }

    public boolean isValidPassword(String sPassword) {
	return sPassword.matches(".{6,32}");
    }

    public User getUser(String sSessionId) throws JoingServerUserException {
	throw new UnsupportedOperationException("Not supported yet.");
    }

    public User updateUser(String sSessionId, User user) throws JoingServerUserException {
	throw new UnsupportedOperationException("Not supported yet.");
    }

    public List<Local> getAvailableLocales(String sSessionId) throws JoingServerUserException {
	throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean areLinked( String sSessionId, String sPassword )
    {
        throw new UnsupportedOperationException( "Not supported yet." );
    }
}
