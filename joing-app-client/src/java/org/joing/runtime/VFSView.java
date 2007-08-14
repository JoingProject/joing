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

import java.io.IOException;
import javax.swing.filechooser.FileSystemView;

/**
 * A FileSystemView that works with Join'g VFS.
 * 
 * @author Francisco Morero Peyrona
 */
public class VFSView extends FileSystemView
{
    private static VFSFile[] afRoot = new VFSFile[0];
    
    //------------------------------------------------------------------------//
    
    public VFSView()
    {
    }
    
    /**
     * @see javax.swing.filechooser.FileSystemView#createFileObject( 
     *                                 java.io.File containingDir, String name )
     */
    @Override
    public java.io.File createFileObject( java.io.File containingDir, String name ) 
    {
        throw new UnsupportedOperationException( "Not supported yet." );
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
    public java.io.File createNewFolder( java.io.File containingDir ) throws IOException
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
        throw new UnsupportedOperationException( "Not supported yet." );
    }
    
    /**
     * @see javax.swing.filechooser.FileSystemView#getHomeDirectory()
     */
    @Override
    public java.io.File getHomeDirectory()
    {
        throw new UnsupportedOperationException( "Not supported yet." );
    }
    
    /**
     * @see javax.swing.filechooser.FileSystemView#getParentDirectory( 
     *                                                  java.io.File directory )
     */
    @Override
    public java.io.File getParentDirectory( java.io.File directory )
    {
        throw new UnsupportedOperationException( "Not supported yet." );
    }
    
    /**
     * @see javax.swing.filechooser.FileSystemView#getRoots()
     */
    @Override
    public java.io.File[] getRoots()
    {
        if( afRoot == null )
            afRoot = (VFSFile[]) VFSFile.listRoots();
        
        VFSFile[] aRet = new VFSFile[ afRoot.length ];
        System.arraycopy( afRoot, 0, aRet, 0, afRoot.length );
        
        return aRet;
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
     * @see javax.swing.filechooser.FileSystemView#isRoot( java.io.File file )
     */
    @Override
    public boolean isRoot( java.io.File file )
    {
        for( int n = 0; n < afRoot.length; n++ )
            if( afRoot[n] == file )       // Both are the same object (have same reference)
                return true;
        
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
}