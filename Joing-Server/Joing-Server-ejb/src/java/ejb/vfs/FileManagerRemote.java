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

import org.joing.common.dto.vfs.FileBinary;
import org.joing.common.dto.vfs.FileDescriptor;
import org.joing.common.dto.vfs.FileText;
import org.joing.common.exception.JoingServerSessionException;
import javax.ejb.Remote;
import org.joing.common.exception.JoingServerVFSException;

/**
 * This is the business interface for VfsFileManager enterprise bean.
 */
@Remote
public interface FileManagerRemote
{
    /** 
     * Returns an instance of class <code>File</code> that represents the file 
     * or directory denoted by passed path.
     *
     * @param sSessionId The session ID assigned to client at login
     * @param sFilePath Path starting at root ("/") and ending with the file or
     *        directory name.
     * @return An instance of class <code>File</code> that represents the file 
     *         or directory denoted by passed path or <code>null</code> if the 
     *         path does not corresponds with an existing file or passed Session 
     *         ID is invalid.
     * @throws JoingServerVFSException if any prerequisite was not satisfied or 
     *         a wrapped third-party exception if something went wrong.
     * @throws JoingServerSessionException
     */
    FileDescriptor getFile( String sSessionId, String sFilePath )
            throws JoingServerVFSException, JoingServerSessionException;
    
    /**
     * Updates <code>File</code> information (file propeties) including file
     * name.
     *
     * @param sSessionId The session ID assigned to client at login
     * @param file  An instance of <code>File</code>.
     * @return A new reference to file after performing the update action (one
     *         or more properties could change during the updating process), or
     *         <code>null</code> if something goes wrong.
     * @throws JoingServerVFSException if any prerequisite was not satisfied or 
     *         a wrapped third-party exception if something went wrong.
     */
    FileDescriptor updateFile(  String sSessionId, FileDescriptor file  )
            throws JoingServerVFSException;
    
    /**
     * Creates a new emtpy directory.
     * 
     * File and directory names must follow these rules:
     * <ul>
     *    <li>These characters are not accepted: 
     *     // TODO: buscarlos
     *    <li>Maximum name length is 255 characters
     *    <li>Minimum name length is 1 character
     *    <li>Can not be duplicated (already existing)
     * </ul>
     *  
     * @param sSessionId The session ID assigned to client at login
     * @param sPath Dir path from root excluding dir name.
     * @param sDirName Name of the directory to be created (if null an automatic 
     *                 one is generated).
     * @return An instance of class <code>File</code> or <code>null</code> if
     *         something goes wrong.
     * @throws JoingServerVFSException if any prerequisite was not satisfied or 
     *         a wrapped third-party exception if something went wrong.

     */
    FileDescriptor createDirectory( String sSessionId, String sPath, String sDirName ) 
            throws JoingServerVFSException;
    
    /**
     * Creates a new emtpy file.
     * 
     * File and directory names must follow these rules:
     * <ul>
     *    <li>These characters are not accepted: 
     *     // TODO: buscarlos
     *    <li>Maximum name length is 255 characters
     *    <li>Minimum name length is 1 character
     *    <li>Can not be duplicated (already existing)
     * </ul>
     *
     * @param sSessionId The session ID assigned to client at login
     * @param sPath File path from root excluding file name.
     * @param sFileName Name of the file to be created (if null an automatic one
     *                  is generated).
     * @return An instance of class <code>File</code> or <code>null</code> if
     *         something goes wrong.
     * @throws JoingServerVFSException if any prerequisite was not satisfied or 
     *         a wrapped third-party exception if something went wrong.
     */
    FileDescriptor createFile( String sSessionId, String sPath, String sFileName )
            throws JoingServerVFSException;
    
    /**
     * Returns an instance of FileText to read requested text file using 
     * requested encoding.
     *
     * @param sSessionId The session ID assigned to client at login
     * @param nFileId The result of invokink <code>File.getId()</code>
     * @param sEncoding A valid encondig (see )
     * @return An instance of <code>FileText</code> class that wraps an
     *         stream where the contents of the file will be dropped.
     *         Or <code>null</code> if something went wrong.
     * @throws JoingServerVFSException if any prerequisite was not satisfied or 
     *         a wrapped third-party exception if something went wrong.
     * @see #writeText
     */
    FileText readTextFile( String sSessionId, int nFileId, String sEncoding )
            throws JoingServerVFSException;
    
    /**
     * Returns an instance of FileBinary to read requested binary file.
     *
     * @param sSessionId The session ID assigned to client at login
     * @param nFileId The result of invokink <code>File.getId()</code>
     * @return An instance of <code>FileBinary</code> class that wraps an
     *         stream where the contents of the file will be dropped.
     *         Or <code>null</code> if something went wrong.
     * @throws JoingServerVFSException if any prerequisite was not satisfied or 
     *         a wrapped third-party exception if something went wrong.
     * @see #writeText
     */
    FileBinary readBinaryFile( String sSessionId, int nFileId )
            throws JoingServerVFSException;
    
