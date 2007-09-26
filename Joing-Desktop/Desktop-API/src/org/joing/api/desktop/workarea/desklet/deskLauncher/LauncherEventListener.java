/*
 * LauncherEventListener.java
 * 
 * Created on 11-sep-2007, 14:33:03
 * 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.joing.api.desktop.workarea.desklet.deskLauncher;

import java.util.EventListener;
import org.joing.api.desktop.workarea.desklet.*;

/**
 *
 * @author fmorero
 */
public interface LauncherEventListener extends EventListener
{
    public void selectedEvent( LauncherEvent le );
    public void selectionIncrementalEvent( LauncherEvent le );
    public void launchedEvent( LauncherEvent le );
}