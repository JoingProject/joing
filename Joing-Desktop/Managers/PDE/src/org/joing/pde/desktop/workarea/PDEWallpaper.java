/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.joing.pde.desktop.workarea;

import java.awt.Toolkit;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import javax.swing.ImageIcon;
import org.joing.common.desktopAPI.workarea.Wallpaper;
import org.joing.pde.PDEUtilities;

/**
 *
 * @author fmorero
 */
public class PDEWallpaper implements Wallpaper
{
    private Wallpaper.Mode mode;
    private ImageIcon      image;
    
    private PropertyChangeSupport pcs;
    
    //------------------------------------------------------------------------//
    
    public PDEWallpaper()
    {
        pcs = new PropertyChangeSupport( this );
    }
    
    //------------------------------------------------------------------------//
    // Interface implementation
    
    public byte[] getSource()
    {
        return PDEUtilities.icon2ByteArray( image );
    }
    
    public void setSource( byte[] image )
    {
        setImage( new ImageIcon( Toolkit.getDefaultToolkit().createImage( image ) ) );
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
    
    public ImageIcon getImage()
    {
        return image;
    }
    
    public void setImage( ImageIcon image )
    {
        ImageIcon oldImage = this.image;
        
        this.image = image;
        
        pcs.firePropertyChange( "image", oldImage, this.image );
    }
    
    public void addPropertyChangeListener( PropertyChangeListener pcl )
    {
        pcs.addPropertyChangeListener( pcl );
    }

    public void removePropertyChangeListener( PropertyChangeListener pcl )
    {
        pcs.removePropertyChangeListener( pcl );
    }
}