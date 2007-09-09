/*
 * WorkArea.java
 *
 * Created on 25 de abril de 2007, 11:55 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.joing.desktop.api;

import java.awt.Component;
import java.awt.Container;
import java.net.URL;
import java.util.List;

/**
 *
 * @author mario
 */
public interface WorkArea
{    
    public static final Integer WALLPAPER_LAYER     = new Integer(-10 );
    public static final Integer CANVAS_LAYER        = new Integer(  0 );
    public static final Integer ITEM_LAYER          = new Integer( 10 );
    public static final Integer DESKLET_LAYER       = new Integer( 20 );
    public static final Integer APPLICATION_LAYER   = new Integer( 30 );
    public static final Integer DIALOG_LAYER        = new Integer( 40 );
    public static final Integer GLASS_LAYER         = new Integer( 50 );
    
    //------------------------------------------------------------------------//
    
    public void addItem( Component comp );
    public void removeItem( Component comp );
    public List<Component> getItems();
    
    public void addContainer( Container container );
    public void removeContainer( Container container );
    public List<Container> getContainers();
    
    public Wallpaper getWallpaper();
    public void setWallpaper( Wallpaper wallpaper );
}