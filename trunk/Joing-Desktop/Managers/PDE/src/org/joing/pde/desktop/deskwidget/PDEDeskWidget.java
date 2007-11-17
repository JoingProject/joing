/*
 * PDEDeskWidget.java
 *
 * Created on 17 de septiembre de 2007, 19:05
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.joing.pde.desktop.deskwidget;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.LayoutManager;
import java.awt.Point;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRootPane;
import org.joing.common.desktopAPI.deskwidget.DeskWidget;

/**
 * A Deskgadget is every component shown in the dektop that can be dragged 
 * (can change its position by the user interaction).
 * <p>
 * DeskLaunchers and Desklets are subclased from here.
 *
 * @author Francisco Morero Peyrona
 */
public class PDEDeskWidget extends JPanel implements DeskWidget
{
    protected JRootPane root;
    private   String    sDescription;
    
    //------------------------------------------------------------------------//
    
    /**
     * Creates a new instance of PDEDeskWidget
     */
    public PDEDeskWidget()
    {
        initGUI();
    }

    //------------------------------------------------------------------------//
    // Following methods from DeskWidget interface already exists in JPanel:
    // getName(), setName(...), getLocation(), setLocation(...), 
    
    public String getDescription()
    {
        return sDescription;
    }
    
    public void setDescription( String sDescription )
    {
        if( sDescription != null )
        {
            sDescription = sDescription.trim();
        
            if( sDescription.length() == 0 )
                sDescription = null;
        }
        
        this.sDescription = sDescription;
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
    
    protected void showPopup( Point ptWhere )
    {
        JPopupMenu popup = getComponentPopupMenu();
        
        if( popup != null && ! popup.isVisible() )
            popup.show( this, ptWhere.x, ptWhere.y );
    }
    
    //------------------------------------------------------------------------//
    
    private void initGUI()
    {
        setOpaque( false );
        
        root = new JRootPane();
        root.setGlassPane( new GlassPaneWidget( this ) );
        ((JPanel) root.getContentPane()).setOpaque( false );

        // Añado el root pane a este JPanel
        super.setLayout( new BorderLayout() );
        super.add( root, BorderLayout.CENTER );
    }
}