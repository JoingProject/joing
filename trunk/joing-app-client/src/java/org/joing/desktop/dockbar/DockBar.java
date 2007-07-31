/*
 * DockBar.java
 *
 * Created on 19-jul-2007, 10:34:58
 *
 * Author: Francisco Morero Peyrona.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or (at
 * your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 */
package org.joing.desktop.dockbar;

import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.swing.ImageIcon;
import java.awt.Image;
import java.awt.Point;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;

/**
 *
 * @author fmorero
 */
public class DockBar extends JPanel
{
    private final static String IMAGE    = "image";
    private final static String LAUNCHER = "launcher";
    private final static String DISTANCE = "distance";
    
    private ContainerMouseListener cml;
    
    private int nMinIconSize  = 48;   // Size when mouse is far away from icon
    private int nMaxIconSize  = 96;   // Size when mouse is in the heart of the icon
    
    //------------------------------------------------------------------------//
    
    public DockBar()
    {
        cml = new ContainerMouseListener();
        
        setOpaque( false );
        setLayout( new MyFlowLayout( FlowLayout.CENTER ) );
        
        addAncestorListener( new AncestorListener() 
        {
            public void ancestorAdded( AncestorEvent ae )
            {
                DockBar.this.getParent().addMouseListener( cml );
                DockBar.this.getParent().addMouseMotionListener( cml );
            }
            
            public void ancestorRemoved( AncestorEvent ae )
            {
                DockBar.this.getParent().removeMouseListener( cml );
                DockBar.this.getParent().removeMouseMotionListener( cml );
            }

            public void ancestorMoved( AncestorEvent ae )
            {
            }
        } );
    }

    public int getMinIconSize()
    {
        return nMinIconSize;
    }

    public void setMinIconSize( int nMinIconSize )
    {
        if( nMinIconSize < 16 )
            nMinIconSize = 16;
        
        if( nMinIconSize > 256 )
            nMinIconSize = 256;
        
        this.nMinIconSize = nMinIconSize;
        scaleImages( nMinIconSize );
    }
    
    public int getMaxIconSize()
    {
        return nMaxIconSize;
    }

    public void setMaxIconSize( int nMaxIconSize )
    {
        if( nMaxIconSize < 16 )
            nMaxIconSize = 16;
        
        if( nMaxIconSize > 256 )
            nMaxIconSize = 256;
        
        this.nMaxIconSize = nMaxIconSize;
        scaleImages( nMaxIconSize );
    }
    
    /*public void add( DeskLauncher launcher )
    {
        ImageIcon icon  = launcher.getIcon();
        Image     image = icon.getImage();
        
        if( icon.getIconHeight() != nMaxIconSize || icon.getIconWidth() != nMaxIconSize )
            image = image.getScaledInstance( nMaxIconSize, nMaxIconSize, Image.SCALE_SMOOTH );

        // Si hago esto para procesar los doble clicks (sería lo lógico): 
        //     label.addMouseListener(...)
        // entonces el escalado se vuelve lentísimo (ni idea de por qué); por lo que
        // tengo que tratar los doble cliks en el mismo MouseListener que hago lo demás.
        JLabel label = new JLabel( new ImageIcon( image.getScaledInstance( nMinIconSize, nMinIconSize, Image.SCALE_SMOOTH ) ) );
               label.putClientProperty( IMAGE   , image    );
               label.putClientProperty( LAUNCHER, launcher );
               label.putClientProperty( DISTANCE,       -1 );
        
        add( label );
    }*/
   
    // TODO: cambiar esta por la anterior cuando esté la clase DeskLauncher
    public void add( ImageIcon icon )
    {
        Image image = icon.getImage();
        
        if( icon.getIconHeight() != nMaxIconSize || icon.getIconWidth() != nMaxIconSize )
            image = image.getScaledInstance( nMaxIconSize, nMaxIconSize, Image.SCALE_SMOOTH );

        JLabel label = new JLabel( new ImageIcon( image.getScaledInstance( nMinIconSize, nMinIconSize, Image.SCALE_SMOOTH ) ) );
               label.putClientProperty( IMAGE   , image );
               label.putClientProperty( DISTANCE,    -1 );
        
        add( label );
    }
    
    //------------------------------------------------------------------------//
    
