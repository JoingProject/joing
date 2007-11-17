/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.joing.pde.desktop.deskwidget.desklet;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.border.EmptyBorder;
import org.joing.pde.swing.JRoundPanel;

/**
 *
 * @author Francisco Morero Peyrona
 */

class DeskAppletToolBar extends JRoundPanel implements ActionListener
{
    private PDEDeskletButton btnSize;
    private PDEDeskletButton btnClose;
    private PDEDeskletButton btnSetup;
    private PDEDeskletButton btnDrag;

    private ImageIcon iconGrow;
    private ImageIcon iconReduce;

    private PDEDesklet owner;
    
    //--------------------------------------------------------------------//

    DeskAppletToolBar( PDEDesklet owner )
    {
        this.owner = owner;
        
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

        if(      sender == btnSize )  { onSize();        }
        else if( sender == btnSize )  { owner.onClose(); }
        else if( sender == btnSize )  { owner.onSetup(); }
        else if( sender == btnSize )  { owner.onDrag();  }
    }
    
    //------------------------------------------------------------------------//
    
    // For user custom buttons (called by PDEDesklet)
    void add( PDEDeskletButton button )
    {
        super.add( button, 0 );
        //validate();
    }

    // For user custom buttons (called by PDEDesklet)
    void remove( PDEDeskletButton button )
    {
        super.remove( button );
        //validate();
    }

    // For standard buttons (called by PDEDesklet)
    void remove( PDEDesklet.ToolBarButton btn )
    {
        switch( btn )
        {
            case SIZE : remove( btnSize  ); break;
            case CLOSE: remove( btnClose ); break;
            case SETUP: remove( btnSetup ); break;
            case DRAG : remove( btnDrag  ); break;
        }
    }
    
    //------------------------------------------------------------------------//
    
    private void onSize()
    {
        if( btnSize.getIcon() == iconGrow )
        {
            btnSize.setName( "REDUCE" );
            btnSize.setToolTipText( "" );
            btnSize.setIcon( iconReduce );
            owner.onReduce();
        }
        else
        {
            btnSize.setName( "GROW" );
            btnSize.setIcon( iconGrow );
            btnSize.setToolTipText( "Expands " );
            owner.onGrow();
        }
    }
}