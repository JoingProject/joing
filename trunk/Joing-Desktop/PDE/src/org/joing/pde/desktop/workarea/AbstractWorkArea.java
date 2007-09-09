/*
 * AbstractWorkArea.java
 *
 * Created on 10 de febrero de 2007, 19:05
 *
 * (c) 2006 - Francisco Morero Peyrona
 *
 * License: {license}
 */

package org.joing.pde.desktop.workarea;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.SwingUtilities;
import javax.swing.event.MouseInputAdapter;
import org.joing.pde.desktop.Selectable;
import org.joing.pde.desktop.workarea.items.WorkAreaItem;
import org.joing.pde.desktop.workarea.items.WorkAreaItemChangeEvent;
import org.joing.pde.desktop.workarea.items.WorkAreaItemChangeEventListener;

/**
 * This class contains internal operativity for the Desk.
 * 
 * It is not public because all public interface has to be accesed using Desk
 * @author Francisco Morero Peyrona
 */
abstract class AbstractWorkArea extends JDesktopPane
{
    private Dimension gridDimension = new Dimension( 16,16 );  // Tamaño de la cuadrícula del grid
    private boolean   bSnapToGrid   = true;
    
    private TheItemChangeEventListener ticel = new TheItemChangeEventListener();
    
    //-------------------------------------------------------------------------//
    
    public AbstractWorkArea()
    {
        // Add listener for mouse events to: 
        //  · Get the focus
        //  · To deselectAll()
        //  · To invoke showPopupMenu(...)
        addMouseListener( new MouseInputAdapter()
        {
            public void mousePressed( MouseEvent me )
            {
                if( ! AbstractWorkArea.this.isFocusOwner() )
                    AbstractWorkArea.this.requestFocusInWindow();
                
                if( me.isPopupTrigger() )
                    showPopupMenu( me.getPoint() );
            }

            public void mouseReleased( MouseEvent me )
            {
                if( me.isPopupTrigger() )
                    showPopupMenu( me.getPoint() );
                else
                    // Si llega aqui es q se ha hecho clic en el desktop, ya q si se hace clic en un 
                    // componente contenido en el desktop, el desktop no llega a recibir el evento.
                    deSelectAll( Component.class );
            }
        } );
    }
    
    // Las pongo solo para no llamarlas por error y q se monte un zapatiesto -------------------------
    public Component add( Component c )                        { throw new IllegalAccessError("Do not use me"); }
    public Component add( Component c, int n )                 { throw new IllegalAccessError("Do not use me"); }
    public Component add( String s, Component c )              { throw new IllegalAccessError("Do not use me"); }
    public void add( Component c, Object o, int n )            { throw new IllegalAccessError("Do not use me"); }
    public void add( Component component, Object constraints ) { throw new IllegalAccessError("Do not use me"); }
    //------------------------------------------------------------------------------------------------
    
    // Esta es la que hay que usar
    public void add( Component component, Integer nLayer )
    {
        // @TODO comprobar q el objeto no es más grande q el escritorio y
        //      q sus bounds no están fuera del escritorio
        
        super.add( component, nLayer );
        
        if( component instanceof WorkAreaItem )
            ((WorkAreaItem) component).addWorkAreaItemChangeEventListener( this.ticel );
    }
    
    /**
     * Has to be redefined by subclases that want to show a popup menu.
     * 
     * @param p Where mouse was clicked
     */
    protected abstract void showPopupMenu( Point p );
    
    //------------------------------------------------------------------------//
    // Grid issues
    //------------------------------------------------------------------------//
    
    public boolean isSnapToGrid()
    {
        return this.bSnapToGrid;
    }

    public void setSnapToGrid( boolean snapToGrid )
    {
        this.bSnapToGrid = snapToGrid;
    }

    public Dimension getGridDimension()
    {
        return this.gridDimension;
    }

    /**
     * Set new grid density.
     * 
     * @param gridDimension
     */
    public void setGridDimension( Dimension gridDimension )
    {
        this.gridDimension = gridDimension;
    }
    
