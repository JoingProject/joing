/*
 * SessionBridge.java
 *
 * Created on 18 de junio de 2007, 13:32
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.joing.runtime.bridge2server;

import ejb.session.LoginResult;

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
}