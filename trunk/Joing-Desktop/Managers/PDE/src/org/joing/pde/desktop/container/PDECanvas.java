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
import java.awt.Container;
import java.awt.Dimension;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import org.joing.common.desktopAPI.Closeable;
import org.joing.common.desktopAPI.DeskComponent;
import org.joing.common.desktopAPI.pane.DeskCanvas;

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
    private static final String DONT_USE_ME = "Do not use me";
    
    protected JRootPane root;
    
    public PDECanvas()
    {
        setOpaque( false );
        
        root = new JRootPane();
        ((JPanel) root.getContentPane()).setOpaque( false );

        // Añado el root pane a este JPanel
        super.setLayout( new BorderLayout() );
        super.add( root, BorderLayout.CENTER );
    }
    
    public void add( DeskComponent dc )
    {
        add( (Component) dc );
    }
    
    public Component add( Component c )
    { 
        return root.getContentPane().add( c );
    }
    
    public void remove( DeskComponent dc )
    {
        root.getContentPane().remove( (Component) dc );
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
    
    public void close()
    {
        close( (Container) this );
    }
    
    private void close( Container c )
    {
        Component aComp[] = root.getContentPane().getComponents();
        
        for( int n = 0; n < aComp.length; n++ )
        {
            if( aComp[n] instanceof Container )
                close( (Container) aComp[n] );
            else if( aComp[n] instanceof Closeable )
                ((Closeable) aComp[n]).close();
        }
    }
    
    // Just to avoid accidental use of them  ---------------------------------------------------------
    ///public Component add( Component c )          { throw new IllegalAccessError(DONT_USE_ME); }
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