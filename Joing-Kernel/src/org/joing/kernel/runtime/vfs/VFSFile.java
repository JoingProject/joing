/*
 * VfsFile.java
 *
 * Created on 06-ago-2007, 21:24:54
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
package org.joing.kernel.runtime.vfs;

import org.joing.common.dto.vfs.*;
import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import org.joing.kernel.api.bridge.VFSBridge;
import org.joing.common.dto.user.User;
import org.joing.common.exception.JoingServerVFSException;

/**
 * This class is needed to build <code>JoingFileSystemView</code>
 * 
 * @author Francisco Morero Peyrona
 */
public class VFSFile extends VFSFileBase
{    
    private static final long serialVersionUID = 1L;    // TODO: cambiarlo usando: serialver -show
    
    private static long nTotalDiskSpace = -1;
    
    //------------------------------------------------------------------------//
    
    /**
     * Creates a new instance of <code>VFSFile</code>.
     * The parent or file name must start with root ("/").
     * <p>
     * It is recommended that directory names end with file separator ("/").
     * 
     * @param parent Parent directory. If null, toot ('/') is assumed.
     * @param child  File (or folder) name. Can't be null.
     */
    public VFSFile( String parent, String child )
    {
        super( parent, child );
        
        if( parent == null )
            parent = separator;
        else
            parent.trim();
        
        if( ! parent.startsWith( separator ) )
            parent = separator + parent;
        
        if( ! parent.endsWith( separator ) )
            parent = parent + separator;
        
        if( child.charAt( 0 ) == separatorChar )
        {
            if( child.length() > 1 )
                child = child.substring( 1 );
            else
                child = "";    // it was just "/"
        }
        
        initFromServer( parent + child );
    }
    
    /**
     * Creates a new instance of <code>VFSFile</code>.
     * The directory or file name must start with root ("/").
     * <p>
     * It is recommended that directory names end with file separator ("/").
     * 
     * @param sFullName Full directory or file name.
     */
    public VFSFile( String sFullName ) throws JoingServerVFSException
    {
        super( sFullName );
        initFromServer( sFullName );
    }
    
    /**
     * Creates a new instance of <code>VFSFile</code>.
     *
     * @param parent Parent directory.
     * @param child  Directory or file name.
     */
    public VFSFile( VFSFile parent, String child ) throws JoingServerVFSException
    {
        super( (parent == null ? "" : parent.getAbsolutePath()), child );
        
        if( parent == null )
            throw new NullPointerException( "Parent directory can't be null." );
        
        if( ! parent.isDirectory() )
            throw new IllegalArgumentException( "Parent must be a directory." );
        
        initFromServer( parent.getAbsolutePath() + separator + child );
    }
    
    public VFSFile( VFSFileBase fBase )
    {
        super( fBase.getParent(), fBase.getName() );
        updateFrom( fBase );
    }
    
    //------------------------------------------------------------------------//
    
    @Override
    public int compareTo( File pathname )
    {
        int nValue = 1;
        
        if( pathname.exists() )
        {
            VFSFile file = (VFSFile) pathname;
        
            if( exists() )
                return -1;
            else
                return this.getAbsolutePath().compareTo( file.getAbsolutePath() );
        }
        else
        {
            if( ! exists() )   // None of both exists
                nValue = 0;
        }
        
        return nValue;
    }
    
    @Override
    public boolean createNewFile() throws JoingServerVFSException
    {
        createIt( false );
        return exists();
    }
    
    @Override
    public boolean delete() throws JoingServerVFSException
    {
        boolean bSuccess = false;
        
        if( exists() )
        {
            List<VFSFile> lstErrors = org.joing.kernel.jvmm.RuntimeFactory.getPlatform().getBridge().
                                          getFileBridge().delete( this );   // NEXT: Se pueden devolver los Ids de error: delete(...) los devuelve
            if( lstErrors.size() == 0 )
            {
                setHandler( -1 );    // exists() == false
                bSuccess = true;
            }
        }
        
        return bSuccess;
    }
    
    @Override
    public void deleteOnExit()
    {
        // NEXT: Implementarlo (se puede enviar un msg al servidor para que los 
        //       borre al cerrar la sesion (habría que revisarlos tb cuando 
        //       se comprueba si una sesion se ha quedado aboerta y se cierra
        //       medinate el timer).
        throw new UnsupportedOperationException( "Not supported yet." );
    }
    
    @Override
    public boolean equals( Object obj )
    {
        boolean bEquals = false;
        
        if( obj instanceof VFSFile )
        {
            VFSFile file = (VFSFile) obj;
            
            return file.exists() &&
                   this.exists() &&
                   file.getHandler() == this.getHandler();
        }
        
        return bEquals;
    }
    