    private void onDoubleClick( Point point )
    {
        point = SwingUtilities.convertPoint( getParent(), point, this );
        JLabel label = (JLabel) getComponentAt( point );
        
        if( label != null )
        {
            // TODO: hacerlo -->
            // DesktopLauncher dl = (DesktopLauncher) label.getClientProperty( LAUNCHER );
            //                 dl.execute();
            System.out.println( "Clicked on "+ label );
        }
    }
    
    private void scaleOn( Point point )
    {
        if( isHot( point ) )
        {
            for( int n = 0; n < getComponentCount(); n++ )
            {
                JLabel label     = (JLabel) getComponent( n );
                int    nDistance = getDistance( label, point );
                int    nLastDist = (Integer) label.getClientProperty( DISTANCE );
///if( n == 3 )
///    System.out.println( "x= "+ point.getX() +"  y= "+ point.getY() +"  d="+ nDistance );
                if( (nDistance != nLastDist) && (nDistance <= nMaxIconSize * 3) )
                {
                    Image image = (Image) label.getClientProperty( IMAGE );
                    int   nSize = 0;

                    if( nDistance <= nMaxIconSize )
                        nSize = Math.max( nMinIconSize, nMaxIconSize - nDistance );
                    else
                        nSize = nMinIconSize;

                    label.setIcon( new ImageIcon( image.getScaledInstance( nSize, nSize, Image.SCALE_SMOOTH ) ) );
                    label.putClientProperty( DISTANCE, nDistance );
                }
            }
        }
    }
    
    // Checks if passed point affects at least one icon
    private boolean isHot( Point point )
    {
        // If mouse Y is not further than max icon size (height == nMaxIconSize)
        if( Math.abs( getY() - point.getY() ) <= nMaxIconSize )
        {
            Component first = getComponent( 0 );
            Component last  = getComponent( getComponentCount() - 1 );
            
            // If mouse X is inside icons
            if( point.getX() >= first.getX() - nMaxIconSize &&
                point.getX() <= last.getX()  + nMaxIconSize )
                return true;
        }
        
        return false;
    }
    
    // Returns the distance from the center of the component to the mouse pointer
    private int getDistance( Component cmp, Point point )
    {
        double X2 = getX() + cmp.getX() + (cmp.getWidth() / 2);
        double X1 = point.getX();
        double Y2 = getY() + cmp.getY() + (cmp.getHeight() / 2);
        double Y1 = point.getY();
        
        double distance = Math.sqrt( Math.pow( X2-X1, 2 ) + Math.pow( Y2-Y1, 2 ) );
        
        return (int) Math.round( distance );
    }
    
    // Scale all icons to their minimun size
    private void resetSizes()
    {
        for( int n = 0; n < getComponentCount(); n++ )
        {
            JLabel label = (JLabel) getComponent( n );
            
            if( label.getIcon().getIconHeight() != nMinIconSize )
            {
                Image image = (Image) label.getClientProperty( IMAGE );
                      image = image.getScaledInstance( nMinIconSize, nMinIconSize, Image.SCALE_SMOOTH );
                       
                label.setIcon( new ImageIcon( image ) );
            }
        }
    }
    
    private void scaleImages( int nSize )
    {
        for( int n = 0; n < getComponentCount(); n++ )
        {
            JLabel label = (JLabel) getComponent( n );
            
            if( label.getIcon().getIconHeight() != nSize )
            {
                Image image = (Image) label.getClientProperty( IMAGE );
                      image = image.getScaledInstance( nSize, nSize, Image.SCALE_SMOOTH );
                       
                label.putClientProperty( IMAGE, image );
            }
        }
        
        resetSizes();
    }
    
    //------------------------------------------------------------------------//
    // INNER CLASSES
    //------------------------------------------------------------------------//
    
    private class ContainerMouseListener implements MouseListener, MouseMotionListener
    {
        public void mouseClicked( MouseEvent me )
        {
            if( me.getClickCount() == 2 && me.getButton() == MouseEvent.BUTTON1 )
                DockBar.this.onDoubleClick( me.getPoint() );
        }

        public void mousePressed( MouseEvent me )
        {
        }

        public void mouseReleased( MouseEvent me )
        {
        }

        public void mouseEntered( MouseEvent me )
        {
        }

        // Invoked when mouse goes out of the window
        public void mouseExited( MouseEvent me )
        {
            DockBar.this.resetSizes();
        }
        
        public void mouseDragged( MouseEvent e )
        {
        }

        public void mouseMoved( MouseEvent me )
        {
            DockBar.this.scaleOn( me.getPoint() );
        }
    }
}