    /**
     * Writes a text file contents to disk.
     * <p>
     * Internally, all text files are stored in Unicode (UTF-16), which is the
     * internal representation in Java.<br>
     * And they are decoded to requested charset mean while are being served.
     * <p>
     * For more information, visit:
     * http://java.sun.com/javase/technologies/core/basic/intl/faq.jsp
     *
     * @param sSessionId The session ID assigned to client at login
     * @param nFileId The result of invokink <code>File.getId()</code>
     * @param reader
     * @param sEncoding
     * @return The updated (new file size) <code>FileDescriptor<code> that 
               represents this text file.
     * @throws JoingServerVFSException if any prerequisite was not satisfied or 
     *         a wrapped third-party exception if something went wrong.
     * @see #writeText
     */
    FileDescriptor writeTextFile( String sSessionId, FileText fileText )
            throws JoingServerVFSException;
    
    /**
     * Writes a binary file contents to disk.
     * 
     * @param sSessionId The session ID assigned to client at login
     * @param nFileId The result of invokink <code>File.getId()</code>
     * @param reader
     * @return The updated (new file size) <code>FileDescriptor<code> that 
               represents this text file.
     * @throws JoingServerVFSException if any prerequisite was not satisfied or 
     *         a wrapped third-party exception if something went wrong.
     */
    FileDescriptor writeBinaryFile( String sSessionId, FileBinary fileBinary )
            throws JoingServerVFSException;
    
    boolean copy( String sSessionId, int nFileId, int nToDirId )
            throws JoingServerVFSException;
    
    boolean move( String sSessionId, int nFileId, int nToDirId )
            throws JoingServerVFSException;
    
    /**
     * Recursively moves a group files and directories from and to the trashcan.
     * Those files that already were in trashcan will be ignored.
     * <p>
     * The only reason by which a file can not be moved to the trashcan (if no
     * exception is thrown) is when the file does not exists. Returned IDs are 
     * invalid IDs.
     * <p>
     * Note: In Linux, moving files to and from TrashCan, does not change 
     *       ACCESSED flag, so this method does not do it neither.
     * 
     * 
     * @param sSessionId The session ID assigned to client at login.
     * @param anFileId An array with files ID (the result of invokink 
     *        <code>File.getId()</code>)
     * @param bInTrashCan <code>true</code> to place the file (or dir) in
     *        trashcan and <code>false</code> to move it in its original place.
     * @return An array with those ID files failed to be moved to or from 
     *         trashcan.
     * @throws JoingServerVFSException if any prerequisite was not satisfied or 
     *         a wrapped third-party exception if something went wrong.
     */
    int[] trashcan(String sSessionId, int[] anFileId, boolean bInTrashCan )
            throws JoingServerVFSException;
    
    /**
     * Recursively moves a file or directory from and to the trashcan.
     * If files were already in trash
     * <p>
     * The only reason by which a file can not be moved to the trashcan (if no
     * exception is thrown) is when the file does not exists. Therefore, 
     * returning <code>false</code> means that passed ID is invalid.
     * <p>
     * Note: In Linux, moving files to and from TrashCan, does not change 
     *       ACCESSED flag, so this method does not do it neither.
     * 
     * 
     * @param sSessionId The session ID assigned to client at login.
     * @param nFileId The result of invokink <code>File.getId()</code>
     * @param bInTrashCan <code>true</code> to place the file (or dir) in
     *        trashcan and <code>false</code> to move it in its original place.
     * @return An array with those ID files failed to be moved to or from 
     *         trashcan.
     * @throws JoingServerVFSException if any prerequisite was not satisfied or 
     *         a wrapped third-party exception if something went wrong.
     */
    int[] trashcan( String sSessionId, int nFileId , boolean bInTrashCan )
            throws JoingServerVFSException;
    
    /**
     * Recursively deletes a bunch of files or directories (in a permanent way).
     *
     * @param sSessionId The session ID assigned to client at login
     * @param anFileId The result of invokink <code>File.getId()</code>
     * @return An array with those ID files failed to be deleted.
     * @throws JoingServerVFSException if any prerequisite was not satisfied or 
     *         a wrapped third-party exception if something went wrong.
     */
    int[] delete( String sSessionId, int[] anFileId )
            throws JoingServerVFSException;

    /**
     * Recursively deletes a file or directory (in a permanent way).
     *
     * @param sSessionId The session ID assigned to client at login
     * @param nFileId The result of invokink <code>File.getId()</code>
     * @return An array with those ID files failed to be deleted.
     * @throws JoingServerVFSException if any prerequisite was not satisfied or 
     *         a wrapped third-party exception if something went wrong.
     */
    int[] delete( String sSessionId, int nFileId )
            throws JoingServerVFSException;
}