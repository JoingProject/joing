/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.joing.pde.desktop.deskwidget.desklet;

import java.awt.Component;
import java.awt.Dimension;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import org.joing.common.desktopAPI.workarea.WorkArea;
import org.joing.pde.PDEUtilities;
import org.joing.pde.desktop.deskwidget.PDEDeskWidget;

/**
 * This is the base class to create DeskApplets: (normally) small applications
 * that are shown on the desktop.
 * <p>
 * There are to ways to create a DeskApplet:
 * <ul>
 * <li>
 * Inheriting from this class. Which allows to use all events.
 * </li>
 * <li>
 * Creating an instance of this class and adding a JPanel to this class:<br>
 * <pre>
 * MyPanel myPanel = new MyPanel();
 * PDEDeskApplet deskApplet = new PDEDeskApplet();
 *               deskApplet.add( myPanel );
 *               deskApplet.setBounds( 10,10, myPanel.getPreferredSize().width, 
 *                                            myPanel.getPreferredSize().height );
 * PDERuntime.getRuntime().getDesktopManager().getDesktop().
 *            getActiveWorkArea().add( deskApplet );
 * </pre>
 * </li>
 * </ul>
 * @author Francisco Morero Peyrona
 */
public abstract class PDEDesklet extends PDEDeskWidget //implements 
{
    private DeskAppletToolBar toolBar;
    
    public PDEDesklet()
    {
        init();
    }
    
    public Dimension getMinimumSize()
    {
        Dimension dimMin = toolBar.getMinimumSize();
                  dimMin.width *= 2;
                  
        return dimMin;
    }
    
    public void close()
    {
        onClose();
    }
    
    // TODO: Mirar aquí 
    // http://msdn.microsoft.com/msdnmag/issues/07/08/SideBar/default.aspx?loc=es
    //------------------------------------------------------------------------//
    
    protected void onShow()
    {
        // Empty
    }
    
    protected void onGrow()
    {
        // Empty
    }
    
    protected void onReduce()
    {
        // Empty
    }
    
    /**
     * As this class just removes the DeskApplet from Subclasses should overwrite this method
     */
    protected void onClose()
    {
        WorkArea wa = PDEUtilities.findWorkAreaFor( this );
        
        wa.remove( this );
        ((Component) wa).repaint( getX(), getY(), getWidth(), getHeight() );
    }
    
    protected void onSetup()
    {
        // Empty
    }
    
    protected void toogleSizeButton()
    {
        // TODO; mirar esto --> toolBar.onSize();
    }
    
    // For user custom buttons
    protected void add( PDEDeskletButton button )
    {
        toolBar.add( button );
    }

    // For user custom buttons
    protected void remove( PDEDeskletButton button )
    {
        toolBar.remove( button );
    }
    
    // For standard buttons
    protected void remove( PDEDesklet.ToolBarButton btn )
    {
        toolBar.remove( btn );
    }
    
    //------------------------------------------------------------------------//
    
    void setToolBarVisible( boolean b )
    {
        toolBar.setVisible( b );
    }
    
    //------------------------------------------------------------------------//
    
    private void init()
    {
        toolBar = new DeskAppletToolBar( this );
        Dimension dimTB = toolBar.getMinimumSize();
        toolBar.setBounds( 0, 0, dimTB.width, dimTB.height );
        toolBar.setVisible( false );
        
        // Has to be added to root pane
        root.add( toolBar );
        root.setComponentZOrder( toolBar, 0 );
        root.setGlassPane( new GlassPaneDesklet( this ) );
        
        addAncestorListener( new AncestorListener()
        {
            public void ancestorAdded(   AncestorEvent ae ) { PDEDesklet.this.onShow(); }
            public void ancestorMoved(   AncestorEvent ae ) {}
            public void ancestorRemoved( AncestorEvent ae ) { PDEDesklet.this.onClose(); }
        } );
    }

    //------------------------------------------------------------------------//
    // INNER CLASS: Enumeration
    //------------------------------------------------------------------------//

    public enum ToolBarButton
    {
        SIZE, CLOSE, SETUP;
    }
}