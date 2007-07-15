/*
 * SessionManagerRemote.java
 *
 * Created on 22 de mayo de 2007, 17:30
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package ejb.session;

import ejb.user.*;
import javax.ejb.Remote;

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
     */
    LoginResult login( String sAccount, String sPassword );
    
    /**
     * Closes an existing session.
     *
     * @param sSessionId An existing Session ID.
     */
    void logout( String sSessionId );
}