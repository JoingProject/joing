/*
 * Copyright (C) Francisco Morero Peyrona. All rights reserved.
 *
 * This software is published under the terms of Open Source
 * License version 1.1, a copy of which has been included with this
 * distribution in the License.txt file.
 */
package org.joing.pde;

import java.awt.Component;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import org.joing.common.desktopAPI.DesktopManagerFactory;
import org.joing.common.desktopAPI.deskwidget.deskLauncher.DeskLauncher;
import org.joing.common.desktopAPI.pane.DeskCanvas;
import org.joing.common.desktopAPI.pane.DeskDialog;
import org.joing.common.desktopAPI.pane.DeskFrame;
import org.joing.common.desktopAPI.workarea.WorkArea;
import org.joing.pde.desktop.container.PDECanvas;
import org.joing.pde.desktop.container.PDEDialog;
import org.joing.pde.desktop.container.PDEFrame;
import org.joing.pde.desktop.deskwidget.deskLauncher.PDEDeskLauncher;
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
    
    public DeskLauncher createLauncher()
    {
        return new PDEDeskLauncher();
    }
    
    public DeskFrame createFrame()
    {
        return new PDEFrame();
    }
    
    public DeskDialog createDialog()
    {
        return new PDEDialog();
    }
    
    //------------------------------------------------------------------------//
    // Images and icons
    //------------------------------------------------------------------------//
    
    public byte[] getImage( String spec )
    {
        return bufferedImage2ByteArray( getBufferedImage( spec ) );
    }

    public byte[] getStandardImage( String name )
    {
        URL url = ImagesFactory.class.getResource( name + ".png" );
        return getImage( url.toString() );
    }
    
    public byte[] getImage( String spec, int width, int height )
    {
        BufferedImage bi  = getBufferedImage( spec );
        byte[]        ret = null;
        
        if( bi.getWidth() != width || bi.getHeight() != height )
        {
            Image image = bi.getScaledInstance( width, height, Image.SCALE_SMOOTH );
            
        }
        
        return bufferedImage2ByteArray( bi );
    }

    public byte[] getStandardImage( String name, int width, int height )
    {
        URL url = ImagesFactory.class.getResource( name + ".png" );
        return getImage( url.toString(), width, height );
    }
    
    private BufferedImage getBufferedImage( String url )
    {
        BufferedImage bi = null;
        
        try
        {
            bi = ImageIO.read( new URL( url ) );    
        }
        catch( Exception exc )
        {
            // Nothing to do
        }
        
        return bi;
    }
    
    private byte[] bufferedImage2ByteArray( BufferedImage bi )
    {
        byte[] image = null;
        
        if( bi != null )
        {
            try
            {
                ByteArrayOutputStream baos = new ByteArrayOutputStream( 1024 * 64 );
                ImageIO.write( bi, "png", baos );
                baos.flush();
                image = baos.toByteArray();
                baos.close();
            }
            catch( IOException exc )
            {
                // Nothing to do
            }
        }
        
        return image;
    }
    
    //------------------------------------------------------------------------//
    // Frecuent Dialogs
    //------------------------------------------------------------------------//
    
    public void showMessageDialog( String sMessage )
    {
        WorkArea workArea = DesktopManagerFactory.getDM().getDesktop().getActiveWorkArea();
        
        JOptionPane.showInternalMessageDialog( (Component) workArea, sMessage );
    }
    
    public void showMessageDialog( String sTitle, String sMessage )
    {
        WorkArea workArea = DesktopManagerFactory.getDM().getDesktop().getActiveWorkArea();
        
        JOptionPane.showInternalMessageDialog( (Component) workArea, sMessage, sTitle,
                                               JOptionPane.INFORMATION_MESSAGE );
    }
    
    public boolean showAcceptCancelDialog( String sTitle, String sMessage )
    {
        WorkArea workArea = DesktopManagerFactory.getDM().getDesktop().getActiveWorkArea();
        
        return JOptionPane.showInternalConfirmDialog( 
                                     (Component) workArea, sMessage, sTitle,
                                     JOptionPane.OK_CANCEL_OPTION ) == JOptionPane.OK_OPTION;
    }
    
    public boolean showYesNoDialog( String sTitle, String sMessage )
    {
        WorkArea workArea = DesktopManagerFactory.getDM().getDesktop().getActiveWorkArea();
        
        return JOptionPane.showInternalConfirmDialog( 
                                     (Component) workArea, sMessage, sTitle,
                                     JOptionPane.YES_NO_OPTION ) == JOptionPane.YES_OPTION;
    }
    
    public boolean askForPasswordDialog()
    {// TODO: cambiar el JTextField por un JPasswordField
        String sPassword = JOptionPane.showInputDialog( null, "", "Enter password", 
                                                        JOptionPane.QUESTION_MESSAGE );
        
        // TODO: comprobar la password (mandarla al servidor y que él si es válide)
        return sPassword != null && sPassword.trim().length() > 0;
    }
}