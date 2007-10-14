/*
 * FileDTOs.java
 *
 * Created on 12 de octubre de 2007, 18:19
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package ejb.vfs;

import ejb.Constant;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Date;

/**
 *
 * @author Francisco Morero Peyrona
 */
class FileDTOs
{
    static FileDescriptor createFileDescriptor( FileEntity fileEntity  )
    {
        FileDescriptor fileDescriptor = new FileDescriptor();
        
        transfer( fileEntity, fileDescriptor );
        
        return fileDescriptor;
    }
    
    static FileEntity createFileEntity( FileDescriptor fileDescriptor )
    {
        FileEntity fileEntity = new FileEntity();
        
        transfer( fileDescriptor, fileEntity );
        
        return fileEntity;
    }
    
    static FileText createFileText( FileEntity fileEntity ) 
           throws IOException, FileNotFoundException
    {
        FileText fileText = new FileText();
        
        transfer( fileEntity, fileText );
        fileText.setContents( getTextContents( fileText ) );
        fileText.setMimetype( null );   // TODO: averiguarlo
        
        return fileText;
    }
    
    static FileBinary createFileBinary( FileEntity fileEntity ) 
           throws IOException
    {
        FileBinary fileBinary = new FileBinary();
        
        transfer( fileEntity, fileBinary );
        fileBinary.setContents( getBinaryContents( fileBinary ) );
        
        return fileBinary;
    }
    
    static void transfer( FileEntity fromFileEntity, FileDescriptor toFileDescriptor )
    {
        long nSize = 0;
        
        if( fromFileEntity.getIsDir() == 0 )    // Is not a directory
            nSize = FileSystemTools.getFileSize( fromFileEntity.getFileEntityPK().getAccount(), 
                                                 fromFileEntity.getIdFile() );
        
        //toFileDescriptor.setAccount      = _file.getFileEntityPK().getAccount();
        toFileDescriptor.setName(       fromFileEntity.getFileEntityPK().getFileName() );
        toFileDescriptor.setPath(       fromFileEntity.getFileEntityPK().getFilePath() );
        
        toFileDescriptor.setIdFile(     fromFileEntity.getIdFile()     );
        toFileDescriptor.setDirectory(  fromFileEntity.getIsDir() != 0 );
        toFileDescriptor.setIdOriginal( fromFileEntity.getIdOriginal() );
        toFileDescriptor.setOwner(      fromFileEntity.getOwner()      );
        toFileDescriptor.setLockedBy(   fromFileEntity.getLockedBy()   );
        
        toFileDescriptor.setHidden(     fromFileEntity.getIsHidden()     != 0 );
        toFileDescriptor.setPublic(     fromFileEntity.getIsPublic()     != 0 );
        toFileDescriptor.setReadable(   fromFileEntity.getIsReadable()   != 0 );
        toFileDescriptor.setModifiable( fromFileEntity.getIsModifiable() != 0 );
        toFileDescriptor.setDeleteable( fromFileEntity.getIsDeleteable() != 0 );
        toFileDescriptor.setExecutable( fromFileEntity.getIsExecutable() != 0 );
        toFileDescriptor.setDuplicable( fromFileEntity.getIsDuplicable() != 0 );
        toFileDescriptor.setAlterable(  fromFileEntity.getIsAlterable()  != 0 );
        toFileDescriptor.setInTrashcan( fromFileEntity.getIsInTrashcan() != 0 );
        
        toFileDescriptor.setCreated(    fromFileEntity.getCreated()  );
        toFileDescriptor.setModified(   fromFileEntity.getModified() );
        toFileDescriptor.setAccessed(   fromFileEntity.getAccessed() );
        toFileDescriptor.setNotes(      fromFileEntity.getNotes()    );
        toFileDescriptor.setSize( nSize );
    }
    
    static void transfer( FileDescriptor fromFileDescriptor, FileEntity toFileEntity )
    {
        // This field allows to indentify uniquely the File faster than using the PK
        toFileEntity.setIdFile(     fromFileDescriptor.getId() );
        toFileEntity.setIdOriginal( fromFileDescriptor.getIdOriginal() );
        toFileEntity.setOwner(      fromFileDescriptor.getOwner()      );
        toFileEntity.setLockedBy(   fromFileDescriptor.getLockedBy()   );
        toFileEntity.setIsDir( (short)(fromFileDescriptor.isDirectory() ? 1 : 0) );
        
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
        
        java.io.File      fNative = FileSystemTools.getFile( fText.getOwner(), fText.getId() );
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
        
        java.io.File    fNative = FileSystemTools.getFile( fBinary.getOwner(), fBinary.getId() );
        FileInputStream fis     = new FileInputStream( fNative );

        fis.read( btContent, 0, btContent.length );
        fis.close();
        
        return btContent;
    }
}