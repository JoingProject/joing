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
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import org.joing.common.desktopAPI.DeskComponent;
import org.joing.common.desktopAPI.pane.DeskFrame;
import org.joing.pde.desktop.container.PDEFrame;
import org.joing.pde.desktop.taskbar.PDETaskBarComponent;
import org.joing.pde.swing.PDEAboutPanel;

/**
 *
 * @author Francisco Morero Peyrona
 */
public class ClockDigital extends PDETaskBarComponent
{
    private final static float nFONT_SIZE = 10f;
    
    private SimpleDateFormat sdfTime;
    private DateFormat       dfDate;
    private JLabel           lblTime;
    private JLabel           lblDate;
    private CompoundBorder   borderOn;
    private EmptyBorder      borderOff;
    private boolean          bShowDate;
    private boolean          bShowSecs;
    private boolean          b24Format;
    private Timer            timer;
    
    //------------------------------------------------------------------------//
    
    public ClockDigital()
    {
        lblTime = new JLabel();
        lblTime.setHorizontalAlignment( JLabel.CENTER );
        lblTime.setFont( getFont().deriveFont( Font.PLAIN, nFONT_SIZE ) );
        
        lblDate = new JLabel();
        lblDate.setHorizontalAlignment( JLabel.CENTER );
        lblDate.setFont( getFont().deriveFont( Font.PLAIN, nFONT_SIZE ) );
        
        borderOn  = new CompoundBorder( new LineBorder( getForeground() ), new EmptyBorder( 1,2,1,2 ) );
        borderOff = new EmptyBorder( 2,3,2,3 );  // To make borderOn shown softer
        
        setLayout( new BorderLayout() );
        setBorder( borderOff );
        add( lblTime, BorderLayout.CENTER );
        add( lblDate, BorderLayout.SOUTH  );
        
        setInheritsPopupMenu( true );
        
        changeFormat( true, true, true );
        
        timer = new Timer( 1000, new ActionListener()
        {
            public void actionPerformed( ActionEvent ae )
            {
                ClockDigital.this.update();
            }
        } );
        
        addAncestorListener( new AncestorListener()
        {
            public void ancestorAdded(   AncestorEvent ae ) { ClockDigital.this.timer.start(); }
            public void ancestorMoved(   AncestorEvent ae ) {}
            public void ancestorRemoved( AncestorEvent ae ) { ClockDigital.this.timer.stop(); }
        } );
        
        addMouseListener( new MouseAdapter()
        {
            public void mouseEntered( MouseEvent me )
            {
                ClockDigital.this.setBorder( ClockDigital.this.borderOn );
            }
            
            public void mouseExited( MouseEvent me )
            {
                ClockDigital.this.setBorder( ClockDigital.this.borderOff );
            }
        } );
    }
    
    //------------------------------------------------------------------------//
    // PDETaskBarPanel interface
    
    public void onAbout()
    {
        PDEAboutPanel panel = new PDEAboutPanel();
                      panel.setProductName( "Digital Clock" );
                      panel.setVersion( "1.0" );
                      panel.setDescription( "A very simple and configurable digital clock with date.\nThis is the default PDE clock.");
                      
        // Better to use a Frame than a Dialog (modaless: this is the way Gnome does it)
        DeskFrame frame = org.joing.jvmm.RuntimeFactory.getPlatform().getDesktopManager().getRuntime().createFrame();
                  frame.setTitle( "About" );
                  frame.add( (DeskComponent) panel );
                  
        org.joing.jvmm.RuntimeFactory.getPlatform().getDesktopManager().getDesktop().getActiveWorkArea().add( frame );
    }

    public void onPreferences()
    {
        JCheckBox chkDate = new JCheckBox( "Show date"           );
        JCheckBox chkSecs = new JCheckBox( "Show seconds"        );
        JCheckBox chk24Hr = new JCheckBox( "Use 24 hours format" );
        
        chkDate.setSelected( bShowDate );
        chkDate.addActionListener( new ActionListener()
        {
            public void actionPerformed( ActionEvent ae )
            {
                boolean bShowDate = ((JCheckBox) ae.getSource()).isSelected();
                ClockDigital.this.changeFormat( bShowDate, ClockDigital.this.bShowSecs, ClockDigital.this.b24Format );
            }
        } );
        
        chkSecs.setSelected( this.bShowSecs );
        chkSecs.addActionListener( new ActionListener()
        {
            public void actionPerformed( ActionEvent ae )
            {
                boolean bShowSecs = ((JCheckBox) ae.getSource()).isSelected();
                ClockDigital.this.changeFormat( ClockDigital.this.bShowDate, bShowSecs, ClockDigital.this.b24Format );
            }
        } );
        
        chk24Hr.setSelected( this.b24Format );
        chk24Hr.addActionListener( new ActionListener()
        {
            public void actionPerformed( ActionEvent ae )
            {
                boolean b24Format = ((JCheckBox) ae.getSource()).isSelected();
                ClockDigital.this.changeFormat( ClockDigital.this.bShowDate, ClockDigital.this.bShowSecs, b24Format );
            }
        } );
        
        JPanel panel = new JPanel( new GridLayout( 3,1 ) );
               panel.setBorder( new EmptyBorder( 9,9,9,9 ) );
               panel.add( chkDate );
               panel.add( chkSecs );
               panel.add( chk24Hr );
               
        PDEFrame frame = new PDEFrame();
                 frame.setTitle( "Clock Preferences" );
                 frame.add( panel );
        
        org.joing.jvmm.RuntimeFactory.getPlatform().getDesktopManager().getDesktop().getActiveWorkArea().add( frame );
    }
    
    public void onRemove()
    {
        org.joing.jvmm.RuntimeFactory.getPlatform().getDesktopManager().getRuntime().showMessageDialog( null, "Option not yet implemented" );
        // TODO: Hacerlo
    }
    
    public void onMove()
    {
        org.joing.jvmm.RuntimeFactory.getPlatform().getDesktopManager().getRuntime().showMessageDialog( null, "Option not yet implemented" );
        // TODO: Hacerlo: muy posiblmenete esto no será un método sino algo más complicado. 
    }
    
    //------------------------------------------------------------------------//
    // Closeable interface
    
    public void close()
    {
        // TODO: hacerlo
    }
    
    //------------------------------------------------------------------------//
    
    protected void update()
    {
        long nSecsFromMidnight = (System.currentTimeMillis() / 1000) % (24 * 60 * 60);
        Date date = new Date();
                
        lblTime.setText( sdfTime.format( date ) );
                
        if( (nSecsFromMidnight <= 1) || (lblDate.getText().isEmpty()) )
            lblDate.setText( dfDate.format( date ) );
    }
    
    private void changeFormat( boolean bShowDate, boolean bShowSecs, boolean b24Format )
    {
        this.bShowDate = bShowDate;
        this.bShowSecs = bShowSecs;
        this.b24Format = b24Format;
        
        sdfTime = new SimpleDateFormat( (bShowSecs ? "HH:mm:ss" : "HH:mm") + (b24Format ? "" : " a" ) );
        dfDate = DateFormat.getDateInstance( DateFormat.MEDIUM );
        
        float nFontSize = (float) (bShowDate ? nFONT_SIZE : nFONT_SIZE * 1.2);
        
        lblTime.setFont( getFont().deriveFont( Font.PLAIN, nFontSize ) );
        lblDate.setVisible( bShowDate );
        
        setMaximumSize( getPreferredSize() );
        validate();
    }
}