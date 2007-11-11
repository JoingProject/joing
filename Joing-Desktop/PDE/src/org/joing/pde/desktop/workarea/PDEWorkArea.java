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
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JDesktopPane;
import javax.swing.event.MouseInputAdapter;
import org.joing.api.desktop.Selectable;
import org.joing.api.desktop.workarea.Wallpaper;
import org.joing.api.desktop.workarea.WorkArea;
import org.joing.api.desktop.workarea.WorkAreaListener;
import org.joing.pde.desktop.container.PDECanvas;
import org.joing.pde.desktop.container.PDEDialog;
import org.joing.pde.desktop.container.PDEFrame;
import org.joing.pde.desktop.deskwidget.PDEDeskWidget;
import org.joing.pde.desktop.deskwidget.deskLauncher.PDEDeskLauncher;
import org.joing.pde.desktop.deskwidget.desklet.PDEDesklet;
import org.joing.pde.runtime.ColorSchema;
import org.joing.pde.runtime.PDERuntime;

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
    private static final Integer LAYER_WALLPAPER     = new Integer(-10 );
    private static final Integer LAYER_CANVAS        = new Integer(  0 );
    private static final Integer LAYER_DESKLET       = new Integer( 10 );
    private static final Integer LAYER_DESK_LAUNCHER = new Integer( 20 );
    private static final Integer LAYER_APPLICATION   = new Integer( 30 );
    private static final Integer LAYER_DIALOG        = new Integer( 40 );
    
    private Wallpaper wallpaper;
    
    private Dimension gridDimension = new Dimension( 16,16 );  // Grid size
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
    { // FIXME: cuando tenga resuelto el asunto de las Dialog, puedo prescindir
      //        de la JOptionPane y entonces puedo hacer con este add como con los demás (throw ...)
        super.add( component, constraints );
    }
    
    public void load( InputStream in ) throws IOException, ClassNotFoundException
    {
        // TODO: hacerlo
    }
    
    public void save( OutputStream out )
    {
        // TODO: guardar las properties de la WorkArea
        
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
        // Has to be created every time because some items can change from ivocation to invocation.
        // And in this way, we also save memory (it exists in memory only while needed).
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
    {// TODO: hacerlo
        /*List vSelected = getSelected( clazz );
        
        if( vSelected.size() > 0 )
        {
            Client.getClient().getClipBoard().clear();
            Client.getClient().getClipBoard().add( vSelected );
            
            removeSelectedComponents( clazz );
        }*/
        PDERuntime.getRuntime().showMessage( "Option not yet implemented" );
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
        PDERuntime.getRuntime().showMessage( "Option not yet implemented" );
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
        PDERuntime.getRuntime().showMessage( "Option not yet implemented" );
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
        List vSelected = getSelected( clazz, true );
        
        if( vSelected.size() > 0 )
        {
            while( vSelected.size() > 0 )
                remove( (Component) vSelected.get( 0 ) );
            
            repaint();  // Without repaint() does not work: dont't touch
        }
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
       /* Dimension dim = getSize();
        
        if( dim.width > 0 && dim.height > 0 )       // WA is visible
        {
            Rectangle bounds = component.getBounds();

            if( bounds.x < 0 || bounds.y < 0 || bounds.width <= 0 || bounds.height <= 0 )
                throw new IndexOutOfBoundsException( "Component bounds are invalid" );

            // Check size (has to be done before position)
            if( bounds.width > dim.width )
                component.setSize( new Dimension( dim.width, bounds.height ) );

            if( bounds.height > dim.height )
                component.setSize( new Dimension( bounds.width, dim.height ) );

            // Check position
            if( (bounds.x + bounds.width) > dim.width )
                component.setLocation( 0, bounds.y );      // NEXT: esto es mejorable

            if( (bounds.y + bounds.height) > dim.height )
                component.setLocation( bounds.x, 0 );      // NEXT: esto es mejorable
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
        else if( component instanceof PDEDesklet      )  addDesklet( (PDEDesklet) component );
        else if( component instanceof PDECanvas       )  super.add( component, LAYER_CANVAS );
        else if( component instanceof PDEDialog       )  super.add( component, LAYER_DIALOG );
        else if( component instanceof PDEFrame        )  addFrame( (PDEFrame) component );
        else                                             super.add( component, LAYER_APPLICATION );
        
        fireComponentAdded( component );
        
        return this;
    }
    
    // super is in charge of detecting when the JinternalFrame is closed, so it
    // can remove the frame from the container. 
    // As this method overrides the parent one, this one does not need to 
    // "listen" to the frame events.
    public void remove( Component component )
    {
        super.remove( component );
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
    
    //------------------------------------------------------------------------//
    
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
    
    //------------------------------------------------------------------------//
    
    private void addDesklet( PDEDesklet desklet )
    {
        // TODO: comprobar que no es menor que el tamaño mínimo ni mayor que el máximo
        
        super.add( desklet, LAYER_DESKLET );
    }
    
    private void addFrame( PDEFrame frame )
    {
        frame.pack();
        
        super.add( frame, LAYER_APPLICATION );
        
        // Ensures that frame is not bigger than WorkArea
        Insets    insets = getInsets();
        Dimension dim    = getSize();
                  dim.width  -= (insets.left + insets.right);
                  dim.height -= (insets.top  + insets.bottom);

        if( frame.getWidth() > dim.width )
            frame.setSize( dim.width, frame.getHeight() );

        if( frame.getHeight() > dim.height )
            frame.setSize( frame.getWidth(), dim.height );
    }
}