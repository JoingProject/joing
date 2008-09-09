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
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 *
 * @author Francisco Morero Peyrona
 */
// TODO: Decirle al cargador de clases que use esta cuando se pida: FileInputStream
public class JoingFileOutputStream extends OutputStream
{
    private FileOutputStream local  = null;
    private OutputStream     remote = null;

    //------------------------------------------------------------------------//
    
    public JoingFileOutputStream( FileDescriptor fdObj )
    {
        local = new FileOutputStream( fdObj );
    }

    public JoingFileOutputStream( String filename ) throws IOException
    {
        this( JoingFileSystemView.getFileSystemView().createFileObject( filename ) );
    }
    
    public JoingFileOutputStream( File file ) throws IOException
    {
        if( file instanceof VFSFile )
            remote = org.joing.kernel.jvmm.RuntimeFactory.getPlatform().getBridge().
                     getFileBridge().getFileReaderAndWriter( (VFSFile) file ).getOutputStream();
        else
            local = new FileOutputStream( file );
    }
    
    public JoingFileOutputStream( String name, boolean append )
    {
        
    }

    public JoingFileOutputStream( File file, boolean append )
    {
        
    }
    
    //------------------------------------------------------------------------//
    
    @Override
    public void write( int b ) throws IOException
    {
        throw new UnsupportedOperationException( "Not supported yet." );
    }
    
    @Override
    public void write( byte[] b ) throws IOException
    {
        super.write( b );
    }
    
    @Override
    public void write( byte[] b, int off, int len ) throws IOException
    {
        super.write( b, off, len );
    }
    
    @Override
    public void flush() throws IOException
    {
        super.flush();
    }
    
    @Override
    public void close() throws IOException
    {
        super.close();
    }
}