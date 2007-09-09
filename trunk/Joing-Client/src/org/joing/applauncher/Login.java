/*
 * Login.java
 *
 * Created on 9 de septiembre de 2007, 20:43
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.joing.applauncher;

import javax.swing.JFrame;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

/**
 *
 * @author Francisco Morero Peyrona
 */
public class Login extends JFrame
{
    private JTextField     txtAccount  = new JTextField();
    private JPasswordField txtPassword = new JPasswordField();
    
    /** Creates a new instance of Login */
    public Login()
    {
        super( "Join'g :: Loging" );
        initGUI();
    }
    
    //------------------------------------------------------------------------//
    
    private void initGUI()
    {
        setLayout( new SpringLayout() );
        
    }
}