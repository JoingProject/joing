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

import ejb.user.User;
import java.io.File;
import ejb.vfs.FileDescriptor;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.net.MalformedURLException;
import java.net.URI;
import java.util.List;
import org.joing.runtime.bridge2server.Bridge2Server;

/**
 * This class is needed to build <code>JoingFileSystemView</code>
 * 
 * @author Francisco Morero Peyrona
 */
public class VFSFile extends File
{
    private static long nTotalDiskSpace = -1;
    
    private FileDescriptor fd;
    
    //------------------------------------------------------------------------//
    
    public VFSFile( File parent, String child )
    {
        this( parent.getAbsolutePath(), child );
    }
    
    public VFSFile( String parent, String child ) 
    {
        this( parent +'/'+ child );     // VFS uses '/' as path separator
    }
    
    public VFSFile( URI uri ) throws MalformedURLException
    {
        this( uri.toURL().getFile() );
    }

    public VFSFile( String pathname )
    {
        super( pathname );
        fd = createFileDescriptor( pathname );
    }
    
    /**
     * This constructor is added for speed: whenever the file descriptor is
     * available, this is the rpeferred way.
     */ 
    public VFSFile( FileDescriptor fd )
    {
        super( fd.getAbsolutePath() );
        this.fd = fd;
    }
    
    //------------------------------------------------------------------------//
    
    @Override
    public boolean canExecute()
    {
        return (exists() ? fd.isExecutable() : false);
    }
    
    @Override
    public boolean canRead()
    {
        return (exists() ? ! fd.isSystem() : false);
    }
    
    @Override
    public boolean canWrite()
    {
        return (exists() ? fd.isModifiable() : false);
    }
    
    @Override
    public int compareTo( File pathname )
    {
        // TODO Hacerlo
        throw new UnsupportedOperationException( "Not supported yet." );
    }
    
    @Override
    public boolean createNewFile()
    {
        // TODO Hacerlo
        throw new UnsupportedOperationException( "Not supported yet." );
    }
    
    @Override
    public boolean delete()
    {
        // TODO Hacerlo
        throw new UnsupportedOperationException( "Not supported yet." );
    }
    
    @Override
    public void deleteOnExit()
    {
        throw new UnsupportedOperationException( "Not supported operation." );
    }
 
    @Override
    public boolean equals( Object obj )
    {
        // TODO Hacerlo
        throw new UnsupportedOperationException( "Not supported yet." );        
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
     * @see java.io.File#getAbsoluteFile()
     */
    @Override
    public File getAbsoluteFile()
    {
        return (exists() ? new VFSFile( fd.getAbsolutePath() ) : null);
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
    public File getCanonicalFile()
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
    public long getFreeSpace()
    {
        long          nFree = -1;
        Bridge2Server b2s   = Bridge2Server.getInstance();
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
            int    index = sPath.lastIndexOf( '/' );

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
    public File getParentFile()
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
    public long getTotalSpace()
    {
        if( nTotalDiskSpace == -1 )
        {
            Bridge2Server b2s  = Bridge2Server.getInstance();    
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
        return 1;   // TODO hacerlo>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    }
    
    /**
     * @see java.io.File#isAbsolute()
     */
    @Override
    public boolean isAbsolute()
    {
        return (exists() ? fd.getAbsolutePath().charAt( 0 ) == '/' : false);
    }
    
    /**
     * @see java.io.File#isDirectory()
     */
    @Override
    public boolean isDirectory()
    {
        return (exists() ? isDirectory() : false) ;
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
    public String[] list()
    {
        File[]   af = listFiles();
        String[] as = null;
        
        if( af != null )
        {
            as = new String[ af.length ];

            for( int n = 0; n < af.length; n++ )
                as[n] = af[n].getName();

            return as;
        }
        
        return as;
    }
    
    @Override
    public String[] list( FilenameFilter filter )
    {
        // TODO Hacerlo
        throw new UnsupportedOperationException( "Not supported yet." );
    }
    
    /**
     * @see java.io.File#listFiles()
     */
    @Override
    public File[] listFiles()
    {
        VFSFile[] af = null;
        
        // TODO Hacerlo
        if( exists() && fd.isDirectory() )
        {
            Bridge2Server b2s = Bridge2Server.getInstance();
            List<FileDescriptor> list = b2s.getFileBridge().getChilds( fd.getId() );
            
            af = new VFSFile[ list.size() ];
            
            for( int n = 0; n < list.size(); n++ )
                af[n] = new VFSFile( list.get( n ) );
        }
        
        return af;
    }
    
    @Override
    public File[] listFiles( FileFilter filter )
    {
        // TODO Hacerlo
        throw new UnsupportedOperationException( "Not supported yet." );
    }
    
    @Override
    public File[] listFiles( FilenameFilter filter )
    {
        // TODO Hacerlo
        throw new UnsupportedOperationException( "Not supported yet." );
    }
    
    /**
     * @see java.io.File#listRoots()
     */
    public static File[] listRoots()
    {
        // TODO Hacerlo
        throw new UnsupportedOperationException( "Not supported yet." );
    }

    /**
     * @see java.io.File#mkdir()
     */
    @Override
    public boolean mkdir()
    {
        // TODO Hacerlo
        throw new UnsupportedOperationException( "Not supported yet." );
    }
    
    /**
     * @see java.io.File#mkdirs()
     */
    @Override
    public boolean mkdirs()
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
        if( exists() && fd.isAlterable() && (! fd.isDirectory()) )
        {
            fd.setExecutable( executable );
            return true;
        }
        else
        {
            return false;
        }
    }
    
    /**
     * @see java.io.File#setExecutable( boolean executable, boolean ownerOnly )
     */
    @Override
    public boolean setExecutable( boolean executable, boolean ownerOnly )
    {
        return setExecutable( executable );
    }
    
    /**
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
        // TODO Hacerlo
        throw new UnsupportedOperationException( "Not supported yet." );
    }
    
    /**
     * @see java.io.File#setReadable( boolean readable, boolean ownerOnly )
     */
    @Override
    public boolean setReadable( boolean readable, boolean ownerOnly )
    {
        return setReadable( readable );
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
        return setWritable( writable );
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
    
    private FileDescriptor createFileDescriptor( String sFullPath )
    {
        Bridge2Server b2s = Bridge2Server.getInstance();
        
        return b2s.getFileBridge().getFile( sFullPath );
    }
}