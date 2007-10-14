/*
 * PDEDeskLauncher.java
 * 
 * Created on 11-sep-2007, 21:41:50
 * 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.joing.pde.desktop.workarea.desklet.deskLauncher;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.image.FilteredImageSource;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.MenuElement;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.event.MouseInputAdapter;
import org.joing.api.desktop.workarea.desklet.deskLauncher.Launcher;
import org.joing.api.desktop.workarea.desklet.deskLauncher.LauncherEvent;
import org.joing.api.desktop.workarea.desklet.deskLauncher.LauncherEventListener;
import org.joing.pde.desktop.workarea.PDEWorkArea;
import org.joing.pde.desktop.workarea.desklet.PDEDesklet;
import org.joing.pde.runtime.ColorSchema;
import org.joing.pde.runtime.PDERuntime;
import org.joing.pde.swing.ImageHighlightFilter;
import org.joing.pde.swing.JMultiLineEditableLabel;
import org.joing.pde.swing.JRoundPanel;

/**
 *
 * @author Francisco Morero Peyrona
 */
public class PDEDeskLauncher extends PDEDesklet implements Launcher
{
    // Launcher interface variables
    private Image   image;
    private boolean bSelected;
    
    // Swing variables
    private JRoundPanel             pnlAll;
    private IconComponent           icon;
    private JMultiLineEditableLabel text;
    
    //------------------------------------------------------------------------//
    
    public PDEDeskLauncher()
    {
        this( "No Name" );
    }
    
    public PDEDeskLauncher( String sName )
    {
        this( sName, null );
    }
    
    public PDEDeskLauncher( String sName, Image image )
    {
        this( sName, image, null );
    }
    
    public PDEDeskLauncher( String sName, Image image, String sDescription )
    {
        if( image == null )
            image = new ImageIcon( getClass().getResource( "images/launcher.png" ) ).getImage();
        
        initGUI();
        
        setName( sName );
        setImage( image );
        setDescription( sDescription );
    }
    
    //------------------------------------------------------------------------//
    // Launcher interface implementation
    // Following methos are already in JDesktop: getName(), getLocation(), setLocation(...)
    
    public void setName( String sName )
    {
        super.setName( sName );
        text.setText( sName );
    }
    
    public Image getImage()
    {
        return image;
    }
    
    public void setImage( Image image )
    {
        this.image = image;
        icon.setIcon( new ImageIcon( image ) );
    }
    
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
    public void setSelected( boolean bNewStatus )
    {
        if( bNewStatus != isSelected() )
        {
            text.setSelected( bNewStatus );
            bSelected = bNewStatus;
            fireSelectedEvent( new LauncherEvent( this, bNewStatus ) );
        }
    }
    
    /**
     * Has to be redefined by every subclass tha can launch the Launcher.
     */
    public boolean launch()
    {
        return false;
    }
    
    public void addLauncherListener( LauncherEventListener ll )
    {
        listenerList.add( LauncherEventListener.class, ll );
    }
    
    public void removeLauncherListener( LauncherEventListener ll ) 
    {
        listenerList.remove( LauncherEventListener.class, ll );
    }
    
    public void fireSelectedEvent( LauncherEvent le )
    {
        Object[] listeners = listenerList.getListenerList();
        
        // Process the listeners last to first, notifying
        for( int n = listeners.length - 2; n >= 0; n -= 2 )
        {
            if( listeners[n] == LauncherEventListener.class )
                ((LauncherEventListener) listeners[n+1]).selectedEvent( le );
        }
    }
    
    public void fireSelectionIncrementalEvent( LauncherEvent le )
    {
        Object[] listeners = listenerList.getListenerList();
        
        // Process the listeners last to first, notifying
        for( int n = listeners.length - 2; n >= 0; n -= 2 )
        {
            if( listeners[n] == LauncherEventListener.class )
                ((LauncherEventListener) listeners[n+1]).selectionIncrementalEvent( le );
        }
    }
    
    public void fireLaunchedEvent( LauncherEvent le )
    {
        Object[] listeners = listenerList.getListenerList();
        
        // Process the listeners last to first, notifying
        for( int n = listeners.length - 2; n >= 0; n -= 2 )
        {
            if( listeners[n] == LauncherEventListener.class )
                ((LauncherEventListener) listeners[n+1]).launchedEvent( le );
        }
    }
    
    //------------------------------------------------------------------------//
    
    public Launcher clone()
    { // TODO: hay que revisar todos los clone para poder hacer los cut, copy & paste
        PDEDeskLauncher clone = new PDEDeskLauncher();
                        clone.setName( "Copy of "+ getName() );
                        clone.setDescription( getDescription() );
                        clone.setImage( getImage() );
                 
        return clone;
    }
    
