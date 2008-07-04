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
package org.joing.runtime.vfs;

import java.io.IOException;
import javax.swing.Icon;
import javax.swing.filechooser.FileSystemView;

/**
 * Common class that internally handles one <code>FileSystemView</code> instance
 * for localView OS (in case that Join'g is not running as unsigned applet) and one
 * <code>FileSystemView</code> for the remoteView VFS (and instance of 
 * <code>VFSView</code>).
 * 
 * @author Francisco Morero Peyrona
 */
public class JoingFileSystemView extends FileSystemView
{
    private FileSystemView localView  = null;
    private VFSView        remoteView = null;
    
    private static JoingFileSystemView instance = null;
    
    //------------------------------------------------------------------------//
    
    public static JoingFileSystemView getFileSystemView()
    {
        synchronized( JoingFileSystemView.class )
        {
            if( instance == null )
                instance = new JoingFileSystemView();
        }
        
        return instance;
    }
    
    /**
     * @see javax.swing.filechooser.FileSystemView#createFileObject( 
     *                                 java.io.File containingDir, String name )
     */
    public java.io.File createFileObject( java.io.File dir, String name  )
    {
        if( dir instanceof VFSFile )
            return remoteView.createFileObject( dir, name );
        else
            return localView.createFileObject( dir, name );
    }
    
    /**
     * @see javax.swing.filechooser.FileSystemView#createFileObject(String path)
     */
    public java.io.File createFileObject( String path )
    {
        java.io.File fLocal = new java.io.File( path );
        
        // FIXME: hacerlo
        return new java.io.File( path );
    }
    
    /**
     * @see javax.swing.filechooser.FileSystemView#createNewFolder( 
     *                                              java.io.File containingDir )
     */
    public java.io.File createNewFolder( java.io.File dir ) throws IOException
    {
        if( dir instanceof VFSFile )
            return remoteView.createNewFolder( dir );
        else
            return localView.createNewFolder( dir );
    }
    
    /**
     * @see javax.swing.filechooser.FileSystemView#getChild(java.io.File parent,
     *                                                      String fileName)
     */
    public java.io.File getChild( java.io.File dir, String fileName )
    {
        return createFileObject( dir, fileName );
    }
    
    /**
     * @see javax.swing.filechooser.FileSystemView#getDefaultDirectory()
     */  
    public java.io.File getDefaultDirectory()
    {
        return getHomeDirectory();   // Default starting directory for the file chooser.
    }
    
    /**
     * @see javax.swing.filechooser.FileSystemView#getFiles( 
     *                           java.io.File directory, boolean useFileHiding )
     */
    public java.io.File[] getFiles( java.io.File dir, boolean useFileHiding )
    {
        if( dir instanceof VFSFile )
            return remoteView.getFiles( dir, useFileHiding );
        else
            return localView.getFiles( dir, useFileHiding );
    }
    
    /**
     * @see javax.swing.filechooser.FileSystemView#getHomeDirectory()
     */
    public java.io.File getHomeDirectory()
    {
        // In Local File System View it would be:
        // return createFileObject( System.getProperty("user.home") );
        // But a Joing user normally want to use the Remote FS
	return localView.getHomeDirectory();   // FIXME: cambiarlo por remoteView cuando esté corregido el bug
    }
    
    /**
     * @see javax.swing.filechooser.FileSystemView#getParentDirectory( 
     *                                                  java.io.File directory )
     */
    public java.io.File getParentDirectory( java.io.File dir )
    {
        if( dir instanceof VFSFile )
            return remoteView.getParentDirectory( dir );
        else
            return localView.getParentDirectory( dir );
    }
    
    /**
     * @see javax.swing.filechooser.FileSystemView#getRoots()
     */
    public java.io.File[] getRoots()
    {
        java.io.File[] afLocal  = localView.getRoots();
        VFSFile[]      afRemote = remoteView.getRoots();
        java.io.File[] afAll    = new java.io.File[ afLocal.length + afRemote.length ];
        
        // FIXME: user este cuando esté corregido el bug
        //System.arraycopy( afRemote, 0, afAll,               0, afRemote.length );
        //System.arraycopy( afLocal , 0, afAll, afRemote.length, afLocal.length );
        
        // FIXME: Quitarlo cuando esté corregido el bug
        System.arraycopy( afLocal , 0, afAll,              0, afLocal.length );
        System.arraycopy( afRemote, 0, afAll, afLocal.length, afRemote.length );
        
        return afAll;
    }
    
