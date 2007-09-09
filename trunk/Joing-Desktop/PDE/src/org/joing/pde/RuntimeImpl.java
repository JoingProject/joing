/*
 * Copyright (C) Francisco Morero Peyrona. All rights reserved.
 *
 * This software is published under the terms of Open Source
 * License version 1.1, a copy of which has been included with this
 * distribution in the License.txt file.
 */
package org.joing.pde;

import java.applet.Applet;
import java.awt.Cursor;
import java.awt.Image;
import java.net.URL;
import java.security.InvalidParameterException;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import org.joing.pde.desktop.workarea.containers.JoingDialog;

/**
 * The Runtime class.
 * <p>
 * Contains miscellaneous methods (all static) used intensively and extensively through all WebPC.
 * 
 * @author Francisco Morero Peyrona
 */
public final class RuntimeImpl 
       implements org.joing.Runtime
{
    private static RuntimeImpl instance  = null;

    //-------------------------------------------------------------------------//
    
    public static RuntimeImpl getRuntime()
    {
        if( instance == null )
        {
            synchronized( RuntimeImpl.class )
            {
                if( instance == null )
                     instance = new RuntimeImpl();
            }
        }
        
        return instance;
    }
    
    private RuntimeImpl()
    {
    }
    
    //-------------------------------------------------------------------------//
    // Show exception and messages
    //-------------------------------------------------------------------------//
    
    /**
     * Shows an exception in a JDialog.
     * 
     * @param exc     Exception to be shown
     * @param bReport <code>true</code> when error should be reported to TelcoDomo
     */
    public void showException( Throwable exc, String sTitle )
    {
        exc.printStackTrace();   // TODO: quitarlo en la version final

        /*  TODO hacer q JShowException herede de DesktopDialog y q actue en consecuencia
        JShowException dialog = new JShowException( sTitle, exc );
                       dialog.setLocationRelativeTo( getDesktop() );
                       dialog.setVisible( true );*/
    }

    /**
     * Shows a message in a dialog with title "Information" and an OK button.
     * 
     * @param sMessage Message to be shown
     */
    public void showMessage( String sMessage )
    {
        showMessage( "Information", sMessage );
    }

    /**
     * Shows a message in a dialog and an OK button.
     * 
     * @param sTitle   Dialog window title (if <code>null</code> or empty string passed, title will be empty)
     * @param sMessage Message to be shown
     */
    public void showMessage( String sTitle, String sMessage )
    {
        Cursor cursor = getCursor();
        
        setCursor( Cursor.DEFAULT_CURSOR );

        JoingDialog dialog = new JoingDialog( sTitle, new JOptionPane( sMessage, JOptionPane.INFORMATION_MESSAGE ) );
                    dialog.pack();
                    dialog.setVisible( true );
        
        setCursor( cursor.getType() );
    }

    /**
     * Shows a confimation modal dialog.
     * 
     * @param sTitle    Dialog window title (if <code>null</code> or empty string passed, title will be empty)
     * @param sMessage  Message to ask confirmation about
     * @return <code>true</code> if 'OK' button pressed, otherwise ('CANCEL' button or close dialog) return <code>false</code>
     */
    public boolean confirmDialog( String sTitle, String sMessage )
    {
        return commonDialog( sTitle, sMessage, JOptionPane.OK_CANCEL_OPTION );
    }

    /**
     * Shows a confimation modal dialog.
     * 
     * @param sTitle    Dialog window title (if <code>null</code> or empty string passed, title will be empty)
     * @param sMessage  Message to ask confirmation about
     * @return <code>true</code> if 'YES' button pressed, otherwise ('NO' button or close dialog) return <code>false</code>
     */
    public boolean yesOrNoDialog( String sTitle, String sMessage )
    {
        return commonDialog( sTitle, sMessage, JOptionPane.YES_NO_OPTION );
    }
    
    // This private is used by confirmDialog() and yesOrNoDialog()
    private boolean commonDialog( String sTitle, String sMessage, int nOptions )
    {
        Cursor cursor = getCursor();
        
        setCursor( Cursor.DEFAULT_CURSOR );

        JoingDialog dialog = new JoingDialog( sTitle, new JOptionPane( sMessage, nOptions, JOptionPane.YES_NO_OPTION ) );
                    dialog.pack();
                    dialog.setVisible( true );

        setCursor( cursor.getType() );
        
        int nOption = (Integer) dialog.getValue();
        
        return ((nOption == JOptionPane.OK_OPTION) || (nOption == JOptionPane.YES_OPTION));
    }
    
    //-------------------------------------------------------------------------//
    // Local resources
    //-------------------------------------------------------------------------// 
    
    public ImageIcon getIcon( Object invoker, String sName )
    {
        URL       url  = null;
        ImageIcon icon = null;
        
        if( invoker != null && sName != null )
        {
            url = invoker.getClass().getResource( sName );
            
            if( url != null )
                icon = new ImageIcon( url );
        }
        
        if( icon == null )
        {
            url  = RuntimeImpl.class.getResource( "images/no_image.png" );
            icon = new ImageIcon( url );
        }
        
        return icon;
    }
    
    public ImageIcon getIcon( Object o, String s, int nWidth, int nHeight )
    {
        ImageIcon icon = getIcon( o, s );
        
        if( icon.getIconWidth() != nWidth || icon.getIconHeight() != nHeight )
            icon.setImage( icon.getImage().getScaledInstance( nWidth, nHeight, Image.SCALE_SMOOTH ) );
        
        return icon;
    }
    
    public void play( URL urlSound )
    {
        Applet.newAudioClip( urlSound ).play();
    }
    
    //-------------------------------------------------------------------------//
    // Miscellaneous
    //-------------------------------------------------------------------------//
    
    /**
     * Shortcut for Client.getInstance().getDesktop().getCursor();
     */
    public Cursor getCursor()
    {
        ///return Client.getClient().getDesktop().getCursor();
        return null;
    }
    
    /**
     * Shortcut for Client.getInstance().getDesktop().setCursor( new Cursor( nCursorType ) );
     */
    public void setCursor( int nCursorType )
    {
        ///Client.getClient().getDesktop().setCursor( new Cursor( nCursorType ) );
    }
}
