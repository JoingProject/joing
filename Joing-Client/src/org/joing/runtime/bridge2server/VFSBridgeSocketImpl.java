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
import org.joing.common.dto.vfs.VFSFile4IO;
import org.joing.common.dto.vfs.FileDescriptor;
import org.joing.common.clientAPI.runtime.VFSBridge;
// NEXT: Implementar esta clase
import org.joing.common.exception.JoingServerVFSException;

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

    public FileDescriptor getFileDescriptor( String sPath, boolean bCreateFile ) throws JoingServerVFSException
    {
        throw new UnsupportedOperationException( "Not supported yet." );
    }

    public FileDescriptor createDirectories( String sPath ) throws JoingServerVFSException
    {
        throw new UnsupportedOperationException( "Not supported yet." );
    }

    public FileDescriptor createDirectory( String sParent, String sDir ) throws JoingServerVFSException
    {
        throw new UnsupportedOperationException( "Not supported yet." );
    }

    public FileDescriptor createFile( String sParent, String sFileName, boolean bCreateParentDirs ) throws JoingServerVFSException
    {
        throw new UnsupportedOperationException( "Not supported yet." );
    }

    public VFSFile4IO getFile( FileDescriptor fd ) throws JoingServerVFSException
    {
        throw new UnsupportedOperationException( "Not supported yet." );
    }

    public FileDescriptor update( FileDescriptor fd ) throws JoingServerVFSException
    {
        throw new UnsupportedOperationException( "Not supported yet." );
    }

    public List<FileDescriptor> copy( FileDescriptor fdFromFileOrFolder, FileDescriptor fdToFolder ) throws JoingServerVFSException
    {
        throw new UnsupportedOperationException( "Not supported yet." );
    }

    public List<FileDescriptor> move( FileDescriptor fdFromFileOrFolder, FileDescriptor fdToFolder ) throws JoingServerVFSException
    {
        throw new UnsupportedOperationException( "Not supported yet." );
    }

    public List<FileDescriptor> trashcan( FileDescriptor fd, boolean bInTrashCan ) throws JoingServerVFSException
    {
        throw new UnsupportedOperationException( "Not supported yet." );
    }

    public List<FileDescriptor> trashcan( List<FileDescriptor> list, boolean bInTrashCan ) throws JoingServerVFSException
    {
        throw new UnsupportedOperationException( "Not supported yet." );
    }

    public List<FileDescriptor> delete( FileDescriptor fd ) throws JoingServerVFSException
    {
        throw new UnsupportedOperationException( "Not supported yet." );
    }

    public List<FileDescriptor> delete( List<FileDescriptor> list ) throws JoingServerVFSException
    {
        throw new UnsupportedOperationException( "Not supported yet." );
    }

    public List<FileDescriptor> getRoots() throws JoingServerVFSException
    {
        throw new UnsupportedOperationException( "Not supported yet." );
    }

    public List<FileDescriptor> getChilds( FileDescriptor fd ) throws JoingServerVFSException
    {
        throw new UnsupportedOperationException( "Not supported yet." );
    }

    public List<FileDescriptor> getByNotes( String sSubString, boolean bGlobal ) throws JoingServerVFSException
    {
        throw new UnsupportedOperationException( "Not supported yet." );
    }

    public List<FileDescriptor> getTrashCan() throws JoingServerVFSException
    {
        throw new UnsupportedOperationException( "Not supported yet." );
    }
}