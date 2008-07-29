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

import java.util.List;
import org.joing.common.dto.vfs.FileBinary;
import org.joing.common.dto.vfs.FileDescriptor;
import org.joing.common.dto.vfs.FileText;
import org.joing.common.clientAPI.runtime.VFSBridge;
// NEXT: Implementar esta clase

/**
 *
 *
 * @author Francisco Morero Peyrona
 */
public class VFSBridgeSocketImpl 
       extends BridgeSocketBaseImpl
       implements VFSBridge
{    
    /**
     * Creates a new instance of VFSBridgeDirectImpl
     *
     * Package scope: only Runtime class can create instances of this class.
     */
    VFSBridgeSocketImpl()
    {
        super();
    }   

    public FileDescriptor getFile( String sFilePath )
    {
        return null;
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
        return null;
    }

    public FileBinary readBinary(int nFileId)
    {
        return null;
    }

    public FileDescriptor writeText(FileText fileText)
    {
        return null;
    }

    public FileDescriptor writeBinary(FileBinary fileBinary)
    {
        return null;
    }

    public FileDescriptor update(FileDescriptor file)
    {
        return null;
    }

    public boolean copy(int nFileId, int toDir)
    {
        return false;
    }

    public boolean move(int nFileId, int toDir)
    {
        return false;
    }

    public boolean trashcan(int[] anFileId, boolean bInTrashCan)
    {
        return false;
    }

    public boolean trashcan(int nFileID, boolean bInTrashCan)
    {
        return false;
    }

    public int[] delete(int[] anFileId)
    {
        return new int[0];
    }

    public int[] delete(int nFileId)
    {
        return new int[0];
    }

    //------------------------------------------------------------------------//
    
    public List<FileDescriptor> getRoots( )
    {
        throw new UnsupportedOperationException( "Not supported yet." );
    }
    
    public List<FileDescriptor> getChilds(Integer nFileId)
    {
        return null;
    }

    public List<FileDescriptor> getChilds(String sBaseDir)
    {
        return null;
    }

    public List<FileDescriptor> getByNotes(String sSubString)
    {
        return null;
    }

    public List<FileDescriptor> getTrashCan()
    {
        return null;
    }
}