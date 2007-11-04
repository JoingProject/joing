/*
 * WindowsList.java
 *
 * Created on 15 de febrero de 2007, 11:11
 *
 * (c) 2006 - Francisco Morero Peyrona
 *
 * License: {license}
 */

package org.joing.pde.desktop.taskbar.frameslist;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.Hashtable;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;
import org.joing.api.desktop.DesktopListener;
import org.joing.api.desktop.taskbar.TaskBar;
import org.joing.api.desktop.workarea.Wallpaper;
import org.joing.api.desktop.workarea.WorkArea;
import org.joing.api.desktop.workarea.WorkAreaListener;
import org.joing.pde.desktop.container.PDEFrame;
import org.joing.pde.desktop.taskbar.TaskPanel;
import org.joing.pde.runtime.PDERuntime;

/**
 * A component that shows all opened frames: Frame, JFrame, JDesktopFrame
 * 
 * @author Francisco Morero Peyrona
 */
public class FramesList extends TaskPanel
{
    private Hashtable<Container, FrameButton> vButtons;   // For speed
    
    private TheDesktopListener      tdl;
    private TheWorkAreaListener     twal;
    private TheWindowListener       twl;
    private TheIntenalFrameListener tifl;
    
    private GridLayout grid;
    
    //------------------------------------------------------------------------//
    
    public FramesList()
    {
        vButtons = new Hashtable<Container, FrameButton>();
        grid     = new GridLayout( 1, 0, 2, 0 );
        
        tdl  = new TheDesktopListener();
        twal = new TheWorkAreaListener();
        twl  = new TheWindowListener();
        tifl = new TheIntenalFrameListener();
        
        setLayout( grid );
        setMinimumSize( new Dimension( 80,20 ) );
        setMaximumSize( new Dimension( Integer.MAX_VALUE, Integer.MAX_VALUE ) );
        setPreferredSize( new Dimension( 700,24 ) );   // FIXME: esto no puede estar a pelo
        
        PDERuntime.getRuntime().getDesktopManager().getDesktop().addDesktopListener( tdl );
    }
    
    //------------------------------------------------------------------------//
    
    public void add( PDEFrame iframe )
    {
        iframe.addInternalFrameListener( tifl );        
        _add( iframe, new FrameButton( iframe ) );
    }
    
    public void add( Frame frame )
    {
        frame.addWindowListener( twl );
        _add( frame, new FrameButton( frame ) );
    }
    
    //------------------------------------------------------------------------//
    
    private void _add( Container frame, FrameButton fb )
    {
        fb.addActionListener( new ActionListener()
        {
            public void actionPerformed( ActionEvent ae )
            {
                onButtonClicked( ae );
            }
        } );
                    
        vButtons.put( frame, fb );
        
        grid.setColumns( grid.getColumns() + 1 );
        super.add( fb );
        grid.layoutContainer( this );
    }
    
    private void remove( Container frm )
    {
        FrameButton fb = vButtons.get( frm );
        
        super.remove( fb );       // Removes the button from the panel
        vButtons.remove( frm );
        grid.setColumns( grid.getColumns() - 1 );
        updateSelected();
    }
    
    public void onButtonClicked( ActionEvent ae )
    {
        FrameButton btn = (FrameButton) ae.getSource();
        Container   frm = btn.getFrame();
        
        if( frm instanceof PDEFrame )
        {
            PDEFrame iframe = (PDEFrame) frm;
            
            if(      iframe.isSelected() )  iframe.setIcon( true );
            else if( iframe.isIcon()     )  iframe.restore();
            else                            iframe.setSelected( true );
        }
        else
        {
            Frame frame = (Frame) frm;
            
            if( frame.isActive() )
            {
                frame.setExtendedState( frame.getExtendedState() | Frame.ICONIFIED );
            }
            else if( frame.getExtendedState() == Frame.ICONIFIED )
            {
                frame.setExtendedState( frame.getExtendedState() | Frame.NORMAL );
            }
            else   // Make it the active (selected) frame
            {
                frame.setVisible( false );
                frame.setVisible( true );
            }
        }
    }
    
    //------------------------------------------------------------------------//
    
    protected JPanel getAboutPanel()
    {
        return null;  // NEXT: hacerlo
    }

    protected JPanel getPreferencesPanel()
    {
        return null;   // NEXT: hacerlo
    }

