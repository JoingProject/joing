/*
 * WorkAreaImpl.java
 *
 * Created on 10 de febrero de 2007, 18:34
 *
 * (c) 2006 - Francisco Morero Peyrona
 *
 * License: {license}
 */

package org.joing.pde.desktop.workarea;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Point;
import java.util.List;
import javax.swing.BorderFactory;
import org.joing.desktop.api.Wallpaper;
import org.joing.desktop.api.WorkArea;

/**
 * The area of the desktop where <code>DeskItem</code> instances are shown.
 * 
 * @author Francisco Morero Peyrona
 */
public class WorkAreaImpl extends AbstractWorkArea implements WorkArea
{  // TODO: clase sin terminar
    
    private Wallpaper wallpaper;
    
    //------------------------------------------------------------------------//
    
    public WorkAreaImpl()
    {
        setOpaque( true );
        setBackground( Color.BLUE.darker().darker().darker() );
    }
        
    //------------------------------------------------------------------------//
    // DEFINED IN INTERFACE WORKAREA

    public void addItem( Component comp )
    {
        comp.setBounds( 5,5, 80,45 );
        add( comp, new Integer( 10 ) );/// WorkArea.ITEM_LAYER );
    }
    
    public void removeItem( Component comp )
    {
        remove( comp );
    }
    
    public List<Component> getItems()
    {
        return null;
    }
    
    public void addContainer( Container container )
    {
        add( container, new Integer( 10 ) );   ///
    }
    
    public void removeContainer( Container container )
    {
        remove( container );
    }
    
    public List<Container> getContainers()
    {
        return null;
    }
    
    public Wallpaper getWallpaper()
    {
        return null;
    }
    
    public void setWallpaper( Wallpaper wallpaper )
    {
    }
    
    //------------------------------------------------------------------------//
    
    protected void showPopupMenu( Point p )
    {
        throw new UnsupportedOperationException( "Not supported yet." );
    }
}