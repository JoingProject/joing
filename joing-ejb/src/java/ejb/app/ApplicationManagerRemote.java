
package ejb.app;

import java.util.List;
import java.util.Map;
import javax.ejb.Remote;

/**
 * This is the business interface for <code>ApplicationManager</code> enterprise
 * bean.
 */
@Remote
public interface ApplicationManagerRemote
{
    /**
     * Retrieve a <code>List</code> with instances of <code>Application</code> 
     * class (the DTO for <code>ApplicationEntity</code>) containing, all
     * applications that user can install (regardless of their installation
     * status).
     * <p>
     * The list should not be empty, will be <code>null</code> if something 
     * went wrong.
     *
     * @param sSessionId A valid session (sSessionId in order to obtain the user).
     * @return List of applications matching the criteria.
     */
    List<AppsByGroup> getAvailableForUser( String sSessionId )
                      throws JoingServerAppException;
    
    /**
     * Retrieve a <code>List</code> with instances of <code>Application</code> 
     * class (the DTO for <code>ApplicationEntity</code>) containing, all the
     * set of applications that user can install, only those that are not
     * installed yet.
     * <p>
     * The list can be empty if there is none Application matching the criteria,
     * or will be <code>null</code> if something went wrong.
     *
     * @param sSessionId A valid session (sSessionId in order to obtain the user).
     * @return List of applications matching the criteria.
     */
    List<AppsByGroup> getNotInstalledForUser( String sSessionId )
                      throws JoingServerAppException;
    
    /**
     * Retrieve a <code>List</code> with instances of <code>Application</code> 
     * class (the DTO for <code>ApplicationEntity</code>) containing, from the
     * set of applications that user can install, only those that already have
     * installed.
     * <p>
     * The list can be empty if there is none Application matching the criteria,
     * or will be <code>null</code> if something went wrong.
     *
     * @param sSessionId A valid session (sSessionId in order to obtain the user).
     * @return List of applications matching the criteria.
     */
    List<AppsByGroup> getInstalledForUser( String sSessionId )
                      throws JoingServerAppException;
    
    /**
     * Mark an application as to be installed for certain user.
     * <p>
     * This only means that the application will be shown in the user's menu.
     *
     * @param sSessionId A valid session (sSessionId in order to obtain the user).
     * @param app The <code>Application</code> instance to be installed.
     */
    boolean install( String sSessionId, Application app )
            throws JoingServerAppException;
    
    /**
     * Mark an application as not installed for certain user.
     * <p>
     * This only means that the application will not be shown in the user's menu.
     *
     * @param sSessionId A valid session (sSessionId in order to obtain the user).
     * @param app The <code>Application</code> instance to be uninstalled.
     */
    boolean uninstall( String sSessionId, Application app )
            throws JoingServerAppException;
    
    /**
     * Returns the prefrerred application (if any) for requested file extension.
     * <p>
     * Note: As this intormation is not relevant and is not attached to the User,
     * Session ID is not requested by this method.
     *
     * @param sSessionId A valid session (sSessionId in order to obtain the user).
     * @param sFileExtension The file extension to be checked (without the 
     *                       initial dot '.', just the extension)
     * @return The prefrerred application or <code>null</code> if there is not a
     *         registered application for this file extension
     */
    Application getPreferredForType( String sSessionId, String sFileExtension )
                throws JoingServerAppException;
}