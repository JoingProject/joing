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
import org.joing.pde.desktop.PDEDesktop;
import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Toolkit;
import javax.swing.ImageIcon;
import javax.swing.JApplet;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JRootPane;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import org.joing.common.clientAPI.runtime.Bridge2Server;
import org.joing.common.desktopAPI.DesktopManager;
import org.joing.common.desktopAPI.StandardImage;
import org.joing.common.desktopAPI.StandardSound;
import org.joing.jvmm.RuntimeFactory;

/**
 * DesktopManager interface implementation.
 * <p>
 * The only one instance of this class that exists in PDE is holded by the
 * Runtime (it is private and is not shared). Therefore the only statment that 
 * can access DesktopManager is Runtime.
 * <p>
 * In this way, applications will use Runtime to access DesktopManager 
 * funcionality (Runtome re-directs the calls to DesktopManager).
 * 
 * @author Francisco Morero Peyrona
 */
public class PDEManager extends JApplet implements DesktopManager
{
    private PDEDesktop desktop;
    private PDERuntime runtime;
    private JFrame     frame;
    
    //------------------------------------------------------------------------//
    
    public PDEManager()
    {
        runtime = new PDERuntime();
        desktop = new PDEDesktop();
        getContentPane().add( desktop, BorderLayout.CENTER );
        
        // Continuous layout on frame resize
        Toolkit.getDefaultToolkit().setDynamicLayout( true );
    }
    
