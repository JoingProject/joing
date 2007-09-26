/*
 * LauncherImpl.java
 *
 * Created on 9 de septiembre de 2007, 11:59
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.joing.impl.desktop.workarea;

import java.awt.Component;
import java.awt.Image;
import java.awt.Point;
import javax.swing.event.EventListenerList;
import org.joing.api.desktop.workarea.desklet.deskLauncher.Launcher;
import org.joing.api.desktop.workarea.desklet.deskLauncher.LauncherEvent;
import org.joing.api.desktop.workarea.desklet.deskLauncher.LauncherEventListener;

/**
 *
 * @author Francisco Morero Peyrona
 */
public class DefaultLauncher implements Launcher
{    
    private String    sName;
    private String    sDescription;
    private Point     ptLocation;
    private Image     image;
    private Component popup;
    
    private boolean bSelected;
    
    private EventListenerList listenerList;
    
    //------------------------------------------------------------------------//
    
    public DefaultLauncher()
    {
        listenerList = new EventListenerList();
        ptLocation   = new Point( 0, 0 );
    }
    
    public String getName()
    {
        return sName;
    }
    
    public void setName( String sName )
    {
        this.sName = sName;
    }
    
    public String getDescription()
    {
        return sDescription;
    }
    
    public void setDescription( String sDescription )
    {
        this.sDescription = sDescription;
    }
    
    /**
     * Guaranted not to be null
     */
    public Point getLocation()
    {
        return ptLocation;
    }

    public void setLocation( Point pt )
    {
        ptLocation = pt;
    }

    public Component getPopupMenu( )
    {
        return popup;
    }

    public void setPopupMenu( Component popup )
    {
        this.popup = popup;
    }
    
    public Image getImage()
    {
        return image;
    }

    public void setImage( Image image )
    {
        this.image = image;
    }
    
    public boolean isSelected()
    {
        return bSelected;
    }
    
    /** 
     * When a subclass redefines this method, the redefined one should do all its work 
     * and later call <code>super.setSelected( boolean )</code>
     *  
     * @param b  New selected status 
     */
    public void setSelected( boolean b )
    {
        if( b != isSelected() )
        {
            // FIXME: Este método debe lanzar este evento, pero si lo hace, 
            //        se entra en una cinta de Moebius.
            //        LauncherEvent le = new LauncherEvent( this, b );
            bSelected = b;
            ///////////////////////////////////////fireSelectedEvent( le );
        }
    }
    
    /**
     * Has to be redefined by every subclass tha can launch the Launcher.
     */
    public boolean launch()
    {
        return false;
    }
    
    //------------------------------------------------------------------------//
    
    public Launcher clone()
    {
        Launcher clone = new DefaultLauncher();
                 clone.setName( "Copy of "+ getName() );
                 clone.setDescription( getDescription() );
                 clone.setImage( getImage() );
                 
        return clone;
    }
    
    //------------------------------------------------------------------------//
    // EVENTS
    
    public void addLauncherListener( LauncherEventListener ll )
    {
        listenerList.add( LauncherEventListener.class, ll );
    }
    
    public void removeLauncherListener( LauncherEventListener ll ) 
    {
        listenerList.remove( LauncherEventListener.class, ll );
    }
    
    public void fireSelectedEvent( LauncherEvent le )
    {
        Object[] listeners = listenerList.getListenerList();
        
        // Process the listeners last to first, notifying
        for( int n = listeners.length - 2; n >= 0; n -= 2 )
        {
            if( listeners[n] == LauncherEventListener.class )
                ((LauncherEventListener) listeners[n+1]).selectedEvent( le );
        }
    }
    
    public void fireSelectionIncrementalEvent( LauncherEvent le )
    {
        Object[] listeners = listenerList.getListenerList();
        
        // Process the listeners last to first, notifying
        for( int n = listeners.length - 2; n >= 0; n -= 2 )
        {
            if( listeners[n] == LauncherEventListener.class )
                ((LauncherEventListener) listeners[n+1]).selectionIncrementalEvent( le );
        }
    }
    
    public void fireLaunchedEvent( LauncherEvent le )
    {
        Object[] listeners = listenerList.getListenerList();
        
        // Process the listeners last to first, notifying
        for( int n = listeners.length - 2; n >= 0; n -= 2 )
        {
            if( listeners[n] == LauncherEventListener.class )
                ((LauncherEventListener) listeners[n+1]).launchedEvent( le );
        }
    }
}