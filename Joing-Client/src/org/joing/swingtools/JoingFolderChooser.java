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

package org.joing.swingtools;

import java.awt.BorderLayout;
import java.io.File;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import org.joing.common.desktopAPI.DeskComponent;
import org.joing.swingtools.fsviewer.FileSystemJobs;
import org.joing.swingtools.fsviewer.fstree.JoingFileSystemTree;

/**
 * Allows users to select a folder.
 * <p>
 * Even if the standard way to select a folder is via JoingFileChooser,
 * this is another way to do the same with a different user interface: a tree
 * instead of a list.
 * 
 * @author Francisco Morero Peyrona
 */
public class JoingFolderChooser extends JPanel implements DeskComponent
{
    private JoingFileSystemTree tree;
    
    public JoingFolderChooser()
    {
        tree = new JoingFileSystemTree();
        tree.setFileSystemWorks( new FileSystemJobs() );
        
        setBorder( new EmptyBorder( 4,4,4,4 ) );
        setLayout( new BorderLayout() );
        add( new JScrollPane( tree ), BorderLayout.CENTER );
    }
    
    public File getSelected()
    {
        return tree.getSelectedFile();
    }
    
    /**
     * Open a dialog showing in a tree all installed applications for current
     * user.
     * 
     * @return An instance of <code>org.joing.common.dto.app.AppDescriptor</code>
     *         if user selected an application or <code>null</code> if cancelled.
     */
    public static File showDialog()
    {
        File file = null;
        
        synchronized( JoingFolderChooser.class )
        {
            JoingFolderChooser jfc = new JoingFolderChooser();
        
            if( org.joing.jvmm.RuntimeFactory.getPlatform().getDesktopManager().getRuntime().
                    showAcceptCancelDialog( "Select Folder", jfc ) )
            {
                file = jfc.getSelected();
            }
        }
        
        return file;
    }
}