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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.joing.kernel.api.kernel.log.JoingLogger;
import org.joing.kernel.api.kernel.log.Logger;
import org.joing.kernel.api.kernel.log.SimpleLoggerFactory;

/**
 * Execution Task.
 * 
 * @author Antonio Varela Lizardi <antonio@icon.net.mx>
 */
public class Task implements Runnable {

    private BridgeClassLoader classLoader;
    private String mainClassName;
    private String args[];
    
    private Logger logger = SimpleLoggerFactory.getLogger(JoingLogger.ID);

    public Task(final BridgeClassLoader classLoader, 
            final String mainClassName, final String[] args) {
        this.classLoader = classLoader;
        this.mainClassName = mainClassName;
        this.args = args;
    }

    public void run() {
        try {
            
            Class c = classLoader.loadClass(mainClassName);
            
            Method main = c.getMethod("main", new Class[] {String[].class});
            main.setAccessible(true);
            
            // main is static, the argument will be ignored
            main.invoke(c, new Object[]{args});
            
        } catch (ClassNotFoundException cnfe) {
            logger.critical("Class '{0}' not found.", mainClassName);
            throw new ExecutionTaskException("ClassNotFoundException", cnfe);
        } catch (InvocationTargetException ite) {
            logger.critical("InvocationTargetExcepion: {0}", ite.getMessage());
            logger.critical("Cause: {0}",
                    ite.getCause() != null ? ite.getCause().getMessage() : 
                        "null");
            logger.critical("StackTrace will follow.");
            logger.printStackTrace(ite);
            throw new ExecutionTaskException("InvocationTargetException", ite);
        } catch (NullPointerException npe) {
            logger.critical("NullPointerException: {0}", npe.getMessage());
            throw new ExecutionTaskException(npe.getMessage(), npe);
        } catch (Exception e) {
            logger.critical("Error in ExecutionTask: {0}", e.getMessage());
            logger.critical("Cause: {0}",
                    e.getCause() != null ? e.getCause().getMessage() : "null");
            throw new ExecutionTaskException(e.getMessage(), e);
        }
    }
}
