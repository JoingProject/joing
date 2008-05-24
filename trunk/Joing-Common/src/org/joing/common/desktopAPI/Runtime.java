/*
 * Runtime.java
 *
 * Created on 14 de febrero de 2007, 14:22
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.joing.common.desktopAPI;

import java.awt.Image;
import org.joing.common.desktopAPI.deskwidget.deskLauncher.DeskLauncher;
import org.joing.common.desktopAPI.pane.DeskCanvas;
import org.joing.common.desktopAPI.pane.DeskDialog;
import org.joing.common.desktopAPI.pane.DeskFrame;

/**
 *
 * @author fmorero
 */
public interface Runtime
{
    DeskCanvas createCanvas();
    
    DeskLauncher createLauncher();
    
    DeskFrame createFrame();
    
    DeskDialog createDialog();
    
    /**
     * Shows a message in a dialog and an OK button.
     * 
     * @param sTitle   Dialog window title (if <code>null</code> or empty string passed, title will be empty)
     * @param sMessage Message to be shown
     */
    void    showMessageDialog( String sTitle, String sMessage );
    /**
     * Shows a confimation modal dialog.
     * 
     * @param sTitle    Dialog window title (if <code>null</code> or empty string passed, title will be empty)
     * @param sMessage  Message to ask confirmation about
     * @return <code>true</code> if 'OK' button pressed, otherwise ('CANCEL' button or close dialog) return <code>false</code>
     */
    boolean showAcceptCancelDialog( String sTitle, DeskComponent panel );
    /**
     * Shows a confimation modal dialog.
     * 
     * @param sTitle    Dialog window title (if <code>null</code> or empty string passed, title will be empty)
     * @param sMessage  Message to ask confirmation about
     * @return <code>true</code> if 'YES' button pressed, otherwise ('NO' button or close dialog) return <code>false</code>
     */
    boolean showYesNoDialog( String sTitle, String sMessage );
    
    /**
     * Return an image from the standard collection.
     * 
     * @param image Image to be retrieved.
     * @return The ImageIcon instance.
     */
    Image getImage( StandardImage image );
    
    /**
     * Return an image from the standard collection with specified dimension.
     * 
     * @param image Image to be retrieved.
     * @param nWidth The width
     * @param nHeight The height
     * @return The ImageIcon instance.
     */
    Image getImage( StandardImage image, int width, int height );
    
    /**
     * Plays a sound in background.
     * 
     * @param sound
     */
    void play( StandardSound sound );
}