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
import java.io.InputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Serializable;
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
       implements FileManagerRemote, FileManagerLocal, Serializable   // TODO hacer el serializable
{    
    @PersistenceContext
    private EntityManager em;
    
    @EJB
    private SessionManagerLocal sessionManagerBean;
    
    //------------------------------------------------------------------------//
    
    public FileDescriptor getFile( String sSessionId, String sPath )
    {
        FileDescriptor    file     = null;
        String  sAccount = sessionManagerBean.getUserAccount( sSessionId );
        
        if( sAccount != null )
        {
            FileEntity _file = path2File( sAccount, sPath );
            
            if( _file != null )
                file = new FileDescriptor( _file );
        }
        
        return file;
    }
    
    public FileDescriptor updateFile(  String sSessionId, FileDescriptor file  )
    {
        String sAccount = sessionManagerBean.getUserAccount( sSessionId );
        FileDescriptor   fileRet  = null;
        
        if( sAccount != null && isOwnerOfFile( sAccount, file.getId() ) )
        {
            try
            {
                // Get the original file
                FileEntity _fileOld = em.find( FileEntity.class, file.getId() );
                FileEntity _fileNew = em.find( FileEntity.class, file.getId() );
                file.update( _fileNew );    // Places new information in this object
                
                // If the file name is changed
                String sNameNew = file.getName();
                String sNameOld = _fileOld.getFileEntityPK().getName();
                
                if( ! sNameOld.equals( sNameNew ) )
                {
                    /*if( isValidName( sNameNew ) && 
                        (! exists( file.getName() )) )*/
                    // TODO: hacerlo
                }
                
                // TODO: comprobar todos los permisos
                //       p.ej. si es un dir, no puede cambiar a tipo fichero, ni a tipo ejecutable, etc.
                
                _fileNew.setAccessed( new Date() );  // Modified is only when modifiying contents
                em.persist( _fileNew );
                fileRet = new FileDescriptor( _fileNew );
            }
            catch( Exception exc )
            {
                Constant.getLogger().throwing( getClass().getName(), "update(...)", exc );
            }
        }
        
        return fileRet;
    }
    
    public FileDescriptor createDirectory( String sSessionId, int nParentId, String sDirName )
    {
        return createEntry( sSessionId, nParentId, sDirName, true );
    }
    
    public FileDescriptor createFile( String sSessionId, int nParentId, String sFileName )
    {
        return createEntry( sSessionId, nParentId, sFileName, false );
    }

    /* TODO: para read y write files
     * Pensar qué hacer con esto:
     * El problema es que no se puede serializar (RMI/IIOP) un stream, y sin
     * embargo, los métodos de los EJBs tienen que ser serializables.
     * Por lo que no sé cómo obtener un stream desde una invocación @Remote a 
     * un EJB.*/
    // Help at: http://java.sun.com/docs/books/tutorial/i18n/text/stream.html
    public FileText readTextFile( String sSessionId, int nFileId, String sEncoding )
    {        
        FileText fileText = null;
        String   sAccount = sessionManagerBean.getUserAccount( sSessionId );
        
        if( sAccount != null && isOwnerOfFile( sAccount, nFileId ) )
        {
            String       sNative = Integer.toString( nFileId );
            java.io.File fNative = FileSystemTools.getFile( sAccount, sNative );
            
            try
            {
                FileInputStream   fis = new FileInputStream( fNative );
                InputStreamReader isw = new InputStreamReader( fis, sEncoding );
                BufferedReader    br  = new BufferedReader( isw );
                
                FileEntity _file = getFileEntityById( nFileId );
                           _file.setAccessed( new Date() );
                           
                em.persist( _file );
                
                fileText = new FileText( _file );
                fileText.setMimetype( null );   // TODO: averiguarlo
                fileText.setContents( br );
                
                br.close();
            }
            catch( Exception exc )
            {
                Constant.getLogger().throwing( getClass().getName(), "readTextFile(...)", exc );
            }
        }
        
        return fileText;
    }
    
    public FileBinary readBinaryFile( String sSessionId, int nFileId )
    {
        FileBinary fileBinary = null;
        String     sAccount   = sessionManagerBean.getUserAccount( sSessionId );
        
        if( sAccount != null && isOwnerOfFile( sAccount, nFileId ) )
        {
            String       sNative = Integer.toString( nFileId );
            java.io.File fNative = FileSystemTools.getFile( sAccount, sNative );

            try
            {
                FileInputStream fis = new FileInputStream( fNative );
                
                FileEntity _file = getFileEntityById( nFileId );
                           _file.setAccessed( new Date() );
                em.persist( _file );

                fileBinary = new FileBinary( _file );
                fileBinary.setContents( fis );
                
                fis.close();
            }
            catch( IOException exc )
            {
                Constant.getLogger().throwing( getClass().getName(), "readBinaryFile(...)", exc );
            }
        }
        
        return fileBinary;
    }
    
    // TODO: Para writeTextFile y writeBinaryFile
    // Hay que poner un límite al tamaño del fichero que el usuario envía
    // si el límite se supera, el fichero queda truncado.
    // Si el límite es 0, el tamaño es ilimitado.
    // Este valor se guarda en UserEntity.
    public boolean writeTextFile( String sSessionId, FileText fileText )
    {
        boolean bSuccess = false;
        String  sAccount = sessionManagerBean.getUserAccount( sSessionId );
        
        if( sAccount != null && isOwnerOfFile( sAccount, fileText.getId() ) )
        {
            try
            {                
                String       sNative = Integer.toString( fileText.getId() );
                java.io.File fNative = FileSystemTools.getFile( sAccount, sNative );
                
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
                
                FileEntity _file = getFileEntityById( fileText.getId() );
                           _file.setAccessed( new Date() );
                           _file.setModified( new Date() );
                em.persist( _file );
                
                bSuccess = true;
            }
            catch( Exception exc )
            {
                Constant.getLogger().throwing( getClass().getName(), "readText(...)", exc );
            }
        }
        
        return bSuccess;
    }
    
    public boolean writeBinaryFile( String sSessionId, FileBinary fileBinary )
    {
        boolean bSuccess = false;
        String  sAccount = sessionManagerBean.getUserAccount( sSessionId );
        
        if( sAccount != null && isOwnerOfFile( sAccount, fileBinary.getId() ) )
        {
            try
            {
                String       sNative = Integer.toString( fileBinary.getId() );
                java.io.File fNative = FileSystemTools.getFile( sAccount, sNative );
                
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
                
                FileEntity _file = getFileEntityById( fileBinary.getId() );
                           _file.setAccessed( new Date() );
                           _file.setModified( new Date() );
                em.persist( _file );
                
                bSuccess = true;
            }
            catch( Exception exc )
            {
                Constant.getLogger().throwing( getClass().getName(), "readText(...)", exc );
            }
        }
        
        return bSuccess;
    }
    
    public boolean copy( String sSessionId, int nFileId, int nToDirId )
    {
        boolean bSuccess = false;
        
        // Modificar el ACCESSED en la tabla
        // TODO: Hacerlo
        return bSuccess;
    }
    
    public boolean move( String sSessionId, int nFileId, int nToDirId )
    {
        boolean bSuccess = false;
        
        // TODO: hacerlo
        /*
        if( file.isDeleteable() )
        {
            bSuccess = copy( sSessionId, file, toDir );
            
            if( bSuccess )
            {
                ///_delete( sAccount, file );
                // Modificar el ACCESSED en la tabla
            }
        }
        */
        return bSuccess;
    }
    
    public boolean trashcan( String sSessionId, int[] anFileId, boolean bInTrashCan )
    {        
        boolean bSuccess = true;
        String  sAccount = sessionManagerBean.getUserAccount( sSessionId );
        
        if( sAccount != null )
        {            
            for( int n = 0; n <= anFileId.length; n++ )
                bSuccess = bSuccess && _trashCan( sAccount, 
                                                  getFileEntityById( anFileId[n] ), 
                                                  bInTrashCan );
        }
        
        return bSuccess;
    }
    
    public boolean trashcan( String sSessionId, int nFileId, boolean bInTrashCan )
    {        
        boolean bSuccess = true;
        String  sAccount = sessionManagerBean.getUserAccount( sSessionId );
        
        if( sAccount != null )
            bSuccess = _trashCan( sAccount, 
                                  getFileEntityById( nFileId ), 
                                  bInTrashCan );
        
        return bSuccess;
    }
    
    public boolean delete( String sSessionId, int[] anFileId )
    {
        boolean bSuccess = false;
        String  sAccount = sessionManagerBean.getUserAccount( sSessionId );
        
        if( sAccount != null )
        {
            bSuccess = true;
            
            for( int n = 0; n <= anFileId.length; n++ )
                bSuccess = bSuccess && _delete( sAccount, getFileEntityById( anFileId[n] ) );
        }
        
        return bSuccess;
    }
    
    public boolean delete( String sSessionId, int nFileId )
    {
        boolean bSuccess = false;
        String  sAccount = sessionManagerBean.getUserAccount( sSessionId );
        
        if( sAccount != null )
            bSuccess = _delete( sAccount, getFileEntityById( nFileId ) );
        
        return bSuccess;
    }
    
    /*public java.io.File getNativeFile( String sSessionId, int nFileId, boolean bToWrite )
    {
        java.io.File fNative  = null;
        String       sAccount = sessionManagerBean.getUserAccount( sSessionId );
        
        if( sAccount != null && isOwnerOfFile( sAccount, nFileId ) )
        {            
            fNative = FileSystemTools.getFile( sAccount, Integer.toString( nFileId ) );
            
            FileEntity _file = getFileEntityById( nFileId );
                       _file.setAccessed( new Date() );
                       
            if( bToWrite )
                _file.setModified( new Date() );
            
            em.persist( _file );
        }
        
        return fNative;
    }*/
    
    //------------------------------------------------------------------------//
    // PACKAGE SCOPE
    
    /**
     * Obtains the FileEntity instance that represents the file or directory
     * passed as String.
     *
     * @param sAccount An Account representing the owner of the file ro dir.
     * @param sPath Path starting at root ("/") and ending with the file or dir
     *              name.
     * @return An instance of class <code>FileEntity</code> that represents the 
     *         file or directory denoted by passed path or <code>null</code> if 
     *         the path does not corresponds with an existing file.
     */
    FileEntity path2File( String sAccount, String sPath )
    {
        FileEntity _file = null;
                           // Path must start from root
        if( sPath != null && sPath.trim().charAt( 0 ) == '/' )
        { 
            sPath = sPath.trim();

            Query query = this.em.createQuery( "SELECT f FROM FileEntity f"+
                                               " WHERE f.account = :account"+
                                               "   AND f.fullPath = :fullPath" );
            
            // Assuming that it is a file (it is not a dir)
            if( ! sPath.endsWith( "/" ) )
            {
                int    nIndex   = sPath.lastIndexOf( '/' );
                String sFile    = sPath.substring( nIndex + 1 );
                String sPathTmp = sPath.substring( 0, nIndex );    // Can't modify sPath because is used later
                
                query.setParameter( "account" , sAccount );
                query.setParameter( "fullPath", sPathTmp );
                
                try
                {
                    FileEntity _dir = (FileEntity) query.getSingleResult();
                    
                    String s = "SELECT f FROM FileEntity f"+
                               " WHERE f.account = '"+ sAccount +"'"+
                               "   AND f.fileEntityPK.idParent = "+ _dir.getFileEntityPK().getIdParent() +
                               "   AND f.name = '"+ sFile +"'";
                    
                    Query  q = this.em.createQuery( s );
                    
                    _file = (FileEntity) q.getSingleResult();
                }
                catch( NoResultException exc )
                {
                    // Nothing to do
                }
            }
            
            // Assuming that it is a dir (it is not a file)
            if( _file == null )
            {
                if( ! sPath.endsWith( "/" ) )
                    sPath += "/";
                
                query.setParameter( "account", sAccount );
                query.setParameter( "fullPath", sPath );

                try
                {
                    _file = (FileEntity) query.getSingleResult();
                }
                catch( NoResultException exc )
                {
                    // Nothing to do
                }
            }
        }
        
        return _file;
    }
    
    //------------------------------------------------------------------------//
    // PRIVATE SCOPE
 
    // Creates a new entry: either a directory or a file
    private FileDescriptor createEntry( String sSessionId, int nParentId, String sChild, boolean bIsDir )
    {
        FileDescriptor   file     = null;
        String sAccount = sessionManagerBean.getUserAccount( sSessionId );
        
        if( sAccount != null && isOwnerOfFile( sAccount, nParentId ) && isValidName( sChild ) )
        {
            FileEntity _feParent = getFileEntityById( nParentId );
            
            if( (_feParent.getFileEntityPK().getIsDir() != 0) && 
                (! exists( nParentId, sChild, bIsDir )) )
            {
                try
                {
                    this.em.getTransaction().begin();

                    FileEntityPK _fepk = new FileEntityPK();    // TODO: no estoy seguro que esto sirva
                                 _fepk.setIdParent( _feParent.getIdFile() );
                                 _fepk.setIsDir( (short) (bIsDir ? 1 : 0) );
                                 _fepk.setName( sChild );
                                 
                    FileEntity _file = new FileEntity();
                               _file.setAccount( sAccount );
                               _file.setFileEntityPK( _fepk );
                               _file.setIsAlterable(  (short) 1 );
                               _file.setIsDeleteable( (short) 1 );
                               _file.setIsDuplicable( (short) 1 );
                               _file.setIsExecutable( (short) 0 );
                               _file.setIsHidden(     (short) 0 );
                               _file.setIsInTrashcan( (short) 0 );
                               _file.setIsLocked(     (short) 0 );
                               _file.setIsModifiable( (short) 1 );
                               _file.setIsPublic(     (short) 0 );
                               _file.setIsSystem(     (short) 0 );
                               _file.setCreated(  new Date() );
                               _file.setAccessed( new Date() );
                               _file.setModified( new Date() );

                    this.em.persist( _file );
                    
                    if( bIsDir )  // If file -> save full path from root
                        _file.setFullPath( _feParent.getFullPath() + sChild + "/" );
                    else          // Files exist in FILES table and in host FS, dirs only in FILES
                        FileSystemTools.createFile( sAccount,
                                                    Integer.toString( _file.getIdFile() ) );
                    
                    // If code arrives to this point, everything was OK: now can commit
                    this.em.getTransaction().commit();
                    
                    file = new FileDescriptor( _file );
                }
                catch( Exception exc )
                {
                    Constant.getLogger().throwing( getClass().getName(), "create(...)", exc );
                }

                finally
                {
                    if( this.em.getTransaction().isActive() )
                        this.em.getTransaction().rollback();
                }
            }
        }
        
        return file;
    }
    
    // Recursively deletes a file or a directory in DB (not in FS)
    private boolean _delete( String sAccount, FileEntity _file )
    {
        boolean bSuccess = false;
        
        if( _file.getAccount().equals( sAccount ) )   // Is sAccount the owner of the file?
        {
            try
            {
                if( (_file != null) && (_file.getFileEntityPK().getIsDir() != 0) )
                {
                    // Gets all childs (those which parent is _file)
                    Query query = this.em.createNamedQuery( "FileEntity.findByIdParent" );
                          query.setParameter( "idParent", _file.getIdFile() );
                          
                    List<FileEntity> _files = (List<FileEntity>) query.getResultList();
                    
                    for( FileEntity _f : _files )
                        _delete( sAccount, _f );

                    // Deletes table row (directories only exist in table FILES not in FS)
                    this.em.remove( _file );
                }
                else
                {
                    this.em.remove( _file );
                    bSuccess = FileSystemTools.deleteFile( sAccount, _file.getIdFile() );
                }
            }
            catch( Exception exc )
            {
                // Nothing to do
            }
        }
        
        return bSuccess;
    }
    
    // Recursively moves files and directories from and to the trashcan
    private boolean _trashCan( String sAccount, FileEntity _file, boolean bInTrashCan )
    {
        boolean bSuccess = true;
        
        if( _file.getAccount().equals( sAccount ) )   // Is sAccount the owner of the file? 
        {
            if( _file.getFileEntityPK().getIsDir() != 0 )   // Is a directory
            {
                // Gets all childs (those which parent is _file)
                Query query = this.em.createNamedQuery( "FileEntity.findByIdParent" );
                      query.setParameter( "idParent", _file.getIdFile() );

                List<FileEntity> _files = (List<FileEntity>) query.getResultList();
                
                for( FileEntity _f : _files )
                    _trashCan( sAccount, _f, bInTrashCan );
            }
            else                                            // Is a file
            {
                _file.setIsInTrashcan( (short) (bInTrashCan ? 1 : 0) );
                this.em.persist( _file );
            }
        }
        
        return bSuccess;
    }
    
    // Checks if a file already exists
    private boolean exists( int nParentId, String sChild, boolean bIsDir )
    {
        boolean bExists = true;        
        Query   query   = this.em.createQuery( "SELECT a FROM ApplicationEntity a"+
                                               " WHERE a.id_parent = "+ nParentId +
                                               "   AND a.name = '"+ sChild +"'"+
                                               "   AND a.is_dir ="+ (bIsDir ? 1 : 0) );
        try
        {
            query.getSingleResult();
        }
        catch( NoResultException exc )
        {
            bExists = false;
        }
        
        return bExists;
    }
    
    // Checks that the user is the owner of the file (a security measure)
    private boolean isOwnerOfFile( String sAccount, int nIdFile )
    {
        boolean bIsOwner = true;
        
        try
        {
            Query query = this.em.createQuery( "SELECT f FROM FileEntity f "+
                                               " WHERE f.idFile = "+ nIdFile +
                                               "   AND f.account ='"+ sAccount +"'" );
                  query.getSingleResult();
        }
        catch( NoResultException exc )    // Impossible to retun more than one result
        {
            bIsOwner = false;
        }
        
        return bIsOwner;
    }
    
    // Returns an instance of FileEntity based on an File ID
    private FileEntity getFileEntityById( int nFileId )
    {
        FileEntity _file = null;
        
        try
        {
            Query query = this.em.createNamedQuery( "FileEntity.findByIdFile" );
                  query.setParameter( "idFile", nFileId );
                  
            _file = (FileEntity) query.getSingleResult();
        }
        catch( Exception exc )
        {
            // Nothing to do
        }
        
        return _file;
    }
    
    // Is this a valid name for a file or directory?
    private boolean isValidName( String sName )
    {
        if( sName == null )
            return false;
        
        if( sName.length() > 255 )
            return false;
        
        if( sName.indexOf( '/' ) > -1 )   // Can't contain path separator
            return false;
        
        if( sName.charAt( 0 ) == ' ' )    // can't start with space
            return false;
        
        if( sName.charAt( sName.length() - 1 ) == ' ' )   // can't end with space
            return false;
        
        return true;   // TODO: terminarlo (decidir qué chars NO se admiten)
    }
}