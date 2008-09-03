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
package org.joing.pde.desktop.taskbar.waSwitcher;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import javax.swing.JPanel;
import javax.swing.event.MouseInputAdapter;
import org.joing.kernel.api.desktop.desktop.Desktop;
import org.joing.pde.desktop.workarea.PDEWorkArea;

/**
 * Each one of the small boxes representing a WorkArea space.
 * 
 * @author Francisco Morero Peyrona
 */
class Map extends JPanel
{
    // TODO: estos colores tienen que ser parte del sistema de colores
    private static Color clrSelected   = Color.pink;
    private static Color clrUnSelected = Color.gray;
    
    private PDEWorkArea workArea;
    
    //------------------------------------------------------------------------//
    
    Map( PDEWorkArea wa )
    {
        workArea = wa;
        
        setBackground( clrUnSelected );
        setInheritsPopupMenu( true );
        
        addMouseListener( new MouseInputAdapter()
        {
            public void mouseClicked( MouseEvent me )
            {
                if( me.getButton() == MouseEvent.BUTTON1 )
                {
                    Desktop desktop = org.joing.kernel.jvmm.RuntimeFactory.getPlatform().getDesktopManager().getDesktop();
                            desktop.setActiveWorkArea( Map.this.workArea );
                }
            }
        } );
    }

    public void paintComponent( Graphics g )
    {
        g.setColor( getBackground() );
        g.fillRect( 1, 1, getWidth() -2, getHeight() - 2 );
        g.dispose();
    }
    
    public boolean isSelected()
    {
        return getBackground().equals( clrSelected );   // Saving one variable (bSelected)
    }
    
    public void setSelected( boolean b )
    {
        if( b != isSelected() )
            setBackground( (b ? clrSelected  : clrUnSelected) );
    }
    
    public int hashCode()
    {
        int hash = 7;    // TODO: hacerlo bien
        return hash;
    }
    
    public boolean equals( Object obj )
    {
        return (obj instanceof Map) && (((Map) obj).workArea == workArea);
    }
    
    //------------------------------------------------------------------------//
    
    PDEWorkArea getTarget()
    {
        return workArea;
    }
}