    protected void onPreferencesChanged(JPanel pnlPrefs)
    {
        // NEXT: nada que hacer hasta que no se fabrique el panel de preferencias
    }
    
    //------------------------------------------------------------------------//

    private void updateSelected()
    {
        WorkArea waActive = PDERuntime.getRuntime().getDesktopManager().getDesktop().getActiveWorkArea();
        
        removeAll();
        
        for( FrameButton btn : vButtons.values() )
        {
            Container frame = btn.getFrame();
            
            if( frame.getParent() == waActive )
            {
                boolean b = (frame instanceof Frame) ? ((Frame) frame).isActive()
                                                     : ((JInternalFrame) frame).isSelected();
                btn.setSelected( b );
                add( btn );                
            }
        }
        
        grid.setColumns( getComponentCount() );
        grid.layoutContainer( this );
        repaint();
    }
    
    //------------------------------------------------------------------------//
    // INNER CLASS: Desktop Listener
    //------------------------------------------------------------------------//
    
    private class TheDesktopListener implements  DesktopListener
    {
        public void taskBarAdded( TaskBar tb )
        {
        }
        public void taskBarRemoved( TaskBar tb )
        {
        }
        public void workAreaAdded( WorkArea wa )
        {
            wa.addWorkAreaListener( FramesList.this.twal );
        }
        public void workAreaRemoved( WorkArea wa )
        {
            // Not need to remove the listener -> G.C. takes care of it
        }
        public void workAreaSelected( WorkArea waPrevious, WorkArea waCurrent )
        {
            updateSelected();
        }
    }
    
    //------------------------------------------------------------------------//
    // INNER CLASS: WorkArea Listener
    //    Heritage:
    //        Container -> JComponent -> JInternalFrame
    //        Container -> Window     -> Frame          -> JFrame
    //------------------------------------------------------------------------//
    private class TheWorkAreaListener implements WorkAreaListener
    {
        public void componentAdded( Component comp )
        {
            if( comp instanceof PDEFrame )
                FramesList.this.add( (PDEFrame) comp );
                
            if( comp instanceof Frame )           // Used for Frame and JFrame
                FramesList.this.add( (Frame) comp );
        }

        public void componentRemoved( Component comp )
        {
            // Not needed to remove the listener -> G.C. takes care of it
        }

        public void wallpaperChanged( Wallpaper wpNew )
        {
        }
    }
    
    //------------------------------------------------------------------------//
    // INNER CLASS: Internal Window Listener
    //------------------------------------------------------------------------//
    
    private class TheIntenalFrameListener implements InternalFrameListener
    {
        public void internalFrameOpened( InternalFrameEvent ife )
        {
            updateSelected();
        }
        
        public void internalFrameClosing( InternalFrameEvent ife )
        {
        }
        
        public void internalFrameClosed( InternalFrameEvent ife )
        {
            remove( (Container) ife.getInternalFrame() );
        }
        
        public void internalFrameIconified( InternalFrameEvent ife )
        {
            updateSelected();
        }
        
        public void internalFrameDeiconified( InternalFrameEvent ife )
        {
            updateSelected();
        }

        // Invocado cuando se hace InternalFrame.setSelected( true )
        public void internalFrameActivated( InternalFrameEvent ife )
        {
            updateSelected();
        }
        
        // Invocado cuando se hace InternalFrame.setSelected( false ): se procesa en la TaskBar
        public void internalFrameDeactivated( InternalFrameEvent ife )
        {
            updateSelected();
        }
    }
    
    //------------------------------------------------------------------------//
    // INNER CLASS: Window Listener
    //------------------------------------------------------------------------//
    
    private class TheWindowListener implements WindowListener
    {
        public void windowOpened( WindowEvent we )
        {
            updateSelected();
        }
        
        public void windowClosing( WindowEvent we )
        {
        }
        
        public void windowClosed( WindowEvent we )
        {
            remove( (Container) we.getWindow() );
        }
        
        public void windowIconified( WindowEvent we )
        {
            updateSelected();
        }
        
        public void windowDeiconified( WindowEvent we )
        {
            updateSelected();
        }
        
        public void windowActivated( WindowEvent we )
        {
            updateSelected();
        }
        
        public void windowDeactivated( WindowEvent we )
        {
            updateSelected();
        }
    }
}