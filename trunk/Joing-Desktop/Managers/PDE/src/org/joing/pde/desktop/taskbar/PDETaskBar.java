/*
 * Copyright (C) 2007, 2008 Join'g Team Members. All Rights Reserved.
 * Join'g Team Members are listed at project's home page. By the time of 
 * writting this at: https://joing.dev.java.net/servlets/ProjectMemberList.
 *
 * This file is part of Join'g project: www.joing.org
 *
 * GNU Classpath is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the free
 * Software Foundation; either version 3, or (at your option) any later version.
 * 
 * GNU Classpath is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * GNU Classpath; see the file COPYING.  If not, write to the Free Software 
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */
package org.joing.pde.desktop.taskbar;

import java.awt.Component;
import java.awt.Dimension;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.border.EtchedBorder;
import org.joing.common.desktopAPI.DeskComponent;
import org.joing.common.desktopAPI.DeskContainer;
import org.joing.common.desktopAPI.taskbar.TaskBarListener;
import org.joing.common.desktopAPI.taskbar.TaskBarComponent;
import org.joing.pde.desktop.taskbar.clock.ClockDigital;
import org.joing.pde.desktop.taskbar.frameslist.FrameList;
import org.joing.pde.desktop.taskbar.start.StartButton;
import org.joing.pde.desktop.taskbar.systray.SysTray;
import org.joing.pde.media.PDEColorSchema;
import org.joing.common.desktopAPI.taskbar.TaskBar;
import org.joing.common.desktopAPI.taskbar.TaskBarPanel;
import org.joing.pde.desktop.taskbar.waSwitcher.WorkAreaSwitcher;
import org.joing.pde.swing.EventListenerList;

/**
 * TaskBar can hold JComponents or instances of TaskBarComponent
 * 
 * @author Francisco Morero Peyrona
 */
public class PDETaskBar extends JPanel implements TaskBar
{
    private static final String DONT_USE_ME = "Do not use me";
    
    private TaskBar.Orientation orientation;
    private boolean             bAutoHide;
    
    private EventListenerList listenerList;
    
    //------------------------------------------------------------------------//
    
    /** Creates a new instance of TaskBar */
    public PDETaskBar()
    {
        orientation  = TaskBar.Orientation.BOTTOM;
        bAutoHide    = false;
        listenerList = new EventListenerList();
                
        setOpaque( true );
        setLayout( new BoxLayout( this, BoxLayout.X_AXIS ) );
        setBackground( PDEColorSchema.getInstance().getTaskBarBackground() );
        setBorder( new EtchedBorder( EtchedBorder.LOWERED ) );
        setComponentPopupMenu( createPopupMenu() );
        
        setMinimumSize( new Dimension( 180, 14 ) );
        setMaximumSize( new Dimension( Integer.MAX_VALUE, 76 ) );
        setPreferredSize( new Dimension( Integer.MAX_VALUE, 36 ) );
    }
    
    public void createDefaultComponents()
    {   
        add( (TaskBarComponent) new StartButton() );
        add( (TaskBarPanel)     new FrameList() );
        add( (TaskBarComponent) new WorkAreaSwitcher() );
        add( (TaskBarPanel)     new SysTray() );
        
        getSysTray().add( (TaskBarComponent) new ClockDigital() );
    }
    
    /**
     * Returns the SysTray in this Taskbar.
     * <p>
     * If there is more than one SysTray (it shoud not but could happen), the
     * first one is returned.
     */
    public SysTray getSysTray()
    {
        // TODO: hacer la búsqueda recursiva por si está dentro de otro comp.
        Component[] aComp = getComponents();
        
        for( int n = 0; n < aComp.length; n++ )
        {
            if( aComp[n] instanceof SysTray )
                return (SysTray) aComp[n];
            /*else
                return getSysTray( aComp[n] );*/
        }
        
        return null;  
    }
    
    //------------------------------------------------------------------------//
    // TaskBar interface implementation methods
    
    
    public void add( TaskBarPanel tbp )
    {
        super.add( (Component) tbp );
        validate();
        fireComponentAdded( tbp );
    }

    public void add( TaskBarPanel tbp, int x, int y )
    {
        add( tbp );   // TODO: mejorarlo
    }

    public void add( TaskBarComponent tbo )
    {
        super.add( (Component) tbo );
        validate();
        fireComponentAdded( tbo );
    }

    public void add( TaskBarComponent tbo, int x, int y )
    {
        add( tbo );   // TODO: mejorarlo con la x,y
    }

    public void remove( TaskBarPanel tbp )
    {
        super.remove( (Component) tbp );
        validate();
        fireComponentRemoved( tbp );
    }
    
    public void remove( TaskBarComponent dc )
    {
        super.remove( (Component) dc );
        validate();
        fireComponentRemoved( dc );
    }

    public void close()
    {
        // FIXME: hacerlo
    }
    
    // Just to avoid accidental use of them  ---------------------------------------------------------
    public Component add( Component c )             { throw new IllegalAccessError(DONT_USE_ME); }
    public Component add( Component c, int n )      { throw new IllegalAccessError(DONT_USE_ME); }
    public Component add( String s, Component c )   { throw new IllegalAccessError(DONT_USE_ME); }
    public void add( Component c, Object o )        { throw new IllegalAccessError(DONT_USE_ME); }
    public void add( Component c, Object o, int n ) { throw new IllegalAccessError(DONT_USE_ME); }
    public void remove( Component c )               { throw new IllegalAccessError(DONT_USE_ME); }
    //------------------------------------------------------------------------------------------------

    public boolean isAutoHide()
    {
        return bAutoHide;
    }

    public void setAutoHide( boolean bAutoHide )
    {
        this.bAutoHide = bAutoHide;
    }
    
    public TaskBar.Orientation getOrientation()
    {
        return orientation;
    }

    public void setOrientation( TaskBar.Orientation orientation )
    {
        this.orientation = orientation;
    }

    public void addTaskBarListener( TaskBarListener tbl )
    {
        listenerList.add( TaskBarListener.class, tbl );
    }

    public void removeTaskBarListener( TaskBarListener tbl )
    {
        listenerList.remove( tbl );
    }
    
    //------------------------------------------------------------------------//
    
    protected void fireComponentAdded( DeskComponent dc )
    {
        fire( dc, true );
    }
    
    protected void fireComponentRemoved( DeskComponent dc )
    {
        fire( dc, false );
    }
    
    protected void fireComponentAdded( DeskContainer dc )
    {
        fire( dc, true );
    }
    
    protected void fireComponentRemoved( DeskContainer dc )
    {
        fire( dc, false );
    }
    
    //------------------------------------------------------------------------//
    
    private void fire( Object obj, boolean bAdded )
    {
        TaskBarListener[] al = listenerList.getListeners( TaskBarListener.class );
        
        // Process the al last to first, notifying
        for( int n = al.length - 1; n >= 0; n-- )
        {
            if( bAdded )
            {
                if( obj instanceof DeskComponent )
                    al[n].componentAdded( (DeskComponent) obj );
                else
                    al[n].componentAdded( (DeskContainer) obj );
            }
            else
            {
                if( obj instanceof DeskComponent )
                    al[n].componentRemoved( (DeskComponent) obj );
                else
                    al[n].componentRemoved( (DeskContainer) obj );
            }
        }
    }
       
    private JPopupMenu createPopupMenu()
    {
        JPopupMenu popup = new JPopupMenu();
        
        // TODO: hacerlo
        
        return popup;
    }
}