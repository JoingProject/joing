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
import javax.swing.JInternalFrame;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;
import org.joing.common.desktopAPI.DeskComponent;
import org.joing.common.desktopAPI.desktop.DesktopListener;
import org.joing.common.desktopAPI.pane.DeskFrame;
import org.joing.common.desktopAPI.taskbar.TaskBar;
import org.joing.common.desktopAPI.workarea.Wallpaper;
import org.joing.common.desktopAPI.workarea.WorkArea;
import org.joing.common.desktopAPI.workarea.WorkAreaListener;
import org.joing.pde.desktop.container.PDEFrame;
import org.joing.pde.desktop.taskbar.PDETaskBarPanel;

/**
 * A component that shows all opened frames: Frame, JFrame, JDesktopFrame in all
 * work areas: there is only one FrameList for all WorkAreas.
 * 
 * @author Francisco Morero Peyrona
 */
public class FrameList extends PDETaskBarPanel
{
    private GridLayout grid;
    
    private TheDesktopListener      tdl;
    private TheWorkAreaListener     twal;
    private TheWindowListener       twl;
    private TheIntenalFrameListener tifl;
    
    //------------------------------------------------------------------------//
    
    public FrameList()
    {
        grid = new GridLayout( 1, 0, 2, 0 );
        // NEXT: hay que buscar un buen layout
        setLayout( grid );
        
        tdl  = new TheDesktopListener();
        twal = new TheWorkAreaListener();
        twl  = new TheWindowListener();
        tifl = new TheIntenalFrameListener();
        
        org.joing.jvmm.RuntimeFactory.getPlatform().getDesktopManager().getDesktop().addDesktopListener( tdl );
        
        setMinimumSize( new Dimension( 120,22 ) );
        setMaximumSize( new Dimension( Integer.MAX_VALUE, Integer.MAX_VALUE ) );
        setPreferredSize( new Dimension( 480,24 ) );
    }
    
    //------------------------------------------------------------------------//
    // TaskBarPanel interface
    
    public void onPreferences()
    {
        // TODO: Hacerlo
    }

    public void onAbout()
    {
        // TODO: Hacerlo
    }

    public void onRemove()
    {
        // TODO: Hacerlo
    }

    public void onMove()
    {
        // TODO: Hacerlo
    }
    
    //------------------------------------------------------------------------//
    // Closeable interface
    
    public void close()
    {
        // TODO: hacerlo
    }
    
    //------------------------------------------------------------------------//
    
    private void add( Frame frame )
    {
        frame.addWindowListener( twl );
        _add( new FrameButton( frame ) );
    }
    
    private void add( PDEFrame iframe )
    {
        iframe.addInternalFrameListener( tifl );        
        _add( new FrameButton( iframe ) );
    }
    
    private void _add( FrameButton fb )
    {
        fb.addActionListener( new ActionListener()
        {
            public void actionPerformed( ActionEvent ae )
            {
                onButtonClicked( ae );
            }
        } );
        
        grid.setColumns( grid.getColumns() + 1 );
        add( (DeskComponent) fb );
        grid.layoutContainer( this );
        updateSelected();
    }
    
    private void remove( Container frame )
    {
        Component[] aComp = getComponents();
        
        // Find the button associated with passed frame
        for( int n = 0; n < aComp.length; n++ )
        {
            if( aComp[n] instanceof FrameButton )
            {
                FrameButton fb = (FrameButton) aComp[n];
                
                if( frame == fb.getFrame() )
                {
                    remove( (DeskComponent) fb );       // Removes the button from the panel
                    grid.setColumns( grid.getColumns() - 1 );
                    grid.layoutContainer( this );
                    updateSelected();
                    break;
                }
            }
        }
        
        if( getComponentCount() == 0 )
            repaint();    // This is needed, otherwise last button is not erased
    }
    
    public void onButtonClicked( ActionEvent ae )
    {
        FrameButton btn = (FrameButton) ae.getSource();
        Container   frm = btn.getFrame();
        
        if( (WorkArea) frm.getParent() != org.joing.jvmm.RuntimeFactory.getPlatform().getDesktopManager().getDesktop().getActiveWorkArea() )
        {
            // If the frame was in a different workarea, then change active work area
            org.joing.jvmm.RuntimeFactory.getPlatform().getDesktopManager().getDesktop().setActiveWorkArea( (WorkArea) frm.getParent() );
            
            // And if it was minimized, then restore; but if it was restored don't minimize. Always make selected
            if( frm instanceof PDEFrame )
            {
                PDEFrame iframe = (PDEFrame) frm;

                if( iframe.isIcon() )  iframe.setStatus( DeskFrame.Status.RESTORED  );
                else                   iframe.setSelected( true );
            }
            else
            {
                Frame frame = (Frame) frm;

                if( frame.getExtendedState() == Frame.ICONIFIED )
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
        else
        {
            if( frm instanceof PDEFrame )
            {
                PDEFrame iframe = (PDEFrame) frm;

                if(      iframe.isSelected() )  iframe.setStatus( DeskFrame.Status.MINIMIZED );
                else if( iframe.isIcon()     )  iframe.setStatus( DeskFrame.Status.RESTORED  );
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
    }
    
    private void updateSelected()
    {
        WorkArea    waActive = org.joing.jvmm.RuntimeFactory.getPlatform().getDesktopManager().getDesktop().getActiveWorkArea();
        Component[] aComp    = getComponents();
        
        for( int n = 0; n < aComp.length; n++ )
        {
            if( aComp[n] instanceof FrameButton )
            {
                FrameButton frmbtn = (FrameButton) aComp[n];
                Container   frame  = frmbtn.getFrame();

                if( frame.getParent() == waActive )
                {
                    boolean b = (frame instanceof Frame) ? ((Frame) frame).isActive()
                                                         : ((JInternalFrame) frame).isSelected();
                    frmbtn.setSelected( b );
                }
                else
                {
                    frmbtn.setSelected( false );
                }
            }
        }
    }
    
    //------------------------------------------------------------------------//
    // INNER CLASS: Desktop Listener
    //------------------------------------------------------------------------//
    
    private class TheDesktopListener implements  DesktopListener
    {
        public void taskBarAdded( TaskBar tb )    { /* Nothing to do */ }
        public void taskBarRemoved( TaskBar tb )  { /* Nothing to do */ }
        
        public void workAreaAdded( WorkArea wa )
        {
            wa.addWorkAreaListener( FrameList.this.twal );
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
        public void componentAdded( DeskComponent comp )
        {
                 if( comp instanceof PDEFrame ) FrameList.this.add( (PDEFrame) comp );
            else if( comp instanceof Frame    ) FrameList.this.add( (Frame)    comp ); // Used for Frame and JFrame
        }

        public void componentRemoved( DeskComponent comp )
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