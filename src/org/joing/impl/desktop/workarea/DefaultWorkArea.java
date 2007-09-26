/*
 * DefaultWorkArea.java
 * 
 * Created on 11-sep-2007, 12:09:27
 * 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.joing.impl.desktop.workarea;

import java.awt.Component;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import javax.swing.event.EventListenerList;
import org.joing.api.desktop.workarea.Wallpaper;
import org.joing.api.desktop.workarea.WorkArea;
import org.joing.api.desktop.workarea.WorkAreaListener;
import org.joing.impl.desktop.Selectable;

/**
 *
 * @author Francisco Morero Peyrona
 */
public class DefaultWorkArea implements WorkArea
{
    private String sName;
    private Vector<Component>  vComponents;
    private Wallpaper wallpaper;
    protected EventListenerList listenerList;
    
    //------------------------------------------------------------------------//
    
    public DefaultWorkArea()
    {
        vComponents  = new Vector<Component>();
        listenerList = new EventListenerList();
    }
    
    //------------------------------------------------------------------------//
    
    public String getName()
    {
        return sName;
    }
    
    public void setName(String sName)
    {
        this.sName = sName;
    }

    public void close()
    {
        // TODO: se podría hacer un close de todos los elementos
    }
    
    //------------------------------------------------------------------------//
    // LAUNCHERS
    
    public Component add( Component component  )
    {
        // The same ( == ) launcher can't be added twice
        if( ! vComponents.contains( component  ) )
        {
            vComponents.add( component  );
            fireComponentAdded( component );
        }
        
        return component;
    }
    
    public void remove( Component component )
    {
        if( vComponents.contains( component ) )
        {
            vComponents.remove( component );
            fireComponentRemoved( component );
        }
    }
    
    public List<Component> getOfType( Class clazz )
    {
        ArrayList<Component> list = new ArrayList<Component>();
        
        for( Component comp : vComponents )
        {
            if( clazz.isInstance( comp ) )
                list.add( comp );
        }
        
        return list;
    }
    
    public List<Component> getSelected( Class clazz, boolean bSelected )
    {
        List<Component> list = getOfType( clazz );
        
        for( Component comp : list )
        {
            if( comp instanceof Selectable && ((Selectable) comp).isSelected() == bSelected )
                list.add( comp );
        }
        
        return list;
    }
    
    /**
     * Selects objects in desktop that match passed class.
     * <p>
     * Clazz must be implement interface <code>Selectable</code>
     *  
     * @param clazz The Class
     */
    public void setSelected( Class clazz, boolean bSelected )
    {
        List<Component> list = getSelected( clazz, false );   // false, becasue the others are already selected
        
        for( Component comp : list )
            ((Selectable) comp).setSelected( bSelected );
    }
    
    //------------------------------------------------------------------------//
    // BACKGROUND
    
    public Wallpaper getWallpaper()
    {
        return wallpaper;
    }
    
    public void setWallpaper( Wallpaper wallpaper )
    {
        // TODO: hacerlo
        fireWallpaperChanged( wallpaper );
    }
    
    //------------------------------------------------------------------------//
    // EVENTS
    
    public void addWorkAreaListener( WorkAreaListener dl )
    {
        listenerList.add( WorkAreaListener.class, dl );
    }

    public void removeWorkAreaListener( WorkAreaListener wal ) 
    {
        listenerList.remove( WorkAreaListener.class, wal );
    }
    
    protected void fireComponentAdded( Component l )
    {
        Object[] listeners = listenerList.getListenerList();
        
        // Process the listeners last to first, notifying
        for( int n = listeners.length - 2; n >= 0; n -= 2 )
        {
            if( listeners[n] == WorkAreaListener.class )
                ((WorkAreaListener) listeners[n+1]).componentAdded( l );
        }
    }
    
    protected void fireComponentRemoved( Component l )
    {
        Object[] listeners = listenerList.getListenerList();
        
        // Process the listeners last to first, notifying
        for( int n = listeners.length - 2; n >= 0; n -= 2 )
        {
            if( listeners[n] == WorkAreaListener.class )
                ((WorkAreaListener) listeners[n+1]).componentRemoved( l );
        }
    }
    
    public void fireWallpaperChanged( Wallpaper wpNew )
    {
        Object[] listeners = listenerList.getListenerList();
        
        // Process the listeners last to first, notifying
        for( int n = listeners.length - 2; n >= 0; n -= 2 )
        {
            if( listeners[n] == WorkAreaListener.class )
                ((WorkAreaListener) listeners[n+1]).wallpaperChanged( wpNew );
        }
    }
}