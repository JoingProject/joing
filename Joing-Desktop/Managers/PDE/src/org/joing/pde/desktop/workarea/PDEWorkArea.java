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
import java.awt.event.MouseEvent;
import javax.swing.JDesktopPane;
import javax.swing.event.MouseInputAdapter;
import org.joing.common.desktopAPI.DeskComponent;
import org.joing.common.desktopAPI.DesktopManagerFactory;
import org.joing.common.desktopAPI.Selectable;
import org.joing.common.desktopAPI.deskwidget.deskLauncher.DeskLauncher;
import org.joing.common.desktopAPI.pane.DeskWindow;
import org.joing.common.desktopAPI.workarea.Wallpaper;
import org.joing.common.desktopAPI.workarea.WorkArea;
import org.joing.common.desktopAPI.workarea.WorkAreaListener;
import org.joing.pde.desktop.container.PDECanvas;
import org.joing.pde.desktop.container.PDEDialog;
import org.joing.pde.desktop.container.PDEFrame;
import org.joing.pde.desktop.deskwidget.deskLauncher.PDEDeskLauncher;
import org.joing.pde.desktop.deskwidget.desklet.PDEDesklet;
import org.joing.pde.ColorSchema;
import org.joing.pde.swing.EventListenerList;

/**
 * This class contains internal operativity for the Desk.
 * 
 * It is not public because all public interface has to be accesed using Desk
 * @author Francisco Morero Peyrona
 */
public class PDEWorkArea extends JDesktopPane implements WorkArea
{
    private static final String DONT_USE_ME = "Do not use me";
    
    private static final Integer LAYER_WALLPAPER     = new Integer(-10 );
    private static final Integer LAYER_CANVAS        = new Integer(  0 );
    private static final Integer LAYER_DESKLET       = new Integer( 10 );
    private static final Integer LAYER_DESK_LAUNCHER = new Integer( 20 );
    private static final Integer LAYER_APPLICATION   = new Integer( 30 );
    private static final Integer LAYER_DIALOG        = new Integer( 40 );
    
    private Wallpaper wallpaper;
    
    private Dimension gridDimension = new Dimension( 16,16 );  // Grid size
    private boolean   bSnapToGrid   = true;
    
    private EventListenerList listenerList;
    
    private DeskLauncherListener deskLauncherListener;    // Only one instance for all launchers to save memory
    
    //------------------------------------------------------------------------//
    
    public PDEWorkArea()
    {
        super();
        listenerList         = new EventListenerList();
        deskLauncherListener = new DeskLauncherListener();
        
        initGUI();
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
    // WorkAreaPopupMenu issues
    //------------------------------------------------------------------------//
    
    /**
     * 
     * @param p Where mouse was clicked
     */
    protected void showPopupMenu( Point p )
    {
        // Has to be created every time because some items can change from ivocation to invocation.
        // And in this way, we also save memory (it exists in memory only while needed).
        WorkAreaPopupMenu popup = new WorkAreaPopupMenu( this );
                  popup.show( this, p.x, p.y );
    }
    
    //------------------------------------------------------------------------//
    // Cut, copy & paste components
    //------------------------------------------------------------------------//

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
        DesktopManagerFactory.getDM().getRuntime().showMessage( "Option not yet implemented" );
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
        DesktopManagerFactory.getDM().getRuntime().showMessage( "Option not yet implemented" );
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
        DesktopManagerFactory.getDM().getRuntime().showMessage( "Option not yet implemented" );
    }

    /**
     * Remove from desktop those components in clipboard that match passed class.
     * <p>
     * To remove all components in clipboard, simply pass <code>Component.class</code>
     * 
     * @param clazz Desired class
     */
//    public void removeSelectedComponents( Class clazz )
//    {
//        List vSelected = getSelected( clazz, true );
//        
//        if( vSelected.size() > 0 )
//        {
//            while( vSelected.size() > 0 )
//                remove( (Component) vSelected.get( 0 ) );
//            
//            repaint();  // Without repaint() does not work: dont't touch
//        }
//    }
    
//    private List<Component> getSelected( Class clazz, boolean bSelected )
//    {
//        List<Component> in  = getOfType( clazz );
//        List<Component> out = new ArrayList<Component>( in.size() );
//        
//        for( Component comp : in )
//        {
//            if( comp instanceof Selectable && ((Selectable) comp).isSelected() != bSelected )
//                out.add( comp );
//        }
//        
//        return out;
//    }
//    
//    /**
//     * Selects objects in desktop that match passed class.
//     * <p>
//     * Clazz must be implement interface <code>Selectable</code>
//     *  
//     * @param clazz The Class
//     */
//    private void setSelected( Class clazz, boolean bSelected )
//    {
//        List<Component> list = getOfType( clazz );
//        
//        for( Component comp : list )
//        {
//            if( comp instanceof Selectable )
//                ((Selectable) comp).setSelected( bSelected );
//        }
//    }
    
