/*
 * Copyright (C) 2007, 2008 Join'g Team Members. All Rights Reserved.
 * Join'g Team Members are listed at project's home page. By the time of 
 * writting this at: https://joing.dev.java.net/servlets/ProjectMemberList.
 *
 * This file is part of Join'g project: www.joing.org
 *
 * GNU Classpath is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the free
 * Software Foundation; either version 3, or (at your option) any later version.
 * 
 * GNU Classpath is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * GNU Classpath; see the file COPYING.  If not, write to the Free Software 
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */

package org.joing.kernel.runtime.vfs;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * This is a ja.io.FileReader sustitution that works with file.io.File
 * instances as well as with org.joing.runtime.vfs.VFSFile instances.
 * 
 * @author Francisco Morero Peyrona
 */
// TODO: Decirle al cargador de clases que use esta cuando se pida: FileReader
public class JoingFileReader extends InputStreamReader
{
    public JoingFileReader( String fileName ) throws FileNotFoundException, IOException
    {
	this( JoingFileSystemView.getFileSystemView().createFileObject( fileName ) );
    }
    
    public JoingFileReader( File file ) throws FileNotFoundException, IOException
    {
        super( getInputStream( file ) );
    }
    
    public JoingFileReader( java.io.FileDescriptor fd )
    {
        super( new FileInputStream( fd ) );
    }
    
    //------------------------------------------------------------------------//
    // Added constructors to handle VFS files
    
    public JoingFileReader( VFSFile file ) throws IOException
    {
        super( org.joing.kernel.jvmm.RuntimeFactory.getPlatform().getBridge().
                   getFileBridge().getFileReaderAndWriter( file ).getInputStream() );
    }
    
    //------------------------------------------------------------------------//
    
    private static InputStream getInputStream( File file ) throws FileNotFoundException, IOException
    {
        InputStream is = null;
        
        if( file instanceof VFSFile )
        {
            is = org.joing.kernel.jvmm.RuntimeFactory.getPlatform().getBridge().
                     getFileBridge().getFileReaderAndWriter( (VFSFile) file ).getInputStream();
        }
        else
        {
            is = new FileInputStream( file );
        }
        
        return is;
    }
}