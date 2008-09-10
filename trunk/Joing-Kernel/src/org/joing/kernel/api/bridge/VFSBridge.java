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

package org.joing.kernel.api.bridge;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import org.joing.common.exception.JoingServerVFSException;
import org.joing.kernel.runtime.vfs.VFSFile;

/**
 * This interface describes operations related to three areas:
 * <ul>
 *    <li>Obtaining FileDescriptors for exsisting a created files and folders</li>
 *    <li>Reading and writing text and binary files</li>
 *    <li>File searching and listing</li>
 * </ul>
 * <p>
 * <b>Important</b>: note that even if VFS (Virtual File System) uses a class
 * named FileDescriptor it is not the same as the one used by standard Java IO
 * API:
 * <pre><code>
 *    java.io.FileDescriptor
 *    org.joing.common.dto.vfs.FileDescriptor
 * </code></pre>
 *
 * @author Francisco Morero Peyrona
 */
public interface VFSBridge
{
    //------------------------------------------------------------------------//
    // VfsFileRemote
    
    /**
     * Get a remote (stored in Join'g Server) file.
     * <p>
     * Returned object provides convenience methods to read (OutputStream) and 
     * write (InputStream) file contents.
     * 
     * @param fd FileDescriptor instace representing file to read from.
     * @return An instance of VFSFile4IO.
     * @throws org.joing.common.exception.JoingServerException
     */
    VFSFile getFile( String sFilePath, boolean bCreateFile )
            throws JoingServerVFSException;
    
    /**
     * Creates all directories represented by sPath.<br>
     * If all of them exists, nothing happend and a reference to the last one
     * will be return.
     * 
     * @param sPath A secuence of direcotries starting from root ('/')
     * @return An instance of FileDescriptor representing the last directory 
     *         created or <code>null</code> if something goes wrong.
     * @throws JoingServerVFSException if any prerequisite was not satisfied or 
     *         a wrapped third-party exception if something went wrong.
     */
    VFSFile createDirectories( String sPath )
            throws JoingServerVFSException;
    
    /**
     * Creates a new directory based on a parent directory.
     * <p>
     * Note: to create a bunch of directories in one single operation, use 
     * <code>createDirectories( String sPath )</code>.
     * 
     * @param sParent Parent directory for the new one.
     * @param sDir Directory name. If null, an automatic name will be generated.
     * @param bCreateParentDirs If true, all non existing directories in sParent
     *        will be created.
     * @return The FileDescriptor that represents the new created directory or
     *         null if there were any error.
     * @throws org.joing.common.exception.JoingServerException If any 
     *         prerequisite was not satisfied or a wrapped third-party exception 
     *         if something went wrong.
     * @see #createDirectories(java.lang.String)
     */
    VFSFile createDirectory( String sParent, String sDir )
            throws JoingServerVFSException;
    
    /**
     * Create a file and optionally create all its parent directories (if they
     * do not exists).
     * 
     * @param sParent Path from root ('/') where file will be created.
     * @param sFileName A valid file name.
     * @param bCreateParentDirs  If true, all no existing parent directories 
     *        will be created.
     * @return The FileDescriptor that represents the new created directory or
     *         null if there were any error.
     * @throws org.joing.common.exception.JoingServerVFSException
     */
    VFSFile createFile( String sParent, String sFileName, boolean bCreateParentDirs )
            throws JoingServerVFSException;

    /**
     * Returns an stream to read binary data.
     * 
     * @param file Target file.
     * @return An stream to read binary data.
     */
    InputStream getInputStream( VFSFile file );
    
    /**
     * Returns an stream to write binary data.
     * 
     * @param file Target file.
     * @param bAppend Create the stream in append mode.
     * @return An stream to write binary data.
     */
    OutputStream getOutputStream( VFSFile file, boolean bAppend );
    
