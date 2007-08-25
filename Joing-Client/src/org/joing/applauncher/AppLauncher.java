/*
 * AppLauncher.java
 *
 * Created on 24 de junio de 2007, 11:26
 *
 * Copyright (C) 2007 Francisco Morero Peyrona
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or (at
 * your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 */

package org.joing.applauncher;

import org.joing.jvmm.*;
import org.joing.applauncher.AppLauncherListener;
import org.joing.applauncher.AppStartedEvent;
import org.joing.applauncher.AppStatusChangedEvent;
import org.joing.applauncher.AppStoppedEvent;
import javax.swing.event.EventListenerList;

/**
 * Antonio: copia estos métodos en tu clase AppLauncher
 *
 * @author Francisco Morero Peyrona
 */
public class AppLauncher {

    private EventListenerList listenerList;

    //------------------------------------------------------------------------//
    public AppLauncher() {
        this.listenerList = new EventListenerList();
    }

    public void addListener(AppLauncherListener l) {
        this.listenerList.add(AppLauncherListener.class, l);
    }

    public void removeListener(AppLauncherListener l) {
        this.listenerList.remove(AppLauncherListener.class, l);
    }

    protected void fireAppStarted() {
        // Guaranteed to return a non-null array
        Object[] listeners = listenerList.getListenerList();

        AppStartedEvent ase = new AppStartedEvent(this);

        // Process the listenerList last to first, notifying
        // those that are interested in this event
        for (int n = listeners.length - 2; n >= 0; n -= 2) {
            if (listeners[n] == AppLauncherListener.class) {
                ((AppLauncherListener) listeners[n+1]).appStarted(ase);
            }
        }
    }

    protected void fireAppStopped() {
        // Guaranteed to return a non-null array
        Object[] listeners = listenerList.getListenerList();

        AppStoppedEvent ase = new AppStoppedEvent(this);

        // Process the listenerList last to first, notifying
        // those that are interested in this event
        for (int n = listeners.length - 2; n >= 0; n -= 2) {
            if (listeners[n] == AppLauncherListener.class) {
                ((AppLauncherListener) listeners[n+1]).appStopped(ase);
            }
        }
    }

    // No lo tengo muy claro, pero este método podría utilizarse para
    // inicar cambios en el consumo de RAM de la aplicación y cosas
    // por el estilo.
    // Se podría hacer un timer en esta clase que periódicamente
    // invocase éste método para todas las aplicaciones, indicando su
    // consumo de memoria, the CPU, de Threads y ese tipo de cosas.
    protected void fireAppStatusChanged() {
        // Guaranteed to return a non-null array
        Object[] listeners = listenerList.getListenerList();

        AppStatusChangedEvent ase = new AppStatusChangedEvent(this);
        ase.setMemory(150); // Los Kb de RAM que sean
        ase.setCPU(3); // El procetanje que sea
        // Process the listenerList last to first, notifying
        // those that are interested in this event
        for (int n = listeners.length - 2; n >= 0; n -= 2) {
            if (listeners[n] == AppLauncherListener.class) {
                ((AppLauncherListener) listeners[n+1]).appStatusChanged(ase);
            }
        }
    }
}