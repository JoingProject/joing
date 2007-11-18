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
    void close();
    
    // Work Areas
    List<WorkArea> getWorkAreas();
    void addWorkArea( WorkArea workarea );
    void removeWorkArea( WorkArea workarea );
    WorkArea getActiveWorkArea();
    void setActiveWorkArea( WorkArea workarea  );
    
    // Task Bars
    List<TaskBar> getTaskBars();
    void addTaskBar( TaskBar taskbar );
    void removeTaskBar( TaskBar taskbar );
    
    // Genric add(), remove() and find()
    /**
     * Convenience method to add a component to the default work area.
     * @param comp Component to be added.
     * @return Component added.
     */
    Component add( Component comp );
    /**
     * Convenience method to remove a component from the work area where it is
     * located.
     * 
     * @param comp Component to be removed.
     * @@return Component removed or null if component was not found.
     */
    void remove( Component comp );
    WorkArea findWorkAreaFor( Component comp );
    
    // Events
    public void addDesktopListener( DesktopListener dl );
    public void removeDesktopListener( DesktopListener dl );
}