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
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.FilteredImageSource;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRootPane;
import javax.swing.JTextField;
import javax.swing.MenuElement;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.event.MouseInputAdapter;
import org.joing.api.desktop.workarea.desklet.deskLauncher.Launcher;
import org.joing.api.desktop.workarea.desklet.deskLauncher.LauncherEvent;
import org.joing.api.desktop.workarea.desklet.deskLauncher.LauncherEventListener;
import org.joing.impl.desktop.workarea.DefaultLauncher;
import org.joing.pde.desktop.workarea.PDEWorkArea;
import org.joing.pde.desktop.workarea.desklet.PDEDesklet;
import org.joing.pde.desktop.workarea.desklet.PDEDesklet;
import org.joing.pde.runtime.ColorSchema;
import org.joing.pde.runtime.PDERuntime;
import org.joing.pde.swing.ImageHighlightFilter;
import org.joing.pde.swing.JRoundLabel;

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
    private   JLabel      lblIcon;
    private   JRoundLabel lblCaption;
    
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
        lblCaption.setText( sName );
    }
    
    public Image getImage()
    {
        return image;
    }
    
    public void setImage( Image image )
    {
        this.image = image;
        lblIcon.setIcon( new ImageIcon( image ) );
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
            lblCaption.setOpaque( bNewStatus );
            // Not needed to change background (can be visible or not, but it is always the same color)
            lblCaption.setForeground( bNewStatus ? ColorSchema.getInstance().getDesktopLauncherForegroundSelected() :
                                                   ColorSchema.getInstance().getDesktopLauncherForegroundUnSelected() );                    
            lblCaption.repaint();
            
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
    {
        Launcher clone = new DefaultLauncher();
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
    protected void setSelected( final boolean bNewStatus, final boolean bIncremental )
    {
        if( bNewStatus != isSelected() )
        {
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
            
            // When selection is incremental, 2 events are fire: the 1st one
            // indicating that the Launcher was selected and the 2nd one 
            // indicating that it is an incremental selection
            if( bIncremental )
                fireSelectionIncrementalEvent( new LauncherEvent( this, bNewStatus ) );
        }
    }
    
    //------------------------------------------------------------------------//
    // ACTIONS
    
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
        }
    }
    
    public void rename()
    {
        PopupToRename p4t = new PopupToRename( lblCaption );
        Rectangle  rec = lblCaption.getBounds();
        
        p4t.setPreferredSize( new Dimension( rec.width, rec.height ) );
        p4t.show( lblCaption, rec.x, rec.y-35 );
        // TODO:  fireLauncherRenamed( this );
    }
    
    public void editProperties()
    {
        // Has to be re-defined by subclasses
    }
    
    /**
     * Used to highlight the component.
     * 
     * @param me
     */
    protected void highlight()
    {
        if( getImage() != null )
        {
           ImageHighlightFilter ihf = new ImageHighlightFilter( true, 32 );
           Image imgHigh = createImage( new FilteredImageSource( getImage().getSource(), ihf ) );
        
            lblIcon.setIcon( new ImageIcon( imgHigh ) );
            lblIcon.repaint();   // Needed
        }
    }
    
    /**
     * Used to de-highlight the component.
     * 
     * @param me
     */
    protected void lowlight()
    {
        if( getImage() != null )
        {
            lblIcon.setIcon( new ImageIcon( getImage() ) );
            lblIcon.repaint();  // Needed
        }
    }
    
    //------------------------------------------------------------------------//
    
    private void initGUI()
    {
        Dimension sizePreferred = new Dimension( 65, 55 );
        Dimension sizeMinimum  = new Dimension( 56, 56 );
        Dimension sizeMaximum  = new Dimension( 85,175 );
        
        // Inicializo los componentes
        lblIcon = new JLabel();
        lblIcon.setHorizontalAlignment( JLabel.CENTER );
        lblIcon.setBorder( new EmptyBorder( 0, (sizePreferred.width - 48) / 2,
                                            0, (sizePreferred.width - 48) / 2  )  );  // Border para centrar el icon
        
        lblCaption = new JRoundLabel();
        lblCaption.setOpaque( false );
        lblCaption.setHorizontalAlignment( JLabel.CENTER );
        lblCaption.setVerticalAlignment( JLabel.TOP );
        lblCaption.setFont( lblCaption.getFont().deriveFont( Font.PLAIN, 11f ) );
        lblCaption.setBackground( ColorSchema.getInstance().getDesktopLauncherBackground()  );  // Is not opaque
        lblCaption.setForeground( ColorSchema.getInstance().getDesktopLauncherForegroundUnSelected() );
        lblCaption.setBorder( new EmptyBorder( 3,4,3,4 ) );
        
        // Los componentes se añanden al root pane
        setLayout( new BorderLayout( 0, 3 ) );
        add( lblIcon   , BorderLayout.CENTER );
        add( lblCaption, BorderLayout.SOUTH  );
        
        // Varios
        setOpaque( false );
        setMinimumSize( sizeMinimum  );
        setMaximumSize( sizeMaximum  );
        setPreferredSize( sizePreferred  );
        setBounds( 0, 
                   0,
                   (int) getPreferredSize().getWidth(), 
                   (int) getPreferredSize().getHeight() );
        setComponentPopupMenu( new ThisPopupMenu() );
        getGlassPane().addMouseListener( new GlassPaneMouseListener() );
    }

    //------------------------------------------------------------------------//
    // INNER CLASSES
    //------------------------------------------------------------------------//
    // TODO: Esto tiene que ir en el Launcher
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
            PDEDeskLauncher.this.highlight();
        }
        
        // Quita el icono de highlighted
        public void mouseExited( MouseEvent me )
        {
            PDEDeskLauncher.this.lowlight();
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

    //------------------------------------------------------------------------//
    // INNER CLASS: JPopupMenu --> para mostrar un JTextField y renombrar
    private final class PopupToRename extends JPopupMenu
    {
        private JTextField txt;
        
        private PopupToRename( JLabel lbl )
        {
            txt = new JTextField( lbl.getText() );
            txt.setMargin( new Insets( 0,0,0,0 ) );
            txt.setBorder( null );
            txt.setFont( lbl.getFont() );
            txt.setHorizontalAlignment( JTextField.CENTER );
            txt.addKeyListener( new KeyAdapter()
                {
                   public void keyPressed( KeyEvent ke )
                   {
                       if( ke.getKeyCode() == KeyEvent.VK_ENTER )
                       {
                           PDEDeskLauncher.this.setName( txt.getText() );
                           setVisible( false );
                       }
                   }
                } );
            
            add( txt );
            setBorder( null );
        }
        
        public void show( Component invoker, int x, int y )
        {
            super.show( invoker, x, y );
            
            txt.grabFocus();
            txt.selectAll();
        }
    }   
}