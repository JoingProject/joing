/*
 * StartButton.java
 *
 * Created on 14 de febrero de 2007, 20:56
 *
 * (c) 2006 - Francisco Morero Peyrona
 *
 * License: {license}
 */

package org.joing.pde.desktop.taskbar.start;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.FilteredImageSource;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.MenuSelectionManager;
import javax.swing.border.EmptyBorder;
import org.joing.common.desktopAPI.DeskComponent;
import org.joing.common.desktopAPI.DesktopManagerFactory;
import org.joing.pde.PDEUtilities;
import org.joing.pde.swing.ImageHighlightFilter;

/**
 *
 * @author fmorero
 */
public final class StartButton extends JLabel implements DeskComponent
{
    private ImageIcon icon;
    private StartMenu popup;
    
    //------------------------------------------------------------------------//
    
    public StartButton()
    {
        popup = new StartMenu();            
        initGUI();        
    }
        
    // Redefined from JComponent
    public Point getPopupLocation()
    {
        Dimension size = popup.getPreferredSize();
        int       x    = 0;
        int       y    = -size.height;
        
        // TODO: hay q mirar dónde mostarlo (la barra puede estar: arriba, abajo, izq o dcha)
        
        return new Point( x, y );
    }
    
    //------------------------------------------------------------------------//
    
    private void togglePopup()
    {
        ///System.out.println(popup.isVisible()+" -> "+popup.isShowing());
        if( popup.isVisible() )
        {
            popup.setVisible( false );
            MenuSelectionManager.defaultManager().clearSelectedPath();
        }
        else
        {
            Point position = getPopupLocation();
            popup.show( StartButton.this, position.x, position.y );
        }
    }
    
    private void initGUI()
    {
        icon = PDEUtilities.getIcon( this, "images/start" );
        
        setBorder( new EmptyBorder( 0,2,0,4 ) );
        setIcon( icon );
        
        addMouseListener( new MouseAdapter()
        {
            public void mousePressed( MouseEvent me )
            {
                StartButton.this.togglePopup();
            }
    
            public void mouseEntered( MouseEvent me )
            {
                Image imgHigh = createImage( new FilteredImageSource( icon.getImage().getSource(), new ImageHighlightFilter( true, 32 ) ) );
                setIcon( new ImageIcon (imgHigh ) );                        
                setForeground( getForeground().brighter() );
            }

            public void mouseExited( MouseEvent me )
            {
                setIcon( icon );
                setForeground( getForeground().darker() );
            }
        } );
    }
}