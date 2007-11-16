/*
 * Launcher.java
 *
 * Created on 17 de junio de 2007, 03:35 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.joing.common.desktopAPI.deskwidget.deskLauncher; 

import java.awt.Image;
import org.joing.common.desktopAPI.Selectable;
import org.joing.common.desktopAPI.deskwidget.DeskWidget;

/**
 * Common base class for all kind of "icons like" that are shown in the desk.
 * Among them: applications, documents, devices, etc.
 * 
 * @author mario
 * 
 * updated by: Francisco Morero Peyrona
 */
public interface Launcher extends DeskWidget, Selectable
{
    public Image  getImage();               // I use Image because it is in AWT (ImageIcon is in 
    public void   setImage( Image image );  // Swing) and in this way the interface can be use widely
    
    public boolean launch();
    
    public void addLauncherListener( LauncherEventListener ll );
    public void removeLauncherListener( LauncherEventListener ll );
}