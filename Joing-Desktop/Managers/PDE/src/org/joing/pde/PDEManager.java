/*
 * Main.java
 *
 * Created on 9 de septiembre de 2007, 7:51
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.joing.pde;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Component;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import javax.swing.JPanel;
import javax.swing.Timer;
import org.joing.common.clientAPI.jvmm.Platform;
import org.joing.pde.desktop.PDEDesktop;
import java.awt.BorderLayout;
import java.awt.Toolkit;
import javax.swing.JApplet;
import javax.swing.JFrame;
import org.joing.common.desktopAPI.DesktopManager;
import org.joing.common.clientAPI.runtime.Bridge2Server;

/**
 * DesktopManager interface implementation.
 * <p>
 * The only one instance of this class that exists in PDE is holded by 
 * PDERuntime and is private and is not shared. Therefore the only statment in
 * PDE that can access PDEManager is PDERuntime.
 * 
 * @author Francisco Morero Peyrona
 */
public class PDEManager extends JApplet implements DesktopManager
{
    private Platform   platform;
    private PDEDesktop desktop;
    private PDERuntime runtime;
    private JFrame     frame;
    
    private static PDEManager instance;
    
    //------------------------------------------------------------------------//
    
    public PDEManager()
    {
        if( instance == null )
        {
            instance = this;
            runtime  = new PDERuntime();
            desktop  = new PDEDesktop();
            getContentPane().add( desktop, BorderLayout.CENTER );

            // Continuous layout on frame resize
            Toolkit.getDefaultToolkit().setDynamicLayout( true );
        }
        else
        {
            throw new IllegalStateException( "DesktopManager is already built: use getInstance()" );
        }
    }
    
    public static PDEManager getInstance()
    {
        return instance;
    }
    
    //------------------------------------------------------------------------//
    
    public void init()
    {
    }
    
    public void start()
    {
    }
    
    public void stop()
    {
    }
    
    //------------------------------------------------------------------------//
    // Methods defined in DesktopManager interface
    
    public void showInFrame()
    {
        getMainFrame().pack();
        getMainFrame().setVisible( true );
        
        if( Toolkit.getDefaultToolkit().isFrameStateSupported( Frame.MAXIMIZED_BOTH ) )
            getMainFrame().setExtendedState( Frame.MAXIMIZED_BOTH );
        else
            getMainFrame().setSize( Toolkit.getDefaultToolkit().getScreenSize() );
        
        getDesktop().load();   // Do not move this line !
    }
    
    public void showInFullScreen()
    {
        getMainFrame().setUndecorated( true );
        getMainFrame().setResizable( false );
        getMainFrame().pack();
        
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice      gs = ge.getDefaultScreenDevice();
        
        try
        {
            if( gs.isFullScreenSupported() )
                gs.setFullScreenWindow( frame );
            else
                getMainFrame().setVisible( true );
        }
        catch( Exception exc )
        {
            exc.printStackTrace();
        }
        
        getDesktop().load();   // Do not move this line !
    }
    
    public PDEDesktop getDesktop()
    {
        return desktop;
    }
    
    public PDERuntime getRuntime()
    {
        return runtime;
    }
    
    public Bridge2Server getBridge()
    {
        return platform.getBridge();
    }
    
    public void close()
    {
        getDesktop().close();

        // TODO: Quizás, tras enviar la orden al Desktop para que cierre todas sus
        //       WorkAreas, habría que crear aquí una thread (o un bucle) donde se comprueba
        //       cada cierto tiempo que todas las Frames están cerradas, y si pasado
        //       un tiempo máximo, las que aún no están cerradas, se cierran a las bravas.
        
        if( frame != null )   // Can't call getFrame(), because this method constructs the frame
            frame.dispose();
        else
            stop();
    }
    
    public void lock()
    {
        Component   previous = (frame != null ? frame.getGlassPane() : getRootPane().getGlassPane());
        MyGlassPane myGlass  = new MyGlassPane( previous );
        
        if( frame != null )
            frame.setGlassPane( myGlass );
        else
            getRootPane().setGlassPane( myGlass );
        
        myGlass.lock();
    }
    
    private void unlock()
    {
        if( runtime.askForPassword() )
        {
            MyGlassPane myGlass = (MyGlassPane) (frame != null ? frame.getGlassPane()
                                                               : getRootPane().getGlassPane());
            
            if( frame != null )
                frame.setGlassPane( myGlass.getPreviousGlassPane() );
            else
                getRootPane().setGlassPane( myGlass.getPreviousGlassPane() );
            
            myGlass.unlock();
        }
    }
   
    public void setPlatform( Platform platform )
    {
        this.platform = platform;
    }
        
    //------------------------------------------------------------------------//
    
    private JFrame getMainFrame()
    {
        if( frame == null )
        {
            frame = new JFrame( "Joing :: PDE" );
            frame.setDefaultCloseOperation( JFrame.DO_NOTHING_ON_CLOSE );
            frame.setContentPane( getContentPane() );
            frame.addWindowListener( new WindowListener()
            {
                public void windowActivated( WindowEvent we )   {}
                public void windowClosed( WindowEvent we )      {}
                public void windowClosing( WindowEvent we )     { PDEManager.this.close(); }
                public void windowDeactivated( WindowEvent we ) {}
                public void windowDeiconified( WindowEvent we ) {}
                public void windowIconified( WindowEvent we )   {}
                public void windowOpened( WindowEvent we )      {}
            } );
        }
        
        return frame;
    }
    
    //------------------------------------------------------------------------//
    // INNER CLASS: MyGlassPane
    // Used to lock the screen.
    //------------------------------------------------------------------------//
    
    private final class MyGlassPane
            extends JPanel 
            implements MouseListener, ActionListener
    {
        private Timer     timer;
        private float     nTranslucent; 
        private Component previousGlassPane;
        
        private MyGlassPane( Component previousGlassPane )
        {
            this.previousGlassPane = previousGlassPane;
            this.timer = new Timer( 60, this );
        }
        
        private synchronized void lock()
        {
            nTranslucent = 0f;
            setVisible( true );
            addMouseListener( this );
            timer.start();
        }
        
        private synchronized void unlock()
        {
            timer.stop();
            setVisible( false );
            removeMouseListener( getMouseListeners()[0] );
        }
        
        private Component getPreviousGlassPane()
        {
            return previousGlassPane;
        }
        
        public void actionPerformed( ActionEvent ae )
        {
            nTranslucent += .01;
            
            if( nTranslucent <= .4f )  // At .4 it is totally opaque
                repaint();
            else
                timer.stop();
        }
        
        protected void paintComponent( Graphics g )
        {
            Graphics2D g2 = (Graphics2D) g;

            g2.setColor( Color.black );
            g2.setComposite( AlphaComposite.getInstance( AlphaComposite.SRC_OVER, nTranslucent ));
            g2.fillRect( 0,0, getWidth(), getHeight() );
            g2.setColor( Color.white );
            g2.drawString( "Click to unlock", 15,15 );
            g2.dispose();         
        }
        
        //--------------------------------------------------------------------//
        // Listeners
        
        public void mouseClicked( MouseEvent me )  {}
        public void mouseEntered( MouseEvent me )  {}
        public void mouseExited( MouseEvent me )   {}
        public void mouseReleased( MouseEvent me ) {}
        public void mousePressed( MouseEvent me )  { PDEManager.this.unlock(); }
    }
}