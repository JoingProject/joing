
package ejb.user;

import java.util.List;
import javax.ejb.Remote;

/**
 * This is the business interface for UserManager enterprise bean.
 */
@Remote
public interface UserManagerRemote
{
    /**
     * Return a <code>User</code> based on its Session ID.
     *
     * Note: This convenience method to be used locally as well as remotely.
     *
     * @param  sSessionId An existing SessionId
     * @return User A DTO representing the UserEntity that holds this Session,
     *         or <code>null</code> if sSessionId was not found.
     */
    User getUser( String sSessionId )
         throws JoingServerUserException;
    
    /**
     * Update user information.
     *
     * @param  sSessionId An existing SessionId
     * @param user The user to be updated
     */
    void updateUser( String sSessionId, User user )
         throws JoingServerUserException;

    /**
     * Returns all available locales (can be used by users to select their one).
     *
     * @param  sSessionId An existing SessionId
     * @return All available locales or null if something goes wrong.
     */
    List<Local> getAvailableLocales( String sSessionId )
                throws JoingServerUserException;
}