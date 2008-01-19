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
import java.net.URL;
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
    void      showMessageDialog( String sTitle, String sMessage );
    /**
     * Shows a confimation modal dialog.
     * 
     * @param sTitle    Dialog window title (if <code>null</code> or empty string passed, title will be empty)
     * @param sMessage  Message to ask confirmation about
     * @return <code>true</code> if 'OK' button pressed, otherwise ('CANCEL' button or close dialog) return <code>false</code>
     */
    boolean   showAcceptCancelDialog( String sTitle, DeskComponent panel );
    /**
     * Shows a confimation modal dialog.
     * 
     * @param sTitle    Dialog window title (if <code>null</code> or empty string passed, title will be empty)
     * @param sMessage  Message to ask confirmation about
     * @return <code>true</code> if 'YES' button pressed, otherwise ('NO' button or close dialog) return <code>false</code>
     */
    boolean   showYesNoDialog( String sTitle, String sMessage );
    /**
     * Asks for password and validates it agains the server.<br>
     * (For security reasons, the password is not stored locally, not even in 
     * memory).
     * @return <code>true</code> if password is correct.
     */
    boolean   askForPasswordDialog();
    
    /**
     * One of the standard icons found by name (without extension).
     * <p>
     * This method will try its best to find it by name, but if it was impossible,
     * then will return a default image representing "image not found".<b>
     * Do NOT include the file name extension.
     * 
     * @param name The image name.
     * @return The image.
     */
    Image getStandardImage( String name );
    
    Image getStandardImage( String name, int width, int height );
}