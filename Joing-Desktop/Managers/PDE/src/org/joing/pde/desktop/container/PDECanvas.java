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
package org.joing.pde.desktop.container;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import javax.swing.JPanel;
import org.joing.kernel.api.desktop.Closeable;
import org.joing.kernel.api.desktop.DeskComponent;
import org.joing.kernel.api.desktop.pane.DeskCanvas;
import org.joing.kernel.api.desktop.workarea.WorkArea;
import org.joing.kernel.swingtools.JoingSwingUtilities;

/**
 * A very basic component.
 * 
 * @author Francisco Morero Peyrona
 */
public class PDECanvas extends JPanel implements DeskCanvas
{
    private static final String DONT_USE_ME = "Do not use me";
    
    //------------------------------------------------------------------------//
    
    public PDECanvas()
    {
        setOpaque( false );
        setLayout( new BorderLayout() );
    }
    
    public void add( DeskComponent dc )
    {
        super.add( (Component) dc );
    }
    
    public void remove( DeskComponent dc )
    {
        super.remove( (Component) dc );
    }
    
    /**
     *  Center this in desktopPane
     */
    public void center()
    {
        setLocationRelativeTo( (DeskComponent) getParent() );
    }
    
    public void setLocationRelativeTo( DeskComponent parent )
    {
        if( parent != null )
        {
            Dimension size1 = ((Component) parent).getSize();
            Dimension size2 = getPreferredSize();
            int nX = (size1.width  - size2.width)  / 2;
            int nY = (size1.height - size2.height) / 2;
            
            setBounds( Math.max( nX, 0 ), Math.max( nY, 0 ), size2.width, size2.height );
        }
    }
    
    public void close()
    {
        setVisible( false );
        
        // Detach from container WorkArea
        WorkArea wa = JoingSwingUtilities.findWorkAreaFor( this );
        
        if( wa != null )
            wa.remove( this );
        
        // Recursively closes all its childs
        close( (Container) this );
    }
    
    // Just to avoid accidental use of them  ---------------------------------------------------------
    // NEXT: Tengo que permitir este metodo porque no he fabricado una clase asï:
    //       PDEDesckComponent extends JComponent implements DesckComponent
    //       Para que todo fuese bonito, habría que hacer igual con las demás.
    ///public Component add( Component c )             { throw new IllegalAccessError(DONT_USE_ME); }
    public Component add( Component c, int n )      { throw new IllegalAccessError(DONT_USE_ME); }
    public Component add( String s, Component c )   { throw new IllegalAccessError(DONT_USE_ME); }
    public void add( Component c, Object o )        { throw new IllegalAccessError(DONT_USE_ME); }
    public void add( Component c, Object o, int n ) { throw new IllegalAccessError(DONT_USE_ME); }
    public void remove( Component c )               { throw new IllegalAccessError(DONT_USE_ME); }
    //------------------------------------------------------------------------------------------------
    
    /**
     *
     */
    public void setTranslucency( int nPercent )
    {
        Color clr = getBackground();
        
        nPercent = (nPercent < 0 ? 0 : nPercent > 100 ? 100 : nPercent);
        nPercent = (255 * nPercent) / 100;    // Moves from range [0 a 100] to range [0 a 255]
        
        setBackground( new Color( clr.getRed(), clr.getGreen(), clr.getBlue(), 255 - nPercent ) );
    }
    
    //------------------------------------------------------------------------//
    
    // Recursively closes all its childs
    private void close( Container c )
    {
        Component aComp[] = c.getComponents();
        
        for( int n = 0; n < aComp.length; n++ )
        {
            if( aComp[n] instanceof Container )
                close( (Container) aComp[n] );
            else if( aComp[n] instanceof Closeable )
                ((Closeable) aComp[n]).close();
        }
    }
}