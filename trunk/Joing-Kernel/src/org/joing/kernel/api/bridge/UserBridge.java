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

import java.util.List;
import org.joing.common.dto.user.Local;
import org.joing.common.dto.user.User;
import org.joing.common.exception.JoingServerUserException;

/**
 * Interface that describes session related information and actions at 
 * the server side that can be accessed from the client side.
 * 
 * @author Francisco Morero Peyrona
 */
public interface UserBridge
{
    /**
     * Return a <code>User</code> based on its Session ID.
     *
     * @return User An instance of User DTO class, or <code>null</code> if 
     *         sent sSessionId was not found.
     * @throws JoingServerUserException if any prerequisite was not satisfied or 
     *         a wrapped third-party exception if something went wrong.
     */
    User getUser()
            throws JoingServerUserException;
    
    /**
     * Update user information.
     *
     * @param user The user to be updated
     * @return The user after update process
     * @throws JoingServerUserException if any prerequisite was not satisfied or 
     *         a wrapped third-party exception if something went wrong.
     */
    User updateUser( User user )
            throws JoingServerUserException;
    
    /**
     * Returns all available locales (can be used by users to select their one).
     *
     * @param  sSessionId An existing SessionId
     * @return All available locales or null if something goes wrong.
     * @throws JoingServerUserException if any prerequisite was not satisfied or 
     *         a wrapped third-party exception if something went wrong.
     */
    List<Local> getAvailableLocales()
            throws JoingServerUserException;
}