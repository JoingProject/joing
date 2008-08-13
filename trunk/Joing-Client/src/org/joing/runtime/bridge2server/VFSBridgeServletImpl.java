/*
 * VFSBridgeServletImpl.java
 *
 * Created on 18 de junio de 2007, 16:21
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

package org.joing.runtime.bridge2server;

import java.util.List;
import org.joing.common.CallBackable;
import org.joing.common.dto.vfs.FileDescriptor;
import org.joing.common.clientAPI.runtime.VFSBridge;
import org.joing.runtime.vfs.VFSFileOverArray;
import org.joing.common.exception.JoingServerVFSException;

/**
 *
 *
 * @author Francisco Morero Peyrona
 */
public class VFSBridgeServletImpl
       extends BridgeServletBaseImpl
       implements VFSBridge
{
    /**
     * Creates a new instance of VFSBridgeServletImpl
     * 
     * Package scope: only Runtime class can create instances of this class.
     */
    VFSBridgeServletImpl()
    {
    }
    
    public FileDescriptor getFileDescriptor( String sFilePath, boolean bCreateFile )
            throws JoingServerVFSException
    {
        FileDescriptor file = null;
        
        Channel channel = new Channel( VFS_GET_FILE_DESCRIPTOR );
                channel.write( platform.getBridge().getSessionBridge().getSessionId() );
                channel.write( sFilePath );
                channel.write( bCreateFile );
        file = (FileDescriptor) channel.read();
                channel.close();

        return file;
    }
    
    public FileDescriptor createDirectories( String sPath )
            throws JoingServerVFSException
    {
        FileDescriptor file = null;
        
        Channel channel = new Channel( VFS_CREATE_DIR );
                channel.write( platform.getBridge().getSessionBridge().getSessionId() );
                channel.write( sPath );
        file = (FileDescriptor) channel.read();
                channel.close();
        
        return file;
    }
    
    public FileDescriptor createDirectory( String sParent, String sDirName )
           throws JoingServerVFSException
    {
        FileDescriptor file = null;
        
        Channel channel = new Channel( VFS_CREATE_DIR );
                channel.write( platform.getBridge().getSessionBridge().getSessionId() );
                channel.write( sParent );
                channel.write( sDirName );
        file = (FileDescriptor) channel.read();
                channel.close();
        
        return file;
    }
    
    public FileDescriptor createFile( String sPath, String sFileName, boolean bCreateParentDirs )
           throws JoingServerVFSException
    {
        FileDescriptor file = null;
        
        Channel channel = new Channel( VFS_CREATE_FILE );
                channel.write( platform.getBridge().getSessionBridge().getSessionId() );
                channel.write( sPath );
                channel.write( sFileName );
                channel.write( bCreateParentDirs );
        file = (FileDescriptor) channel.read();
                channel.close();
        
        return file;
    }
    
    public VFSFileOverArray getFile( FileDescriptor fd )
           throws JoingServerVFSException
    {
        VFSFileOverArray foa = null;
        
        Channel channel = new Channel( VFS_GET_FILE );
                channel.write( platform.getBridge().getSessionBridge().getSessionId() );
                channel.write( fd.getId() );
        byte[] abContent = (byte[]) channel.read();
                channel.close();

        foa = new VFSFileOverArray( fd, abContent );
        // Add a callback, so when the OutputStream returned by this method is closed, 
        // the callback will be invoked and the file will be sent to Server to be stored.
        foa.addCallBackListener( new CallBackable()
        {
            public void execute( Object sender )
            {
                 VFSBridgeServletImpl.this.writeFileFromArray( (VFSFileOverArray) sender );
            }
        } );
        
        return foa;
    }
    
    public FileDescriptor update( FileDescriptor fd )
           throws JoingServerVFSException
    {
        FileDescriptor fd2Ret = null;
        
        Channel channel = new Channel( VFS_UPDATE );
                channel.write( platform.getBridge().getSessionBridge().getSessionId() );
                channel.write( fd );
        fd2Ret = (FileDescriptor) channel.read();
                channel.close();

        return fd2Ret;
    }
    
    public List<FileDescriptor> copy( FileDescriptor fdFromFileOrFolder, FileDescriptor fdToFolder )
           throws JoingServerVFSException
    {
        List<FileDescriptor> lstErrors = null;
        
        Channel channel = new Channel( VFS_COPY );
                channel.write( platform.getBridge().getSessionBridge().getSessionId() );
                channel.write( fdFromFileOrFolder.getId() );
                channel.write( fdToFolder.getId() );
        lstErrors = (List<FileDescriptor>) channel.read();
                channel.close();

        return lstErrors;
    }
    
    public List<FileDescriptor> move( FileDescriptor fdFromFileOrFolder, FileDescriptor fdToFolder )
           throws JoingServerVFSException
    {
        List<FileDescriptor> files = null;
        
        Channel channel = new Channel( VFS_MOVE );
                channel.write( platform.getBridge().getSessionBridge().getSessionId() );
                channel.write( fdFromFileOrFolder.getId()  );
                channel.write( fdToFolder.getId() );
        files = (List<FileDescriptor>) channel.read();
                channel.close();

        return files;
    }
    
    public List<FileDescriptor> trashcan( FileDescriptor fd, boolean bInTrashCan )
           throws JoingServerVFSException
    {
        List<FileDescriptor> lstErrors = null;
        
        Channel channel = new Channel( VFS_TRASHCAN );
                channel.write( platform.getBridge().getSessionBridge().getSessionId() );
                channel.write( fd.getId() );
                channel.write( bInTrashCan );
        lstErrors = (List<FileDescriptor>) channel.read();
                channel.close();
        
        return lstErrors;
    }
    
    public List<FileDescriptor> trashcan( List<FileDescriptor> list, boolean bInTrashCan )
           throws JoingServerVFSException
    {
        List<FileDescriptor> lstErrors = null;
        
        // Gets IDs from FileDescriptors in the List
        int[] anIDs = new int[ list.size() ];
        int   n     = 0;
        
        for( FileDescriptor fd : list )
            anIDs[n++] = fd.getId();
        
        // Calling servlet
        Channel channel = new Channel( VFS_TRASHCAN );
                channel.write( platform.getBridge().getSessionBridge().getSessionId() );
                channel.write( anIDs );
                channel.write( bInTrashCan );
        lstErrors = (List<FileDescriptor>) channel.read();
                channel.close();

        return lstErrors;
    }
    
    public List<FileDescriptor> delete( FileDescriptor fd )
           throws JoingServerVFSException
    {
        List<FileDescriptor> lstErrors = null;
        
        Channel channel = new Channel( VFS_DELETE );
                channel.write( platform.getBridge().getSessionBridge().getSessionId() );
                channel.write( fd.getId() );
        lstErrors = (List<FileDescriptor>) channel.read();
                channel.close();
        
        return lstErrors;
    }
    
    public List<FileDescriptor> delete( List<FileDescriptor> list )
           throws JoingServerVFSException
    {
        List<FileDescriptor> lstErrors = null;
        
        // Gets IDs from FileDescriptors in the List
        int[] anIDs = new int[ list.size() ];
        int   n     = 0;
        
        for( FileDescriptor fd : list )
            anIDs[n++] = fd.getId();
        
        // Calling servlet
        Channel channel = new Channel( VFS_DELETE );
                channel.write( platform.getBridge().getSessionBridge().getSessionId() );
                channel.write( anIDs );
        lstErrors = (List<FileDescriptor>) channel.read();
                channel.close();

        return lstErrors;
    }
    
    // To be used internally
    private FileDescriptor writeFileFromArray( VFSFileOverArray foa )
           throws JoingServerVFSException
    {
        FileDescriptor fd = null;
        
        Channel channel = new Channel( VFS_WRITE_FILE );
                channel.write( platform.getBridge().getSessionBridge().getSessionId() );
                channel.write( foa.getFileDescriptor().getId() );
                channel.write( foa.intern() );
        fd = (FileDescriptor) channel.read();
                channel.close();
        
        return fd;
    }
    
    //------------------------------------------------------------------------//
    
    public List<FileDescriptor> getRoots() 
           throws JoingServerVFSException
    {
        List<FileDescriptor> roots = null;
        
        Channel channel = new Channel( VFS_GET_ROOTS );
                channel.write( platform.getBridge().getSessionBridge().getSessionId() );
        roots = (List<FileDescriptor>) channel.read();
                channel.close();
        
        return roots;
    }
    
    public List<FileDescriptor> getChilds( FileDescriptor fd )
           throws JoingServerVFSException
    {
        List<FileDescriptor> files = null;
        
        Channel channel = new Channel( VFS_GET_CHILDS );
                channel.write( platform.getBridge().getSessionBridge().getSessionId() );
                channel.write( fd.getId() );
        files = (List<FileDescriptor>) channel.read();
                channel.close();
        
        return files;
    }
    
    public List<FileDescriptor> getByNotes( String sSubString, boolean bGlobal )
           throws JoingServerVFSException
    {
        List<FileDescriptor> files = null;
     
        Channel channel = new Channel( VFS_GET_BY_NOTES );
                channel.write( platform.getBridge().getSessionBridge().getSessionId() );
                channel.write( sSubString );
        files = (List<FileDescriptor>) channel.read();
                channel.close();
        
        return files;
    }
    
    public List<FileDescriptor> getTrashCan()
           throws JoingServerVFSException
    {
        List<FileDescriptor> files = null;
        
        Channel channel = new Channel( VFS_GET_TRASHCAN );
                channel.write( platform.getBridge().getSessionBridge().getSessionId() );
        files = (List<FileDescriptor>) channel.read();
                channel.close();

        return files;
    }
}