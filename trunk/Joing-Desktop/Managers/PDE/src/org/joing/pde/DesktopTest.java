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
package org.joing.pde;

import java.awt.BorderLayout;
import java.awt.Container;
import java.lang.reflect.Method;
import javax.swing.JButton;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JTextArea;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;

/**
 *
 * @author Francisco Morero Peyrona
 */

// See: http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=6506360

public class DesktopTest extends JFrame
{
    private JDesktopPane desktop = new JDesktopPane();
    
    public DesktopTest()
    {
        super( "Desktop Test" );
        setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        add( desktop );
    }
    
    //------------------------------------------------------------------------//
    
    private void testModality()
    {        
        JInternalFrame frame = new JInternalFrame( "Frame", true, true, true, true );
                       frame.add( new JButton( "Frame Button" ) );
                       frame.setBounds( 20, 20, 400, 300 );
                       
        // Dialogs should be closable only 
        JInternalFrame dialog = new JInternalFrame( "Dialog", false, true, false, false );
                       dialog.setBounds( 100, 100, 400, 300 );
                       dialog.add( new JTextArea( "Text" ), BorderLayout.CENTER );
                       dialog.add( new JButton( "Button" ), BorderLayout.SOUTH );
                       dialog.addInternalFrameListener( new InternalFrameAdapter()
                        {
                            public void internalFrameClosing( InternalFrameEvent e )
                            {
                                try
                                {    
                                    Method method = Container.class.getDeclaredMethod( "stopLWModal", (Class[]) null );
                                    method.setAccessible( true );
                                    method.invoke( (JInternalFrame) e.getSource(), (Object[]) null );
                                }
                                catch( Exception exc )
                                {
                                    exc.printStackTrace();
                                }
                            }
                        } );
                
        desktop.add( frame , JDesktopPane.DEFAULT_LAYER );
        desktop.add( dialog, JDesktopPane.MODAL_LAYER );
        
        desktop.validate(); 
        
        frame.setVisible( true );
        dialog.setVisible( true );
        
        try
        {   // Starts modal
            Method method = Container.class.getDeclaredMethod( "startLWModal", (Class[]) null );
            method.setAccessible( true );
            method.invoke( dialog, (Object[]) null );
        }
        catch( Exception exc )
        {
            exc.printStackTrace();
        }
        
        System.out.println( "App never arrives to this line" );
    }
    
    //------------------------------------------------------------------------//
    
    public static void main( String[] as )
    {
        java.io.File f = new java.io.File( "/home/francisco/tusmulas" );
        System.out.println( f + " exists = "+ f.exists() );
        System.exit(0);
        
        DesktopTest test = new DesktopTest();
                    test.setVisible( true );
                    test.setExtendedState( JFrame.MAXIMIZED_BOTH );   // setExtendedState(...) only works after made visible
                    
        test.testModality();
    }
}