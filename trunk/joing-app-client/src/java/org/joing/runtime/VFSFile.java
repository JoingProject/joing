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
import org.joing.runtime.bridge2server.Bridge2Server;

/**
 * This class is needed to build <code>JoingFileSystemView</code>
 * 
 * @author Francisco Morero Peyrona
 */
public class VFSFile extends File
{
    private FileDescriptor fd;
    private long           nTotalDiskSpace;
    
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
        nTotalDiskSpace = -1;
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
    
    @Override
    public boolean exists()
    {
        return fd != null;
    }
    
    @Override
    public File getAbsoluteFile()
    {
        return (exists() ? new VFSFile( fd.getAbsolutePath() ) : null);
    }
    
    @Override
    public String getAbsolutePath()
    {
        return (exists() ? fd.getAbsolutePath() : null);
    }
    
    @Override
    public File getCanonicalFile()
    {
        return getAbsoluteFile();
    }
    
    @Override
    public String getCanonicalPath()
    {
        return getAbsolutePath();
    }
    
    @Override
    public long getFreeSpace()
    {
        Bridge2Server b2s  = Bridge2Server.getInstance();    
        User          user = b2s.getUserBridge().getUser();
        
        if( user != null && nTotalDiskSpace == -1 )
            nTotalDiskSpace = user.getTotalSpace();
        
        return ((user == null) ? -1 : nTotalDiskSpace - user.getUsedSpace());
    }
    
    @Override
    public String getName()
    {
        return (exists() ? fd.getName() : null);
    }
    
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
    
    @Override
    public File getParentFile()
    {
        return (exists() ? new VFSFile( getParent() ) : null);
    }
    
    @Override
    public String getPath()
    {
        return getAbsolutePath();
    }
    
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
    
    @Override
    public long getUsableSpace()
    {
        return getFreeSpace();
    }
    
    @Override
    public int hashCode()
    {
        return 1;   // TODO hacerlo>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    }
    
    @Override
    public boolean isAbsolute()
    {
        return (exists() ? fd.getAbsolutePath().charAt( 0 ) == '/' : false);
    }
    
    @Override
    public boolean isDirectory()
    {
        return (exists() ? isDirectory() : false) ;
    }
    
    @Override
    public boolean isFile()
    {
        return exists() && (! isDirectory());
    }
    
    @Override
    public boolean isHidden()
    {
        return (exists() ? fd.isHidden() : false);
    }
    
    @Override
    public long lastModified()
    {
        return (exists() ? fd.getModified().getTime() : -1L);
    }
    
    @Override
    public long length()
    {
        return (exists() ? fd.getSize() : -1L);
    }
    
    @Override
    public String[] list()
    {
        // TODO Hacerlo
        throw new UnsupportedOperationException( "Not supported yet." );
    }
    
    @Override
    public String[] list( FilenameFilter filter )
    {
        // TODO Hacerlo
        throw new UnsupportedOperationException( "Not supported yet." );
    }
    
    @Override
    public File[] listFiles()
    {
        // TODO Hacerlo
        throw new UnsupportedOperationException( "Not supported yet." );
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
    
    public static File[] listRoots() 
    {
        // TODO Hacerlo
        throw new UnsupportedOperationException( "Not supported yet." );
    }
    
    @Override
    public boolean mkdir()
    {
        // TODO Hacerlo
        throw new UnsupportedOperationException( "Not supported yet." );
    }
    
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
    
    @Override
    public boolean setExecutable( boolean executable, boolean ownerOnly )
    {
        return setExecutable( executable );
    }
    
    @Override
    public boolean setLastModified( long time )
    {
        return false;   // It is managed by the Server
    }
    
    @Override
    public boolean setReadable( boolean readable )
    {
        // TODO Hacerlo
        throw new UnsupportedOperationException( "Not supported yet." );
    }
    
    @Override
    public boolean setReadable( boolean readable, boolean ownerOnly )
    {
        return setReadable( readable );
    }
    
    @Override
    public boolean setReadOnly()
    {
        return setWritable( false );
    }
    
    @Override
    public boolean setWritable( boolean writable ) 
    {
        // TODO Hacerlo
        throw new UnsupportedOperationException( "Not supported yet." );
    }
    
    @Override
    public boolean setWritable( boolean writable, boolean ownerOnly )
    {
        return setWritable( writable );
    }
    
    @Override
    public String toString() 
    {
        return getAbsolutePath();
    }
    
    @Override
    public URI toURI()
    {
        // TODO Hacerlo
        throw new UnsupportedOperationException( "Not supported yet." );
    }
    
    //------------------------------------------------------------------------//
    
    private FileDescriptor createFileDescriptor( String sFullPath )
    {
        FileDescriptor fd = null;
        
        return fd;
    }
}