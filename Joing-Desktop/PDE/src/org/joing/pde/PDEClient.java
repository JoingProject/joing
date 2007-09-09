/*
 * Main.java
 *
 * Created on 9 de septiembre de 2007, 7:51
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.joing.pde;

import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;
import javax.swing.JApplet;
import javax.swing.JComponent;
import javax.swing.JFrame;
import org.joing.Client;
import org.joing.desktop.api.Desktop;
import org.joing.pde.desktop.DesktopImpl;
import org.joing.pde.desktop.LauncherImpl;
import org.joing.pde.desktop.workarea.containers.JoingFrame;
import org.joing.pde.desktop.workarea.items.WorkAreaLauncher;

/**
 * Client interface implementation.<br>
 * The application entry point (main equivalent).
 *
 * @author Francisco Morero Peyrona
 */
public class PDEClient extends JApplet implements Client
{
    private Desktop desktop;
    private JFrame  frame;
    
    public PDEClient()
    {
        desktop = new DesktopImpl();
        
        getContentPane().add( (JComponent) desktop, BorderLayout.CENTER );
    }
    
    //------------------------------------------------------------------------//
    
    public Applet getApplet()
    {
        return this;
    }
    
    public void showInFrame()
    {
        getMainFrame().setVisible( true );
desktop.getActiveWorkArea().addItem( (Component) new WorkAreaLauncher() );

JoingFrame frm = new JoingFrame( "Join'g Frame" );
           frm.setBounds( 100, 100, 600, 400 );
           frm.setSize( 600, 400 );
           frm.setVisible( true );
desktop.getActiveWorkArea().addContainer( frm );
frm.setSelected( true );
    }
    
    public void showInFullScreen()
    {
        /* TODO: hacerlo
        frame = getMainFrame();
        frame.setDefaultLookAndFeelDecorated( false );
        init();
        start();
               
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice      gs = ge.getDefaultScreenDevice();
        
        gs.setFullScreenWindow( frame );
        frame.validate();*/
    }
    
    //------------------------------------------------------------------------//
    
    private JFrame getMainFrame()
    {
        if( frame == null )
        {
            Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
            
            frame = new JFrame( "Joing :: PDE" );
            frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
            frame.getContentPane().add( getContentPane() );
            frame.pack();
            frame.setSize( new Dimension( dim.width, dim.height ) );
            init();
            start();
        }
        
        return frame;
    }
    
    public static void main( String[] args )
    {
        PDEClient pde = new PDEClient();
                  pde.showInFrame();
    }
}