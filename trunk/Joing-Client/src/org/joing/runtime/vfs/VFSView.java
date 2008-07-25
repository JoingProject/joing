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
import javax.swing.UIManager;
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

    private static final String sNewFolder     = UIManager.getString( "FileChooser.other.newFolder" );
    private static final String sNewFolderNext = UIManager.getString( "FileChooser.other.newFolder.subsequent" );
    
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
        synchronized( VFSView.class )
        {
            if( instance == null )
                instance = new VFSView();
        }
        
        return instance;
    }
    
    /**
     * @see javax.swing.filechooser.FileSystemView#createFileObject( 
     *                                 java.io.File containingDir, String name )
     */
    public VFSFile createFileObject( java.io.File parentDir, String name ) 
    {
        VFSFile vfs = null;
        
        if( parentDir != null )
        {
            if( parentDir instanceof VFSFile )
                vfs = new VFSFile( (VFSFile) parentDir, name );
            else
                vfs = new VFSFile( parentDir.getAbsolutePath(), name );
        }
        else
        {
            vfs = new VFSFile( name );
        }
        
        return vfs;
    }
    
    /**
     * @see javax.swing.filechooser.FileSystemView#createFileObject(String path)
     */
    public VFSFile createFileObject( String path )
    {
        return new VFSFile( path );
    }
    
    /**
     * @see javax.swing.filechooser.FileSystemView#createNewFolder( 
     *                                              java.io.File containingDir )
     */
    public VFSFile createNewFolder( java.io.File parent ) throws IOException
    {
        if( parent == null )
            throw new IOException( "Containing directory is null." );
        
        if( ! parent.isDirectory() )
            throw new IllegalArgumentException( "Parent is not a directory." );
        
        VFSFile vfsNewFolder = null;
        
        if( ! parent.exists() )    // The directory itself does not exists
        {
            vfsNewFolder = createFileObject( parent.getAbsolutePath() );
        }
        else                    // A new directory has to be created inside passed dir
        {
            vfsNewFolder = createFileObject( parent, sNewFolder );
            
            if( vfsNewFolder.exists() )
                vfsNewFolder = createFileObject( parent, sNewFolder +"."+ System.currentTimeMillis() );  // This name can't exists
        }
        
        return (vfsNewFolder.mkdir() ? vfsNewFolder : null);
    }
    
    /**
     * @see javax.swing.filechooser.FileSystemView#getChild(java.io.File parent,
     *                                                      String fileName)
     */
    public java.io.File getChild( java.io.File parent, String fileName )
    {
        return createFileObject( parent, fileName );
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
    public VFSFile[] getFiles( java.io.File directory, boolean bIncludeHidden )
    {
        VFSFile[] aFiles = null;
        
        if( directory instanceof VFSFile )
        {
            VFSFile fVFS = (VFSFile) directory;
            
            if( bIncludeHidden )
                aFiles = fVFS.listFiles();
            else
                aFiles = fVFS.listFiles( new FilterExcludeHidden() );
        }
        
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
    public VFSFile getParentDirectory( java.io.File directory )
    {
        VFSFile fParent = null;
        
        if( directory != null )
        {
            VFSFile dir = (VFSFile) directory;
            
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
    public String getSystemDisplayName( java.io.File file )
    {
        String  sName = null;
        
        if( file != null )
        {
            VFSFile fVFS  = (VFSFile) file;

            sName = fVFS.getName();

            if( isRoot( fVFS ) )
                sName += " ("+ "n/a" +")";   // FIXME Averiguar de dónde sacar el nombre del sistema
            else if( fVFS.isLink() )
                sName += " ^";
        }
        
        return sName;
    }
    
    // NEXT: Si quiero, puedo poner mis propios iconos para los fic de Joing
    public Icon getSystemIcon( java.io.File f )
    {
        return super.getSystemIcon( f );
    }
    
    public String getSystemTypeDescription( java.io.File file ) 
    {
        return file.getAbsolutePath();
    }
    
    /**
     * @see javax.swing.filechooser.FileSystemView#isComputerNode( java.io.File dir )
     */
    public boolean isComputerNode( java.io.File dir )
    {
        return isRoot( dir );
    }
    
    /**
     * @see javax.swing.filechooser.FileSystemView#isDrive( java.io.File dir )
     */
    public boolean isDrive( java.io.File dir )
    {
        return false;  // FIXME: Mirar si es mejor devolver false o esto: isRoot( dir );
    }
    
    /**
     * @see javax.swing.filechooser.FileSystemView#isFileSystem( java.io.File f )
     */
    public boolean isFileSystem( java.io.File f )
    {
        return true;    // In VFS all files are "real"
    }
    
    /**
     * @see javax.swing.filechooser.FileSystemView#isFileSystemRoot( java.io.File dir )
     */
    public boolean isFileSystemRoot( java.io.File dir )
    {
        return isRoot( dir );
    }
    
    /**
     * @see javax.swing.filechooser.FileSystemView#isFloppyDrive( java.io.File dir )
     */
    public boolean isFloppyDrive( java.io.File dir )
    {
        return false;    // In VFS there is no "drive"
    }
    
    /**
     * @see javax.swing.filechooser.FileSystemView#isHiddenFile(java.io.File file)
     */
    public boolean isHiddenFile( java.io.File file )
    {
        return file.isHidden();
    }
    
    /**
     * @see javax.swing.filechooser.FileSystemView#isParent( java.io.File folder, java.io.File file )
     */
    public boolean isParent( java.io.File folder, java.io.File file )
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
    public boolean isRoot( java.io.File file )
    {
        if( file != null )
            return ((VFSFile) file).getParent() == null;
        
        return false;
    }
    
    /**
     * @see javax.swing.filechooser.FileSystemView#isTraversable( java.io.File file )
     */
    public Boolean isTraversable( java.io.File file )
    {
	return Boolean.valueOf( file.isDirectory() );
    }
    
    //------------------------------------------------------------------------//
    
    private VFSView()
    {
    }
    
    private final class FilterExcludeHidden implements FileFilter
    {
        public boolean accept( java.io.File f ) 
        {
            return ! (f.isHidden());
        }
    }
}