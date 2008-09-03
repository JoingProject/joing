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
package org.joing.pde.desktop;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import org.joing.kernel.api.desktop.StandardImage;
import org.joing.pde.desktop.container.PDECanvas;

/**
 * An information panel indicating that a connection with server is in progress.
 * 
 * Note: Package scope to be used only by PDEManager (in fact it should be an
 * innner class of PDEManager but I prefer to place it in a separate file).
 * 
 * @author Francisco Morero Peyrona.
 */
class NotificationPanel extends PDECanvas
{
    public NotificationPanel()
    {
        this( null );
    }
    
    public NotificationPanel( String sMessage )
    {
        this( sMessage, null );
    }
    
    public NotificationPanel( String sMessage, Image icon )
    {
        JLabel       label    = new JLabel();
        JProgressBar progress = new JProgressBar();
                     progress.setIndeterminate( true );
                     
        if( sMessage != null )
        {
            label.setText( sMessage );
            label.setHorizontalTextPosition( JLabel.RIGHT );
            label.setVerticalTextPosition( JLabel.CENTER );
        }
        
        if( icon == null )
            icon = org.joing.kernel.jvmm.RuntimeFactory.getPlatform().getDesktopManager().getRuntime().getImage( StandardImage.NOTIFICATION );
        
        label.setIcon( new ImageIcon( icon ) );
        
        JPanel panel = new JPanel( new BorderLayout( 8,6 ) );
               panel.setBorder( new CompoundBorder( new LineBorder( Color.darkGray, 2 ), new EmptyBorder( 3,5,3,5 ) ) );
               panel.add( label   , BorderLayout.CENTER );
               panel.add( progress, BorderLayout.SOUTH  );
        add( panel );
    }
}