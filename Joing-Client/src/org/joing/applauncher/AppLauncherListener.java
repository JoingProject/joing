/*
 * AppLauncherListener.java
 *
 * Created on 24 de junio de 2007, 11:44
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.joing.AppLauncher;

import org.joing.AppLauncher.gui.*;
import org.joing.jvmm.*;
import org.joing.AppLauncher.AppStartedEvent;
import org.joing.AppLauncher.AppStatusChangedEvent;
import org.joing.AppLauncher.AppStoppedEvent;
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