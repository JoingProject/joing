/*
 * UserManagerBean.java
 *
 * Created on 3 de junio de 2007, 18:20
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

package ejb.user;

import ejb.Constant;
import ejb.session.*;
import ejb.vfs.FileEntity;
import ejb.vfs.FileEntityPK;
import ejb.vfs.FileSystemTools;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 * This class is charge of all methods related with users management.
 * <p>
 * For the API javadoc, refer to the 'remote' and 'local' interfaces.
 *
 * @author Francisco Morero Peyrona
 */
@Stateless
public class UserManagerBean
    implements UserManagerRemote, UserManagerLocal, Serializable  // TODO: implementar el serializable
{
    @PersistenceContext
    private EntityManager em;
    
    @EJB
    private SessionManagerLocal sessionManagerBean;
    
    //------------------------------------------------------------------------//
    // REMOTE INTERFACE
    
    public User getUser( String sSessionId )
    {
        User   user     = null;
        String sAccount = sessionManagerBean.getUserAccount( sSessionId );
        
        if( sAccount != null )
        {
            UserEntity _user = this.em.find( UserEntity.class, sAccount );
            
            if( _user != null )
            {
                user = new User( _user );
                // Injected (for more info, refer to these methods in User class)
                user.setUsedSpace( FileSystemTools.getUsedSpace( sAccount ) );
                user.setFreeSpace( FileSystemTools.getDiskFreeSpace() );
            }
        }
                
        return user;
    }
    
    public void updateUser( String sSessionId, User user )
    {
        String  sAccount = sessionManagerBean.getUserAccount( sSessionId );
        
        if( sAccount != null && sAccount.equals( user.getAccount() ) )
        {
            // I have to do em.find(...), because if I would make: 
            // UserEntity _user = new UserEntity();
            // the password field would be sat to null at persist(...)
            // (password field does not exists in User class)
            UserEntity _user = em.find( UserEntity.class, user.getAccount() );
            
            user.update( _user );
            em.persist( _user );
        }
    }
    
    public List<Local> getAvailableLocales( String sSessionId )
    {
        String      sAccount = sessionManagerBean.getUserAccount( sSessionId );
        List<Local> locals   = null;
        
        if( sAccount != null )
        {
            Query query = this.em.createQuery( "SELECT l FROM LocaleEntity" );

            List<LocaleEntity> _locales = (List<LocaleEntity>) query.getResultList();

            locals = new ArrayList( _locales.size() );

            for( LocaleEntity _locale : _locales )
                locals.add( new Local( _locale ) );
        }
        
        return locals;
    }
    
    //------------------------------------------------------------------------//
    // LOCAL INTERFACE
    
    public User createUser( String sAccount, String sPassword, String sEmail,
                            String sFirstName, String sSecondName, 
                            boolean bIsMale, Locale locale, int nQuota )
    {
        User user = null;
        
        // By checking here the availability, we ensure that all
        // operations will be made in same transaction
        if( isValidAccount(  sAccount  )  &&
            isValidPassword( sPassword )  &&
            sessionManagerBean.isAccountAvailable( sAccount ) )
        {
            try
            {
                this.em.getTransaction().begin();
                
                if( locale == null )
                    Locale.getDefault();
                
                // Creates user in USERS DB table
                UserEntity _user = new UserEntity();
                           _user.setAccount( sAccount );
                           _user.setEmail( sEmail );
                           _user.setFirstName( sFirstName );
                           _user.setIsMale( (short) (bIsMale ? 1 : 0) );
                           _user.setIdLocale( findIdLocale( locale ) );
                           _user.setPassword( sPassword );
                           _user.setSecondName( sSecondName );
                           _user.setQuota( nQuota );
                           
                this.em.persist( _user );
                
                // Creates root ("/") in FILES DB table
                FileEntityPK _fepk = new FileEntityPK();   // TODO: no estoy seguro que esto sirva
                           //_fepk.setIdParent( null );
                             _fepk.setIsDir( (short) 1 );
                             _fepk.setName( "/" );
                             
                FileEntity _file = new FileEntity();
                           _file.setFileEntityPK( _fepk );
                           _file.setAccount( _user.getAccount() );
                           _file.setFullPath( "/" );
                           _file.setIsAlterable(  (short) 0 );
                           _file.setIsDeleteable( (short) 0 );
                           _file.setIsDuplicable( (short) 0 );
                           _file.setIsExecutable( (short) 0 );
                           _file.setIsHidden(     (short) 0 );
                           _file.setIsInTrashcan( (short) 0 );
                           _file.setIsLocked(     (short) 0 );
                           _file.setIsModifiable( (short) 0 );
                           _file.setIsPublic(     (short) 0 );
                           _file.setIsSystem(     (short) 1 );
                           _file.setAccessed( new Date() );
                           _file.setCreated(  new Date() );
                           _file.setModified( new Date() );                       

                this.em.persist( _file );

                // Create home directory for user
                if( FileSystemTools.createAccount( _user.getAccount() ) )
                {
                    user = new User( _user );
                    
                    // When code arrives to this point, everything was OK: now can commit
                    this.em.getTransaction().commit();
                }
                else
                {
                    throw new IOException( "Can't create user home directory" );
                }
            }
            catch( Exception exc )
            {
                Constant.getLogger().severe( "Error creating user\n"+
                                             "Check that AppServer has enough priviledges." );
            }
            finally 
            {
                if( this.em.getTransaction().isActive() )
                    this.em.getTransaction().rollback();
            }
        }
        
        return user;
    }
    
    public void removeUser( User user )
    {
        try
        {
            this.em.getTransaction().begin();
     
            // Removes user from USERS table
            UserEntity _user = new UserEntity();
                        
            user.update( _user );

            this.em.remove( _user );

            // Removes all files from FILES table
            Query query = this.em.createQuery( "delete from FileEntity f where f.account = "+ user.getAccount() );
                  query.executeUpdate();

            // Removes all files from FS
            FileSystemTools.removeAccount( _user.getAccount() );
            
            this.em.getTransaction().commit();
        }
        catch( Exception exc )
        {
            Constant.getLogger().severe( "Error deleting user\n"+
                                         "Check that AppServer has enough priviledges." );
        }
        finally 
        {
            if( this.em.getTransaction().isActive() )
                this.em.getTransaction().rollback();
        }
    }
    
    //------------------------------------------------------------------------//
    
    private boolean isValidAccount( String s )
    {
        boolean bValid = (s != null) && (s.length() >= 6) && (s.length() <= 32);
        char[]  ac     = s.toCharArray();  // Unicode can be converted to char (both are 16 bits)
        
        if( bValid )
        {
            for( int n = 0; n < ac.length; n++ )
            {
                bValid = (ac[n] >= 48 && ac[n] <=  57) ||   // Is number
                         (ac[n] >= 97 && ac[n] <= 122) ||   // Is LowerCase
                          ac[n] == '-'                 || 
                          ac[n] == '_'                 || 
                          ac[n] == '.';
                
                if( ! bValid )
                    break;
            }
        }
        
        return bValid;
    }
    
    private boolean isValidPassword( String s )
    {
        return (s != null        && 
                s.length() >=  6 && 
                s.length() <= 32);
    }
    
    private LocaleEntity findIdLocale( Locale locale )
    {
        LocaleEntity _local = null;
        
        Query query = this.em.createQuery( "SELECT l FROM LocaleEntity "+
                                           " WHERE l.language = '"+ locale.getLanguage() +"'"+ 
                                           "   AND l.country ='"+ locale.getCountry() +"'" );
        try
        {
            _local = (LocaleEntity) query.getSingleResult();
        }
        catch( Exception exc )
        {
            // Nothing to do: requested locale does not exists (and it is already == 1)
        }
        
        if( _local == null )
            _local = this.em.find( LocaleEntity.class, 1 );
        
        return _local;
    }
}