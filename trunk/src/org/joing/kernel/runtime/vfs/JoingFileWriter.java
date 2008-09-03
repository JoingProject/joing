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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

/**
 *
 * @author Francisco Morero Peyrona
 */
// TODO: Decirle al cargador de clases que use esta cuando se pida: FileWriter
public class JoingFileWriter extends OutputStreamWriter
{
    public JoingFileWriter( String fileName ) throws IOException
    {
	this( JoingFileSystemView.getFileSystemView().createFileObject( fileName ) );
    }
    
    public JoingFileWriter( String fileName, boolean append ) throws IOException
    {
	this( JoingFileSystemView.getFileSystemView().createFileObject( fileName ), append );
    }
    
    public JoingFileWriter( File file ) throws IOException
    {
	super( getOutputStream( file, false ) );
    }
    
    public JoingFileWriter( File file, boolean append ) throws IOException
    {
        super( getOutputStream( file, append ) );
    }
    
    public JoingFileWriter( java.io.FileDescriptor fd )
    {
        super( new FileOutputStream( fd ) );
    }
    
    //------------------------------------------------------------------------//
    // Added constructors to handle VFS files
    
    public JoingFileWriter( VFSFile file ) throws IOException
    {
	super( org.joing.kernel.jvmm.RuntimeFactory.getPlatform().getBridge().
                   getFileBridge().getFileReaderAndWriter( file ).getByteWriter() );
    }
    
    public JoingFileWriter( VFSFile file, boolean append ) throws IOException
    {   // FIXME: implementarlo
        this( file );
        throw new IOException( "Option not yet implemented" );
    }
    
    //------------------------------------------------------------------------//
    
    private static OutputStream getOutputStream( File file, boolean append ) throws IOException
    {
        OutputStream os = null;
        
        if( file instanceof VFSFile )
        {
            os = org.joing.kernel.jvmm.RuntimeFactory.getPlatform().getBridge().
                     getFileBridge().getFileReaderAndWriter( (VFSFile) file ).getByteWriter(); // FIXME: considerar el append
        }
        else
        {
            os = new FileOutputStream( file, append );
        }
        
        return os;
    }
}