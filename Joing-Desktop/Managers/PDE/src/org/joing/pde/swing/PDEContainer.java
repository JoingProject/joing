/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.joing.pde.swing;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import org.joing.common.desktopAPI.Closeable;
import org.joing.common.desktopAPI.DeskComponent;
import org.joing.common.desktopAPI.DeskContainer;

/**
 * Class implementing the DeskContainer interface.
 * 
 * @author Francisco Morero Peyrona
 */
public class PDEContainer extends JPanel implements DeskContainer
{
    private static final String DONT_USE_ME = "Do not use me";
    
    protected JRootPane root;
    
    //------------------------------------------------------------------------//
    
    public PDEContainer()
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
        root.getContentPane().add( (Component) dc );   
    }
    
    public void remove( DeskComponent dc )
    {
        root.getContentPane().remove( (Component) dc );
    }
    
    public void close()
    {
        close( (Container) this );
    }
    
    //------------------------------------------------------------------------//
    
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
    public Component add( Component c )             { throw new IllegalAccessError(DONT_USE_ME); }
    public Component add( Component c, int n )      { throw new IllegalAccessError(DONT_USE_ME); }
    public Component add( String s, Component c )   { throw new IllegalAccessError(DONT_USE_ME); }
    public void add( Component c, Object o )        { throw new IllegalAccessError(DONT_USE_ME); }
    public void add( Component c, Object o, int n ) { throw new IllegalAccessError(DONT_USE_ME); }
    public void remove( Component c )               { throw new IllegalAccessError(DONT_USE_ME); }
    //------------------------------------------------------------------------------------------------
}