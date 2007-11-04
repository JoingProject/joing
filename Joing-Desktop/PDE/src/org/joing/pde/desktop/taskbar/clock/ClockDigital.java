/*
 * ClockDigital.java
 *
 * Created on 15 de septiembre de 2007, 21:53
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.joing.pde.desktop.taskbar.clock;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.Timer;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import org.joing.pde.desktop.taskbar.TaskPanel;

/**
 *
 * @author Francisco Morero Peyrona
 */
public class ClockDigital extends TaskPanel
{
    private final static float nFONT_SIZE = 10f;
    
    private SimpleDateFormat sdfTime;
    private SimpleDateFormat sdfDate;
    private JLabel  lblTime;
    private JLabel  lblDate;
    private boolean bShowDate;
    private boolean bShowSecs;
    private boolean b24Format;
    private Timer   timer;
    
    //------------------------------------------------------------------------//
    
    public ClockDigital()
    {
        lblTime = new JLabel();
        lblTime.setHorizontalAlignment( JLabel.CENTER );
        lblTime.setFont( getFont().deriveFont( Font.PLAIN, nFONT_SIZE ) );
        
        lblDate = new JLabel();
        lblDate.setHorizontalAlignment( JLabel.CENTER );
        lblDate.setFont( getFont().deriveFont( Font.PLAIN, nFONT_SIZE ) );
        
        setLayout( new BorderLayout() );
        add( lblTime, BorderLayout.CENTER );
        add( lblDate, BorderLayout.SOUTH  );
      
        setOpaque( false );
        setInheritsPopupMenu( true );
        
        changeFormat( true, true, true );
        createPopup();
        
        timer = new Timer( 1000, new ActionListener()
        {
            public void actionPerformed( ActionEvent ae )
            {
                Date date = new Date();
                
                ClockDigital.this.lblTime.setText( ClockDigital.this.sdfTime.format( date ) );
                
                if( ClockDigital.this.bShowDate )
                {
                    String sDate = ClockDigital.this.sdfDate.format( date );
                    
                    if( ! ClockDigital.this.lblDate.getText().equals( sDate ) )
                        ClockDigital.this.lblDate.setText( sDate );
                }
            }
        } );
        
        addAncestorListener( new AncestorListener()
        {
            public void ancestorAdded(   AncestorEvent ae ) { ClockDigital.this.timer.start(); }
            public void ancestorMoved(   AncestorEvent ae ) {}
            public void ancestorRemoved( AncestorEvent ae ) { ClockDigital.this.timer.stop(); }
        } );
    }
    
    //------------------------------------------------------------------------//
    
    protected JPanel getAboutPanel()
    {
        return null;   // TODO: hacerlo
    }

    protected JPanel getPreferencesPanel()
    {
        return null;
    }

    protected void onPreferencesChanged( JPanel pnlPrefs )
    {
        // As this component has no preferences panel, tehre is nothing to do.
    }
    
    //------------------------------------------------------------------------//
    
    private void changeFormat( boolean bShowDate, boolean bShowSecs, boolean b24Format )
    {
        this.bShowDate = bShowDate;
        this.bShowSecs = bShowSecs;
        this.b24Format = b24Format;
        
        sdfTime = new SimpleDateFormat( (bShowSecs ? "HH:mm:ss" : "HH:mm") + (b24Format ? "" : " a" ) );
        sdfDate = new SimpleDateFormat( "dd/MM/yyyy" ); // TODO: la fecha tiene que estar en formato Local

        float nFontSize = (float) (bShowDate ? nFONT_SIZE : nFONT_SIZE * 1.2);
        
        lblTime.setFont( getFont().deriveFont( Font.PLAIN, nFontSize ) );
        lblDate.setVisible( bShowDate );
        
        setMaximumSize( getPreferredSize() );
        validate();
    }
    
    private void createPopup()
    {
        JCheckBoxMenuItem itemDate = new JCheckBoxMenuItem( "Show date" );
        JCheckBoxMenuItem itemSecs = new JCheckBoxMenuItem( "Show seconds" );
        JCheckBoxMenuItem item24Hr = new JCheckBoxMenuItem( "24 hours" );
        
        itemDate.setState( this.bShowDate );
        itemDate.addActionListener( new ActionListener()
        {
            public void actionPerformed( ActionEvent ae )
            {
                boolean bShowDate = ((JCheckBoxMenuItem) ae.getSource()).getState();
                ClockDigital.this.changeFormat( bShowDate, ClockDigital.this.bShowSecs, ClockDigital.this.b24Format );
            }
        } );

        itemSecs.setState( this.bShowSecs );
        itemSecs.addActionListener( new ActionListener()
        {
            public void actionPerformed( ActionEvent ae )
            {
                boolean bShowSecs = ((JCheckBoxMenuItem) ae.getSource()).getState();
                ClockDigital.this.changeFormat( ClockDigital.this.bShowDate, bShowSecs, ClockDigital.this.b24Format );
            }
        } );
        
        item24Hr.setState( this.b24Format );
        item24Hr.addActionListener( new ActionListener()
        {
            public void actionPerformed( ActionEvent ae )
            {
                boolean b24Format = ((JCheckBoxMenuItem) ae.getSource()).getState();
                ClockDigital.this.changeFormat( ClockDigital.this.bShowDate, ClockDigital.this.bShowSecs, b24Format );
            }
        } );
        
        // Working with inherited PopupMenu
        JPopupMenu popup = getComponentPopupMenu();
                   popup.remove( itemPreferences );
                   popup.add( itemDate, 0 );
                   popup.add( itemSecs, 1 );
                   popup.add( item24Hr, 2 );
    }
}