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
import java.beans.XMLEncoder;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.joing.common.desktopAPI.DesktopManager;
import org.joing.common.desktopAPI.desktop.Desktop;
import org.joing.common.desktopAPI.desktop.DesktopListener;
import org.joing.common.desktopAPI.workarea.WorkArea;
import org.joing.pde.desktop.workarea.PDEWorkArea;
import org.joing.common.desktopAPI.taskbar.TaskBar;
import org.joing.common.dto.vfs.FileText;
import org.joing.pde.desktop.container.PDECanvas;
import org.joing.pde.desktop.container.PDEFrame;
import org.joing.pde.desktop.taskbar.PDETaskBar;
import org.joing.pde.desktop.workarea.PDEWallpaper;
import org.joing.pde.swing.EventListenerList;
import org.joing.runtime.vfs.JoingFileChooser;
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
    
    private EventListenerList listenerList;
    
    private Hashtable<Integer, PDECanvas> htInfoPanels;
    
    //------------------------------------------------------------------------//
    
    public PDEDesktop()
    {
        vWorkAreas   = new Vector<WorkArea>();
        vTaskBars    = new Vector<TaskBar>();
        waActive     = null;
        listenerList = new EventListenerList();
        htInfoPanels = new Hashtable<Integer, PDECanvas>();
        
        initGUI();
    }
    
    //------------------------------------------------------------------------//
    
    public void load()
    {
        FileText ftStatus = null;
        
        if( ftStatus == null )
            createDefaultDesktop();
        else
            processPreferences( ftStatus.getContent() );
        
        setActiveWorkArea( getWorkAreas().get( 0 ) );
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
        
        //createTestComponents( getWorkAreas().get( 0 ) );   // TODO: quitarlo
    }
    
    private void processPreferences( BufferedReader in )
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
    
    private void initGUI()
    {
        setLayout( new BorderLayout() );
        pnlWorkAreas = new JPanel( new CardLayout() );
        add( pnlWorkAreas, BorderLayout.CENTER );
    }
    
    //------------------------------------------------------------------------//
    // Desktop interface implementation
    //------------------------------------------------------------------------//
    // CLOSEABLE INTERFACE
    
    public void close()
    { // Info at: http://java.sun.com/products/jfc/tsc/articles/persistence4/index.html
        FileOutputStream os = null;
        try
        {
            os = new FileOutputStream( "/home/fmorero/Escritorio/joing_status.xml" );
            XMLEncoder encoder = new XMLEncoder( os );
            encoder.writeObject( this );
            encoder.close();
        }
        catch( FileNotFoundException ex )
        {
            Logger.getLogger( PDEDesktop.class.getName() ).log( Level.SEVERE, null, ex );
        }
        finally
        {
            try
            {
                os.close();
            }
            catch( IOException ex )
            {
                Logger.getLogger( PDEDesktop.class.getName() ).log( Level.SEVERE, null, ex );
            }
        }
        
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
    
    //------------------------------------------------------------------------//
    // OTHER THINGS
    
    public int showNotification( String sMessage, Image icon, boolean bShowAnimation )
    {   
        NotificationPanel notification = new NotificationPanel( sMessage, icon, bShowAnimation );
        
        htInfoPanels.put( htInfoPanels.size() + 1, notification );
        
        getActiveWorkArea().add( notification );
        notification.animate();
        
        return htInfoPanels.size();
    }
    
    public void hideNotification( int nHandle )
    {
        if( htInfoPanels != null && htInfoPanels.contains( nHandle ) )
        {
            htInfoPanels.get( nHandle ).close();
            htInfoPanels.remove( nHandle );
        }
    }
    
    //------------------------------------------------------------------------//
    // JUST FOR TESTING
    //------------------------------------------------------------------------//
    
    private void createTestComponents( WorkArea wa )
    {
        PDEWallpaper wp0 = (PDEWallpaper) getWorkAreas().get( 0 ).getWallpaper();
        
        if( wp0 == null )
            wp0 = new PDEWallpaper();
        
        wp0.setImage( new ImageIcon( "/home/fmorero/Imágenes/iconos/duke/starwars.png" ) );
        getWorkAreas().get( 0 ).setWallpaper( wp0 );
        
        PDEWallpaper wp1 =  (PDEWallpaper) getWorkAreas().get( 1 ).getWallpaper();
        
        if( wp1 == null )
            wp1 = new PDEWallpaper();
        
        wp1.setImage( new ImageIcon( "/home/fmorero/Imágenes/reflections.jpg" ) );
        getWorkAreas().get( 1 ).setWallpaper( wp1 );
        
        SwingUtilities.invokeLater( new Runnable() 
        {
            public void run()
            {
                DesktopManager dm = org.joing.jvmm.RuntimeFactory.getPlatform().getDesktopManager();
                
                /*PDEFrame frame = (PDEFrame) dm.getRuntime().createFrame();
                frame.setTitle( "Frame" );
                frame.add( new JTextArea( "Soy un Frame" ) );
                
                dm.getDesktop().getActiveWorkArea().add( frame );*/
                
                
                //JFileChooser fc = new JFileChooser( JoingFileSystemView.getFileSystemView() );
                //PDEWorkArea wa = (PDEWorkArea) getActiveWorkArea();
                //JOptionPane.showInternalMessageDialog( (JDesktopPane) wa, fc );
                
                JoingFileChooser jfc = new JoingFileChooser();
                int nSelection = jfc.showDialog( null, "Zarva" );
                
//                if( nSelection == JoingFileChooser.APPROVE_OPTION )
//                    dm.getRuntime().showMessageDialog( "Result", "Approved" );
//                else
//                    dm.getRuntime().showMessageDialog( "Result", "Canceled" );
                
                
//                if( nSelection == JoingFileChooser.APPROVE_OPTION )
//                    dm.getRuntime().showMessageDialog( "Result", "Approved" );
//                else
//                    dm.getRuntime().showMessageDialog( "Result", "Canceled" );
            }
        } );

        
//        SwingUtilities.invokeLater( new Runnable() 
//        {
//            public void run()
//            {
//                new org.joing.notes.Notes();
//            }
//        } );

//        SwingUtilities.invokeLater( new Runnable()
//        {
//            public void run()
//            {
//                DeskDialog dialog = org.joing.jvmm.RuntimeFactory.getPlatform().getDesktopManager().getRuntime().createDialog();
//                           dialog.setTitle( "Testing File Chosser" );
//                           dialog.add( (DeskComponent) new EditUser() );
//                org.joing.jvmm.RuntimeFactory.getPlatform().getDesktopManager().getDesktop().getActiveWorkArea().add( dialog );
//                
//            }
//        } );
        /*NasaPhoto nasa = new NasaPhoto();
                  nasa.setBounds( 10,350, nasa.getPreferredSize().width, nasa.getPreferredSize().height );
        wa.add( nasa );
        
        JLabel lblCanvas = new JLabel( "<html><h2>Soy un canvas.</h2>Y esto es <u>texto <font color=\"#0066CC\">HTML</font></u>.</h3></html>" );
        PDECanvas canvas = new PDECanvas();
                  canvas.add( lblCanvas );
                  canvas.setBounds( 330, 130, 240, 80 );
        wa.add( canvas );*/
        
        //---------------------------------------------------------------------
       
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
                    
        PDEFrame frm = new PDEFrame();
                 frm.setTitle( "Example Join'g Frame" );
                 frm.add( new JLabel( "Translucency" ), BorderLayout.NORTH );
                 frm.add( slrTranslucency, BorderLayout.SOUTH );
                 frm.setBounds( 380, 200, 600, 580 );
        wa.add( frm, false );   // Do not autoarrange
    }
}