    /**
     * @see java.io.File#exists()
     */
    @Override
    public boolean exists()
    {
        return (getHandler() > -1);
    }
    
    /**
     * As instances of <code>VFSFile</code> are always absolute, this method 
     * always return <code>this</code>.
     * 
     * @see java.io.File#getAbsoluteFile()
     */
    @Override
    public VFSFile getAbsoluteFile()
    {
        // NEXT: Should I return a clone?
        return this; // VFSs are always absolute
    }
    
    /**
     * @see java.io.File#getCanonicalFile()
     */
    @Override
    public VFSFile getCanonicalFile()
    {
        return getAbsoluteFile();
    }

    /**
     * @see java.io.File#getCanonicalPath()
     */
    @Override
    public String getCanonicalPath()
    {
        return getAbsolutePath();
    }
    
    /**
     * @see java.io.File#getFreeSpace()
     */
    @Override
    public long getFreeSpace() throws JoingServerVFSException
    {// NEXT: Hay que avriguar un modo de refrescar esto sólo cuando sea necesario
        long nFree = -1;
        User user  = org.joing.kernel.jvmm.RuntimeFactory.getPlatform().getBridge().getUserBridge().getUser();
        
        if( user != null )
        {
            if( nTotalDiskSpace == -1 )
                nTotalDiskSpace = user.getTotalSpace();

            nFree = nTotalDiskSpace - user.getUsedSpace();
        }
        
        return nFree;
    }
    
    /**
     * @see java.io.File#getParentFile()
     */
    @Override
    public VFSFile getParentFile() throws JoingServerVFSException
    {
        VFSFile fParent = null;
        String  sParent = getParent();
        
        // If exists, then parent == null only when is root
        if( exists() && sParent != null && sParent.length() > 0 )
        {
            new VFSFile( sParent );
        }
        
        return fParent;
    }
    
    /**
     * @see java.io.File#getPath()
     */
    @Override
    public String getPath()
    {
        return getAbsolutePath();
    }
    
    /**
     * @see java.io.File#getTotalSpace()
     */
    @Override
    public long getTotalSpace() throws JoingServerVFSException
    {
        if( nTotalDiskSpace == -1 )
        {
            synchronized( this )
            {
                if( nTotalDiskSpace == -1 )
                {
                    User user = org.joing.kernel.jvmm.RuntimeFactory.getPlatform().getBridge().
                                    getUserBridge().getUser();
                    
                    if( user != null )
                        nTotalDiskSpace = user.getTotalSpace();
                }
            }
        }
        
        return nTotalDiskSpace;
    }
    
    /**
     * @see java.io.File#getUsableSpace()
     */
    @Override
    public long getUsableSpace()
    {
        return getFreeSpace();
    }
    
    /**
     * @see java.io.File#hashCode()
     */
    @Override
    public int hashCode()
    {
        return super.hashCode() + getHandler();
    }
    
    /**
     * @see java.io.File#isAbsolute()
     */
    @Override
    public boolean isAbsolute()
    {
        return true;  // VFSFile are always absolute (must start with '/')
    }
    
    /**
     * @see java.io.File#isFile()
     */
    @Override
    public boolean isFile()
    {
        return (! isDirectory());
    }
    
    public boolean isLink()
    {
        return false;    // TODO: implementarlo cuando haya aclarado cómo va lo de los links
    }
    
    /**
     * @see java.io.File#list()
     */
    @Override
    // By contract, must return null if list is empty instead of an empty list
    public String[] list() throws JoingServerVFSException
    {
        String[] asName = null;

        if( isDirectory() )
        {
            List<VFSFile> lstChilds = getChildren();

            if( lstChilds.size() > 0 )
            {
                asName = new String[ lstChilds.size() ];

                for( int n = 0; n < asName.length; n++ )
                    asName[n] = lstChilds.get( n ).getName();
            }
        }
        
        return asName;
    }
    
    @Override
    // By contract, must return null if list is empty instead of an empty list
    public String[] list( FilenameFilter filter ) throws JoingServerVFSException
    {
        File[]   af = listFiles( filter );
        String[] as = null;
        
        if( af != null )
        {
            as = new String[ af.length ];
            
            for( int n = 0; n < af.length; n++ )
                as[n] = af[n].getName();
        }
        
        return as;
    }
    
    /**
     * @see java.io.File#listFiles()
     */
    @Override
    // By contract, must return null if list is empty instead of an empty list
    public VFSFile[] listFiles() throws JoingServerVFSException
    {
        List<VFSFile> lstChildren = getChildren();
        
        return (lstChildren == null ? null : lstChildren.toArray( new VFSFile[0] ));
    }
    
