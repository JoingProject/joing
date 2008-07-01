/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.joing.pde.desktop.deskwidget.desklet;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.FilteredImageSource;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import org.joing.pde.PDEUtilities;
import org.joing.pde.swing.ImageHighlightFilter;

/**
 *
 * @author Francisco Morero Peyrona
 */
public class PDEDeskletButton extends JLabel
{
    private ImageIcon iconBase;
    private ImageIcon iconHigh;
    
    //------------------------------------------------------------------------//
    
    /**
     * This constructor is to be used by PDEDeskApplet class.
     */
    PDEDeskletButton( String sImage, String sTipText )
    {
        init( PDEUtilities.getIcon( this, "images/"+ sImage, 12, 12 ), sTipText );
    }
    
    public PDEDeskletButton( ImageIcon icon, String sTipText )
    {
        init( icon, sTipText );
    }
    
    //------------------------------------------------------------------------//
    // Listners and actions
    
    public void addActionListener( ActionListener al )
    {
        listenerList.add( ActionListener.class, al );
    }
    
    public void removeActionListener( ActionListener al )
    {
        listenerList.remove( ActionListener.class, al );
    }
    
    protected void fileActionPerformed( Object source )
    {
         ActionEvent ae = new ActionEvent( source, ActionEvent.ACTION_PERFORMED, null );
         
         Object[] listeners = listenerList.getListenerList();
         
         for( int n = listeners.length - 2; n >= 0; n -= 2 )
         {
            if( listeners[n] == ActionListener.class )
                ((ActionListener) listeners[n + 1]).actionPerformed( ae );
         }
    }
    
    //------------------------------------------------------------------------//
    
    private void init( ImageIcon icon, String sTipText )
    {
        if( icon.getIconWidth() > 12 || icon.getIconHeight() > 12 )
            icon.setImage( icon.getImage().getScaledInstance( 12, 12, Image.SCALE_SMOOTH ) );
        
        iconBase = icon;
        
        ImageHighlightFilter ihf = new ImageHighlightFilter( true, 32 );
        Image image = createImage( new FilteredImageSource( icon.getImage().getSource(), ihf ) );
        
        iconHigh = new ImageIcon( image );
        
        setIcon( iconBase );
        setToolTipText( sTipText );
        
        MyMouseAdapter mouseListener = new MyMouseAdapter();
        
        addMouseListener( mouseListener );
        addMouseMotionListener( mouseListener );
    }
    
    //------------------------------------------------------------------------//
    // INNER CLASS: MouseListener for ToolBarButton
    //------------------------------------------------------------------------//
    
    private final class MyMouseAdapter implements MouseListener, MouseMotionListener
    {
        public void mouseClicked( MouseEvent me )
        {
            fileActionPerformed( me.getSource() );
        }

        public void mouseEntered( MouseEvent me )
        {
            setIcon( iconHigh );
        }

        public void mouseExited( MouseEvent me )
        {
            setIcon( iconBase );
        }
        
        public void mousePressed( MouseEvent me )  {}
        public void mouseReleased( MouseEvent me ) {}
        public void mouseDragged( MouseEvent me )  {}
        public void mouseMoved( MouseEvent me )    {}
    }
}