    /**
     * When selection is incremental, 2 events are fire: the 1st one
     * indicating that the Launcher was selected and the 2nd one
     * indicating that it is an incremental selection
     */
    // This method is called from this.GlassPaneMouseListner::mousePressed(...)
    protected void setSelected( final boolean bNewStatus, final boolean bIncremental )
    {
        if( bNewStatus != isSelected() )
        {
            // FIXME: creo que en lugar de buscar el padre e invocar sus métodos,
            //       sería más elegante (y más acorde con Java) lanzar simplemente
            //       el evento y que el padre lo escuche y reaccione
            if( PDEDeskLauncher.this.getParent() != null && 
                PDEDeskLauncher.this.getParent() instanceof PDEWorkArea )
            {
                PDEWorkArea wa = (PDEWorkArea) PDEDeskLauncher.this.getParent();

                if( ! bIncremental )
                    wa.setSelected( Component.class, false );  // Deselects all
                else
                    wa.moveToFront( PDEDeskLauncher.this );
            }

            setSelected( bNewStatus );
            
            // When selection is incremental, 2 events are fired: the 1st one
            // indicating that the Launcher was selected and the 2nd one 
            // indicating that it is an incremental selection
            if( bIncremental )
                fireSelectionIncrementalEvent( new LauncherEvent( this, bNewStatus ) );
        }
    }
    
    //------------------------------------------------------------------------//
    // ACTIONS
    // TODO: Creo que estas actions no pertencen al DeskLauncher, 
    //       sino a la WorkArea: hay que pensar en ello.
    //       Estas son lanzadas desde el popup y sólo deberían llamar al container (WorkArea)
    public void delete()
    {
        if( PDERuntime.getRuntime().confirmDialog( "Delete launcher", 
                                                   "Are you sure you want to delete it?\n"+
                                                   "(deleted objects can not be recovered)" ) )
        {
            PDEWorkArea workArea = (PDEWorkArea) SwingUtilities.getAncestorOfClass( PDEWorkArea.class, this );
                        workArea.remove( this );
                
            // There is not a fireLauncherDeleted() because it can be resolved   
            // by adding a listner to the WorkArea
        }
    }
    
    public void toTrashcan()
    {
        if( PDERuntime.getRuntime().confirmDialog( "Send launcher to trashcan", 
                                                   "Are you sure you want to send it to trashcan?" ) )
        {
            
            PDEWorkArea workArea = (PDEWorkArea) SwingUtilities.getAncestorOfClass( PDEWorkArea.class, this );
                        workArea.remove( this );
                        
            // TODO: mandarlo a la papelera e implementar el fire
            // fireLauncherToTrashcan( this );
            PDERuntime.getRuntime().showMessage( "Option not yet implemented" );
        }
    }
    
    public void rename()
    {
        text.edit( true );
        /*PopupToRename p4t = new PopupToRename( text );
        Rectangle     rec = text.getBounds();
        
        p4t.setPreferredSize( new Dimension( rec.width, rec.height ) );
        p4t.show( text, rec.x, rec.y-35 );
        // TODO:  fireLauncherRenamed( this );*/
    }
    
    public void editProperties()
    {
        // Has to be re-defined by subclasses
    }
    
    /**
     * Used to highlight and de-highlight the component.
     * 
     * @param b New status
     */
    protected void setHighlighted( boolean b )
    {
        pnlAll.setOpaque( b );
        icon.setHighlighted( b );
        repaint();
    }
    
    //------------------------------------------------------------------------//
    
    private void initGUI()
    {
        // Sizes must be initialized prior to components (they used these)
        setMinimumSize(   new Dimension( 62, 65 ) );
        setMaximumSize(   new Dimension( 92,182 ) );
        setPreferredSize( new Dimension( 72, 65 ) );

        // Inicializo los componentes
        icon = new IconComponent();
        
        text = new JMultiLineEditableLabel();
        text.setFont( text.getFont().deriveFont( Font.BOLD, 11f ) );
        int nPixelsWidth = SwingUtilities.computeStringWidth( text.getFontMetrics( text.getFont() ), "ABC" ) / 3;   // This line must be after setFont(...)
        text.setColumns( PDEDeskLauncher.this.getPreferredSize().width / nPixelsWidth );
        
        pnlAll = new JRoundPanel();
        pnlAll.setOpaque( false );
        pnlAll.setBackground( ColorSchema.getInstance().getDesktopLauncherBackground() );
        pnlAll.setTransparency( 85 );
        pnlAll.setLayout( new BorderLayout( 0,0 ) );
        pnlAll.setBorder( new EmptyBorder( 3,3,3,3 ) );
        pnlAll.add( icon, BorderLayout.CENTER );
        pnlAll.add( text, BorderLayout.SOUTH  );
        
        // Initializes this
        setComponentPopupMenu( new ThisPopupMenu() );
        setBounds( 0,
                   0,
                   (int) getPreferredSize().getWidth(), 
                   (int) getPreferredSize().getHeight() );
        add( pnlAll, BorderLayout.CENTER );
        getGlassPane().addMouseListener( new GlassPaneMouseListener() );
    }

