/*
 * ListManagerRemote.java
 *
 * Created on 3 de junio de 2007, 13:18
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

import java.util.List;
import javax.ejb.Remote;
import org.joing.common.dto.vfs.FileDescriptor;
import org.joing.common.exception.JoingServerVFSException;

/**
 * This is the business interface for VfsFile enterprise bean.
 *
 * @author Francisco Morero Peyrona
 */
@Remote
public interface ListManagerRemote
{
    /**
     * List all roots that user has.
     * <p>
     * The number of roots per community and user is always 1, but a user can 
     * belong to more than one community.
     * 
     * @param sSessionId The client session ID
     * @return All roots that user has
     * @throws If any prerequisite was not satisfied or 
     *         a wrapped third-party exception if something went wrong.
     */
    List<FileDescriptor> getRoots( String sSessionId )
            throws JoingServerVFSException;
    
    /**
     * Get all files in passed directory ID.
     * <p>
     * <code>nDirId</code> is assumed to represent a directory , if it is not or
     * the <code>nDirId</code> code does not represent a valid directory in the
     * user file space, an <code>JoingServerVFSException</code> will be thrown.
     * <p>
     * Note: this method is preferred (it is much faster) over the others.
     *
     * @param sSessionId The client session ID
     * @param nDirId A <code>File.id</code> that belongs to directory.
     * @return All files in passed directory ID or <code>null</code> if
     *         sSessionId and/or nFileId are invalid.
     * @throws If any prerequisite was not satisfied or 
     *         a wrapped third-party exception if something went wrong.
     */
    List<FileDescriptor> getChilds( String sSessionId, int nDirId )
            throws JoingServerVFSException;
    
    /**
     * Searches files that contain passed string in the 'Notes' associated field.
     * <p>
     * It searches even in hidden files and files in trashcan.
     * <p>
     * If the total number of files is high, this method can take quite a lot of
     * time to be executed.
     *
     * @param sSessionId The client session ID
     * @param sSubString Sub-string to be searched in 'Notes' field
     * @param bGlobal If true searches in all public files on all users of the
     *        same community, if false searches on all files (public or not) of
     *        current user only.
     * @throws If any prerequisite was not satisfied or 
     *         a wrapped third-party exception if something went wrong.
     */
    List<FileDescriptor> getByNotes( String sSessionId, String sSubString, boolean bGlobal )
            throws JoingServerVFSException; 

    /**
     * Return all files in trash can.
     *
     * @param sSessionId The client session ID
     * @return All files in trash can or <code>null</code> if sSessionId is 
     *         invalid.
     * @throws If any prerequisite was not satisfied or 
     *         a wrapped third-party exception if something went wrong.
     */
    List<FileDescriptor> getTrashCan( String sSessionId )
            throws JoingServerVFSException;
}