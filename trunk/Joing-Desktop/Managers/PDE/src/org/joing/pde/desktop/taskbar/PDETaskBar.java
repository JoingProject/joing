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
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.border.EtchedBorder;
import org.joing.common.desktopAPI.DeskComponent;
import org.joing.common.desktopAPI.DeskContainer;
import org.joing.common.desktopAPI.taskbar.TaskBarListener;
import org.joing.common.desktopAPI.taskbar.TaskBarPanel;
import org.joing.pde.desktop.taskbar.clock.ClockDigital;
import org.joing.pde.desktop.taskbar.frameslist.FramesList;
import org.joing.pde.desktop.taskbar.start.StartButton;
import org.joing.pde.desktop.taskbar.systray.SysTray;
import org.joing.pde.ColorSchema;
import org.joing.common.desktopAPI.taskbar.TaskBar;
import org.joing.pde.desktop.taskbar.waSwitcher.WorkAreaSwitcher;

/**
 * TaskBar can hold JComponents or instances of TaskBarPanel
 * 
 * @author Francisco Morero Peyrona
 */
public class PDETaskBar extends JPanel implements TaskBar
{
    private static final String DONT_USE_ME = "Do not use me";
    
    private TaskBar.Orientation orientation;
    private boolean             bAutoHide;
    
    //------------------------------------------------------------------------//
    
    /** Creates a new instance of TaskBar */
    public PDETaskBar()
    {
        orientation = TaskBar.Orientation.BOTTOM;
        bAutoHide   = false;
        
        setOpaque( true );
        setLayout( new BoxLayout( this, BoxLayout.X_AXIS ) );
        setBackground( ColorSchema.getInstance().getTaskBarBackground() );
        setBorder( new EtchedBorder( EtchedBorder.LOWERED ) );
        setComponentPopupMenu( createPopupMenu() );
    }
    
    public void createDefaultComponents()
    {                
        add( (DeskComponent) new StartButton() );
        add( (DeskComponent) new FramesList() );
        add( (DeskComponent) new WorkAreaSwitcher() );
        ///add( Box.createHorizontalGlue() );
        add( (DeskComponent) new SysTray() );
        getSysTray().add( (DeskComponent) new ClockDigital() );
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
    
    //------------------------------------------------------------------------//
    // TaskBar interface implementation methods
    
    
    public void add( TaskBarPanel tbp )
    {
        super.add( (Component) tbp );
        validate();
        fireComponentAdded( tbp );
    }

    public void add( TaskBarPanel tbp, Point pt )
    {
        add( tbp );   // TODO: mejorarlo
    }

    public void add( DeskComponent tbo )
    {
        super.add( (Component) tbo );
        validate();
        fireComponentAdded( tbo );
    }

    public void add( DeskComponent tbo, Point pt )
    {
        add( tbo );   // TODO: mejorarlo
    }

    public void remove( TaskBarPanel tbp )
    {
        super.remove( (Component) tbp );
        validate();
        fireComponentRemoved( tbp );
    }
    
    public void remove( DeskComponent dc )
    {
        super.remove( (Component) dc );
        validate();
        fireComponentRemoved( dc );
    }

    public void close()
    {
        // FIXME
        throw new UnsupportedOperationException( "Not supported yet." );
    }
    
    // Just to avoid accidental use of them  ---------------------------------------------------------
    public Component add( Component c )             { throw new IllegalAccessError(DONT_USE_ME); }
    public Component add( Component c, int n )      { throw new IllegalAccessError(DONT_USE_ME); }
    public Component add( String s, Component c )   { throw new IllegalAccessError(DONT_USE_ME); }
    public void add( Component c, Object o )        { throw new IllegalAccessError(DONT_USE_ME); }
    public void add( Component c, Object o, int n ) { throw new IllegalAccessError(DONT_USE_ME); }
    public void remove( Component c )               { throw new IllegalAccessError(DONT_USE_ME); }
    //------------------------------------------------------------------------------------------------

    public boolean isAutoHide()
    {
        return bAutoHide;
    }

    public void setAutoHide( boolean bAutoHide )
    {
        this.bAutoHide = bAutoHide;
    }
    
    public TaskBar.Orientation getOrientation()
    {
        return orientation;
    }

    public void setOrientation( TaskBar.Orientation orientation )
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
    
    protected void fireComponentAdded( DeskComponent dc )
    {
        fire( dc, true );
    }
    
    protected void fireComponentRemoved( DeskComponent dc )
    {
        fire( dc, false );
    }
    
    protected void fireComponentAdded( DeskContainer dc )
    {
        fire( dc, true );
    }
    
    protected void fireComponentRemoved( DeskContainer dc )
    {
        fire( dc, false );
    }
    
    //------------------------------------------------------------------------//
    
    private void fire( Object obj, boolean bAdded )
    {
        Object[] listeners = listenerList.getListenerList();
        
        // Process the listeners last to first, notifying
        for( int n = listeners.length - 2; n >= 0; n -= 2 )
        {
            if( listeners[n] == TaskBarListener.class )
            {
                if( bAdded )
                {
                    if( obj instanceof DeskComponent )
                        ((TaskBarListener) listeners[n+1]).componentAdded( (DeskComponent) obj );
                    else
                        ((TaskBarListener) listeners[n+1]).componentAdded( (DeskContainer) obj );
                }
                else
                {
                    if( obj instanceof DeskComponent )
                        ((TaskBarListener) listeners[n+1]).componentRemoved( (DeskComponent) obj );
                    else
                        ((TaskBarListener) listeners[n+1]).componentRemoved( (DeskContainer) obj );
                }
            }
        }
    }
       
    private JPopupMenu createPopupMenu()
    {
        JPopupMenu popup = new JPopupMenu();
        
        // TODO: hacerlo
        
        return popup;
    }
}