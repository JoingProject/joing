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

package org.joing.kernel.runtime.bridge2server;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.joing.kernel.runtime.vfs.VFSFile;

/**
 *
 * @author Francisco Morero Peyrona
 */
class ByteArrayOutputStream4VFS extends ByteArrayOutputStream
{
    private VFSFile file    = null;
    private boolean bAppend = false;
    private boolean bClosed = false;
    
    //------------------------------------------------------------------------//
    
    /**
     * Class constructor.
     */
    ByteArrayOutputStream4VFS( VFSFile file, boolean bAppend, int nSize )
    {
        super( nSize );
        this.file    = file;
        this.bAppend = bAppend;
    }

    VFSFile getFile()
    {
        return file;
    }
    
    boolean isAppend()
    {
        return bAppend;
    }

    @Override
    public void flush() throws IOException
    {
        super.flush();
        (new VFSBridgeServletImpl()).writeFileFromArray( this  );
    }
    
    @Override
    public void close() throws IOException
    {
        if( ! bClosed )
        {
            flush();
            super.close();   // Not needed (does nothing): placed just for clarity
            bClosed = true;
        }
    }
    
    @Override
    protected void finalize() throws Throwable
    {
        close();
        super.finalize();
    }
}