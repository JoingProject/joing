/*
 * Client.java
 *
 * Created on 9 de septiembre de 2007, 9:41
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.joing;

import java.applet.Applet;

/**
 *
 * @author Francisco Morero Peyrona
 */
public interface Client
{
    public Applet getApplet();
    
    // Showing in different ways
    public void showInFrame();
    public void showInFullScreen();
}