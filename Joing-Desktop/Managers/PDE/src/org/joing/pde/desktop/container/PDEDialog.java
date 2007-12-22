/*
 * Copyright (C) Francisco Morero Peyrona. All rights reserved.
 *
 * This software is published under the terms of Open Source
 * License version 1.1, a copy of which has been included with this
 * distribution in the License.txt file.
 */

package org.joing.pde.desktop.container;

import java.awt.AWTEvent;
import java.awt.ActiveEvent;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.MenuComponent;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.SwingUtilities;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.MouseInputAdapter;
import org.joing.common.desktopAPI.DesktopManagerFactory;
import org.joing.common.desktopAPI.pane.DeskDialog;
import org.joing.pde.PDEManager;

/**
 * Instances of this class can't be added to the Desktop, because this class uses
 * a trick to be modal (they add themselves to the top-level-component 
 * RootPane-GlassPane).<br>
 * Remember: do not add instances of this class to anyplace.
 * 
 * @author Francisco Morero Peyrona
 */
public class PDEDialog extends PDEWindow implements DeskDialog
{
    private JPanel    glassNew;
    private Component glassOld;
    
    //------------------------------------------------------------------------//
    
    public PDEDialog()
    {           // resizable, closable, maximizable, minimizable
        super( "", true,      true,     false,       false );
        
        setDefaultCloseOperation( JInternalFrame.DISPOSE_ON_CLOSE );
        
        addInternalFrameListener( new InternalFrameAdapter()
        {
            public void internalFrameClosed( InternalFrameEvent e )
            {   // dispose() can't be here because goes into infinite loop
                clean();
            }
        } );
        
        // PDEManager root pane
        JRootPane root = ((PDEManager) DesktopManagerFactory.getDM()).getTheRootPane();
        
        // Sin esto, los eventos de ratón "atraviesan" el glass
        MouseInputAdapter mia = new MouseInputAdapter() { };
        
        // Create not-opaque glass pane
        glassNew = new JPanel();
        glassNew.setOpaque( false );
        glassNew.setSize( root.getSize() );////
        glassNew.addMouseListener( mia );
        glassNew.addMouseMotionListener( mia );
        
        // Add modal internal frame to glass pane
        glassNew.add( this );

        // Change glass pane to our panel in rootPane
        glassOld = root.getGlassPane();
        root.setGlassPane( glassNew );
                  
        // Show glass pane, then modal dialog
        glassNew.validate();
        glassNew.setVisible( true );
    }
    
    //------------------------------------------------------------------------//
    // DeskDialog Interface Implementation
    
    public boolean isModal()
    {
        return true;
    }
    
    //------------------------------------------------------------------------//
    // Closeable interface
    
    public void close()
    {
        clean();
        dispose(); // dispose() can't be at the InternalFrameAdapter because goes into infinite loop
    }
    
    //------------------------------------------------------------------------//
    
    public void setVisible( boolean value )
    {
        super.setVisible( value );
        
        if( value )
            startModal();
        else
            stopModal();
    }
    
    //------------------------------------------------------------------------//
    
    private void clean()
    {
        JRootPane root = ((PDEManager) DesktopManagerFactory.getDM()).getTheRootPane();
                  root.setGlassPane( glassOld );
                  
        stopModal();
    }
    
    private synchronized void startModal()
    {
        try
        {
            if( SwingUtilities.isEventDispatchThread() )
            {
                EventQueue theQueue = getToolkit().getSystemEventQueue();
                
                while( isVisible() )
                {
                    AWTEvent event  = theQueue.getNextEvent();
                    Object   source = event.getSource();
                    
                         if( event instanceof ActiveEvent )     ((ActiveEvent) event).dispatch();
                    else if( source instanceof Component )      ((Component) source).dispatchEvent( event );
                    else if( source instanceof MenuComponent )  ((MenuComponent) source).dispatchEvent( event );
                    else System.err.println( "Unable to dispatch: " + event );
                }
            }
            else
            {
                while( isVisible() )
                    wait();
            }
        }
        catch( InterruptedException ignored )
        {
            // Nothing to do
        }
    }

    private synchronized void stopModal()
    {
        notifyAll();
    }
}