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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.joing.kernel.api.kernel.jvmm.App;
import org.joing.kernel.api.kernel.jvmm.AppListener;
import org.joing.kernel.api.kernel.jvmm.AppManager;

/**
 *
 * @author Antonio Varela Lizardi <antonio@icon.net.mx>
 */
public class AppManagerImpl implements AppManager {

    /** Listeners para el Lifecycle. */
    Set<AppListener> appListeners = new HashSet<AppListener>();
    Set<App> apps = new HashSet<App>();

    public AppManagerImpl() {
    }

    /**
     * addAppListener
     * Agrega un Listener para el Lifecycle.
     */
    @Override
    public void addAppListener(AppListener listener) {
        synchronized (appListeners) {
            appListeners.add(listener);
            // After adding a new listener it has to be notified 
            // about already existing (opened) applications.
            for( App app : apps )
                listener.applicationAdded(app);
        }
    }

    @Override
    public void removeAppListener(AppListener listener) {
        synchronized (appListeners) {
            appListeners.remove(listener);
        }
    }

    /**
     * addApp
     * Agrega una aplicación al sistema. Invoca los
     * Listeners que estén registrados.
     */
    @Override
    public void addApp(App app) {
        synchronized (apps) {
            apps.add(app);
        }
        fireAppAdded(app);
    }

    @Override
    public void removeApp(App app) {
        synchronized (apps) {
            apps.remove(app);
        }
        fireAppRemoved(app);
    }

    @Override
    public void fireAppRemoved(App app) {
        final AppListener[] arr = new AppListener[appListeners.size()];
        synchronized (appListeners) {
            appListeners.toArray(arr);
        }
        for (int i = 0; i < arr.length; i++) {
            arr[i].applicationRemoved(app);
        }
    }

    @Override
    public void fireAppAdded(App app) {
        AppListener[] arr = new AppListener[appListeners.size()];
        synchronized (appListeners) {
            appListeners.toArray(arr);
        }
        for (int i = 0; i < arr.length; i++) {
            arr[i].applicationAdded(app);
        }
    }

    @Override
    public List<App> applications() {
        
        List<App> list = new ArrayList<App>();
        
        list.addAll(this.apps);
        
        return list;
    }
}
