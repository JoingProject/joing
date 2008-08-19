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
package org.joing.yace;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

/**
 *
 * @author Francisco Morero Peyrona
 */
public class JoingCommunityTree extends JTree // TODO: Que herede de JoingSwingTree
{
    private boolean bOnlyConnected;
    
    //------------------------------------------------------------------------//
    
    public JoingCommunityTree()
    {
        bOnlyConnected = false;
        
        setRootVisible( true );
        setShowsRootHandles( true );
        setModel( new DefaultTreeModel( createRootNodes() ) );
        setCellRenderer( new UserTreeCellRenderer() );
    }
    
    /**
     * To show only dirs or dirs and files.
     * Invoking this method affect only when loading new nodes.
     * 
     * By default is <code>false</code>
     * @param bOnlyDirs
     */
    public void setOnlyConnected( boolean bOnlyDirs )
    {
        this.bOnlyConnected = bOnlyDirs;
    }
    
    /**
     * Is showing only dirs or also files?
     * 
     * @return <code>true</code> if is showing only dirs.
     */
    public boolean isOnlyConnected()
    {
        return bOnlyConnected;
    }
    
    //------------------------------------------------------------------------//
    
    private DefaultMutableTreeNode createRootNodes()
    {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode( "Community: joing.org" );
                               root.add( new DefaultMutableTreeNode( "John Doe" ) );
                               root.add( new DefaultMutableTreeNode( "Mary Doe" ) );
                               root.add( new DefaultMutableTreeNode( "Frank Doe" ) );
                               root.add( new DefaultMutableTreeNode( "Albert Einstein" ) );
                               root.add( new DefaultMutableTreeNode( "Friedrich Nietszche" ) );
                               root.add( new DefaultMutableTreeNode( "Agustina de Aragón" ) );
                               root.add( new DefaultMutableTreeNode( "José María 'el Tempranillo'" ) );
        return root;
    }
}