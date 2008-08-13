/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.joing.common.clientAPI.runtime;

import java.util.List;
import org.joing.common.dto.vfs.VFSFile4IO;
import org.joing.common.dto.vfs.FileDescriptor;
import org.joing.common.exception.JoingServerVFSException;

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
     * Returns a Join'g VFS FileDescriptor instance for passed path.
     * <p>
     * It is assumed that path starts from root ('/') and that corresponds to
     * an existing file in Server.
     * 
     * @param sPath
     * @param bCreateFile 
     * @return
     */
    FileDescriptor getFileDescriptor( String sPath, boolean bCreateFile )
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
    FileDescriptor createDirectories( String sPath )
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
    FileDescriptor createDirectory( String sParent, String sDir )
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
    FileDescriptor createFile( String sParent, String sFileName, boolean bCreateParentDirs )
            throws JoingServerVFSException;
    
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
    VFSFile4IO getFile( FileDescriptor fd )
            throws JoingServerVFSException; 
        
    /**
     * Updates <code>FileDescriptor</code> information (file propeties) 
     * including file name.
     *
     * @param sSessionId The session ID assigned to client at login
     * @param file  An instance of <code>FileDescriptor</code>.
     * @return The recieved instance modified to reflect the updates (one
     *         or more properties would or would not change during the updating 
     *         process), or <code>null</code> if something goes wrong.
     * @throws JoingServerVFSException if any prerequisite was not satisfied or 
     *         a wrapped third-party exception if something went wrong.
     */
    FileDescriptor update( FileDescriptor fd )
            throws JoingServerVFSException;
    
    // Devuelve la lista de los ficheros que no s epuedienron mover o una lista vacía si todo fue bien
    List<FileDescriptor> copy( FileDescriptor fdFromFileOrFolder, FileDescriptor fdToFolder )
            throws JoingServerVFSException;
    
    // Devuelve la lista de los ficheros que no s epuedienron mover o una lista vacía si todo fue bien
    List<FileDescriptor> move( FileDescriptor fdFromFileOrFolder, FileDescriptor fdToFolder )
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
    List<FileDescriptor> trashcan( FileDescriptor fd, boolean bInTrashCan )
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
    List<FileDescriptor> trashcan( List<FileDescriptor> list, boolean bInTrashCan )
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
    List<FileDescriptor> delete( FileDescriptor fd )
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
    List<FileDescriptor> delete( List<FileDescriptor> list )
            throws JoingServerVFSException;
    
    //------------------------------------------------------------------------//
    // VfsListRemote
    
    List<FileDescriptor> getRoots()
            throws JoingServerVFSException;
    
    List<FileDescriptor> getChilds( FileDescriptor fd )
            throws JoingServerVFSException;
    
    List<FileDescriptor> getByNotes( String sSubString, boolean bGlobal )
            throws JoingServerVFSException;
    
    List<FileDescriptor> getTrashCan()
            throws JoingServerVFSException;
}