    /**
     * If needed, moves passed component to be aligned to grid.
     * 
     * @param comp
     */
    public void alignToGrid( Component comp )
    {
        if( isSnapToGrid() )
        {
            int nWidth  = getGridDimension().width;
            int nHeight = getGridDimension().height;
            
            // + 5 para despegarlo de los bordes izquierdo y superior
            comp.setLocation( Math.round( comp.getLocation().x / nWidth  ) * nWidth  + 5,
                              Math.round( comp.getLocation().y / nHeight ) * nHeight + 5 );
        }
    }
    
    /**
     * Find first available location on desktop to place a component
     * 
     * @param comp
     * @return An instance of <code>Point</code> indicating the available place
     */
    protected Point findEmptyLocation4( Component comp )
    {
        Collection cLaunchers = getAll( WorkAreaItem.class );
        // @TODO hacerlo mejor
        return new Point( cLaunchers.size() * 80 + 10, cLaunchers.size() * 80 + 10);
    }
    
    //-------------------------------------------------------------------------//
    // Components issues
    //-------------------------------------------------------------------------//

    /**
     * Removes from clipboard those components that match passed class.
     *  <p>
     * To remove all, simply pass <code>Component.class</code>
     *   
     * @param clazz Desired class
     */
    public void cutSelectedComponents( Class clazz )
    {// TODO: terminarlo
        /*List vSelected = getSelected( clazz );
        
        if( vSelected.size() > 0 )
        {
            Client.getClient().getClipBoard().clear();
            Client.getClient().getClipBoard().add( vSelected );
            
            removeSelectedComponents( clazz );
        }*/
    }
    
    /**
     * Copy to clipboard those selected components that match passed class.
     *  <p>
     * To copy all seleceted components, simply pass <code>Component.class</code>
     *   
     * @param clazz Desired class
     */
    public void copySelectedComponents( Class clazz )
    {
        // TODO: terminarlo
        /*List vSelected = getSelected( clazz );
        
        if( vSelected.size() > 0 )
        {
            Client.getClient().getClipBoard().clear();
            Client.getClient().getClipBoard().add( vSelected );
        }*/
    }

    /**
     * Copy from clipboard to desktop those components that match passed class.
     * <p>
     * To copy all components in clipboard, simply pass <code>Component.class</code>
     *   
     * @param clazz Desired class
     */
    public void pasteSelectedComponents( Class clazz )
    {
        // TODO: terminarlo
        /*
        if( ! Client.getClient().getClipBoard().isEmpty() )
        {
            Iterator  it   = Client.getClient().getClipBoard().getElements();
            Component root = SwingUtilities.getRoot( this );
                      root.setCursor( Cursor.getPredefinedCursor( Cursor.WAIT_CURSOR ) );
            
            while( it.hasNext() )
            {
                Object obj = it.next();
                
                if( obj instanceof com.peyrona.webpc.client.iapi.DeskComponent
                    &&
                    obj instanceof Cloneable )
                {
                    // @FIXME Arreglarlo: imagino q habrá q hacerlo con reflection: bsuacndo si hay un método public llamado clone()
                }
            }
            
            root.setCursor( Cursor.getPredefinedCursor( Cursor.DEFAULT_CURSOR) );
        }*/
    }

    /**
     * Remove from desktop those components in clipboard that match passed class.
     * <p>
     * To remove all components in clipboard, simply pass <code>Component.class</code>
     * 
     * @param clazz Desired class
     */
    public void removeSelectedComponents( Class clazz )
    {
        List vSelected = getSelected( clazz );
        
        if( vSelected.size() > 0 )
        {
            while( vSelected.size() > 0 )
                remove( (Component) vSelected.get( 0 ) );
            
            repaint();  // Sin repaint() no funciona: NO tocar
        }
    }   
    
    //------------------------------------------------------------------------//
    // METODOS LLAMADOS DESDE EL LISTENER
    
    protected void selectionRequested( WorkAreaItem wa )
    {
        deSelectAll( WorkAreaItem.class );
        wa.setSelected( true );
    }
    
