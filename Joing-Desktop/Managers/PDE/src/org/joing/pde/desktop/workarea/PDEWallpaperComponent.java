/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.joing.pde.desktop.workarea;

import java.awt.BorderLayout;
import java.awt.Component;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.joing.common.desktopAPI.workarea.Wallpaper;

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