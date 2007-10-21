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
package org.joing.runtime;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.joing.common.dto.user.User;
import org.joing.common.dto.vfs.FileDescriptor;
import org.joing.common.exception.JoingServerVFSException;
import org.joing.jvmm.Platform;
import org.joing.runtime.bridge2server.Bridge2Server;

/**
 * This class is needed to build <code>JoingFileSystemView</code>
 * 
 * @author Francisco Morero Peyrona
 */
public class VFSFile extends File
{
    // TODO: Revisar la clase para que las búsquedas de ficheros en el Servidor
    //       se hagan siempre que sea posible en base al ID_PARENT en lugar de
    //       en base al Path.
    
    private static final long serialVersionUID = 1L;    // TODO: cambiarlo por un nº apropiado
    
    private static long nTotalDiskSpace = -1;
    
    private FileDescriptor fd;
    
    private FileDescriptor fdParent = null;   // Used only to create files and dirs
    private String         sChild   = null;   // Used only to create files and dirs
    
    
    private Platform platform = Platform.getInstance();
    //------------------------------------------------------------------------//
    
    /**
     * Creates a new instance of <code>VFSFile</code>.
     * The directory or file name must start with root ("/").
     * <p>
     * It is recommended that directory names end with file separator ("/").
     * 
     * @param parent Parent directory.
     * @param child  Directory or file name.
     */
    public VFSFile( String parent, String child )
    {
        this( parent +'/'+ child );     // VFS uses '/' as path separator
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
        super( sFullName );    // Needed but never used
        
        sFullName = sFullName.trim();
        
        if( sFullName.charAt( 0 ) != '/' )
            throw new IllegalArgumentException( "Name must be absolute (starting with '/')" );
        
        // If fd == null, then file or dir not exists
        fd = platform.getBridge().getFileBridge().getFile( sFullName );
        
        if( fd == null )  // The file does not exists: it can be created (remember: root always exists)
        {
            if( sFullName.endsWith( "/" ) )   // Then, removes last '/'
                sFullName = sFullName.substring( 0, sFullName.length() - 2 );
            
            String sParent = sFullName.substring( 0, sFullName.lastIndexOf( '/' ) );
            String sHijo   = sFullName.substring( sParent.length() );
            
            fdParent = platform.getBridge().getFileBridge().getFile( sParent );
            sChild   = sHijo;
        }
    }
    
    /**
     * Creates a new instance of <code>VFSFile</code>.
     *
     * @param parent Parent directory.
     * @param child  Directory or file name.
     */
    public VFSFile( VFSFile parent, String child ) throws JoingServerVFSException
    {
        super( parent.getAbsolutePath() +"/"+ child );    // Needed but never used
        
        if( ! parent.isDirectory() )
            throw new IllegalArgumentException( "parent must be a directory" );
        
        // If fd == null, then parent and or child not exists
        fd = platform.getBridge().getFileBridge().getFile( parent.getAbsolutePath() +"/"+ child.trim() );
        
        if( fd == null )
        {
            fdParent = parent.fd;
            sChild   = child.trim();
        }
    }
    
    // Used only internally
    private VFSFile( FileDescriptor fd )
    {
        super( fd.getName() );   // Needed but never used        
        this.fd = fd;
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
                return this.fd.getAbsolutePath().compareTo( file.fd.getAbsolutePath() );
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
        if( fd == null && fdParent != null && sChild != null )
            fd = platform.getBridge().getFileBridge().createFile( fdParent.getAbsolutePath(), sChild );
        
        return fd != null;
    }
    
    @Override
    public boolean delete() throws JoingServerVFSException
    {
        boolean bSuccess = false;
        
        if( exists() )
        {
            Bridge2Server b2s = platform.getBridge();
            bSuccess = b2s.getFileBridge().delete( fd.getId() );
        }
        
        return bSuccess;
    }
    
