/*
 * SessionTimerBean.java
 *
 * Created on 18 de mayo de 2007, 11:07
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package ejb.session;

import ejb.Constant;
import ejb.user.*;
import java.sql.Date;
import java.util.List;
import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * Note: Good article about Timers in EJB 3.0:
 *       http://www.javabeat.net/javabeat/ejb3/articles/timer_services_api_in_ejb_3_0_1.php
 * @author Francisco Morero Peyrona
 */
@Stateless
public class SessionTimerBean 
       implements ejb.session.SessionTimerLocal
{
    @Resource
    private SessionContext context;
    private Timer          timer;
    
    @PersistenceContext
    private EntityManager em;
    private Query         query;
    
    /** Creates a new instance of SessionTimerBean */
    public SessionTimerBean()
    { // TODO: Para arrancar el timer automaticamente, mirar esto:
      //       http://www.onjava.com/pub/a/onjava/2004/10/13/j2ee-timers.html?page=2
        long nInterval = 15 * 60 * 1000;   // 15 minutes
        
        this.timer = this.context.getTimerService().createTimer( nInterval, nInterval );
        this.query = this.em.createQuery( "SELECT s FROM Session s WHERE s.accesed < :nTime" );
    }
    
    @Timeout
    public void updateStatus( Timer timer )
    {
        List<SessionEntity> timedOutSessions;
            
        // As Timers exists for ever, it is a good practice to read
        // from Constant class periodically (the value could changed)
        long nTime = System.currentTimeMillis() - Constant.getSessionTimeOut();
        
        // Get all sessions without activity since nTime
        query.setParameter( "nTime", new Date( nTime ) );
        timedOutSessions = (List<SessionEntity>) query.getResultList();

        for( SessionEntity s : timedOutSessions )
            em.remove( s );
        
        Constant.getLogger().info( "Se han borrando "+ timedOutSessions.size() +" sessiones inactivas." );
    }
}