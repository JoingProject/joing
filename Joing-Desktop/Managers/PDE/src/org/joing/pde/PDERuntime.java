/*
 * Copyright (C) Francisco Morero Peyrona. All rights reserved.
 *
 * This software is published under the terms of Open Source
 * License version 1.1, a copy of which has been included with this
 * distribution in the License.txt file.
 */
package org.joing.pde;

import java.applet.Applet;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.net.URL;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import org.joing.common.desktopAPI.DesktopManagerFactory;
import org.joing.common.desktopAPI.pane.DeskCanvas;
import org.joing.common.desktopAPI.pane.DeskDialog;
import org.joing.common.desktopAPI.pane.DeskFrame;
import org.joing.common.desktopAPI.workarea.WorkArea;
import org.joing.pde.desktop.container.PDECanvas;
import org.joing.pde.desktop.container.PDEDialog;
import org.joing.pde.desktop.container.PDEFrame;
import org.joing.pde.misce.images.ImagesFactory;

/**
 * The Runtime class.
 * <p>
 * Contains miscellaneous methods (all static) used intensively and extensively through all WebPC.
 * 
 * @author Francisco Morero Peyrona
 */
public final class PDERuntime implements org.joing.common.desktopAPI.Runtime
{
    // Only PDEManager has an instance of PDERuntime
    PDERuntime()
    {
    }
    
    //------------------------------------------------------------------------//
    // Create Containers
    //------------------------------------------------------------------------//
    
    public DeskCanvas createCanvas()
    {
        return new PDECanvas();
    }
    
    public DeskFrame createFrame()
    {
        return new PDEFrame();
    }
    
    public DeskDialog createDialog( DeskFrame owner )
    {
        return new PDEDialog( owner );
    }
    
    public DeskDialog createDialog( DeskDialog owner )
    {
        return new PDEDialog( owner );
    }
    
    //------------------------------------------------------------------------//
    // Show exception and messages
    //------------------------------------------------------------------------//
    
    public void showException( Throwable exc, String sTitle )
    {
        exc.printStackTrace();   // TODO: quitarlo en la version final 
                                 //       y hacer una ventana para mostrar exc -->
        /*
        JShowException dialog = new JShowException( sTitle, exc );
                       dialog.setLocationRelativeTo( getDesktop() );
                       dialog.setVisible( true );*/
    }
    
    public void showMessage( String sMessage )
    {
        WorkArea workArea = DesktopManagerFactory.getDM().getDesktop().getActiveWorkArea();
        
        JOptionPane.showInternalMessageDialog( (Component) workArea, sMessage );
    }
    
    public void showMessage( String sTitle, String sMessage )
    {
        WorkArea workArea = DesktopManagerFactory.getDM().getDesktop().getActiveWorkArea();
        
        JOptionPane.showInternalMessageDialog( (Component) workArea, sMessage, sTitle,
                                               JOptionPane.INFORMATION_MESSAGE );
    }
    
    public boolean confirmDialog( String sTitle, String sMessage )
    {
        WorkArea workArea = DesktopManagerFactory.getDM().getDesktop().getActiveWorkArea();
        
        return JOptionPane.showInternalConfirmDialog( 
                                     (Component) workArea, sMessage, sTitle,
                                     JOptionPane.OK_CANCEL_OPTION ) == JOptionPane.OK_OPTION;
    }
    
    public boolean yesOrNoDialog( String sTitle, String sMessage )
    {
        WorkArea workArea = DesktopManagerFactory.getDM().getDesktop().getActiveWorkArea();
        
        return JOptionPane.showInternalConfirmDialog( 
                                     (Component) workArea, sMessage, sTitle,
                                     JOptionPane.YES_NO_OPTION ) == JOptionPane.YES_OPTION;
    }
    
    public boolean askForPassword()
    {// TODO: cambiar el JTextField por un JPasswordField
        String sPassword = JOptionPane.showInputDialog( null, "", "Enter password", 
                                                        JOptionPane.QUESTION_MESSAGE );
        
        // TODO: comprobar la password (mandarla al servidor y que él si es válide)
        return sPassword != null && sPassword.trim().length() > 0;
    }
    
    //------------------------------------------------------------------------//
    // Local resources
    //------------------------------------------------------------------------// 
    
    public ImageIcon getIcon( Object invoker, String sName )
    {
        URL       url  = null;
        ImageIcon icon = null;
        
        if( sName != null && sName.length() > 0 )
        {
            if( invoker == null )
            {
                url = ImagesFactory.class.getResource( sName );
            }
            else
            {
                if( invoker instanceof Class )
                    url = ((Class) invoker).getResource( sName );
                else
                    url = invoker.getClass().getResource( sName );
            }
            
            if( url != null )
                icon = new ImageIcon( url );
        }
        
        if( icon == null )
        {
            url  = ImagesFactory.class.getResource( "no_image.png" );
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
    
    //------------------------------------------------------------------------//
    // Extra functions not defined in Runtime Interface
    // They are only used internally by PDE but can't be used by portable
    // (cross-desktops) Joing applications.
    //------------------------------------------------------------------------//
    
    /**
     * Creates a custom cursor asuming that the image is under cursor 
     * application directory.
     * 
     * @param sImageName
     * @param pHotSpot
     * @param sName
     * @return
     */
    public Cursor createCursor( String sImageName, Point pHotSpot, String sName )
    {
        if( sImageName.indexOf( '.' ) == -1 )
            sImageName += ".png";
        
        Image image = getIcon( null, "cursors/"+ sImageName ).getImage();
        
        return Toolkit.getDefaultToolkit().createCustomCursor( image, pHotSpot, sName );        
    }
}