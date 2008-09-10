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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 *
 * @author Francisco Morero Peyrona
 */
// TODO: Decirle al cargador de clases que use esta cuando se pida: FileInputStream
// NEXT: Ir order to make this class compatible with existing Java apps, more methods
//       should be added (like mark(...))
public class JoingFileInputStream extends InputStream
{
    private FileInputStream local  = null;
    private InputStream     remote = null;
    
    //------------------------------------------------------------------------//
    
    public JoingFileInputStream( FileDescriptor fdObj )
    {
        local = new FileInputStream( fdObj );
    }
    
    public JoingFileInputStream( String filename ) throws FileNotFoundException, IOException
    {
        this( JoingFileSystemView.getFileSystemView().createFileObject( filename ) );
    }
    
    public JoingFileInputStream( File file ) throws FileNotFoundException, IOException
    {
        if( file instanceof VFSFile )
            remote = org.joing.kernel.jvmm.RuntimeFactory.getPlatform().getBridge().
                     getFileBridge().getInputStream( (VFSFile) file );
        else
            local = new FileInputStream( file );
    }
    
    //------------------------------------------------------------------------//

    @Override
    public int read() throws IOException
    {
        return ((local != null) ? local.read() : remote.read());
    }

    @Override
    public int read( byte[] b ) throws IOException
    {
        return ((local != null) ? local.read( b ) : remote.read( b ));
    }
    
    @Override
    public void close() throws IOException
    {
        if( local != null )
            local.close();
        else
            remote.close();
    }
}