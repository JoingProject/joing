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

package org.joing.kernel.api.desktop.taskbar;

import org.joing.kernel.api.desktop.Closeable;
import org.joing.kernel.api.desktop.DeskComponent;

/**
 *
 * @author Francisco Morero Peyrona
 */
public interface TaskBar extends Closeable
{
    enum Orientation { TOP, BOTTOM, LEFT, RIGHT, FREE }
    
    void add( TaskBarPanel tbp );
    void add( TaskBarPanel tbp, int x, int y );
    void add( TaskBarComponent dc );
    void add( TaskBarComponent dc, int x, int y );
    void remove( TaskBarPanel tbp );
    void remove( TaskBarComponent dc );
    
    int  getWidth();
    int  getHeight();
    
    TaskBarComponent getSysTray();
    
    boolean isAutoHide();
    void setAutoHide( boolean autohide );
    
    Orientation getOrientation();
    void setOrientation( Orientation orientation );
    
    void addTaskBarListener( TaskBarListener tbl );
    void removeTaskBarListener( TaskBarListener tbl );
}