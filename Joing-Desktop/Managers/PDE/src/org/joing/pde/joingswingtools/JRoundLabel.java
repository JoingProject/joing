/*
 * JRoundLabel.java
 *
 * Created on 10 de febrero de 2007, 21:18
 *
 * (c) 2006 - Francisco Morero Peyrona
 *
 * License: {license}
 */

package org.joing.pde.joingswingtools;
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