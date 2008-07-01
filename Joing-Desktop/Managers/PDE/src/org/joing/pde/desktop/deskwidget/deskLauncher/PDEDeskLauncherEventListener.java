/*
 * PDEDeskLauncherEventListener.java
 * 
 * Created on 13-sep-2007, 19:52:04
 * 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.joing.pde.desktop.deskwidget.deskLauncher;

import org.joing.common.desktopAPI.deskwidget.deskLauncher.DeskLauncherListener;

/**
 *
 * @author fmorero
 */
public interface PDEDeskLauncherEventListener extends DeskLauncherListener
{
    public void deletedEvent( PDEDeskLauncher launcher );
    public void toTrashcanEvent( PDEDeskLauncher launcher );
    public void propertiesChangedEvent( PDEDeskLauncher launcherOld, PDEDeskLauncher launcherNew );
    
    public void deleted( PDEDeskLauncher launcher );
    public void sentToTrashcan( PDEDeskLauncher launcher );
    public void propertiesEdited( PDEDeskLauncher launcher );
}
