/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.joing.pde.desktop.taskbar.waSwitcher;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.border.LineBorder;
import org.joing.api.desktop.Desktop;
import org.joing.api.desktop.DesktopListener;
import org.joing.api.desktop.taskbar.TaskBar;
import org.joing.api.desktop.workarea.WorkArea;
import org.joing.pde.desktop.container.PDEFrame;
import org.joing.pde.desktop.workarea.PDEWorkArea;
import org.joing.pde.runtime.PDERuntime;

/**
 *
 * @author fmorero
 */
public class WorkAreaSwitcher extends JPanel  // FIXME: extends ElMismoQueTodosLosElementosDeLasToolBar  // Así puede heredar el PopupMenu
{
    private GridLayout grid;
    
    //------------------------------------------------------------------------//
    
    public WorkAreaSwitcher()
    {
        grid = new GridLayout( 1, 0, 2, 0 );
        
        setLayout( grid );
        setBorder( new LineBorder( Color.black, 1 ) );
        setComponentPopupMenu( new ThisPopupMenu() );    // It is inherited by sub-components
        
        Desktop desktop = PDERuntime.getRuntime().getDesktopManager().getDesktop();
                
        List<WorkArea> lstWorAreas = desktop.getWorkAreas();
        
        for( WorkArea wa : lstWorAreas )
            onWorkAreaAdded( wa );
        
        desktop.addDesktopListener( new DesktopListener()
        {
            public void workAreaAdded( WorkArea wa )   { onWorkAreaAdded( wa );   }
            public void workAreaRemoved( WorkArea wa ) { onWorkAreaRemoved( wa ); }
            public void workAreaSelected( WorkArea waPrevious, WorkArea waCurrent ) { onWorkAreaSelected( waPrevious, waCurrent ); }
            public void taskBarAdded( TaskBar tb )     {}
            public void taskBarRemoved( TaskBar tb )   {}
        } );
        
        calculateSizes( lstWorAreas.size() );
    }
    
    //------------------------------------------------------------------------//
    
    private void onWorkAreaAdded( WorkArea wa )
    {
        calculateSizes( grid.getColumns() + 1 );
        
        Map map = new Map( (PDEWorkArea) wa );
        
        grid.setColumns( grid.getColumns() + 1 );
        super.add( map );
        grid.layoutContainer( this );
    }
    
    private void onWorkAreaRemoved( WorkArea wa )
    {
        calculateSizes( grid.getColumns() - 1 );
        
        super.remove( getMapFor( wa ) );       // Removes the button from the panel
        grid.setColumns( grid.getColumns() - 1 );
        grid.layoutContainer( this );
    }
    
    private void onWorkAreaSelected( WorkArea waPrevious, WorkArea waCurrent )
    {   
        if( waPrevious != null )
            getMapFor( waPrevious ).setSelected( false );
        
        if( waCurrent != null )
            getMapFor( waCurrent ).setSelected( true );
    }
    
    private void calculateSizes( int nComponents )
    {
        Insets    insets = getBorder().getBorderInsets( this );   
        int       nWidth = (nComponents * 22) + ((nComponents - 1) * grid.getHgap()) + (insets.left * 2);
        Dimension dim    = new Dimension( nWidth, 22 + (insets.top * 2) );
        
        setMinimumSize(   dim );
        setMaximumSize(   dim );
        setPreferredSize( dim );
    }
    
    private Map getMapFor( WorkArea wa )
    {
        Component[] aComp = getComponents();
        
        for( int n = 0; n < aComp.length; n++ )
        {
            Map map = (Map) aComp[n];
            
            if( map.getTarget() == wa )
                return map;
        }
        
        return null;    // Should never arrive to here
    }
    
    //------------------------------------------------------------------------//
    // INNER CLASS: Popup Menu
    //------------------------------------------------------------------------//
    private final class ThisPopupMenu extends JPopupMenu implements ActionListener
    {
        private final static String PREFERENCES = "PR";
        
        private ThisPopupMenu()
        {
            JMenuItem item;
            
            item = new JMenuItem( "Preferences" );
            item.setActionCommand( PREFERENCES );
            item.addActionListener( this );
            
            add( item );
        }
        
        public void actionPerformed( ActionEvent ae )
        {
            Properties prop = new Properties();
            // FIXME: cambiar esta frame por una dialog modal
            PDEFrame frame = new PDEFrame( "WorkArea Selector Preferences" );
                     frame.setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );
                     frame.getContentPane().add( prop );
                     
            PDERuntime.getRuntime().add( frame );
                     
            // TODO: Al cerrar la dialog, si se hizo click en aceptar, procesar el panel de Porperties
        }
    }
}