/*
 * Desklet.java
 *
 * Created on 18 de septiembre de 2007, 0:13
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.joing.api.desktop.workarea.desklet;

import java.awt.Component;
import java.awt.Point;

/**
 *
 * @author Francisco Morero Peyrona
 */
public interface Desklet
{
    public String getName();
    public void   setName( String name );
    public String getDescription();
    public void   setDescription( String name );
    
    public Point  getLocation();
    public void   setLocation( Point pt );    
}