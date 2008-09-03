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

package org.joing.kernel.api.bridge;

import org.joing.common.dto.session.LoginResult;
import org.joing.common.dto.session.SystemInfo;
import org.joing.common.exception.JoingServerSessionException;

/**
 * Interface that describes session related information and actions at 
 * the server side that can be accessed from the client side.
 * 
 * @author Francisco Morero Peyrona
 */
public interface SessionBridge
{
    /**
     * Performs the login.
     * 
     * @param sAccount
     * @param sPassword
     * @return <code>true</code> if login was ok.
     * @throws org.joing.common.exception.JoingServerSessionException
     */
    LoginResult login( String sAccount, String sPassword )
            throws JoingServerSessionException;
    
    /**
     * Peform a logout for current session.
     * <p>
     * Note: This method silently performs the logout and does not report 
     * an exception.    
     */
    void logout();
    
    /**
     * Retreives usefull information insde a data structure
     * @return An instace of data structure <code>SystemInfo</code>
     */
    SystemInfo getSystemInfo()
            throws JoingServerSessionException;
    
    /**
     * Checks if passed password corresponds with passed session ID or not.
     * 
     * @param sPassword The user password
     * @return <code>true</code> if passed password corresponds with passed 
     *         session, <code>false</code> otherwise.
     */
    boolean isValidPassword( String sPassword )
            throws JoingServerSessionException;
    
    /**
     * Gets the current session Id.
     * <p>
     * Every concrete implementation should set this value internally an not let 
     * anyone else to change it.
     * 
     * @return The session ID String.
     */
    public String getSessionId();
}