    //------------------------------------------------------------------------//
    
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
                {   // Si llega aqui es q se ha hecho clic en el desktop, ya q si se hace clic en un 
                    // componente contenido en el desktop, el desktop no llega a recibir el evento.
                    Component[] aComp = getComponents();

                    for( int n = 0; n < aComp.length; n++ )
                        if( aComp[n] instanceof Selectable )
                            ((Selectable) aComp[n]).setSelected( false );
                }
            }
        }  );
    }
    
    //------------------------------------------------------------------------//
    // WorkArea interface implementation
    //------------------------------------------------------------------------//
    
    // Just to avoid accidental use of them  ---------------------------------------------------------
    public Component add( Component c )             { throw new IllegalAccessError(DONT_USE_ME); }
    public Component add( Component c, int n )      { throw new IllegalAccessError(DONT_USE_ME); }
    public Component add( String s, Component c )   { throw new IllegalAccessError(DONT_USE_ME); }
    public void add( Component c, Object o )        { throw new IllegalAccessError(DONT_USE_ME); }
    public void add( Component c, Object o, int n ) { throw new IllegalAccessError(DONT_USE_ME); }
    //------------------------------------------------------------------------------------------------
    
    public void add( DeskComponent dc )
    {
        Component component = (Component) dc;
        
        if(      component instanceof PDEDeskLauncher )  addDeskLauncher( (PDEDeskLauncher) dc );
        else if( component instanceof PDEDesklet      )  addDesklet( (PDEDesklet) component );
        else if( component instanceof PDECanvas       )  super.add( component, LAYER_CANVAS );
        else if( component instanceof PDEDialog       )  super.add( component, LAYER_DIALOG );
        else if( component instanceof PDEFrame        )  addFrame( (PDEFrame) component, true );
        else                                             super.add( component, LAYER_APPLICATION );
        
        moveToFront( (Component) dc );
        fireComponentAdded( dc );
    }
    
    public void add( DeskWindow dc, boolean bAutoArrange )
    {
        addFrame( (PDEFrame) dc, bAutoArrange );
    }
    
    // super is in charge of detecting when the JinternalFrame is closed, so it
    // can remove the frame from the container. 
    // As this method overrides the parent one, this one does not need to 
    // "listen" to the frame events.
    public void remove( DeskComponent dc )
    {
        remove( (Component) dc );
    }
    
    /**
     * This method is needed because it is used by JDesktopPane among others
     * <p>
     * Redefined to invoke fireComponentRemoved(...)
     * @param c Component to be removed
     */
    public void remove( Component c )               
    { 
        super.remove( c );
        fireComponentRemoved( (DeskComponent) c );
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
        listenerList.remove( wal );
    }
    
    public void close()
    {
        // TODO: hacerlo
    }
    
    //------------------------------------------------------------------------//
    
    protected void fireComponentAdded( DeskComponent dc )
    {
        WorkAreaListener[] al = listenerList.getListeners( WorkAreaListener.class );
        
        // Process the listeners last to first, notifying
        for( int n = al.length - 1; n >= 0; n-- )
            al[n].componentAdded( dc );
    }
    
    protected void fireComponentRemoved( DeskComponent dc )
    {
         WorkAreaListener[] al = listenerList.getListeners( WorkAreaListener.class );
        
        // Process the listeners last to first, notifying
        for( int n = al.length - 1; n >= 0; n-- )
            al[n].componentRemoved( dc );
    }
    
    protected void fireWallpaperChanged( Wallpaper wpNew )
    {
         WorkAreaListener[] al = listenerList.getListeners( WorkAreaListener.class );
        
        // Process the listeners last to first, notifying
        for( int n = al.length - 1; n >= 0; n-- )
            al[n].wallpaperChanged( wpNew );
    }
    
    //------------------------------------------------------------------------//
    
    private void addDesklet( PDEDesklet desklet )
    {
        // TODO: comprobar que no es menor que el tamaño mínimo ni mayor que el máximo
        
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
        
        super.add( desklet, LAYER_DESKLET );
    }
    
    private void addDeskLauncher( PDEDeskLauncher dl )
    {
        dl.addLauncherListener( deskLauncherListener );
        super.add( dl, LAYER_DESK_LAUNCHER );
    }
    
    private void addFrame( PDEFrame frame, boolean bAutoArrange )
    {
        if( bAutoArrange )
            frame.pack();
        
        // Ensures that frame is not bigger than WorkArea
        // (because the same user can run Joing in many different-size devices)
        Insets    insets = getInsets();
        Dimension dim    = getSize();
                  dim.width  -= (insets.left + insets.right);
                  dim.height -= (insets.top  + insets.bottom);
                  
        if( frame.getWidth() > dim.width )
            frame.setSize( dim.width, frame.getHeight() );
            
        if( frame.getHeight() > dim.height )
            frame.setSize( frame.getWidth(), dim.height );
        
        // Add to container
        super.add( frame, LAYER_APPLICATION );
        
        if( bAutoArrange )
        {
            frame.setVisible( true );  // 1st, must be visble, because this is the way to have a parent
            frame.center();
            frame.setSelected( true );
            frame.toFront();
        }
    }
    
    //------------------------------------------------------------------------//
    // INNER CLASS: PDEDeskLauncher listener
    //------------------------------------------------------------------------//
    
    private final class DeskLauncherListener 
            implements org.joing.common.desktopAPI.deskwidget.deskLauncher.DeskLauncherListener    
    {
        public void selection( DeskLauncher dl )
        {
            if( dl.isSelected() )  // If the event refers to a selected launcher that becomes to unselected, we are not interested
            {
                Component[] aComp = getComponents();

                for( int n = 0; n < aComp.length; n++ )
                {
                    if( aComp[n] instanceof PDEDeskLauncher )
                        ((PDEDeskLauncher) aComp[n]).setSelected( aComp[n] == dl );
                }
            }
        }
        
        public void selectionIncremental( DeskLauncher dl )
        {
            // Nothing to do
        }
        
        public void launched( DeskLauncher dl )
        {
            // Nothing to do
        }
    }
}