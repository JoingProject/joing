/*
 * SessionBean.java
 *
 * Created on 12 de mayo de 2007, 17:48
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package ejb.session;

import ejb.Constant;
import ejb.user.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Random;
import javax.ejb.Stateless;
import javax.persistence.PersistenceContext;
import javax.persistence.EntityManager;

/**
 * This class is in charge of login, logout and basic User management.
 * <p>
 * For the API javadoc, refer to the 'remote' and 'local' interfaces.
 *
 * @author Francisco Morero Peyrona
 */
@Stateless
public class SessionManagerBean
       implements SessionManagerLocal, SessionManagerRemote, Serializable // TODO Hacer lo del seriazable
{
    @PersistenceContext
    private EntityManager em;
    
    //------------------------------------------------------------------------//
    // INTERFACE REMOTE
    
    public String login( String sAccount, String sPassword )
    {
        String sSessionId = null;
        
        if( sAccount != null && sPassword != null )
        {
            UserEntity _user = this.em.find( UserEntity.class, sAccount );

            // @TODO: la password tiene que estar encriptada en la DB,
            //        por lo que aquí habría que des-encriptarla
            if( _user != null && _user.getPassword().equals( sPassword ) )
            {
                // If _user has already a Session (an entry in Sessions table),
                // it will be deleted by the SessionTimer at due time: It is more
                // secure to create a new SessionId than using an existing one.
                SessionEntity _session = new SessionEntity();
                              _session.setIdSession( generateSessionId() );
                              _session.setAccount( _user.getAccount() );
                              _session.setCreated(  new Date() );
                              _session.setAccessed( new Date() );
                              
                this.em.persist( _session );
                sSessionId = _session.getIdSession();
            }
        }
        
        return sSessionId;
    }
    
    public void logout( String sSessionId )
    {
        if( sSessionId != null )
        {
            try
            {
                SessionEntity _session = this.em.find( SessionEntity.class, sSessionId );

                if( _session != null )
                    this.em.remove( _session );
            }
            catch( Exception exc )
            {
                Constant.getLogger().throwing( getClass().getName(), "logout(...)", exc );
            }
        }
    }
    
    //------------------------------------------------------------------------//
    // INTERFACE LOCAL
    
    public String getUserAccount( String sSessionId )
    {
        SessionEntity _session = this.em.find( SessionEntity.class, sSessionId );
            
        // As User.Account is used extensively, getUserAccount(...) will be invoked
        // very frecuently, and therefore is a good place to update the TimeStamp
        // (last time the session was accessed).
        _session.setAccessed( new Date( System.currentTimeMillis() ) );
        this.em.persist( _session );
                
        return ((_session == null) ? null : _session.getAccount());
    }
    
    public boolean isAccountAvailable( String sAccount )
    {
        if( sAccount == null )
            return false;
        else
            return (this.em.find( UserEntity.class, sAccount ) == null);
    }
    
    //------------------------------------------------------------------------//
    
    // Combinatoria de 75 elementos tomados de 32 en 32
    // = 1.560.606.121.612.270.000.000 (1.560 Billones)

    // <,>,?,&,% are special chars: it is easier not use them
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
        
        // @TODO: la que está aquí debajo es la buena, pero hay que probarla
        /*char[] ac   = new char[32];
        Random rnd  = new Random();
        int    nLen = sVALID4ID.length();
        
        do
        {
            for( int n = 0; n < 16; n++ )
                ac[n] = sVALID.charAt( rnd.nextInt( nLen ) );
        } // Search if generated ID already exists (even if it is almost impossible)
        while( this.em.find( SessionEntity.class, String.valueOf( ac ) ) != null );
        
        return String.valueOf( ac );*/
    }
}