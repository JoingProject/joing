/*
 * Launcher.java
 *
 * Created on 17 de junio de 2007, 03:35 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.joing.common.desktopAPI.deskwidget.deskLauncher; 

import org.joing.common.desktopAPI.deskwidget.DeskWidget;

/**
 * Common base class for all kind of "icons like" that are shown in the desk.
 * Among them: applications, documents, devices, etc.
 * 
 * @author mario
 * 
 * updated by: Francisco Morero Peyrona
 */
public interface Launcher extends DeskWidget
{
    boolean isSelected();
    void    setSelected( boolean b );
    
    public boolean launch();
    
    public void addLauncherListener( LauncherEventListener ll );
    public void removeLauncherListener( LauncherEventListener ll );
}