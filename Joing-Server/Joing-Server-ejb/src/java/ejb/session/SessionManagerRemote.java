/*
 * SessionManagerRemote.java
 *
 * Created on 22 de mayo de 2007, 17:30
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package ejb.session;

import javax.ejb.Remote;
import org.joing.common.dto.session.LoginResult;
import org.joing.common.exception.JoingServerSessionException;

/**
 * This is the business interface for <code>SessionManager</code> enterprise
 * bean.
 *
 * @author Francisco Morero Peyrona
 */
@Remote
public interface SessionManagerRemote
{
    /**
     * Performs the login.
     *
     * @param  sAccount  The user account (user ID, normally his/her name)
     * @param  sPassword The user password
     * @return String A valid new SessionId that will be used from now and on
     *                meanwhile the session is alive. Or <code>null</null> if the
     *                Account and/or Password were invalid.
     * @throws JoingServerSessionException If something goes wrong.
     */
    LoginResult login( String sAccount, String sPassword )
                throws JoingServerSessionException;
    
    /**
     * Peform a logout for an existing session.
     * <p>
     * Note: This method silently performs the logout and does not report 
     * an exception to client.
     * 
     * @param sSessionId An existing Session ID.
     */
    void logout( String sSessionId );
}