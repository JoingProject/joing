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
import java.io.FileFilter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import org.joing.kernel.api.desktop.DeskComponent;
import org.joing.kernel.api.desktop.deskwidget.deskLauncher.DeskLauncher;
import org.joing.kernel.api.desktop.pane.DeskFrame;
import org.joing.common.dto.app.AppDescriptor;
import org.joing.kernel.runtime.vfs.JoingFileSystemView;
import org.joing.kernel.runtime.vfs.VFSFile;
import org.joing.kernel.swingtools.JoingApplicationChooser;
import org.joing.kernel.swingtools.JoingSwingUtilities;

/**
 * Actions that work the same for JoingFileSystemTree and JoingFileSystemList.
 * <p>
 * Note: both implement FileSystemActinable interface.
 * 
 * @author Francisco Morero Peyrona
 */
public class FileSystemActionableCommonActions
{
    public File createFolder( File fParent )
    {
        return _create( fParent, true );
    }
    
    public File createFile( File fParent )
    {
        return _create( fParent, false );
    }
    
    public boolean delete( File file )
    {
        return ((file instanceof VFSFile) ? file.delete() : deepDeleteLocalFS( file ));
    }
    
    // Retrieve all files in passed dir or null if none
    public File[] getFilesIn( File fParent, final boolean bShowFiles, final boolean bShowHidden )
    {
        // List files, hidden are not included
        File[] afChildren = fParent.listFiles( new FileFilter() 
        {
            public boolean accept( File f )
            {
                return (bShowFiles || f.isDirectory()) && (bShowHidden || ! f.isHidden());
            }
        } );
        
        if( afChildren == null )    // Yes, afChildren can be null
        {
            afChildren = new File[0];
        }
        else                        // Sort directories before files
        {
            Arrays.sort( afChildren, new Comparator<File>()
            {
                public int compare( File f1, File f2 )
                {
                    if( f1.isDirectory() && ! f2.isDirectory() )       return -1;
                    else if ( ! f1.isDirectory() && f2.isDirectory() ) return 1;
                    else                                               return f1.getName().compareTo( f2.getName() );
                }
            } );
        }
        
        return afChildren;
    }
    
    public void properties( File file )
    {
        DeskComponent panel = null;
        DeskFrame     frame = org.joing.kernel.jvmm.RuntimeFactory.getPlatform().
                                    getDesktopManager().getRuntime().createFrame();
                      frame.setTitle( "Virtual File Properties" );
        
        if( file instanceof VFSFile )
        {
            panel = new JVFSPropertiesPanel( ((VFSFile) file) );
        }
        else
        {
            org.joing.kernel.jvmm.RuntimeFactory.getPlatform().getDesktopManager().getRuntime().
                showMessageDialog( null, "Option not yet implemented" );
        }
        
        if( panel != null )
        {
            frame.add( panel );
            org.joing.kernel.jvmm.RuntimeFactory.getPlatform().getDesktopManager().
                    getDesktop().getActiveWorkArea().add( frame );
        }
    }
    
    /**
     * 
     * @param f File to be opened: can't be null.
     */
    public void openFile( File f )
    {
        AppDescriptor appDesc = null;
        int           nIndex  = f.getName().lastIndexOf( '.' );

        if( nIndex > 0 )
        {
            String sFileExtension = f.getName().substring( nIndex );

            appDesc = org.joing.kernel.jvmm.RuntimeFactory.getPlatform().getBridge().
                                    getAppBridge().getPreferredForType( sFileExtension );
        }

        if( appDesc == null )
        {
            org.joing.kernel.jvmm.RuntimeFactory.getPlatform().getDesktopManager().getRuntime().
                showMessageDialog( "Open file", "There is no application associated with this type of file." );

            AppDescriptor app = JoingApplicationChooser.showDialog();
        }

        if( appDesc != null )
        {
            JoingSwingUtilities.launch( DeskLauncher.Type.APPLICATION, 
                                        String.valueOf( appDesc.getId() ), 
                                        "\""+ f.getAbsolutePath() +"\"" );  // "" are only needed when file name contains spaces
        }
    }
    
    //------------------------------------------------------------------------//
    
    private File _create( File fParent, boolean bFolder )
    {
        File fRet = null;
        
        try
        {
            if( bFolder )
                fRet = JoingFileSystemView.getFileSystemView().createNewFolder( fParent );
            else
                fRet = JoingFileSystemView.getFileSystemView().createNewFile( fParent );
        }
        catch( IOException exc )
        {
            org.joing.kernel.jvmm.RuntimeFactory.getPlatform().getDesktopManager().getRuntime().showException( exc, null );
        }
        
        return fRet;
    }
    
    // If passed file is a directory, recursively deletes it an all its files.
    // If passed file is just a file, deletes it.
    private boolean deepDeleteLocalFS( java.io.File f )
    {
        boolean bSuccess = false;
        
        if( f.exists() )
        {
            bSuccess = true;
            java.io.File[] files = f.listFiles();
            
            if( files != null )
            {
                for( int n = 0; n < files.length; n++ )
                {
                    if( files[n].isDirectory() )
                        bSuccess = bSuccess && deepDeleteLocalFS( files[n] );
                    else
                        bSuccess = bSuccess && files[n].delete();
                }
            }
            
            bSuccess = bSuccess && f.delete();
        }
        
        return bSuccess;
    }
}