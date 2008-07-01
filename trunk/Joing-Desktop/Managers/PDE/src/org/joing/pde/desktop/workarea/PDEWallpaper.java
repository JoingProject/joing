/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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