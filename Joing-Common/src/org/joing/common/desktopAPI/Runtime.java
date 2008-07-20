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
 * @author Francisco Morero Peyrona
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
     * Shows a dialog with buttons [Accept] and [Cancel] and optionally chanes
     * the text for these buttons.
     * 
     * @param sTitle Dialog title. If null an empty title will be used.
     * @param content Panel to be shown.
     * @param sAcceptText New text to be shown in Accept button (null will not change)
     * @param sCancelText New text to be shown in Cancel button (null will not change)
     * @return true if dialog was closed via [Accpet] button and false otherwise.
     */
    boolean showBasicDialog( String sTitle, DeskComponent content, String sAcceptText, String sCancelText );
    /**
     * Shows a confimation modal dialog.
     * 
     * @param sTitle    Dialog window title (if <code>null</code> or empty string passed, title will be empty)
     * @param sMessage  Message to ask confirmation about
     * @return <code>true</code> if 'YES' button pressed, otherwise ('NO' button or close dialog) return <code>false</code>
     */
    boolean showYesNoDialog( String sTitle, String sMessage );
    
    /**
     * Shows an error in a dialog.
     * 
     * @param exc Error to show
     * @param sTitle Optional dialog frame title
     */
    void showException( Throwable exc, String sTitle );
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