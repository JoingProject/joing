/*
 * DeskCanvas.java
 *
 * Created on 4 de octubre de 2007, 0:15
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.joing.common.desktopAPI.container;

import java.awt.Component;
import java.awt.Rectangle;

/**
 *
 * @author Francisco Morero Peyrona
 */
public interface DeskCanvas
{
    public Component add( Component c );
    public void      remove( Component c );
    public void      setBounds( int x, int y, int width, int height );
    public Rectangle getBounds( Rectangle rv );
}