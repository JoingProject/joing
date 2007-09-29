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
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JDesktopPane;
import javax.swing.event.MouseInputAdapter;
import org.joing.api.desktop.workarea.Wallpaper;
import org.joing.api.desktop.workarea.WorkArea;
import org.joing.api.desktop.workarea.WorkAreaListener;
import org.joing.impl.desktop.Selectable;
import org.joing.pde.desktop.workarea.container.PDECanvas;
import org.joing.pde.desktop.workarea.container.PDEDialog;
import org.joing.pde.desktop.workarea.container.PDEFrame;
import org.joing.pde.desktop.workarea.desklet.deskApplet.PDEDeskApplet;
import org.joing.pde.desktop.workarea.desklet.deskLauncher.PDEDeskLauncher;
import org.joing.pde.runtime.ColorSchema;

/**
 * This class contains internal operativity for the Desk.
 * 
 * It is not public because all public interface has to be accesed using Desk
 * @author Francisco Morero Peyrona
 */
public class PDEWorkArea 
       extends JDesktopPane 
       implements WorkArea
{
    public static final Integer LAYER_WALLPAPER     = new Integer(-10 );
    public static final Integer LAYER_CANVAS        = new Integer(  0 );
    public static final Integer LAYER_DESK_LAUNCHER = new Integer( 10 );
    public static final Integer LAYER_DESK_APPLET   = new Integer( 20 );
    public static final Integer LAYER_APPLICATION   = new Integer( 30 );
    public static final Integer LAYER_DIALOG        = new Integer( 40 );
    
    private Wallpaper wallpaper;
    
    private Dimension gridDimension = new Dimension( 16,16 );  // Tamaño de la cuadrícula del grid
    private boolean   bSnapToGrid   = true;
    
    //-------------------------------------------------------------------------//
    
    public PDEWorkArea()
    {
        super();
        initGUI();
    }
    
    // Las pongo sólo para no llamarlas por error y q se monte un zapatiesto -------------------------
    public Component add( Component c, int n )      { throw new IllegalAccessError("Do not use me"); }
    public Component add( String s, Component c )   { throw new IllegalAccessError("Do not use me"); }
    public void add( Component c, Object o, int n ) { throw new IllegalAccessError("Do not use me"); }

    // This method has to be kept because it is used by JOptionInternalXXX
    public void add( Component component, Object constraints )
    {
        super.add( component, constraints );
    }
    
    
    public void load( InputStream in ) throws IOException, ClassNotFoundException
    {
        // TODO: hacerlo
        
        XMLDecoder decoder = new XMLDecoder( in );
    }
    
    public void save( OutputStream out )
    {
        // TODO: guardar las properties de la WorkArea
        
        XMLEncoder encoder = new XMLEncoder( out );
        
        List<Component> lstComponents = getOfType( Component.class );
        
        for( Component comp : lstComponents )
        {
            /*if( comp instanceof Externalizable )
                ((Externalizable) comp).writeExternal( oo );*/
        }
    }
    
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
        
    //------------------------------------------------------------------------//
    // PopupMenu issues
    //-------------------------------------------------------------------------//
    
    /**
     * 
     * @param p Where mouse was clicked
     */
    protected void showPopupMenu( Point p )
    {
        // Has to be created every time because some items can change from ivocation to invocation
        PopupMenu popup = new PopupMenu( this );
                  popup.show( this, p.x, p.y );
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
        /*List vSelected = getSelected( clazz );
        
        if( vSelected.size() > 0 )
        {
            while( vSelected.size() > 0 )
                remove( (Component) vSelected.get( 0 ) );
            
            repaint();  // Sin repaint() no funciona: NO tocar
        }*/
    }   
    
    //------------------------------------------------------------------------//
    
    /** 
     * Cheks (and chage if necessary) that passed component is not bigger than
     * the desktop and that it is inside visible area.
     * <p>
     * If its size is zero, it is sat to a minimum size.
     */
    protected void adjustComponentSize( Component component )
    {
        // TODO: ver: GraphicsConfiguration (por ahora asumo que sólo hay 1 monitor)
        //       Si activo este código, no se ve nada en el Desktop
        
        // Check position
        /*if( component.getX() < 0 || component.getY() < 0 )
        {
            Point point = findEmptyLocation( component  );
            component.setLocation( point.x, point.y );
        }
        else if( isShowing() )
        {
            // TODO: comprobar que el comp no se ha salido por abajo y/o por la derecha
            //comp.setLocation( point.x, point.y );
        }
        
        // Check size
        if( component.getWidth() == 0 )
        {
            component.setSize( Math.max( 10, component.getPreferredSize().width    ),
                          Math.max( 10, component.getPreferredSize().height  )  );
        }
        else if( isShowing() )
        {
            component.setSize( Math.min( component.getWidth() , getWidth()    ),
                          Math.max( component.getHeight(), getHeight()  )  );
        }*/
    }
    
    protected Point findEmptyLocation( Component component )
    {
        // TODO: hacerlo mejor
        
        if( component instanceof PDEDeskLauncher )
        {
            return new Point( 0,0 );
        }
        else
        {
            return new Point( 0, 0 );
        }
    }
    
    //------------------------------------------------------------------------//
    
    private void initGUI()
    {        
        setOpaque( true );
        setBackground( ColorSchema.getInstance().getDesktopBackground() );
        
        // Add listener for mouse events to: 
        //  · Get the focus
        //  · To deselectAll()
        //  · To invoke showPopupMenu(...)
        addMouseListener( new MouseInputAdapter()
        {
            public void mousePressed( MouseEvent me )
            {
                if( ! PDEWorkArea.this.isFocusOwner() )
                    PDEWorkArea.this.requestFocusInWindow();
                
                if( me.isPopupTrigger() )
                    showPopupMenu( me.getPoint() );
                else
                    // Si llega aqui es q se ha hecho clic en el desktop, ya q si se hace clic en un 
                    // componente contenido en el desktop, el desktop no llega a recibir el evento.
                    setSelected( Component.class, false );
            }
        }  );
    }
    
    //------------------------------------------------------------------------//
    // WorkArea interface implementation
    //------------------------------------------------------------------------//
    
    public Component add( Component component )
    {
        adjustComponentSize( component );
        
        if(      component instanceof PDEDeskLauncher )  super.add( component, LAYER_DESK_LAUNCHER );
        else if( component instanceof PDEDeskApplet   )  super.add( component, LAYER_DESK_APPLET );
        else if( component instanceof PDECanvas       )  super.add( component, LAYER_CANVAS );
        else if( component instanceof PDEDialog       )  super.add( component, LAYER_DIALOG );
        else if( component instanceof PDEFrame        )  super.add( component, LAYER_APPLICATION );
        else                                             super.add( component, LAYER_APPLICATION );
        
        fireComponentAdded( component );
        
        return this;
    }
    
    public void remove( Component component )
    {
        super.remove( component );
        validate();
        repaint( component.getBounds() );
        
        fireComponentRemoved( component );
    }
    
    public Wallpaper getWallpaper()
    {
        return wallpaper;
    }

    public void setWallpaper( Wallpaper wallpaper )
    {
        // TODO: hacerlo
        fireWallpaperChanged( wallpaper );
    }

    public void addWorkAreaListener( WorkAreaListener wal )
    {
        listenerList.add( WorkAreaListener.class, wal );
    }

    public void removeWorkAreaListener( WorkAreaListener wal )
    {
        listenerList.remove( WorkAreaListener.class, wal );
    }
    
    /**
     * Get all objects in dekstop that match passed class,
     * or an empty <code>Vector</code> one if there is no one selected.
     * <p>
     * To select objects of all classes, simply pass <code>Component</code>
     */
    public List<Component> getOfType( Class clazz )
    {
        ArrayList<Component> list  = new ArrayList<Component>();                
        Component[]          aComp = getComponents();
        
        for( int n = 0; n < aComp.length; n++ )
        {
            if( clazz.isInstance( aComp[n] ) )
                list.add( aComp[n] );
        }
        
        return list;
    }
    
    public List<Component> getSelected( Class clazz, boolean bSelected )
    {
        List<Component> in  = getOfType( clazz );
        List<Component> out = new ArrayList<Component>( in.size() );
        
        for( Component comp : in )
        {
            if( comp instanceof Selectable && ((Selectable) comp).isSelected() != bSelected )
                out.add( comp );
        }
        
        return out;
    }
    
    /**
     * Selects objects in desktop that match passed class.
     * <p>
     * Clazz must be implement interface <code>Selectable</code>
     *  
     * @param clazz The Class
     */
    public void setSelected( Class clazz, boolean bSelected )
    {
        List<Component> list = getOfType( clazz );
        
        for( Component comp : list )
        {
            if( comp instanceof Selectable )
                ((Selectable) comp).setSelected( bSelected );
        }
    }
    
    public void close()
    {
        // TODO: hacerlo
    }
    
    protected void fireComponentAdded( Component l )
    {
        Object[] listeners = listenerList.getListenerList();
        
        // Process the listeners last to first, notifying
        for( int n = listeners.length - 2; n >= 0; n -= 2 )
        {
            if( listeners[n] == WorkAreaListener.class )
                ((WorkAreaListener) listeners[n+1]).componentAdded( l );
        }
    }
    
    protected void fireComponentRemoved( Component l )
    {
        Object[] listeners = listenerList.getListenerList();
        
        // Process the listeners last to first, notifying)
        for( int n = listeners.length - 2; n >= 0; n -= 2 )
        {
            if( listeners[n] == WorkAreaListener.class )
                ((WorkAreaListener) listeners[n+1]).componentRemoved( l );
        }
    }
    
    protected void fireWallpaperChanged( Wallpaper wpNew )
    {
        Object[] listeners = listenerList.getListenerList();
        
        // Process the listeners last to first, notifying
        for( int n = listeners.length - 2; n >= 0; n -= 2 )
        {
            if( listeners[n] == WorkAreaListener.class )
                ((WorkAreaListener) listeners[n+1]).wallpaperChanged( wpNew );
        }
    }
}