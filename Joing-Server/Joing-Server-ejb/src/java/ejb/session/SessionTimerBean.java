/*
 * Copyright (C) 2007, 2008 Join'g Team Members. All Rights Reserved.
 * Join'g Team Members are listed at project's home page. By the time of 
 * writting this at: https://joing.dev.java.net/servlets/ProjectMemberList.
 *
 * This file is part of Join'g project: www.joing.org
 *
 * GNU Classpath is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the free
 * Software Foundation; either version 3, or (at your option) any later version.
 * 
 * GNU Classpath is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * GNU Classpath; see the file COPYING.  If not, write to the Free Software 
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
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