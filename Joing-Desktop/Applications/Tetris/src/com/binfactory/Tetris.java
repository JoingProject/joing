/*
 * @(#)Main.java
 *
 * This work is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License as
 * published by the Free Software Foundation; either version 2 of
 * the License, or (at your option) any later version.
 *
 * This work is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * Copyright (c) 2003 Per Cederberg. All rights reserved.
 */

package com.binfactory;

import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;

import com.binfactory.client.desktop.DesktopFrame;
import com.binfactory.client.runtime.Runtime;

/**
 * The main class of the Tetris game. This class contains the necessary methods to run the game.
 * 
 * @version 1.2
 * @author Per Cederberg, per@percederberg.net
 */
public class Tetris extends DesktopFrame
{
    // The Tetris game being played.
    private Game game = null;

    public Tetris()
    {
        super( "Tetris" );
        
        setFrameIcon( Runtime.getIcon( this, "tetris.png", 22,22 ) );
        
        this.game = new Game();

        add( this.game.getComponent() );
        pack();
        
        addInternalFrameListener( new InternalFrameListener() 
            {
                public void internalFrameOpened( InternalFrameEvent e )
                {
                }
                public void internalFrameClosing( InternalFrameEvent e )
                {
                   Tetris.this.game.quit();
                }
                public void internalFrameClosed( InternalFrameEvent e )
                {
                }
                public void internalFrameIconified( InternalFrameEvent e )
                {
                }
                public void internalFrameDeiconified( InternalFrameEvent e )
                {   
                }
                public void internalFrameActivated( InternalFrameEvent e )
                {   
                }
                public void internalFrameDeactivated( InternalFrameEvent e )
                {   
                }
            } );
        
        Runtime.getDesktop().add( this );
    }
    
    /**
     * The stand-alone main routine.
     * 
     * @param args the command-line arguments
     */
    public static void main( String[] args )
    {
        new Tetris();
        
        /*Tetris t = new Tetris();
        
        javax.swing.JDesktopPane desktop = new javax.swing.JDesktopPane();
                                 desktop.add( t, new Integer( 2 ) );
                                 
        t.setLocation( 180,90 );
        t.setVisible( true );
                     
        javax.swing.JFrame frame = new javax.swing.JFrame( "Documents Test" );
                           frame.setContentPane( desktop );
                           frame.setSize( 1024, 768 );
                           frame.setDefaultCloseOperation( javax.swing.JFrame.EXIT_ON_CLOSE );
                           frame.setVisible( true );*/
    }
}