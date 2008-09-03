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
     * To show only folders or folders and files.<b>
     * Invoking this method affect only after reloading.
     * 
     * By default is <code>true</code>
     * @param bShowFiles
     */
    public void setShowingFiles( boolean bShowFiles );
    /**
     * Is showing only folders or also files?
     * 
     * @return <code>true</code> if is showing folders and files.
     */
    public boolean isShowingFiles();
    /**
     * To show or hide files and folders.
     * Invoking this method affects only after reloading.
     * 
     * By default is <code>false</code>
     * @param bShowHidden
     */
    public void setShowingHidden( boolean bShowHidden );
    /**
     * Is showing hidden folders and files?
     * 
     * @return <code>true</code> if is showing hidden folders and files.
     */
    public boolean isShowingHidden();   
    /**
     * If more than one item is selected this returns the first one and makes
     * the rest selected nodes unselected.
     * 
     * @return The file representing the first selected item.
     */
    File getSelected();
    /**
     * Selects a file or folder.
     * <p>
     * The path to the file or folder has to be selected. Selected parent will 
     * be the entity's parent and the file or folder inside parent will appear
     * as selected (highlighted).
     * 
     * @param file File or foler to be selected.
     */
    void setSelected( File file );
    /**
     * Reloads files and folders from root.
     */
    void reloadAll();
    /**
     * Reloads files and folders under selected path.
     */
    void reloadSelected();
    /**
     * Creates new empty folder with default name.
     */
    void newFolder();
    /**
     * Creates new empty file with default name.
     */
    void newFile();
    /**
     * Mark selected files and folders to be moved at paste location.
     */    
    void cut();
    /**
     * Mark selected files and folders to be duplicated at paste location.
     */
    void copy();
    /**
     * Paste all previously cuted or copied files and folders.
     */
    void paste();
    /**
     * Delete all selected files and folders.
     */
    void delete();
    /**
     * Moves all selected files and folders to trashcan.
     */
    void toTrascan();
    /**
     * Rename first selected file or folder.
     */
    void rename();
    /**
     * Shows a dialog to view/edit selected file or folder properties.
     */
    void properties();
    /**
     * Add a new listner to be notified when an event occurs.
     * @param fsal The new listener.
     */
    void addListener( FileSystemActionableListener fsal );
    /**
     * Removes an existing listener.
     * @param fsal The listener to be removed.
     */
    void removeListener( FileSystemActionableListener fsal );
}