    /**
     * Updates <code>VFSFile</code> information (file properties) including file
     * name.
     *
     * @param sSessionId The session ID assigned to client at login
     * @param file  An instance of <code>VFSFile</code>.
     * @return The modified instance reflecting the updates (one or more 
     *         properties would or would not change during the updating 
     *         process), or <code>null</code> if something goes wrong.
     * @throws JoingServerVFSException if any prerequisite was not satisfied or 
     *         a wrapped third-party exception if something went wrong.
     */
    VFSFile update( VFSFile file )
            throws JoingServerVFSException;
    
    /**
     * Copy files from one folder to another folder.
     * 
     * @param fFromFileOrFolder Source folder.
     * @param fToFolder Destination folder.
     * @return The list of files that failed to be copied or an empty list if 
     *         all files were moved.
     * @throws org.joing.common.exception.JoingServerVFSException
     */
    List<VFSFile> copy( VFSFile fFromFileOrFolder, VFSFile fToFolder )
            throws JoingServerVFSException;
    
    /**
     * Move files from one folder to another folder.
     * 
     * @param fFromFileOrFolder Source folder.
     * @param fToFolder Destination folder.
     * @return The list of files that failed to be moved or an empty list if all
     *         files were moved.
     * @throws org.joing.common.exception.JoingServerVFSException
     */
    List<VFSFile> move( VFSFile fFromFileOrFolder, VFSFile fToFolder )
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
     * @param fd The <code>FileDescriptor</code> representing the file or 
     *        directory to be moved.
     * @param bInTrashCan <code>true</code> to place the file (or dir) in
     *        trashcan and <code>false</code> to move it in its original place.
     * @return A list of <code>FileDescriptor</code> instances representing
     *         those files and directories that failed when moving them to or 
     *         from trashcan.
     * @throws JoingServerVFSException if any prerequisite was not satisfied or 
     *         a wrapped third-party exception if something went wrong.
     */
    List<VFSFile> toTrashcan( VFSFile file, boolean bInTrashCan )
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
     * @param list A list of <code>FileDescriptor</code> representing the files
     *        and directories to be moved.
     * @param bInTrashCan <code>true</code> to place the file (or dir) in
     *        trashcan and <code>false</code> to move it in its original place.
     * @return A list of <code>FileDescriptor</code> instances representing
     *         those files and directories that failed when moving them to or 
     *         from trashcan.
     * @throws JoingServerVFSException if any prerequisite was not satisfied or 
     *         a wrapped third-party exception if something went wrong.
     */
    List<VFSFile> toTrashcan( List<VFSFile> list, boolean bInTrashCan )
            throws JoingServerVFSException;
    
    /**
     * Deletes one file or recursively one directory (in a permanent way).
     *
     * @param sSessionId The session ID assigned to client at login
     * @param fd The <code>FileDescriptor</code> representing the file or 
     *        directory to be deleted.
     * @return A list of <code>FileDescriptor</code> instances representing
     *         those files and directories that failed to be deleted.
     * @throws JoingServerVFSException if any prerequisite was not satisfied or 
     *         a wrapped third-party exception if something went wrong.
     */
    List<VFSFile> delete( VFSFile file )
            throws JoingServerVFSException;
    
    /**
     * Recursively deletes a group of files or directories (in a permanent way).
     *
     * @param list A list of <code>FileDescriptor</code> representing the files
     *        and directories to be moved.
     * @return A list of <code>FileDescriptor</code> instances representing
     *         those files and directories that failed to be deleted.
     * @throws JoingServerVFSException if any prerequisite was not satisfied or 
     *         a wrapped third-party exception if something went wrong.
     */
    List<VFSFile> delete( List<VFSFile> list )
            throws JoingServerVFSException;
    
    //------------------------------------------------------------------------//
    // VfsListRemote
    
    List<VFSFile> getRoots()
            throws JoingServerVFSException;
    
    List<VFSFile> getChildren( VFSFile file )
            throws JoingServerVFSException;
    
    List<VFSFile> getByNotes( String sSubString, boolean bGlobal )
            throws JoingServerVFSException;
    
    List<VFSFile> getTrashCan()
            throws JoingServerVFSException;
}