/*
 * Copyright (C) 2007, 2008 Join'g Team Members.  All Rights Reserved.
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

package org.joing.runtime.swap;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import org.joing.runtime.vfs.VFSFile;

/**
 * A replacement for java.io.FileReader that takes in consideration Join'g VFS.
 * 
 * @author Francisco Morero Peyrona
 */
public class JoingFileReader extends InputStreamReader
{
    /**
    * Creates a new <tt>FileReader</tt>, given the name of the
    * file to read from.
    *
    * @param fileName the name of the file to read from
    * @exception  FileNotFoundException  if the named file does not exist,
    *                   is a directory rather than a regular file,
    *                   or for some other reason cannot be opened for
    *                   reading.
    */
    public JoingFileReader( String fileName ) throws FileNotFoundException
    {
        super( new FileInputStream( fileName ) );
    }

    /**
     * Creates a new <tt>FileReader</tt>, given the <tt>File</tt> 
     * to read from.
     *
     * @param file the <tt>File</tt> to read from
     * @exception  FileNotFoundException  if the file does not exist,
     *                   is a directory rather than a regular file,
     *                   or for some other reason cannot be opened for
     *                   reading.
     */
    public JoingFileReader( File file ) throws FileNotFoundException
    {
        super( new FileInputStream( file ) );
    }

    /**
     * Creates a new <tt>FileReader</tt>, given the 
     * <tt>FileDescriptor</tt> to read from.
     *
     * @param fd the FileDescriptor to read from
     */
    public JoingFileReader( FileDescriptor fd ) throws IOException
    {
        super( new FileInputStream( fd ) );
    }
    
    public JoingFileReader( VFSFile file ) throws IOException
    {
        this( file.getFileDescriptor() );
    }
    
    public JoingFileReader( org.joing.common.dto.vfs.FileDescriptor fd ) throws IOException
    {
        super( org.joing.jvmm.RuntimeFactory.getPlatform().getBridge().getFileBridge().
                    getFile( fd ).getReader() );
    }
}