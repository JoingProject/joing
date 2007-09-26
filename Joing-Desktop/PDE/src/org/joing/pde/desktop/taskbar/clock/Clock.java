/*
 * Clock.java
 *
 * Created on 15 de septiembre de 2007, 21:53
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.joing.pde.desktop.taskbar.clock;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import org.joing.pde.desktop.taskbar.TaskPanel;

/**
 *
 * @author Francisco Morero Peyrona
 */
public class Clock extends TaskPanel 
{
    private JLabel           lblClock;
    private SimpleDateFormat sdf;
    private boolean          bShowDate;
    private boolean          bShowSecs;
    private boolean          b24Format;
    private Timer            timer;
    
    //------------------------------------------------------------------------//
    
    public Clock()
    {
        lblClock = new JLabel();
        lblClock.setFont( getFont().deriveFont( Font.PLAIN, 11f ) );
        lblClock.setBorder( new EmptyBorder( 0, 3, 0, 3 ) );
        lblClock.setInheritsPopupMenu( true );
        
        changeFormat( false, true, true );
        createPopup();
        add( lblClock );
        
        timer = new Timer( 1000, new ActionListener()
        {
            public void actionPerformed( ActionEvent ae )
            {
                Clock.this.lblClock.setText( Clock.this.sdf.format( new Date() ) );
            }
        } );
        
        addAncestorListener( new AncestorListener()
        {
            public void ancestorAdded(AncestorEvent event)
            {
                Clock.this.timer.start();
            }
            public void ancestorMoved(AncestorEvent event)
            {
            }
            public void ancestorRemoved(AncestorEvent event)
            {
                Clock.this.timer.stop();
            }
        } );
    }
    
    //------------------------------------------------------------------------//
    
    private void changeFormat( boolean bShowDate, boolean bShowSecs, boolean b24Format )
    {
        this.bShowDate = bShowDate;
        this.bShowSecs = bShowSecs;
        this.b24Format = b24Format;
                                                  // TODO: la fecha tiene que estar en formato Local
        sdf = new SimpleDateFormat( (bShowDate ? "dd/MM/yyyy" : ""   ) +
                                    (bShowSecs ? "HH:mm:ss" : "HH:mm") +
                                    (b24Format ? "" : " a" ) );
        
        validate();
        setPreferredSize( new Dimension( 80,22 ) );// TODO: lblClock.getPreferredSize() );
        setMaximumSize( getPreferredSize() );
    }
    
    private void createPopup()
    {
        JPopupMenu        popup    = new JPopupMenu();
        JCheckBoxMenuItem itemDate = new JCheckBoxMenuItem( "Show date" );
        JCheckBoxMenuItem itemSecs = new JCheckBoxMenuItem( "Show seconds" );
        JCheckBoxMenuItem item24Hr = new JCheckBoxMenuItem( "24 hours" );
        
        itemDate.setState( this.bShowDate );
        itemDate.addActionListener( new ActionListener()
        {
            public void actionPerformed( ActionEvent ae )
            {
                boolean bShowDate = ((JCheckBoxMenuItem) ae.getSource()).getState();
                Clock.this.changeFormat( bShowDate, Clock.this.bShowSecs, Clock.this.b24Format );
            }
        } );

        itemSecs.setState( this.bShowSecs );
        itemSecs.addActionListener( new ActionListener()
        {
            public void actionPerformed( ActionEvent ae )
            {
                boolean bShowSecs = ((JCheckBoxMenuItem) ae.getSource()).getState();
                Clock.this.changeFormat( Clock.this.bShowDate, bShowSecs, Clock.this.b24Format );
            }
        } );
        
        item24Hr.setState( this.b24Format );
        item24Hr.addActionListener( new ActionListener()
        {
            public void actionPerformed( ActionEvent ae )
            {
                boolean b24Format = ((JCheckBoxMenuItem) ae.getSource()).getState();
                Clock.this.changeFormat( Clock.this.bShowDate, Clock.this.bShowSecs, b24Format );
            }
        } );
        
        popup.add( itemDate );
        popup.add( itemSecs );
        popup.add( item24Hr );
        
        setComponentPopupMenu( popup );
    }
}