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
import org.joing.common.dto.vfs.FileDescriptor;
import org.joing.pde.PDEUtilities;

/**
 * PDE Implementation of Wallpaper interface.
 * 
 * @author Francisco Morero Peyrona
 */
public class PDEWallpaper implements Wallpaper
{
    private String         sFile;
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
    
    public String getSource()
    {
        return sFile;
    }
    
    public void setSource( String sFileName )
    {
        if( (sFile != null && (! sFile.equals( sFileName ))) || 
            (sFile == null && sFileName == null ) )
            return;    // Trying to set the same image again
        
        if( sFileName != null )
        {
            sFileName = sFileName.trim();
        
            if( sFileName.length() == 0 )
                sFileName = null;
        }
        
        String sOldFile = sFile;
        sFile = sFileName;
        pcs.firePropertyChange( "source", sOldFile, sFile );
        
        if( sFile != null )
        {
            Object oFile = PDEUtilities.getFile( sFile );
            
            if( oFile != null )
            {
                byte[] img = null;
                
                if( oFile instanceof java.io.File )
                {
                    java.io.File file = (java.io.File) oFile;
                    // TODO: hacerlo  --> image = ...
                }
                else
                {
                    FileDescriptor fd = (FileDescriptor) oFile;
                    // TODO: hacerlo  --> image = ...
                }
                
                if( img != null )
                    setImage( new ImageIcon( Toolkit.getDefaultToolkit().createImage( img ) ) );
            }
        }
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