    /**
     * @see javax.swing.filechooser.FileSystemView#getSystemDisplayName( java.io.File file )
     */
    public String getSystemDisplayName( java.io.File file )
    {
        if( file instanceof VFSFile )
            return remoteView.getSystemDisplayName( file );
        else
            return localView.getSystemDisplayName( file );
    }
    
    public Icon getSystemIcon( java.io.File file  )
    {
        if( file instanceof VFSFile )
            return remoteView.getSystemIcon( file  );
        else
            return localView.getSystemIcon( file  );
    }
    
    public String getSystemTypeDescription( java.io.File file ) 
    {
        if( file instanceof VFSFile )
            return remoteView.getSystemTypeDescription( file );
        else
            return localView.getSystemTypeDescription( file );
    }
    
    /**
     * @see javax.swing.filechooser.FileSystemView#isComputerNode( java.io.File dir )
     */
    public boolean isComputerNode( java.io.File dir )
    {
        if( dir instanceof VFSFile )
            return remoteView.isComputerNode( dir );
        else
            return localView.isComputerNode( dir );
    }
    
    /**
     * @see javax.swing.filechooser.FileSystemView#isDrive( java.io.File dir )
     */
    public boolean isDrive( java.io.File dir )
    {
        if( dir instanceof VFSFile )
            return remoteView.isDrive( dir );
        else
            return localView.isDrive( dir );
    }
    
    /**
     * @see javax.swing.filechooser.FileSystemView#isFileSystem( java.io.File file )
     */
    public boolean isFileSystem( java.io.File file )
    {
        if( file instanceof VFSFile )
            return remoteView.isFileSystem( file );
        else
            return localView.isFileSystem( file );
    }
    
    /**
     * @see javax.swing.filechooser.FileSystemView#isFileSystemRoot( java.io.File dir )
     */
    public boolean isFileSystemRoot( java.io.File dir )
    {
        if( dir instanceof VFSFile )
            return remoteView.isFileSystemRoot( dir );
        else
            return localView.isFileSystemRoot( dir );
    }
    
    /**
     * @see javax.swing.filechooser.FileSystemView#isFloppyDrive( java.io.File dir )
     */
    public boolean isFloppyDrive( java.io.File dir )
    {
        if( dir instanceof VFSFile )
            return remoteView.isFloppyDrive( dir );
        else
            return localView.isFloppyDrive( dir );
    }
    
    /**
     * @see javax.swing.filechooser.FileSystemView#isHiddenFile(java.io.File file)
     */
    public boolean isHiddenFile( java.io.File file )
    {
        if( file instanceof VFSFile )
            return remoteView.isHiddenFile( file );
        else
            return localView.isHiddenFile( file );
    }
    
    /**
     * @see javax.swing.filechooser.FileSystemView#isParent( java.io.File folder, java.io.File file )
     */
    public boolean isParent( java.io.File folder, java.io.File file )
    {
        if( folder instanceof VFSFile )
            return remoteView.isParent( folder, file );
        else
            return localView.isParent( folder, file );
    }
    
    /**
     * @see javax.swing.filechooser.FileSystemView#isRoot( java.io.File file )
     */
    public boolean isRoot( java.io.File file )
    {
        if( file instanceof VFSFile )
            return remoteView.isRoot( file );
        else
            return localView.isRoot( file );
    }
    
    /**
     * @see javax.swing.filechooser.FileSystemView#isTraversable( java.io.File file )
     */
    public Boolean isTraversable( java.io.File file )
    {
	if( file instanceof VFSFile )
            return remoteView.isTraversable( file );
        else
            return localView.isTraversable( file );
    }
       
    //------------------------------------------------------------------------//
    
    private JoingFileSystemView()
    {
        localView  = FileSystemView.getFileSystemView();
        remoteView = VFSView.getFileSystemView();
    }
}