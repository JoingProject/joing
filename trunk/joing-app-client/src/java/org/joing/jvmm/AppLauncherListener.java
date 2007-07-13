/*
 * AppLauncherListener.java
 *
 * Created on 24 de junio de 2007, 11:44
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.joing.jvmm;

import org.joing.jvmm.AppStartedEvent;
import org.joing.jvmm.AppStatusChangedEvent;
import org.joing.jvmm.AppStoppedEvent;
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