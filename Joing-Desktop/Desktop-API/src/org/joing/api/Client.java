/*
 * Client.java
 *
 * Created on 9 de septiembre de 2007, 9:41
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.joing.api;

import org.joing.api.desktop.Desktop;

/**
 *
 * @author Francisco Morero Peyrona
 */
public interface Client
{
    // Showing in different ways
    public void showInFrame();
    public void showInFullScreen();
    
    // Others
    public void lock();
    public void close();
    public Desktop getDesktop();
}