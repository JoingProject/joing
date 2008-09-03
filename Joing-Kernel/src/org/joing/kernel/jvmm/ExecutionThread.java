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
import org.joing.kernel.api.kernel.jvmm.JThreadGroup;
import org.joing.kernel.api.kernel.log.JoingLogger;
import org.joing.kernel.api.kernel.log.Logger;
import org.joing.kernel.api.kernel.log.SimpleLoggerFactory;

/**
 *
 * @author Antonio Varela Lizardi <antonio@icon.net.mx>
 */
public class ExecutionThread extends Thread {

    private AppManager appManager;
    private App application;
    private Task executionTask;
    final private Logger logger = SimpleLoggerFactory.getLogger(JoingLogger.ID);
    
    private boolean sharedAWTContext = false;

    public ExecutionThread(AppManager appManager,
            App application, Task executionTask) {
        super(application.getThreadGroup(), "JoingExecutionThread " + 
                application.getThreadGroup().getName());
        this.appManager = appManager;
        this.application = application;
        this.executionTask = executionTask;
    }

    public void setSharedAWTContext(boolean b) {
        this.sharedAWTContext = b;
    }
    
    
    @Override
    public void run() {

        // createNewAppContext will grab the current thread group,
        // we need to supply an aditional thread group for the
        // disposer
        ThreadGroup currentThreadGroup = getThreadGroup();
        ThreadGroup dtg = new ThreadGroup(currentThreadGroup,
                "DisposerThreadGroup for " + 
                application.getThreadGroup().getName());
        DisposerThread disposerThread = new DisposerThread(dtg);
        
        if (!this.sharedAWTContext) {
            sun.awt.AppContext appContext =
                    sun.awt.SunToolkit.createNewAppContext();

            disposerThread.setDisposerTask(
                    new DisposerTask(appManager, application, appContext));
        } else {
            // The DisposerThread does not disposes main Context
            disposerThread.setDisposerTask(
                    new DisposerTask(appManager, application, null));
        }
        
//        DisposerTask disposerTask =
//                new DisposerTask(appManager, application, appContext);
//        
//        DisposerThread disposerThread =
//                new DisposerThread(dtg, disposerTask);
        
        //application.getThreadGroup().setDisposer(disposerThread);
        ThreadGroup tg = getThreadGroup();
        if (tg instanceof JThreadGroup) {
            JThreadGroup jtg = (JThreadGroup)tg;
            jtg.setDisposer(disposerThread);
        } else {
            application.getThreadGroup().setDisposer(disposerThread);
        }

        try {
            ///SwingUtilities.invokeAndWait(executionTask);
            executionTask.run();

        } catch (Exception e) {
            appManager.removeApp(application);
            application.getThreadGroup().close();
            StringBuilder sb = new StringBuilder();
            sb.append("Exception caught in ExecutionThread for App '{0}; ");
            logger.critical(sb.toString(), application.getApplication().getName());
            logger.critical("StackTrace will follow.");
            logger.printStackTrace(e);
        }
    }
}