    protected void selectionIncrementalRequested( WorkAreaItem wa )
    {
        wa.setSelected( true );
    }
    
    protected void deselectionIncrementalRequested( WorkAreaItem wa )
    {
        wa.setSelected( false );
    }
    
    protected void movedInformed( WorkAreaItem wa )
    {
        alignToGrid( wa );
    }
    
    //-------------------------------------------------------------------------//
    // Selección y deselección
    //-------------------------------------------------------------------------//

    /**
     * Return a Vector with selected objects that match passed class, 
     * or an empty one if there is no one selected.
     * <p>
     * To select all, simply pass <code>Component</code>
     */
    protected ArrayList<Component> getSelected( Class clazz )
    {
        ArrayList<Component> list = getAll( clazz );
        
        for( int n = 0; n < list.size(); n++ )
        {
            Object comp = list.get( n );
            
            if( ! (comp instanceof Selectable) ||
                ! ((Selectable) comp).isSelected() )
            {
                list.remove( n-- );
            }
        }
        
        return list;
    }

    /**
     * Get all objects in dekstop that match passed class,
     * or an empty <code>Vector</code> one if there is no one selected.
     * <p>
     * To select objects of all classes, simply pass <code>Component</code>
     */
    protected ArrayList<Component> getAll( Class clazz )
    {
        ArrayList<Component> list  = new ArrayList<Component>();
        Component[]          aComp = getComponents();
        
        for( int n = 0; n < aComp.length; n++ )
        {
            if( isKindOf( aComp[n], clazz ) )
                list.add( aComp[n] );
        }
        
        return list;
    }
    
    /**
     * Selects objects in desktop that match passed class.
     * <p>
     * Clazz must be implement interface <code>Selectable</code>
     *  
     * @param clazz The Class
     */
    protected void selectAll( Class clazz )
    {
        List list = getAll( clazz );
        
        for( int n = 0; n < list.size(); n++ )
        {
            Object comp = list.get( n );
            
            if( comp instanceof Selectable )
                ((Selectable) comp).setSelected( true );
        }
    }
    
    /**
     * Deselects objects in desktop that match passed class.
     * <p>
     * Clazz must be implement interface <code>Selectable</code> 
     * @param clazz The Class
     */
    protected void deSelectAll( Class clazz )
    {
        List v = getAll( clazz );
        
        for( int n = 0; n < v.size(); n++ )
        {
            Object comp = v.get( n );
            
            if( comp instanceof Selectable )
                ((Selectable) comp).setSelected( false );
        }
    }
    
    // Esto funciona como el operador instanceof, pero la clase es una variable (argumento)
    private boolean isKindOf( Object obj, Class clazz ) 
    {
        // Clase del objeto q se desa comprobar
        Class c = obj.getClass();

        // Miramos si es de esa clase o de alguna de las superclases
        while( c != null ) 
        {
            if ( c == clazz ) 
                return true;

            c = c.getSuperclass();
        }

        return false;
    }
    
    // Manejadora de los eventos que lanza los objetos DesktopComponent.
    // La hago como una clase para ahorrar memoria (para no crear una instancia 
    // por cada componente q se añade)
    private final class TheItemChangeEventListener implements WorkAreaItemChangeEventListener
    {
        public void deskComponentChanged( WorkAreaItemChangeEvent evt )
        {
            WorkAreaItem source = (WorkAreaItem) evt.getSource();
            
            switch( evt.getType() )
            {
                case WorkAreaItemChangeEvent.SELECTION_REQUEST:
                    selectionRequested( source );
                    break;
                
                case WorkAreaItemChangeEvent.SELECTION_INCR_REQUEST:
                    selectionIncrementalRequested( source );
                    break;
                
                case WorkAreaItemChangeEvent.DESELECTION_INCR_REQUEST:
                    deselectionIncrementalRequested( source );
                    break;
                    
                case WorkAreaItemChangeEvent.MOVED_INFORM:
                    movedInformed( source );
                    break;
            }
        }
    }
}