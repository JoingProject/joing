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
package org.joing.server.ejb.vfs;

import java.io.InputStream;
import java.util.List;
import javax.ejb.Remote;
import org.joing.common.dto.vfs.VFSFileBase;
import org.joing.common.exception.JoingServerVFSException;

/**
 * This is the business interface for VfsFileManager enterprise bean.
 * <p>
 * Note: All methods here recieve Strings (instead of FileDescriptor instances).
 * It is because I believe it is faster to find a file in DB on the Server side 
 * than to serialize at Client side an instance of FileDescritor, send it over 
 * the Internet and deserialize it at Server side.
 */
@Remote
public interface FileManagerRemote
{
    /** 
     * Returns an instance of class <code>VFSFile</code> that represents the 
     * file or folder denoted by passed path.
     *
     * @param sSessionId The session ID assigned to client at login
     * @param sFilePath Path starting at root ("/") and ending with the file or
     *        directory name.
     * @param bCreateIfNotExists Creates the file (and all necessary intermediate
     *        folders) if file does not exists.
     * @return An instance of class <code>VFSFile</code> that represents the file 
     *         or directory denoted by passed path or <code>null</code> if the 
     *         path does not corresponds with an existing file or passed Session 
     *         ID is invalid.
     */
    VFSFileBase getFile( String sSessionId, String sFilePath, boolean bCreateIfNotExists );
    
    /**
     * Creates all directories represented by sPath.<br>
     * If all of them exists, nothing happend and a reference to the last one
     * will be return.
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
     * @param sPath A secuence of direcotries starting from root ('/')
     * @return An instance of <code>VFSFile</code> representing the last directory 
     *         created or <code>null</code> if something goes wrong.
     * @throws JoingServerVFSException if any prerequisite was not satisfied or 
     *         a wrapped third-party exception if something went wrong.
     */
    VFSFileBase createDirectories( String sSessionId, String sPath )
            throws JoingServerVFSException;
            
    /**
     * Creates a new directory based on a parent directory and optionally all 
     * non existing parent directories.
     * 
     * File and directory names must follow these rules:
     * <ul>
     *    <li>These characters are not accepted: 
     *     // TODO: buscarlos
     *    <li>Maximum name length is 255 characters
     *    <li>Minimum name length is 1 character
     *    <li>Can not be duplicated (already existing)
     * </ul>
     * <p>
     * Note: to create a bunch of directories in one single operation, use 
     * <code>createDirectories( String sSessionId, String sPath )</code>.
     * 
     * @param sSessionId The session ID assigned to client at login
     * @param sParent Parent path from root excluding dir name.
     * @param sDir Name of the directory to be created. If null, an automatic
     *             name will be generated.
     * @param bCreateParentDirs If true, all non existing directories in sParent
     *        will be created.
     * @return An instance of <code>VFSFile</code> representing the last directory 
     *         created (sDir) or <code>null</code> if something goes wrong.
     * @throws JoingServerVFSException if any prerequisite was not satisfied or 
     *         a wrapped third-party exception if something went wrong.
     * @see #createDirectories(java.lang.String, java.lang.String) 
     */
    VFSFileBase createDirectory( String sSessionId, String sPath, String sDir ) 
            throws JoingServerVFSException;
    
    /**
     * Creates a new emtpy file, and optionally all non existing parent 
     * directories.
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
     * @param bCreateParentDirs If true, all non existing directories in sParent
     *        will be created.
     * @return An instance of class <code>VFSFile</code> or <code>null</code> if
     *         something goes wrong.
     * @throws JoingServerVFSException if any prerequisite was not satisfied or 
     *         a wrapped third-party exception if something went wrong.
     */
    VFSFileBase createFile( String sSessionId, String sPath, String sFileName, boolean bCreateParentDirs )
            throws JoingServerVFSException;
    
    /**
     * Returns an <code>InputStream</code> to read from file. 
     * <p>
     * It is invoker resposability to close the stream after finishing with it.
     * 
     * @param sSessionId The session ID assigned to client at login
     * @param nFileId An unique file identifier (normaly obtained by invoking:
     *        <code>VFSFile::getHandler()</code> method).
     * @return An instance of <code>InputStream</code> class that access to file
     *         contents. Internally the file can be strored into native FS, a DB
     *         or any other layer. Returns <code>null</code> if something went 
     *         wrong.
     * @throws JoingServerVFSException if any prerequisite was not satisfied or 
     *         a wrapped third-party exception if something went wrong.
     * @see #writeFile
     */
    InputStream readFile( String sSessionId, int nFileId )
            throws JoingServerVFSException;
    
    /**
     * Writes contents from passed <code>InputStream</code>.
     * <p>
     * This method reads from passed stream and store the contents in system
     * physical layer.<br>
     * Internally the file can be strored into native FS, a DB or any other 
     * layer.
     * 
     * @param sSessionId The session ID assigned to client at login.
     * @param nFileId An unique file identifier (normaly obtained by invoking:
     *        <code>VFSFile::getHandler()</code> method).
     * @param reader An <code>InputStream</code> to read file contents.
     * @return The updated (new file size, ...) <code>VFSFile<code> that 
               represents this text file.
     * @throws JoingServerVFSException if any prerequisite was not satisfied or 
     *         a wrapped third-party exception if something went wrong.
     * @see #readFile
     */
    VFSFileBase writeFile( String sSessionId, int nFileId, InputStream reader )
            throws JoingServerVFSException;
    
