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
import ejb.JoingServerException;
import ejb.session.JoingServerSessionException;
import ejb.session.SessionManagerLocal;
import ejb.user.User;
import ejb.user.UserManagerLocal;
import java.io.InputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
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
            FileEntity _file = Tools.path2File( em, sAccount, sFullName  );
            
            if( _file != null )
                file = FileDTOs.createFileDescriptor( _file );
            else
                throw new JoingServerVFSException( JoingServerVFSException.FILE_NOT_EXISTS ); 
        }
        
        return file;
    }
    
    // This method does not need to implement the logic to handle (validate) file 
    // attributes): the logic (validation) is done by class FileDescriptor.
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
                if( ! isFileInUserSpace( sAccount, fileIn.getId() ) )
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
                String sNameOld = _file.getFileEntityPK().getFileName();
                
                if( (! sNameOld.equals( sNameNew )) && (sNameNew != null) )
                {
                    String sNameErrors = validateName( sNameNew );
                    
                    if( sNameErrors.length() > 0 )
                        throw new JoingServerVFSException( sNameErrors );

                    if( existsName( sAccount, _file.getFileEntityPK().getFilePath(), sNameNew ) )
                        throw new JoingServerVFSException( JoingServerVFSException.FILE_NAME_EXISTS );
                    
                    _file.getFileEntityPK().setFileName( sNameNew );
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
            if( ! isFileInUserSpace( sAccount, nFileId ) )
                throw new JoingServerVFSException( JoingServerVFSException.FILE_NOT_EXISTS );

            if( ! fileText.isReadable() )
                throw new JoingServerVFSException( JoingServerVFSException.NOT_READABLE );
            
            try
            {
                FileEntity _file = em.find( FileEntity.class, nFileId );
                
                if( _file != null )
                {
                    _file.setAccessed( new Date() );
                    em.persist( _file );
                    fileText = FileDTOs.createFileText( _file );
                }
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
            if( ! isFileInUserSpace( sAccount, nFileId ) )
                throw new JoingServerVFSException( JoingServerVFSException.FILE_NOT_EXISTS );
            
            if( ! fileBinary.isReadable() )
                throw new JoingServerVFSException( JoingServerVFSException.NOT_READABLE );
            
            try
            {
                FileEntity _file = em.find( FileEntity.class, nFileId );
                
                if( _file != null )
                {
                    _file.setAccessed( new Date() );
                    em.persist( _file );

                    fileBinary = FileDTOs.createFileBinary( _file );
                }
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
            if( ! isFileInUserSpace( sAccount, fileText.getId() ) )
                throw new JoingServerVFSException( JoingServerVFSException.FILE_NOT_EXISTS );
            
            if( ! fileText.isModifiable() )
                throw new JoingServerVFSException( JoingServerVFSException.NOT_MODIFIABLE );

            if( ! fileText.ownsLock() )
                throw new JoingServerVFSException( JoingServerVFSException.LOCKED_BY_ANOTHER );
                
            if( ! hasQuota( sSessionId, fileText.getSize() ) )
                throw new JoingServerVFSException( JoingServerVFSException.NO_QUOTA );
            
            try
            {
                java.io.File       fNative  = FileSystemTools.getFile( sAccount, fileText.getId() );   
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
                fileDesc.setSize( FileSystemTools.getFileSize( sAccount, fileText.getId() ) );
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
            if( ! isFileInUserSpace( sAccount, fileBinary.getId() ) )
                throw new JoingServerVFSException( JoingServerVFSException.FILE_NOT_EXISTS );
            
            if( ! fileBinary.isModifiable() )
                throw new JoingServerVFSException( JoingServerVFSException.NOT_MODIFIABLE );

            if( ! fileBinary.ownsLock() )
                throw new JoingServerVFSException( JoingServerVFSException.LOCKED_BY_ANOTHER );
                
            if( ! hasQuota( sSessionId, fileBinary.getSize() ) )
                throw new JoingServerVFSException( JoingServerVFSException.NO_QUOTA );
            
            try
            {
                java.io.File     fNative  = FileSystemTools.getFile( sAccount, fileBinary.getId() );   
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
                fileDesc.setSize( FileSystemTools.getFileSize( sAccount, fileBinary.getId() ) );
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
            {
                int[] anErr = _trashCan( sAccount, anFileId[n], bInTrashCan );
                
                for( int x = 0; x < anErr.length; x++ )
                    fileErrors.add( anErr[x] );
            }
        }
        
        int[] anFileErrors = new int[ fileErrors.size() ];
        
        for( int n = 0; n < fileErrors.size(); n++ )
            anFileErrors[n] = fileErrors.get( n );
        
        return anFileErrors;
    }
    
    public int[] trashcan( String sSessionId, int nFileId, boolean bInTrashCan )
           throws JoingServerVFSException
    {
        String sAccount  = sessionManagerBean.getUserAccount( sSessionId );
        int[]  anFileErr = new int[0];
        
        if( sAccount != null )
            anFileErr = _trashCan( sAccount, nFileId, bInTrashCan );
        
        return anFileErr;
    }
    
    public int[] delete( String sSessionId, int[] anFileId )
           throws JoingServerVFSException
    {
        String             sAccount   = sessionManagerBean.getUserAccount( sSessionId );
        ArrayList<Integer> fileErrors = new ArrayList<Integer>();
        
        if( sAccount != null )
        {
            for( int n = 0; n <= anFileId.length; n++ )
            {
                int[] anErr = _delete( sAccount, anFileId[n] );
                
                for( int x = 0; x < anErr.length; x++ )
                    fileErrors.add( anErr[x] );
            }
        }
        
        int[] anFileErrors = new int[ fileErrors.size() ];
        
        for( int n = 0; n < fileErrors.size(); n++ )
            anFileErrors[n] = fileErrors.get( n );
        
        return anFileErrors;
    }
    
    public int[] delete( String sSessionId, int nFileId )
           throws JoingServerVFSException
    {
        String sAccount  = sessionManagerBean.getUserAccount( sSessionId );
        int[]  anFileErr = new int[0];
        
        if( sAccount != null )
            anFileErr = _delete( sAccount, nFileId );
        
        return anFileErr;
    }
    
    //------------------------------------------------------------------------//
    // LOCAL INTERFACE
    /**
     * This is a method needed just because I can't find a way to return an 
     * stream from an EJB method (streams can't be serialized).
     * See coments inside FileManagerBean class code for more information.
     * <p>
     * This is the reason why this method is accesible only from @Local.
     * 
     * @param sAccount The user account
     * @param nFileId The result of invokink <code>File.getId()</code>
     * @param bToWrite <code>true</code> if the file is going to be used to 
     *        write and <code>false</code> if will be used to read only.
     */
    //java.io.File getNativeFile( String sAccount, int nFileId, boolean bToWrite );
    /*public java.io.File getNativeFile( String sAccount, int nFileId, boolean bToWrite )
    {
        java.io.File fNative = null;
        
        if( isOwnerOfFile( sAccount, nFileId ) )
        {            
            fNative = FileSystemTools.getFile( sAccount, Integer.toString( nFileId ) );
            
            FileEntity _file = em.find( FileEntity.class, nFileId );
                       _file.setAccessed( new Date() );
                       
            if( bToWrite )
                _file.setModified( new Date() );
            
            em.persist( _file );
        }
        
        return fNative;
    }*/
    
    //------------------------------------------------------------------------//
    // LOCAL INTERFACE
    
    public FileEntity createRootEntity( String sAccount )
    {
        FileEntityPK _fepk = new FileEntityPK();
                         _fepk.setAccount( sAccount );
                         _fepk.setFilePath( "" );         // Can't be null
                         _fepk.setFileName( "/" );
                         
        FileEntity _file = new FileEntity();
                   _file.setFileEntityPK( _fepk );    // This method is protected, and therefore it has to be accessed in the same package
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
    private int[] _trashCan( String sAccount, int nFileId, boolean bInTrashCan )
            throws JoingServerVFSException
    {
        ArrayList<Integer> fileErrors = new ArrayList<Integer>();
        
        try
        {
            FileEntity _file = em.find( FileEntity.class, nFileId );
            
            if( _file == null )
                throw new JoingServerVFSException( JoingServerVFSException.FILE_NOT_EXISTS );
            
            if( ! _file.getFileEntityPK().getAccount().equals( sAccount ) )   // File is not in user disk space
                throw new JoingServerVFSException( JoingServerVFSException.FILE_NOT_EXISTS );

            if( _file.getIsDir() != 0 )   // Is a directory
            {
                // Get all childs (those which parent is _file)
                Query query = em.createNamedQuery( "FileEntity.findByIdParent" );
                      query.setParameter( "idParent", _file.getIdFile() );

                List<FileEntity> _fChilds = (List<FileEntity>) query.getResultList();

                for( FileEntity _f : _fChilds )
                {
                    int[] err = _trashCan( sAccount, _f.getIdFile(), bInTrashCan );
                    
                    if( err.length > 0 )
                        fileErrors.add( err[0] );
                }
            }
            else      // Is a file
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
        
        int[] anFileErrors = new int[ fileErrors.size() ];
        
        for( int n = 0; n < fileErrors.size(); n++ )
            anFileErrors[n] = fileErrors.get( n );
        
        return anFileErrors;
    }
 
    // Recursively deletes a file or a directory in DB (not in FS)
    private int[] _delete( String sAccount, int nFileId )
    {
        ArrayList<Integer> fileErrors = new ArrayList<Integer>();
        
        try
        {
            FileEntity _file = em.find( FileEntity.class, nFileId );
            
            if( _file == null )
                throw new JoingServerVFSException( JoingServerVFSException.FILE_NOT_EXISTS );
            
            if( ! _file.getFileEntityPK().getAccount().equals( sAccount ) )   // File is not in user disk space
                throw new JoingServerVFSException( JoingServerVFSException.FILE_NOT_EXISTS );

            if( _file.getIsDeleteable() == 0 )   // Marked as not deleteable (to delete: change the attribute and try again)
                throw new JoingServerVFSException( JoingServerVFSException.NOT_DELETEABLE );

            if( _file.getIsDir() != 0 )  // Is a directory
            {
                // Gets all childs (those which parent is _file)
                Query query = em.createNamedQuery( "FileEntity.findByIdParent" );
                      query.setParameter( "idParent", _file.getIdFile() );

                List<FileEntity> _fChilds = (List<FileEntity>) query.getResultList();

                for( FileEntity _f : _fChilds )
                {
                    int[] err = _delete( sAccount, _f.getIdFile() );
                    
                    if( err.length > 0 )
                        fileErrors.add( err[0] );
                }
                
                // Si borro el dir y algunos fics no se han podido borrar se quedarán en el limbo para siempre
                if( fileErrors.size() == 0 )
                    em.remove( _file );   // Deletes table row (directories only exist in table FILES not in FS)
            }
            else
            {
                em.remove( _file );
                if( ! FileSystemTools.deleteFile( sAccount, _file.getIdFile() ) )
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
        
        int[] anFileErrors = new int[ fileErrors.size() ];
        
        for( int n = 0; n < fileErrors.size(); n++ )
            anFileErrors[n] = fileErrors.get( n );
        
        return anFileErrors;
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
            FileEntity _feParent = Tools.path2File( em, sAccount, sPath );

            if( _feParent == null )
                throw new JoingServerVFSException( JoingServerVFSException.PARENT_DIR_NOT_EXISTS );
            
            if( _feParent.getIsDir() == 0 )
                throw new JoingServerVFSException( JoingServerVFSException.INVALID_PARENT );
            
            if( ! _feParent.getFileEntityPK().getAccount().equals( sAccount ) )   // Parent is not user disk-space
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

                FileEntityPK _fepk = new FileEntityPK();
                             _fepk.setAccount( sFullAccount );
                             _fepk.setFilePath( sPath  );
                             _fepk.setFileName( sChild );

                FileEntity _file = new FileEntity();
                           _file.setOwner( sFullAccount );
                           _file.setIsDir( (short) (bIsDir ? 1 : 0) );
                           _file.setLockedBy( null );
                           _file.setFileEntityPK( _fepk );
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
                    FileSystemTools.createFile( sAccount, _file.getIdFile() );
                    
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
    
    // Returns parent Entity based on its ID, or null if it does not exists.
    private FileEntity getParentEntity( int nParentId )
            throws JoingServerVFSException
    {
        FileEntity _feParent = null;
        
        try
        {
            _feParent = em.find( FileEntity.class, nParentId );
        }
        catch( RuntimeException exc )
        {
            Constant.getLogger().throwing( getClass().getName(), "getParentEntity(...)", exc );
            throw new JoingServerVFSException( JoingServerException.ACCESS_DB, exc );
        }
        
        return _feParent;
    }
    
    // Checks if a file name already exists
    private boolean existsName( String sAccount, String sPath, String sName )
            throws JoingServerVFSException
    {
        boolean bExists = true;
        
        try
        {
            Query query = em.createQuery( "SELECT a FROM ApplicationEntity a"+
                                          " WHERE a.account  = :account"+
                                          "   AND a.filePath = :path"+
                                          "   AND a.fileName = :name" );
            
            if( sAccount.indexOf( '@' ) == -1 )
                sAccount = sAccount +'@'+ Constant.getSystemName();
            
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
    private boolean isFileInUserSpace( String sAccount, int nIdFile )
    {
        FileEntity _file = em.find( FileEntity.class, nIdFile );
        
        return _file.getFileEntityPK().getAccount().equals( sAccount );
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