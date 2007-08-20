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
     * Create a FileEntity that represents the root directory for the passed
     * account.
     * The entity is returned but it is not persisted by this method.
     * @param sAccount A valid user account ID
     * @return A FileEntity that represents the root directory for the passed
     */
    public FileEntity createRootEntity( String sAccount );
}