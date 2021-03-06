/*
 * Copyright (C) 2007, 2008 Join'g Team Members. All Rights Reserved.
 * Join'g Team Members are listed at project's home page. By the time of 
 * writting this at: https://joing.dev.java.net/servlets/ProjectMemberList.
 *
 * This file is part of Join'g project: www.joing.org
 *
 * GNU Classpath is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the free
 * Software Foundation; either version 3, or (at your option) any later version.
 * 
 * GNU Classpath is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * GNU Classpath; see the file COPYING.  If not, write to the Free Software 
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */
package org.joing.pde.desktop.workarea;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import javax.swing.JDesktopPane;
import javax.swing.event.MouseInputAdapter;
import org.joing.kernel.api.desktop.DeskComponent;
import org.joing.kernel.api.desktop.Selectable;
import org.joing.kernel.api.desktop.deskwidget.deskLauncher.DeskLauncher;
import org.joing.kernel.api.desktop.pane.DeskWindow;
import org.joing.kernel.api.desktop.workarea.Wallpaper;
import org.joing.kernel.api.desktop.workarea.WorkArea;
import org.joing.kernel.api.desktop.workarea.WorkAreaListener;
import org.joing.pde.desktop.container.PDECanvas;
import org.joing.pde.desktop.container.PDEDialog;
import org.joing.pde.desktop.container.PDEFrame;
import org.joing.pde.desktop.deskwidget.deskLauncher.PDEDeskLauncher;
import org.joing.pde.desktop.deskwidget.desklet.PDEDesklet;
import org.joing.pde.media.PDEColorSchema;
import org.joing.pde.desktop.container.PDEWindow;
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
    
    private static final Integer LAYER_DIALOG    = JDesktopPane.MODAL_LAYER;
    private static final Integer LAYER_FRAME     = LAYER_DIALOG - 10;
    private static final Integer LAYER_LAUNCHER  = LAYER_DIALOG - 20;
    private static final Integer LAYER_CANVAS    = LAYER_DIALOG - 30;
    private static final Integer LAYER_WALLPAPER = LAYER_DIALOG - 40;
    
    private PDEWallpaperComponent pwc;
    
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
    // Grid issues: PDE extra-functionality (not part of WorkArea interface)
    //------------------------------------------------------------------------//
    
    public boolean isSnapToGrid()
    {
        return this.bSnapToGrid;
    }

    public void setSnapToGrid( boolean snapToGrid )
    {
        this.bSnapToGrid = snapToGrid;
    }
    
    /**
     * If needed, moves passed component to be aligned to grid.
     * 
     * @param comp
     */
    public void snapToGrid( Component comp )
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
    
    //------------------------------------------------------------------------//
    // PDEWorkAreaPopupMenu issues
    //------------------------------------------------------------------------//
    
    /**
     * 
     * @param p Where mouse was clicked
     */
    protected void showPopupMenu( Point p )
    {
        // Has to be created every time because some items can change from invocation to invocation.
        // And in this way, we also save memory (it exists in memory only while needed).
        PDEWorkAreaPopupMenu popup = new PDEWorkAreaPopupMenu( this, p );
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
        org.joing.kernel.jvmm.RuntimeFactory.getPlatform().getDesktopManager().getRuntime().showMessageDialog( null, "Option not yet implemented" );
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
        org.joing.kernel.jvmm.RuntimeFactory.getPlatform().getDesktopManager().getRuntime().showMessageDialog( null, "Option not yet implemented" );
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

                }
            }
            
            root.setCursor( Cursor.getPredefinedCursor( Cursor.DEFAULT_CURSOR) );
        }*/
        org.joing.kernel.jvmm.RuntimeFactory.getPlatform().getDesktopManager().getRuntime().showMessageDialog( null, "Option not yet implemented" );
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
    
    private void initGUI()
    {
        setLayout( null );
        setOpaque( true );
        setBackground( PDEColorSchema.getInstance().getDesktopBackground() );
        
        // Add listener for mouse events to: 
        //  · Get the focus
        //  · deselectAll()
        //  · Invoke showPopupMenu(...)
        addMouseListener( new MouseInputAdapter()
        {
            public void mousePressed( MouseEvent me )
            {
                if( ! PDEWorkArea.this.isFocusOwner() )
                    PDEWorkArea.this.requestFocusInWindow();
                
                if( me.isPopupTrigger() )
                {
                    showPopupMenu( me.getPoint() );
                }
                else
                {   // Si llega aqui es q se ha hecho clic en el desktop, ya q si se hace clic en un 
                    // componente contenido en el desktop, el desktop no llega a recibir el evento.
                    Component[] aComp = getComponents();
                    
                    for( int n = 0; n < aComp.length; n++ )
                        if( aComp[n] instanceof Selectable )
                            ((Selectable) aComp[n]).setSelected( false );
                }
            }
            
            public void mouseReleased( MouseEvent me )   // Needed for Windows
            {
                if( me.isPopupTrigger() )
                    showPopupMenu( me.getPoint() );
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
    //FIXME: quiatrlo --> public void add( Component c, Object o )        { throw new IllegalAccessError(DONT_USE_ME); } // This is the one used by JOptionPane
    public void add( Component c, Object o, int n ) { throw new IllegalAccessError(DONT_USE_ME); }
    //------------------------------------------------------------------------------------------------
    
    public void add( DeskComponent dc )
    {
        Component component = (Component) dc;
        
        if( component instanceof PDEDialog )  
        {   // As dialogs are modal, fireComponentAdded() must be launched before 
            // addign dialog to workarea. And moveToFrom() has no sense.
            addDialog( (PDEDialog) component, true );
        }
        else
        {
            if(      component instanceof PDEDeskLauncher )  addDeskLauncher( (PDEDeskLauncher) dc );
            else if( component instanceof PDEDesklet      )  addDesklet( (PDEDesklet) component );
            else if( component instanceof PDECanvas       )  PDEWorkArea.super.add( component, LAYER_CANVAS );
            else if( component instanceof PDEFrame        )  addFrame( (PDEFrame) component, true );
            else                                             PDEWorkArea.super.add( component, LAYER_FRAME  );
            
            fireComponentAdded( dc );
        }
    }
    
    public void add( DeskWindow window )
    {
        add( window, true );
    }
    
    public void add( DeskWindow window, boolean bAutoArrange )
    {
        if( window instanceof PDEDialog )
            addDialog( (PDEDialog) window, bAutoArrange );
        else
            addFrame( (PDEFrame) window, bAutoArrange );
        
        fireComponentAdded( window );
    }
    
    // super is in charge of detecting when the JinternalFrame is closed, so it
    // can remove the dialog from the container. 
    // As this method overrides the parent one, this one does not need to 
    // "listen" to the dialog events.
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
    
    public void moveToFront( DeskComponent dc )
    {
        moveToFront( (Component) dc );
    }

    public void moveToBack( DeskComponent dc )
    {
        moveToBack( (Component) dc );
    }
    
    public Wallpaper getWallpaper()
    {
        return ((pwc != null) ? pwc.getWallpaper() : null);
    }

    public void setWallpaper( Wallpaper wp )
    {
        if( pwc != null && pwc.getWallpaper() == wp )
            return;   // Trying to set the same wallpaper again
        
        if( wp == null )
        {
            if( pwc != null )
                remove( pwc );
            
            pwc = null;
        }
        else
        {
            if( pwc == null )
            {
                pwc = new PDEWallpaperComponent( wp );
                pwc.setLocation( 0,0 );
                pwc.setSize( getSize() );
                super.add( pwc, LAYER_WALLPAPER );
                
                // To re-center the image when WorkArea is resized
                addComponentListener( new ComponentListener() 
                {
                    public void componentResized( ComponentEvent ce )
                    {
                        PDEWorkArea.this.pwc.setSize( ce.getComponent().getSize() );
                    }
                    
                    public void componentMoved( ComponentEvent e )  {}
                    public void componentShown( ComponentEvent e )  {}
                    public void componentHidden( ComponentEvent e ) {}
                } );
            }
            else
            {
                pwc.setWallPaper( wp );
            }
        }
        
        fireWallpaperChanged( wp );
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
    
    protected void fireComponentSelected( DeskComponent dcOldSelected, DeskComponent dcNewSelected )
    {
         WorkAreaListener[] al = listenerList.getListeners( WorkAreaListener.class );
        
        // Process the listeners last to first, notifying
        for( int n = al.length - 1; n >= 0; n-- )
            al[n].componentSelected( dcOldSelected, dcNewSelected );
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
        // NEXT: Check desklet bounds to ensure it is visible
        super.add( desklet, LAYER_LAUNCHER );
        validate();
    }
    
    private void addDeskLauncher( PDEDeskLauncher dl )
    {
        dl.addLauncherListener( deskLauncherListener );
        super.add( dl, LAYER_LAUNCHER  );
        validate();
        moveToFront( (Component) dl );
    }
    
    //------------------------------------------------------------------------//
    // FIXME: Estoy usando JDialog enlugar de JInternalframe hasta que solucione 
    //        el problema con LWModal
    // Este método es el que uso cuando PDEDialog hereda de JDialog (en lugar de PDEWindow, que es lo suyo)
    private void addDialog( PDEDialog dialog, boolean bAutoArrange )
    {
        if( bAutoArrange )
        {
            dialog.pack();
            dialog.center();
        }
        
        // As dialogs are modal, fireComponentAdded() must be launched before addign dialog to workarea
        fireComponentAdded( dialog );
        dialog.setVisible( true );
    }
    /* Este método es el que hay que usar cuando PDEDialog hereda de PDEWindow
    private void addDialog( PDEDialog dialog, boolean bAutoArrange )
    {
        if( bAutoArrange )
            dialog.pack();
        
        adjustWindowBounds( dialog );
        
        super.add( dialog, LAYER_DIALOG );
        
        validate();  // Validate before make it visible
        // 1st, must be visble, because this is the way to have a parent
        dialog.setVisible( true );   // DeskFrame interface does not include setVisible(...)
        
        if( bAutoArrange )
        {
            dialog.center();
            dialog.setSelected( true );
            moveToFront( (Component) dialog );
        }
        
        // As dialogs are modal, fireComponentAdded() must be launched before addign dialog to workarea
        fireComponentAdded( dialog );
        dialog.startModal();
    }
    --------------------------------------------------------------------------*/
    
    private void addFrame( final PDEFrame frame, final boolean bAutoArrange )
    {
        if( bAutoArrange )
            frame.pack(); // Pack calls internally to validate()
            
        adjustWindowBounds( frame );
        
        // Add to container
        if( frame.isAlwaysOnTop() )
            super.add( frame, LAYER_FRAME + 1 );
        else
            super.add( frame, LAYER_FRAME );
        
        if( bAutoArrange )
            frame.center();
        
        // DeskFrame interface does not include setVisible(...)
        frame.setVisible( true ); // 1st, must be visble, because this is the way to have a parent
        
        if( bAutoArrange )
        {
            frame.setSelected( true );
            moveToFront( (Component) frame );
        }
    }
    
    // Ensures that passed dialog or frame is not bigger than WorkArea
    // (the same user can run Join'g in many different-size devices)
    private void adjustWindowBounds( PDEWindow window )
    {
        Insets    insets = getInsets();
        Dimension dim    = getSize();
                  dim.width  -= (insets.left + insets.right);
                  dim.height -= (insets.top  + insets.bottom);
                  
        if( window.getWidth() > dim.width )
            window.setSize( dim.width, window.getHeight() );
            
        if( window.getHeight() > dim.height )
            window.setSize( window.getWidth(), dim.height );
    }
    
    //------------------------------------------------------------------------//
    // INNER CLASS: PDEDeskLauncher listener
    //------------------------------------------------------------------------//
    
    private final class DeskLauncherListener 
            implements org.joing.kernel.api.desktop.deskwidget.deskLauncher.DeskLauncherListener    
    {
        public void selection( DeskLauncher dl )
        {
            if( dl.isSelected() )  // If the event refers to a selected launcher that becomes unselected, we are not interested
            {
                DeskComponent oldSel = null;
                DeskComponent newSel = dl;
                
                moveToFront( dl );    // Moves to front inside its layer
                
                Component[] aComp = getComponents();

                for( int n = 0; n < aComp.length; n++ )
                {
                    if( aComp[n] instanceof PDEDeskLauncher )
                    {
                        PDEDeskLauncher pdl = (PDEDeskLauncher) aComp[n];
                        
                        if( pdl.isSelected() )
                            oldSel = pdl;
                        
                        pdl.setSelected( aComp[n] == dl );
                    }
                }
                
                PDEWorkArea.this.fireComponentSelected( oldSel, newSel );
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