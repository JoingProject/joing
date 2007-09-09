/*
 * WorkAreaItem.java
 *
 * Created on 10 de febrero de 2007, 20:23
 *
 * (c) 2006 - Francisco Morero Peyrona
 *
 * License: {license}
 */

package org.joing.pde.desktop.workarea.items;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import org.joing.pde.desktop.Selectable;

/**
 * Base class for all components that can be shown in the desk.
 * 
 * @author fmorero
 */
public abstract class WorkAreaItem extends JPanel implements Selectable
{
    private boolean bSelected = false;
    
    //------------------------------------------------------------------------//
    
    public boolean isSelected()
    {
        return bSelected;
    }
    
    /** 
     * When a subclass redefines this method, the redefined one should do all its work 
     * and later call <code>super.setSelected( boolean )</code>
     *  
     * @param b  New selected status 
     */
    public void setSelected( boolean b )
    {
        if( b != isSelected() )
        {
            bSelected = b;
            moveToFront();
            
            fireWorkAreaItemChangeEvent( b ? WorkAreaItemChangeEvent.SELECTED_INFORM : 
                                              WorkAreaItemChangeEvent.DESELECTED_INFORM, null );
        }
    }
    
    /**
     * Convenience method that moves this component to position 0 if its 
     * parent is a <code>JLayeredPane</code>.
     */
    public void moveToFront() 
    {
        if( getParent() != null && getParent() instanceof JLayeredPane )
            ((JLayeredPane) getParent()).moveToFront( this );
    }

    /**
      * Convenience method that moves this component to position -1 if its 
      * parent is a <code>JLayeredPane</code>.
      */
    public void moveToBack() 
    {
        if( getParent() != null && getParent() instanceof JLayeredPane )
            ((JLayeredPane) getParent()).moveToBack(this);
    }
    
    //------------------------------------------------------------------------//
    // Eventos
    
    // Para los que quieren eschuchar cuando el componente es movido (setLocation(...))
    // o seleccionado / deseleccionado
    public void addWorkAreaItemChangeEventListener( WorkAreaItemChangeEventListener l )
    {
       this.listenerList.add( WorkAreaItemChangeEventListener.class, l );
    }
    
    public void removeWorkAreaItemChangeEventListener( WorkAreaItemChangeEventListener l )
    {
       this.listenerList.remove( WorkAreaItemChangeEventListener.class, l );
    }
    
    protected void fireWorkAreaItemChangeEvent( int nType, MouseEvent me )
    {
        WorkAreaItemChangeEvent event = new WorkAreaItemChangeEvent( this, me );
                                     event.setType( nType );
        Object[] listeners = this.listenerList.getListenerList();
        
        for( int n = listeners.length - 2; n >= 0; n -= 2 )
        {
            if( listeners[n] == WorkAreaItemChangeEventListener.class )
                ((WorkAreaItemChangeEventListener) listeners[n+1]).deskComponentChanged( event );
        }
    }
    
    // Para los q quieran eschuchar las acciones: OPEN, REMOVE, ...
    public void addActionListener( ActionListener l )
    {
       this.listenerList.add( ActionListener.class, l );
    }
    
    public void removeActionListenerListener( ActionListener l )
    {
       this.listenerList.remove( ActionListener.class, l );
    }
    
    protected void fireActionPerformed( String sCommand )
    {
        ActionEvent event = new ActionEvent( this, ActionEvent.ACTION_PERFORMED, sCommand );
        
        Object[] listeners = listenerList.getListenerList();

        for( int n = listeners.length - 2; n >= 0; n -= 2 )
        {
            if( listeners[n] == ActionListener.class )
                ((ActionListener) listeners[n+1]).actionPerformed( event );
        }
    }
}