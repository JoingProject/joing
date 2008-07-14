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
package org.joing.runtime.vfs;

import org.joing.common.dto.vfs.*;
import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.joing.common.dto.user.User;
import org.joing.common.exception.JoingServerVFSException;
import org.joing.common.clientAPI.runtime.Bridge2Server;
import org.joing.jvmm.RuntimeFactory;

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
    //       También habría que hacer un caché de ficheros descargados para no
    //       tener que estar contínuamente leyendo del servidor que puede ser muy
    //       lento.
 
    /**
     * The system-dependent default name-separator character.  This field is
     * initialized to contain the first character of the value of the system
     * property <code>file.separator</code>. Join'g uses same as UNIX systems, 
     * the value of this field is <code>'/'</code>.
     *
     * @see     java.lang.System#getProperty(java.lang.String)
     */
    public static final char   separatorChar     = VFSView.separatorChar;
    /**
     * The system-dependent default name-separator character, represented as a
     * string for convenience.  This string contains a single character, namely
     * <code>{@link #separatorChar}</code>.
     */
    public static final String separator         = "" + separatorChar;           // Not needed, but placed for clarity
    /**
     * The system-dependent path-separator character.  This field is
     * initialized to contain the first character of the value of the system
     * property <code>path.separator</code>.  This character is used to
     * separate filenames in a sequence of files given as a <em>path list</em>.
     * On UNIX systems, this character is <code>':'</code>; on Microsoft Windows systems it
     * is <code>';'</code>.
     *
     * @see     java.lang.System#getProperty(java.lang.String)
     */
    public static final char   pathSeparatorChar = VFSView.pathSeparatorChar;
    /**
     * The system-dependent path-separator character.  This field is
     * initialized to contain the first character of the value of the system
     * property <code>path.separator</code>.  This character is used to
     * separate filenames in a sequence of files given as a <em>path list</em>.
     * Join'g uses same as UNIX systems, this character is <code>':'</code>.
     *
     * @see     java.lang.System#getProperty(java.lang.String)
     */
    public static final String pathSeparator     = "" + pathSeparatorChar;      // Not needed, but placed for clarity
    
    //------------------------------------------------------------------------//
    
    private static final long serialVersionUID = 1L;    // TODO: cambiarlo por un nº apropiado
    
    private static long nTotalDiskSpace = -1;
    
    private FileDescriptor fd;
    
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
        this( buildFullPath( parent, child ) );
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
        
        fd = initFileDescriptor( sFullName );
    }
    
    /**
     * Creates a new instance of <code>VFSFile</code>.
     *
     * @param parent Parent directory.
     * @param child  Directory or file name.
     */
    public VFSFile( VFSFile parent, String child ) throws JoingServerVFSException
    {
        super( buildFullPath( (parent == null ? "" : parent.getAbsolutePath()), 
                              child ) );
        
        if( parent == null )
            throw new NullPointerException( "Parent directory can't be null." );
        
        if( ! parent.isDirectory() )
            throw new IllegalArgumentException( "Parent must be a directory." );
        
        fd = initFileDescriptor( buildFullPath( parent.getAbsolutePath(), child ) );
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
        fd = createIt( false );
        return exists();
    }
    
    @Override
    public boolean delete() throws JoingServerVFSException
    {
        boolean bSuccess = false;
        
        if( exists() )
        {
            Bridge2Server b2s = RuntimeFactory.getPlatform().getBridge();
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
        return (exists() ? fd.getAbsolutePath() : super.getAbsolutePath() +" [file does not exists]");
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
    {// TODO: Hay que avriguar un modo de refrescar esto sólo cuando sea necesario
        long          nFree = -1;
        Bridge2Server b2s   = RuntimeFactory.getPlatform().getBridge();
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
        return (exists() ? fd.getName() : super.getName());
    }
    
    /**
     * @see java.io.File#getParent()
     */
    @Override
    public String getParent()
    {
        return ((exists() && fd.getParent().length() > 0)
                ? fd.getParent()
                : super.getParent());
    }
    
    /**
     * @see java.io.File#getParentFile()
     */
    @Override
    public VFSFile getParentFile() throws JoingServerVFSException
    {
        String sParent = getParent();
        
        return ((sParent == null) ? null : new VFSFile( sParent ));
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
                    Bridge2Server b2s  = RuntimeFactory.getPlatform().getBridge();
                    User          user = b2s.getUserBridge().getUser();
                    
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
    // A directory can always be written (add files and sub-dirs)
    public boolean canWrite()
    {
        if( exists() )
            return (isDirectory() ? fd.isReadable() : fd.isModifiable());
        
        return false;
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
        return (exists() ? fd.isPublic() : false);
    }
    
    public boolean setPublic( boolean b )
    {
        return (exists() ? fd.setPublic( b ) : false);
    }
    
    public boolean isDeleteable()
    {
        return (exists() ? fd.isDeleteable() : false);
    }
    
    public boolean setDeleteable( boolean b )
    {
        return (exists() ? fd.setDeleteable( b ) : false);
    }
    
    public boolean isDuplicable()
    {
        return (exists() ? fd.isDuplicable() : false);
    }
    
    public boolean setDuplicable( boolean b )
    {
        return (exists() ? fd.setDuplicable( b ) : false);
    }
    
    public boolean isAlterable()
    {
        return (exists() ? fd.isAlterable() : false);
    }
    
    public boolean setAlterable( boolean b )
    {
        return (exists() ? fd.setAlterable( b ) : false);
    }
    
    public boolean isLink()
    {
        return false;    // TODO: implementarlo cuando haya aclarado cómo va lo de los links
    }
    
    public boolean isLocked()
    {
        return (exists() ? fd.isLocked() : false);
    }
    
    public boolean setLocked( boolean b )
    {
        return (exists() ? fd.setLocked( b ) : false);
    }
    
    public boolean ownsLock()
    {
        return (exists() ? fd.ownsLock() : false);
    }
    
    public boolean isInTrashcan()
    {
        return (exists() ? fd.isInTrashcan() : false);
    }
    
    public boolean setInTrashcan( boolean b )
    {
        return (exists() ? fd.setInTrashcan( b ) : false);
    }
    
    public String getNotes()
    {
        return (exists() ? fd.getNotes() : null);
    }
    
    public boolean setNotes( String notes )
    {
        return (exists() ? fd.setNotes( notes ) : false);
    }
    
    public Date getCreated()
    {
        return (exists() ? fd.getCreated() : null);
    }
    
    public Date getAccessed()
    {
        return (exists() ? fd.getAccessed() : null );
    }
    
    public long getSize()
    {
        return (exists() ? fd.getSize() : -1);
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
        //throw new UnsupportedOperationException( "Not supported yet." );
    }
    
    /**
     * @see java.io.File#listRoots()
     */
    // By contract has to return null instead of empty array
    public static VFSFile[] listRoots() throws JoingServerVFSException
    {
        List<FileDescriptor> lstRoots   = RuntimeFactory.getPlatform().getBridge().getFileBridge().getRoots();
        VFSFile              vfsRoots[] = new VFSFile[ lstRoots.size() ];
        
        for( int n = 0; n < vfsRoots.length; n++ )
            vfsRoots[n] = new VFSFile( lstRoots.get( n ) );
        
        return vfsRoots;
    }

    /**
     * @see java.io.File#mkdir()
     */
    @Override
    public boolean mkdir() throws JoingServerVFSException
    {
        fd = createIt( true );
        return exists();
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
    public boolean renameTo( File fNewName )
    {
        FileDescriptor fdTemp = null;
        
        if( fd != null && fNewName != null && fNewName.getName() != null )
        {
            if( ! fd.getName().equals( fNewName.getName() ) )
            {
                fd.setName( fNewName.getName() );
                fdTemp = RuntimeFactory.getPlatform().getBridge().getFileBridge().update( fd );

                if( fdTemp != null )
                    fd = fdTemp;
            }
            else     // Old name and new name are the same => Nothing to do
            {
                fdTemp = fd;    // Just to return true
            }
        }
        
        return( fdTemp != null );
    }
    
    /**
     * @see java.io.File#setExecutable( boolean executable )
     */
    @Override
    public boolean setExecutable( boolean executable )
    {
        return( exists() ? fd.setExecutable( executable ) : false );
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
        FileDescriptor _fd = RuntimeFactory.getPlatform().getBridge().getFileBridge().update( fd );
        
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
    
    // Used only internally
    private VFSFile( FileDescriptor fd )
    {
        super( fd.getAbsolutePath() );
        this.fd = fd;
    }
    
    /**
     * Called by constructors to initialize the FileDescriptor.
     * It can be that the FD finally is null because this file does not yet 
     * exists in the VFS.
     * 
     * @param sFullName Full path representing a file in VFS
     * @return The FD or null if the file does not yet exists in the VFS
     */
    private FileDescriptor initFileDescriptor( String sFullName )
    {
        if( sFullName == null )
            throw new NullPointerException( "File name can't be null." );
        
        sFullName = sFullName.trim();
        
        if( sFullName.length() == 0 )
            throw new IllegalArgumentException( "File name can't be empty." );
        
        if( sFullName.charAt( 0 ) != separatorChar )
            throw new IllegalArgumentException( "Name must be absolute (starting with '/')." );
        
        // If fd == null, then file or dir not exists
        return RuntimeFactory.getPlatform().getBridge().getFileBridge().getFile( sFullName );
    }
    
    /**
     * Creates a new file or directory in the VFS.
     * 
     * @param bAsDir
     * @return The FD that represents the new file or directory
     */
    private FileDescriptor createIt( boolean bAsDir )
    {
        FileDescriptor fdNew = fd;
        
        if( fd == null )  // The file does not exists: it can be created (remember: root always exists)
        {
            String sFullName = super.getPath();   // As fd == null, this is the only way to get the inf.
            
            if( sFullName.endsWith( separator ) )   // Then, removes last '/'
                sFullName = sFullName.substring( 0, sFullName.length() - 2 );
            
            String sParent = sFullName.substring( 0, sFullName.lastIndexOf( separatorChar ) );  // Does not include trailing separatorChar
            String sChild  = sFullName.substring( sParent.length() + 1 );                       // Does not include leading separatorChar
            
            if( bAsDir )
                fdNew = RuntimeFactory.getPlatform().getBridge().getFileBridge().createDirectory( sParent, sChild );
            else
                fdNew = RuntimeFactory.getPlatform().getBridge().getFileBridge().createFile( sParent, sChild );
        }
        
        return fdNew;
    }
    
    private List<FileDescriptor> getChilds() throws JoingServerVFSException
    {
        List<FileDescriptor> list = new ArrayList<FileDescriptor>();
        
        if( exists() && fd.isDirectory() )
            list = RuntimeFactory.getPlatform().getBridge().getFileBridge().getChilds( fd.getId() );
        
        return list;
    }
    
    /**
     * Constructs a file-path based on parent and child passed strings.
     * 
     * @param sParent
     * @param sChild
     * @return The full path composed by passed parent and child
     */
    private static String buildFullPath( String sParent, String sChild )
    {
        StringBuffer sb = new StringBuffer( sParent.length() + sChild.length() + 16 );
        
        // Parent part
        if( sParent == null || sParent.trim().length() == 0 )
            sb.append( separatorChar );
        else
            sb.append( sParent.trim() );
        
        // Child part
        if( sChild != null && sChild.trim().length() > 0 )
        {
            if( sb.charAt( sb.length() - 1 ) != separatorChar )  // If not ends with sep, append it
                sb.append( separatorChar );
             
            sChild = sChild.trim();
            
            if( sChild.charAt( 0 ) == separatorChar )
                sChild = sChild.substring( 1 );                           // Removes first '/'
            
            if( sChild.charAt( sChild.length() - 1 ) == separatorChar )   // Removes last '/'
                sChild = sChild.substring( 0, sChild.length() - 2 );
            
            sb.append( sChild );
        }
        
        return sb.toString();
    }
}