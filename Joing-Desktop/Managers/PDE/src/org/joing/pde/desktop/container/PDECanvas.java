/*
 * (c) Telco Domótica S.L.
 * Todos los derechos reservados.
 * 
 * Creado el 22-ago-2005 a las 16:34:44
 */

package org.joing.pde.desktop.container;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.LayoutManager;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import org.joing.common.desktopAPI.container.DeskCanvas;

/**
 * A JPanel placed over the background image (if any) and below all other
 * desktop objects.
 * <p>
 * Useful for information, publicity panels and similar. 
 * 
 * @author Francisco Morero Peyrona
 */
public class PDECanvas extends JPanel implements DeskCanvas
{
    protected JRootPane root;
    
    //------------------------------------------------------------------------//
    
    public PDECanvas()
    {
        setOpaque( false );
        
        root = new JRootPane();
        ((JPanel) root.getContentPane()).setOpaque( false );

        // Añado el root pane a este JPanel
        super.setLayout( new BorderLayout() );
        super.add( root, BorderLayout.CENTER );
    }
    
    //------------------------------------------------------------------------//
    // METHODS REDIRECTED TO this.root.getContentPane

    public LayoutManager getLayout()
    {
        if( root != null )
            return root.getContentPane().getLayout();
        else
            return null;
    }
    
    public void setLayout( LayoutManager lm )
    { 
        if( root != null )
            root.getContentPane().setLayout( lm );
    }
    
    public Component getGlassPane()
    {
        return root.getGlassPane();
    }
    
    public void setGlassPane( Component glass )
    {
        root.setGlassPane( glass );
    }
    
    //------------------------------------------------------------------------//
    
    public Component add( Component c )             { return root.getContentPane().add( c );    }
    public Component add( Component c, int n )      { return root.getContentPane().add( c, n ); }
    public Component add( String s, Component c )   { return root.getContentPane().add( s, c ); }
    public void add( Component c, Object o, int n ) { root.getContentPane().add( c, o, n );     }
    public void add( Component c, Object o )        { root.getContentPane().add( c, o );        }
    
    //------------------------------------------------------------------------//
    
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