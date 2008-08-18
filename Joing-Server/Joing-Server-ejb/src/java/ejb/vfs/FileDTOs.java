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
package ejb.vfs;

import org.joing.common.dto.vfs.FileDescriptor;

/**
 *
 * @author Francisco Morero Peyrona
 */
class FileDTOs
{
    private FileEntity fileEntity;
    
    FileDTOs( FileEntity fileEntity )
    {
        this.fileEntity = fileEntity;
    }
    
    FileDescriptor createFileDescriptor()
    {
        FileDescriptor fileDescriptor = new FileDescriptor();
        
        transfer( fileEntity, fileDescriptor );
        
        return fileDescriptor;
    }
    
    //------------------------------------------------------------------------//
    
    private void transfer( FileEntity fromFileEntity, FileDescriptor toFileDescriptor )
    {
        long nSize = 0;
        
        if( fromFileEntity.getIsDir() == 0 )    // Is not a directory
            nSize = NativeFileSystemTools.getFileSize( fromFileEntity.getAccount(), fromFileEntity.getIdFile() );
        
        int nIdOriginal = (fromFileEntity.getIdOriginal() == null) ? -1 : fromFileEntity.getIdOriginal();
        
        toFileDescriptor.setIdFile(     fromFileEntity.getIdFile()   );
        toFileDescriptor.setAccount(    fromFileEntity.getAccount()  );
        toFileDescriptor.setName(       fromFileEntity.getFileName() );
        toFileDescriptor.setPath(       fromFileEntity.getFilePath() );
        toFileDescriptor.setIdOriginal( nIdOriginal                  );
        toFileDescriptor.setOwner(      fromFileEntity.getOwner()    );
        toFileDescriptor.setLockedBy(   fromFileEntity.getLockedBy() );
        
        toFileDescriptor.setDirectory(  fromFileEntity.getIsDir()                   != 0 );
        toFileDescriptor.setHidden(     fromFileEntity.getIsHidden().intValue()     != 0 );
        toFileDescriptor.setPublic(     fromFileEntity.getIsPublic().intValue()     != 0 );
        toFileDescriptor.setReadable(   fromFileEntity.getIsReadable().intValue()   != 0 );
        toFileDescriptor.setModifiable( fromFileEntity.getIsModifiable().intValue() != 0 );
        toFileDescriptor.setDeleteable( fromFileEntity.getIsDeleteable().intValue() != 0 );
        toFileDescriptor.setExecutable( fromFileEntity.getIsExecutable().intValue() != 0 );
        toFileDescriptor.setDuplicable( fromFileEntity.getIsDuplicable().intValue() != 0 );
        toFileDescriptor.setAlterable(  fromFileEntity.getIsAlterable().intValue()  != 0 );
        toFileDescriptor.setInTrashcan( fromFileEntity.getIsInTrashcan().intValue() != 0 );
        
        toFileDescriptor.setCreated(    fromFileEntity.getCreated()  );
        toFileDescriptor.setModified(   fromFileEntity.getModified() );
        toFileDescriptor.setAccessed(   fromFileEntity.getAccessed() );
        toFileDescriptor.setNotes(      fromFileEntity.getNotes()    );
        toFileDescriptor.setSize(       nSize );
    }
}