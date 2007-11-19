/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.joing.pde.desktop.deskwidget.desklet;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

    private ImageIcon iconGrow;
    private ImageIcon iconReduce;

    private PDEDesklet owner;
    
    //--------------------------------------------------------------------//

    DeskAppletToolBar( PDEDesklet owner )
    {
        this.owner = owner;
        
        setBorder( new EmptyBorder( 3,2,3,2 ) );
        setLayout( new GridLayout( 0,1,0,3 ) );
        
        // I make this just to obtain the icons
        iconGrow   = (ImageIcon) (new PDEDeskletButton( "expand", "" )).getIcon();
        iconReduce = (ImageIcon) (new PDEDeskletButton( "reduce", "" )).getIcon();

        // Initializes all buttons
        btnSize = new PDEDeskletButton( "expand", "Expand" ); // For both 'grow' and 'reduce'
        btnSize.addActionListener( this );
        super.add( btnSize );   // Faster than callin this::add(...)
                
        btnSetup = new PDEDeskletButton( "setup", "Configuration"  );
        btnSetup.addActionListener( this );
        super.add( btnSetup );   // Faster than callin this::add(...)
        
        btnClose = new PDEDeskletButton( "close", "Close" );
        btnClose.addActionListener( this );
        super.add( btnClose );   // Faster than callin this::add(...)
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

        return new Dimension( 16, ((nButtons*12) + ((nButtons-1)*3) + 6) );
    }
    
    public void actionPerformed( ActionEvent ae )
    {
        Object sender = ae.getSource();

        if(      sender == btnSize  )  { onSize();        }
        else if( sender == btnSetup )  { owner.onSetup(); }
        else if( sender == btnClose )  { owner.onClose(); }
    }
    
    //------------------------------------------------------------------------//
    
    // For user custom buttons (called by PDEDesklet)
    void add( PDEDeskletButton button )
    {
        super.add( button, 0 );
        validate();
    }

    // For user custom buttons (called by PDEDesklet)
    void remove( PDEDeskletButton button )
    {
        super.remove( button );
        validate();
    }

    // For standard buttons (called by PDEDesklet)
    void remove( PDEDesklet.ToolBarButton btn )
    {
        switch( btn )
        {
            case SIZE : remove( btnSize  ); break;
            case CLOSE: remove( btnClose ); break;
            case SETUP: remove( btnSetup ); break;
        }
        
        validate();
    }
    
    //------------------------------------------------------------------------//
    
    private void onSize()
    {
        if( btnSize.getIcon() == iconGrow )
        {
            btnSize.setName( "REDUCE" );
            btnSize.setToolTipText( "Reduce" );
            btnSize.setIcon( iconReduce );
            owner.onReduce();
        }
        else
        {
            btnSize.setName( "GROW" );
            btnSize.setIcon( iconGrow );
            btnSize.setToolTipText( "Expand" );
            owner.onGrow();
        }
    }
}