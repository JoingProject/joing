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

package org.joing.kernel.swingtools.filesystem.fsviewer.fstree;

import java.io.File;
import javax.swing.tree.DefaultMutableTreeNode;
import org.joing.kernel.runtime.vfs.JoingFileSystemView;
import org.joing.kernel.runtime.vfs.VFSFile;

/**
 *
 * @author Francisco Morero Peyrona
 */
class TreeNodeFile extends DefaultMutableTreeNode
{
    private boolean bFakedNode = false;
    
    //------------------------------------------------------------------------//
    
    TreeNodeFile()
    {
        super( null, true );   // true because this constructor is used by roots
    }
    
    TreeNodeFile( File f )
    {
        super( f, f.isDirectory() );
    }
    
    TreeNodeFile( boolean bFakedNode )
    {
        super( null, false );
        this.bFakedNode = bFakedNode;
    }
    
    //------------------------------------------------------------------------//
    
    File getFile()
    {
        return (File) super.userObject;
    }
    
    public boolean getAllowsChildren()
    {
        File f = (File) getUserObject();
        
        return (f == null || f.isDirectory());    // f == null => root
    }
    
    public boolean isLeaf()
    {
        return ! getAllowsChildren();
    }
    
    boolean isFakedNode()
    {
        return bFakedNode;
    }
    
    public String toString()
    {
        File   f = (File) getUserObject();
        String s = null;

        if( f == null )
        {
            if( bFakedNode )
                s = "Reading files ...";
            else
                s = "Join'g File System";   // Should not happen because root should not be visible
        }
        else
        {
            s = f.getName();

            if( JoingFileSystemView.getFileSystemView().isRoot( f ) )
                s += (f instanceof VFSFile ? " (Remote)" : " (Local)");
        }

        return s;
    }
}