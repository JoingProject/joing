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
import java.awt.Component;
import java.util.List;
import java.util.Vector;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.joing.common.desktopAPI.desktop.Desktop;
import org.joing.common.desktopAPI.desktop.DesktopListener;
import org.joing.common.desktopAPI.workarea.WorkArea;
import org.joing.pde.desktop.workarea.PDEWorkArea;
import org.joing.common.desktopAPI.taskbar.TaskBar;
import org.joing.pde.desktop.container.PDECanvas;
import org.joing.pde.desktop.deskwidget.deskLauncher.PDEDeskLauncher;
import org.joing.pde.desktop.taskbar.PDETaskBar;
import org.joing.pde.misce.desklets.NasaPhoto;
import org.joing.pde.misce.apps.memon.MemoryMonitor;
import org.joing.pde.swing.EventListenerList;

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
    
    private EventListenerList listenerList;
    
    //------------------------------------------------------------------------//
    
    public PDEDesktop()
    {
        vWorkAreas   = new Vector<WorkArea>();
        vTaskBars    = new Vector<TaskBar>();
        waActive     = null;
        listenerList = new EventListenerList();
        
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
                       /// TODO: probar esto --> tb.setOrientation( TaskBarOrientation.TOP );
                       
            addTaskBar( tb );
            
            addWorkArea( new PDEWorkArea() );
            addWorkArea( new PDEWorkArea() );
            setActiveWorkArea( getWorkAreas().get( 0 ) );
            
            createTestWorkArea( getWorkAreas().get( 0 ) );   // TODO: quitarlo
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
    
    private void createTestWorkArea( WorkArea wa )  // TODO: quitar este metodo
    {
        PDEDeskLauncher dl1 = new PDEDeskLauncher();
        PDEDeskLauncher dl2 = new PDEDeskLauncher();
                        dl2.setText( "Test" );
                        dl2.setLocation( 0,100 );

        wa.add( dl1 );
        wa.add( dl2 );
        
        MemoryMonitor.showFrame();
        
        NasaPhoto nasa = new NasaPhoto();
                  nasa.setBounds( 10,350, nasa.getPreferredSize().width, nasa.getPreferredSize().height );
        wa.add( nasa );
        
        JLabel lblCanvas = new JLabel( "<html><h2>Soy un canvas.</h2>Y esto es <u>texto <font color=\"#0066CC\">HTML</font></u>.</h3></html>" );
        PDECanvas canvas = new PDECanvas();
                  canvas.add( lblCanvas );
                  canvas.setBounds( 330, 130, 240, 80 );
        wa.add( canvas );

        //---------------------------------------------------------------------
        /*
        JSlider slrTranslucency = new JSlider( JSlider.HORIZONTAL, 0, 100, 0 );
                slrTranslucency.setMajorTickSpacing( 10 );
                slrTranslucency.setPaintLabels( true );
                slrTranslucency.setPaintTicks( true );
                slrTranslucency.addChangeListener( new ChangeListener() 
                {
                    public void stateChanged( ChangeEvent ce )
                    {
                        JSlider slr = (JSlider) ce.getSource();
                        
                        PDEFrame frm = (PDEFrame) SwingUtilities.getAncestorOfClass( PDEFrame.class, slr );
                                 frm.setTranslucency( slr.getValue() );
                    }
                } );
                    
        PDEFrame frm = new PDEFrame( "Example Join'g Frame" );
                 frm.add( new JLabel( "Translucency" ), BorderLayout.NORTH );
                 frm.add( slrTranslucency, BorderLayout.SOUTH );
                 frm.setBounds( 150, 50, 300, 200 );
                 frm.setAutoArrange( false );
                 frm.setVisible( true );
                 wa.add( frm );
                 frm.setSelected( true );
         */
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
        if( ! vWorkAreas.contains( wa ) )
        {
            if( wa.getName() == null )
                wa.setName( "WorkArea "+ vWorkAreas.size() );
            
            vWorkAreas.add( wa );
            pnlWorkAreas.add( (Component) wa, wa.getName() );
            fireWorkAreaAdded( wa );     // First add it
            setActiveWorkArea( wa );     // Now select it
        }
    }
    
    public void removeWorkArea( WorkArea wa )
    {
        // Can't remove last WorkArea (a desktop must have at least one workarea)
        if( vWorkAreas.size() > 0 && vWorkAreas.contains( wa ) )
        {
            remove( (Component) wa );
            vWorkAreas.remove( wa );
            setActiveWorkArea( vWorkAreas.get( 0 ) );  // 1st Selected event is launched
            fireWorkAreaRemoved( wa );                 // Now Removed event is launched
        }
    }
    
    public List<WorkArea> getWorkAreas()
    {
        return vWorkAreas;
    }
    
    public WorkArea getActiveWorkArea()
    {
        return waActive;
    }
    
    public void setActiveWorkArea( WorkArea wa )
    {
        if( vWorkAreas.contains( wa ) && (waActive != wa) )
        {
            WorkArea waOld = waActive;
            
            waActive = wa;
            CardLayout cl = (CardLayout) pnlWorkAreas.getLayout();
                       cl.show( pnlWorkAreas, wa.getName() );
            fireWorkAreaSelected( waOld, wa );
        }
    }
    
    //------------------------------------------------------------------------//
    // TASK BARS
    
    public List<TaskBar> getTaskBars()
    {
        return vTaskBars;
    }

    public void addTaskBar( TaskBar taskbar )
    {
        TaskBar.Orientation orientation = taskbar.getOrientation();
        
        vTaskBars.add( taskbar );
        
        // TODO: ¿Qué pasaría si ya hubiese otra taskbar en esa posición?
        //       Solución: ignorar (¿lanzar exc?) cuando se quiere añadir una TB donde ya hay una
        if( orientation == TaskBar.Orientation.BOTTOM )
        {
            add( (Component) taskbar, BorderLayout.SOUTH );
        }
        else if( orientation == TaskBar.Orientation.TOP )
        {
            add( (Component) taskbar, BorderLayout.NORTH );
        }
        else if( orientation == TaskBar.Orientation.LEFT )
        {
            add( (Component) taskbar, BorderLayout.WEST );
        }
        else if( orientation == TaskBar.Orientation.RIGHT )
        {
            add( (Component) taskbar, BorderLayout.EAST );
        }
        
        fireTaskBarAdded( taskbar );
    }
    
    public void removeTaskBar( TaskBar taskbar )
    {
        // A desktop _can_ contain zero taskbars
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
        listenerList.remove( dl );
    }
    
    protected void fireWorkAreaAdded( WorkArea wa )
    {
        DesktopListener[] al = listenerList.getListeners( DesktopListener.class );
        
        // Process the al last to first, notifying
        for( int n = al.length -1; n >= 0; n-- )
             al[n].workAreaAdded( wa );
    }
    
    protected void fireWorkAreaRemoved( WorkArea wa )
    {
        DesktopListener[] al = listenerList.getListeners( DesktopListener.class );
        
        // Process the al last to first, notifying
        for( int n = al.length -1; n >= 0; n-- )
             al[n].workAreaRemoved( wa );
    }
    
    protected void fireWorkAreaSelected( WorkArea waPrevious, WorkArea waCurrent )
    {
        DesktopListener[] al = listenerList.getListeners( DesktopListener.class );
        
        // Process the al last to first, notifying
        for( int n = al.length -1; n >= 0; n-- )
             al[n].workAreaSelected( waPrevious, waCurrent );
    }
    
    protected void fireTaskBarAdded( TaskBar tb )
    {
        DesktopListener[] al = listenerList.getListeners( DesktopListener.class );
        
        // Process the al last to first, notifying
        for( int n = al.length -1; n >= 0; n-- )
             al[n].taskBarAdded( tb );
    }
    
    protected void fireTaskBarRemoved( TaskBar tb )
    {
        DesktopListener[] al = listenerList.getListeners( DesktopListener.class );
        
        // Process the al last to first, notifying
        for( int n = al.length -1; n >= 0; n-- )
             al[n].taskBarRemoved( tb );
    }
}