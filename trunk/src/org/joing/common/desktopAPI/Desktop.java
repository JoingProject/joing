/*
 * Desktop.java
 *
 * Created on 25 de abril de 2007, 11:54 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.joing.common.desktopAPI;

import java.awt.Component;
import org.joing.common.desktopAPI.workarea.WorkArea;
import java.util.List;
import org.joing.common.desktopAPI.taskbar.TaskBar;

/**
 *
 * @author Mario Serrano
 */
public interface Desktop
{
    public void close();
    
    // Work Areas
    public List<WorkArea> getWorkAreas();
    public void addWorkArea( WorkArea workarea );
    public void removeWorkArea( WorkArea workarea );
    public WorkArea getActiveWorkArea();
    public void setActiveWorkArea( WorkArea workarea  );
    
    // Task Bars
    public List<TaskBar> getTaskBars();
    public void addTaskBar( TaskBar taskbar );
    public void removeTaskBar( TaskBar taskbar );
    
    // GENERIC ADD AND REMOVE
    /**
     * Convenience method to add a component to the default work area.
     * @param comp Component to be added.
     */
    public Component add( Component comp );
    /**
     * Convenience method to remove a component from the work area where it is
     * located.
     * 
     * @param comp Component to be removed.
     */
    public void remove( Component comp );
    
    // Events
    public void addDesktopListener( DesktopListener dl );
    public void removeDesktopListener( DesktopListener dl );
}