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
package org.joing.server.ejb.user;

import org.joing.server.ejb.Constant;
import org.joing.common.dto.user.Local;
import org.joing.common.dto.user.User;
import org.joing.common.exception.JoingServerException;
import org.joing.common.exception.JoingServerUserException;
import org.joing.common.exception.JoingServerSessionException;
import org.joing.server.ejb.vfs.FileManagerLocal;
import org.joing.server.ejb.vfs.NativeFileSystemTools;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.joing.server.ejb.app.ApplicationManagerLocal;
import org.joing.server.ejb.session.SessionManagerLocal;

/**
 * This class is charge of all methods related with users management.
 * <p>
 * For the API javadoc, refer to the 'remote' and 'local' interfaces.
 *
 * @author Francisco Morero Peyrona
 */
@Stateless
public class UserManagerBean
    implements UserManagerLocal, Serializable
{
    private static final long serialVersionUID = 1L;    // TODO: cambiarlo usando: serialver -show
    
    @PersistenceContext
    private EntityManager em;
    
    @EJB
    private SessionManagerLocal sessionManagerBean;
 
    @EJB
    private FileManagerLocal fileManagerBean;
    
    @EJB
    private ApplicationManagerLocal appManagerBean;
    
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
                
                user = UserDTOs.createUser( _user );
                // Injected (for more info, refer to these methods in User class)
                user.setUsedSpace( NativeFileSystemTools.getUsedSpace( sAccount ) );
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
                
                UserDTOs.transfer( user, _user );
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
                Query query = em.createQuery( "SELECT l FROM LocaleEntity l" );

                List<LocaleEntity> _locales = (List<LocaleEntity>) query.getResultList();

                locals = new ArrayList<Local>( _locales.size() );
                
                for( LocaleEntity _locale : _locales )
                    locals.add( UserDTOs.createLocal( _locale ) );
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
        if( ! sAccount.matches( Constant.sREG_EXP_VALID_ACCOUNT ) )
            throw new JoingServerUserException( JoingServerUserException.INVALID_ACCOUNT );
        
        if( ! sPassword.matches( Constant.sREG_EXP_VALID_PASSWORD ) )
            throw new JoingServerUserException( JoingServerUserException.INVALID_PASSWORD );
        
        sAccount = sessionManagerBean.composeAccount( sAccount );
        
        // By checking here the availability, we ensure that all
        // operations will be made in same transaction.
        if( ! sessionManagerBean.isAccountAvailable( sAccount ) )
            throw new JoingServerUserException( JoingServerSessionException.LOGIN_EXISTS );
            
        try
        {
            LocaleEntity _local = findLocale( locale );
            
            if( _local == null )
                _local = getDefaultLocale();
            
            // Creates user in USERS DB table
            UserEntity _user = new UserEntity();
                       _user.setAccount( sAccount.trim().toLowerCase() );
                       _user.setPassword( sPassword );
                       _user.setEmail( sEmail );
                       _user.setFirstName( sFirstName );
                       _user.setSecondName( sSecondName );
                       _user.setIsMale( (short) (bIsMale ? 1 : 0) );
                       _user.setIdLocale( _local );
                       _user.setQuota( nQuota );

            em.persist( _user );

            // Creates root ("/") in FILES DB table
            fileManagerBean.createRootFor( sAccount );
            
            // Create home directory for user
            NativeFileSystemTools.createAccount( sAccount );
            
            // Even if code after this line can not be executed, the user has 
            // accomplished all pre-requisites to exists: following code will
            // make things nicer, but is not mandatory.
            user = UserDTOs.createUser( _user );
            
            // Apps Welcome Pack: Basic (free) apps that are available for all users
            appManagerBean.attachInitialAppsTo( sAccount );
            
            // Creates initial directory and its files
            fileManagerBean.createInitialFilesFor( sAccount );
            
            // Save all changes
            em.flush();
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
        
        return user;
    }
    
    public void removeUser( User user )
           throws JoingServerUserException
    {
        try
        {
            // Removes user from USERS table
            UserEntity _user = UserDTOs.createUserEntity( user );
            em.remove( _user );

            // Removes all files from FILES table
            Query query = em.createQuery( "delete from FileEntity f where f.account = "+ user.getAccount() );
                  query.executeUpdate();

            // Removes all files from FS
            if( ! NativeFileSystemTools.removeAccount( _user.getAccount() ) )
                Constant.getLogger().log( Level.SEVERE, "Can't delete user directory ("+ user.getAccount() +
                                                        "): has to be done manually." );
        }
        catch( RuntimeException exc )
        {
            Constant.getLogger().throwing( getClass().getName(), "removeUser(...)", exc );
            throw new JoingServerUserException( JoingServerException.ACCESS_DB, exc );
        }
    }
    
    //------------------------------------------------------------------------//
    
    private LocaleEntity findLocale( Locale locale )
            throws JoingServerUserException
    {
        LocaleEntity _local = null;
        
        if( locale == null )
            locale = Locale.getDefault();
        
        try
        {
            Query query = em.createQuery( "SELECT l FROM LocaleEntity l"+
                                          " WHERE l.idiom='"+ locale.getLanguage() +"'"+ 
                                          "   AND l.country='"+ locale.getCountry() +"'" );
            
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