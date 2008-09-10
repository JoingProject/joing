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
package org.joing.images;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

/**
 * 
 * @author Francisco Morero Peyrona
 */
class StatusBar extends Box
{
    private JLabel lblHelp, lblSize, lblRotation, lblScale;

    public StatusBar()
    {
        super( BoxLayout.X_AXIS );
        
        Dimension dimScreen = Toolkit.getDefaultToolkit().getScreenSize();
        Font      font      = new Font( "Dialog", Font.PLAIN, 10 );

        lblHelp = new JLabel( " ", SwingConstants.CENTER );
        lblHelp.setFont( font );
        lblHelp.setPreferredSize( new Dimension( (int) (0.6 * dimScreen.width), 22 ) );
        lblHelp.setBorder( BorderFactory.createLoweredBevelBorder() );
        add( lblHelp, null );

        lblSize = new JLabel( " ", SwingConstants.CENTER );
        lblSize.setFont( font );
        lblSize.setPreferredSize( new Dimension( (int) (0.2 * dimScreen.width), 22 ) );
        lblSize.setBorder( BorderFactory.createLoweredBevelBorder() );
        add( lblSize, null );
        
        lblScale = new JLabel( " ", SwingConstants.CENTER );
        lblScale.setFont( font );
        lblScale.setPreferredSize( new Dimension( (int) (0.1 * dimScreen.width), 22 ) );
        lblScale.setBorder( BorderFactory.createLoweredBevelBorder() );
        add( lblScale, null );
        
        lblRotation = new JLabel( " ", SwingConstants.CENTER );
        lblRotation.setFont( font );
        lblRotation.setPreferredSize( new Dimension( (int) (0.1 * dimScreen.width), 22 ) );
        lblRotation.setBorder( BorderFactory.createLoweredBevelBorder() );
        add( lblRotation, null );
        
        setHelp( null );
        setOriginalSize( null );
    }
    
    void setHelp( JComponent comp )
    {
        String sHelp = " ";
        
        if( comp != null )
            sHelp += comp.getToolTipText();
        
        lblHelp.setText( sHelp );
    }
    
    void setOriginalSize( WImage image )
    {
        Dimension dimSize = new Dimension();
        
        if( image != null )
            dimSize = image.getOriginalSize();
        
        lblSize.setText(  " Size: "+ dimSize.width +" x "+ dimSize.height +" Pixels" );
    }
    
    void setRotation( WImage image )
    {
        int nRotation = 0;
        
        if( image != null )
            nRotation = image.getRotation();
        
        lblRotation.setText( " Rotation: "+ nRotation + "º" );
    }
    
    void setScale( WImage image )
    {
        int nScale = 0;
        
        if( image != null )
            nScale = image.getZoom();
        
        lblScale.setText( " Scale: "+ nScale +"%" );
    }
}