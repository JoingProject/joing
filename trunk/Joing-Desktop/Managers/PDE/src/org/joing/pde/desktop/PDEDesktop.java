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
import java.awt.Image;
import java.io.BufferedReader;
import java.io.File;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JPanel;
import javax.swing.SwingWorker;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.joing.common.desktopAPI.desktop.Desktop;
import org.joing.common.desktopAPI.desktop.DesktopListener;
import org.joing.common.desktopAPI.workarea.WorkArea;
import org.joing.pde.desktop.workarea.PDEWorkArea;
import org.joing.common.desktopAPI.taskbar.TaskBar;
import org.joing.pde.desktop.container.PDECanvas;
import org.joing.pde.desktop.taskbar.PDETaskBar;
import org.joing.pde.swing.EventListenerList;
import org.xml.sax.SAXException;

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
    
    private EventListenerList listenerList;       // FIXME: Cambiar esto por la de JComponent
    
    private Hashtable<Integer, PDECanvas> htInfoPanels;
    
    //------------------------------------------------------------------------//
    
    public PDEDesktop()
    {
        vWorkAreas   = new Vector<WorkArea>();
        vTaskBars    = new Vector<TaskBar>();
        waActive     = null;
        listenerList = new EventListenerList();
        htInfoPanels = new Hashtable<Integer, PDECanvas>();
        
        // Init GUI
        setLayout( new BorderLayout() );
        pnlWorkAreas = new JPanel( new CardLayout() );
        add( pnlWorkAreas, BorderLayout.CENTER );
    }
    
    //------------------------------------------------------------------------//
    
    public void restore()
    {
        SwingWorker sw = new SwingWorker()
        {
            protected Object doInBackground() throws Exception
            {
                // Do not move next lines !
                File fSavedStatus = null;
        
                if( fSavedStatus == null )
                    createDefaultDesktop();
//                else
//                    processSavedStatus( ftSavedStatus.getContent() );
    
                setActiveWorkArea( getWorkAreas().get( 0 ) );
                
                return null;
            }
        };
        sw.execute();
    }
    
    //------------------------------------------------------------------------//
 
    private void createDefaultDesktop()
    {
        PDETaskBar tb = new PDETaskBar();
                   tb.createDefaultComponents();
                   // TODO: probar esto --> tb.setOrientation( TaskBarOrientation.TOP );
        addTaskBar( tb );
        
        for( int n = 0; n < 3; n++ )
            addWorkArea( new PDEWorkArea() );
        
        Testing.createTestComponents();
    }
    
    private void processSavedStatus( BufferedReader in )
    {
        try
        {
            SAXParserFactory factory = SAXParserFactory.newInstance();

            SAXParser saxParser = factory.newSAXParser();
//                      saxParser.parse( in, new XMLPreferencesReader() );
        }
        catch( ParserConfigurationException ex )
        {
            Logger.getLogger( PDEDesktop.class.getName() ).log( Level.SEVERE, null, ex );
        }
        catch( SAXException ex )
        {
            Logger.getLogger( PDEDesktop.class.getName() ).log( Level.SEVERE, null, ex );
        }
        finally
        {
            // At least one WA must exists
            if( getWorkAreas().size() == 0 )
                addWorkArea( new PDEWorkArea() );
        }
    }
    
    //------------------------------------------------------------------------//
    // Desktop interface implementation
    //------------------------------------------------------------------------//
    // CLOSEABLE INTERFACE
    
    /**
     * Saves current status (to be restored next time) and detaches (close) all 
     * workareas and toolbars.
     */
    public void close()
    { // Info at: http://java.sun.com/products/jfc/tsc/articles/persistence4/index.html
//        FileOutputStream os = null;
//        try
//        {
//            os = new FileOutputStream( "/home/fmorero/Escritorio/joing_status.xml" );
//            XMLEncoder encoder = new XMLEncoder( os );
//            encoder.writeObject( this );
//            encoder.close();
//        }
//        catch( FileNotFoundException exc )
//        {
//            Logger.getLogger( PDEDesktop.class.getName() ).log( Level.SEVERE, null, exc );
//        }
//        finally
//        {
//            try{ os.close(); } catch( IOException ex ) { }
//        }
        
        for( TaskBar tb : vTaskBars )
        {
//            if( tb.instanceof PDETaskBar )
//                ((PDETaskBar) tb).save( writer );
            tb.close();
        }

        for( WorkArea wa : vWorkAreas )
        {
//            if( wa.instanceof PDEWorkArea )
//                ((PDEWorkArea) wa).save( writer );
            wa.close();
        }
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
        if( (waActive != wa) && vWorkAreas.contains( wa ) )
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
    
    //------------------------------------------------------------------------//
    // OTHER THINGS
    
    public int showNotification( String sMessage, Image icon )
    {   
        NotificationPanel notification = new NotificationPanel( sMessage, icon );
        int               nHandle      = notification.hashCode();
        
        htInfoPanels.put( nHandle, notification );
        
        // FIXME: Se muestra al mismo tiempo que la ventana a la que debe de servir de aviso
        //        y debería mostrarse inmediatamente
        getActiveWorkArea().add( notification );
        
        return nHandle;
    }
    
    public void hideNotification( int nHandle )
    {
        if( htInfoPanels != null && htInfoPanels.containsKey( nHandle ) )
        {
            htInfoPanels.get( nHandle ).close();
            htInfoPanels.remove( nHandle );
        }
    }
}