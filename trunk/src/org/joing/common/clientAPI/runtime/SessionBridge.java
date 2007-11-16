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

/**
 *
 * @author fmorero
 */
public interface SessionBridge
{
    /**
     *
     * @param sAccount
     * @param sPassword
     * @return <code>true</code> if login was ok.
     */
    LoginResult login( String sAccount, String sPassword );
    
    void logout();
    
    /**
     * Gets the current session Id. The concrete implementation should
     * set this value internally an not let anyone else to change it.
     * @return The session ID String.
     */
    public String getSessionId();
}