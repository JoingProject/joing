/*
 * JRoundLabel.java
 *
 * Created on 10 de febrero de 2007, 21:18
 *
 * (c) 2006 - Francisco Morero Peyrona
 *
 * License: {license}
 */

package org.joing.pde.desktop.swing;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JLabel;

public class JRoundLabel extends JLabel
{
    private boolean bOpaco;   // Uso esta var en lugar de la de JLabel para q JLabel nunca pinte el fondo 
    
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
    
    // Necesito q super.paintComponent(...) no pinte nunca el fondo
    public boolean isOpaque()
    {
        return false;
    }
    
    public void setOpaque( boolean bOpaque )
    {
        super.setOpaque( false );   // Siempre falso
        this.bOpaco = bOpaque;
    }
    
    protected void paintComponent( Graphics g )
    {
        if( this.bOpaco )
        {
            int width  = getWidth();
            int height = getHeight();
         
            ((Graphics2D) g).setRenderingHint( RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON );
            g.setColor( getBackground() );
            g.fillRoundRect( 0, 0, width, height, 10, 10 );
        }
        
        // Llamo a super para q pinte el texto (el fondo no lo pinta pq isOpaque() == false
        super.paintComponent( g );
    }
}