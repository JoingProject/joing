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

import ejb.Constant;
import ejb.session.SessionManagerLocal;
import org.joing.common.dto.session.LoginResult;
import org.joing.common.exception.JoingServerSessionException;

class SessionManagerMock implements SessionManagerLocal {

    public boolean isAccountAvailable(String sAccount) throws JoingServerSessionException {

	return !("not_available@" + Constant.getSystemName()).equals(sAccount);
    }

    public String getUserAccount(String sSessionId) throws JoingServerSessionException {
	throw new UnsupportedOperationException("Not supported yet.");
    }

    public LoginResult login(String sAccount, String sPassword) throws JoingServerSessionException {
	throw new UnsupportedOperationException("Not supported yet.");
    }

    public void logout(String sSessionId) throws JoingServerSessionException {
	throw new UnsupportedOperationException("Not supported yet.");
    }

    public String composeAccount(String sAccount) {
	return String.format("%s@%s", sAccount, Constant.getSystemName());
    }
}
