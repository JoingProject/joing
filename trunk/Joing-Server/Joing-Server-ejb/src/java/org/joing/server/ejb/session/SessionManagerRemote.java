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
package org.joing.server.ejb.session;

import javax.ejb.Remote;
import org.joing.common.dto.session.LoginResult;
import org.joing.common.exception.JoingServerSessionException;

/**
 * This is the business interface for <code>SessionManager</code> enterprise
 * bean.
 *
 * @author Francisco Morero Peyrona
 */
@Remote
public interface SessionManagerRemote
{
    /**
     * Performs the login.
     *
     * @param  sAccount  The user account (user ID, normally his/her name)
     * @param  sPassword The user password
     * @return String A valid new SessionId that will be used from now and on
     *                meanwhile the session is alive. Or <code>null</null> if the
     *                Account and/or Password were invalid.
     * @throws JoingServerSessionException If something goes wrong.
     */
    LoginResult login( String sAccount, String sPassword )
                throws JoingServerSessionException;
    
    /**
     * Peform a logout for an existing session.
     * <p>
     * Note: This method silently performs the logout and does not report 
     * an exception to client.
     * 
     * @param sSessionId An existing Session ID.
     */
    void logout( String sSessionId );
    
    /**
     * Check if passed password corresponds with passed session ID or not.
     * 
     * @param sSessionId An existing Session ID.
     * @param sPassword The user password
     * @return <code>true</code> if passed password corresponds with passed 
     *         session, <code>false</code> otherwise.
     * @throws JoingServerSessionException If something goes wrong.
     */
    boolean isValidPassword( String sSessionId, String sPassword )
            throws JoingServerSessionException;
}