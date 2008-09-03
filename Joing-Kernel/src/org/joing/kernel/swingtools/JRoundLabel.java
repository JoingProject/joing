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

package org.joing.kernel.swingtools;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JLabel;

public class JRoundLabel extends JLabel
{
    public JRoundLabel()
    {
        this( null );
    }
    
    public JRoundLabel( String text )
    {
        this( text, JLabel.CENTER );
    }

    public JRoundLabel( String text, int horizontalAlignment )
    {
        super( text, horizontalAlignment );
    }

    //------------------------------------------------------------------------//
    
    protected void paintComponent( Graphics g )
    {
        if( isOpaque() )
        {
            ((Graphics2D) g).setRenderingHint( RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON );
            g.setColor( getBackground() );
            g.fillRoundRect( 0, 0, getWidth(), getHeight(), 10, 10 );
        }
        
        // If not opaque, super paints again backgroud (makes it square)
        boolean bOpaque = isOpaque();
        setOpaque( false );
        super.paintComponent( g );
        setOpaque( bOpaque );
    }
}