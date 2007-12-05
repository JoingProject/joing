/*
 * DeskLauncher.java
 *
 * Created on 17 de junio de 2007, 03:35 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.joing.common.desktopAPI.deskwidget.deskLauncher; 

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
public interface DeskLauncher extends DeskWidget, Selectable
{
    /**
     * 
     * Returns the image or null if the deafult one was used: this is important 
     * because the default one can change from one desktop to another.
     * 
     * @return The image or null if the deafult one was used.
     */
    byte[] getImage();
    /**
     * If image is null or empty, the desktop will asign a default one.
     * @param image
     */
    void   setImage( byte[] image );
    
    String getText();
    void   setText( String sText );
    
    public void launch();
    
    /**
     * The target can be one of following:
     * <ul>
     * <li>A path to a file or directory (local or remote): must start with a valid root ("/", "C:/", etc)</li>
     * <li>A remote application: must be an integer number</li>
     * </ul>
     * 
     * @return The target of this launcher.
     */
    public String getTarget();
    /**
     * See getTarget()
     * @param s
     * @see getTarget()
     */
    public void   setTarget( String s );
    
    public String getArguments();
    public void   setArguments( String s );
    
    public void addLauncherListener( DeskLauncherListener ll );
    public void removeLauncherListener( DeskLauncherListener ll );
}