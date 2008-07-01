/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
 * @author fmorero
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
        DesktopTest test = new DesktopTest();
                    test.setVisible( true );
                    test.setExtendedState( JFrame.MAXIMIZED_BOTH );   // setExtendedState(...) only works after made visible
                    
        test.testModality();
    }
}