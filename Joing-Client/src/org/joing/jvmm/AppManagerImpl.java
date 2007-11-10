/*
 * AppManager.java
 * 
 * Created on Aug 5, 2007, 6:47:59 PM
 * 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.joing.jvmm;

import java.util.HashSet;
import java.util.Set;
import org.joing.common.jvmm.App;
import org.joing.common.jvmm.AppListener;
import org.joing.common.jvmm.AppManager;

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
        apps.add(app);
        fireAppAdded(app);
    }
    
    
    @Override
    public void removeApp(App app) {
        apps.remove(app);
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
}
