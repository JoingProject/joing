/*
 * PDEDeskLauncherEventListener.java
 * 
 * Created on 13-sep-2007, 19:52:04
 * 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.joing.pde.desktop.workarea.desklet.deskLauncher;

import org.joing.api.desktop.workarea.desklet.deskLauncher.LauncherEventListener;

/**
 *
 * @author fmorero
 */
public interface PDEDeskLauncherEventListener extends LauncherEventListener
{
    public void deletedEvent( PDEDeskLauncher launcher );
    public void toTrashcanEvent( PDEDeskLauncher launcher );
    public void propertiesChangedEvent( PDEDeskLauncher launcherOld, PDEDeskLauncher launcherNew );
    
    public void deleted( PDEDeskLauncher launcher );
    public void sentToTrashcan( PDEDeskLauncher launcher );
    public void propertiesEdited( PDEDeskLauncher launcher );
}
