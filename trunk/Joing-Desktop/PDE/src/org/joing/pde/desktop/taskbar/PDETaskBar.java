/*
 * TaskBar.java
 *
 * Created on 10 de febrero de 2007, 20:47
 *
 * (c) 2006 - Francisco Morero Peyrona
 *
 * License: {license}
 */

package org.joing.pde.desktop.taskbar;

import java.awt.Component;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;
import javax.swing.border.EtchedBorder;
import org.joing.api.desktop.enums.TaskBarOrientation;
import org.joing.api.desktop.taskbar.TaskBarListener;
import org.joing.pde.desktop.taskbar.clock.ClockDigital;
import org.joing.pde.desktop.taskbar.frameslist.FramesList;
import org.joing.pde.desktop.taskbar.start.StartButton;
import org.joing.pde.desktop.taskbar.systray.SysTray;
import org.joing.pde.runtime.ColorSchema;
import org.joing.api.desktop.taskbar.TaskBar;

/**
 * TaskBar can hold JComponents or instances of TaskBarPanel
 * 
 * @author Francisco Morero Peyrona
 */
public class PDETaskBar extends JPanel implements TaskBar
{
    private TaskBarOrientation orientation;
    private boolean            bAutoHide;
    
    //------------------------------------------------------------------------//
    
    /** Creates a new instance of TaskBar */
    public PDETaskBar()
    {
        orientation = TaskBarOrientation.BOTTOM;
        bAutoHide   = false;
        
        setOpaque( true );
        setLayout( new BoxLayout( this, BoxLayout.X_AXIS ) );
        setBackground( ColorSchema.getInstance().getTaskBarBackground() );
        setBorder( new EtchedBorder( EtchedBorder.LOWERED ) );
        setComponentPopupMenu( createPopupMenu() );
    }
    
    public void createDefaultComponents()
    {
        TaskPanel tpFrameList = new TaskPanel();
                  tpFrameList.add( new FramesList() );
                
        add( new StartButton() );
        add( tpFrameList );
        addSysTray();
        getSysTray().add( new ClockDigital() );
    }
    
    /**
     * Returns the SysTray in this Taskbar.
     * <p>
     * If there is more than one SysTray (it shoud not but could happen), the
     * first one is returned.
     */
    public SysTray getSysTray()
    {
        // TODO: hacer la búsqueda recursiva por si está dentro de otro comp.
        Component[] aComp = getComponents();
        
        for( int n = 0; n < aComp.length; n++ )
        {
            if( aComp[n] instanceof SysTray )
                return (SysTray) aComp[n];
            /*else
                return getSysTray( aComp[n] );*/
        }
        
        return null;  
    }
    
    /**
     * Creates a new instance of SysTray and adds it and the end (trailing) of 
     * the TaskBar.
     */
    public void addSysTray()
    {
        add( Box.createHorizontalGlue() );
        add( new SysTray() );
    }
    
    public void addSysTray( Point ptWhere )
    {
        // TODO: hacerlo
    }
    
    //------------------------------------------------------------------------//
    // TaskBar interface implementation methods
    
    public Component add( Component component )
    {
        super.add( component );
        validate();
        fireComponentAdded( component );
        
        return component;
    }
    
    public void remove( Component component )
    {
        super.remove( component );
        validate();
        fireComponentRemoved( component );
    }
    
    /**
     * Same as <code>this.add( Component component )</code>.<br>
     * PDE does not support this method.
     */
    public Component add( Component component, Point pt )
    {
        return add( component );
    }

    public List<Component> getOfType( Class clazz )
    {
        ArrayList<Component> list = new ArrayList<Component>();
        Component[] aComponent    = getComponents();
        
        for( int n = 0; n < aComponent.length; n++ )
        {
            if( clazz.isInstance( aComponent[n] ) )
                list.add( aComponent[n] );
        }
        
        return list;
    }

    public boolean isAutoHide()
    {
        return bAutoHide;
    }

    public void setAutoHide( boolean bAutoHide )
    {
        this.bAutoHide = bAutoHide;
    }
    
    public TaskBarOrientation getOrientation()
    {
        return orientation;
    }

    public void setOrientation( TaskBarOrientation orientation )
    {
        this.orientation = orientation;
    }

    public void addTaskBarListener( TaskBarListener tbl )
    {
        listenerList.add( TaskBarListener.class, tbl );
    }

    public void removeTaskBarListener( TaskBarListener tbl )
    {
        listenerList.remove( TaskBarListener.class, tbl );
    }
    
    //------------------------------------------------------------------------//
    
    protected void fireComponentAdded( Component component )
    {
        Object[] listeners = listenerList.getListenerList();
        
        // Process the listeners last to first, notifying
        for( int n = listeners.length - 2; n >= 0; n -= 2 )
        {
            if( listeners[n] == TaskBarListener.class )
                ((TaskBarListener) listeners[n+1]).componentAdded( component );
        }
    }
    
    protected void fireComponentRemoved( Component component )
    {
        Object[] listeners = listenerList.getListenerList();
        
        // Process the listeners last to first, notifying
        for( int n = listeners.length - 2; n >= 0; n -= 2 )
        {
            if( listeners[n] == TaskBarListener.class )
                ((TaskBarListener) listeners[n+1]).componentRemoved( component );
        }
    }
    
    //------------------------------------------------------------------------//
       
    private JPopupMenu createPopupMenu()
    {
        JPopupMenu popup = new JPopupMenu();
        
        // TODO: hacerlo
        
        return popup;
    }
}