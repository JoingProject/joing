/*
 * FileDTOs.java
 *
 * Created on 12 de octubre de 2007, 18:19
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
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
    
    FileDTOs( FileEntity fileEntity)
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