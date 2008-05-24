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
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import org.joing.common.desktopAPI.StandardImage;
import org.joing.common.desktopAPI.DeskComponent;
import org.joing.common.desktopAPI.StandardSound;
import org.joing.common.desktopAPI.deskwidget.deskLauncher.DeskLauncher;
import org.joing.common.desktopAPI.pane.DeskCanvas;
import org.joing.common.desktopAPI.pane.DeskDialog;
import org.joing.common.desktopAPI.pane.DeskFrame;
import org.joing.common.desktopAPI.workarea.WorkArea;
import org.joing.pde.desktop.container.PDECanvas;
import org.joing.pde.desktop.container.PDEDialog;
import org.joing.pde.desktop.container.PDEFrame;
import org.joing.pde.desktop.deskwidget.deskLauncher.PDEDeskLauncher;
import org.joing.pde.media.images.CommonImagesUtil;

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
    // Images and sounds
    //------------------------------------------------------------------------//
    
    public Image getImage( StandardImage image )
    {
        String    sFileName = CommonImagesUtil.getFileName4Icon( image );
        ImageIcon img       = new ImageIcon( CommonImagesUtil.class.getResource( sFileName ) );
        
        return img.getImage();
    }

    public Image getImage( StandardImage image, int width, int height )
    {
        String    sFileName = CommonImagesUtil.getFileName4Icon( image );
        ImageIcon img       = new ImageIcon( CommonImagesUtil.class.getResource( sFileName ) );

        if( img.getIconWidth() != width || img.getIconHeight() != height )
            img.setImage( img.getImage().getScaledInstance( width, height, Image.SCALE_SMOOTH ) );

        return img.getImage();
    }
    
    public void play( StandardSound sound )
    {
        // FIXME: hacerlo
    }
    
    //------------------------------------------------------------------------//
    // Frecuent Dialogs
    //------------------------------------------------------------------------//
    
    public void showMessageDialog( String sTitle, String sMessage )
    {
        WorkArea workArea = PDEUtilities.getDesktopManager().getDesktop().getActiveWorkArea();
        // TODO: Can't use showInternalMessageDialog(...) because bug ID = 6178755
        JOptionPane.showMessageDialog( (Component) workArea, sMessage, sTitle,
                                               JOptionPane.INFORMATION_MESSAGE );
    }
    
    public boolean showAcceptCancelDialog( String sTitle, DeskComponent panel )
    {
        return PDEUtilities.showBasicDialog( sTitle, (JComponent) panel );
    }
    
    public boolean showYesNoDialog( String sTitle, String sMessage )
    {
        WorkArea workArea = PDEUtilities.getDesktopManager().getDesktop().getActiveWorkArea();
        // TODO: Can't use showInternalMessageDialog(...) because bug ID = 6178755
        return JOptionPane.showConfirmDialog( 
                                     (Component) workArea, sMessage, sTitle,
                                     JOptionPane.YES_NO_OPTION ) == JOptionPane.YES_OPTION;
    }
}