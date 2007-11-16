/*
 * (c) Telco Domótica S.L.
 * Todos los derechos reservados.
 * 
 * Creado el 11-sep-2005 a las 13:20:45
 */

package com.binfactory;

import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;

import com.binfactory.client.runtime.Runtime;

public class ToolBar extends JToolBar
{
    private JButton rotateLeft  = createButton( new RotateLeft()  );
    private JButton rotateRight = createButton( new RotateRight() );
    
    private JButton zoomIn      = createButton( new ZoomIn()      );
    private JButton zoomOut     = createButton( new ZoomOut()     );
    private JButton zoom100     = createButton( new Zoom100()     );
    private JButton zoomStretch = createButton( new ZoomStretch() );
    
    private JButton print       = createButton( new Print()       );
    private JButton close       = createButton( new Close()       );
    
    private Images owner;
    
    //------------------------------------------------------------------------//
    
    public ToolBar( Images owner )
    {
        this.owner = owner;
        
        setRollover( true );
        
        add( createButton( new Open() ) );
        add( print );
        addSeparator();
        add( rotateLeft );
        add( rotateRight );
        addSeparator();
        add( zoomIn );
        add( zoomOut );
        add( zoom100 );
        add( zoomStretch );
        addSeparator();
        add( close );
        addSeparator();
        add( createButton( new About() ) );
    }
    
    public void updateButtons()
    {
        WImage image = owner.getSelectedImage();
        
        if( image != null )
        {
            print.getAction().setEnabled( true );
            rotateLeft.getAction().setEnabled( true );
            rotateRight.getAction().setEnabled( true );
            
            zoomIn.getAction().setEnabled( image.canIncreaseZoom() );
            zoomOut.getAction().setEnabled( image.canDecreaseZoom() );
            zoom100.getAction().setEnabled( ! image.getOriginalSize().equals( image.getCurrentSize() ) );
            zoomStretch.getAction().setEnabled( ! image.isStretched() );
            
            this.close.getAction().setEnabled( true );
        }
        else
        {
            print.getAction().setEnabled( false );
            rotateLeft.getAction().setEnabled( false );
            rotateRight.getAction().setEnabled( false );
            
            zoomIn.getAction().setEnabled( false );
            zoomOut.getAction().setEnabled( false );
            zoom100.getAction().setEnabled( false );
            zoomStretch.getAction().setEnabled( false );
            
            close.getAction().setEnabled( false );
        }
    }
    
    private JButton createButton( Action action )
    {
        JButton button = new JButton( action );
                button.setFocusPainted( false );
                button.setText( null );
                button.setOpaque( false );
                button.setBorderPainted( false );
                button.addMouseListener( new MouseAdapter() 
                    {
                        public void mouseEntered( MouseEvent me )
                        {
                            JButton btn = (JButton) me.getSource();
                                    btn.setBorderPainted( true );
                            owner.setHelp( btn );
                        }

                        public void mouseExited( MouseEvent me )
                        {
                            JButton btn = (JButton) me.getSource();
                                    btn.setBorderPainted( false );
                            owner.setHelp( null );
                        }
                    } );
        
        KeyStroke keyStroke = (KeyStroke) action.getValue( AbstractAction.ACCELERATOR_KEY );
        
        if( keyStroke != null )
        {
            button.getInputMap( JComponent.WHEN_IN_FOCUSED_WINDOW ).put( keyStroke, "action" );
            button.getActionMap().put( "action",
                new AbstractAction()
                {
                    public void actionPerformed( java.awt.event.ActionEvent ae )
                    {
                         ((JButton) ae.getSource()).doClick();
                    }
                }
            );
        }
                
        return button;
    }
    
    //------------------------------------------------------------------------//
    // INNER CLASSES FOR ACTIONS 
    
    private final class Open extends AbstractAction
    {
        private Open()
        {
            super( "Open" );
            putValue( AbstractAction.SMALL_ICON       , Runtime.getIcon( this, "images/open.png" ) );
            putValue( AbstractAction.SHORT_DESCRIPTION, "Open existing file" );
            putValue( AbstractAction.ACCELERATOR_KEY  , KeyStroke.getKeyStroke( new Integer( KeyEvent.VK_O ), InputEvent.CTRL_MASK ) );
        }

