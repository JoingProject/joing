/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.joing.pde.desktop.deskwidget.desklet;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.border.EmptyBorder;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import org.joing.pde.PDEManager;
import org.joing.pde.desktop.deskwidget.PDEDeskWidget;
import org.joing.pde.swing.JRoundPanel;

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
public abstract class PDEDesklet extends PDEDeskWidget
{
    private DeskAppletToolBar toolBar;
    
    public PDEDesklet()
    {
        init();
    }
    
    public Dimension getMinimumSize()
    {
        return toolBar.getMinimumSize();
    }
    
    // TODO: Mirar aquí 
    // http://msdn.microsoft.com/msdnmag/issues/07/08/SideBar/default.aspx?loc=es
    //------------------------------------------------------------------------//
    
    protected void onShow()   // TODO: hacer que se llame este método
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
        PDEManager.getInstance().getDesktop().remove( this );
    }
    
    protected void onSetup()
    {
        // Empty
    }
    
    protected void onDrag()
    {
        // TODO: hacerlo
    }
    
    protected void toogleSizeButton()
    {
        //toolBar.onSize();
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
    protected void remove( ToolBarButton btn )
    {
        switch( btn )
        {
            case SIZE : toolBar.remove( toolBar.btnSize  ); break;
            case CLOSE: toolBar.remove( toolBar.btnClose ); break;
            case SETUP: toolBar.remove( toolBar.btnSetup ); break;
            case DRAG : toolBar.remove( toolBar.btnDrag  ); break;
        }
    }
    
    //------------------------------------------------------------------------//
    
    private void init()
    {
        toolBar = new DeskAppletToolBar();
        Dimension dimTB = toolBar.getMinimumSize();
        toolBar.setBounds( 0, 0, dimTB.width, dimTB.height );
        
        // Has to be added to root pane
        root.add( toolBar );
        root.setComponentZOrder( toolBar, 0 );
        
        // Mouse Listener
        // FIXME: hacerlo vía GlassPane
        addMouseMotionListener( new MouseMotionAdapter()
        {
            public void mouseEntered( MouseEvent me )
            {
                toolBar.setVisible( true );
            }
            
            public void mouseExited( MouseEvent me )
            {
                toolBar.setVisible( false );
            }
        } );
        
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
        SIZE, CLOSE, SETUP, DRAG;
    }

    //------------------------------------------------------------------------//
    // INNER CLASS: MouseListener for ToolBarButton
    //------------------------------------------------------------------------//
    private class DeskAppletToolBar extends JRoundPanel implements ActionListener
    {
        private PDEDeskletButton btnSize;
        private PDEDeskletButton btnClose;
        private PDEDeskletButton btnSetup;
        private PDEDeskletButton btnDrag;
        
        private ImageIcon iconGrow;
        private ImageIcon iconReduce;
 
        //--------------------------------------------------------------------//
        
        private DeskAppletToolBar()
        {
            setBorder( new EmptyBorder( 1,1,1,1 ) );
            setLayout( new BoxLayout( this, BoxLayout.Y_AXIS ) );
            
            // I make this just to obtain the icons
            iconGrow   = (ImageIcon) (new PDEDeskletButton( "grow"  , "" )).getIcon();
            iconReduce = (ImageIcon) (new PDEDeskletButton( "reduce", "" )).getIcon();
            
            // Initializes all buttons
            btnSize = new PDEDeskletButton( "grow", "Expand" ); // For both 'grow' and 'reduce'
            btnSize.addActionListener( this );
            add( btnSize );
            add( Box.createRigidArea( new Dimension( 0,9 ) ) );
            
            btnClose = new PDEDeskletButton( "close", "Close" );
            btnClose.addActionListener( this );
            add( btnClose );
            add( Box.createRigidArea( new Dimension( 0,9 ) ) );

            btnSetup = new PDEDeskletButton( "setup", "Configuration"  );
            btnSetup.addActionListener( this );
            add( btnSetup );
            add( Box.createRigidArea( new Dimension( 0,9 ) ) );
            
            btnDrag = new PDEDeskletButton( "drag", "Drag (move)"  );
            btnDrag.addActionListener( this );
            add( btnDrag );
        }
        
        private void onSize()
        {
            if( btnSize.getIcon() == iconGrow )
            {
                btnSize.setName( "REDUCE" );
                btnSize.setToolTipText( "" );
                btnSize.setIcon( iconReduce );
                onReduce();
            }
            else
            {
                btnSize.setName( "GROW" );
                btnSize.setIcon( iconGrow );
                btnSize.setToolTipText( "Expands " );
                onGrow();
            }
        }
        
        private void add( PDEDeskletButton button )
        {
            super.add( button, 0 );
            //validate();
        }

        // For user custom buttons
        private void remove( PDEDeskletButton button )
        {
            super.remove( button );
            //validate();
        }
        
        public Dimension getPreferredSize()
        {
            return getMinimumSize();
        }
        
        public Dimension getMaximumSize()
        {
            return getMinimumSize();
        }
        
        public Dimension getMinimumSize()
        {
            Component[] aComp    = getComponents();
            int         nButtons = 0;
            
            for( int n = 0; n < aComp.length; n++ )
            {
                if( aComp[n] instanceof PDEDeskletButton )
                    nButtons++;
            }
            
            return new Dimension( 16, ((nButtons*12) + ((nButtons-1)*3) + 4) );
        }

        public void actionPerformed( ActionEvent ae )
        {
            Object sender = ae.getSource();
            
            if(      sender == btnSize )  { onSize();  }
            else if( sender == btnSize )  { onClose(); }
            else if( sender == btnSize )  { onSetup(); }
            else if( sender == btnSize )  { onDrag();  }
        }
    }
}