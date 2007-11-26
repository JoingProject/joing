/*
 * JoingFileSystemView.java
 *
 * Created on 05-ago-2007, 12:06:14
 *
 * Author: Francisco Morero Peyrona.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or (at
 * your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 */
package org.joing.pde.vfs;

import java.io.IOException;
import javax.swing.filechooser.FileSystemView;

/**
 * Common class that internally handles one <code>FileSystemView</code> instance
 * for local OS (in case that Join'g is not running as unsigned applet) and one
 * <code>FileSystemView</code> for the remote VFS (and instance of 
 * <code>VFSView</code>).
 * 
 * @author Francisco Morero Peyrona
 */
public class JoingFileSystemView extends FileSystemView
{
    private FileSystemView local;
    private VFSView        remote;
    
    private static JoingFileSystemView instance = null;
    
    //------------------------------------------------------------------------//
    
    public static JoingFileSystemView getFileSystemView()
    {
        if( instance == null )
        {
            synchronized( VFSView.class )
            {
                if( instance == null )
                    instance = new JoingFileSystemView();
            }
        }

        return instance;
    }
    
    /**
     * @see javax.swing.filechooser.FileSystemView#getRoots()
     */
    @Override
    public java.io.File[] getRoots()
    {
        java.io.File[]  afLocal  = local.getRoots();
        java.io.File[]  afRemote = remote.getRoots();
        java.io.File[]  afAll    = new java.io.File[ afLocal.length + afRemote.length ];
        
        System.arraycopy( afRemote, 0, afAll,               0, afRemote.length );
        System.arraycopy( afLocal , 0, afAll, afRemote.length, afLocal.length );
        
        return afAll;
    }
    
    /**
     * @see javax.swing.filechooser.FileSystemView#createFileObject( 
     *                                 java.io.File containingDir, String name )
     */
    @Override
    public java.io.File createFileObject( java.io.File parentDir, String name ) 
    {
        if( parentDir instanceof VFSFile )
            return remote.createFileObject( parentDir, name );
        else
            return local.createFileObject( parentDir, name );
    }
    
    /**
     * @see javax.swing.filechooser.FileSystemView#createFileObject(String path)
     */
    @Override
    public java.io.File createFileObject( String path )
    {
        throw new UnsupportedOperationException( "Not supported yet." );
    }
    
    /**
     * @see javax.swing.filechooser.FileSystemView#createNewFolder( 
     *                                              java.io.File containingDir )
     */
    public java.io.File createNewFolder( java.io.File dir ) throws IOException
    {
        throw new UnsupportedOperationException( "Not supported yet." );
    }
    
    /**
     * @see javax.swing.filechooser.FileSystemView#getFiles( 
     *                           java.io.File directory, boolean useFileHiding )
     */    
    @Override
    public java.io.File[] getFiles( java.io.File directory, boolean useFileHiding )
    {
        if( directory instanceof VFSFile )
            return remote.getFiles( directory, useFileHiding );
        else
            return local.getFiles( directory, useFileHiding );
    }
    
    /**
     * @see javax.swing.filechooser.FileSystemView#getHomeDirectory()
     */
    @Override
    public java.io.File getHomeDirectory()
    {
	return remote.getRoots()[0];
    }
    
    /**
     * @see javax.swing.filechooser.FileSystemView#getParentDirectory( 
     *                                                  java.io.File directory )
     */
    @Override
    public VFSFile getParentDirectory( java.io.File directory )
    {
        throw new UnsupportedOperationException( "Not supported yet." );
    }
    
    /**
     * @see javax.swing.filechooser.FileSystemView#getSystemDisplayName( java.io.File f )
     */
    @Override
    public String getSystemDisplayName( java.io.File file )
    {
        if( file instanceof VFSFile )
            return remote.getSystemDisplayName( file );
        else
            return local.getSystemDisplayName( file );
    }
    
    /**
     * @see javax.swing.filechooser.FileSystemView#isComputerNode( java.io.File dir )
     */
    @Override
    public boolean isComputerNode( java.io.File dir )
    {
        if( dir instanceof VFSFile )
            return remote.isComputerNode( dir );
        else
            return local.isComputerNode( dir );
    }
    
    /**
     * @see javax.swing.filechooser.FileSystemView#isDrive( java.io.File dir )
     */
    @Override
    public boolean isDrive( java.io.File dir )
    {
        if( dir instanceof VFSFile )
            return remote.isDrive( dir );
        else
            return local.isDrive( dir );
    }
    
    /**
     * @see javax.swing.filechooser.FileSystemView#isFileSystem( java.io.File f )
     */
    @Override
    public boolean isFileSystem( java.io.File file )
    {
        if( file instanceof VFSFile )
            return remote.isFileSystem( file );
        else
            return local.isFileSystem( file );
    }
    
    /**
     * @see javax.swing.filechooser.FileSystemView#isFileSystemRoot( java.io.File dir )
     */
    @Override
    public boolean isFileSystemRoot( java.io.File dir )
    {
        if( dir instanceof VFSFile )
            return remote.isFileSystemRoot( dir );
        else
            return local.isFileSystemRoot( dir );
    }
    
    /**
     * @see javax.swing.filechooser.FileSystemView#isFloppyDrive( java.io.File dir )
     */
    @Override
    public boolean isFloppyDrive( java.io.File dir )
    {
        if( dir instanceof VFSFile )
            return remote.isFloppyDrive( dir );
        else
            return local.isFloppyDrive( dir );
    }
    
    /**
     * @see javax.swing.filechooser.FileSystemView#isHiddenFile(java.io.File file)
     */
    @Override
    public boolean isHiddenFile( java.io.File file )
    {
        if( file instanceof VFSFile )
            return remote.isHiddenFile( file );
        else
            return local.isHiddenFile( file );
    }
    
    /**
     * @see javax.swing.filechooser.FileSystemView#isParent( java.io.File folder, java.io.File file )
     */
    @Override
    public boolean isParent( java.io.File folder, java.io.File file )
    {
        if( folder instanceof VFSFile )
            return remote.isParent( folder, file );
        else
            return local.isParent( folder, file );
    }
    
    /**
     * @see javax.swing.filechooser.FileSystemView#isRoot( java.io.File file )
     */
    @Override
    public boolean isRoot( java.io.File file )
    {
        if( file instanceof VFSFile )
            return remote.isRoot( file );
        else
            return local.isRoot( file );
    }
    
    /**
     * @see javax.swing.filechooser.FileSystemView#isTraversable( java.io.File file )
     */
    @Override
    public Boolean isTraversable( java.io.File file )
    {
	if( file instanceof VFSFile )
            return remote.isTraversable( file );
        else
            return local.isTraversable( file );
    }
       
    //------------------------------------------------------------------------//
    
    private JoingFileSystemView()
    {
        local  = FileSystemView.getFileSystemView();
        remote = VFSView.getFileSystemView();
    }
}