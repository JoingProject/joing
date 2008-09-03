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

package org.joing.kernel.runtime.bridge2server;

import java.util.ArrayList;
import java.util.List;
import org.joing.common.CallBackable;
import org.joing.common.dto.vfs.VFSFileBase;
import org.joing.kernel.api.bridge.VFSBridge;
import org.joing.common.dto.vfs.VFSFile4IO;
import org.joing.kernel.runtime.vfs.VFSFile;
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
    
    public VFSFile getFile( String sFilePath, boolean bCreateFile )
            throws JoingServerVFSException
    {
        VFSFileBase fBase = null;
        
        Channel channel = new Channel( VFS_GET_FILE  );
                channel.write( org.joing.kernel.jvmm.RuntimeFactory.getPlatform().getBridge().getSessionBridge().getSessionId() );
                channel.write( sFilePath );
                channel.write( bCreateFile );
        fBase = (VFSFileBase) channel.read();
                channel.close();

        return (fBase == null ? null : new VFSFile( fBase ));
    }
    
    public VFSFile createDirectories( String sPath )
            throws JoingServerVFSException
    {
        VFSFileBase fBase = null;
        
        Channel channel = new Channel( VFS_CREATE_DIR );
                channel.write( org.joing.kernel.jvmm.RuntimeFactory.getPlatform().getBridge().getSessionBridge().getSessionId() );
                channel.write( sPath );
        fBase = (VFSFileBase) channel.read();
                channel.close();
        
        return (fBase == null ? null : new VFSFile( fBase ));
    }
    
    public VFSFile createDirectory( String sParent, String sDirName )
           throws JoingServerVFSException
    {
        VFSFileBase fBase = null;
        
        Channel channel = new Channel( VFS_CREATE_DIR );
                channel.write( org.joing.kernel.jvmm.RuntimeFactory.getPlatform().getBridge().getSessionBridge().getSessionId() );
                channel.write( sParent );
                channel.write( sDirName );
        fBase = (VFSFileBase) channel.read();
                channel.close();
        
        return (fBase == null ? null : new VFSFile( fBase ));
    }
    
    public VFSFile createFile( String sPath, String sFileName, boolean bCreateParentDirs )
           throws JoingServerVFSException
    {
        VFSFileBase fBase = null;
        
        Channel channel = new Channel( VFS_CREATE_FILE );
                channel.write( org.joing.kernel.jvmm.RuntimeFactory.getPlatform().getBridge().getSessionBridge().getSessionId() );
                channel.write( sPath );
                channel.write( sFileName );
                channel.write( bCreateParentDirs );
        fBase = (VFSFileBase) channel.read();
                channel.close();
        
        return (fBase == null ? null : new VFSFile( fBase ));
    }
    
    public VFSFile4IO getFileReaderAndWriter( VFSFile file )
           throws JoingServerVFSException
    {
        VFSFileOverArray foa = null;
        
        Channel channel = new Channel( VFS_READ_FILE_TO_ARRAY  );
                channel.write( org.joing.kernel.jvmm.RuntimeFactory.getPlatform().getBridge().getSessionBridge().getSessionId() );
                channel.write( file.getHandler() );
        byte[] abContent = (byte[]) channel.read();
                channel.close();

        foa = new VFSFileOverArray( file, abContent );
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
    
    public VFSFile update( VFSFile file )
           throws JoingServerVFSException
    {
        VFSFileBase fBase = null;
        
        Channel channel = new Channel( VFS_UPDATE );
                channel.write( org.joing.kernel.jvmm.RuntimeFactory.getPlatform().getBridge().getSessionBridge().getSessionId() );
                channel.write( file );
        fBase = (VFSFileBase) channel.read();
                channel.close();

        return (fBase == null ? null : new VFSFile( fBase ));
    }
    
    public List<VFSFile> copy( VFSFile fFromFileOrFolder, VFSFile fToFolder )
           throws JoingServerVFSException
    {
        List<VFSFile>     lstToReturn = new ArrayList<VFSFile>();
        List<VFSFileBase> lstErrors   = null;
        
        Channel channel = new Channel( VFS_COPY );
                channel.write( org.joing.kernel.jvmm.RuntimeFactory.getPlatform().getBridge().getSessionBridge().getSessionId() );
                channel.write( fFromFileOrFolder.getHandler() );
                channel.write( fToFolder.getHandler() );
        lstErrors = (List<VFSFileBase>) channel.read();
                channel.close();
        
        if( lstErrors != null )
        {
            for( VFSFileBase fBase : lstErrors )
                lstToReturn.add( new VFSFile( fBase ) );
        }
        
        return lstToReturn;
    }
    
    public List<VFSFile> move( VFSFile fFromFileOrFolder, VFSFile fToFolder )
           throws JoingServerVFSException
    {
        List<VFSFile>     lstToReturn = new ArrayList<VFSFile>();
        List<VFSFileBase> lstErrors   = null;
        
        Channel channel = new Channel( VFS_MOVE );
                channel.write( org.joing.kernel.jvmm.RuntimeFactory.getPlatform().getBridge().getSessionBridge().getSessionId() );
                channel.write( fFromFileOrFolder.getHandler()  );
                channel.write( fToFolder.getHandler() );
        lstErrors = (List<VFSFileBase>) channel.read();
                channel.close();
        
        if( lstErrors != null )
        {
            for( VFSFileBase fBase : lstErrors )
                lstToReturn.add( new VFSFile( fBase ) );
        }
        
        return lstToReturn;
    }
    
    public List<VFSFile> toTrashcan( VFSFile file, boolean bInTrashCan )
           throws JoingServerVFSException
    {
        List<VFSFile>     lstToReturn = new ArrayList<VFSFile>();
        List<VFSFileBase> lstErrors   = null;
        
        Channel channel = new Channel( VFS_TO_TRASHCAN );
                channel.write( org.joing.kernel.jvmm.RuntimeFactory.getPlatform().getBridge().getSessionBridge().getSessionId() );
                channel.write( file.getHandler() );
                channel.write( bInTrashCan );
        lstErrors = (List<VFSFileBase>) channel.read();
                channel.close();
        
        if( lstErrors != null )
        {
            for( VFSFileBase fBase : lstErrors )
                lstToReturn.add( new VFSFile( fBase ) );
        }
        
        return lstToReturn;
    }
    
    public List<VFSFile> toTrashcan( List<VFSFile> list, boolean bInTrashCan )
           throws JoingServerVFSException
    {
        List<VFSFile>     lstToReturn = new ArrayList<VFSFile>();
        List<VFSFileBase> lstErrors   = null;
        
        // Gets IDs from FileDescriptors in the List
        int[] anIDs = new int[ list.size() ];
        int   n     = 0;
        
        for( VFSFile fd : list )
            anIDs[n++] = fd.getHandler();
        
        // Calling servlet
        Channel channel = new Channel( VFS_TO_TRASHCAN );
                channel.write( org.joing.kernel.jvmm.RuntimeFactory.getPlatform().getBridge().getSessionBridge().getSessionId() );
                channel.write( anIDs );
                channel.write( bInTrashCan );
        lstErrors = (List<VFSFileBase>) channel.read();
                channel.close();
        
        if( lstErrors != null )
        {
            for( VFSFileBase fBase : lstErrors )
                lstToReturn.add( new VFSFile( fBase ) );
        }
        
        return lstToReturn;
    }
    
    public List<VFSFile> delete( VFSFile file )
           throws JoingServerVFSException
    {
        List<VFSFile>     lstToReturn = new ArrayList<VFSFile>();
        List<VFSFileBase> lstErrors   = null;
        
        Channel channel = new Channel( VFS_DELETE );
                channel.write( org.joing.kernel.jvmm.RuntimeFactory.getPlatform().getBridge().getSessionBridge().getSessionId() );
                channel.write( file.getHandler() );
        lstErrors = (List<VFSFileBase>) channel.read();
                channel.close();
        
        if( lstErrors != null )
        {            
            for( VFSFileBase fBase : lstErrors )
                lstToReturn.add( new VFSFile( fBase ) );
        }
        
        return lstToReturn;
    }
    
    public List<VFSFile> delete( List<VFSFile> list )
           throws JoingServerVFSException
    {
        List<VFSFile>     lstToReturn = new ArrayList<VFSFile>();
        List<VFSFileBase> lstErrors   = null;
        
        // Gets IDs from FileDescriptors in the List
        int[] anIDs = new int[ list.size() ];
        int   n     = 0;
        
        for( VFSFile file : list )
            anIDs[n++] = file.getHandler();
        
        // Calling servlet
        Channel channel = new Channel( VFS_DELETE );
                channel.write( org.joing.kernel.jvmm.RuntimeFactory.getPlatform().getBridge().getSessionBridge().getSessionId() );
                channel.write( anIDs );
        lstErrors = (List<VFSFileBase>) channel.read();
                channel.close();
        
        if( lstErrors != null )
        {            
            for( VFSFileBase fBase : lstErrors )
                lstToReturn.add( new VFSFile( fBase ) );
        }
        
        return lstToReturn;
    }
    
    // To be used internally
    private VFSFile writeFileFromArray( VFSFileOverArray foa )
           throws JoingServerVFSException
    {
        VFSFileBase fBase = null;
        
        Channel channel = new Channel( VFS_WRITE_FILE_FROM_ARRAY  );
                channel.write( org.joing.kernel.jvmm.RuntimeFactory.getPlatform().getBridge().getSessionBridge().getSessionId() );
                channel.write( foa.getHandler() );
                channel.write( foa.intern() );
        fBase = (VFSFileBase) channel.read();
                channel.close();
        
        return (fBase == null ? null : new VFSFile( fBase ));
    }
    
    //------------------------------------------------------------------------//
    
    public List<VFSFile> getRoots() 
           throws JoingServerVFSException
    {
        List<VFSFile>     lstRoots = new ArrayList<VFSFile>();
        List<VFSFileBase> lstBase  = null;
        
        Channel channel = new Channel( VFS_GET_ROOTS );
                channel.write( org.joing.kernel.jvmm.RuntimeFactory.getPlatform().getBridge().getSessionBridge().getSessionId() );
        lstBase = (List<VFSFileBase>) channel.read();
                channel.close();
        
        if( lstBase != null )
        {            
            for( VFSFileBase fBase : lstBase )
                lstRoots.add( new VFSFile( fBase ) );
        }
        
        return lstRoots;
    }
    
    public List<VFSFile> getChildren( VFSFile file )
           throws JoingServerVFSException
    {
        List<VFSFile>     lstChildren = new ArrayList<VFSFile>();
        List<VFSFileBase> lstBase     = null;
        
        Channel channel = new Channel( VFS_GET_CHILDREN  );
                channel.write( org.joing.kernel.jvmm.RuntimeFactory.getPlatform().getBridge().getSessionBridge().getSessionId() );
                channel.write( file.getHandler() );
        lstBase = (List<VFSFileBase>) channel.read();
                channel.close();
        
        if( lstBase != null )
        {            
            for( VFSFileBase fBase : lstBase )
                lstChildren.add( new VFSFile( fBase ) );
        }
        
        return lstChildren;
    }
    
    public List<VFSFile> getByNotes( String sSubString, boolean bGlobal )
           throws JoingServerVFSException
    {
        List<VFSFile>     lstByNotes = new ArrayList<VFSFile>();
        List<VFSFileBase> lstBase    = null;
     
        Channel channel = new Channel( VFS_GET_BY_NOTES );
                channel.write( org.joing.kernel.jvmm.RuntimeFactory.getPlatform().getBridge().getSessionBridge().getSessionId() );
                channel.write( sSubString );
        lstBase = (List<VFSFileBase>) channel.read();
                channel.close();
        
        if( lstBase != null )
        {            
            for( VFSFileBase fBase : lstBase )
                lstByNotes.add( new VFSFile( fBase ) );
        }
        
        return lstByNotes;
    }
    
    public List<VFSFile> getTrashCan()
           throws JoingServerVFSException
    {
        List<VFSFile>     lstTrashcan = new ArrayList<VFSFile>();
        List<VFSFileBase> lstBase     = null;
        
        Channel channel = new Channel( VFS_GET_TRASHCAN );
                channel.write( org.joing.kernel.jvmm.RuntimeFactory.getPlatform().getBridge().getSessionBridge().getSessionId() );
        lstBase = (List<VFSFileBase>) channel.read();
                channel.close();
        
        if( lstBase != null )
        {            
            for( VFSFileBase fBase : lstBase )
                lstTrashcan.add( new VFSFile( fBase ) );
        }
        
        return lstTrashcan;
    }
}