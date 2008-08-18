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

import java.awt.Image;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import javax.swing.ImageIcon;
import org.joing.common.desktopAPI.workarea.Wallpaper;

/**
 * PDE Implementation of Wallpaper interface.
 * 
 * @author Francisco Morero Peyrona
 */
public class PDEWallpaper implements Wallpaper
{
    private Wallpaper.Mode mode;
    private ImageIcon      image;
    
    private PropertyChangeSupport pcs;
    
    //------------------------------------------------------------------------//
    
    public PDEWallpaper()
    {
        mode  = Wallpaper.Mode.CENTER;
        image = null;
        pcs   = new PropertyChangeSupport( this );
    }
    
    //------------------------------------------------------------------------//
    // Interface implementation
    
    public Image getImage()
    {
        return image.getImage();
    }
    
    public void setImage( Image newImage )
    {
        Image oldImage = this.image.getImage();
        
        this.image = new ImageIcon( newImage );
        
        pcs.firePropertyChange( "image", oldImage, newImage );
    }
    
    public Mode getMode()
    {
        return mode;
    }
    
    public void setMode( Wallpaper.Mode mode )
    {
        Wallpaper.Mode oldMode = this.mode;
        
        this.mode = mode;
        
        pcs.firePropertyChange( "mode", oldMode, this.mode );
    }
    
    //------------------------------------------------------------------------//
    // Special functions for PDE
    
    public void addPropertyChangeListener( PropertyChangeListener pcl )
    {
        pcs.addPropertyChangeListener( pcl );
    }

    public void removePropertyChangeListener( PropertyChangeListener pcl )
    {
        pcs.removePropertyChangeListener( pcl );
    }
}