        public void actionPerformed( ActionEvent evt )
        {
            owner.Open();
        }
    }
        
    private final class Print extends AbstractAction
    {
        private Print()
        {
            super( "Print" );
            putValue( AbstractAction.SMALL_ICON       , Runtime.getIcon( this, "images/print.png" ) );
            putValue( AbstractAction.SHORT_DESCRIPTION, "Print text" );
            putValue( AbstractAction.ACCELERATOR_KEY  , KeyStroke.getKeyStroke( new Integer( KeyEvent.VK_P ), InputEvent.CTRL_MASK ) );
        }

        public void actionPerformed( ActionEvent evt )
        {
            owner.Print();
        }
    }
    
    private final class RotateLeft extends AbstractAction
    {
        private RotateLeft()
        {
            super( "RotateLeft" );
            putValue( AbstractAction.SMALL_ICON       , Runtime.getIcon( this, "images/rotate_ccw.png" ) );
            putValue( AbstractAction.SHORT_DESCRIPTION, "Rotate left 90º" );
        }

        public void actionPerformed( ActionEvent evt )
        {
            owner.RotateLeft();
        }
    }
    
    private final class RotateRight extends AbstractAction
    {
        private RotateRight()
        {
            super( "RotateRight" );
            putValue( AbstractAction.SMALL_ICON       , Runtime.getIcon( this, "images/rotate_cw.png" ) );
            putValue( AbstractAction.SHORT_DESCRIPTION, "Rotate right 90º" );
        }

        public void actionPerformed( ActionEvent evt )
        {
            owner.RotateRight();
        }
    }
    
    private final class ZoomIn extends AbstractAction
    {
        private ZoomIn()
        {
            super( "ZoomIn" );
            putValue( AbstractAction.SMALL_ICON       , Runtime.getIcon( this, "images/zoom_in.png" ) );
            putValue( AbstractAction.SHORT_DESCRIPTION, "Makes image bigger" );
        }

        public void actionPerformed( ActionEvent evt )
        {
            owner.ZoomIn();
        }
    }
    
    private final class ZoomOut extends AbstractAction
    {
        private ZoomOut()
        {
            super( "ZoomOut" );
            putValue( AbstractAction.SMALL_ICON       , Runtime.getIcon( this, "images/zoom_out.png" ) );
            putValue( AbstractAction.SHORT_DESCRIPTION, "Makes image smaller" );
        }

        public void actionPerformed( ActionEvent evt )
        {
            owner.ZoomOut();
        }
    }
    
    private final class Zoom100 extends AbstractAction
    {
        private Zoom100()
        {
            super( "Zoom100" );
            putValue( AbstractAction.SMALL_ICON       , Runtime.getIcon( this, "images/zoom_100.png" ) );
            putValue( AbstractAction.SHORT_DESCRIPTION, "Takes image to its original size" );
        }

        public void actionPerformed( ActionEvent evt )
        {
            owner.Zoom100();
        }
    }
    
    private final class ZoomStretch extends AbstractAction
    {
        private ZoomStretch()
        {
            super( "ZoomStretch" );
            putValue( AbstractAction.SMALL_ICON       , Runtime.getIcon( this, "images/zoom_stretch.png" ) );
            putValue( AbstractAction.SHORT_DESCRIPTION, "Makes image as big as the window" );
        }

        public void actionPerformed( ActionEvent evt )
        {
            owner.ZoomStretch();
        }
    }
    
    private final class Close extends AbstractAction
    {
        private Close()
        {
            super( "Close" );
            putValue( AbstractAction.SMALL_ICON       , Runtime.getIcon( this, "images/close_tab.png" ) );
            putValue( AbstractAction.SHORT_DESCRIPTION, "Close selected tab" );
        }

        public void actionPerformed( ActionEvent evt )
        {
            owner.Close();
        }
    }
    
    private final class About extends AbstractAction
    {
        private About()
        {
            super( "About" );
            putValue( AbstractAction.SMALL_ICON       , Runtime.getIcon( this, "images/about.png" ) );
            putValue( AbstractAction.SHORT_DESCRIPTION, "About this application" );
        }

        public void actionPerformed( ActionEvent evt )
        {
            owner.About();
        }
    }
}