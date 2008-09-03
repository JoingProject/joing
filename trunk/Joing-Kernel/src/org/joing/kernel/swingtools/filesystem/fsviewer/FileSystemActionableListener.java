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

package org.joing.kernel.swingtools.filesystem.fsviewer;

import java.io.File;
import java.util.EventListener;
import java.util.List;

/**
 *
 * @author Francisco Morero Peyrona
 */
public interface FileSystemActionableListener extends EventListener
{
    void selectionChanged( File fSelected );
    void folderCreated( File fCreated );
    void fileCreated( File fCreated );
    void cutted( List<File> cutted );
    void copied( List<File> copied );
    void pasted( List<File> pasted );
    void deleted( List<File> deleted );
    void movedToTrashcan( List<File> trashcan );
    void renamed( File fRenamed );
    void propertiesChanged( File fChanged );
    /**
     * Selected entity (file or folder) is requested to be opened.<br>
     * Fired normally when enter is pressed or double click is performed.
     * 
     * @param fToOpen
     */
    void open( File fToOpen );
}