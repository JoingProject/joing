/*
 * VFSView.java
 * 
 * Created on 04-ago-2007, 19:33:45
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
package org.joing.runtime;

import java.io.FileFilter;
import java.io.IOException;
import javax.swing.filechooser.FileSystemView;

/**
 * A FileSystemView that works with Join'g VFS.
 * <p>
 * This class handles (hide: do not report them) the JoingVFSExceptions.
 * 
 * @author Francisco Morero Peyrona
 */
public class VFSView extends FileSystemView
{
    private static VFSView   instance = null;
    private static VFSFile[] afRoot   = null;
    
    //------------------------------------------------------------------------//
    
    /**
     * Singleton constructor.
     * <p>
     * This method is thread safe
     * 
     * @return The only instance of this class.
     */
    public static VFSView getFileSystemView()
    {
        if( instance == null )
        {
            synchronized( VFSView.class )
            {
                if( instance == null )
                    instance = new VFSView();
            }
        }
        
        return instance;
    }
    
    /**
     * @see javax.swing.filechooser.FileSystemView#createFileObject( 
     *                                 java.io.File containingDir, String name )
     */
    @Override
    public VFSFile createFileObject( java.io.File parentDir, String name ) 
    {
        return new VFSFile( parentDir.getAbsolutePath(), name );
    }
    
    /**
     * @see javax.swing.filechooser.FileSystemView#createFileObject(String path)
     */
    @Override
    public VFSFile createFileObject( String path )
    {
        return new VFSFile( path );
    }
    
    /**
     * @see javax.swing.filechooser.FileSystemView#createNewFolder( 
     *                                              java.io.File containingDir )
     */
    public VFSFile createNewFolder( java.io.File dir ) throws IOException
    {
        throw new UnsupportedOperationException( "Not supported yet." );
    }
    
    /**
     * @see javax.swing.filechooser.FileSystemView#getFiles( 
     *                           java.io.File directory, boolean useFileHiding )
     */    
    @Override
    public VFSFile[] getFiles( java.io.File directory, boolean useFileHiding )
    {
        VFSFile[] aFiles = new VFSFile[0];
        
        if( directory instanceof VFSFile )
        {
            VFSFile fVFS = (VFSFile) directory;
            
            if( useFileHiding )
            {
                FileFilter filter = new FileFilter()
                    {
                        public boolean accept( java.io.File f ) 
                        {
                            return ((VFSFile) f).isDirectory();
                        }
                    };
                
                aFiles = fVFS.listFiles( filter );
            }
            else
            {
                aFiles = fVFS.listFiles();
            }
        }
        
        return aFiles;
    }
    
    /**
     * @see javax.swing.filechooser.FileSystemView#getHomeDirectory()
     */
    @Override
    public VFSFile getHomeDirectory()
    {
	return getRoots()[0];
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
     * @see javax.swing.filechooser.FileSystemView#getRoots()
     */
    @Override
    public VFSFile[] getRoots()
    {
        VFSFile[] aRet = null;    // By contract has to return null instead of empty array
        
        if( afRoot == null )
            afRoot = VFSFile.listRoots();
        
        if( afRoot != null )
        {
            // Defensive copy
            aRet = new VFSFile[ afRoot.length ];
            System.arraycopy( afRoot, 0, aRet, 0, afRoot.length );
        }
        
        return aRet;
    }
    
    /**
     * @see javax.swing.filechooser.FileSystemView#getSystemDisplayName( java.io.File f )
     */
    @Override
    public String getSystemDisplayName( java.io.File f )
    {
        String s = f.getName();
        
        if( isRoot( f ) )
            s += " (Joing)";
        
        return s;
    }
    
    /**
     * @see javax.swing.filechooser.FileSystemView#isComputerNode( java.io.File dir )
     */
    @Override
    public boolean isComputerNode( java.io.File dir )
    {
        return isRoot( dir );
    }
    
    /**
     * @see javax.swing.filechooser.FileSystemView#isDrive( java.io.File dir )
     */
    @Override
    public boolean isDrive( java.io.File dir )
    {
        return isRoot( dir );
    }
    
    /**
     * @see javax.swing.filechooser.FileSystemView#isFileSystem( java.io.File f )
     */
    @Override
    public boolean isFileSystem( java.io.File f )
    {
        return true;    // In VFS all files are "real"
    }
    
    /**
     * @see javax.swing.filechooser.FileSystemView#isFileSystemRoot( java.io.File dir )
     */
    @Override
    public boolean isFileSystemRoot( java.io.File dir )
    {
        return isRoot( dir );
    }
    
    /**
     * @see javax.swing.filechooser.FileSystemView#isFloppyDrive( java.io.File dir )
     */
    @Override
    public boolean isFloppyDrive( java.io.File dir )
    {
        return false;    // In VFS there is no "drive"
    }
    
    /**
     * @see javax.swing.filechooser.FileSystemView#isHiddenFile(java.io.File file)
     */
    @Override
    public boolean isHiddenFile( java.io.File file )
    {
        return file.isHidden();
    }
    
    /**
     * @see javax.swing.filechooser.FileSystemView#isParent( java.io.File folder, java.io.File file )
     */
    @Override
    public boolean isParent( java.io.File folder, java.io.File file )
    {
        return false;  // Only in Windows _same_ file can be in more than one directory
    }
    
    /**
     * @see javax.swing.filechooser.FileSystemView#isRoot( java.io.File file )
     */
    @Override
    public boolean isRoot( java.io.File file )
    {
        for( int n = 0; n < afRoot.length; n++ )
        {
            if( afRoot[n] == file )       // Both are the same object (have same reference)
                return true;
        }
        
        return false;
    }
    
    /**
     * @see javax.swing.filechooser.FileSystemView#isTraversable( java.io.File file )
     */
    @Override
    public Boolean isTraversable( java.io.File file )
    {
	return Boolean.valueOf( file.isDirectory() );
    }
    
    //------------------------------------------------------------------------//
    
    public VFSView()
    {
    }
}