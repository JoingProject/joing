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

package org.joing.common.dto.vfs;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

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
    InputStream getInputStream() throws IOException;
    
    /**
     * An <code>OutputStream</code> to write remote file contents as bytes.
     * <p>
     * When the <code>OutputStream</code> is closed, the contents are sent to
     * Join'g Server.
     * 
     * @return An <code>OutputStream</code> to write remote file contents.
     * @see #getReader() 
     */
    OutputStream getOutputStream() throws IOException;
}