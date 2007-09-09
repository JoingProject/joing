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
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import org.joing.desktop.api.Desktop;
import org.joing.desktop.api.WorkArea;
import org.joing.pde.desktop.workarea.WorkAreaImpl;
import org.joing.taskbar.api.TaskBar;

/**
 *
 * @author Francisco Morero Peyrona
 */
public class DesktopImpl extends JPanel implements Desktop
{ // TODO: clase sin terminar
    private Vector<TaskBar>  vTaskBars;
    private Vector<WorkArea> vWorkAreas;
    
    private int nActiveWorkArea;
    
    //------------------------------------------------------------------------//
    
    /** Creates a new instance of DesktopImpl */
    public DesktopImpl()
    {
        setLayout( new BorderLayout() );
        
        vWorkAreas = new Vector<WorkArea>();
        vTaskBars  = new Vector<TaskBar>();
        
        for( WorkArea wa : getWorkAreasFromServer() )
            addWorkArea( wa );
        
        for( TaskBar tb : getTaskBarsFromServer() )
            addTaskBar( tb );
    }
    
    //------------------------------------------------------------------------//
    
    public List<WorkArea> getWorkAreas()
    {
        // Defensive copy
        ArrayList<WorkArea> list = new ArrayList<WorkArea>( vWorkAreas.size() );
                            list.addAll( vWorkAreas );
        return list;
    }
    
    public void addWorkArea( WorkArea wa )
    {
        add( (Component) wa );
        vWorkAreas.add( wa );
        setActiveWorkArea( wa );
    }
    
    public void removeWorkArea( WorkArea wa )
    {
        if( vWorkAreas.size() > 0 && vWorkAreas.contains( wa ) )
        {
            remove( (Component) wa );
            vWorkAreas.remove( wa );
        }
    }
    
    public WorkArea getActiveWorkArea()
    {
        return vWorkAreas.get( nActiveWorkArea );
    }
    
    public void setActiveWorkArea( WorkArea wa )
    {
        int nIndex = vWorkAreas.indexOf( wa );
        
        if( nIndex != -1 )
        {
            nActiveWorkArea = nIndex;
            // TODO: mostrarla -> ((CardLayout) getLayout()).first( this );
        }
    }
    
    public List<TaskBar> getTaskBars()
    {
        // Defensive copy
        ArrayList<TaskBar> list = new ArrayList<TaskBar>( vTaskBars.size() );
                           list.addAll( vTaskBars );
        return list;
    }

    public void addTaskBar( TaskBar taskbar )
    {
        // TODO: hacerlo
    }
    
    public void removeTaskBar( TaskBar taskbar )
    {
        // TODO: hacerlo
    }
    
    //------------------------------------------------------------------------//
    
    private List<WorkArea> getWorkAreasFromServer()
    {
        ArrayList<WorkArea> list = new ArrayList<WorkArea>();   // TODO: leer del server
        
        if( list.isEmpty() )
            list.add( new WorkAreaImpl() );
            
        return list;
    }
    
    private List<TaskBar> getTaskBarsFromServer()
    {
        ArrayList<TaskBar> list = new ArrayList<TaskBar>();   // TODO: leer del server
        
        ///if( list.isEmpty() )
        ///    list.add( new WorkAreaImpl() );
            
        return list;
    }
}