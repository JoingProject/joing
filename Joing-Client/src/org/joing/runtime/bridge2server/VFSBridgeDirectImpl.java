/*
 * VFSBridgeDirectImpl.java
 *
 * Created on 18 de junio de 2007, 16:22
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
import javax.naming.Context;

/**
 *
 *
 * @author Francisco Morero Peyrona
 */
public class VFSBridgeDirectImpl 
       extends BridgeDirectBaseImpl
       implements VFSBridge
{
    /**
     * Creates a new instance of VFSBridgeDirectImpl
     *
     * Package scope: only Runtime class can create instances of this class.
     */
    VFSBridgeDirectImpl( Context context )
    {
        super( context );
    }   

    public FileDescriptor getFile( String sFilePath )
    {
        throw new UnsupportedOperationException( "Not supported yet." );
    }
    
    public FileDescriptor createDirectory( String sPath, String sDirName )
    {
        throw new UnsupportedOperationException( "Not supported yet." );
    }

    public FileDescriptor createFile( String sPath, String sFileName )
    {
        throw new UnsupportedOperationException( "Not supported yet." );
    }

    public FileText readText(int nFileId, String sEncoding)
    {
        throw new UnsupportedOperationException( "Not supported yet." );
    }

    public FileBinary readBinary(int nFileId)
    {
        throw new UnsupportedOperationException( "Not supported yet." );
    }

    public FileDescriptor writeText(FileText fileText)
    {
        throw new UnsupportedOperationException( "Not supported yet." );
    }

    public FileDescriptor writeBinary(FileBinary fileBinary)
    {
        throw new UnsupportedOperationException( "Not supported yet." );
    }

    public FileDescriptor update(FileDescriptor file)
    {
        throw new UnsupportedOperationException( "Not supported yet." );
    }

    public boolean copy(int nFileId, int toDir)
    {
        throw new UnsupportedOperationException( "Not supported yet." );
    }

    public boolean move(int nFileId, int toDir)
    {
        throw new UnsupportedOperationException( "Not supported yet." );
    }

    public boolean trashcan(int[] anFileId, boolean bInTrashCan)
    {
        throw new UnsupportedOperationException( "Not supported yet." );
    }

    public boolean trashcan(int nFileID, boolean bInTrashCan)
    {
        throw new UnsupportedOperationException( "Not supported yet." );
    }

    public boolean delete(int[] anFileId)
    {
        throw new UnsupportedOperationException( "Not supported yet." );
    }

    public boolean delete(int nFileId)
    {
        throw new UnsupportedOperationException( "Not supported yet." );
    }

    //------------------------------------------------------------------------//
    // VfsListRemote
    
    public List<FileDescriptor> getRoots()
    {
        throw new UnsupportedOperationException( "Not supported yet." );
    }
    
    public List<FileDescriptor> getChilds(Integer nFileId)
    {
        throw new UnsupportedOperationException( "Not supported yet." );
    }

    public List<FileDescriptor> getChilds(String sBaseDir)
    {
        throw new UnsupportedOperationException( "Not supported yet." );
    }

    public List<FileDescriptor> getByNotes(String sSubString)
    {
        throw new UnsupportedOperationException( "Not supported yet." );
    }

    public List<FileDescriptor> getTrashCan()
    {
        throw new UnsupportedOperationException( "Not supported yet." );
    }
}