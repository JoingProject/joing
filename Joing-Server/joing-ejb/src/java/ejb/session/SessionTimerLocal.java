package ejb.session;

import javax.ejb.Local;
import javax.ejb.Timeout;
import javax.ejb.Timer;

/**
 * This is the business interface for SessionTimer enterprise bean.
 */
@Local
public interface SessionTimerLocal
{
    @Timeout
    void updateStatus( Timer timer );
}