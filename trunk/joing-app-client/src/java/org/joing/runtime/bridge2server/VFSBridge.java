/*
 * VFSBridge.java
 *
 * Created on 18 de junio de 2007, 16:12
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.joing.runtime.bridge2server;

import ejb.vfs.File;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.util.List;

/**
 *
 * @author fmorero
 */
public interface VFSBridge
{
    //------------------------------------------------------------------------//
    // VfsFileRemote
    
    File getFile( String sFilePath );
    
    File createDirectory( int nParentId, String sDirName );
    
    File createFile( int nParentId, String sFileName );
    
    BufferedReader readText( int nFileId, String sEncoding );
    
    FileInputStream readBinary( int nFileId );
    
    boolean writeText( int nFileId, BufferedReader reader, String sEncoding );
    
    boolean writeBinary( int nFileId, FileInputStream reader );
    
    File update( File file );
    
    boolean copy( int nFileId, int toDir );
    
    boolean move( int nFileId, int toDir );
    
    boolean trashcan( int[] anFileId, boolean bInTrashCan );
    
    boolean trashcan( int nFileID, boolean bInTrashCan );
    
    boolean delete( int[] anFileId );
    
    boolean delete( int nFileId );
    
    //------------------------------------------------------------------------//
    // VfsListRemote
    
    List<File> getChilds( Integer nFileId );
    
    List<File> getChilds( String sBaseDir );
    
    List<File> getByNotes( String sSubString );
    
    List<File> getTrashCan();
}