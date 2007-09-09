/*
 * Copyright (C) Francisco Morero Peyrona. All rights reserved.
 *
 * This software is published under the terms of Open Source
 * License version 1.1, a copy of which has been included with this
 * distribution in the License.txt file.
 */

package org.joing.pde.desktop.workarea.containers;

import java.awt.AWTEvent;
import java.awt.ActiveEvent;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.MenuComponent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyVetoException;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.event.MouseInputAdapter;

/**
 * Instances of this class can't be added to the Desktop, because this class uses
 * a trick to be modal (the add themselves to the top-level-component 
 * RootPane-GlassPane).<br>
 * Remember: do not add instances of this class to anyplace.
 * 
 * @author Francisco Morero Peyrona
 */
public class JoingDialog extends JInternalFrame
{
    private JOptionPane pane;
    
    //------------------------------------------------------------------------//
    
    public JoingDialog( String sTitle )
    {
        super( sTitle );
    }
    
    public JoingDialog( String sTitle, JPanel panel, Object[] values, Object initialValue )
    {
        this( sTitle, new JOptionPane( panel, JOptionPane.PLAIN_MESSAGE, JOptionPane.NO_OPTION, null, values, initialValue ) );
    }
    
    public JoingDialog( String sTitle, JOptionPane pane )
    {
        super( sTitle );
        setPane( pane );
    }
    
    public void setPane( JOptionPane pane )
    {
        MouseInputAdapter mia = new MouseInputAdapter() { };   // Sin esto los eventos de ratón "atraviesan" el glass
        
        // create not opaque glass pane
        final JPanel glass = new JPanel();
                     glass.setLayout( null );
                     glass.setOpaque( false );
                     glass.addMouseListener( mia );
                     glass.addMouseMotionListener( mia );

        this.pane = pane;
        
        // Add option pane
        getContentPane().removeAll();  // Por si se llama más de una vez setPane(...)             
        getContentPane().add( pane, BorderLayout.CENTER );

        // Define close behavior
        pane.addPropertyChangeListener( new PropertyChangeListener()
            {
                public void propertyChange( PropertyChangeEvent event )
                {
                    if( isVisible() && 
                        (event.getPropertyName().equals( JOptionPane.VALUE_PROPERTY ) || 
                         event.getPropertyName().equals( JOptionPane.INPUT_VALUE_PROPERTY )) )
                    {
                        dispose();                    
                        glass.setVisible( false );
                    }
                }
            } );
        
        // Change frame border
        putClientProperty( "JInternalFrame.frameType", "optionDialog" );
        
        // Centers this in desktopPane and makes it selected
        Dimension size1 = rootPane.getPreferredSize();
        Dimension size2 = getPreferredSize();
        int nX = (size1.width  - size2.width)  / 2;
        int nY = (size1.height - size2.height) / 2;
            
        setBounds( Math.max( nX, 0 ), Math.max( nY, 0 ), size2.width, size2.height );
        
        try
        {
            setSelected( true );
        }
        catch( PropertyVetoException ignored )
        {
        }

        // Add modal internal frame to glass pane
        glass.add( this );

        // Change glass pane to our panel in rootPane
        // @TODO implementar esto --> Runtime.getRootPane().setGlassPane( glass );

        // Show glass pane, then modal dialog
        glass.validate();
        glass.setVisible( true );
    }

    public void setVisible( boolean value )
    {
        super.setVisible( value );
        
        if( value )
            startModal();
        else
            stopModal();
    }
    
    public Object getValue()
    {
        return this.pane.getValue();
    }
    
    //------------------------------------------------------------------------//
    
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
        }
    }

    private synchronized void stopModal()
    {
        notifyAll();
    }
}