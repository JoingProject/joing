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

package org.joing.tetris;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.event.InternalFrameEvent;
import javax.swing.ImageIcon;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.event.InternalFrameAdapter;
import org.joing.kernel.api.desktop.DeskComponent;
import org.joing.kernel.api.desktop.DesktopManager;
import org.joing.kernel.api.desktop.pane.DeskFrame;

/**
 * The main class of the Tetris game. This class contains the necessary methods to run the game.
 * 
 * @version 1.2
 * @author Per Cederberg, per@percederberg.net
 */
public class Tetris extends JPanel implements DeskComponent
{
    private Game game = null;   // The Tetris game being played.
    
    public Tetris()
    {
        game = new Game();
        add( game.getComponent() );
    }
    
    public void showInFrame()
    {
        DesktopManager dm   = org.joing.kernel.jvmm.RuntimeFactory.getPlatform().getDesktopManager();
        ImageIcon      icon = new ImageIcon( getClass().getResource( "tetris.png" ) );
        
        if( dm != null )
        {
            // Show this panel in a frame created by DesktopManager Runtime.
            DeskFrame frame = dm.getRuntime().createFrame();
                      frame.setTitle( "Tetris" );
                      frame.setIcon( icon.getImage() );
                      frame.add( (DeskComponent) this );
                      ((JInternalFrame) frame).addInternalFrameListener( new InternalFrameAdapter()
                         {
                            public void internalFrameClosed( InternalFrameEvent e )
                            {
                                Tetris.this.game.quit();
                            }
                         } );
            dm.getDesktop().getActiveWorkArea().add( frame );
        }
        else
        {
            javax.swing.JFrame frame = new javax.swing.JFrame();
                               frame.add( game.getComponent() );
                               frame.setIconImage( icon.getImage() );
                               frame.pack();
                               frame.setDefaultCloseOperation( javax.swing.JFrame.EXIT_ON_CLOSE );
                               frame.setVisible( true );
                               frame.addWindowListener( new WindowAdapter()
                               {                
                                   public void windowClosed( WindowEvent e )
                                   {
                                       Tetris.this.game.quit();
                                   }
                               } ); 
        }
    }
    
    //------------------------------------------------------------------------//
    
    /**
     * The stand-alone main routine.
     * 
     * @param args the command-line arguments
     */
    public static void main( String[] args )
    {
        (new Tetris()).showInFrame();
    }
}