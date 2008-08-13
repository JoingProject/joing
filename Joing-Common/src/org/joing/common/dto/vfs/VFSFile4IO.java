/*
 * VFSFile4IO.java
 * 
 * Created on 15-jul-2007, 11:44:38
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

package org.joing.common.dto.vfs;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Serializable;

/**
 * An interface that contains methods to access (read and write) contents 
 * from/to the native remote file.
 * <p>
 * Note: Different ways to access remote files (Virtual File System (VFS)),
 * will depend on how every class implementing this interface will perform this
 * task.<br>
 * For example, when the access to VFS is done via Servlets, the contents of 
 * remote file are sent to client into a byte[] and the InputStream reads from 
 * this internal array (this is done by VFSFileOverArray class).
 * 
 * @author Francisco Morero Peyrona
 */
public interface VFSFile4IO
{
    /**
     * An <code>InputStream</code> to read remote file contents as bytes.
     * 
     * @return An <code>InputStream</code> to read remote file contents.
     * @see #getWriter() 
     */
    InputStream getByteReader() throws IOException;
    
    /**
     * An <code>OutputStream</code> to write remote file contents as bytes.
     * <p>
     * When the <code>OutputStream</code> is closed, the contents are sent to
     * Join'g Server.
     * 
     * @return An <code>OutputStream</code> to write remote file contents.
     * @see #getReader() 
     */
    OutputStream getByteWriter() throws IOException;
    
    /**
     * An <code>InputStreamReader</code> to read remote file contents as chars.
     * 
     * @return An <code>InputStreamReader</code>
     * @throws java.io.IOException
     */
    InputStreamReader getCharReader() throws IOException;
    
    /**
     * An <code>OutputStreamWriter</code> to write remote file contents as chars.
     * 
     * @return An <code>OutputStreamWriter</code>
     * @throws java.io.IOException
     */
    OutputStreamWriter getCharWriter() throws IOException;
    
    /**
     * An <code>InputStreamReader</code> to read remote file contents as chars.
     * 
     * @param sCharsetName
     * @return An <code>InputStreamReader</code>
     * @throws java.io.IOException
     */
    InputStreamReader getCharReader( String sCharsetName ) throws IOException;
    
    /**
     * An <code>OutputStreamWriter</code> to write remote file contents as chars.
     * 
     * @param sCharsetName
     * @return An <code>OutputStreamWriter</code>
     * @throws java.io.IOException
     */
    OutputStreamWriter getCharWriter( String sCharsetName ) throws IOException;
}