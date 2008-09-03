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

package org.joing.kernel.jvmm;

import org.joing.kernel.api.kernel.jvmm.App;
import org.joing.kernel.api.kernel.jvmm.AppManager;
import org.joing.kernel.api.kernel.log.JoingLogger;
import org.joing.kernel.api.kernel.log.Logger;
import org.joing.kernel.api.kernel.log.SimpleLoggerFactory;

/**
 * Simple Task for disposing an application.
 * 
 * @author Antonio Varela Lizardi <antonio@icon.net.mx>
 */
public class DisposerTask implements Runnable {

    private AppManager appManager;
    private App application;
    private sun.awt.AppContext appContext;
    private final Logger logger = 
            SimpleLoggerFactory.getLogger(JoingLogger.ID);

    public DisposerTask(AppManager appManager, App application, 
            sun.awt.AppContext appContext) {
        this.appManager = appManager;
        this.application = application;
        this.appContext = appContext;
    }
    
    public void run() {
        
        logger.debugJVMM("Removing application '{0}'.",
                application.getMainClassName());
        
        appManager.removeApp(application);
        
        if (appContext != null) {
            logger.debugJVMM("Disposing AppContext.");
            try {
                appContext.dispose();
            } catch (Exception e) {
                logger.debugJVMM("Exception en appContext.dispose(): {0}",
                        e.getMessage());
            }
        }
    }

}
