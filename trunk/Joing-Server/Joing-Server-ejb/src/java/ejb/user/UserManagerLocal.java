
package ejb.user;

import java.util.Locale;
import javax.ejb.Local;
import org.joing.common.dto.user.User;
import org.joing.common.exception.JoingServerUserException;

/**
 * This is the business interface for UserManager enterprise bean.
 */
@Local
public interface UserManagerLocal extends UserManagerRemote
{
    /**
     * Add a new user to the community.
     * 
     * Important: Classes invoking this class must ensure that all 
     * pre-requisites are satisfied prior to the invocation:
     *<pre>
     * a) User.Account is:
     *    a.1) Not NULL
     *    a.2) Is lower case
     *    a.3) Does not exists
     *    a.4) Is at least 6 chars long
     *    a.5) Is not longer that 32 chars
     *
     * b) User.Password is:
     *    b.1) Not NULL
     *    b.2) Is at least 6 chars long
     *    b.3) Is not longer that 32 chars
     *
     * c) For both:
     *    c.1) Only letters (ASCII 65-90 and 97-122), numbers (ASCII 48-57) 
     *         and following chars "-", "_" and "." (spaces are not allowed)
     *</pre>
     * @param sAccount    Account (as specified before)
     * @param sPassword   Password (as specified before)
     * @param sEmail      User's preferred email
     * @param sFirstName  First name
     * @param sSecondName Second name
     * @param bIsMale     Is male?
     * @param locale      User's locale
     * @param nQuota      Max amout of disk space in MegaBytes (0 == no limit)
     * @return boolean <code>false</code> if user was not added. Most probably
     *                 because the Account (User ID) already exists.
     * @see isAccountAvailable( String sAccount )
     */
    User createUser( String sAccount, String sPassword, String sEmail,
                     String sFirstName, String sSecondName, 
                     boolean bIsMale, Locale locale, int nQuota )
         throws JoingServerUserException;
    
    /**
     * Delete the user and all his/her information.
     *
     * @param user The user to be deleted
     */
    void removeUser( User user )
         throws JoingServerUserException;
   
    /**
     * Checks if passed value is a valid account.
     * Note: It is public to be invoked from the Joing's Web side.
     * 
     * @param sAccount Account to be checked.
     */
    boolean isValidAccount( String sAccount );
    
    /**
     * Checks if passed value is a valid password.
     * Note: It is public to be invoked from the Joing's Web side.
     * 
     * @param sPassword Password to be checked.
     */
    boolean isValidPassword( String sPassword );
}