    //------------------------------------------------------------------------//
    // Methods from JApplet
    
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
        try
        {
            SwingUtilities.invokeLater( new Runnable()
            {
                public void run()
                {
                    getMainFrame().pack();
                    getMainFrame().setVisible( true );

                    if( Toolkit.getDefaultToolkit().isFrameStateSupported( Frame.MAXIMIZED_BOTH ) )
                        getMainFrame().setExtendedState( Frame.MAXIMIZED_BOTH );
                    else
                        getMainFrame().setSize( Toolkit.getDefaultToolkit().getScreenSize() );
                    
                    desktop.restore();
                }
            } );
        }
        catch( Exception exc )
        {
            exc.printStackTrace();  // Shown only in console (not inside a JFrame/JDialog)
        }
    }
    
    public void showInFullScreen()
    {
        try
        {
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            GraphicsDevice      gs = ge.getDefaultScreenDevice();

            // NEXT: When bug is fixed
            // Due to a bug in Ubuntu and also in JDK, gs.isFullScreenSupported() always return false.
            if( true ) ///////// gs.isFullScreenSupported() )
            {
                getMainFrame().setUndecorated( true );
                getMainFrame().setResizable( false );
                getMainFrame().pack();
                getMainFrame().setVisible( true );
                
                gs.setFullScreenWindow( getMainFrame() );
                runtime.play( StandardSound.WELCOME );
                desktop.restore();
            }
            else
            {
                showInFrame();
            }
        }
        catch( Exception exc )
        {
            exc.printStackTrace();  // Shown only in console (not inside a JFrame/JDialog)
        }
    }
    
    public PDEDesktop getDesktop()
    {
        return desktop;
    }
    
    public PDERuntime getRuntime()
    {
        return runtime;
    }
    
    
    public void shutdown()
    {
        runtime.play( StandardSound.GOODBYE );
        getDesktop().close();

        // TODO: Quizás, tras enviar la orden al Desktop para que cierre todas sus
        //       WorkAreas, habría que crear aquí una thread (o un bucle) donde se comprueba
        //       cada cierto tiempo que todas las Frames están cerradas, y si pasado
        //       un tiempo máximo, las que aún no están cerradas, se cierran a las bravas.
        
        halt();
    }
    
    public void halt()
    {
        if( frame != null )   // Can't call getFrame(), because this method constructs the frame
            frame.dispose();
    }
    
    public String getName()
    {
        return "Peyrona Desktop Environment";
    }
    
    public String getVersion()
    {
        return "0.1";
    }
    
    public String getComercialInfo()
    {
        return "©Join'g Team - Published under GPLv3\n"+
               "Author: Francisco Morero Peyrona";
    }
    
    //------------------------------------------------------------------------//
    // exit() and lock() methods are called from the "start-menu" in the desktop
    
    /**
     * This method is inovked by start-menu item "Exit".<br>
     * This method simply invokes Platform::shutdown()
     */
    public void exit()
    {
        org.joing.jvmm.RuntimeFactory.getPlatform().shutdown();   // Calls this::shutdown()
    }

    /**
     * This method is inovked by start-menu item "Lock".<br>
     * It makes the desktop black and asks for password when user clicks on it.
     */
    public void lock()
    {
        Component   previous = (frame != null ? frame.getGlassPane() 
                                              : getRootPane().getGlassPane());
        MyGlassPane myGlass  = new MyGlassPane( previous, new AskForPassword() );
        
        if( frame != null )
            frame.setGlassPane( myGlass );
        else
            getRootPane().setGlassPane( myGlass );
        
        myGlass.lock();
    }
    
    public void unlock()
    {
        MyGlassPane myGlass = (MyGlassPane) (frame != null ? frame.getGlassPane()
                                                           : getRootPane().getGlassPane());
        myGlass.unlock();
        
        if( frame != null )
            frame.setGlassPane( myGlass.getPreviousGlassPane() );
        else
            getRootPane().setGlassPane( myGlass.getPreviousGlassPane() );
        
        myGlass = null;
    }
    
    //------------------------------------------------------------------------//
    
    private JFrame getMainFrame()
    {
        if( frame == null )
        {
            frame = new JFrame( "Join'g :: PDE - Version "+ getVersion() );
            frame.setIconImage( getRuntime().getImage( StandardImage.DESKTOP ) );
            frame.setDefaultCloseOperation( JFrame.DO_NOTHING_ON_CLOSE );
            frame.setContentPane( getContentPane() );
            frame.addWindowListener( new WindowListener()
            {
                public void windowActivated( WindowEvent we )   {}
                public void windowClosed( WindowEvent we )      {}
                public void windowClosing( WindowEvent we )     { PDEManager.this.exit(); }
                public void windowDeactivated( WindowEvent we ) {}
                public void windowDeiconified( WindowEvent we ) {}
                public void windowIconified( WindowEvent we )   {}
                public void windowOpened( WindowEvent we )      {}
            } );
        }
        
        return frame;
    }
    
    //------------------------------------------------------------------------//
    // INNER CLASS: AskForPassword
    //------------------------------------------------------------------------//
    private final class AskForPassword implements Runnable
    {
        public void run()
        {
            JPanel panel = new JPanel();
            JLabel lblPicture = new JLabel( new ImageIcon( getRuntime().getImage( StandardImage.USER_MALE, 32, 32 ) ) );
                   lblPicture.setBorder( new EmptyBorder( 8,8,8,8 ) );
                   
            JPasswordField txtPassword = new JPasswordField( 32 );
            
            panel.add( lblPicture , BorderLayout.WEST   );
            panel.add( txtPassword, BorderLayout.CENTER );
            
            if( PDEUtilities.showBasicDialog( "Enter your password", panel, "Unlock", "Lock" ) )
            {
                String sPassword = String.valueOf( txtPassword.getPassword() );
                
                if( sPassword != null && sPassword.trim().length() > 0 )
                {        
                    JRootPane root = (frame != null ? frame.getRootPane() : getRootPane());
                    Cursor    cursor = root.getCursor();

                    root.setCursor( new Cursor( Cursor.WAIT_CURSOR ) );

                    sPassword = sPassword.trim();

                    try
                    {
                        Bridge2Server b2s = RuntimeFactory.getPlatform().getBridge();
                        boolean       bOk = sPassword.length() > 0; // TODO: implementar un servicio en el servidor para comprobar la password

                        if( bOk )
                            unlock();
                    }
                    catch( Exception exc )
                    {
                        // TODO: Informar que por falta de comunicación con el servidor no se puede comprobar la password
                        //       y que el sistema tiene que seguir bloqueado.
                    }
                    finally
                    {
                        root.setCursor( cursor );
                    }
                }
            }
        }
    }
    
    //------------------------------------------------------------------------//
    // INNER CLASS: MyGlassPane. Used to lock the screen.
    // This GlasPane is not based on GlassPaneBase because in this way is more
    // efficient: it is visible only upon request and locks the screen.
    //------------------------------------------------------------------------//
    
    private final class MyGlassPane
            extends JPanel 
            implements MouseListener, ActionListener
    {
        private Timer     timer;
        private float     nTranslucent; 
        private Component previousGlassPane;
        private Runnable  callbackOnEvent;     // Object to be invoked on event:
                                               // Is Runnable just to avoid the creation of a new interface
        
        private MyGlassPane( Component previousGlassPane, Runnable callbackOnEvent )
        {
            this.previousGlassPane = previousGlassPane;
            this.timer             = new Timer( 60, this );
            this.callbackOnEvent   = callbackOnEvent;
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
        public void mousePressed( MouseEvent me )  { callbackOnEvent.run(); }
    }
    
    //------------------------------------------------------------------------//
    // NOTE: This is just to facilitate PDE development
    
    public static void main( String[] asArg )
    {
        org.joing.applauncher.Bootstrap.init();
        org.joing.applauncher.Bootstrap.loginDialog();
    }
}