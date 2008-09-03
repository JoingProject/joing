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

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.Icon;

/**
 * An image representing image_not_found.
 * Note: Got from: http://java.sun.com/docs/books/tutorial/uiswing/components/icon.html#efficiency
 * @author Francisco Morero Peyrona
 */
public class NoImage implements Icon
{
    private int width  = 32;
    private int height = 32;
    private BasicStroke stroke = new BasicStroke( 4 );

    public void paintIcon( Component c, Graphics g, int x, int y )
    {
        Graphics2D g2d = (Graphics2D) g.create();

        g2d.setColor( Color.WHITE );
        g2d.fillRect( x + 1, y + 1, width - 2, height - 2 );

        g2d.setColor( Color.BLACK );
        g2d.drawRect( x + 1, y + 1, width - 2, height - 2 );

        g2d.setColor( Color.RED );

        g2d.setStroke( stroke );
        g2d.drawLine( x + 10, y + 10, x + width - 10, y + height - 10 );
        g2d.drawLine( x + 10, y + height - 10, x + width - 10, y + 10 );

        g2d.dispose();
    }

    public int getIconWidth()
    {
        return width;
    }

    public int getIconHeight()
    {
        return height;
    }
}