/*
 * WDesktopComponentChangeEvent.java
 *
 * Created on 10 de febrero de 2007, 19:24
 *
 * (c) 2006 - Francisco Morero Peyrona
 *
 * License: {license}
 */

package org.joing.api.desktop.workarea.desklet.deskLauncher;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.util.EventObject;
import org.joing.api.desktop.workarea.desklet.*;

/**
 * Events that can occur over a Launcher.
 * 
 * Sólo clases de este paquete pueden construir instancias de esta clase.  
 * 
 * @author Francisco Morero Peyrona
 */
public class LauncherEvent extends EventObject
{
    private boolean    bSelected;   // For selection events
    private MouseEvent mouse;       // If it was originated by a mouse, the mouse event
    private Point      ptLocation;  // New Launcher's location (to be moved)
    
    public LauncherEvent( Object source )
    {
        super( source );
    }
    
    public LauncherEvent( Object source, boolean bSelected )
    {
        super( source );
        this.bSelected = bSelected;
    }
    
    public LauncherEvent( Object source, MouseEvent me )
    {
        this( source, me, null );
    }
    
    public LauncherEvent( Object source, MouseEvent me, Point ptLocation )
    {
        super( source );
        this.mouse      = me;
        this.ptLocation = ptLocation;
    }
    
    public boolean getNewSelectionStatus()
    {
        return bSelected;
    }
    
    /** 
     * El evento de ratón q lo originó o <code>null</code> si no fue un evento de ratón
     * 
     * @return El evento de ratón q lo originó o <code>null</code> si no fue un evento de ratón
     */ 
    public MouseEvent getMouseEvent()
    {
        return mouse;
    }
    
    public Point getNewLocation()
    {
        return ptLocation;
    }
}