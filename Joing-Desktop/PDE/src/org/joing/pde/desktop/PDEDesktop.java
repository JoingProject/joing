/*
 * DesktopImpl.java
 *
 * Created on 9 de septiembre de 2007, 9:38
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.joing.pde.desktop;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.RootPaneContainer;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.joing.api.desktop.Desktop;
import org.joing.api.desktop.DesktopListener;
import org.joing.api.desktop.enums.TaskBarOrientation;
import org.joing.api.desktop.workarea.WorkArea;
import org.joing.pde.PDEClient;
import org.joing.pde.desktop.taskbar.clock.AnAnimation;
import org.joing.pde.desktop.workarea.PDEWorkArea;
import org.joing.api.desktop.taskbar.TaskBar;
import org.joing.impl.desktop.DefaultDesktop;
import org.joing.pde.desktop.taskbar.PDETaskBar;
import org.joing.pde.desktop.taskbar.start.StartButton;
import org.joing.pde.desktop.workarea.container.PDECanvas;
import org.joing.pde.desktop.workarea.container.PDEFrame;
import org.joing.pde.desktop.workarea.desklet.deskApplet.PDEDeskApplet;
import org.joing.pde.desktop.workarea.desklet.deskLauncher.PDEDeskLauncher;
import org.joing.pde.runtime.PDERuntime;

/**
 *
 * @author Francisco Morero Peyrona
 */
public class PDEDesktop extends JPanel implements Desktop
{
    private Vector<TaskBar>  vTaskBars;
    private Vector<WorkArea> vWorkAreas;
    
    private JPanel   pnlWorkAreas;   // Where all W.A. are stored using CardLayout
    private WorkArea waActive;
    
    //------------------------------------------------------------------------//
    
    public PDEDesktop()
    {
        vWorkAreas = new Vector<WorkArea>();
        vTaskBars  = new Vector<TaskBar>();
        waActive   = null;
        
        initGUI();
    }
    
    //------------------------------------------------------------------------//
    
    public void load()
    {
        String sPreferences = loadDesktopPreferencesFile();
        
        if( sPreferences == null )
        {
            PDETaskBar tb = new PDETaskBar();
                       tb.createDefaultComponents();
                       
            addTaskBar( tb );
            addWorkArea( new PDEWorkArea() );
            createTestWorkArea();
        }
        else
        {
            process( sPreferences );
        }
    }
    
    public void save()
    {
        // TODO: hacerlo
    }
    
    //------------------------------------------------------------------------//
 
    private String loadDesktopPreferencesFile()
    {
        return null; // TODO: hacerlo
    }
 
    private void process( String sPreferences )
    {
        // TODO: hacerlo
    }
    
    private void initGUI()
    {
        setLayout( new BorderLayout() );        
        pnlWorkAreas = new JPanel( new CardLayout() );
        add( pnlWorkAreas, BorderLayout.CENTER );
    }
    
    private WorkArea createTestWorkArea()  // TODO: quitar este metodo
    {
        WorkArea wa = getActiveWorkArea();
        
        PDEDeskLauncher pl1 = new PDEDeskLauncher( "Test" );
        PDEDeskLauncher pl2 = new PDEDeskLauncher( "Notes" );
                        pl2.setLocation( 0,100 );

        wa.add( pl1 );
        wa.add( pl2 );

        AnAnimation animation = new AnAnimation();
                    animation.start();
        PDEDeskApplet applet = new PDEDeskApplet();
                      applet.add( animation );
                      applet.setBounds( 100, 150, 190,170 );  
        wa.add( applet );

        JLabel lblCanvas = new JLabel( "<html><h2>Soy un canvas.</h2>Y esto es <u>texto <font color=\"#0066CC\">HTML</font></u>.</h3></html>" );
        PDECanvas canvas = new PDECanvas();
                    canvas.setOpaque( false );
                    canvas.add( lblCanvas, BorderLayout.CENTER );
                    canvas.setBounds( 330, 130, 240, 80 );
                    wa.add( canvas );

        JSlider slrTranslucency = new JSlider( JSlider.HORIZONTAL, 0, 100, 25 );
                slrTranslucency.setMajorTickSpacing( 10 );
                slrTranslucency.setPaintLabels( true );
                slrTranslucency.setPaintTicks( true );
                slrTranslucency.addChangeListener( new ChangeListener() 
                {
                    public void stateChanged( ChangeEvent ce )
                    {
                        JSlider slr = (JSlider) ce.getSource();

                        PDEFrame frm = (PDEFrame) SwingUtilities.getAncestorOfClass( PDEFrame.class,slr );
                        frm.setTranslucency( slr.getValue() );
                    }
                } );
                    
        PDEFrame frm = new PDEFrame( "Example Join'g Frame" );
                   frm.getContentPane().add( new JLabel( "Translucency" ), BorderLayout.NORTH );
                   frm.getContentPane().add( slrTranslucency, BorderLayout.SOUTH );
                   frm.setBounds( 150, 50, 300, 200 );
                   frm.setTranslucency( 20 );
                   frm.setVisible( true );
                   wa.add( frm );
                   frm.setSelected( true );
                   
        return wa;
    }
    
