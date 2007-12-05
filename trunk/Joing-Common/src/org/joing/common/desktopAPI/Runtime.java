/*
 * Runtime.java
 *
 * Created on 14 de febrero de 2007, 14:22
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.joing.common.desktopAPI;

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
    
    DeskDialog createDialog( DeskFrame owner );
    
    DeskDialog createDialog( DeskDialog owner );
    
    /**
     * Shows an exception in a JDialog.
     * 
     * @param exc     Exception to be shown
     * @param bReport <code>true</code> when error should be reported to TelcoDomo
     */
    void      showException( Throwable exc, String sTitle );
    /**
     * Shows a message in a dialog with title "Information" and an OK button.
     * 
     * @param sMessage Message to be shown
     */
    void      showMessage( String sMessage );
    /**
     * Shows a message in a dialog and an OK button.
     * 
     * @param sTitle   Dialog window title (if <code>null</code> or empty string passed, title will be empty)
     * @param sMessage Message to be shown
     */
    void      showMessage( String sTitle, String sMessage );
    /**
     * Shows a confimation modal dialog.
     * 
     * @param sTitle    Dialog window title (if <code>null</code> or empty string passed, title will be empty)
     * @param sMessage  Message to ask confirmation about
     * @return <code>true</code> if 'OK' button pressed, otherwise ('CANCEL' button or close dialog) return <code>false</code>
     */
    boolean   confirmDialog( String sTitle, String sMessage );
    /**
     * Shows a confimation modal dialog.
     * 
     * @param sTitle    Dialog window title (if <code>null</code> or empty string passed, title will be empty)
     * @param sMessage  Message to ask confirmation about
     * @return <code>true</code> if 'YES' button pressed, otherwise ('NO' button or close dialog) return <code>false</code>
     */
    boolean   yesOrNoDialog( String sTitle, String sMessage );
    /**
     * Asks for password and validates it agains the server.<br>
     * (For security reasons, the password is not stored locally, not even in 
     * memory).
     * @return <code>true</code> if password is correct.
     */
    boolean   askForPassword();
}