/*
 * DeskWidget.java
 *
 * Created on 18 de septiembre de 2007, 0:13
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.joing.common.desktopAPI.deskwidget;

import java.awt.Point;
import org.joing.common.desktopAPI.container.DeskCanvas;

/**
 * The base class for a kind of desktop gadgets.
 * 
 * @author Francisco Morero Peyrona
 */
public interface DeskWidget extends DeskCanvas
{
    public String getName();
    public void   setName( String name );
    public String getDescription();
    public void   setDescription( String name );
    
    public Point  getLocation();
    public void   setLocation( Point pt );    
}