    @Override
    // By contract, must return null if list is empty instead of an empty list
    public VFSFile[] listFiles( FileFilter filter ) throws JoingServerVFSException
    {
        VFSFile[] aFile = listFiles();
        
        if( aFile != null )
        {
            ArrayList<VFSFile> lstFilteredFiles = new ArrayList<VFSFile>( aFile.length );
            
            for( int n = 0; n < aFile.length; n++ )
            {
                if( filter.accept( aFile[n] ) )
                    lstFilteredFiles.add( aFile[n] );
            }
            
            aFile = lstFilteredFiles.toArray( new VFSFile[0] );
        }
        
        return aFile;
    }
    
    @Override
    // By contract, must return null if list is empty instead of an empty list
    public VFSFile[] listFiles( FilenameFilter filter ) throws JoingServerVFSException
    {
        VFSFile[] aFile = listFiles();
        
        if( aFile != null )
        {
            ArrayList<VFSFile> lstFilteredFiles = new ArrayList<VFSFile>( aFile.length );
            VFSFile            vfsParent        = getParentFile();
            
            for( int n = 0; n < aFile.length; n++ )
            {
                if( filter.accept( vfsParent, aFile[n].getName() ) )
                    lstFilteredFiles.add( aFile[n] );
            }
            
            aFile = lstFilteredFiles.toArray( new VFSFile[0] );
        }
        
        return aFile;
    }
    
    /**
     * @see java.io.File#listRoots()
     */
    // By contract has to return null instead of empty array
    public static synchronized VFSFile[] listRoots() throws JoingServerVFSException
    {
        List<VFSFile> lstRoots = org.joing.kernel.jvmm.RuntimeFactory.getPlatform().getBridge().
                                     getFileBridge().getRoots();
        
        return (lstRoots == null ? null : lstRoots.toArray( new VFSFile[0] ));
    }

    /**
     * @see java.io.File#mkdir()
     */
    @Override
    public boolean mkdir() throws JoingServerVFSException
    {
        createIt( true );
        return exists();
    }
    
    /**
     * @see java.io.File#mkdirs()
     */
    @Override
    public boolean mkdirs() throws JoingServerVFSException
    {
        VFSFileBase file = org.joing.kernel.jvmm.RuntimeFactory.getPlatform().getBridge().
                               getFileBridge().createDirectories( getAbsolutePath() );
        return (file != null);
    }
    
    @Override
    public boolean renameTo( File fNewName )
    {
        VFSFile fTemp    = null;
        boolean bSuccess = false;
        
        if( exists() && fNewName != null && fNewName.getName() != null )
        {
            if( ! getName().equals( fNewName.getName() ) )
            {
                String sOldName = getName();
                
                setName( fNewName.getName() );
                fTemp = org.joing.kernel.jvmm.RuntimeFactory.getPlatform().getBridge().
                            getFileBridge().update( this );
                bSuccess = true;
                
                if( fTemp == null )
                {
                    setName( sOldName );
                    bSuccess = false;
                }
            }
            else     // Old name and new name are the same => Nothing to do
            {
                bSuccess = true;
            }
        }
        
        return bSuccess;
    }
    
    /**
     * @see java.io.File#setExecutable( boolean executable, boolean ownerOnly )
     */
    @Override
    public boolean setExecutable( boolean executable, boolean ownerOnly )
    {
        return setExecutable( executable );   // TODO: mirar si se puede tener en cuenta el ownerOnly
    }
    
    /**
     * As this information is managed by the Server, this method always return 
     * <code>false</code>.
     * 
     * @see java.io.File#setLastModified( long time )
     */
    @Override
    public boolean setLastModified( long time )
    {
        return false;   // It is managed by the Server
    }
    
    /**
     * @see java.io.File#setReadable( boolean readable, boolean ownerOnly )
     */
    @Override
    public boolean setReadable( boolean readable, boolean ownerOnly )
    {
        return setReadable( readable );    // TODO: mirar si se puede tener en cuenta el ownerOnly
    }
    
    /**
     * @see java.io.File#setReadOnly()
     */
    @Override
    public boolean setReadOnly()
    {
        return setWritable( false );
    }
    
    /**
     * @see java.io.File#setWritable( boolean writable )
     */
    @Override
    public boolean setWritable( boolean writable )
    {
        // TODO Hacerlo
        throw new UnsupportedOperationException( "Not supported yet." );
    }
    
    /**
     * @see java.io.File#setWritable( boolean writable, boolean ownerOnly )
     */
    @Override
    public boolean setWritable( boolean writable, boolean ownerOnly )
    {
        return setWritable( writable );   // TODO: mirar si se puede tener en cuenta el ownerOnly
    }
    
