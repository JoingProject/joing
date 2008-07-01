/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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