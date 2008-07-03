/*
 * Copyright (c) 2002 CHS Data Systems GmbH, All Rights Reserved.
 *
 * Permission to use, copy and modify; but NOT to distribute this source code
 * either for NON-COMMERCIAL or COMMERCIAL purposes without the explicit
 * written permission of CHS Data Systems GmbH.
 *
 * Chs makes no representations or warranties about the suitability of
 * the software, either express or implied, including but not limited
 * to the implied warranties of merchantability, fitness for a
 * particular purpose, or non-infringement. Chs shall not be liable for
 * any damages suffered by licensee as a result of using, modifying or
 * distributing this software or its derivatives.
 */
package org.joing.basicalc;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JSplitPane;
import org.joing.common.desktopAPI.DeskComponent;
import org.joing.common.desktopAPI.DesktopManager;
import org.joing.common.desktopAPI.pane.DeskFrame;

/**
 * This class implements a simple calculator pane.
 *
 * Copyright (c) CHS 2001
 * @author Francisco Morero
 * @version 1.0
 */
class Calculator extends JSplitPane implements ActionListener, DeskComponent
{
    private SimpleCalculator calc  = new SimpleCalculator();
    private PaperPanel       paper = new PaperPanel();

    /**
     * Zero argument constructor
     */
    public Calculator()
    {
        init();
        calc.addActionListener( this );
        showInFrame();
    }

    /**
     * Forced by implementing ActionListener
     * @param op action event
     */
    // I use this event to save creating a new event type (OperationPerformedEvent)
    public void actionPerformed( ActionEvent op )
    {
        String sCmd = op.getActionCommand();

        if( sCmd.equals( "RESET" ) )
            this.paper.reset();
        else
            this.paper.addLine( sCmd );
    }
    
    //------------------------------------------------------------------------//
    
    private void showInFrame()
    {
        DesktopManager dm   = org.joing.jvmm.RuntimeFactory.getPlatform().getDesktopManager();
        ImageIcon      icon = new ImageIcon( getClass().getResource( "images/calculator.png" ) );
        
        if( dm != null )
        {
            // Show this panel in a frame created by DesktopManager Runtime.
            DeskFrame frame = dm.getRuntime().createFrame();
                      frame.setTitle( "Basic Calculator" );
                      frame.setIcon( icon.getImage() );
                      frame.add( (DeskComponent) this );
        
            dm.getDesktop().getActiveWorkArea().add( frame );
        }
        else
        {
            JFrame frm = new JFrame();
                   frm.setIconImage( icon.getImage() );
                   frm.add( this );
                   frm.pack();
                   frm.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
                   frm.setVisible( true );
        }
    }
    
    private void init()
    {
        setLeftComponent( paper );
        setRightComponent( calc );
        setDividerLocation( 110 );
        setDividerSize( 4 );
    }
    
    //------------------------------------------------------------------------//
    
    public static void main( String[] args )
    {
        new Calculator();
    }
}