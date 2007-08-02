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
import ejb.JoingServerException;
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
import java.util.logging.Level;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
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
    implements UserManagerRemote, UserManagerLocal, Serializable
{
    private static final long serialVersionUID = 1L;    // TODO: cambiarlo por un nº apropiado
    
    static final int nMIN_LEN =  6;   // For account and password
    static final int nMAX_LEN = 32;   // For account and password
    
    @PersistenceContext
    private EntityManager em;
    
    @EJB
    private SessionManagerLocal sessionManagerBean;
    
    //------------------------------------------------------------------------//
    // REMOTE INTERFACE
    
    public User getUser( String sSessionId ) 
           throws JoingServerUserException
    {
        User   user     = null;
        String sAccount = sessionManagerBean.getUserAccount( sSessionId );
        
        if( sAccount != null )
        {
            try
            {
                UserEntity _user = em.find( UserEntity.class, sAccount );
            
                user = new User( _user );
                // Injected (for more info, refer to these methods in User class)
                user.setUsedSpace( FileSystemTools.getUsedSpace( sAccount ) );
                user.setFreeSpace( FileSystemTools.getDiskFreeSpace() );
            }
            catch( RuntimeException exc )
            {
                Constant.getLogger().throwing( getClass().getName(), "getUser(...)", exc );
                throw new JoingServerUserException( JoingServerException.ACCESS_DB, exc );
            }
        }
        
        return user;
    }
    
    public User updateUser( String sSessionId, User user ) 
           throws JoingServerUserException
    {
        String sAccount = sessionManagerBean.getUserAccount( sSessionId );
        
        if( sAccount != null )
        {
            if( ! sAccount.equals( user.getAccount() ) )
                throw new JoingServerUserException( JoingServerUserException.NOT_ATTRIBUTES_OWNER );
            
            try
            {
                // I have to do em.find(...), because if I would make: 
                // UserEntity _user = new UserEntity();
                // the password field would be sat to null at persist(...)
                // (password field does not exists in User class)
                UserEntity _user = em.find( UserEntity.class, user.getAccount() );
                
                user.update( _user );
                em.persist( _user );
            }
            catch( RuntimeException exc )
            {
                Constant.getLogger().throwing( getClass().getName(), "updateUser(...)", exc );
                throw new JoingServerUserException( JoingServerException.ACCESS_DB, exc );
            }
        }
        
        return user;
    }
    
    public List<Local> getAvailableLocales( String sSessionId ) 
           throws JoingServerUserException
    {
        String      sAccount = sessionManagerBean.getUserAccount( sSessionId );
        List<Local> locals   = null;
        
        if( sAccount != null )
        {
            try
            {
                Query query = em.createQuery( "SELECT l FROM LocaleEntity" );

                List<LocaleEntity> _locales = (List<LocaleEntity>) query.getResultList();

                locals = new ArrayList( _locales.size() );
                
                for( LocaleEntity _locale : _locales )
                    locals.add( new Local( _locale ) );
            }
            catch( RuntimeException exc )
            {
                Constant.getLogger().throwing( getClass().getName(), "getAvailableLocales(...)", exc );
                throw new JoingServerUserException( JoingServerException.ACCESS_DB, exc );
            }
        }
        
        return locals;
    }
    
    //------------------------------------------------------------------------//
    // LOCAL INTERFACE
    
    public User createUser( String sAccount, String sPassword, String sEmail,
                            String sFirstName, String sSecondName, 
                            boolean bIsMale, Locale locale, int nQuota )
           throws JoingServerUserException
    {
        User user = null;
        
        if( ! isValidAccount( sAccount ) )
            new JoingServerUserException( JoingServerUserException.INVALID_ACCOUNT );
        
        if( ! isValidPassword( sPassword ) )
            new JoingServerUserException( JoingServerUserException.INVALID_PASSWORD );
        
        // By checking here the availability, we ensure that all
        // operations will be made in same transaction.
        if( ! sessionManagerBean.isAccountAvailable( sAccount ) )
            new JoingServerUserException( JoingServerSessionException.LOGIN_EXISTS );
            
        try
        {
            em.getTransaction().begin();

            LocaleEntity _local = findLocale( locale );
            
            if( _local == null )
                _local = getDefaultLocale();
            
            // Creates user in USERS DB table
            UserEntity _user = new UserEntity();
                       _user.setAccount( sAccount );
                       _user.setEmail( sEmail );
                       _user.setFirstName( sFirstName );
                       _user.setIsMale( (short) (bIsMale ? 1 : 0) );
                       _user.setIdLocale( _local );
                       _user.setPassword( sPassword );
                       _user.setSecondName( sSecondName );
                       _user.setQuota( nQuota );

            em.persist( _user );

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
                       _file.setCreated(  _file.getAccessed() );
                       _file.setModified( _file.getAccessed() );                       

            em.persist( _file );

            // Create home directory for user
            FileSystemTools.createAccount( _user.getAccount() );

            user = new User( _user );

            // When code arrives to this point, everything was OK: now can commit
            em.getTransaction().commit();
        }
        catch( RuntimeException exc )
        {
            Constant.getLogger().throwing( getClass().getName(), "createUser(...)", exc );
            throw new JoingServerUserException( JoingServerException.ACCESS_DB, exc );
        }
        catch( IOException exc )
        {
            Constant.getLogger().log( Level.SEVERE, "Error creating user. Check that AppServer has enough priviledges." );
            Constant.getLogger().throwing( getClass().getName(), "createUser(...)", exc );
            throw new JoingServerUserException( JoingServerException.ACCESS_NFS );
        }

        finally 
        {
            if( em.getTransaction().isActive() )
                em.getTransaction().rollback();
        }
        
        return user;
    }
    
    public void removeUser( User user )
           throws JoingServerUserException
    {
        try
        {
            em.getTransaction().begin();
     
            // Removes user from USERS table
            UserEntity _user = new UserEntity();
                        
            user.update( _user );

            em.remove( _user );

            // Removes all files from FILES table
            Query query = em.createQuery( "delete from FileEntity f where f.account = "+ user.getAccount() );
                  query.executeUpdate();

            // Removes all files from FS
            if( ! FileSystemTools.removeAccount( _user.getAccount() ) )
                Constant.getLogger().log( Level.SEVERE, "Can't delete user directory ("+ user.getAccount() +
                                                        "): has to be done manually." );
            
            em.getTransaction().commit();
        }
        catch( RuntimeException exc )
        {
            Constant.getLogger().throwing( getClass().getName(), "removeUser(...)", exc );
            throw new JoingServerUserException( JoingServerException.ACCESS_DB, exc );
        }
        finally 
        {
            if( em.getTransaction().isActive() )
                em.getTransaction().rollback();
        }
    }
    
    static String getAccountRestrictions()
    {
        return "Invalid account (user name). It has to follow these rules:"+ 
               "\n   * Minimum length = "+ nMIN_LEN +
               "\n   * Maximum length = "+ nMAX_LEN +
               "\n   * Numbers and lowercase letters"+
               "\n   * Following characters: '.' '-' '_'";
    }
    
    static String getPasswordRestrictions()
    {
        return "Invalid password length, minimum = "+ 
               nMIN_LEN +" and maximum = "+ nMAX_LEN + 
               "characters.";
    }
    
    //------------------------------------------------------------------------//
    
    private boolean isValidAccount( String s )
    {
        boolean bValid = false;
        
        if( (s != null)              && 
            (s.length() >= nMIN_LEN) && 
            (s.length() <= nMAX_LEN) )
        {
            char[] ac = s.toCharArray();  // Unicode can be converted to char (both are 16 bits)
            
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
        return (s != null              && 
                s.length() >= nMIN_LEN && 
                s.length() <= nMAX_LEN );
    }
    
    private LocaleEntity findLocale( Locale locale )
            throws JoingServerUserException
    {
        LocaleEntity _local = null;
        
        if( locale == null )
            Locale.getDefault();
        
        try
        {
            Query query = em.createQuery( "SELECT l FROM LocaleEntity "+
                                      " WHERE l.language = '"+ locale.getLanguage() +"'"+ 
                                      "   AND l.country ='"+ locale.getCountry() +"'" );
            
            _local = (LocaleEntity) query.getSingleResult();
        }
        catch( NoResultException exc )
        {
            // Nothing to do: requested locale does not exists (and it is already == null)
        }
        catch( RuntimeException exc )
        {
            Constant.getLogger().throwing( getClass().getName(), "findLocale(...)", exc );
            throw new JoingServerUserException( JoingServerException.ACCESS_DB, exc );
        }
        
        return _local;
    }
    
    // The locale returned when the requested one does not exists
    private LocaleEntity getDefaultLocale()
            throws JoingServerUserException
    {
        LocaleEntity _local = findLocale( Locale.US );
        
        if( _local == null )
            Constant.getLogger().log( Level.SEVERE, "Table with Locales is wmpty: can't work." );
        
        return _local;
    }
}