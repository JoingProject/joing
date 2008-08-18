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
package ejb.user;

import java.util.List;
import javax.ejb.Remote;
import org.joing.common.dto.user.Local;
import org.joing.common.dto.user.User;
import org.joing.common.exception.JoingServerUserException;

/**
 * This is the business interface for UserManager enterprise bean.
 */
@Remote
public interface UserManagerRemote
{
    /**
     * Return a <code>User</code> based on its Session ID.
     *
     * Note: This convenience method to be used locally as well as remotely.
     *
     * @param  sSessionId An existing SessionId
     * @return User A DTO representing the UserEntity that holds this Session,
     *         or <code>null</code> if sSessionId was not found.
     * @throws JoingServerUserException if any prerequisite was not satisfied or 
     *         a wrapped third-party exception if something went wrong.
     */
    User getUser( String sSessionId )
         throws JoingServerUserException;
    
    /**
     * Update user information.
     *
     * @param sSessionId An existing SessionId
     * @param user The user to be updated
     * @return The user after update process
     * @throws JoingServerUserException if any prerequisite was not satisfied or 
     *         a wrapped third-party exception if something went wrong.
     */
    User updateUser( String sSessionId, User user )
         throws JoingServerUserException;
    
    /**
     * Returns all available locales (can be used by users to select their one).
     *
     * @param  sSessionId An existing SessionId
     * @return All available locales or null if something goes wrong.
     * @throws JoingServerUserException if any prerequisite was not satisfied or 
     *         a wrapped third-party exception if something went wrong.
     */
    List<Local> getAvailableLocales( String sSessionId )
                throws JoingServerUserException;
    
    /**
     * Validates the password by cheking that passed password corresponds to 
     * passed session identifier.
     * 
     * @param SessionId to be checked
     * @param Password to be checked
     * @return <code>true</code> if both are linked.
     */
    boolean areLinked( String sSessionId, String sPassword );
}