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
package org.joing.runtime.vfs;

import java.io.FileFilter;
import java.io.IOException;
import javax.swing.Icon;
import javax.swing.filechooser.FileSystemView;
import org.joing.common.dto.vfs.FileDescriptor;
import org.joing.jvmm.RuntimeFactory;

/**
 * A FileSystemView that works with Join'g VFS.
 * <p>
 * This class handles (hide: do not report them) the JoingVFSExceptions.
 * 
 * @author Francisco Morero Peyrona
 */
public class VFSView
{
    /**
     * The system-dependent default name-separator character.  This field is
     * initialized to contain the first character of the value of the system
     * property <code>file.separator</code>. Join'g uses same as UNIX systems, 
     * the value of this field is <code>'/'</code>.
     *
     * @see     java.lang.System#getProperty(java.lang.String)
     */
    public static final char separatorChar = '/';

    /**
     * The system-dependent default name-separator character, represented as a
     * string for convenience.  This string contains a single character, namely
     * <code>{@link #separatorChar}</code>.
     */
    public static final String separator = "" + separatorChar;

    /**
     * The system-dependent path-separator character.  This field is
     * initialized to contain the first character of the value of the system
     * property <code>path.separator</code>.  This character is used to
     * separate filenames in a sequence of files given as a <em>path list</em>.
     * Join'g uses same as UNIX systems, this character is <code>':'</code>.
     *
     * @see     java.lang.System#getProperty(java.lang.String)
     */
    public static final char pathSeparatorChar = ':';

    /**
     * The system-dependent path-separator character, represented as a string
     * for convenience.  This string contains a single character, namely
     * <code>{@link #pathSeparatorChar}</code>.
     */
    public static final String pathSeparator = "" + pathSeparatorChar;
    
    //------------------------------------------------------------------------//
    
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
    public static synchronized VFSView getFileSystemView()
    {
        if( instance == null )
            instance = new VFSView();
        
        return instance;
    }
    
    /**
     * @see javax.swing.filechooser.FileSystemView#createFileObject( 
     *                                 java.io.File containingDir, String name )
     */
    public VFSFile createFileObject( VFSFile vfsParentDir, String name ) 
    {
        return ((vfsParentDir == null) ? new VFSFile( name ) : 
                                         new VFSFile( vfsParentDir, name ));
    }
    
    /**
     * @see javax.swing.filechooser.FileSystemView#createFileObject(String path)
     */
    public VFSFile createFileObject( String path )
    {
        /*TODO: decidir qué hacer con esto
        int    nIndex  = path.lastIndexOf( VFSView.pathSeparatorChar );
        String sFolder = path.;
        String  sName  = path.;

        FileDescriptor fdNewFile = RuntimeFactory.getPlatform().getBridge().getFileBridge().createFile( sFolder, null );
                       fdNewFile.setName( sName );
                       fdNewFile = RuntimeFactory.getPlatform().getBridge().getFileBridge().update( fdNewFile );*/
        
        return new VFSFile( path );
    }
    
    /**
     * @see javax.swing.filechooser.FileSystemView#createNewFolder( 
     *                                              java.io.File containingDir )
     */
    public VFSFile createNewFolder( VFSFile vfsParentDir ) throws IOException
    {
        if( vfsParentDir == null )
            throw new IOException( "Parent directory is null." );
        
        if( ! vfsParentDir.isDirectory() )
            throw new IllegalArgumentException( "Parent is not a directory." );
        
        VFSFile vfsNewFolder = null;
        
        if( ! vfsParentDir.exists() )    // The directory itself does not exists
        {
            vfsNewFolder = createFileObject( vfsParentDir.getAbsolutePath() );
            vfsNewFolder = (vfsNewFolder.mkdir() ? vfsNewFolder : null);
        }
        else                    // A new directory has to be created inside passed dir
        {
            // For speed, this operation has to be done on the server side
            String         sParent  = vfsParentDir.getAbsolutePath();
            FileDescriptor fdNewDir = RuntimeFactory.getPlatform().getBridge().getFileBridge().createDirectory( sParent, null );
            vfsNewFolder = new VFSFile( fdNewDir );
        }
        
        return vfsNewFolder;
    }
    
    // "Normal" (local) files are created in a wired way: an instance of File is 
    // creted but the physical file is not created until needed. But Virtual
    // files must exists after invocation to the server; that's why I add this 
    // new method (therefore it is used only by VFS).
    public VFSFile createNewFile( VFSFile vfsParentDir ) throws IOException
    {
        if( vfsParentDir == null )
            throw new IOException( "Parent directory is null." );
        
        String         sParent   = vfsParentDir.getAbsolutePath();
        FileDescriptor fdNewFile = RuntimeFactory.getPlatform().getBridge().getFileBridge().createFile( sParent, null, false );
        
        return new VFSFile( fdNewFile );
    }
    
    /**
     * @see javax.swing.filechooser.FileSystemView#getChild(java.io.File parent,
     *                                                      String fileName)
     */
    public VFSFile getChild( VFSFile vfsParentDir, String fileName )
    {
        return createFileObject( vfsParentDir, fileName );
    }
    
