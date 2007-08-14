/*
 * LoginController.java
 *
 * Created on 30 de junio de 2007, 06:53 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.joing.desktop.login;

/**
 *
 * @author Mario Serrano
 */
public interface LoginListener {    
    
    public void onLogin(String username, String password);
    
}
