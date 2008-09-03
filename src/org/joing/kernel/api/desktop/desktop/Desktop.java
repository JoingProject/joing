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

package org.joing.kernel.api.desktop.desktop;

import java.awt.Image;
import org.joing.kernel.api.desktop.workarea.WorkArea;
import java.util.List;
import org.joing.kernel.api.desktop.Closeable;
import org.joing.kernel.api.desktop.taskbar.TaskBar;

/**
 *
 * @author Francisco Morero Peyrona
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