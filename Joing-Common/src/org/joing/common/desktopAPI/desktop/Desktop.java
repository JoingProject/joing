/*
 * Desktop.java
 *
 * Created on 25 de abril de 2007, 11:54 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.joing.common.desktopAPI.desktop;

import org.joing.common.desktopAPI.*;
import org.joing.common.desktopAPI.workarea.WorkArea;
import java.util.List;
import org.joing.common.desktopAPI.taskbar.TaskBar;

/**
 *
 * @author Mario Serrano
 */
public interface Desktop extends Closeable
{
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
    
    // Events
    void addDesktopListener( DesktopListener dl );
    void removeDesktopListener( DesktopListener dl );
}