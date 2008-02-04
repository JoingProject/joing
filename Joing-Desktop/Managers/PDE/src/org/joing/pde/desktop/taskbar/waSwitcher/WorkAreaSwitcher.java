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
import java.util.List;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;
import org.joing.common.desktopAPI.desktop.Desktop;
import org.joing.common.desktopAPI.desktop.DesktopListener;
import org.joing.common.desktopAPI.taskbar.TaskBar;
import org.joing.common.desktopAPI.workarea.WorkArea;
import org.joing.pde.desktop.workarea.PDEWorkArea;
import org.joing.pde.swing.PDEDeskComponent;

/**
 *
 * @author fmorero
 */
public class WorkAreaSwitcher extends PDEDeskComponent
{
    private GridLayout grid;
    
    //------------------------------------------------------------------------//
    
    public WorkAreaSwitcher()
    {
        grid = new GridLayout( 1, 0, 0, 0 );
        
        setLayout( grid );
        setBorder( new LineBorder( Color.black, 1 ) );
        setInheritsPopupMenu( true );  // It is also inherited by sub-components
        //setHandleVisible( false );
        
        Desktop desktop = org.joing.jvmm.RuntimeFactory.getPlatform().getDesktopManager().getDesktop();
        
        // Traverse existing workareas: one or more workareas would exist when this component is shown.
        List<WorkArea> lstWorAreas = desktop.getWorkAreas();
        
        for( WorkArea wa : lstWorAreas )
            onWorkAreaAdded( wa );
        //--------------------------------------------
        
        // Now the listener for future add/remove
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
    
    protected JPanel getAboutPanel()
    {
        return null;   // TODO: hacerlo
    }

    protected JPanel getPreferencesPanel()
    {
        return new Preferences();
    }

    protected void onPreferencesChanged( JPanel pnl )
    {
        Preferences pnlPrefs = (Preferences) pnl;
        
        // TODO: procesar los cambios
    }
    
    //------------------------------------------------------------------------//
    
    private void onWorkAreaAdded( WorkArea wa )
    {
        calculateSizes( grid.getColumns() + 1 );
        
        Map map = new Map( (PDEWorkArea) wa );
        
        grid.setColumns( grid.getColumns() + 1 );
        add( map );
        grid.layoutContainer( this );
    }
    
    private void onWorkAreaRemoved( WorkArea wa )
    {
        calculateSizes( grid.getColumns() - 1 );
        
        remove( getMapFor( wa ) );       // Removes the button from the panel
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
            if( (aComp[n] instanceof Map) )
            {
                Map map = (Map) aComp[n];
                
                if( map.getTarget() == wa )
                    return map;
            }
        }
        
        return null;    // Should never arrive to here
    }
}