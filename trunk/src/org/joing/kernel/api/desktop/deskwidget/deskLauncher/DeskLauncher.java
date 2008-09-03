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

package org.joing.kernel.api.desktop.deskwidget.deskLauncher; 

import java.awt.Image;
import org.joing.kernel.api.desktop.Selectable;
import org.joing.kernel.api.desktop.deskwidget.DeskWidget;

/**
 * Common base class for all kind of "icons like" that are shown in desktop.<br>
 * Among them: applications, documents, devices, etc.
 * 
 * @author Francisco Morero Peyrona
 * 
 * updated by: Francisco Morero Peyrona
 */
public interface DeskLauncher extends DeskWidget, Selectable
{
    enum Type { APPLICATION, DIRECTORY }
    
    Type getType();
    
    /**
     * 
     * Returns the image or null if the deafult one was used: this is important 
     * because the default one can change from one desktop to another.
     * 
     * @return The image or null if the deafult one was used.
     */
    Image getImage();
    /**
     * If image is null or empty, the desktop will asign a default one.
     * @param image
     */
    void  setImage( Image image );
    
    String getText();
    void   setText( String sText );
    
    public void launch();
    
    /**
     * The target can be one of following:
     * <ul>
     * <li>A path to a file or directory (local or remote): must start with a valid root ("/", "C:/", etc)</li>
     * <li>A remote application denoted as an integer number</li>
     * <li>An application denoted as a file in a local or remote directory</li>
     * </ul>
     * 
     * @return The target of this launcher.
     */
    public String getTarget();
    /**
     * See getTarget()
     * @param s
     * @see getTarget()
     */
    public void   setTarget( String s );
    
    /**
     * Application arguments or <code>null</code> if launcher is of type DIRECTORY.
     * 
     * @return The application arguments.
     */
    public String getArguments();
    public void   setArguments( String s );
    
    public void addLauncherListener( DeskLauncherListener dll );
    public void removeLauncherListener( DeskLauncherListener dll );
}