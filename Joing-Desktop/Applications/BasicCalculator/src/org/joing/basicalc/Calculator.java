/*
 * Copyright (C) 2007, 2008 Join'g Team Members. All Rights Reserved.
 * Join'g Team Members are listed at project's home page. By the time of 
 * writting this at: https://joing.dev.java.net/servlets/ProjectMemberList.
 *
 * This file is part of Join'g project: www.joing.org
 *
 * GNU Classpath is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the free
 * Software Foundation; either version 3, or (at your option) any later version.
 * 
 * GNU Classpath is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * GNU Classpath; see the file COPYING.  If not, write to the Free Software 
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */
package org.joing.basicalc;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JSplitPane;
import org.joing.kernel.api.desktop.DeskComponent;
import org.joing.kernel.api.desktop.DesktopManager;
import org.joing.kernel.api.desktop.pane.DeskFrame;

/**
 * This class implements a simple calculator pane.
 *
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
    
    public void showInFrame()
    {
        DesktopManager dm   = org.joing.kernel.jvmm.RuntimeFactory.getPlatform().getDesktopManager();
        ImageIcon      icon = new ImageIcon( getClass().getResource( "images/calculator.png" ) );
        
        // Show this panel in a frame created by DesktopManager Runtime.
        DeskFrame frame = dm.getRuntime().createFrame();
                  frame.setTitle( "Basic Calculator" );
                  frame.setIcon( icon.getImage() );
                  frame.add( (DeskComponent) this );

        dm.getDesktop().getActiveWorkArea().add( frame );
    }
    
    //------------------------------------------------------------------------//
    
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
        (new Calculator()).showInFrame();
    }
}