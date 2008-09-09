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
package org.joing.server.ejb.session;

import org.joing.server.ejb.Constant;
import org.joing.server.ejb.user.UserEntity;
import org.joing.common.dto.session.LoginResult;
import org.joing.common.exception.JoingServerException;
import org.joing.common.exception.JoingServerSessionException;
import java.io.Serializable;
import java.util.Date;
import java.util.Random;
import javax.ejb.Stateless;
import javax.persistence.PersistenceContext;
import javax.persistence.EntityManager;

/**
 * This class is in charge of login, logout and basic User management.
 * <p>For the API javadoc, refer to the 'remote' and 'local' interfaces.</p>
 *
 * @author Francisco Morero Peyrona
 */
@Stateless
public class SessionManagerBean
       implements SessionManagerLocal, Serializable
{
    private static final long serialVersionUID = 1L;    // TODO: cambiarlo usando: serialver -show
    
    @PersistenceContext
    private EntityManager em;
    
    //------------------------------------------------------------------------//
    // INTERFACE REMOTE
    
    public LoginResult login( String sAccount, String sPassword )
           throws JoingServerSessionException
    {
        LoginResult result = new LoginResult();
        
        if( sAccount != null && sPassword != null )
        {
            try
            {
                UserEntity _user = em.find( UserEntity.class, composeAccount( sAccount ) );

                // NEXT: la password tiene que estar encriptada en la DB,
                if( _user != null )
                {
                    result.setAccountValid( true );

                    if( _user.getPassword().equals( sPassword ) )
                    {
                        result.setPasswordValid( true );

                        // If _user has already a Session (an entry in Sessions 
                        // table), it will be deleted by the SessionTimer at due 
                        // time: it is more secure to create a new SessionId than 
                        // using an existing one.
                        SessionEntity _session = new SessionEntity();
                                      _session.setIdSession( generateSessionId() );
                                      _session.setAccount( _user.getAccount() );
                                      _session.setCreated(  new Date() );
                                      _session.setAccessed( new Date() );

                        em.persist( _session );
                        result.setSessionId( _session.getIdSession() );
                    }
                }
            }
            catch( RuntimeException exc )
            {
                Constant.getLogger().throwing( getClass().getName(), "login(...)", exc );
                throw new JoingServerSessionException( JoingServerException.ACCESS_DB, exc );
            }
        }
        
        return result;
    }
    
    public void logout( String sSessionId )
    {
        if( sSessionId != null )
        {
            try
            {
                SessionEntity _session = em.find( SessionEntity.class, sSessionId );

                if( _session != null )
                    em.remove( _session );
            }
            catch( RuntimeException exc )
            {
                Constant.getLogger().throwing( getClass().getName(), "logout(...)", exc );
                // Does not report exception to client: this method silently logout client.
            }
        }
    }
    
    public boolean isValidPassword( String sSessionId, String sPassword )
    {
        boolean bCorrect = false;
        
        if( sSessionId != null && sPassword != null )
        {
            SessionEntity _session = em.find( SessionEntity.class, sSessionId );

                if( _session != null )
                {
                    UserEntity _user = em.find( UserEntity.class, composeAccount( _session.getAccount() ) );
                    
                    if( _user != null )
                        bCorrect = _user.getPassword().equals( sPassword );    // User account password can't be null
                }
        }
        
        return bCorrect;
    }
    
    //------------------------------------------------------------------------//
    // INTERFACE LOCAL
    
    public String getUserAccount( String sSessionId )
           throws JoingServerSessionException
    {
        SessionEntity _session = null;
        
        try
        {
            _session = em.find( SessionEntity.class, sSessionId );
        
            // As User.Account is used extensively, getUserAccount(...) will be invoked
            // very frecuently, and therefore is a good place to update the TimeStamp
            // (last time the session was accessed).
            if( _session != null )
            {
                _session.setAccessed( new Date( System.currentTimeMillis() ) );
                this.em.persist( _session );
            }
        }
        catch( RuntimeException exc )
        {
            Constant.getLogger().throwing( getClass().getName(), "getUserAccount(...)", exc );
            throw new JoingServerSessionException( JoingServerException.ACCESS_DB, exc );
        }
        
        return ((_session == null) ? null : _session.getAccount());
    }
    
    public boolean isAccountAvailable( String sAccount )
           throws JoingServerSessionException
    {
        boolean  bValid     = false;
        String[] asReserved = Constant.getReservedAccounts();
        
        if( sAccount != null )
        {
            for( int n = 0; n < asReserved.length; n++ )
            {
                if( sAccount.equalsIgnoreCase( asReserved[n] ) )
                    return false;
            }
            
            bValid = (em.find( UserEntity.class, sAccount ) == null);
        }
        
        return bValid;
    }
    
    public String composeAccount( String sAccount )
    {
        // Lower case is not accepted in UserManager::isValidAccount(...)
        sAccount = sAccount.trim().toLowerCase();
        
        if( sAccount.indexOf( '@' ) == -1 )
            sAccount = sAccount +"@"+ Constant.getSystemName();
        
        return sAccount;
    }
    
    //------------------------------------------------------------------------//
    
    // Combinatoria de 75 elementos tomados de 32 en 32
    // = 1.560.606.121.612.270.000.000 (1.560 Billones)

    // <,>,?,&,% are special chars: it makes life easier not use them
    private final static String sVALID4ID = "ABCDEFGHIJKLMNOPQRSTUVWXYZ" +
                                            "abcdefghijklmnopqrstuvwxyz" +
                                            "0123456789+-*=$_!()[]{}";
    
    private String generateSessionId()
    {
        char[] ac    = new char[32];
        Random rnd   = new Random();
        int    nLen  = sVALID4ID.length();
        
        for( int n = 0; n < 32; n++ )
            ac[n] = sVALID4ID.charAt( rnd.nextInt( nLen ) );

        return String.valueOf( ac );
        
        // TODO: la que está aquí debajo es la buena, pero hay que probarla
        /*char[] ac   = new char[32];
        Random rnd  = new Random();
        int    nLen = sVALID4ID.length();
        
        do
        {
            for( int n = 0; n < 16; n++ )
                ac[n] = sVALID.charAt( rnd.nextInt( nLen ) );
        } // Search if generated ID already exists (even if it is almost impossible)
        while( em.find( SessionEntity.class, String.valueOf( ac ) ) != null );
        
        return String.valueOf( ac );*/
    }
}