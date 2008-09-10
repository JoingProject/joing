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

import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import org.joing.kernel.runtime.vfs.VFSFile;
import org.joing.kernel.api.bridge.VFSBridge;
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

    public VFSFile getFile( String sFilePath, boolean bCreateFile ) throws JoingServerVFSException
    {
        throw new UnsupportedOperationException( "Not supported yet." );
    }

    public VFSFile createDirectories( String sPath ) throws JoingServerVFSException
    {
        throw new UnsupportedOperationException( "Not supported yet." );
    }

    public VFSFile createDirectory( String sParent, String sDir ) throws JoingServerVFSException
    {
        throw new UnsupportedOperationException( "Not supported yet." );
    }

    public VFSFile createFile( String sParent, String sFileName, boolean bCreateParentDirs ) throws JoingServerVFSException
    {
        throw new UnsupportedOperationException( "Not supported yet." );
    }

    public InputStream getInputStream( VFSFile file )
    {
        throw new UnsupportedOperationException( "Not supported yet." );
    }

    public OutputStream getOutputStream( VFSFile file, boolean append )
    {
        throw new UnsupportedOperationException( "Not supported yet." );
    }

    public VFSFile update( VFSFile file ) throws JoingServerVFSException
    {
        throw new UnsupportedOperationException( "Not supported yet." );
    }

    public List<VFSFile> copy( VFSFile fFromFileOrFolder, VFSFile fToFolder ) throws JoingServerVFSException
    {
        throw new UnsupportedOperationException( "Not supported yet." );
    }

    public List<VFSFile> move( VFSFile fFromFileOrFolder, VFSFile fToFolder ) throws JoingServerVFSException
    {
        throw new UnsupportedOperationException( "Not supported yet." );
    }

    public List<VFSFile> toTrashcan( VFSFile file, boolean bInTrashCan ) throws JoingServerVFSException
    {
        throw new UnsupportedOperationException( "Not supported yet." );
    }

    public List<VFSFile> toTrashcan( List<VFSFile> list, boolean bInTrashCan ) throws JoingServerVFSException
    {
        throw new UnsupportedOperationException( "Not supported yet." );
    }

    public List<VFSFile> delete( VFSFile file ) throws JoingServerVFSException
    {
        throw new UnsupportedOperationException( "Not supported yet." );
    }

    public List<VFSFile> delete( List<VFSFile> list ) throws JoingServerVFSException
    {
        throw new UnsupportedOperationException( "Not supported yet." );
    }

    public List<VFSFile> getRoots() throws JoingServerVFSException
    {
        throw new UnsupportedOperationException( "Not supported yet." );
    }

    public List<VFSFile> getChildren( VFSFile file ) throws JoingServerVFSException
    {
        throw new UnsupportedOperationException( "Not supported yet." );
    }

    public List<VFSFile> getByNotes( String sSubString, boolean bGlobal ) throws JoingServerVFSException
    {
        throw new UnsupportedOperationException( "Not supported yet." );
    }

    public List<VFSFile> getTrashCan() throws JoingServerVFSException
    {
        throw new UnsupportedOperationException( "Not supported yet." );
    }
}