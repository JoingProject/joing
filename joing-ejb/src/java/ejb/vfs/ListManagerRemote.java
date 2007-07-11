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

/**
 * This is the business interface for VfsFile enterprise bean.
 *
 * @author Francisco Morero Peyrona
 */
@Remote
public interface ListManagerRemote
{
    /**
     * Get all files in passed directory ID.
     * <p>
     * <code>nFileDirId</code> is assumed either to represent a directory or
     * be <code>null</code>. If it is not, <code>null</code> will be returned.
     * <p>
     * To get files in root ("/") directory, pass <code>0</code> (zero) as 
     * <code>nFileDirId</code>.
     * <p>
     * Note: this method is preferred (it is much faster) over the other one.
     *
     * @param sSessionId The client session ID
     * @param nFileId A <code>File.id</code> that belongs to directory entry 
     *        or <code>0</code> (zero).
     * @return All files in passed directory ID or <code>null</code> if
     *         sSessionId and/or nFileId are invalid.
     */
    List<File> getChilds( String sSessionId, int nFileId );
    
    /**
     * Get all files in passed directory.
     * <p>
     * <code>sBaseDir</code> is assumed to be a valid directory: has to be a
     * directory and the route has to be complete (starting at root "/"). 
     * Otherwise, <code>null</code> will be returned.
     * <p>
     * To get files in root ("/") directory, pass <code>null</code> or empry 
     * ("") or root ("/") string.
     * <p>
     * Note: whenever possible, use the method 
     * <code>getChilds( String sSessionId, Integer nFileId )</code>
     * instead of this one (the other one is much faster).
     *
     * @param sSessionId The client session ID
     * @param sDirPath A path (from root) representing a directory entry
     * @return All files in passed directory or <code>null</code> if
     *         sSessionId and/or sDirPath are invalid.
     */
    List<File> getChilds( String sSessionId, String sDirPath );
    
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
     */
    List<File> getByNotes( String sSessionId, String sSubString );
    
    /**
     * Return all files in trash can.
     *
     * @param sSessionId The client session ID
     * @return All files in trash can or <code>null</code> if sSessionId is 
     *         invalid.
     */
    List<File> getTrashCan( String sSessionId );
}