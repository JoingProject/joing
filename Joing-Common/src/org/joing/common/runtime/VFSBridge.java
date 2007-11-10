/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.joing.common.runtime;

import java.util.List;
import org.joing.common.dto.vfs.FileBinary;
import org.joing.common.dto.vfs.FileDescriptor;
import org.joing.common.dto.vfs.FileText;

/**
 *
 * @author antoniovl
 */
public interface VFSBridge {
//------------------------------------------------------------------------//
    // VfsFileRemote
    
    FileDescriptor getFile( String sFilePath );
    
    FileDescriptor createDirectory( String sPath, String sDirName );
    
    FileDescriptor createFile( String sPath, String sFileName );
    
    FileText readText( int nFileId, String sEncoding );
    
    FileBinary readBinary( int nFileId );
    
    FileDescriptor writeText( FileText ft );
    
    FileDescriptor writeBinary( FileBinary fb );
    
    FileDescriptor update( FileDescriptor file );
    
    boolean copy( int nFileId, int nToDir );
    
    boolean move( int nFileId, int nToDir );
    
    boolean trashcan( int[] anFileId, boolean bInTrashCan );
    
    boolean trashcan( int nFileID, boolean bInTrashCan );
    
    boolean delete( int[] anFileId );
    
    boolean delete( int nFileId );
    
    //------------------------------------------------------------------------//
    // VfsListRemote
    
    List<FileDescriptor> getRoots();
    
    List<FileDescriptor> getChilds( Integer nFileId );
    
    List<FileDescriptor> getChilds( String sBaseDir );
    
    List<FileDescriptor> getByNotes( String sSubString );
    
    List<FileDescriptor> getTrashCan();
}
