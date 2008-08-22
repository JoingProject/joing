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

import javax.ejb.Local;
import org.joing.common.exception.JoingServerSessionException;

/**
 * This is the business interface for <code>SessionManager</code> enterprise
 * bean.
 *
 * @author Francisco Morero Peyrona
 */
@Local
public interface SessionManagerLocal extends SessionManagerRemote
{
    /**
     * Checks if passed sAccount is already in use.
     * This is a convenience method to be used (among others) by the "Create new
     * user" Servlet.
     *
     * @param sAccount Account to be checked
     * @return <code>true</code> if passed account is available.
     */
    boolean isAccountAvailable( String sAccount )
            throws JoingServerSessionException;
    
    /**
     * Return the <code>User.Account</code> based on a Session ID.
     * <p>
     * When only the <code>Account</code> is needed, this method is preferred 
     * over <code>getUser( sSessionId ).getAccount()</code> invocation.
     * <p>
     * Note: This is just a convenience method to be used only locally.
     *
     * @param  sSessionId A SessionId
     * @return String <code>User.Account</code> (the User ID) or 
     *                <code>null</code> if SessionId does not exists.
     */
    String getUserAccount( String sSessionId );
    
    /**
     * If passed account is "einstein", on system "joing.peyrona.com", this 
     * method will return "einsteis@joing.peyrona.com".
     * 
     * @param sAccount Account to be composed.
     * @return The account plus the system name.
     */
    String composeAccount( String sAccount );
}