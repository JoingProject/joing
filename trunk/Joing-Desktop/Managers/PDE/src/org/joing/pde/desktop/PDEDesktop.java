/*
 * Copyright (C) 2007, 2008 Join'g Team Members. All Rights Reserved.
 * Join'g Team Members are listed at project's home page. By the time of 
 * writting this at: https://joing.dev.java.net/servlets/ProjectMemberList.
 *
 * This file is part of Join'g project: www.joing.org
 *
 * GNU Classpath is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the free
 * Software Foundation; either version 3, or (at your option) any later version.
 * 
 * GNU Classpath is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * GNU Classpath; see the file COPYING.  If not, write to the Free Software 
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */
package org.joing.pde.desktop;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Dimension;
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
import org.joing.kernel.api.desktop.desktop.Desktop;
import org.joing.kernel.api.desktop.desktop.DesktopListener;
import org.joing.kernel.api.desktop.workarea.WorkArea;
import org.joing.pde.desktop.workarea.PDEWorkArea;
import org.joing.kernel.api.desktop.taskbar.TaskBar;
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
    
    private EventListenerList listenerList;       // NEXT: Cambiar esto por la de JComponent
    
    private Hashtable<Integer, PDECanvas> htInfoPanels;   // Synchronized implementation
    
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
            @Override
            protected Object doInBackground() throws Exception
            {
                // Do not move next lines !
                File fSavedStatus = null;
        
                if( fSavedStatus == null )
                    createDefaultDesktop();
//                else
//                    processSavedStatus( ftSavedStatus.getContent() );
                
                return null;
            }
            
            @Override
            protected void done()
            {
                setActiveWorkArea( getWorkAreas().get( 0 ) );
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
        
        // Calculates size and postion
        WorkArea  wa  = getActiveWorkArea();
        Dimension dim = notification.getPreferredSize();
        notification.setSize( dim );
        notification.setLocation( wa.getWidth() - dim.width, wa.getHeight() - dim.height - 5 );   // NEXT: no sé por qué tengo que hacer el -5        
        
        getActiveWorkArea().add( notification );
        
        htInfoPanels.put( nHandle, notification );
        return nHandle;
    }
    
    public void hideNotification( int nHandle )
    {
        if( htInfoPanels != null && htInfoPanels.containsKey( nHandle ) )
        {
            PDECanvas canvas = htInfoPanels.get( nHandle );
            
            synchronized( this )
            {
                canvas.close();
            }
            
            htInfoPanels.remove( nHandle );
        }
    }
}