    //------------------------------------------------------------------------//
    // Desktop interface implementation
    //------------------------------------------------------------------------//
    
    public void close()
    {
        for( WorkArea wa : vWorkAreas )
            wa.close();
    }
    
    //------------------------------------------------------------------------//
    // WORK AREAS
    
    public void addWorkArea( WorkArea wa )
    {
        vWorkAreas.add( wa );
        pnlWorkAreas.add( (Component) wa, 
                          (wa.getName() == null ? "WorkArea"+ vWorkAreas.size() : wa.getName()) );
        setActiveWorkArea( wa );
        fireWorkAreaAdded( wa );
    }
    
    public void removeWorkArea( WorkArea wa )
    {
        // Can't remove last WorkArea (a desktop must have at least one workarea)
        if( vWorkAreas.size() > 0 && vWorkAreas.contains( wa ) )
        {
            remove( (Component) wa );
            vWorkAreas.remove( wa );
            fireWorkAreaRemoved( wa );
        }
    }
    
    public List<WorkArea> getWorkAreas()
    {
        // Defensive copy
        ArrayList<WorkArea> list = new ArrayList<WorkArea>( vWorkAreas.size() );
                            list.addAll( vWorkAreas );
        return list;
    }
    
    public WorkArea getActiveWorkArea()
    {
        return waActive;
    }
    
    public void setActiveWorkArea( WorkArea wa )
    {
        if( vWorkAreas.contains( wa ) )
        {
            waActive = wa;
            CardLayout cl = (CardLayout) pnlWorkAreas.getLayout();
            cl.show( pnlWorkAreas, wa.getName() );
        }
    }
    
    //------------------------------------------------------------------------//
    // TASK BARS
    
    public List<TaskBar> getTaskBars()
    {
        // Defensive copy
        ArrayList<TaskBar> list = new ArrayList<TaskBar>( vTaskBars.size() );
                           list.addAll( vTaskBars );
        return list;
    }

    public void addTaskBar( TaskBar taskbar )
    {
        TaskBarOrientation orientation = taskbar.getOrientation();
        
        vTaskBars.add( taskbar );
        
        // TODO: ¿Qué pasaría si ya hubiese otra taskbar en esa posición?
        if( orientation == TaskBarOrientation.BOTTOM )
        {
            add( (Component) taskbar, BorderLayout.SOUTH );
        }
        else if( orientation == TaskBarOrientation.TOP )
        {
            add( (Component) taskbar, BorderLayout.NORTH );
        }
        else if( orientation == TaskBarOrientation.LEFT )
        {
            add( (Component) taskbar, BorderLayout.WEST );
        }
        else if( orientation == TaskBarOrientation.RIGHT )
        {
            add( (Component) taskbar, BorderLayout.EAST );
        }
        
        fireTaskBarAdded( taskbar );
    }
    
    public void removeTaskBar( TaskBar taskbar )
    {
        // A desktop can contain zero taskbars
        if( vTaskBars.contains( taskbar) )
        {
            remove( (Component) taskbar );
            vTaskBars.remove( taskbar );
            fireTaskBarRemoved( taskbar );
        }
    }

    //------------------------------------------------------------------------//
    // EVENTS
    
    public void addDesktopListener( DesktopListener dl )
    {
        listenerList.add( DesktopListener.class, dl );
    }

    public void removeDesktopListener( DesktopListener dl ) 
    {
        listenerList.add( DesktopListener.class, dl );
    }
    
    protected void fireWorkAreaAdded( WorkArea wa )
    {
        Object[] listeners = listenerList.getListenerList();
        
        // Process the listeners last to first, notifying
        for( int n = listeners.length - 2; n >= 0; n -= 2 )
        {
            if( listeners[n] == DesktopListener.class )
                ((DesktopListener) listeners[n+1]).workAreaAdded( wa );
        }
    }
    
    protected void fireWorkAreaRemoved( WorkArea wa )
    {
        Object[] listeners = listenerList.getListenerList();
        
        // Process the listeners last to first, notifying
        for( int n = listeners.length - 2; n >= 0; n -= 2 )
        {
            if( listeners[n] == DesktopListener.class )
                ((DesktopListener) listeners[n+1]).workAreaRemoved( wa );
        }
    }
    
    protected void fireTaskBarAdded( TaskBar tb )
    {
        Object[] listeners = listenerList.getListenerList();
        
        // Process the listeners last to first, notifying
        for( int n = listeners.length - 2; n >= 0; n -= 2 )
        {
            if( listeners[n] == DesktopListener.class )
                ((DesktopListener) listeners[n+1]).taskBarAdded( tb );
        }
    }
    
    protected void fireTaskBarRemoved( TaskBar tb )
    {
        Object[] listeners = listenerList.getListenerList();
        
        // Process the listeners last to first, notifying
        for( int n = listeners.length - 2; n >= 0; n -= 2 )
        {
            if( listeners[n] == DesktopListener.class )
                ((DesktopListener) listeners[n+1]).taskBarRemoved( tb );
        }
    }
}