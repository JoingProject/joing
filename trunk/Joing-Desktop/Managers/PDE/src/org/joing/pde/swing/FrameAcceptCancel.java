/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.joing.pde.swing;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import org.joing.pde.desktop.container.PDEFrame;

/**
 * Shows a Frame with buttons [Accept] and [Cancel].
 * 
 * Accept button invokes onAccept() method. In this implementation, <code>OnAccept()</code> 
 * and <code>OnCancel()</code> methods simply close the frame.
 * They can (snd should) be re-defined by sub clases.
 * <pre><code>
 * FrameAcceptCancel frame = new FrameAcceptCancel( "Window Title", jpanel )
        {
            public void onAccept()
            {
                // Apply changes
                super.onAccept();
            }
        };
 * </pre></code>
 * 
 * @author Francisco Morero Peyrona
 */
public class FrameAcceptCancel extends PDEFrame
{
    public static final int ACCEPTED  = 1;
    public static final int CANCELLED = 0;
    
    private boolean bExitWithAccept = false;
    private JButton btnAccept       = new JButton();
    private JButton btnCancel       = new JButton();
            
    //------------------------------------------------------------------------//
    
    /**
     * Class constructor.
     * <p>
     * Accept button invokes onAccept() method. At this level, the method simply
     * closes the frame, but can (and should) be re-defined by subclasses.
     * <p>
     * Cancel button invokes onCancel() method. At this level, the method simply
     * closes the frame, but can (and should) be re-defined by subclasses.
     * 
     * @param sTitle Frame title. If null an empty title will be used.
     * @param content Panel to be shown.
     */
    public FrameAcceptCancel( String sTitle, JComponent content )
    {
        super();
        
        btnAccept = new JButton( "Accept" );
        btnAccept.addActionListener( new ActionListener()
            {
                public void actionPerformed( ActionEvent ae )
                {
                    onAccept();
                }
            } );

        btnCancel = new JButton( "Cancel" );
        btnCancel.addActionListener( new ActionListener()
            {
                public void actionPerformed( ActionEvent ae )
                {
                    onCancel();
                }
            } );

        JPanel  pnlButtons = new JPanel( new FlowLayout( FlowLayout.TRAILING, 5, 0 ) );
                pnlButtons.setBorder( new EmptyBorder( 0, 10, 10, 10 ) );
                pnlButtons.add( btnAccept );
                pnlButtons.add( btnCancel );
        JPanel  pnlContent = new JPanel( new BorderLayout() );
                pnlContent.add( content   , BorderLayout.CENTER );
                pnlContent.add( pnlButtons, BorderLayout.SOUTH  );

        getContentPane().add( pnlContent, BorderLayout.CENTER );
        setDefaultCloseOperation( JInternalFrame.DISPOSE_ON_CLOSE );
        getRootPane().setDefaultButton( btnAccept );
        setTitle( (sTitle == null) ? "" : sTitle );
    }
    
    public void setAcceptText( String sAcceptText )
    {
        if( sAcceptText != null )
            btnAccept.setText( sAcceptText );
    }

    public void setCancelText( String sCancelText )
    {
        if( sCancelText != null )
            btnCancel.setText( sCancelText );
    }
    
    /**
     * 
     * Note: if this method is overwritten, you better call super, otherwise the
     * exit status value will not be the correct one and the frame will no close.
     */
    public void onAccept()
    {
        FrameAcceptCancel.this.bExitWithAccept = true;
        close();
    }

    public void onCancel()
    {
        close();
    }
}