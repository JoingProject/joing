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

/**
 *
 * @author Antonio Varela Lizardi <antonio@icon.net.mx>
 */
public class AppManager {

    /** Listeners para el Lifecycle. */
    Set<AppListener> appListeners = new HashSet<AppListener>();
    Set<App> apps = new HashSet<App>();
    
    public AppManager() {
    }

    /**
     * addAppListener
     * Agrega un Listener para el Lifecycle.
     */
    public void addAppListener(AppListener listener) {
        synchronized (appListeners) {
            appListeners.add(listener);
        }
    }
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
    public void addApp(App app) {
        apps.add(app);
        fireAppAdded(app);
    }
    
    
    public void removeApp(App app) {
        apps.remove(app);
        fireAppRemoved(app);
    }
    
    
    void fireAppRemoved(App app) {
        final AppListener[] arr = new AppListener[appListeners.size()];
        synchronized (appListeners) {
            appListeners.toArray(arr);
        }
        for (int i = 0; i < arr.length; i++) {
            arr[i].applicationRemoved(app);
        }
    }
    
    
    void fireAppAdded(App app) {
        AppListener[] arr = new AppListener[appListeners.size()];
        synchronized (appListeners) {
            appListeners.toArray(arr);
        }
        for (int i = 0; i < arr.length; i++) {
            arr[i].applicationAdded(app);
        }
    }
}
