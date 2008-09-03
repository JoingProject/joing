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

package org.joing.kernel.api.kernel.jvmm;

import java.io.OutputStream;
import java.net.URL;
import java.util.Map;
import java.util.Properties;
import org.joing.kernel.api.bridge.Bridge2Server;
import org.joing.kernel.api.desktop.DesktopManager;
import org.joing.common.dto.app.Application;

/**
 *
 * @author Antonio Varela Lizardi <antonio@icon.net.mx>
 */
public interface Platform {

    void halt();
    boolean isHalted();
    void shutdown();
    
    Bridge2Server getBridge();
    
    // Temporal.
    Properties getClientProp();
    
    boolean isInitialized();
    String getServerBaseURL();
    
    long getMainThreadId();
    Thread getMainThread();
    JThreadGroup getJThreadGroup();
    Map<Object, ClassLoader> getClassLoaderCache();
    AppManager getAppManager();
    DesktopManager getDesktopManager();
    void setDesktopManager(DesktopManager desktop);
    
    String getMainClassName(Application application);
    void start(final int appId) throws ApplicationExecutionException;
    void start(final int appId, String[] args, OutputStream out, OutputStream err) 
            throws ApplicationExecutionException;
    void start(final Application application, String[] args, OutputStream out, 
            OutputStream err) throws ApplicationExecutionException;
    /**
     * Base method for application launch. 
     * @param classPath Array of URL Objects with the class path for loading
     * classes and resources. 
     * @param application Application object to be Launched.
     * @param args Arguments for the application, if any.
     * @param out Outputstream object associated to the application's System.out
     * property.
     * @param err Outputstream object associated to the application's Syste.err
     * property.
     * @throws org.joing.common.clientAPI.jvmm.ApplicationExecutionException
     * @see org.joing.jvmm.net.URLFormat URLFormat
     * @see org.joing.jvmm.net.URLFormat#toURL URLFormat.toURL()
     */
    void start(final URL[] classPath, final Application application,
            final String[] args, final OutputStream out, final OutputStream err)
            throws ApplicationExecutionException;
}
