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
package org.joing.pde.desktop.workarea;

import java.awt.BorderLayout;
import java.awt.Component;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.joing.kernel.api.desktop.workarea.Wallpaper;

/**
 * A Swing component (JPanel in this case) that uses the Wallpaper interface
 * implementation (PDEWallPaper in this case) to change its own behaviour.
 * @author Francisco Morero Peyrona
 */
class PDEWallpaperComponent extends JPanel implements PropertyChangeListener
{
    private PDEWallpaper wp;
    
    //------------------------------------------------------------------------//
    
    PDEWallpaperComponent()
    {
        this( null );
    }
    
    PDEWallpaperComponent( Wallpaper wp )
    {
        setOpaque( false );
        setWallPaper( wp );
    }
    
    Wallpaper getWallpaper()
    {
        return wp;
    }
    
    void setWallPaper( Wallpaper wp )
    {
        this.wp = (PDEWallpaper) wp;
        
        if( this.wp != null )
        {
            this.wp.addPropertyChangeListener( this );
            changeMode();
        }
    }
    
    public void propertyChange( PropertyChangeEvent evt )
    {
        if( "image".equals( evt.getPropertyName() ) )
            changeImage();
        else if( "mode".equals( evt.getPropertyName() ) )
            changeMode();
    }
    
    //------------------------------------------------------------------------//
    
    private void changeMode()
    {
        removeAll();
        
        if( wp.getMode() == Wallpaper.Mode.CENTER )
        {
            JLabel lbl = new JLabel( new ImageIcon( wp.getImage() ) );
                   lbl.setHorizontalAlignment( JLabel.CENTER );
                   
            setLayout( new BorderLayout() );
            add( lbl, BorderLayout.CENTER );
        }
        else if( wp.getMode() == Wallpaper.Mode.EXPANDED )
        {
            // TODO: hacerlo
        }
        else if( wp.getMode() == Wallpaper.Mode.TILES )
        {
            // TODO: hacerlo
        }
        
        invalidate();
        repaint();
    }
    
    private void changeImage()
    {
        Component[] aComp = getComponents();
        
        for( int n = 0; n < aComp.length; n++ )
        {
            if( aComp[n] instanceof JLabel )
                ((JLabel) aComp[n]).setIcon( new ImageIcon( wp.getImage() ) );
        }
    }
}