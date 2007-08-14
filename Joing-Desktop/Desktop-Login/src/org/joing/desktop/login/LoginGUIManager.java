/*
 * LoginManager.java
 *
 * Created on 30 de junio de 2007, 06:58 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.joing.desktop.login;

import java.awt.Component;
import javax.swing.JPanel;

/**
 *
 * @author Mario Serrano
 */
public class LoginGUIManager {
    
    private static LoginPanel loginPanel;
    /** Creates a new instance of LoginManager */
    private LoginGUIManager() {
    }
    
    public static void setLoginListener(LoginListener listener){
        if(loginPanel!=null){
            loginPanel.setLoginListener(listener);
        }
    }
    
    public static JPanel createWelcomePanel(){
        WelcomePanel panel = new WelcomePanel();
        loginPanel = panel.getLoginPanel();
        return panel;
    }
    
    
}
