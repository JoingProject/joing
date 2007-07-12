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

import ejb.vfs.File;
import java.io.BufferedReader;
import java.io.FileInputStream;
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

    public File getFile( String sFilePath )
    {
        return null;
    }

    public File createDirectory( int nParentId, String sDirName )
    {
        return null;
    }

    public File createFile( int nParentId, String sFileName )
    {
        return null;
    }

    public BufferedReader readText(int nFileId, String sEncoding)
    {
        return null;
    }

    public FileInputStream readBinary(int nFileId)
    {
        return null;
    }

    public boolean writeText(int nFileId, BufferedReader reader, String sEncoding)
    {
        return false;
    }

    public boolean writeBinary(int nFileId, FileInputStream reader)
    {
        return false;
    }

    public File update(File file)
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

    public boolean delete(int[] anFileId)
    {
        return false;
    }

    public boolean delete(int nFileId)
    {
        return false;
    }

    public List<File> getChilds(Integer nFileId)
    {
        return null;
    }

    public List<File> getChilds(String sBaseDir)
    {
        return null;
    }

    public List<File> getByNotes(String sSubString)
    {
        return null;
    }

    public List<File> getTrashCan()
    {
        return null;
    }
}