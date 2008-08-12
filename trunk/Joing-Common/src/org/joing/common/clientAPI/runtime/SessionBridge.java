/*
 * SessionBridge.java
 *
 * Created on 18 de junio de 2007, 13:32
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.joing.common.clientAPI.runtime;

import org.joing.common.dto.session.LoginResult;
import org.joing.common.exception.JoingServerSessionException;

/**
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
     * Gets the current session Id.
     * <p>
     * Every concrete implementation should set this value internally an not let 
     * anyone else to change it.
     * 
     * @return The session ID String.
     */
    public String getSessionId();
}