    //------------------------------------------------------------------------//
    // INNER CLASSES
    //------------------------------------------------------------------------//
    
    private final class IconComponent extends JLabel
    {
        private IconComponent()
        {
            setHorizontalAlignment( JLabel.CENTER );
            setBorder( new EmptyBorder( 0, (getPreferredSize().width - 48) / 2,
                                        0, (getPreferredSize().width - 48) / 2  )  );  // Border to center the icon
        }

        private void setHighlighted( boolean b )
        {
            Image image = PDEDeskLauncher.this.getImage();
            
            if( image != null )
            {
                if( b )
                {
                    ImageHighlightFilter ihf = new ImageHighlightFilter( true, 32 );
                    Image imgHigh = createImage( new FilteredImageSource( image.getSource(), ihf ) );

                    setIcon( new ImageIcon( imgHigh ) );
                }
                else
                {
                    setIcon( new ImageIcon( image ) );
                }
            }
        }
    }
    
    //------------------------------------------------------------------------//
    // FIXME: Esta clase: ¿tiene que heredar de la misma que tiene el padre (=> hacer la del padre protected)?
    private final class GlassPaneMouseListener extends MouseInputAdapter
    {
        public void mouseClicked( MouseEvent me )
        {
            if( me.getClickCount() == 2 )
                PDEDeskLauncher.this.launch();
        }

        // Pone el icono en highlighted
        public void mouseEntered( MouseEvent me )
        {
            PDEDeskLauncher.this.setHighlighted( true );
        }
        
        // Quita el icono de highlighted
        public void mouseExited( MouseEvent me )
        {
            PDEDeskLauncher.this.setHighlighted( false );
        }
        
        public void mousePressed( MouseEvent me )
        {
            boolean bCtrlPressed = (me.getModifiersEx() & MouseEvent.CTRL_DOWN_MASK) != 0;
            
            PDEDeskLauncher.this.setSelected( true, bCtrlPressed );
        }        
    }
    
    //------------------------------------------------------------------------//
    // El popup del DesktopLauncher
    
    private class ThisPopupMenu extends JPopupMenu implements ActionListener
    {
        private ThisPopupMenu()
        {
            add( createMenuItem( "Open"       , "launcher"   , "OPEN"       ) );
            addSeparator();
            add( createMenuItem( "To trashcan", "to_trashcan", "TRASHCAN"   ) );
            add( createMenuItem( "Delete"     , "delete"     , "DELETE"     ) );
            addSeparator();
            add( createMenuItem( "Rename"     , null         , "RENAME"     ) );
            addSeparator();
            add( createMenuItem( "Properties" , "properties" , "PROPERTIES" ) );
        }
        
        private JMenuItem createMenuItem( String sText, String sIconName, String sCommand )
        {
            JMenuItem item = new JMenuItem( sText );
                      item.setActionCommand( sCommand );
                      item.addActionListener( this );
                      
            if( sIconName != null )
                item.setIcon( PDERuntime.getRuntime().getIcon( this, "images/"+ sIconName +".png", 16, 16 )  );
            
            return item;
        }
        
        public void show( Component invoker, int x, int y )
        {
            MenuElement[] me = getSubElements();
            
            // TODO: ((JMenuItem) me[0]).setEnabled( PDEDeskLauncher.this.getCommand() != null );
            
            super.show( invoker, x, y );
        }
        
        public void actionPerformed( ActionEvent ae )
        {
            String sCommand = ((JMenuItem) ae.getSource()).getActionCommand();
            
            if(      sCommand.equals( "OPEN"       ) )  PDEDeskLauncher.this.launch();
            else if( sCommand.equals( "DELETE"     ) )  PDEDeskLauncher.this.delete();
            else if( sCommand.equals( "TRASHCAN"   ) )  PDEDeskLauncher.this.toTrashcan();
            else if( sCommand.equals( "RENAME"     ) )  PDEDeskLauncher.this.rename();
            else if( sCommand.equals( "PROPERTIES" ) )  PDEDeskLauncher.this.editProperties();
        }
    }
}