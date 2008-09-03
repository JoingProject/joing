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

import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import org.joing.kernel.api.kernel.jvmm.JThreadGroup;
import org.joing.kernel.api.kernel.jvmm.Platform;
import org.joing.kernel.api.kernel.log.JoingLogger;
import org.joing.kernel.api.kernel.log.Logger;
import org.joing.kernel.api.kernel.log.SimpleLoggerFactory;


/**
 *
 * @author Antonio Varela Lizardi <antonio@icon.net.mx>
 */
public class JoingSecurityManager extends SecurityManager {

    private final Logger logger = SimpleLoggerFactory.getLogger(JoingLogger.ID);
    
    public JoingSecurityManager() {
        logger.debugJVMM("Instance of joingSecurityManager created.");
    }

    @Override
    public void checkExit(int status) {     
        
        Throwable t = new Throwable();
        StackTraceElement[] trace = t.getStackTrace();
        String name = Thread.currentThread().getName();

        for (int i = 0; i < trace.length; i++) {

            String className = trace[i].getClassName();
            String methodName = trace[i].getMethodName();

            if (className.equals("java.lang.System") && methodName.equals("exit")) {

                Platform platform = RuntimeFactory.getPlatform();
                
                final JThreadGroup tg = RuntimeFactory.getPlatform().getJThreadGroup();

                if (tg != null) {
                    if (SwingUtilities.isEventDispatchThread()) {
                        SwingWorker worker = new SwingWorker() {

                            @Override
                            protected Object doInBackground() throws Exception {
                                tg.close();
                                return null;
                            }
                        };
                        worker.execute();
                    } else {
                        tg.close();
                    }
                    throw new RuntimeException("exit");
                }
                break;
            }
        }
        
        super.checkExit(status);
        
        if (Thread.currentThread().getId() !=
                RuntimeFactory.getPlatform().getMainThreadId()) {

            StringBuilder sb = new StringBuilder("Call to exit() ");
            sb.append("with status ").append(status).append(" by ");
            sb.append("unauthorized Thread[").append(name).append("] ");
            sb.append("with id ");
            sb.append(Thread.currentThread().getId()).append("\n");
            for (int i = 0; i < trace.length; i++) {
                sb.append(trace[i].toString()).append("\n");
            }
            logger.warning(sb.toString());

            // TODO: Fix this.
            // uncommenting this will prevent the application to exit.
            // We must fix the mainLoop() before doing it.
           // throw new SecurityException(sb.toString());
        }
    }

    @Override
    public void checkPermission(java.security.Permission p) {
    }
}
