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

import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.FilteredImageSource;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.MenuSelectionManager;
import javax.swing.border.EmptyBorder;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import org.joing.api.desktop.workarea.desklet.deskLauncher.Launcher;
import org.joing.pde.runtime.PDERuntime;
import org.joing.pde.swing.ImageHighlightFilter;

/**
 *
 * @author fmorero
 */
public final class StartButton extends JLabel
{
    private ImageIcon icon;
    private StartMenu popup;
    
    //------------------------------------------------------------------------//
    
    public StartButton()
    {
        popup = new StartMenu();            
        initGUI();        
    }
    
    //------------------------------------------------------------------------//
    
    private void initGUI()
    {
        icon = PDERuntime.getRuntime().getIcon( this, "images/start.png" );
        
        setBorder( new EmptyBorder( 0,2,0,4 ) );
        setIcon( icon );
        
        addMouseListener( new MouseAdapter()
        {
            public void mousePressed( MouseEvent me )
            {
                //System.out.println(popup.isVisible()+" -> "+popup.isShowing());
                if( popup.isVisible() )
                {
                    popup.setVisible( false );
                    MenuSelectionManager.defaultManager().clearSelectedPath();
                }
                else
                {
                    int nHeight = popup.getPreferredSize().height;
                    popup.show( StartButton.this, 0, -nHeight );
                    ///popup.setVisible( true );
                }
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