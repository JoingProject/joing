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

import ejb.vfs.FileBinary;
import ejb.vfs.FileDescriptor;
import ejb.vfs.FileText;
import java.util.List;

/**
 *
 *
 * @author Francisco Morero Peyrona
 */
public class VFSBridgeServletImpl
       extends BridgeServletBaseImpl
       implements VFSBridge
{
    //------------------------------------------------------------------------//
    
    /**
     * Creates a new instance of VFSBridgeServletImpl
     * 
     * Package scope: only Runtime class can create instances of this class.
     */
    VFSBridgeServletImpl()
    {
    }

    public FileDescriptor getFile( String sFilePath )
    {
        FileDescriptor file = null;
        
        try
        {
            Channel channel = new Channel( VFS_GET_FILE );
                    channel.write( Bridge2Server.getInstance().getSessionId() );
                    channel.write( sFilePath );
                    file = (FileDescriptor) channel.read();
                    channel.close();
        }
        catch( Exception exc )
        {
            org.joing.runtime.Runtime.getRuntime().showException( exc, "Error communicating with the server" );
        }
        
        return file;
    }

    public FileDescriptor createDirectory( int nParentId, String sDirName )
    {
        FileDescriptor file = null;
        
        try
        {
            Channel channel = new Channel( VFS_CREATE_DIR );
                    channel.write( Bridge2Server.getInstance().getSessionId() );
                    channel.write( nParentId );
                    channel.write( sDirName  );
                    file = (FileDescriptor) channel.read();
                    channel.close();
        }
        catch( Exception exc )
        {
            org.joing.runtime.Runtime.getRuntime().showException( exc, "Error communicating with the server" );
        }
        
        return file;
    }

    public FileDescriptor createFile( int nParentId, String sFileName )
    {
        FileDescriptor file = null;
        
        try
        {
            Channel channel = new Channel( VFS_CREATE_FILE );
                    channel.write( Bridge2Server.getInstance().getSessionId() );
                    channel.write( nParentId );
                    channel.write( sFileName );
                    file = (FileDescriptor) channel.read();
                    channel.close();
        }
        catch( Exception exc )
        {
            org.joing.runtime.Runtime.getRuntime().showException( exc, "Error communicating with the server" );
        }
        
        return file;
    }

    public FileText readText( int nFileId, String sEncoding )
    {
        FileText file = null;
        
        try
        {
            Channel channel = new Channel( VFS_READ_TEXT_FILE );
                    channel.write( Bridge2Server.getInstance().getSessionId() );
                    channel.write( nFileId );
                    channel.write( sEncoding );
                    file = (FileText) channel.read();
                    channel.close();
        }
        catch( Exception exc )
        {
            org.joing.runtime.Runtime.getRuntime().showException( exc, "Error communicating with the server" );
        }
        
        return file;
    }

    public FileBinary readBinary( int nFileId )
    {
        FileBinary file = null;
        
        try
        {
            Channel channel = new Channel( VFS_READ_BINARY_FILE );
                    channel.write( Bridge2Server.getInstance().getSessionId() );
                    channel.write( nFileId );
                    file = (FileBinary) channel.read();
                    channel.close();
        }
        catch( Exception exc )
        {
            org.joing.runtime.Runtime.getRuntime().showException( exc, "Error communicating with the server" );
        }
        
        return file;
    }

    public boolean writeText( FileText file )
    {
        boolean bSuccess = false;
        
        try
        {
            Channel channel = new Channel( VFS_WRITE_TEXT_FILE );
                    channel.write( Bridge2Server.getInstance().getSessionId() );
                    channel.write( file );
            bSuccess = (Boolean) channel.read();
                    channel.close();
        }
        catch( Exception exc )
        {
            org.joing.runtime.Runtime.getRuntime().showException( exc, "Error communicating with the server" );
        }
        
        return bSuccess;
    }

    public boolean writeBinary( FileBinary file )
    {
        boolean bSuccess = false;
        
        try
        {
            Channel channel = new Channel( VFS_WRITE_BINARY_FILE );
                    channel.write( Bridge2Server.getInstance().getSessionId() );
                    channel.write( file );
            bSuccess = (Boolean) channel.read();
                    channel.close();
        }
        catch( Exception exc )
        {
            org.joing.runtime.Runtime.getRuntime().showException( exc, "Error communicating with the server" );
        }
        
        return bSuccess;
    }

    public FileDescriptor update( FileDescriptor file  )
    {
        try
        {
            Channel channel = new Channel( VFS_UPDATE );
                    channel.write( Bridge2Server.getInstance().getSessionId() );
                    channel.write( file );
                    file = (FileDescriptor) channel.read();
                    channel.close();
        }
        catch( Exception exc )
        {
            org.joing.runtime.Runtime.getRuntime().showException( exc, "Error communicating with the server" );
        }
        
        return file;
    }

    public boolean copy( int nFileId, int nToDirId )
    {
        boolean bSuccess = false;
        
        try
        {
            Channel channel = new Channel( VFS_COPY );
                    channel.write( Bridge2Server.getInstance().getSessionId() );
                    channel.write( nFileId  );
                    channel.write( nToDirId );
                    bSuccess = (Boolean) channel.read();
                    channel.close();
        }
        catch( Exception exc )
        {
            org.joing.runtime.Runtime.getRuntime().showException( exc, "Error communicating with the server" );
        }
        
        return bSuccess;
    }

    public boolean move( int nFileId, int nToDirId )
    {
        boolean bSuccess = false;
        
        try
        {
            Channel channel = new Channel( VFS_MOVE );
                    channel.write( Bridge2Server.getInstance().getSessionId() );
                    channel.write( nFileId  );
                    channel.write( nToDirId );
                    bSuccess = (Boolean) channel.read();
                    channel.close();
        }
        catch( Exception exc )
        {
            org.joing.runtime.Runtime.getRuntime().showException( exc, "Error communicating with the server" );
        }
        
        return bSuccess;
    }

    public boolean trashcan( int[] anFileId, boolean bInTrashCan )
    {
        boolean bSuccess = false;
        
        try
        {
            Channel channel = new Channel( VFS_TRASHCAN );
                    channel.write( Bridge2Server.getInstance().getSessionId() );
                    channel.write( anFileId    );
                    channel.write( bInTrashCan );
                    bSuccess = (Boolean) channel.read();
                    channel.close();
        }
        catch( Exception exc )
        {
            org.joing.runtime.Runtime.getRuntime().showException( exc, "Error communicating with the server" );
        }
        
        return bSuccess;
    }

    public boolean trashcan( int nFileId, boolean bInTrashCan )
    {
        boolean bSuccess = false;
        
        try
        {
            Channel channel = new Channel( VFS_TRASHCAN );
                    channel.write( Bridge2Server.getInstance().getSessionId() );
                    channel.write( nFileId );
                    channel.write( bInTrashCan );
                    bSuccess = (Boolean) channel.read();
                    channel.close();
        }
        catch( Exception exc )
        {
            org.joing.runtime.Runtime.getRuntime().showException( exc, "Error communicating with the server" );
        }
        
        return bSuccess;
    }

    public boolean delete( int[] anFileId )
    {
        boolean bSuccess = false;
        
        try
        {
            Channel channel = new Channel( VFS_DELETE );
                    channel.write( Bridge2Server.getInstance().getSessionId() );
                    channel.write( anFileId );
                    bSuccess = (Boolean) channel.read();
                    channel.close();
        }
        catch( Exception exc )
        {
            org.joing.runtime.Runtime.getRuntime().showException( exc, "Error communicating with the server" );
        }
        
        return bSuccess;
    }

    public boolean delete( int nFileId )
    {
        boolean bSuccess = false;
        
        try
        {
            Channel channel = new Channel( VFS_DELETE );
                    channel.write( Bridge2Server.getInstance().getSessionId() );
                    channel.write( nFileId );
                    bSuccess = (Boolean) channel.read();
                    channel.close();
        }
        catch( Exception exc )
        {
            org.joing.runtime.Runtime.getRuntime().showException( exc, "Error communicating with the server" );
        }
        
        return bSuccess;
    }

    public List<FileDescriptor> getChilds( Integer nFileId )
    {
        List<FileDescriptor> files = null;
        
        try
        {
            Channel channel = new Channel( VFS_GET_CHILDS );
                    channel.write( Bridge2Server.getInstance().getSessionId() );
                    channel.write( nFileId );
                    files = (List<FileDescriptor>) channel.read();
                    channel.close();
        }
        catch( Exception exc )
        {
            org.joing.runtime.Runtime.getRuntime().showException( exc, "Error communicating with the server" );
        }
        
        return files;
    }

    public List<FileDescriptor> getChilds( String sBaseDir )
    {
        List<FileDescriptor> files = null;
        
        try
        {
            Channel channel = new Channel( VFS_GET_CHILDS );
                    channel.write( Bridge2Server.getInstance().getSessionId() );
                    channel.write( sBaseDir );
                    files = (List<FileDescriptor>) channel.read();
                    channel.close();
        }
        catch( Exception exc )
        {
            org.joing.runtime.Runtime.getRuntime().showException( exc, "Error communicating with the server" );
        }
        
        return files;
    }

    public List<FileDescriptor> getByNotes( String sSubString )
    {
        List<FileDescriptor> files = null;
        
        try
        {
            Channel channel = new Channel( VFS_GET_BY_NOTES );
                    channel.write( Bridge2Server.getInstance().getSessionId() );
                    channel.write( sSubString );
                    files = (List<FileDescriptor>) channel.read();
                    channel.close();
        }
        catch( Exception exc )
        {
            org.joing.runtime.Runtime.getRuntime().showException( exc, "Error communicating with the server" );
        }
        
        return files;
    }

    public List<FileDescriptor> getTrashCan()
    {
        List<FileDescriptor> files = null;
        
        try
        {
            Channel channel = new Channel( VFS_GET_TRASHCAN );
                    channel.write( Bridge2Server.getInstance().getSessionId() );
                    files = (List<FileDescriptor>) channel.read();
                    channel.close();
        }
        catch( Exception exc )
        {
            org.joing.runtime.Runtime.getRuntime().showException( exc, "Error communicating with the server" );
        }
        
        return files;
    }
}