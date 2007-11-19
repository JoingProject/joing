/*
 * Runtime.java
 *
 * Created on 14 de febrero de 2007, 14:22
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.joing.common.desktopAPI;

import java.net.URL;
import javax.swing.ImageIcon;

/**
 *
 * @author fmorero
 */
public interface Runtime
{
    /**
     * Shows an exception in a JDialog.
     * 
     * @param exc     Exception to be shown
     * @param bReport <code>true</code> when error should be reported to TelcoDomo
     */
    public void      showException( Throwable exc, String sTitle );
    /**
     * Shows a message in a dialog with title "Information" and an OK button.
     * 
     * @param sMessage Message to be shown
     */
    public void      showMessage( String sMessage );
    /**
     * Shows a message in a dialog and an OK button.
     * 
     * @param sTitle   Dialog window title (if <code>null</code> or empty string passed, title will be empty)
     * @param sMessage Message to be shown
     */
    public void      showMessage( String sTitle, String sMessage );
    /**
     * Shows a confimation modal dialog.
     * 
     * @param sTitle    Dialog window title (if <code>null</code> or empty string passed, title will be empty)
     * @param sMessage  Message to ask confirmation about
     * @return <code>true</code> if 'OK' button pressed, otherwise ('CANCEL' button or close dialog) return <code>false</code>
     */
    public boolean   confirmDialog( String sTitle, String sMessage );
    /**
     * Shows a confimation modal dialog.
     * 
     * @param sTitle    Dialog window title (if <code>null</code> or empty string passed, title will be empty)
     * @param sMessage  Message to ask confirmation about
     * @return <code>true</code> if 'YES' button pressed, otherwise ('NO' button or close dialog) return <code>false</code>
     */
    public boolean   yesOrNoDialog( String sTitle, String sMessage );
    /**
     * Asks for password and validates it agains the server.<br>
     * (For security reasons, the password is not stored locally, not even in 
     * memory).
     * @return <code>true</code> if password is correct.
     */
    public boolean   askForPassword();
    /**
     * Return an icon which location is relative to passed class.
     * 
     * @param invoker The class to be used as base to find the files. If null,
     *                <code>ImagesFactory</code> will be used (common images).
     * @param sName   Name of file with its extension.
     * @return        The icon or an standard one if teh requested was not found
     */
    public ImageIcon getIcon( Object invokerClass, String s );
    /**
     * Return an icon (with specific dimension) which location is relative to 
     * passed class.
     * 
     * @param invoker The class to be used as base to find the files. If null,
     *                <code>ImagesFactory</code> will be used (common images).
     * @param sName   Name of file with its extension.
     * @return        The icon or an standard one if teh requested was not found
     */
    public ImageIcon getIcon( Object invokerClass, String s, int nWidth, int nHeight ); 
    /**
     * Play a sound stored in an URL.
     * 
     * @param urlSound
     */
    public void      play( URL urlSound );
}