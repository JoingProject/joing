/*
 * Desktop.java
 *
 * Created on 25 de abril de 2007, 11:54 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.joing.api.desktop;

import java.awt.Container;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.awt.event.WindowListener;
import java.awt.event.WindowStateListener;
import org.joing.api.desktop.workarea.WorkArea;
import java.util.List;
import org.joing.api.desktop.taskbar.TaskBar;

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
    
    // Windows (Frames)
    public DeskFrame  createFrame();
    public DeskDialog createDialog();
    public DeskCanvas createCanvas();
    
    // Events
    public void addDesktopListener( DesktopListener dl );
    public void removeDesktopListener( DesktopListener dl );
}