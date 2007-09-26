/*
 * DefaultDesktop.java
 * 
 * Created on 11-sep-2007, 9:04:24
 * 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.joing.impl.desktop;

import java.awt.BorderLayout;
import java.awt.Component;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import javax.swing.JPanel;
import javax.swing.event.EventListenerList;
import org.joing.api.desktop.Desktop;
import org.joing.api.desktop.DesktopListener;
import org.joing.api.desktop.enums.TaskBarOrientation;
import org.joing.api.desktop.taskbar.TaskBar;
import org.joing.api.desktop.workarea.WorkArea;

/**
 *
 * @author Francisco Morero Peyrona
 */
public class DefaultDesktop implements Desktop
{
    private EventListenerList listenerList;
    private Vector<TaskBar>   vTaskBars;
    private Vector<WorkArea>  vWorkAreas;
    
    private WorkArea waActive;
    
    //------------------------------------------------------------------------//
    
    /** Creates a new instance of DesktopImpl */
    public DefaultDesktop()
    {
        listenerList = new EventListenerList();
        vWorkAreas   = new Vector<WorkArea>();
        vTaskBars    = new Vector<TaskBar>();
    }
    
    //------------------------------------------------------------------------//
    
    public void close()
    {
        for( WorkArea wa : vWorkAreas )
            wa.close();
    }
    
    //------------------------------------------------------------------------//
    // WORK AREAS
    
    public void addWorkArea( WorkArea wa )
    {
        vWorkAreas.add( wa );
        setActiveWorkArea( wa );
        fireWorkAreaAdded( wa );
    }
    
    public void removeWorkArea( WorkArea wa )
    {
        // Can't remove last WorkArea (a desktop must have at least one workarea)
        if( vWorkAreas.size() > 0 && vWorkAreas.contains( wa ) )
        {
            vWorkAreas.remove( wa );
            fireWorkAreaRemoved( wa );
        }
    }
    
    public List<WorkArea> getWorkAreas()
    {
        // Defensive copy
        ArrayList<WorkArea> list = new ArrayList<WorkArea>( vWorkAreas.size() );
                            list.addAll( vWorkAreas );
        return list;
    }
    
    public WorkArea getActiveWorkArea()
    {
        return waActive;
    }
    
    public void setActiveWorkArea( WorkArea wa )
    {
        if( vWorkAreas.contains( wa ) )
            waActive = wa;
    }
    
    //------------------------------------------------------------------------//
    // TASK BARS
    
    public List<TaskBar> getTaskBars()
    {
        // Defensive copy
        ArrayList<TaskBar> list = new ArrayList<TaskBar>( vTaskBars.size() );
                           list.addAll( vTaskBars );
        return list;
    }

    public void addTaskBar( TaskBar taskbar )
    {
        TaskBarOrientation orientation = taskbar.getOrientation();
        
        vTaskBars.add( taskbar );
        fireTaskBarAdded( taskbar );
    }
    
    public void removeTaskBar( TaskBar taskbar )
    {
        // A desktop can contain zero taskbars
        if( vTaskBars.contains( taskbar) )
        {
            vTaskBars.remove( taskbar );
            fireTaskBarRemoved( taskbar );
        }
    }

    //------------------------------------------------------------------------//
    // EVENTS
    
    public void addDesktopListener( DesktopListener dl )
    {
        listenerList.add( DesktopListener.class, dl );
    }

    public void removeDesktopListener( DesktopListener dl ) 
    {
        listenerList.add( DesktopListener.class, dl );
    }
    
    protected void fireWorkAreaAdded( WorkArea wa )
    {
        Object[] listeners = listenerList.getListenerList();
        
        // Process the listeners last to first, notifying
        for( int n = listeners.length - 2; n >= 0; n -= 2 )
        {
            if( listeners[n] == DesktopListener.class )
                ((DesktopListener) listeners[n+1]).workAreaAdded( wa );
        }
    }
    
    protected void fireWorkAreaRemoved( WorkArea wa )
    {
        Object[] listeners = listenerList.getListenerList();
        
        // Process the listeners last to first, notifying
        for( int n = listeners.length - 2; n >= 0; n -= 2 )
        {
            if( listeners[n] == DesktopListener.class )
                ((DesktopListener) listeners[n+1]).workAreaRemoved( wa );
        }
    }
    
    protected void fireTaskBarAdded( TaskBar tb )
    {
        Object[] listeners = listenerList.getListenerList();
        
        // Process the listeners last to first, notifying
        for( int n = listeners.length - 2; n >= 0; n -= 2 )
        {
            if( listeners[n] == DesktopListener.class )
                ((DesktopListener) listeners[n+1]).taskBarAdded( tb );
        }
    }
    
    protected void fireTaskBarRemoved( TaskBar tb )
    {
        Object[] listeners = listenerList.getListenerList();
        
        // Process the listeners last to first, notifying
        for( int n = listeners.length - 2; n >= 0; n -= 2 )
        {
            if( listeners[n] == DesktopListener.class )
                ((DesktopListener) listeners[n+1]).taskBarRemoved( tb );
        }
    }
}