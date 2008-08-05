/*
 * Desktop.java
 *
 * Created on 25 de abril de 2007, 11:54 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.joing.common.desktopAPI.desktop;

import java.awt.Image;
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
    
    // Other things
    /**
     * A way to notify user without disturbing him/her (because in a nice GUI
     * dialogs and other gadgets that block user inputs should be avoided).
     * 
     * Every Join'g desktop can implement this information in different ways:
     * using a mesagge in the status bar, a canvas, a bubble, etc.
     * 
     * This method can be invoked (as an example) from Platform to inform that a
     * long task is in progress against Join'g Server.
     * 
     * @param sMessage Message to be shown: if null an standard one will be used.
     * @param icon     Icon to be shown: if null an standard one will be used.
     * @param bAnimated <code>true</code> to show an animation: again, every implementation 
     *                 decide what to use, v.g. an indeterminate progress bar or
     *                 an animated GIF image.
     * @return A handle to the message GUI to be closed when the trade is over.
     */
    int  showNotification( String sMessage, Image icon );
    /**
     * Hides a notification previously opended via showNotification(...)
     * 
     * @param nHandle The one returned by showNotification(...)
     */
    void hideNotification( int nHandle );
}