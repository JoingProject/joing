/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.joing.pde.desktop.workarea;

import java.awt.BorderLayout;
import java.awt.Component;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.joing.common.desktopAPI.workarea.Wallpaper;

/**
 *
 * @author fmorero
 */
class PDEWallpaperComponent extends JPanel implements PropertyChangeListener
{
    private PDEWallpaper wp;
    
    PDEWallpaperComponent()
    {
        this( null );
    }
    
    PDEWallpaperComponent( Wallpaper wp )
    {
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
            JLabel lbl = new JLabel( wp.getImage() );
                   lbl.setHorizontalAlignment( JLabel.CENTER );
                   
            setLayout( new BorderLayout() );
            add( lbl, BorderLayout.CENTER );
        }
        else if( wp.getMode() == Wallpaper.Mode.EXPANDED )
        {
            
        }
        else if( wp.getMode() == Wallpaper.Mode.TILES )
        {
            
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
                ((JLabel) aComp[n]).setIcon( wp.getImage() );
        }
    }
}