    @Override
    public void deleteOnExit()
    {
        throw new UnsupportedOperationException( "Not supported operation." );
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
                   file.fd.getId() == this.fd.getId();
        }
        
        return bEquals;
    }
    
    /**
     * @see java.io.File#exists()
     */
    @Override
    public boolean exists()
    {
        return fd != null;
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
        return (exists() ? this : null);
    }
    
    /**
     * @see java.io.File#getAbsolutePath()
     */
    @Override
    public String getAbsolutePath()
    {
        return (exists() ? fd.getAbsolutePath() : null);
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
    {
        long          nFree = -1;
        Bridge2Server b2s   = platform.getBridge();
        User          user  = b2s.getUserBridge().getUser();

        if( user != null )
        {
            if( nTotalDiskSpace == -1 )
                nTotalDiskSpace = user.getTotalSpace();

            nFree = nTotalDiskSpace - user.getUsedSpace();
        }
        
        return nFree;
    }
    
    /**
     * @see java.io.File#getName()
     */
    @Override
    public String getName()
    {
        return (exists() ? fd.getName() : null);
    }
    
    /**
     * @see java.io.File#getParent()
     */
    @Override
    public String getParent()
    {
        if( exists() )
        {
            String sPath = fd.getAbsolutePath();
            
            if( sPath.endsWith( "/" ) )     // Then, removes last '/'
                sPath = sPath.substring( 0, sPath.length() - 2 );
            
            int index = sPath.lastIndexOf( '/' );
            
            return ((index < 0) ? null : sPath.substring( 0, index ));
        }
        else
        {
            return null;
        }
    }
    
    /**
     * @see java.io.File#getParentFile()
     */
    @Override
    public VFSFile getParentFile() throws JoingServerVFSException
    {
        return (exists() ? new VFSFile( getParent() ) : null);
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
            Bridge2Server b2s  = platform.getBridge();    
            User          user = b2s.getUserBridge().getUser();
        
            if( user != null )
                nTotalDiskSpace = user.getTotalSpace();
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
        return (exists() ? fd.getId() : -23);
    }
    
    @Override
    public boolean canExecute()
    {
        return (exists() ? fd.isExecutable() : false);
    }
    
    @Override
    public boolean canRead()
    {
        return (exists() ? fd.isReadable() : false);
    }
    
    @Override
    public boolean canWrite()
    {
        return (exists() ? fd.isModifiable() : false);
    }
    
    /**
     * @see java.io.File#isAbsolute()
     */
    @Override
    public boolean isAbsolute()
    {
        return exists();  // VFSFile are always absolute (must start with '/')
    }
    
    /**
     * @see java.io.File#isDirectory()
     */
    @Override
    public boolean isDirectory()
    {
        return (exists() ? fd.isDirectory() : false);
    }
    
    /**
     * @see java.io.File#isFile()
     */
    @Override
    public boolean isFile()
    {
        return exists() && (! isDirectory());
    }
    
    /**
     * @see java.io.File#isHidden()
     */
    @Override
    public boolean isHidden()
    {
        return (exists() ? fd.isHidden() : false);
    }
    
    // Here starts VFS properties that java.io.File does not have
    public boolean isPublic()
    {
        return fd.isPublic();
    }
    
    public boolean setPublic( boolean b )
    {
        return fd.setPublic( b );
    }
    
    public boolean isDeleteable()
    {
        return fd.isDeleteable();
    }
    
    public boolean setDeleteable( boolean b )
    {
        return fd.setDeleteable( b );
    }
    
    public boolean isDuplicable()
    {
        return fd.isDuplicable();
    }
    
    public boolean setDuplicable( boolean b )
    {
        return fd.setDuplicable( b );
    }
    
    public boolean isAlterable()
    {
        return fd.isAlterable();
    }
    
    public boolean setAlterable( boolean b )
    {
        return fd.setAlterable( b );
    }
    
    public boolean isLink()
    {
        return false;    // TODO: implementarlo cuando haya aclarado cómo va lo de los links
    }
    
    public boolean isLocked()
    {
        return fd.isLocked();
    }
    
    public boolean setLocked( boolean b )
    {
        return fd.setLocked( b );
    }
    
    public boolean ownsLock()
    {
        return fd.ownsLock();
    }
    
    public boolean isInTrashcan()
    {
        return fd.isInTrashcan();
    }
    
    public boolean setInTrashcan( boolean b )
    {
        return fd.setInTrashcan( b );
    }
    
    public String getNotes()
    {
        return fd.getNotes();
    }
    
    public boolean setNotes( String notes )
    {
        return fd.setNotes( notes );
    }
    
    public Date getCreated()
    {
        return fd.getCreated();
    }
    
    public Date getAccessed()
    {
        return fd.getAccessed();
    }
    
    public long getSize()
    {
        return fd.getSize();
    }
    //-----------------------------------------------------------
    
    /**
     * @see java.io.File#lastModified()
     */
    @Override
    public long lastModified()
    {
        return (exists() ? fd.getModified().getTime() : -1L);
    }
    
    /**
     * @see java.io.File#length()
     */
    @Override
    public long length()
    {
        return (exists() ? fd.getSize() : -1L);
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
            List<FileDescriptor> lstChilds = getChilds();

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
        VFSFile[] aFile = null;
        
        if( isDirectory() )
        {
            List<FileDescriptor> list = getChilds();

            if( list.size() > 0 )
            {
                aFile = new VFSFile[ list.size() ];

                for( int n = 0; n < list.size(); n++ )
                    aFile[n] = new VFSFile( list.get( n ) );
            }
        }
        
        return aFile;
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
        // TODO Hacerlo
        throw new UnsupportedOperationException( "Not supported yet." );
    }
    
    /**
     * @see java.io.File#listRoots()
     */
    // By contract has to return null instead of empty array
    public static VFSFile[] listRoots() throws JoingServerVFSException
    {
        // TODO: leer los roots del Servidor
        VFSFile[] afRoot = new VFSFile[1];
        afRoot[0] = new VFSFile( "/" );
        System.out.println(afRoot[0].getAbsolutePath());
        return afRoot;
    }

    /**
     * @see java.io.File#mkdir()
     */
    @Override
    public boolean mkdir() throws JoingServerVFSException
    {
        if( fd == null && fdParent != null && sChild != null )
            fd = platform.getBridge().getFileBridge().createDirectory( fdParent.getAbsolutePath(), sChild );
        
        return fd != null;
    }
    
    /**
     * @see java.io.File#mkdirs()
     */
    @Override
    public boolean mkdirs() throws JoingServerVFSException
    {
        // TODO Hacerlo
        throw new UnsupportedOperationException( "Not supported yet." );
    }
    
    @Override
    public boolean renameTo( File dest )
    {
        // TODO Hacerlo
        throw new UnsupportedOperationException( "Not supported yet." );
    }
    
    /**
     * @see java.io.File#setExecutable( boolean executable )
     */
    @Override
    public boolean setExecutable( boolean executable )
    {
        return (exists() ? fd.setExecutable( executable ) : false);
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
     * @see java.io.File#setReadable( boolean readable )
     */
    @Override
    public boolean setReadable( boolean readable )
    {
        return (exists() ? fd.setReadable( readable ) : false);
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
     */
    public boolean updateAttributes() throws JoingServerVFSException
    {
        FileDescriptor _fd = platform.getBridge().getFileBridge().update( fd );
        
        return fd.equals( _fd );
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
    
    private List<FileDescriptor> getChilds() throws JoingServerVFSException
    {
        List<FileDescriptor> list = new ArrayList<FileDescriptor>();
        
        if( exists() && fd.isDirectory() )
            list = platform.getBridge().getFileBridge().getChilds( fd.getId() );
        
        return list;
    }
}