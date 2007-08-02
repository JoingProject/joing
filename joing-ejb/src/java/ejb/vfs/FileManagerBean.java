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
import ejb.session.SessionManagerLocal;
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
    
    //------------------------------------------------------------------------//
    
    public FileDescriptor getFile( String sSessionId, String sPath )
    {
        FileDescriptor file     = null;
        String         sAccount = sessionManagerBean.getUserAccount( sSessionId );
        
        if( sAccount != null )
        {
            FileEntity _file = path2File( sAccount, sPath );
            
            if( _file != null )
                file = new FileDescriptor( _file );
        }
        
        return file;
    }
    
    public FileDescriptor updateFile( String sSessionId, FileDescriptor fileIn )
           throws JoingServerVFSException
    {
        String         sAccount = sessionManagerBean.getUserAccount( sSessionId );
        FileDescriptor fileOut  = null;
        
        if( sAccount != null )
        {
            try
            {
                if( ! isOwnerOfFile( sAccount, fileIn.getId() ) )
                    throw new JoingServerVFSException( JoingServerVFSException.INVALID_OWNER );
                
                // Get the original file
                FileEntity _file = em.find( FileEntity.class, fileIn.getId() );
                
                if( _file.getIsAlterable() == 0 )   // Attributes can't be changed
                    throw new JoingServerVFSException( JoingServerVFSException.NOT_ALTERABLE );
                
                // Attribute name is changed
                String sNameNew = fileIn.getName();
                String sNameOld = _file.getFileEntityPK().getName();
                
                if( ! sNameOld.equals( sNameNew ) )
                {
                    String sNameErrors = validateName( sNameNew );
                    
                    if( sNameErrors.length() > 0 )
                        throw new JoingServerVFSException( sNameErrors );

                    if( exists( _file.getFileEntityPK().getIdParent(), sNameNew, fileIn.isDirectory() ) )
                        throw new JoingServerVFSException( JoingServerVFSException.FILE_NAME_EXISTS );
                    
                    _file.getFileEntityPK().setName( sNameNew );
                }

                // Attribute
                // TODO: comprobar todos los permisos y actualizar el JavaDoc
                //       p.ej. si es un dir, no puede cambiar a tipo fichero, ni a tipo ejecutable, etc.

                _file.setAccessed( new Date() );  // Modified is only when modifiying contents
                em.persist( _file );
                fileOut = new FileDescriptor( _file );
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
    
    public FileDescriptor createDirectory( String sSessionId, int nParentId, String sDirName )
           throws JoingServerVFSException
    {
        return createEntry( sSessionId, nParentId, sDirName, true );
    }
    
    public FileDescriptor createFile( String sSessionId, int nParentId, String sFileName )
           throws JoingServerVFSException
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
           throws JoingServerVFSException
    {        
        FileText fileText = null;
        String   sAccount = sessionManagerBean.getUserAccount( sSessionId );
        
        if( sAccount != null )
        {
            if( ! isOwnerOfFile( sAccount, nFileId ) )
                throw new JoingServerVFSException( JoingServerVFSException.INVALID_OWNER );
            
            try
            {
                String            sNative = Integer.toString( nFileId );
                java.io.File      fNative = FileSystemTools.getFile( sAccount, sNative );
                
                FileInputStream   fis     = new FileInputStream( fNative );
                InputStreamReader isw     = new InputStreamReader( fis, sEncoding );
                BufferedReader    br      = new BufferedReader( isw );
                
                FileEntity _file = em.find( FileEntity.class, nFileId );
                           _file.setAccessed( new Date() );
                           
                em.persist( _file );
                
                fileText = new FileText( _file );
                fileText.setMimetype( null );   // TODO: averiguarlo
                fileText.setContents( br );
                
                br.close();
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
            if( ! isOwnerOfFile( sAccount, nFileId ) )
                throw new JoingServerVFSException( JoingServerVFSException.INVALID_OWNER );
            
            try
            {
                String          sNative = Integer.toString( nFileId );
                java.io.File    fNative = FileSystemTools.getFile( sAccount, sNative );
                
                FileInputStream fis     = new FileInputStream( fNative );
                FileEntity      _file   = em.find( FileEntity.class, nFileId );
                                _file.setAccessed( new Date() );
                em.persist( _file );
                
                fileBinary = new FileBinary( _file );
                fileBinary.setContents( fis );
                
                fis.close();
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
    public boolean writeTextFile( String sSessionId, FileText fileText )
           throws JoingServerVFSException
    {
        boolean bSuccess = false;
        String  sAccount = sessionManagerBean.getUserAccount( sSessionId );
        
        if( sAccount != null )
        {
            if( ! isOwnerOfFile( sAccount, fileText.getId() ) )
                throw new JoingServerVFSException( JoingServerVFSException.INVALID_OWNER );
            
            try
            {
                String             sNative  = Integer.toString( fileText.getId() );
                java.io.File       fNative  = FileSystemTools.getFile( sAccount, sNative );
                
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
                
                bSuccess = true;
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
        
        return bSuccess;
    }
    
    public boolean writeBinaryFile( String sSessionId, FileBinary fileBinary )
           throws JoingServerVFSException
    {
        boolean bSuccess = false;
        String  sAccount = sessionManagerBean.getUserAccount( sSessionId );
        
        if( sAccount != null )
        {
            if( ! isOwnerOfFile( sAccount, fileBinary.getId() ) )
                throw new JoingServerVFSException( JoingServerVFSException.INVALID_OWNER );
            
            try
            {
                String           sNative  = Integer.toString( fileBinary.getId() );
                java.io.File     fNative  = FileSystemTools.getFile( sAccount, sNative );
                
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
                
                bSuccess = true;
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
        
        return bSuccess;
    }
    
    public boolean copy( String sSessionId, int nFileId, int nToDirId )
           throws JoingServerVFSException
    {
        boolean bSuccess = false;
        
        // Modificar el ACCESSED en la tabla
        // TODO: Hacerlo
        return bSuccess;
    }
    
    public boolean move( String sSessionId, int nFileId, int nToDirId )
           throws JoingServerVFSException
    {
        boolean bSuccess = false;
        String  sAccount = sessionManagerBean.getUserAccount( sSessionId );
        
        if( sAccount != null )
        {
            if( ! isOwnerOfFile( sAccount, nFileId ) )
                throw new JoingServerVFSException( JoingServerVFSException.INVALID_OWNER );
              
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
        }
        
        return bSuccess;
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
    // PACKAGE SCOPE
    
    /**
     * Obtains the FileEntity instance that represents the file or directory
     * passed as String.
     *
     * @param sAccount An Account representing the owner of the file ro dir.
     * @param sPath Path starting at root ("/") and ending with the file or dir
     *        name.
     * @return An instance of class <code>FileEntity</code> that represents the 
     *         file or directory denoted by passed path or <code>null</code> if 
     *         the path does not corresponds with an existing file.
     * @throws JoingServerVFSException if any prerequisite was not satisfied or 
     *         a wrapped third-party exception if something went wrong.
     */
    FileEntity path2File( String sAccount, String sPath )
               throws JoingServerVFSException
    {
        FileEntity _file = null;
                           // Path must start from root
        if( sPath != null && sPath.trim().charAt( 0 ) == '/' )
        {
            sPath = sPath.trim();

            try
            {
                Query query = em.createQuery( "SELECT f FROM FileEntity f"+
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

                        Query q = em.createQuery( s );

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

                    query.setParameter( "account" , sAccount );
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
            catch( RuntimeException exc )
            {
                if( ! (exc instanceof JoingServerException) )
                {
                    Constant.getLogger().throwing( getClass().getName(), "path2File(...)", exc );
                    exc = new JoingServerVFSException( JoingServerException.ACCESS_DB, exc );
                }
            
                throw exc;
            }
        }
        
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
            
            if( ! _file.getAccount().equals( sAccount ) )   // Is sAccount the owner of the file? 
                throw new JoingServerVFSException( JoingServerVFSException.INVALID_OWNER );

            if( _file.getFileEntityPK().getIsDir() != 0 )   // Is a directory
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
            
            if( ! _file.getAccount().equals( sAccount ) )   // Is sAccount the owner of the file?
                throw new JoingServerVFSException( JoingServerVFSException.INVALID_OWNER );

            if( _file.getIsDeleteable() == 0 )   // Marked as not deleteable
                throw new JoingServerVFSException( JoingServerVFSException.NOT_DELETEABLE );

            if( _file.getFileEntityPK().getIsDir() != 0 )  // Is a directory
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
    private FileDescriptor createEntry( String sSessionId, int nParentId, String sChild, boolean bIsDir )
            throws JoingServerVFSException
    {
        FileDescriptor file     = null;
        String         sAccount = sessionManagerBean.getUserAccount( sSessionId );
        
        if( sAccount != null )
        {
            if( ! isOwnerOfFile( sAccount, nParentId ) )
                throw new JoingServerVFSException( JoingServerVFSException.INVALID_OWNER );
                
            String sNameErrors = validateName( sChild );
            
            if( sNameErrors.length() > 0 )
                throw new JoingServerVFSException( sNameErrors );
                
            FileEntity _feParent = getParentEntity( nParentId );
            
            if( _feParent == null )
                throw new JoingServerVFSException( JoingServerVFSException.PARENT_DIR_NOT_EXISTS );
            
            if( _feParent.getFileEntityPK().getIsDir() == 0 )
                throw new JoingServerVFSException( JoingServerVFSException.INVALID_PARENT );
            
            if( exists( nParentId, sChild, bIsDir ) )
                throw new JoingServerVFSException( (_feParent.getFileEntityPK().getIsDir() == 1 ? 
                                                        JoingServerVFSException.DIR_ALREADY_EXISTS :
                                                        JoingServerVFSException.FILE_ALREADY_EXISTS) );
            try
            {
                em.getTransaction().begin();

                FileEntityPK _fepk = new FileEntityPK();
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

                em.persist( _file );

                if( bIsDir )  // If file -> save full path from root
                    _file.setFullPath( _feParent.getFullPath() + sChild + "/" );
                else          // Files exist in FILES table and in host FS, dirs only in FILES
                    FileSystemTools.createFile( sAccount,
                                                Integer.toString( _file.getIdFile() ) );

                // If code arrives to this point, everything was OK: now can commit
                em.getTransaction().commit();

                file = new FileDescriptor( _file );
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
    
    // Rreturns parent Entity based on its ID, or null if it does not exists.
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
    
    // Checks if a file already exists
    private boolean exists( int nParentId, String sName, boolean bIsDir )
            throws JoingServerVFSException
    {
        boolean bExists = true;        
        Query   query   = em.createQuery( "SELECT a FROM ApplicationEntity a"+
                                          " WHERE a.id_parent = "+ nParentId +
                                          "   AND a.name = '"+ sName +"'"+
                                          "   AND a.is_dir ="+ (bIsDir ? 1 : 0) );
        try
        {
            query.getSingleResult();
        }
        catch( NoResultException exc )
        {
            bExists = false;
        }
        catch( RuntimeException exc )
        {
            Constant.getLogger().throwing( getClass().getName(), "exists(...)", exc );
            throw new JoingServerVFSException( JoingServerException.ACCESS_DB, exc );
        }
        
        return bExists;
    }
    
    // Checks that the user is the owner of the file (a security measure)
    private boolean isOwnerOfFile( String sAccount, int nIdFile ) 
            throws JoingServerVFSException
    {
        boolean bIsOwner = true;
        Query   query    = em.createQuery( "SELECT f FROM FileEntity f "+
                                           " WHERE f.idFile = "+ nIdFile +
                                           "   AND f.account ='"+ sAccount +"'" );
        try
        {
            query.getSingleResult();
        }
        catch( NoResultException exc )    // Impossible to retun more than one result
        {
            bIsOwner = false;
        }
        catch( RuntimeException exc )
        {
            Constant.getLogger().throwing( getClass().getName(), "isOwnerOfFile(...)", exc );
            throw new JoingServerVFSException( JoingServerException.ACCESS_DB, exc );    
        }
        
        return bIsOwner;
    }
    
    // Is this a valid name for a file or directory?
    private String validateName( String sName )
    {
        StringBuilder sbError = new StringBuilder();
        
        if( sName == null )
            sbError.append( "Can not be null." );
        
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
            sbError.insert( 0, "Invalid file name:\n" );
        
        return sbError.toString();
    }
}