    /**
     * Updates (at Server side) all changed attributes in one operation.
     * 
     * This method should be used when one oer more atrributes (Executable,
     * Readdable, etc.) are changed.
     * 
     * @throws org.joing.common.exception.JoingServerVFSException
     */
    public void updateAttributes() throws JoingServerVFSException
    {
        VFSFileBase file = org.joing.kernel.jvmm.RuntimeFactory.getPlatform().getBridge().
                               getFileBridge().update( this );
        updateFrom( file );
    }
    
    /**
     * @see java.io.File#toString()
     */
    @Override
    public String toString() 
    {
        return getAbsolutePath();
    }
    
    /**
     * @see java.io.File#toURI()
     */
    @Override
    public URI toURI()
    {
        // TODO Hacerlo
        throw new UnsupportedOperationException( "Not supported yet." );
    }
    
    //------------------------------------------------------------------------//
    // Methods that exists with other names in super-class and has to be overwritten from java.io.File
    
    /**
     * Exists to keep compatibility with java.io.File::lastModified().<br>
     * It is equivalent to super.getModified().
     * 
     * @see java.io.File#lastModified()
     * @see VFSFileBase#getModified()
     */
    @Override
    public long lastModified()
    {
        return super.getModified();
    }
    
    /**
     * Exists to keep compatibility with java.io.File::length().<br>
     * It is  equivalent to super.getSize().
     * 
     * @see java.io.File#length()
     * @see VFSFileBase#getSize()
     */
    @Override
    public long length()
    {
        return super.getSize();
    }
    
    /**
     * Exists to keep compatibility with java.io.File::canExecute().<br>
     * It is  equivalent to super.isExecutable().
     * 
     * @see java.io.File#length()
     * @see VFSFileBase#getSize()
     */
    @Override
    public boolean canExecute()
    {
        return isExecutable();
    }
    
    /**
     * Exists to keep compatibility with java.io.File::canRead().<br>
     * It is  equivalent to super.isReadable().
     * 
     * @see java.io.File#length()
     * @see VFSFileBase#getSize()
     */
    @Override
    public boolean canRead()
    {
        return isReadable();
    }
    
    /**
     * Exists to keep compatibility with java.io.File::canWrite().<br>
     * It is  equivalent to super.isReadable() (in case of directory) and
     * super.isModifiable() (in case of file).
     * 
     * @see java.io.File#length()
     * @see VFSFileBase#getSize()
     */
    @Override
    // A directory can always be written (add files and sub-dirs)
    public boolean canWrite()
    {
        return (isDirectory() ? isReadable() : isModifiable());
    }
    
    //------------------------------------------------------------------------//
    
    /**
     * Creates an instace of VFSFile reading the file properties form server.
     * 
     * @param sFullname Path and file name (starting at root '/')
     */
    protected void initFromServer( String sFullname )
    {
        // As file variables are not init yet, here we must invoke super.getAbsolutePath()
        VFSFile f = org.joing.kernel.jvmm.RuntimeFactory.getPlatform().getBridge().
                        getFileBridge().getFile( sFullname, false );   // Must use sFullname, super.getAbsolutePath() doesn't work
        
        if( f != null )
            updateFrom( (VFSFileBase) f );
    }
    
    //------------------------------------------------------------------------//
    
    /**
     * Creates a new file or directory in the VFS.
     * 
     * @param bAsDir
     * @return The FD that represents the new file or directory
     */
    private void createIt( boolean bAsDir )
    {
        VFSFile fNew = null;
        
        if( ! exists() )  // The file does not exists: it can be created (remember: root always exists)
        {
            String    sFullName = getPath();
            VFSBridge bridgeVFS = org.joing.kernel.jvmm.RuntimeFactory.getPlatform().getBridge().getFileBridge();
            
            if( sFullName.endsWith( separator ) )   // Then, removes last '/'
                sFullName = sFullName.substring( 0, sFullName.length() - 1 );
            
            String sParent = sFullName.substring( 0, sFullName.lastIndexOf( separatorChar ) );  // Does not include trailing separatorChar
            String sChild  = sFullName.substring( sParent.length() + 1 );                       // Does not include leading separatorChar
            
            if( bAsDir )
                fNew = bridgeVFS.createDirectory( sParent, sChild );
            else
                fNew = bridgeVFS.createFile( sParent, sChild, false );
            
            updateFrom( (VFSFileBase) fNew );
        }
    }
    
    private List<VFSFile> getChildren() throws JoingServerVFSException
    {
        List<VFSFile> list = new ArrayList<VFSFile>();
        
        if( exists() && isDirectory() )
            list = org.joing.kernel.jvmm.RuntimeFactory.getPlatform().getBridge().getFileBridge().getChildren( this );
        
        return list;
    }
}