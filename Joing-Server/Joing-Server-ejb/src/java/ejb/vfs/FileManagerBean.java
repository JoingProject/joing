/*
 * FileManagerBean.java
 *
 * Created on 8 de junio de 2007, 11:59
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

package ejb.vfs;

import ejb.Constant;
import ejb.session.SessionManagerLocal;
import ejb.user.UserManagerLocal;
import java.io.InputStream;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.joing.common.dto.user.User;
import org.joing.common.dto.vfs.FileBinary;
import org.joing.common.dto.vfs.FileDescriptor;
import org.joing.common.dto.vfs.FileText;
import org.joing.common.exception.JoingServerException;
import org.joing.common.exception.JoingServerSessionException;
import org.joing.common.exception.JoingServerVFSException;

/**
 * Functionality related to Virtual File System (VFS) files' manipulation.
 *  <p>
 * For the API javadoc, refer to the 'remote' and 'local' interfaces.
 *
 * @author Francisco Morero Peyrona
 */
@Stateless
public class FileManagerBean 
       implements FileManagerRemote, FileManagerLocal, Serializable 
{
    private static final long serialVersionUID = 1L;    // TODO: cambiarlo por un nº apropiado
    
    @PersistenceContext
    private EntityManager em;
    
    @EJB
    private SessionManagerLocal sessionManagerBean;
    
    @EJB
    private UserManagerLocal userManagerBean;
    
    //------------------------------------------------------------------------//
    
    public FileDescriptor getFile( String sSessionId, String sFullName )
           throws JoingServerVFSException, JoingServerSessionException
    {
        FileDescriptor file     = null;
        String         sAccount = sessionManagerBean.getUserAccount( sSessionId );
        
        if( sAccount != null )
        {
            FileEntity _file = VFSTools.path2File( em, sAccount, sFullName );
            
            if( _file != null )
                file = FileDTOs.createFileDescriptor( _file );
        }
        
        return file;
    }
    
    // This method does not need to implement the logic to handle (validate) file 
    // attributes: the logic (validation) is done by class FileDescriptor.
    // In this way, there is no need to send file attributes from Client to the 
    // Server in order to be validated.
    public FileDescriptor updateFile( String sSessionId, FileDescriptor fileIn )
           throws JoingServerVFSException
    {
        String         sAccount = sessionManagerBean.getUserAccount( sSessionId );
        FileDescriptor fileOut  = null;
        
        if( sAccount != null )
        {
            try
            {
                if( ! isFileInUserSpace( sAccount, fileIn ) )
                    throw new JoingServerVFSException( JoingServerVFSException.FILE_NOT_EXISTS );
                
                if( ! fileIn.getOwner().equals( sAccount ) )
                    throw new JoingServerVFSException( JoingServerVFSException.INVALID_OWNER );
                
                // TODO: Revisar este metodo
                                
                // Get the original file: this is the only one attribute that has 
                // to be checked: the rest of them are done in FileDescriptor class,
                // but obviously the name can't be cheked at Client.
                FileEntity _file = em.find( FileEntity.class, fileIn.getId() );
                
                // Attribute name is changed
                String sNameNew = fileIn.getName();
                String sNameOld = _file.getFileName();
                
                if( (! sNameOld.equals( sNameNew )) && (sNameNew != null) )
                {
                    String sNameErrors = validateName( sNameNew );
                    
                    if( sNameErrors.length() > 0 )
                        throw new JoingServerVFSException( sNameErrors );

                    if( existsName( sAccount, _file.getFilePath(), sNameNew ) )
                        throw new JoingServerVFSException( JoingServerVFSException.FILE_NAME_EXISTS );
                    
                    _file.setFileName( sNameNew );
                }
                
                _file.setAccessed( new Date() );  // Modified is only when modifiying contents
                em.persist( _file );
                fileOut = FileDTOs.createFileDescriptor( _file );
            }
            catch( RuntimeException exc )
            {
                if( ! (exc instanceof JoingServerException) )
                {
                    Constant.getLogger().throwing( getClass().getName(), "updateFile(...)", exc );
                    exc = new JoingServerVFSException( JoingServerException.ACCESS_DB, exc );
                }
                
                throw exc;
            }
        }
        
        return fileOut;
    }
    
    public FileDescriptor createDirectory( String sSessionId, String sPath, String sDirName )
           throws JoingServerVFSException
    {
        return createEntry( sSessionId, sPath, sDirName, true );
    }
    
    public FileDescriptor createFile( String sSessionId, String sPath, String sFileName )
           throws JoingServerVFSException
    {
        return createEntry( sSessionId, sPath, sFileName, false );
    }

    /* TODO: para read y write files
     * Pensar qué hacer con esto:
     * El problema es que no se puede serializar (RMI/IIOP) un stream, y sin
     * embargo, los métodos de los EJBs tienen que ser serializables.
     * Por lo que no sé cómo obtener un stream desde una invocación @Remote a 
     * un EJB.*/
    // Help at: http://java.sun.com/docs/books/tutorial/i18n/text/stream.html
    public FileText readTextFile( String sSessionId, int nFileId, String sEncoding )
           throws JoingServerVFSException
    {        
        FileText fileText = null;
        String   sAccount = sessionManagerBean.getUserAccount( sSessionId );
        
        if( sAccount != null )
        {
            try
            {
                FileEntity _file = em.find( FileEntity.class, nFileId );
         
                if( _file == null )
                    throw new JoingServerVFSException( JoingServerVFSException.FILE_NOT_EXISTS );
            
                if( ! isFileInUserSpace( sAccount, _file ) )
                    throw new JoingServerVFSException( JoingServerVFSException.FILE_NOT_EXISTS );

                if( _file.getIsReadable() == 0 )
                    throw new JoingServerVFSException( JoingServerVFSException.NOT_READABLE );

                _file.setAccessed( new Date() );
                em.persist( _file );
                fileText = FileDTOs.createFileText( _file );
            }
            catch( RuntimeException exc )
            {
                if( ! (exc instanceof JoingServerException) )
                {
                    Constant.getLogger().throwing( getClass().getName(), "readTextFile(...)", exc );
                    exc = new JoingServerVFSException( JoingServerException.ACCESS_DB, exc );
                }
                
                throw exc;
            }
            catch( IOException exc )
            {
                Constant.getLogger().throwing( getClass().getName(), "readTextFile(...)", exc );
                throw new JoingServerVFSException( JoingServerException.ACCESS_NFS, exc );
            }
        }
        
        return fileText;
    }
    
    public FileBinary readBinaryFile( String sSessionId, int nFileId )
           throws JoingServerVFSException
    {
        FileBinary fileBinary = null;
        String     sAccount   = sessionManagerBean.getUserAccount( sSessionId );
        
        if( sAccount != null )
        {
            try
            {
                FileEntity _file = em.find( FileEntity.class, nFileId );
                
                if( _file == null )
                    throw new JoingServerVFSException( JoingServerVFSException.FILE_NOT_EXISTS );
                
                if( ! isFileInUserSpace( sAccount, _file ) )
                    throw new JoingServerVFSException( JoingServerVFSException.FILE_NOT_EXISTS );

                if( ! fileBinary.isReadable() )
                    throw new JoingServerVFSException( JoingServerVFSException.NOT_READABLE );
                    
                _file.setAccessed( new Date() );
                em.persist( _file );

                fileBinary = FileDTOs.createFileBinary( _file );
            }
            catch( RuntimeException exc )
            {
                if( ! (exc instanceof JoingServerException) )
                {
                    Constant.getLogger().throwing( getClass().getName(), "readBinaryFile(...)", exc );
                    exc = new JoingServerVFSException( JoingServerException.ACCESS_DB, exc );
                }
                
                throw exc;
            }
            catch( IOException exc )
            {
                Constant.getLogger().throwing( getClass().getName(), "readBinaryFile(...)", exc );
                throw new JoingServerVFSException( JoingServerException.ACCESS_NFS, exc );
            }
        }
        
        return fileBinary;
    }
    
    // TODO: Para writeTextFile y writeBinaryFile
    // Hay que poner un límite al tamaño del fichero que el usuario envía
    // si el límite se supera, el fichero queda truncado.
    // Si el límite es 0, el tamaño es ilimitado.
    // Este valor se guarda en UserEntity.
    public FileDescriptor writeTextFile( String sSessionId, FileText fileText )
           throws JoingServerVFSException
    {
        FileDescriptor fileDesc = null;
        String         sAccount = sessionManagerBean.getUserAccount( sSessionId );
        
        if( sAccount != null )
        {
            if( ! isFileInUserSpace( sAccount, fileText ) )
                throw new JoingServerVFSException( JoingServerVFSException.FILE_NOT_EXISTS );
            
            if( ! fileText.isModifiable() )
                throw new JoingServerVFSException( JoingServerVFSException.NOT_MODIFIABLE );

            if( ! fileText.ownsLock() )
                throw new JoingServerVFSException( JoingServerVFSException.LOCKED_BY_ANOTHER );
                
            if( ! hasQuota( sSessionId, fileText.getSize() ) )
                throw new JoingServerVFSException( JoingServerVFSException.NO_QUOTA );
            
            try
            {
                java.io.File       fNative  = NativeFileSystemTools.getFile( sAccount, fileText.getId() );   
                BufferedReader     reader   = fileText.getContent();
                OutputStreamWriter writer   = new OutputStreamWriter( new FileOutputStream( fNative ), "UTF16" );
                char[]             acBuffer = new char[ 1024*8 ];
                int                nReaded  = 0;
                
                while( nReaded != -1 )
                {
                     nReaded = reader.read( acBuffer );
                    
                    if( nReaded != -1 )
                        writer.write( acBuffer, 0, nReaded );
                }
                
                writer.flush();
                writer.close();
                reader.close();
                
                FileEntity _file = em.find( FileEntity.class, fileText.getId() );
                           _file.setAccessed( new Date() );
                           _file.setModified( new Date() );
                em.persist( _file );
                fileDesc = FileDTOs.createFileDescriptor( _file );
                fileDesc.setSize( NativeFileSystemTools.getFileSize( sAccount, fileText.getId() ) );
            }
            catch( RuntimeException exc )
            {
                if( ! (exc instanceof JoingServerException) )
                {
                    Constant.getLogger().throwing( getClass().getName(), "writeTextFile(...)", exc );
                    exc = new JoingServerVFSException( JoingServerException.ACCESS_DB, exc );
                }
                
                throw exc;
            }
            catch( IOException exc )
            {
                Constant.getLogger().throwing( getClass().getName(), "writeTextFile(...)", exc );
                throw new JoingServerVFSException( JoingServerException.ACCESS_NFS, exc );
            }
        }
        
        return fileDesc;
    }
    
    public FileDescriptor writeBinaryFile( String sSessionId, FileBinary fileBinary )
           throws JoingServerVFSException
    {
        FileDescriptor fileDesc = null;
        String         sAccount = sessionManagerBean.getUserAccount( sSessionId );
        
        if( sAccount != null )
        {
            if( ! isFileInUserSpace( sAccount, fileBinary ) )
                throw new JoingServerVFSException( JoingServerVFSException.FILE_NOT_EXISTS );
            
            if( ! fileBinary.isModifiable() )
                throw new JoingServerVFSException( JoingServerVFSException.NOT_MODIFIABLE );

            if( ! fileBinary.ownsLock() )
                throw new JoingServerVFSException( JoingServerVFSException.LOCKED_BY_ANOTHER );
                
            if( ! hasQuota( sSessionId, fileBinary.getSize() ) )
                throw new JoingServerVFSException( JoingServerVFSException.NO_QUOTA );
            
            try
            {
                java.io.File     fNative  = NativeFileSystemTools.getFile( sAccount, fileBinary.getId() );   
                InputStream      reader   = fileBinary.getContent();
                FileOutputStream writer   = new FileOutputStream( fNative );
                byte[]           abBuffer = new byte[ 1024*8 ];
                int              nReaded  = 0;
                
                while( nReaded != -1 )
                {
                     nReaded = reader.read( abBuffer );
                    
                    if( nReaded != -1 )                     
                        writer.write( abBuffer, 0, nReaded );
                }
                
                writer.flush();
                writer.close();
                reader.close();
                
                FileEntity _file = em.find( FileEntity.class, fileBinary.getId() );
                           _file.setAccessed( new Date() );
                           _file.setModified( new Date() );
                em.persist( _file );
                fileDesc = FileDTOs.createFileDescriptor( _file );
                fileDesc.setSize( NativeFileSystemTools.getFileSize( sAccount, fileBinary.getId() ) );
            }
            catch( RuntimeException exc )
            {
                if( ! (exc instanceof JoingServerException) )
                {
                    Constant.getLogger().throwing( getClass().getName(), "writeBinaryFile(...)", exc );
                    exc = new JoingServerVFSException( JoingServerException.ACCESS_DB, exc );
                }
                
                throw exc;
            }
            catch( IOException exc )
            {
                Constant.getLogger().throwing( getClass().getName(), "writeBinaryFile(...)", exc );
                throw new JoingServerVFSException( JoingServerException.ACCESS_NFS, exc );
            }
        }
        
        return fileDesc;
    }
    
    public boolean copy( String sSessionId, int nFileId, int nToDirId )
           throws JoingServerVFSException
    {
        throw new JoingServerVFSException( "Operation not yet implemented" );
        
        /* TODO: Hacerlo
        boolean bSuccess = false;
        
        // Modificar el ACCESSED en la tabla
        
        return bSuccess;*/
    }
    
    public boolean move( String sSessionId, int nFileId, int nToDirId )
           throws JoingServerVFSException
    {
        throw new JoingServerVFSException( "Operation not yet implemented" );
        
        /*boolean bSuccess = false;
        String  sAccount = sessionManagerBean.getUserAccount( sSessionId );
        
        if( sAccount != null )
        {
            if( ! isFileInUserSpace( sAccount, fileBinary.getId() ) )
                throw new JoingServerVFSException( JoingServerVFSException.FILE_NOT_EXISTS );
            
            if( ! isOwnerOfFile( sAccount, nFileId ) )
                throw new JoingServerVFSException( JoingServerVFSException.INVALID_OWNER );
              
            // TODO: hacerlo
            
            if( file.isDeleteable() )
            {
                bSuccess = copy( sSessionId, file, toDir );

                if( bSuccess )
                {
                    //_delete( sAccount, file );
                    // Modificar el ACCESSED en la tabla
                }
            }
            
        }
        
        return bSuccess;*/
    }
    
    public int[] trashcan( String sSessionId, int[] anFileId, boolean bInTrashCan )
           throws JoingServerVFSException
    {
        String             sAccount   = sessionManagerBean.getUserAccount( sSessionId );
        ArrayList<Integer> fileErrors = new ArrayList<Integer>();
        
        if( sAccount != null )
        {
            for( int n = 0; n <= anFileId.length; n++ )
                fileErrors.addAll( _trashCan( sAccount, anFileId[n], bInTrashCan ) );
        }
        
        // From Collection to array
        int[] anFileErrors = new int[ fileErrors.size() ];
        
        for( int n = 0; n < fileErrors.size(); n++ )
            anFileErrors[n] = fileErrors.get( n );
        
        return anFileErrors;
    }
    
    public int[] trashcan( String sSessionId, int nFileId, boolean bInTrashCan )
           throws JoingServerVFSException
    {
        String             sAccount   = sessionManagerBean.getUserAccount( sSessionId );
        ArrayList<Integer> fileErrors = new ArrayList<Integer>();
        
        if( sAccount != null )
        {
            fileErrors = _trashCan( sAccount, nFileId, bInTrashCan );
        }
        
        // From Collection to array
        int[] anFileErrors = new int[ fileErrors.size() ];
        
        for( int n = 0; n < fileErrors.size(); n++ )
            anFileErrors[n] = fileErrors.get( n );
        
        return anFileErrors;
    }
    
    public int[] delete( String sSessionId, int[] anFileId )
           throws JoingServerVFSException
    {
        String             sAccount   = sessionManagerBean.getUserAccount( sSessionId );
        ArrayList<Integer> fileErrors = new ArrayList<Integer>();
        
        if( sAccount != null )
        {
            for( int n = 0; n <= anFileId.length; n++ )
                fileErrors.addAll( _delete( sAccount, anFileId[n] ) );
        }
        
        // From Collection to array
        int[] anFileErrors = new int[ fileErrors.size() ];
        
        for( int n = 0; n < fileErrors.size(); n++ )
            anFileErrors[n] = fileErrors.get( n );
        
        return anFileErrors;
    }
    
    public int[] delete( String sSessionId, int nFileId )
           throws JoingServerVFSException
    {
        String             sAccount   = sessionManagerBean.getUserAccount( sSessionId );
        ArrayList<Integer> fileErrors = new ArrayList<Integer>();
        
        if( sAccount != null )
        {
            fileErrors = _delete( sAccount, nFileId );
        }
        
        // From Collection to array
        int[] anFileErrors = new int[ fileErrors.size() ];
        
        for( int n = 0; n < fileErrors.size(); n++ )
            anFileErrors[n] = fileErrors.get( n );
        
        return anFileErrors;
    }
    
    //------------------------------------------------------------------------//
    // LOCAL INTERFACE
    
    public FileEntity createRootEntity( String sAccount )
    {
        FileEntity _file = new FileEntity();
                   _file.setAccount( sAccount );    // Can't be null
                   _file.setFilePath( "" );         // Can't be null
                   _file.setFileName( "/" );
                   _file.setOwner( Constant.getSystemName() );   // It's guaranted that will never be Account.equals( SystemName ). (see: sessionManagerBean.isAccountAvailable)
                   _file.setLockedBy( null );
                   _file.setIsDir(        (short) 1 );
                   _file.setIsAlterable(  (short) 0 );
                   _file.setIsDeleteable( (short) 0 );
                   _file.setIsDuplicable( (short) 0 );
                   _file.setIsExecutable( (short) 0 );
                   _file.setIsHidden(     (short) 0 );
                   _file.setIsInTrashcan( (short) 0 );
                   _file.setIsModifiable( (short) 0 );
                   _file.setIsPublic(     (short) 0 );
                   _file.setAccessed( new Date() );
                   _file.setCreated(  _file.getAccessed() );
                   _file.setModified( _file.getAccessed() );
                   
       return _file;
    }
    
    //------------------------------------------------------------------------//
    // PRIVATE SCOPE
 
    // Recursively moves files and directories from and to the trashcan
    // If the file is found it can always been moved to and from trashcan.
    // Note: This method returns an ArrayList instead of int[] to increase overall speed.
    private ArrayList<Integer> _trashCan( String sAccount, int nFileId, boolean bInTrashCan )
            throws JoingServerVFSException
    {
        ArrayList<Integer> fileErrors = new ArrayList<Integer>();
        
        try
        {
            FileEntity _file = em.find( FileEntity.class, nFileId );
            
            if( _file == null )
                throw new JoingServerVFSException( JoingServerVFSException.FILE_NOT_EXISTS );
            
            if( ! isFileInUserSpace( sAccount, _file ) )
                throw new JoingServerVFSException( JoingServerVFSException.FILE_NOT_EXISTS );

            if( _file.getIsDir() != 0 )   // Is a directory: get all _file's childs
            {
                List<FileEntity> _fChilds = VFSTools.getChilds( em, sAccount, _file );
                
                for( FileEntity _f : _fChilds )
                    fileErrors.addAll( _trashCan( sAccount, _f.getIdFile(), bInTrashCan ) );
            }
            else                          // Is a file
            {
                _file.setIsInTrashcan( (short) (bInTrashCan ? 1 : 0) );
                em.persist( _file );
            }
        }
        catch( RuntimeException exc )
        {
            if( ! (exc instanceof JoingServerException) )
            {
                Constant.getLogger().throwing( getClass().getName(), "_trashcan(...)", exc );
                exc = new JoingServerVFSException( JoingServerException.ACCESS_DB, exc );
            }
            
            throw exc;
        }
        
        return fileErrors;
    }
 
    // Recursively deletes a file or a directory in DB (not in FS)
    // Note: This method returns an ArrayList instead of int[] to increase overall speed.
    private ArrayList<Integer> _delete( String sAccount, int nFileId )
    {
        ArrayList<Integer> fileErrors = new ArrayList<Integer>();
        
        try
        {
            FileEntity _file = em.find( FileEntity.class, nFileId );
            
            if( _file == null )
                throw new JoingServerVFSException( JoingServerVFSException.FILE_NOT_EXISTS );
            
            if( ! isFileInUserSpace( sAccount, _file ) )
                throw new JoingServerVFSException( JoingServerVFSException.FILE_NOT_EXISTS );

            if( _file.getIsDeleteable() == 0 )   // Marked as not deleteable (to delete: change the attribute and try again)
                throw new JoingServerVFSException( JoingServerVFSException.NOT_DELETEABLE );

            if( _file.getIsDir() != 0 )  // Is a directory: get all _file's childs
            {
                List<FileEntity> _fChilds = VFSTools.getChilds( em, sAccount, _file );
                
                for( FileEntity _f : _fChilds )
                    fileErrors.addAll( _delete( sAccount, _f.getIdFile() ) );
                
                // Si borrase el dir y algunos fics no se pudieron borrar, se quedarán en el limbo para siempre
                if( fileErrors.size() == 0 )
                    em.remove( _file );   // Deletes table row (directories only exist in table FILES not in FS)
            }
            else
            {
                em.remove( _file );
                if( ! NativeFileSystemTools.deleteFile( sAccount, _file.getIdFile() ) )
                    fileErrors.add( _file.getIdFile() );
            }
        }
        catch( RuntimeException exc )
        {
            if( ! (exc instanceof JoingServerException) )
            {
                Constant.getLogger().throwing( getClass().getName(), "_delete(...)", exc );
                exc = new JoingServerVFSException( JoingServerException.ACCESS_DB, exc );
            }
            
            throw exc;
        }
        
        return fileErrors;
    }
    
    // Creates a new entry: either a directory or a file
    // TODO: Tiene que heredar los atributos (properties) del padre (a menos que el padre sea root, que es muy especial)
    private FileDescriptor createEntry( String sSessionId, String sPath, String sChild, boolean bIsDir )
            throws JoingServerVFSException
    {
        FileDescriptor file     = null;
        String         sAccount = sessionManagerBean.getUserAccount( sSessionId );
        
        if( sAccount != null )
        {
            FileEntity _feParent = VFSTools.path2File( em, sAccount, sPath );

            if( _feParent == null )
                throw new JoingServerVFSException( JoingServerVFSException.PARENT_DIR_NOT_EXISTS );
            
            if( _feParent.getIsDir() == 0 )
                throw new JoingServerVFSException( JoingServerVFSException.INVALID_PARENT );
            
            if( ! _feParent.getAccount().equals( sAccount ) )   // Parent is not user disk-space
                throw new JoingServerVFSException( JoingServerVFSException.PARENT_DIR_NOT_EXISTS );
            
            String sNameErrors = validateName( sChild );
            
            if( sNameErrors.length() > 0 )
                throw new JoingServerVFSException( sNameErrors );
            
            sChild = sChild.trim();
            
            
            if( existsName( sAccount, sPath, sChild ) )
                throw new JoingServerVFSException( (_feParent.getIsDir() == 1 ? 
                                                        JoingServerVFSException.DIR_ALREADY_EXISTS :
                                                        JoingServerVFSException.FILE_ALREADY_EXISTS) );
            try
            {
                String sFullAccount = sAccount +'@'+ Constant.getSystemName();
                        
                em.getTransaction().begin();

                FileEntity _file = new FileEntity();
                           _file.setAccount( sFullAccount );
                           _file.setFilePath( sPath  );
                           _file.setFileName( sChild );
                           _file.setOwner( sFullAccount );
                           _file.setIsDir( (short) (bIsDir ? 1 : 0) );
                           _file.setLockedBy( null );
                           _file.setIsAlterable(  (short) 1 );
                           _file.setIsDeleteable( (short) 1 );
                           _file.setIsDuplicable( (short) 1 );
                           _file.setIsExecutable( (short) 0 );
                           _file.setIsHidden(     (short) 0 );
                           _file.setIsInTrashcan( (short) 0 );
                           _file.setIsModifiable( (short) 1 );
                           _file.setIsPublic(     (short) 0 );
                           _file.setCreated(  new Date() );
                           _file.setAccessed( new Date() );
                           _file.setModified( new Date() );
                           
                em.persist( _file );

                if( ! bIsDir )  // Files exist in FILES table and in host FS, dirs only in FILES
                    NativeFileSystemTools.createFile( sAccount, _file.getIdFile() );
                    
                // If code arrives to this point, everything was OK: now can commit
                em.getTransaction().commit();

                file = FileDTOs.createFileDescriptor( _file );
            }
            catch( RuntimeException exc )
            {
                if( ! (exc instanceof JoingServerException) )
                {
                    Constant.getLogger().throwing( getClass().getName(), "createEntry(...)", exc );
                    exc = new JoingServerVFSException( JoingServerException.ACCESS_DB, exc );
                }
            
                throw exc;
            }
            catch( IOException exc )
            {
                Constant.getLogger().throwing( getClass().getName(), "createEntry(...)", exc );
                throw new JoingServerVFSException( JoingServerException.ACCESS_NFS, exc );
            }
            finally
            {
                if( em.getTransaction().isActive() )
                    em.getTransaction().rollback();
            }
        }
        
        return file;
    }
    
    // Checks if a file name already exists
    // Note: a file exists even if it is in the trashcan ('cause it can be moved back)
    private boolean existsName( String sAccount, String sPath, String sName )
            throws JoingServerVFSException
    {
        boolean bExists = true;
        
        try
        {
            Query query = em.createNamedQuery( "FileEntity.findByPathAndName" );
            
            query.setParameter( "account", sAccount );
            query.setParameter( "path"   , sPath    );
            query.setParameter( "name"   , sName    );
            query.getSingleResult();
        }
        catch( NoResultException exc )
        {
            bExists = false;
        }
        catch( RuntimeException exc )
        {
            Constant.getLogger().throwing( getClass().getName(), "existsName(...)", exc );
            throw new JoingServerVFSException( JoingServerException.ACCESS_DB, exc );
        }
        
        return bExists;
    }
    
    // It can be that the user is not the owner of the file but he/she has 
    // access to it because the file is stored in his/her disk space.
    // This is just a security meassure against people trying to explode Join'g
    // (by sending random file Ids)
    private boolean isFileInUserSpace( String sAccount, FileEntity _file )
    {
        if( _file.getAccount().equals( sAccount ) )            // Account got from SessionId and Account from FileEntity match.
        {
            FileEntity _fReal = em.find( FileEntity.class, _file.getIdFile() );  // Get the real FileEntity from DB that corresponds to passed _file (by Id)
        
            return _fReal.getAccount().equals( sAccount );     // Compare both accounts  (Note: sAccount was already compared 2 lines above)
        }
        
        return false;
    }
    
    private boolean isFileInUserSpace( String sAccount, FileDescriptor fd )
    {
        if( fd.getAccount() != null && fd.getAccount().equals( sAccount ) )   // Account got from SessionId and Account from FileDescriptor match.
        {
            FileEntity _fReal = em.find( FileEntity.class, fd.getId() );      // Get the real FileEntity from DB that corresponds to passed _file (by Id)
        
            return _fReal.getAccount().equals( sAccount );  // Compare both accounts  (Note: sAccount was already compared 2 lines above)
        }
        
        return false;
    }
    
    // Is this a valid name for a file or directory?
    private String validateName( String sName )
    {
        StringBuilder sbError = new StringBuilder();
        
        if( sName == null )
            sbError.append( "\nCan not be null." );
        
        if( sName.length() == 0 )
            sbError.append( "\nCan not be empty." );
        
        if( sName.length() > 255 )
            sbError.append( "\nCan not be longer than 255 characters." );
        
        if( sName.indexOf( '/' ) > -1 )   // Can't contain path separator
            sbError.append( "\nInvalid character '/'." );
        
        if( sName.charAt( 0 ) == ' ' )    // can't start with space
            sbError.append( "\nCan not start with blank space." );
        
        if( sName.charAt( sName.length() - 1 ) == ' ' )   // can't end with space
            sbError.append( "\nCan not end with blank space." );
        
        if( sbError.length() > 0 )
            sbError.insert( 0, "Invalid file name:" );
        
        return sbError.toString();
    }
    
    private boolean hasQuota( String sSesionId, long nSize )
    {
        User user = userManagerBean.getUser( sSesionId );
        
        return (user.getTotalSpace() - user.getUsedSpace() - nSize >= 0);
    }
}