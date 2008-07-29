/*
 * FileDTOs.java
 *
 * Created on 12 de octubre de 2007, 18:19
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package ejb.vfs;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;
import org.joing.common.dto.vfs.FileBinary;
import org.joing.common.dto.vfs.FileDescriptor;
import org.joing.common.dto.vfs.FileText;

/**
 *
 * @author Francisco Morero Peyrona
 */
// NEXT: Esta clase habría que hacerla con constructor en lugar de como métodos static para poder crear múltiples instancias y que el servidor escale mejor
class FileDTOs
{
    static synchronized FileDescriptor createFileDescriptor( FileEntity fileEntity  )
    {
        FileDescriptor fileDescriptor = new FileDescriptor();
        
        transfer( fileEntity, fileDescriptor );
        
        return fileDescriptor;
    }
    
    static synchronized FileEntity createFileEntity( FileDescriptor fileDescriptor )
    {
        FileEntity fileEntity = new FileEntity();
        
        transfer( fileDescriptor, fileEntity );
        
        return fileEntity;
    }
    
    static synchronized FileText createFileText( FileEntity fileEntity ) 
           throws IOException, FileNotFoundException
    {
        FileText fileText = new FileText();
        
        transfer( fileEntity, fileText );
        fileText.setContents( getTextContents( fileText ) );
        fileText.setMimetype( null );   // TODO: averiguarlo
        
        return fileText;
    }
    
    static synchronized FileBinary createFileBinary( FileEntity fileEntity ) 
           throws IOException
    {
        FileBinary fileBinary = new FileBinary();
        
        transfer( fileEntity, fileBinary );
        fileBinary.setContents( getBinaryContents( fileBinary ) );
        
        return fileBinary;
    }
    
    static synchronized void transfer( FileEntity fromFileEntity, FileDescriptor toFileDescriptor )
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
    
    static synchronized void transfer( FileDescriptor fromFileDescriptor, FileEntity toFileEntity )
    {
        toFileEntity.setIdFile(     fromFileDescriptor.getId() );
        toFileEntity.setIdOriginal( (fromFileDescriptor.getIdOriginal() == -1 ? null : fromFileDescriptor.getIdOriginal()) );
        toFileEntity.setOwner(      fromFileDescriptor.getOwner()      );
        toFileEntity.setLockedBy(   fromFileDescriptor.getLockedBy()   );
        
        toFileEntity.setIsDir(        (short)(fromFileDescriptor.isDirectory()  ? 1 : 0) );
        toFileEntity.setIsHidden(     (short)(fromFileDescriptor.isHidden()     ? 1 : 0) );
        toFileEntity.setIsPublic(     (short)(fromFileDescriptor.isPublic()     ? 1 : 0) );
        toFileEntity.setIsReadable(   (short)(fromFileDescriptor.isReadable()   ? 1 : 0) );
        toFileEntity.setIsModifiable( (short)(fromFileDescriptor.isModifiable() ? 1 : 0) );
        toFileEntity.setIsDeleteable( (short)(fromFileDescriptor.isDeleteable() ? 1 : 0) );
        toFileEntity.setIsExecutable( (short)(fromFileDescriptor.isExecutable() ? 1 : 0) );
        toFileEntity.setIsDuplicable( (short)(fromFileDescriptor.isDuplicable() ? 1 : 0) );
        toFileEntity.setIsAlterable(  (short)(fromFileDescriptor.isAlterable()  ? 1 : 0) );
        toFileEntity.setIsInTrashcan( (short)(fromFileDescriptor.isInTrashcan() ? 1 : 0) );
        
        toFileEntity.setCreated(  fromFileDescriptor.getCreated()  );
        toFileEntity.setModified( fromFileDescriptor.getModified() );
        toFileEntity.setAccessed( new Date()                       );
        toFileEntity.setNotes(    fromFileDescriptor.getNotes()    );
    }
    
    //------------------------------------------------------------------------//
    
    /**
     * 
     * @param BufferedReader
     */
    private static char[] getTextContents( FileText fText ) 
            throws IOException
    {
        char[] chContent = new char[ (int) fText.getSize() ];
        
        java.io.File      fNative = NativeFileSystemTools.getFile( fText.getOwner(), fText.getId() );
        FileInputStream   fis     = new FileInputStream( fNative );
        InputStreamReader isw     = new InputStreamReader( fis, fText.getEncoding() );
        BufferedReader    br      = new BufferedReader( isw );

        br.read( chContent, 0, chContent.length );
        br.close();
        
        return chContent;
    }
    
    private static byte[] getBinaryContents( FileBinary fBinary )
            throws IOException, FileNotFoundException
    {
        byte[] btContent = new byte[ (int) fBinary.getSize() ];
        
        java.io.File    fNative = NativeFileSystemTools.getFile( fBinary.getOwner(), fBinary.getId() );
        FileInputStream fis     = new FileInputStream( fNative );

        fis.read( btContent, 0, btContent.length );
        fis.close();
        
        return btContent;
    }
}