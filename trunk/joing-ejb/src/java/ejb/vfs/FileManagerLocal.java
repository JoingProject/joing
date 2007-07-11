/*
 * VfsFileManagerBean.java
 *
 * Created on 8 de junio de 2007, 11:59
 *
 * Copyright (C) 2007 Francisco Morero Peyrona
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

package ejb.vfs;

import javax.ejb.Local;

/**
 * This is the business interface for VfsFileManager enterprise bean.
 */
@Local
public interface FileManagerLocal extends FileManagerRemote
{
    /**
     * This is a method needed just because I can't find a way to return an 
     * stream from an EJB method (streams can't be serialized).
     * See coments inside FileManagerBean class code for more information.
     * <p>
     * This is the reason why this method is accesible only from @Local.
     * 
     * @param sSessionId The session ID assigned to client at login
     * @param nFileId The result of invokink <code>File.getId()</code>
     * @param bToWrite <code>true</code> if the file is going to be used to 
     *        write and <code>false</code> if will be used to read only.
     */
    java.io.File getNativeFile( String sSessionId, int nFileId, boolean bToWrite );
}