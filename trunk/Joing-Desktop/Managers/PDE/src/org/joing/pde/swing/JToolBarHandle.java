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
package org.joing.pde.swing;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;
import javax.swing.JComponent;

/**
 *
 * @author Francisco Morero Peyrona
 */
public class JToolBarHandle extends JComponent
{
    private Container parent;
    
    //------------------------------------------------------------------------//
    
    public JToolBarHandle( Container parent )
    {
        this.parent = parent;
        setMinimumSize( new Dimension( 8,6 ) );
        setMaximumSize( new Dimension( 8, Integer.MAX_VALUE ) );
        setPreferredSize( new Dimension( 8, 10 ) );
    }
    
    public void paintComponent( Graphics g )
    {
        if( isVisible() )
        {
            // First, update component's size
            setSize( getPreferredSize().width, parent.getSize().height );
            
            // Calculate lines height
            Insets insets = parent.getInsets();
            int    y1     = insets.top + 3;
            int    y2     = getHeight() - insets.bottom -3;
            
            // Paint lines
            drawLine( g, 2, y1, y2 );
            drawLine( g, 5, y1, y2 );
        }
    }
    
    private void drawLine( Graphics g, int x, int y1, int y2 )
    {
        g.setColor( Color.white );
        g.drawLine( x, y1, x, y2 );
        
        x += 1;
        
        g.setColor( Color.darkGray );
        g.drawLine( x, y1, x, y2 );
    }
}