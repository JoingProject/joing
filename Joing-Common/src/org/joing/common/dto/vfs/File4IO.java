/*
 * File4IO.java
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
import java.io.OutputStream;
import java.io.Serializable;

/**
 * An aabstract subclass of FileDescritor that contains methods to access (read
 * and write) contents from/to the native remote file. 
 * 
 * @author Francisco Morero Peyrona
 */
public abstract class File4IO extends FileDescriptor implements Serializable
{
    /**
     * An <code>InputStream</code> to read remote file contents.
     * <p>
     * Note: Different ways to access remote files (Virtual File System (VFS)),
     * will inherint from this class and implement its own way to perform this
     * task.<br>
     * For example, when the access to VFS is done via Servlets, the
     * contents of remote file are sent to client into a byte[] and the
     * InputStream reads from this internal array.
     * 
     * @return An <code>InputStream</code> to read remote file contents.
     * @see #getWriter() 
     */
    public abstract InputStream getReader() throws IOException;
    
    /**
     * An <code>OutputStream</code> to write remote file contents.
     * <p>
     * Note: Different ways to access remote files (Virtual File System (VFS)),
     * will inherint from this class and implement its own way to perform this
     * task.<br>
     * For example, when the access to VFS is done via Servlets, the
     * contents of remote file are sent to client into a byte[] and the
     * InputStream writes to this internal array.
     * When the <code>OutputStream</code> is closed, the contents are sent to
     * Join'g Server.
     * 
     * @return An <code>OutputStream</code> to write remote file contents.
     * @see #getReader() 
     */
    public abstract OutputStream getWriter() throws IOException;
}