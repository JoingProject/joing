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
import org.joing.common.desktopAPI.Closeable;
import org.joing.common.desktopAPI.DeskComponent;
import org.joing.common.desktopAPI.pane.DeskCanvas;
import org.joing.common.desktopAPI.workarea.WorkArea;
import org.joing.pde.PDEUtilities;

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
    {// TODO: Comprobar que se hace el detach del WorkArea
        setVisible( false );
        
        // Detach from container WorkArea
        WorkArea wa = PDEUtilities.findWorkAreaFor( this );
        
        if( wa != null )
            wa.remove( this );
        
        // Recursively closes all its childs
        close( (Container) this );
    }
    
    // Just to avoid accidental use of them  ---------------------------------------------------------
    // NEXT: Tengo que permitir este metjod porque no he fabricado una clase asï:
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