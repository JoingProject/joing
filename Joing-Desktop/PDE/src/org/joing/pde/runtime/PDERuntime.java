/*
 * Copyright (C) Francisco Morero Peyrona. All rights reserved.
 *
 * This software is published under the terms of Open Source
 * License version 1.1, a copy of which has been included with this
 * distribution in the License.txt file.
 */
package org.joing.pde.runtime;

import java.applet.Applet;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.net.URL;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import org.joing.api.desktop.workarea.WorkArea;
import org.joing.jvmm.Platform;
import org.joing.pde.PDEManager;
import org.joing.pde.desktop.PDEDesktop;
import org.joing.pde.desktop.workarea.PDEWorkArea;
import org.joing.pde.desktop.container.PDEFrame;
import org.joing.pde.misce.images.ImagesFactory;
import org.joing.runtime.bridge2server.Bridge2Server;

/**
 * The Runtime class.
 * <p>
 * Contains miscellaneous methods (all static) used intensively and extensively through all WebPC.
 * 
 * @author Francisco Morero Peyrona
 */
public final class PDERuntime implements org.joing.api.Runtime
{
    private static PDERuntime instance = null;

    //------------------------------------------------------------------------//
    
    public static PDERuntime getRuntime()
    {
        if( instance == null )
        {
            synchronized( PDERuntime.class )
            {
                if( instance == null )
                     instance = new PDERuntime();
            }
        }
        
        return instance;
    }
    
    private PDERuntime()
    {
    }
    
    //------------------------------------------------------------------------//
    
    /**
     * Convenience method to add a PDEFrame to the default work area.
     * It also makes: pack(), center() and setVisible( true ).
     */
    public void add( final PDEFrame frame )
    {
        SwingUtilities.invokeLater( new Runnable()
        {
            public void run()
            {
                // First, add to desktop
                getDesktopManager().getDesktop().getActiveWorkArea().add( frame );

                frame.center();
                frame.setVisible( true );
            }
        } );
    }
    
    public void remove( Component comp )
    {
        List<WorkArea> lstWA = getDesktopManager().getDesktop().getWorkAreas();
        
        for( WorkArea wa : lstWA )
        {
            Component[] ac  = ((PDEWorkArea) wa).getComponents();
            
            for( int n = 0; n < ac.length; n++ )
            {
                if( ac[n] == comp )
                {
                    wa.remove( comp );
                    break;
                }
            }
        }
    }
    
    //------------------------------------------------------------------------//
    // Show exception and messages
    //------------------------------------------------------------------------//
    
    /**
     * Shows an exception in a JDialog.
     * 
     * @param exc     Exception to be shown
     * @param bReport <code>true</code> when error should be reported to TelcoDomo
     */
    public void showException( Throwable exc, String sTitle )
    {
        exc.printStackTrace();   // TODO: quitarlo en la version final 
                                 //       y hacer una ventana para mostrar exc -->
        /*
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
        PDEWorkArea workArea = (PDEWorkArea) getDesktopManager().getDesktop().getActiveWorkArea();
        
        JOptionPane.showInternalMessageDialog( workArea, sMessage );
    }

    /**
     * Shows a message in a dialog and an OK button.
     * 
     * @param sTitle   Dialog window title (if <code>null</code> or empty string passed, title will be empty)
     * @param sMessage Message to be shown
     */
    public void showMessage( String sTitle, String sMessage )
    {
        PDEWorkArea workArea = (PDEWorkArea) getDesktopManager().getDesktop().getActiveWorkArea();
        
        JOptionPane.showInternalMessageDialog( workArea, sMessage, sTitle,
                                               JOptionPane.INFORMATION_MESSAGE );
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
        PDEWorkArea workArea = (PDEWorkArea) getDesktopManager().getDesktop().getActiveWorkArea();
        
        return JOptionPane.showInternalConfirmDialog( 
                                     workArea, sMessage, sTitle,
                                     JOptionPane.OK_CANCEL_OPTION ) == JOptionPane.OK_OPTION;
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
        PDEWorkArea workArea = (PDEWorkArea) getDesktopManager().getDesktop().getActiveWorkArea();
        
        return JOptionPane.showInternalConfirmDialog( 
                                     workArea, sMessage, sTitle,
                                     JOptionPane.YES_NO_OPTION ) == JOptionPane.YES_OPTION;
    }
    
    /**
     * Asks for password and validates it agains the server.
     * 
     * @return <code>true</code> if password is correct.
     */
    public boolean askForPassword()
    {// TODO: cambiar el JTextField por un JPasswordField
        String sPassword = JOptionPane.showInputDialog( null, "", "Enter password", 
                                                        JOptionPane.QUESTION_MESSAGE );
        
        // TODO: comprobar la password (mandarla al servidor y que él la valide)
        return sPassword != null && sPassword.trim().length() > 0;
    }
    
    //------------------------------------------------------------------------//
    // Local resources
    //------------------------------------------------------------------------// 
    
    /**
     * 
     * @param invoker The class to be used as base to find the files. If null,
     *                <code>ImagesFactory</code> will be used (common images).
     * @param sName   Name of file with its extension.
     * @return        The icon or an standard one if teh requested was not found
     */
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
    // Cursors
    //------------------------------------------------------------------------//
    
    /**
     * Shortcut for Client.getInstance().getDesktop().getCursor();
     */
    public Cursor getCursor()
    {
        PDEDesktop desktop = (PDEDesktop) getDesktopManager().getDesktop();
        
        return desktop.getCursor();
    }
    
    /**
     * Shortcut for Client.getInstance().getDesktop().setCursor( new Cursor( nCursorType ) );
     */
    public void setCursor( int nCursorType )
    {
        PDEDesktop desktop = (PDEDesktop) getDesktopManager().getDesktop();
                   desktop.setCursor( new Cursor( nCursorType ) );
    }
    
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
    
    /**
     * Sets the cursor by first creating the custom image.
     * 
     * @param sImageName
     * @param pHotSpot
     * @param sName
     */
    public void setCursor( String sImageName, Point pHotSpot, String sName )
    {
        PDEDesktop desktop = (PDEDesktop) getDesktopManager().getDesktop();
                   desktop.setCursor( createCursor( sImageName, pHotSpot, sName ) );
    }
    
    //------------------------------------------------------------------------//
    // Miscellaneous
    //------------------------------------------------------------------------//
    
    
    //------------------------------------------------------------------------//
    // Esto lo necesito para ejecutar PDE de modo autónomo (sin que lo lance
    // Joing-Client)
    //------------------------------------------------------------------------//
    
    public final static String PDE_AUTONOMO = "PDE_AUTONOMO";
    private PDEManager  pdeManager = null;
    
    public void setDesktopManager( PDEManager mgr )
    {
        if( this.pdeManager == null )   // Previene de asignarlo varias veces
            this.pdeManager = mgr;
    }
    
    public PDEManager getDesktopManager()
    {
        if( System.getProperty( PDE_AUTONOMO ) != null )
            return pdeManager;
        else
            return (PDEManager) Platform.getInstance().getDesktopManager();
    }
    
    public Bridge2Server getBridge()
    {
        if( System.getProperty( PDE_AUTONOMO ) != null )
            return Bridge2Server.getInstance();
        else
            return Platform.getInstance().getBridge();
    }
    
    public void shutdown()
    {
        if( System.getProperty( PDE_AUTONOMO ) != null )
            System.exit( 0 );
        else
            Platform.getInstance().shutdown();
    }
    
    public void halt()
    {
        if( System.getProperty( PDE_AUTONOMO ) != null )
            System.exit( 0 );
        else
            Platform.getInstance().halt();
    }
}