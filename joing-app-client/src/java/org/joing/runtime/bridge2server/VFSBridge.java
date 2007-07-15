/*
 * VFSBridge.java
 *
 * Created on 18 de junio de 2007, 16:12
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.joing.runtime.bridge2server;

import ejb.vfs.FileBinary;
import ejb.vfs.FileDescriptor;
import ejb.vfs.FileText;
import java.util.List;

/**
 *
 * @author fmorero
 */
public interface VFSBridge
{
    //------------------------------------------------------------------------//
    // VfsFileRemote
    
    FileDescriptor getFile( String sFilePath );
    
    FileDescriptor createDirectory( int nParentId, String sDirName );
    
    FileDescriptor createFile( int nParentId, String sFileName );
    
    FileText readText( int nFileId, String sEncoding );
    
    FileBinary readBinary( int nFileId );
    
    boolean writeText( FileText ft );
    
    boolean writeBinary( FileBinary fb );
    
    FileDescriptor update( FileDescriptor file );
    
    boolean copy( int nFileId, int nToDir );
    
    boolean move( int nFileId, int nToDir );
    
    boolean trashcan( int[] anFileId, boolean bInTrashCan );
    
    boolean trashcan( int nFileID, boolean bInTrashCan );
    
    boolean delete( int[] anFileId );
    
    boolean delete( int nFileId );
    
    //------------------------------------------------------------------------//
    // VfsListRemote
    
    List<FileDescriptor> getChilds( Integer nFileId );
    
    List<FileDescriptor> getChilds( String sBaseDir );
    
    List<FileDescriptor> getByNotes( String sSubString );
    
    List<FileDescriptor> getTrashCan();
}