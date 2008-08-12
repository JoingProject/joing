/*
 * UserBridge.java
 *
 * Created on 18 de junio de 2007, 15:50
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.joing.common.clientAPI.runtime;

import java.util.List;
import org.joing.common.dto.user.Local;
import org.joing.common.dto.user.User;
import org.joing.common.exception.JoingServerUserException;

/**
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