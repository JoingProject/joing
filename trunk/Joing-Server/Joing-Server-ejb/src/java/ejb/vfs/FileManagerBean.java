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

import ejb.Constant;
import ejb.session.SessionManagerLocal;
import ejb.user.UserManagerLocal;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.swing.UIManager;
import org.joing.common.dto.user.User;
import org.joing.common.dto.vfs.FileDescriptor;
import org.joing.common.exception.JoingServerException;
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
       implements FileManagerLocal, Serializable 
{
    private static final long serialVersionUID = 1L;    // TODO: cambiarlo usando: serialver -show
    
    @PersistenceContext
    private EntityManager em;
    
    @EJB
    private SessionManagerLocal sessionManagerBean;
    
    @EJB
    private UserManagerLocal userManagerBean;
    
    //------------------------------------------------------------------------//
    
    public FileDescriptor getFileDescriptor( String sSessionId, String sFilePath, boolean bCreateIfNotExists )
    {
        FileDescriptor fdRet    = null;
        String         sAccount = sessionManagerBean.getUserAccount( sSessionId );
        
        if( sAccount != null )
        {
            FileEntity _file = VFSTools.path2File( em, sAccount, sFilePath );
            
            // It is not needed to check if isFileInUserSpace(...) because 
            // VFSTools.path2File(...) returns not null only if the _file is in user space.
            
            if( _file != null )
            {
                fdRet = (new FileDTOs( _file )).createFileDescriptor();
            }
            else if( bCreateIfNotExists )
            {
                int    nIndex    = sFilePath.lastIndexOf( '/' );
                String sParent   = ((nIndex == -1) ? "/" : sFilePath.substring( 0, nIndex - 1 ) );
                String sFileName = ((nIndex >= sFilePath.length() - 1) ? getGeneratedName( sAccount, sParent, false ) :
                                                                         sFilePath.substring( nIndex + 1 ));
                mkDirs( sAccount, sParent );
                fdRet = createEntry( sAccount, sParent, sFileName, false );
            }
        }
        
        return fdRet;
    }
    
    public FileDescriptor createDirectories( String sSessionId, String sPath )
    {
        FileDescriptor fdRet    = null;
        String         sAccount = sessionManagerBean.getUserAccount( sSessionId );
        
        if( sAccount != null )
        {
            fdRet = mkDirs( sAccount, sPath );
        }
        
        return fdRet;
    }
    
    public FileDescriptor createDirectory( String sSessionId, String sParent, String sDir )
           throws JoingServerVFSException
    {
        FileDescriptor fdRet    = null;
        String         sAccount = sessionManagerBean.getUserAccount( sSessionId );
        
        if( sAccount != null )
        {
            if( sDir == null )
                sDir = getGeneratedName( sAccount, sParent, true );
            
            fdRet = createEntry( sAccount, sParent, sDir, true );
        }
        
        return fdRet;
    }
    
    public FileDescriptor createFile( String sSessionId, String sParent, String sFileName, boolean bCreateParentDirs )
           throws JoingServerVFSException
    {
        String sAccount = sessionManagerBean.getUserAccount( sSessionId );
        
        if( sAccount != null )
        {
            if( bCreateParentDirs )
                mkDirs( sAccount, sParent );
            
            if( sFileName == null )
                sFileName = getGeneratedName( sAccount, sParent, false );
        
            return createEntry( sAccount, sParent, sFileName, false );
        }
        
        return null;
    }
    
    public InputStream readFile( String sSessionId, int nFileId )
           throws JoingServerVFSException
    {
        InputStream is       = null;
        String      sAccount = sessionManagerBean.getUserAccount( sSessionId );
        
        if( sAccount != null )
        {
            try
            {
                FileEntity _file = em.find( FileEntity.class, nFileId );
                
                if( _file == null )
                    throw new JoingServerVFSException( JoingServerVFSException.FILE_NOT_EXISTS );
                
                if( ! isFileInUserSpace( sAccount, _file ) )
                    throw new JoingServerVFSException( JoingServerVFSException.FILE_NOT_EXISTS );

                if( ! _file.getOwner().equals( sAccount ) )
                    throw new JoingServerVFSException( JoingServerVFSException.INVALID_OWNER );
                
                if( _file.getIsReadable() == 0)
                    throw new JoingServerVFSException( JoingServerVFSException.NOT_READABLE );
                    
                _file.setAccessed( new Date() );
                em.persist( _file );
                
                java.io.File fNative = NativeFileSystemTools.getFile( sAccount, nFileId );
                is = new FileInputStream( fNative );
            }
            catch( RuntimeException exc )
            {
                if( ! (exc instanceof JoingServerException) )
                {
                    Constant.getLogger().throwing( getClass().getName(), "readFile(...)", exc );
                    exc = new JoingServerVFSException( JoingServerException.ACCESS_DB, exc );
                }
                
                throw exc;
            }
            catch( IOException exc )
            {
                Constant.getLogger().throwing( getClass().getName(), "readFile(...)", exc );
                throw new JoingServerVFSException( JoingServerException.ACCESS_NFS, exc );
            }
        }
        
        return is;
    }
    
    public FileDescriptor writeFile( String sSessionId, int nFileId, InputStream reader )
           throws JoingServerVFSException
    {
        FileDescriptor fileDesc = null;
        String         sAccount = sessionManagerBean.getUserAccount( sSessionId );
        
        if( sAccount != null )
        {
            try
            {
                FileEntity   _file   = em.find( FileEntity.class, nFileId );
                java.io.File fNative = NativeFileSystemTools.getFile( sAccount, nFileId );
                
                if( _file == null )
                    throw new JoingServerVFSException( JoingServerVFSException.FILE_NOT_EXISTS );
                
                if( ! isFileInUserSpace( sAccount, _file ) )
                    throw new JoingServerVFSException( JoingServerVFSException.FILE_NOT_EXISTS );

                if( ! _file.getOwner().equals( sAccount ) )
                    throw new JoingServerVFSException( JoingServerVFSException.INVALID_OWNER );
            
                if( _file.getIsModifiable() == 0 )
                    throw new JoingServerVFSException( JoingServerVFSException.NOT_MODIFIABLE );

                if( _file.getLockedBy() != null &&  ! _file.getLockedBy().equals( sAccount ) )
                    throw new JoingServerVFSException( JoingServerVFSException.LOCKED_BY_ANOTHER );
                
                if( ! hasQuota( sSessionId, fNative.length() ) )
                    throw new JoingServerVFSException( JoingServerVFSException.NO_QUOTA );
            
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
                
                _file.setAccessed( new Date() );
                _file.setModified( new Date() );
                em.persist( _file );
                fileDesc = (new FileDTOs( _file )).createFileDescriptor();
                fileDesc.setSize( NativeFileSystemTools.getFileSize( sAccount, nFileId ) );
            }
            catch( RuntimeException exc )
            {
                if( ! (exc instanceof JoingServerException) )
                {
                    Constant.getLogger().throwing( getClass().getName(), "writeFile(...)", exc );
                    exc = new JoingServerVFSException( JoingServerException.ACCESS_DB, exc );
                }
                
                throw exc;
            }
            catch( IOException exc )
            {
                Constant.getLogger().throwing( getClass().getName(), "writeFile(...)", exc );
                throw new JoingServerVFSException( JoingServerException.ACCESS_NFS, exc );
            }
        }
        
        return fileDesc;
    }
    
    // This method does not need to implement the logic to handle (validate) _file 
    // attributes: the logic (validation) is done by class FileDescriptor.
    // In this way, there is no need to send _file attributes from Client to the 
    // Server in order to be validated.
    public FileDescriptor updateFileDescriptor( String sSessionId, FileDescriptor fileIn )
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
                
                if( ! fileIn.getOwner().equals( sAccount ) )    // Only owner can modify
                    throw new JoingServerVFSException( JoingServerVFSException.INVALID_OWNER );
                
                // TODO: Revisar este metodo
                                
                // Get the original _file name: this is the only attribute that has 
                // to be checked: the rest of them are done in FileDescriptor class,
                // because (obviously) the name can't be cheked at Client side.
                FileEntity _file = em.find( FileEntity.class, fileIn.getId() );
                
                // Attribute name is changed
                String sNameNew = fileIn.getName();
                String sNameOld = _file.getFileName();
                
                if( (! sNameOld.equals( sNameNew )) && (sNameNew != null) )
                {
                    String sNameErrors = VFSTools.validateName( sNameNew );
                    
                    if( sNameErrors.length() > 0 )
                        throw new JoingServerVFSException( sNameErrors );

                    if( VFSTools.existsName( em, sAccount, _file.getFilePath(), sNameNew ) )
                        throw new JoingServerVFSException( JoingServerVFSException.FILE_NAME_EXISTS );
                    
                    // If _file is a directory, all its childs have also to be renamed.
                    // And have to be renamed before their parent changes its name.
                    if( _file.getIsDir() == 1 )
                    {
                        List<FileEntity> lstChilds = VFSTools.getChilds( em, sAccount, _file );
                        renamePath( sAccount, lstChilds, sNameOld, sNameNew );
                    }
                    
                    // Now we can change parent's name.
                    _file.setFileName( sNameNew );
                    _file.setAccessed( new Date() );  // Modified flag changes only when contents are modified, not when name is modified
                    em.persist( _file );
                    fileOut = (new FileDTOs( _file )).createFileDescriptor();
                }
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
    
    public List<FileDescriptor> copy( String sSessionId, int nFileId, int nToDirId )
           throws JoingServerVFSException
    {
        throw new JoingServerVFSException( "Operation not yet implemented" );
        
        /* TODO: Hacerlo
        boolean bSuccess = false;
        
        // Modificar el ACCESSED en la tabla
        
        return bSuccess;*/
    }
    
    public List<FileDescriptor> move( String sSessionId, int nFileId, int nToDirId )
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
            
            if( _file.isDeleteable() )
            {
                bSuccess = copy( sSessionId, _file, toDir );

                if( bSuccess )
                {
                    //_delete( sAccount, _file );
                    // Modificar el ACCESSED en la tabla
                }
            }
            
        }
        
        return bSuccess;*/
    }
    
    public List<FileDescriptor> trashcan( String sSessionId, int[] anFileId, boolean bInTrashCan )
            throws JoingServerVFSException
    {
        String                    sAccount   = sessionManagerBean.getUserAccount( sSessionId );
        ArrayList<FileDescriptor> fileErrors = new ArrayList<FileDescriptor>();
        
        if( sAccount != null )
        {
            for( int n = 0; n <= anFileId.length; n++ )
                _trashCan( sAccount, anFileId[n], bInTrashCan, fileErrors );
        }
        
        return fileErrors;
    }
    
    public List<FileDescriptor> trashcan( String sSessionId, int nFileId, boolean bInTrashCan )
            throws JoingServerVFSException
    {
        String sAccount = sessionManagerBean.getUserAccount( sSessionId );
        ArrayList<FileDescriptor> fileErrors = new ArrayList<FileDescriptor>();
        
        if( sAccount != null )
        {
            _trashCan( sAccount, nFileId, bInTrashCan, fileErrors );
        }
        
        return fileErrors;
    }
    
    public List<FileDescriptor> delete( String sSessionId, int[] anFileId )
            throws JoingServerVFSException
    {
        String sAccount = sessionManagerBean.getUserAccount( sSessionId );
        ArrayList<FileDescriptor> fileErrors = new ArrayList<FileDescriptor>();
        
        if( sAccount != null )
        {
            for( int n = 0; n <= anFileId.length; n++ )
                _delete( sAccount, anFileId[n], fileErrors );
        }
        
        return fileErrors;
    }
    
    public List<FileDescriptor> delete( String sSessionId, int nFileId )
            throws JoingServerVFSException
    {
        String sAccount = sessionManagerBean.getUserAccount( sSessionId );
        ArrayList<FileDescriptor> fileErrors = new ArrayList<FileDescriptor>();
        
        if( sAccount != null )
        {
            _delete( sAccount, nFileId, fileErrors );
        }
        
        return fileErrors;
    }
    
    //------------------------------------------------------------------------//
    // LOCAL INTERFACE
    
    public FileEntity createRootEntity( String sAccount )
    {
        Date dNow = new Date();
        
        // All fields must be set
        FileEntity _file = new FileEntity();
                   _file.setIdOriginal( null );
                   _file.setAccount( sAccount );    // Can't be null
                   _file.setFilePath( "" );         // Can't be null
                   _file.setFileName( "/" );
                   _file.setOwner( Constant.getSystemAccount() );   // It's guaranted that will never be Account.equals( SystemName ). (see: sessionManagerBean.isAccountAvailable)
                   _file.setLockedBy( null );
                   _file.setIsDir(        (short) 1 );
                   _file.setIsHidden(     (short) 0 );
                   _file.setIsPublic(     (short) 0 );
                   _file.setIsReadable(   (short) 1 );
                   _file.setIsModifiable( (short) 0 );
                   _file.setIsDeleteable( (short) 0 );
                   _file.setIsExecutable( (short) 0 );
                   _file.setIsDuplicable( (short) 0 );
                   _file.setIsAlterable(  (short) 0 );
                   _file.setIsInTrashcan( (short) 0 );
                   _file.setCreated(  dNow );
                   _file.setModified( dNow );
                   _file.setAccessed( dNow );
                   
       return _file;
    }
    
    public void createExamples( String sAccount )
    {
        createEntry( sAccount, "/", "Examples", true );
        
        try
        {
            FileDescriptor fdWelcome = createEntry( sAccount, "/Examples", "welcome.txt"   , false );
            java.io.File   fWelcome  = NativeFileSystemTools.getFile( sAccount, fdWelcome.getId() );
            FileWriter writer = new FileWriter( fWelcome );
                       writer.write( "Welcome to Join'g.\n\nThis is just a text file example.\nAmong other things, you can modify it and save back to server.");
                       writer.close();
        }
        catch( IOException exc )
        {
            // Nothing to do
        }
        
        try
        {
            FileDescriptor fdNasaPict = createEntry( sAccount, "/Examples", "HomeByNASA.jpg", false );
            java.io.File   fNasaPict  = NativeFileSystemTools.getFile( sAccount, fdNasaPict.getId() );
            
            FileInputStream  fis = new FileInputStream( getClass().getResource( "homebynasa.jpg" ).getFile() );
            FileOutputStream fos = new FileOutputStream( fNasaPict );
            int nByte = 0;
            
            while( (nByte = fis.read()) != -1 )   // NEXT: Really slow: use a buffer
                fos.write( nByte );

            fos.close();
            fis.close();
        }
        catch( IOException exc )
        {
            // Nothing to do
        }
    }
    
    //------------------------------------------------------------------------//
    // PRIVATE SCOPE
    
    // Recursively moves files and directories from and to the trashcan
    // If the _file is found it can always been moved to and from trashcan.
    private void _trashCan( String sAccount, int nFileId, boolean bInTrashCan, ArrayList<FileDescriptor> fileErrors )
            throws JoingServerVFSException
    {        
        try
        {
            FileEntity _file = em.find( FileEntity.class, nFileId );
            
            if( _file == null )
                throw new JoingServerVFSException( JoingServerVFSException.FILE_NOT_EXISTS );
            
            if( ! isFileInUserSpace( sAccount, _file ) )
                throw new JoingServerVFSException( JoingServerVFSException.FILE_NOT_EXISTS );

            if( ! _file.getOwner().equals( sAccount ) )    // Only owner can modify
                throw new JoingServerVFSException( JoingServerVFSException.INVALID_OWNER );            
                
            if( _file.getIsDir() != 0 )   // Is a directory: get all _file's childs
            {
                List<FileEntity> _fChilds = VFSTools.getChilds( em, sAccount, _file );
                
                for( FileEntity _f : _fChilds )
                    _trashCan( sAccount, _f.getIdFile(), bInTrashCan, fileErrors );
            }
            else                          // Is a _file
            {
                try
                {
                    _file.setIsInTrashcan( (short) (bInTrashCan ? 1 : 0) );
                    em.persist( _file );
                }
                catch( Exception exc )
                {
                    fileErrors.add( (new FileDTOs( _file )).createFileDescriptor() );
                }
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
    }
 
    // Recursively deletes a _file or a directory in DB (not in FS)
    // Note: This method returns an ArrayList instead of int[] to increase overall speed.
    private void _delete( String sAccount, int nFileId,  ArrayList<FileDescriptor> fileErrors )
            throws JoingServerVFSException
    {
       try
        {
            FileEntity _file = em.find( FileEntity.class, nFileId );
            
            if( _file == null )
                throw new JoingServerVFSException( JoingServerVFSException.FILE_NOT_EXISTS );
            
            if( ! isFileInUserSpace( sAccount, _file ) )
                throw new JoingServerVFSException( JoingServerVFSException.FILE_NOT_EXISTS );

            if( ! _file.getOwner().equals( sAccount ) )    // Only owner can modify
                throw new JoingServerVFSException( JoingServerVFSException.INVALID_OWNER );            
                
            if( _file.getIsDeleteable() == 0 )   // Marked as not deleteable (to delete: change the attribute and try again)
                throw new JoingServerVFSException( JoingServerVFSException.NOT_DELETEABLE );

            if( _file.getIsDir() != 0 )  // Is a directory: get all _file's childs
            {
                List<FileEntity> _fChilds = VFSTools.getChilds( em, sAccount, _file );
                
                for( FileEntity _f : _fChilds )
                    _delete( sAccount, _f.getIdFile(), fileErrors );
                
                // Si borrase el _file y algunos fics no se pudieron borrar, se quedarían en el limbo para siempre
                if( fileErrors.size() == 0 )
                    em.remove( _file );   // Deletes table row (directories only exist in table FILES not in FS)
            }
            else
            {
                try
                {
                    em.remove( _file );
                    if( ! NativeFileSystemTools.deleteFile( sAccount, _file.getIdFile() ) )
                        fileErrors.add( (new FileDTOs( _file )).createFileDescriptor() );
                }
                catch( Exception exc )
                {
                    fileErrors.add( (new FileDTOs( _file )).createFileDescriptor() );
                }
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
    }
    
    // Creates a new entry: either a directory or a _file
    private FileDescriptor createEntry( String sAccount, String sParent, String sChild, boolean bIsDir )
            throws JoingServerVFSException
    {
        FileDescriptor fileDesc  = null;
        FileEntity     _feParent = VFSTools.path2File( em, sAccount, sParent );

        if( _feParent == null )
            throw new JoingServerVFSException( JoingServerVFSException.PARENT_DIR_NOT_EXISTS );

        if( _feParent.getIsDir() == 0 )
            throw new JoingServerVFSException( JoingServerVFSException.INVALID_PARENT );

        // It is not needed to check if isFileInUserSpace(...) because 
        // VFSTools.path2File(...) returns not null only if the _file is in user space.

        sChild = sChild.trim();

        String sNameErrors = VFSTools.validateName( sChild );

        if( sNameErrors.length() > 0 )
            throw new JoingServerVFSException( sNameErrors );

        if( VFSTools.existsName( em, sAccount, sParent, sChild ) )
            throw new JoingServerVFSException( (_feParent.getIsDir() == 1 ? 
                                                    JoingServerVFSException.DIR_ALREADY_EXISTS :
                                                    JoingServerVFSException.FILE_ALREADY_EXISTS) );
        try
        {
            Date date = new Date();
            
            // When the EntityManager persists the entity, it first creates a new record,
            // and the record has the values defined in "DEFAULT" clause in "CREATE TABLE",
            // but after that, EntityManager overwrites these values because it copies all
            // the object field values to the table columns.
            // So, to make things more clear, I specify all object field values.
            // Another thing: if parent is not a System _file, child will inherit its 
            // attributes from parent.

            FileEntity _file = new FileEntity();
                       _file.setIdOriginal( null );
                       _file.setAccount( sAccount );
                       _file.setFilePath( VFSTools.getAbsolutePath( _feParent ) );
                       _file.setFileName( sChild );
                       _file.setOwner( sAccount );
                       _file.setLockedBy( null );
                       _file.setIsDir( (short) (bIsDir ? 1 : 0) );
                       _file.setIsHidden(     (short) 0 );
                       _file.setIsPublic(     (short) 0 );
                       _file.setIsReadable(   (short) 1 );
                       _file.setIsModifiable( (short) 1 );
                       _file.setIsDeleteable( (short) 1 );
                       _file.setIsExecutable( (short) 0 );
                       _file.setIsDuplicable( (short) 1 );
                       _file.setIsAlterable(  (short) 1 );
                       _file.setIsInTrashcan( (short) 0 );
                       _file.setCreated(  date );
                       _file.setModified( date );
                       _file.setAccessed( date );
                       
            em.persist( _file );
            em.flush();
            em.refresh( _file );

            if( ! bIsDir )  // Files exist in FILES table and in host FS, dirs only in FILES table
                NativeFileSystemTools.createFile( sAccount, _file.getIdFile() );

            fileDesc = (new FileDTOs( _file )).createFileDescriptor();
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
        
        return fileDesc;
    }
    
    // Creates all non existing directories in sPath
    // Returns last created dir
    private FileDescriptor mkDirs( String sAccount, String sPath ) throws JoingServerVFSException
    {
        FileDescriptor fd = null;
        
        if( sPath == null || sPath.length() == 0 || sPath.charAt( 0 ) != '/' )
                throw new JoingServerVFSException( JoingServerVFSException.INVALID_PARENT );
        
        if( sPath.endsWith( "/" ) )
            sPath = sPath.substring( 0, sPath.length() - 2 );   // Removes last '/'

        String[] asDir = sPath.split( "/" );

        // Checks if all previous dirs exists
        for( int n = 1; n < asDir.length; n++ )  // Starts at 1 because asDir[0] == "/" and root always exists
        {
            StringBuilder sbFather = new StringBuilder( 256 );

            // Constructs parent name until n position
            for( int x = 0; x < n; x++ )
                sbFather.append( asDir[x] );

            // If not exists, create it
            if( ! VFSTools.existsName( em, sAccount, sbFather.toString(), asDir[n] ) )
                fd = createEntry( sAccount, sbFather.toString(), asDir[n], true );
        }
        
        return fd;
    }
    
    // Recursively rename all passed directory childs 
    private void renamePath( String sAccount, List<FileEntity> _files, String sNameOld, String sNameNew )
    {
        Date dateNew = new Date();
        
        for( FileEntity fe: _files )
        {
            if( fe.getIsDir() == 1 )  // Childs have to be renamed before parent changes its name.
            {
                List<FileEntity> childs = VFSTools.getChilds( em, sAccount, fe );   
                renamePath( sAccount, childs, sNameOld, sNameNew );
            }
            
            fe.setFilePath( fe.getFilePath().replaceFirst( sNameOld, sNameNew ) );
            fe.setAccessed( dateNew );
            em.persist( fe );
        }
    }
    
    // This is just a security meassure against people trying to explode Join'g
    // (by sending random _file Ids)
    private boolean isFileInUserSpace( String sAccount, FileEntity _file )
    {
        if( _file.getAccount().equals( sAccount ) )            // Account got from SessionId and Account from FileEntity match.
        {
            FileEntity _fReal = em.find( FileEntity.class, _file.getIdFile() );  // Get the real FileEntity from DB that corresponds to passed _file (by Id)
        
            return _fReal.getAccount().equals( sAccount );     // Compare both accounts  (Note: sAccount was already compared 2 lines above)
        }
        
        return false;
    }
    
    // This is just a security meassure against people trying to explode Join'g
    // (by sending random FileDescriptors)
    private boolean isFileInUserSpace( String sAccount, FileDescriptor fd )
    {
        if( fd.getAccount() != null && fd.getAccount().equals( sAccount ) )   // Account got from SessionId and Account from FileDescriptor match.
        {
            FileEntity _fReal = em.find( FileEntity.class, fd.getId() );      // Get the real FileEntity from DB that corresponds to passed _file (by Id)
        
            return _fReal.getAccount().equals( sAccount );  // Compare both accounts  (Note: sAccount was already compared 2 lines above)
        }
        
        return false;
    }
    
    private String getGeneratedName( String sAccount, String sParent, boolean bForDir )
    {
        String sName = null;
        
        // TODO: hacerlo mejor: usando un contador y buscando nombres existentes
        if( bForDir )
        {
            sName = UIManager.getString( "FileChooser.other.newFolder" ) +"_"+ System.currentTimeMillis();
        }
        else
        {
            sName = "New file_"+ System.currentTimeMillis();
        }
        
        return sName;
    }
    
    private boolean hasQuota( String sSesionId, long nSize )
    {
        User user = userManagerBean.getUser( sSesionId );
        
        return (user.getTotalSpace() - user.getUsedSpace() - nSize >= 0);
    }
}