    /**
     * Updates <code>FileDescriptor</code> information (file propeties) 
     * including file name.
     *
     * @param sSessionId The session ID assigned to client at login.
     * @param file An instance of <code>VFSFile</code>.
     * @return The recieved instance modified to reflect the updates (one or
     *         more properties would or would not change during the updating 
     *         process), or <code>null</code> if something goes wrong.
     * @throws JoingServerVFSException if any prerequisite was not satisfied or 
     *         a wrapped third-party exception if something went wrong.
     */
    VFSFileBase updateFile( String sSessionId, VFSFileBase file )
            throws JoingServerVFSException;
    
    /**
     * Copy (duplicate) files and folders from source to destination.
     * 
     * @param sSessionId sSessionId The session ID assigned to client at login.
     * @param nFileId An unique file (or folder) identifier representing the
     *        source.
     * @param nToDirId An unique folder identifier representing the destination.
     * @return // NEXT: documentar
     * @throws org.joing.common.exception.JoingServerVFSException
     */
    List<VFSFileBase> copy( String sSessionId, int nFileId, int nToDirId )
            throws JoingServerVFSException;
    
    /**
     * Moves files and folders from source to destination.
     * 
     * @param sSessionId sSessionId The session ID assigned to client at login.
     * @param nFileId An unique file (or folder) identifier representing the
     *        source.
     * @param nToDirId An unique folder identifier representing the destination.
     * @return // NEXT: documentar
     * @throws org.joing.common.exception.JoingServerVFSException
     */
    List<VFSFileBase> move( String sSessionId, int nFileId, int nToDirId )
            throws JoingServerVFSException;
    
    /**
     * Recursively moves a group files and directories from and to the trashcan.
     * Those files that already were in trashcan will be ignored.
     * <p>
     * The only reason by which a file can not be moved to the trashcan (if no
     * exception is thrown) is when the file does not exists: returned IDs are 
     * invalid IDs. A file marked as not deleteable can be moved to trashcan, 
     * because can be moved back from trashcan, but can not be removed from
     * system.
     * <p>
     * Note: In Linux, moving files to and from TrashCan, does not change 
     *       ACCESSED flag, so this method does not do it neither.
     *
     * @param sSessionId The session ID assigned to client at login.
     * @param anFileId An array with files ID (the result of invoking
     *        <code>VFSFile.getHandler()</code>) representing files (or folders) 
     *        to send to trashcan.
     * @param bInTrashCan <code>true</code> to place the file (or dir) in
     *        trashcan and <code>false</code> to move it in its original place.
     * @return A list of <code>VFSFile</code> instances representing
     *         those files and directories that failed when moving them to or 
     *         from trashcan.
     * @throws JoingServerVFSException if any prerequisite was not satisfied or 
     *         a wrapped third-party exception if something went wrong.
     */
    List<VFSFileBase> toTrashcan( String sSessionId, int[] anFileId, boolean bInTrashCan )
            throws JoingServerVFSException;
    
    /**
     * Moves one file or recursively one directory from and to the trashcan.
     * Those files that already were in trashcan will be ignored.
     * <p>
     * The only reason by which a file can not be moved to the trashcan (if no
     * exception is thrown) is when the file does not exists: returned IDs are 
     * invalid IDs. A file marked as not deleteable can be moved to trashcan, 
     * because can be moved back from trashcan, but can not be removed from
     * system.
     * <p>
     * Note: In Linux, moving files to and from TrashCan, does not change 
     *       ACCESSED flag, so this method does not do it neither.
     * 
     * @param sSessionId The session ID assigned to client at login.
     * @param nFileId A file ID (the result of invoking
     *        <code>VFSFile.getHandler()</code>) representing files (or folders) 
     *        to send to trashcan.
     * @param bInTrashCan <code>true</code> to place the file (or dir) in
     *        trashcan and <code>false</code> to move it in its original place.
     * @return A list of <code>VFSFile</code> instances representing
     *         those files and directories that failed when moving them to or 
     *         from trashcan.
     * @throws JoingServerVFSException if any prerequisite was not satisfied or 
     *         a wrapped third-party exception if something went wrong.
     */
    List<VFSFileBase> toTrashcan( String sSessionId, int nFileId , boolean bInTrashCan )
            throws JoingServerVFSException;
    
    /**
     * Recursively deletes a group of files or directories (in a permanent way).
     *
     * @param sSessionId The session ID assigned to client at login
     * @param anFileId The result of invokink <code>File.getId()</code>
     * @return A list of <code>VFSFile</code> instances representing
     *         those files and directories that failed to be deleted.
     * @throws JoingServerVFSException if any prerequisite was not satisfied or 
     *         a wrapped third-party exception if something went wrong.
     */
    List<VFSFileBase> delete( String sSessionId, int[] anFileId )
            throws JoingServerVFSException;

    /**
     * Deletes one file or recursively one directory (in a permanent way).
     *
     * @param sSessionId The session ID assigned to client at login
     * @param nFileId The result of invokink <code>File.getId()</code>
     * @return A list of <code>VFSFile</code> instances representing
     *         those files and directories that failed to be deleted.
     * @throws JoingServerVFSException if any prerequisite was not satisfied or 
     *         a wrapped third-party exception if something went wrong.
     */
    List<VFSFileBase> delete( String sSessionId, int nFileId )
            throws JoingServerVFSException;
}