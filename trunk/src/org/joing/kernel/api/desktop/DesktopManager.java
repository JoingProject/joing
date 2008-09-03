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

package org.joing.kernel.api.desktop;

import org.joing.kernel.api.desktop.desktop.Desktop;

/**
 *
 * @author Francisco Morero Peyrona
 */
public interface DesktopManager
{
    /**
     * Desktop runs inside a Frame (either Frame or JFrame)
     */
    void showInFrame();
    /**
     * Desktop runs in full-screen modes (when available).
     */
    void showInFullScreen();
    
    /**
     * This method exists to be called from Platform and should <u>never</u> be 
     * called from the Desktop.
     */
    void shutdown();
    /**
     * This method exists to be called from Platform and should <u>never</u> be 
     * called from the Desktop.
     */
    void halt();
    
    /**
     * Returns a reference to the Desktop
     * @return A reference to the Desktop
     */
    Desktop getDesktop();
    /**
     * Returns a reference to the Runtime
     * @return A reference to the Runtime
     */
    Runtime getRuntime();
    
    /**
     * Locks desktop.
     * <p>
     * Note: For security reasons, unlock does not exists because there should 
     * not be an API (a programatically way) to unlock the screen.<br>
     * Therefore, the lock module has to provide the mechanism to unlock the 
     * desktop.
     */
    void lock();
}