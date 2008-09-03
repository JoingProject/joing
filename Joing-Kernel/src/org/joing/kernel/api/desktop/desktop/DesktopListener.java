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

import org.joing.kernel.api.desktop.workarea.WorkArea;
import org.joing.kernel.api.desktop.taskbar.TaskBar;

/**
 *
 * @author Francisco Morero Peyrona
 */
public interface DesktopListener
{
    /**
     * Invoked when a WorkArea has been added to the Desktop.
     */
    public void workAreaAdded( WorkArea wa );
    
    /**
     * Invoked when a WorkArea has been removed from the Desktop.
     */
    public void workAreaRemoved( WorkArea wa );
    
    /**
     * Invoked when selecetd (active) WorkArea changes.
     */
    public void workAreaSelected( WorkArea waPrevious, WorkArea waCurrent );
    
    /**
     * Invoked when a TaskBar has been added to the Desktop.
     */
    public void taskBarAdded( TaskBar tb );
    
    /**
     * Invoked when a TaskBar has been removed from the Desktop.
     */
    public void taskBarRemoved( TaskBar tb );
}