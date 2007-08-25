/*
 * AppLauncherListener.java
 *
 * Created on 24 de junio de 2007, 11:44
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.joing.applauncher;

import org.joing.applauncher.gui.*;
import org.joing.jvmm.*;
import org.joing.applauncher.AppStartedEvent;
import org.joing.applauncher.AppStatusChangedEvent;
import org.joing.applauncher.AppStoppedEvent;
import java.util.EventListener;

/**
 *
 * @author fmorero
 */
public interface AppLauncherListener extends EventListener
{
    void appStarted( AppStartedEvent ase );
    
    void appStopped( AppStoppedEvent ase );
    
    void appStatusChanged( AppStatusChangedEvent ase );
}