/*
 * DeskWidget.java
 *
 * Created on 18 de septiembre de 2007, 0:13
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.joing.api.desktop.workarea.deskwidget;

import java.awt.Point;

/**
 * The base class for a kind of desktop gadgets.
 * 
 * @author Francisco Morero Peyrona
 */
public interface DeskWidget
{
    public String getName();
    public void   setName( String name );
    public String getDescription();
    public void   setDescription( String name );
    
    public Point  getLocation();
    public void   setLocation( Point pt );    
}