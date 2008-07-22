/*
 * JRoundPanel.java
 *
 * Created on 1 de octubre de 2007, 13:18
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.joing.pde.joingswingtools;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JPanel;

/**
 *
 * @author Francisco Morero Peyrona
 */
public class JRoundPanel extends JPanel
{
    public void setTransparency( int nPercent )
    {
        nPercent = (nPercent < 0 ? 0 : nPercent > 100 ? 100 : nPercent);
        nPercent = 255 * nPercent / 100;    // Pasa de [0 a 100] a [0 a 255]
        
        setOpaque( nPercent == 0 );
        
        if( nPercent > 0 )
        {
            Color clr = getBackground();
            setBackground( new Color( clr.getRed(), clr.getGreen(), clr.getBlue(), 255 - nPercent ) );
        }
        
        repaint();
    }
    
    //------------------------------------------------------------------------//
    
    protected void paintComponent( Graphics g )
    {
        if( isOpaque() )
        {
            int width  = getWidth();
            int height = getHeight();
         
            ((Graphics2D) g).setRenderingHint( RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON );
            g.setColor( getBackground() );
            g.fillRoundRect( 0, 0, width, height, 10, 10 );
        }
        
        paintChildren( g );
    }
}