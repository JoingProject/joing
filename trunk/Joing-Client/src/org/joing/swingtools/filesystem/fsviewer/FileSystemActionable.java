/*
 * Copyright (C) 2007, 2008 Join'g Team Members.  All Rights Reserved.
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

package org.joing.swingtools.filesystem.fsviewer;

import java.io.File;

/**
 * These are the actions that classes implemeting this interface can execute.
 * <p>
 * Note: actins are stored in FileSystemWorks.
 * 
 * @author Francisco Morero Peyrona
 */
public interface FileSystemActionable
{
    /**
     * If more than one item is selected this returns the first one and makes
     * the rest selected nodes unselected.
     * 
     * @return The file representing the first selected item.
     */
    File getSelectedFile();
    void setSelectedFile( File file );
    void reloadAll();
    void reloadSelected();
    void createFolder();
    void createFile();
    void cut();
    void copy();
    void paste();
    void deleteAllSelected();
    void rename();
    void properties();
}