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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.joing.common.dto.vfs.VFSFile4IO;
import org.joing.kernel.runtime.vfs.VFSFile;

/**
 *
 * @author Francisco Morero Peyrona
 */
class VFSFileOverArray extends VFSFile implements VFSFile4IO
{
    private byte[] abContent = new byte[0];
    
    //------------------------------------------------------------------------//
    
    @Override
    public InputStream getInputStream() throws IOException
    {
        if( abContent == null )
            throw new IOException( "Error accessing file '"+ getAbsolutePath() +"'" );
        else
            return (new ByteArrayInputStream( abContent ));
    }
    
    @Override
    public OutputStream getOutputStream() throws IOException
    {
        return new MyOutputStream();
    }
    
    //------------------------------------------------------------------------//
    
    /**
     * Class constructor (this class is a DTO).
     * <p>
     * For security and encapsulation reasons, the constructor should have 
     * package scope. But it is impossible as Java OOP is defined. Therefore,
     * this constructor should not be used from applications.
     */
    VFSFileOverArray( VFSFile file, byte[] ab )
    {
        super( file );
        abContent = ab;  // Defensive copy is not needed because constructor has package scope
    }
    
    /**
     * The internal reference to the file contents (a byte[]).
     * 
     * @return The internal reference to the file contents (a byte[]).
     */
    byte[] intern()
    {
        return abContent; // Defensive copy is not needed because intern() has package scope
    }
    
    //------------------------------------------------------------------------//
    // INNER CLASS: An OutputStream that informs when it is closed.
    //------------------------------------------------------------------------//
    private final class MyOutputStream extends ByteArrayOutputStream
    {
        private boolean bClosed = false;
        
        public void flush() throws IOException
        {
            VFSFileOverArray.this.abContent = super.toByteArray();
            
            (new VFSBridgeServletImpl()).writeFileFromArray( VFSFileOverArray.this );
        }
        
        public void close() throws IOException
        {
            if( ! bClosed )
            {
                flush();
                super.close();   // Not needed (does nothing): placed just for clarity
                bClosed = true;
            }
        }
        
        protected void finalize() throws Throwable
        {
            close();
            super.finalize();
        }
    }
}