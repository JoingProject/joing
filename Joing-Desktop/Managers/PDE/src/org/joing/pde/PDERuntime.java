/*
 * Copyright (C) 2007, 2008 Join'g Team Members. All Rights Reserved.
 * Join'g Team Members are listed at project's home page. By the time of 
 * writting this at: https://joing.dev.java.net/servlets/ProjectMemberList.
 *
 * This file is part of Join'g project: www.joing.org
 *
 * GNU Classpath is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the free
 * Software Foundation; either version 3, or (at your option) any later version.
 * 
 * GNU Classpath is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * GNU Classpath; see the file COPYING.  If not, write to the Free Software 
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */
package org.joing.pde;

import org.joing.pde.swing.DialogAcceptCancel;
import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.Component;
import java.awt.Image;
import java.net.URL;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import org.joing.kernel.api.desktop.StandardImage;
import org.joing.kernel.api.desktop.DeskComponent;
import org.joing.kernel.api.desktop.StandardSound;
import org.joing.kernel.api.desktop.deskwidget.deskLauncher.DeskLauncher;
import org.joing.kernel.api.desktop.pane.DeskCanvas;
import org.joing.kernel.api.desktop.pane.DeskDialog;
import org.joing.kernel.api.desktop.pane.DeskFrame;
import org.joing.kernel.api.desktop.workarea.WorkArea;
import org.joing.pde.desktop.container.PDECanvas;
import org.joing.pde.desktop.container.PDEDialog;
import org.joing.pde.desktop.container.PDEFrame;
import org.joing.pde.desktop.deskwidget.deskLauncher.PDEDeskLauncher;
import org.joing.pde.media.images.ImagesUtil;
import org.joing.pde.media.sounds.SoundsUtil;
import org.joing.kernel.swingtools.JErrorPanel;

/**
 * The Runtime class.
 * <p>
 * Contains miscellaneous methods (all static) used intensively and extensively 
 * through all Join'g.
 * 
 * @author Francisco Morero Peyrona
 */
public final class PDERuntime implements org.joing.kernel.api.desktop.Runtime
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
        String    sFileName = ImagesUtil.getFileName4Icon( image );
        ImageIcon img       = new ImageIcon( ImagesUtil.class.getResource( sFileName ) );
        
        return img.getImage();
    }

    public Image getImage( StandardImage image, int width, int height )
    {
        String    sFileName = ImagesUtil.getFileName4Icon( image );
        ImageIcon img       = new ImageIcon( ImagesUtil.class.getResource( sFileName ) );

        if( img.getIconWidth() != width || img.getIconHeight() != height )
            img.setImage( img.getImage().getScaledInstance( width, height, Image.SCALE_SMOOTH ) );

        return img.getImage();
    }
    
    public void play( StandardSound sound )
    {
        URL url = SoundsUtil.getURL4Sound( sound );
        
        if( url != null )
        {
            AudioClip audio = Applet.newAudioClip( url );
                      audio.play();
        }
    }
    
    //------------------------------------------------------------------------//
    // Frecuent Dialogs
    //------------------------------------------------------------------------//
    
    public void showMessageDialog( String sTitle, String sMessage )
    {
        WorkArea workArea = org.joing.kernel.jvmm.RuntimeFactory.getPlatform().getDesktopManager().getDesktop().getActiveWorkArea();
        // TODO: Can't use showInternalMessageDialog(...) because bug ID = 6178755
        JOptionPane.showMessageDialog( (Component) workArea, sMessage, sTitle,
                                               JOptionPane.INFORMATION_MESSAGE );
    }
    
    public boolean showAcceptCancelDialog( String sTitle, DeskComponent content )
    {
        return showAcceptCancelDialog( sTitle, content, null, null );
    }
    
    public boolean showAcceptCancelDialog( String sTitle, DeskComponent content, String sAcceptText, String sCancelText )
    {
        DialogAcceptCancel dialog = new DialogAcceptCancel( sTitle, (JComponent) content );
                           dialog.setAcceptText( sAcceptText );
                           dialog.setCancelText( sCancelText );
        
        org.joing.kernel.jvmm.RuntimeFactory.getPlatform().getDesktopManager().getDesktop().getActiveWorkArea().add( dialog );
        
        return dialog.getExitStatus() == DialogAcceptCancel.ACCEPTED;
    }
    
    public boolean showYesNoDialog( String sTitle, String sMessage )
    {
        WorkArea workArea = org.joing.kernel.jvmm.RuntimeFactory.getPlatform().getDesktopManager().getDesktop().getActiveWorkArea();
        // TODO: Can't use showInternalMessageDialog(...) because bug ID = 6178755
        return JOptionPane.showConfirmDialog( 
                                     (Component) workArea, sMessage, sTitle,
                                     JOptionPane.YES_NO_OPTION ) == JOptionPane.YES_OPTION;
    }
    
    public void showException( Throwable exc, String sTitle )
    {
        if( sTitle == null || sTitle.length() == 0 )
            sTitle = "Error during execution";
        
        PDEDialog dialog = new PDEDialog();
                  dialog.setTitle( sTitle );
                  dialog.add( (DeskComponent) new JErrorPanel( exc ) );
                  
        org.joing.kernel.jvmm.RuntimeFactory.getPlatform().getDesktopManager().getDesktop().getActiveWorkArea().add( dialog );
        
        exc.printStackTrace();
    }
}