/*
 * PDEDesklet.java
 *
 * Created on 17 de septiembre de 2007, 19:05
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.joing.pde.desktop.workarea.desklet;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.LayoutManager;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRootPane;
import javax.swing.event.MouseInputAdapter;
import org.joing.api.desktop.workarea.desklet.Desklet;

/**
 * A desklet is every gadget shown in the dektop that can be dragged (can change
 * its position by the user iteraction).
 * <p>
 * Launchers and DeskApplets are subclased from here.
 *
 * @author Francisco Morero Peyrona
 */
public class PDEDesklet extends JPanel implements Desklet
{
    private JRootPane root;
    
    private String    sDescription;
    
    //------------------------------------------------------------------------//
    
    /**
     * Creates a new instance of PDEDesklet
     */
    public PDEDesklet()
    {
        initGUI();
    }

    //------------------------------------------------------------------------//
    // Following methods from Desklet interface already exists in JPanel:
    // getName(), setName(...), getLocation(), setLocation(...), 
    
    public String getDescription()
    {
        return sDescription;
    }
    
    public void setDescription( String sDescription )
    {
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
        if( root != null )
            return root.getGlassPane();
        else
            return null; 
    }
    
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
        ((JPanel) root.getContentPane()).setOpaque( false );
        
        // Glass Pane: do not touch
        GlassPaneMouseListener gpml = new GlassPaneMouseListener();
        
        root.getGlassPane().setVisible( true );
        root.getGlassPane().addMouseListener( gpml );
        root.getGlassPane().addMouseMotionListener( gpml );
        ((JPanel) root.getGlassPane()).setOpaque( false );
        
        // Añado el root pane a este JPanel
        super.setLayout( new BorderLayout() );
        super.add( root, BorderLayout.CENTER );
    }

    //------------------------------------------------------------------------//
    // INNER CLASSES
    //------------------------------------------------------------------------//
    
    private final class GlassPaneMouseListener implements MouseListener, MouseMotionListener
    {            
        private Point ptMousePosition = new Point();
        
        // MouseListener -------------------------------------------------------
        
        public void mousePressed( MouseEvent me )  
        {
            if( me.getButton() == MouseEvent.BUTTON1 )
                ptMousePosition = me.getPoint();
                        
            // I prefer to handle events personaly in order to show the popup. 
            // See: http://www.jguru.com/forums/view.jsp?EID=1239349
            if( me.isPopupTrigger() )
                PDEDesklet.this.showPopup( me.getPoint() );
        }
        
        public void mouseClicked( MouseEvent me )  {}
        public void mouseReleased( MouseEvent me ) {}
        public void mouseEntered( MouseEvent me )  {}
        public void mouseExited( MouseEvent me )   {}
        
        // MouseMotionListener ------------------------------------------------- 
        
        public void mouseDragged( MouseEvent me )
        {
            if( ptMousePosition != null )
            {
                int x = me.getPoint().x + PDEDesklet.this.getX() - ptMousePosition.x;
                int y = me.getPoint().y + PDEDesklet.this.getY() - ptMousePosition.y;

                PDEDesklet.this.setLocation( x,y );
            }
        }
        
        public void mouseMoved( MouseEvent me ) {}
    }
}