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
import org.joing.pde.desktop.taskbar.PDETaskBarComponent;
import org.joing.kernel.swingtools.JoingSwingUtilities;
import org.joing.kernel.swingtools.ImageHighlightFilter;

/**
 *
 * @author Francisco Morero Peyrona
 */
public final class StartButton extends PDETaskBarComponent
{
    private JLabel    label;
    private ImageIcon icon;
    private StartMenu popup;
    //------------------------------------------------------------------------//
    
    // Popup Menu (StartMenu) is created everytime it is needed to save memory
    
    public StartButton()
    {
        label = new JLabel();
        popup = new StartMenu();
        
        initGUI();
        
        getComponentPopupMenu().remove( itemRemove );
        getComponentPopupMenu().remove( itemPreferences );
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
    // TaskBarComponent interface
    
    public void onAbout()
    {
        // TODO: Implementarlo
    }

    public void onRemove()
    {
        // Nothing to do: better to not allow to remove Start button
    }

    public void onMove()
    {
        // TODO: Implementarlo
    }

    public void onPreferences()
    {
        // Nothing to do: Start button has no preferences
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
        icon = JoingSwingUtilities.getIcon( this, "images/start" );
        label.setIcon( icon );
        
        add( label );
        
        addMouseListener( new MouseAdapter()
        {
            public void mousePressed( MouseEvent me )
            {
                StartButton.this.togglePopup();
            }
    
            public void mouseEntered( MouseEvent me )
            {
                Image imgHigh = createImage( new FilteredImageSource( icon.getImage().getSource(), new ImageHighlightFilter( true, 32 ) ) );
                StartButton.this.label.setIcon( new ImageIcon (imgHigh ) );                        
                setForeground( getForeground().brighter() );
            }

            public void mouseExited( MouseEvent me )
            {
                StartButton.this.label.setIcon( icon );
                setForeground( getForeground().darker() );
            }
        } );
    }
}