    /**
     * @see javax.swing.filechooser.getDefaultDirectory()
     */
    public VFSFile getDefaultDirectory()
    {
        return getHomeDirectory();
    }
    
    /**
     * @see javax.swing.filechooser.FileSystemView#getFiles( 
     *                           java.io.File directory, boolean useFileHiding )
     */
    // IMPORTANT: It is needed to return an empty array instead of null.
    public VFSFile[] getFiles( VFSFile vfsParentDir, boolean bIncludeHidden )
    {
        VFSFile[] aFiles = null;
            
        if( bIncludeHidden )
            aFiles = vfsParentDir.listFiles();
        else
            aFiles = vfsParentDir.listFiles( new FilterExcludeHidden() );
        
        return (aFiles == null ? new VFSFile[0] : aFiles);
    }
    
    /**
     * @see javax.swing.filechooser.FileSystemView#getHomeDirectory()
     */
    public VFSFile getHomeDirectory()
    {
	return getRoots()[0];
    }
    
    /**
     * @see javax.swing.filechooser.FileSystemView#getParentDirectory( 
     *                                                  java.io.File directory )
     */
    public VFSFile getParentDirectory( VFSFile dir )
    {
        VFSFile fParent = null;
        
        if( dir != null )
        {
            if( isRoot( dir ) )
                fParent = dir;
            else
                fParent = new VFSFile( dir.getParent() );
        }
        
        return fParent;
    }
    
    /**
     * @see javax.swing.filechooser.FileSystemView#getRoots()
     */
    // By contract has to return null instead of empty array
    public VFSFile[] getRoots()
    {
        VFSFile[] aRet = null;
        
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
    public String getSystemDisplayName( VFSFile file )
    {
        String sName = null;
        
        if( file != null )
        {
            sName = file.getName();

            if( isRoot( file ) )
                sName += " (Remote)";   // TODO: Averiguar de dónde sacar el nombre del sistema
            else if( file.isLink() )
                sName += " ^";
        }
        
        return sName;
    }
    
    public Icon getSystemIcon( VFSFile vfs )
    {
        return (new VFSIconMapper()).getIcon( vfs );
    }
    
    public String getSystemTypeDescription( VFSFile vfs ) 
    {
        return vfs.getAbsolutePath();
    }
    
    /**
     * @see javax.swing.filechooser.FileSystemView#isComputerNode( java.io.File dir )
     */
    public boolean isComputerNode( VFSFile vfsDir )
    {
        return isRoot( vfsDir );
    }
    
    /**
     * @see javax.swing.filechooser.FileSystemView#isDrive( java.io.File dir )
     */
    public boolean isDrive( VFSFile file )
    {
        return isRoot( file );
    }
    
    /**
     * @see javax.swing.filechooser.FileSystemView#isFileSystem( java.io.File f )
     */
    public boolean isFileSystem( VFSFile file )
    {
        return true;    // In VFS all files are "real"
    }
    
    /**
     * @see javax.swing.filechooser.FileSystemView#isFileSystemRoot( java.io.File dir )
     */
    public boolean isFileSystemRoot( VFSFile file )
    {
        return isRoot( file );
    }
    
    /**
     * @see javax.swing.filechooser.FileSystemView#isFloppyDrive( java.io.File dir )
     */
    public boolean isFloppyDrive( VFSFile file )
    {
        return false;    // In VFS there is no "drives": there is only one file system
    }
    
    /**
     * @see javax.swing.filechooser.FileSystemView#isHiddenFile(java.io.File file)
     */
    public boolean isHiddenFile( VFSFile file )
    {
        return file.isHidden();
    }
    
    /**
     * @see javax.swing.filechooser.FileSystemView#isParent( java.io.File folder, java.io.File file )
     */
    public boolean isParent( VFSFile folder, VFSFile file )
    {
        boolean bParent = false;
        
        if( folder != null && file != null && folder.isDirectory() )
        {
            String sParentPath = folder.getPath();
            String sFilePath   = file.getParent();
            
	    bParent = sParentPath != null && sParentPath.equals( sFilePath );
        }
        
        return bParent;
    }
    
    /**
     * @see javax.swing.filechooser.FileSystemView#isRoot( java.io.File file )
     */
    public boolean isRoot( VFSFile file )
    {
        return ((file != null) && (file.getParent() == null));
    }
    
    /**
     * @see javax.swing.filechooser.FileSystemView#isTraversable( java.io.File file )
     */
    public Boolean isTraversable( VFSFile file )
    {
	return Boolean.valueOf( file.isDirectory() );
    }
    
    //------------------------------------------------------------------------//
    
    private VFSView()
    {
    }
    
    //------------------------------------------------------------------------//
    
    private final class FilterExcludeHidden implements FileFilter
    {
        public boolean accept( java.io.File f ) 
        {
            return ! (f.isHidden());
        }
    }
}