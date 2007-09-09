/*
 * (c) Telco Domótica S.L.
 * Todos los derechos reservados.
 * 
 * Creado el 22-ago-2005 a las 16:34:44
 */

package org.joing.pde.desktop.workarea.containers;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 * A JPanel placed over the background image (if any) and below all other
 * desktop objects.
 * <p>
 * Useful for information, publicity panels and similar. 
 * 
 * @author Francisco Morero Peyrona
 */
public class JoingCanvas extends JPanel
{
    public JoingCanvas()
    {                
    }
    
    /**
     *  Center this in desktopPane
     */
    public void center()
    {
        Dimension size1 = getParent().getSize();
        Dimension size2 = getPreferredSize();
        int nX = (size1.width  - size2.width)  / 2;
        int nY = (size1.height - size2.height) / 2;
        
        setBounds( Math.max( nX, 0 ), Math.max( nY, 0 ), size2.width, size2.height );
    }
    
    /**
     *
     */
    public void setTranslucency( int nPercent )
    {
        nPercent = (nPercent < 0 ? 0 : nPercent > 100 ? 100 : nPercent);
        nPercent = 255 * nPercent / 100;    // Pasarlo de 0 a 100 a 0 a 255
        
        setOpaque( nPercent == 0 );
        
        if( nPercent > 0 )
        {
            Color clr = getBackground();
            setBackground( new Color( clr.getRed(), clr.getGreen(), clr.getBlue(), 255 - nPercent ) );
        }
    }
}