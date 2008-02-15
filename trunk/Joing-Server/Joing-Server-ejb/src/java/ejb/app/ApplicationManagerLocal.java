
package ejb.app;

import java.util.List;
import javax.ejb.Local;
import org.joing.common.dto.app.Application;
import org.joing.common.exception.JoingServerAppException;

/**
 * This is the business interface for ApplicationManager enterprise bean.
 */
@Local
public interface ApplicationManagerLocal extends ApplicationManagerRemote
{
    Application getApplicationByName(String sessionId, String executableName) throws JoingServerAppException;

    List<Application> getAvailableDesktops() throws JoingServerAppException;
}
