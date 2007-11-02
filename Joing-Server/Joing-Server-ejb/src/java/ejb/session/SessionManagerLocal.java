package ejb.session;

import javax.ejb.Local;
import org.joing.common.exception.JoingServerSessionException;

/**
 * This is the business interface for <code>SessionManager</code> enterprise
 * bean.
 *
 * @author Francisco Morero Peyrona
 */
@Local
public interface SessionManagerLocal extends SessionManagerRemote
{
    /**
     * Checks if passed sAccount is already in use.
     * This is a convenience method to be used (among others) by the "Create new
     * user" Servlet.
     *
     * @param sAccount Account to be checked
     * @return <code>true</code> if passed account is available.
     */
    boolean isAccountAvailable( String sAccount )
            throws JoingServerSessionException;
    
    /**
     * Return the <code>User.Account</code> based on a Session ID.
     * <p>
     * When only the <code>Account</code> is needed, this method is preferred 
     * over <code>getUser( sSessionId ).getAccount()</code> invocation.
     * <p>
     * Note: This is just a convenience method to be used only locally.
     *
     * @param  sSessionId A SessionId
     * @return String <code>User.Account</code> (the User ID) or 
     *                <code>null</code> if SessionId does not exists.
     */
    String getUserAccount( String sSessionId )
           throws JoingServerSessionException;
}