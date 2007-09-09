/*
 * WDesktopComponent.java
 *
 * Created on 10 de febrero de 2007, 19:23
 *
 * (c) 2006 - Francisco Morero Peyrona
 *
 * License: {license}
 */

package org.joing.pde.desktop.workarea.items;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.Point;
import java.awt.PopupMenu;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.FilteredImageSource;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRootPane;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.event.MouseInputAdapter;
import org.joing.pde.desktop.swing.ColorSchema;
import org.joing.pde.desktop.swing.ImageHighlightFilter;
import org.joing.pde.desktop.swing.JRoundLabel;

/**
 * Common base class for all kind of "icons like" that are shown in the desk.
 * Among them: launchers, documents, etc.
 * 
 * No olvidar poner el setPreferredSize y el JPopupMenu (si se quiere popup menu)
 * 
 * @author Francisco Morero Peyrona
 */
public abstract class WorkAreaComponent 
       extends WorkAreaItem 
       implements Cloneable
{
    public static final String OPEN   = "OPEN";
    public static final String REMOVE = "REMOVE";   // Lo manda a la papelera
    public static final String DELETE = "DELETE";   // Lo elimina completamente
    public static final String RENAME = "RENAME";
    public static final String PROPS  = "PROPS";    // No puedo usar PROPERTIES porque está definido en una super-class
    
    protected final static Dimension SIZE_PREFERED = new Dimension( 85, 75 );
    protected final static Dimension SIZE_MINIMUM  = new Dimension( 75, 75 );
    protected final static Dimension SIZE_MAXIMUM  = new Dimension( 85,175 );
    
    //------------------------------------------------------------------------//
    
    private JRootPane root;
    
    private Image  image;
    private String sText;
            
    private JLabel      icon;
    private JRoundLabel label;
    private JPopupMenu  popup;
    
    //------------------------------------------------------------------------//
    
    public WorkAreaComponent()
    {
        root = new JRootPane();
        ((JPanel) root.getContentPane()).setOpaque( false );
        
        // Glass Pane: do not touch
        GlassPaneMouseListener gpml = new GlassPaneMouseListener();
        
        root.getGlassPane().setVisible( true );
        root.getGlassPane().addMouseListener( gpml );
        root.getGlassPane().addMouseMotionListener( gpml );
        ((JPanel) root.getGlassPane()).setOpaque( false );
        
        // Añado el root pane a este JPanel
        super.setLayout( new BorderLayout() );
        super.add( root, BorderLayout.CENTER );
        
        // Inicializo los componentes
        icon  = new JLabel();
        icon.setBorder( new EmptyBorder( 0, (SIZE_PREFERED.width - 48) / 2,
                                              0, (SIZE_PREFERED.width - 48) / 2 ) );  // Border para centrar el icon
        
        label = new JRoundLabel();
        label.setHorizontalAlignment( JLabel.CENTER );
        label.setFont( label.getFont().deriveFont( Font.PLAIN, 11f ) );
        label.setBackground( ColorSchema.getInstance().getDesktopLauncherBackgroundUnSelected() );
        label.setForeground( ColorSchema.getInstance().getDesktopLauncherForegroundUnSelected() );
        label.setBorder( new EmptyBorder( 4,4,4,4 ) );
        
        // Los componentes se añanden al root pane
        setLayout( new BorderLayout( 0, 3 ) );
        add( icon , BorderLayout.CENTER );
        add( label, BorderLayout.SOUTH  );
        
        // Varios
        setOpaque( false );
        setMinimumSize( SIZE_MINIMUM );
        setMaximumSize( SIZE_MAXIMUM );
        setPreferredSize( SIZE_PREFERED );
    }
    
    //------------------------------------------------------------------------//
    // REDEFINIDOS PARA REDIRIGIRLOS A this.root.getContentPane
    
    public Component add( Component c )             { return root.getContentPane().add( c );    }
    public Component add( Component c, int n )      { return root.getContentPane().add( c, n ); }
    public Component add( String s, Component c )   { return root.getContentPane().add( s, c ); }
    public void add( Component c, Object o, int n ) { root.getContentPane().add( c, o, n );     }
    public void add( Component c, Object o )        { root.getContentPane().add( c, o );        }
    
    public void setLayout( LayoutManager lm )
    { 
        if( root != null )
            root.getContentPane().setLayout( lm );
    }
    
    //------------------------------------------------------------------------//
    // ACTIONS    
    
    public void open()
    {
        fireActionPerformed( OPEN );
    }
    
    public void delete()
    {
        fireActionPerformed( DELETE );
    }
    
    public void remove()
    {
        fireActionPerformed( REMOVE );
    }
    
    public void rename()
    {
        fireActionPerformed( RENAME );
    }
    
    public void showProperties()
    {
        fireActionPerformed( PROPS );
    }
    
    //------------------------------------------------------------------------//
    // ABSTRACTS
    
    /**
     * Subsclases will do whatever they need (v.g. rename associated file)
     * 
     * @param sNewName New Component name
     */
    protected abstract void doRename( String sNewName );
    
    //------------------------------------------------------------------------//
    
    public void setSelected( boolean b )
    {
        if( b != isSelected() )
        {
            this.label.setOpaque( b );
        
            this.label.setBackground( b ? ColorSchema.getInstance().getDesktopLauncherBackgroundSelected() :
                                          ColorSchema.getInstance().getDesktopLauncherBackgroundUnSelected() );
            
            this.label.setForeground( b ? ColorSchema.getInstance().getDesktopLauncherForegroundSelected() :
                                          ColorSchema.getInstance().getDesktopLauncherForegroundUnSelected() );
            this.label.repaint();
            
            super.setSelected( b );
        }
    }
    
    public void showPopupMenu( Point p )
    {
        if( popup != null && ! popup.isVisible() )
            popup.show( this, p.x, p.y );
    }
    
    public Image getImage()
    {
        return image;
    }
    
    protected void setImage( Image image )
    {
        this.image = image;
        icon.setIcon( new ImageIcon( image ) );
    }
    
    public String getText()
    {
        return sText;
    }
    
    protected void setText( String sText )
    {
        this.sText = sText;
        label.setText( sText );
    }
    
    /**
     * Used to highlight the component.
     * 
     * @param me
     */
    protected void mouseEntered( MouseEvent me )
    {
        if( image != null )
        {
           ImageHighlightFilter ihf = new ImageHighlightFilter( true, 32 );
           Image imgHigh = createImage( new FilteredImageSource( image.getSource(), ihf ) );
        
            ((ImageIcon) icon.getIcon()).setImage( imgHigh );
            icon.repaint();   // Needed
        }
    }
    
    /**
     * Used to de-highlight the component.
     * 
     * @param me
     */
    protected void mouseExited( MouseEvent me )
    {
        if( image != null )
        {
            ((ImageIcon) icon.getIcon()).setImage( image );
            icon.repaint();  // Needed
        }
    }
    
    /**
     * Shows a JTextField on top of passed JLabel component with same
     * Font, Background, Foreground and Text.<br>
     * If user accepts, <code>doRename( String sNewName )</code> will be invoked.
     */
    protected void doRenameAction( JLabel label )
    {
        Popup4Text p4t = new Popup4Text( label );
        Rectangle  rec = label.getBounds();
        
        p4t.setPreferredSize( new Dimension( rec.width, rec.height ) );
        p4t.show( label, rec.x, rec.y-53 );
    }
    
    //------------------------------------------------------------------------//
    // INNER CLASSES
    //------------------------------------------------------------------------//
    
    private final class GlassPaneMouseListener extends MouseInputAdapter
    {            
        private Point ptMousePosition;
        private Point ptWidgetPosition;
        
        public void mouseClicked( MouseEvent me )
        {
            if( me.getClickCount() == 2 )
                open();
        }

        // Pone el icono en highlighted
        public void mouseEntered( MouseEvent me )
        {
            WorkAreaComponent.this.mouseEntered( me );
        }
        
        // Quita el icono de highlighted
        public void mouseExited( MouseEvent me )
        {
            WorkAreaComponent.this.mouseExited( me );
        }
        
        public void mousePressed( MouseEvent me )
        {
            boolean bCtrlDown = (me.getModifiersEx() & MouseEvent.CTRL_DOWN_MASK) != 0;
            
            if( me.getButton() == MouseEvent.BUTTON1 )
            {
                int nSelectionType = WorkAreaItemChangeEvent.SELECTION_REQUEST;
                
                this.ptMousePosition  = me.getPoint();
                this.ptWidgetPosition = WorkAreaComponent.this.getLocation();
                    
                if( bCtrlDown )
                    nSelectionType = (WorkAreaComponent.this.isSelected()) ? WorkAreaItemChangeEvent.DESELECTION_INCR_REQUEST :
                                                                     WorkAreaItemChangeEvent.SELECTION_INCR_REQUEST;
                
                WorkAreaComponent.this.fireWorkAreaItemChangeEvent( nSelectionType, me );
            }                                                // Para esta aplicación es mejor utilizar esta
            else if( me.getButton() == MouseEvent.BUTTON3 )  // constante en lugar de isPopupTrigger(), 
            {                                                // me lo he pensado mucho: no cambiarlo.
                if( ! bCtrlDown )
                    WorkAreaComponent.this.fireWorkAreaItemChangeEvent( WorkAreaItemChangeEvent.SELECTION_REQUEST, me );
                
                if( WorkAreaComponent.this.isSelected() )
                    WorkAreaComponent.this.showPopupMenu( me.getPoint() );
            }
        }
 
        public void mouseReleased( MouseEvent me )
        {
            if( ! WorkAreaComponent.this.getLocation().equals( ptWidgetPosition ) )
                WorkAreaComponent.this.fireWorkAreaItemChangeEvent( WorkAreaItemChangeEvent.MOVED_INFORM, me );
        }
        
        public void mouseDragged( MouseEvent me )
        {
            if( WorkAreaComponent.this.isSelected() && this.ptMousePosition != null )   // No quitar != null: ocuren eventos raros
                WorkAreaComponent.this.setLocation(
                                   me.getPoint().x + WorkAreaComponent.this.getX() - this.ptMousePosition.x,
                                   me.getPoint().y + WorkAreaComponent.this.getY() - this.ptMousePosition.y );
        }
    }
    
    //------------------------------------------------------------------------//
    
    // INNER CLASS: JPopupMenu --> para mostrar un JTextField y renombrar
    private final class Popup4Text extends JPopupMenu
    {
        private JTextField txt;
        
        private Popup4Text( JLabel lbl )
        {            
            txt = new JTextField( lbl.getText() );
            txt.setMargin( new Insets( 0,0,0,0 ) );
            txt.setBorder( null );
            txt.setBackground( lbl.getBackground() );
            txt.setForeground( lbl.getForeground() );
            txt.setFont( lbl.getFont() );
            txt.addKeyListener( new KeyAdapter()
                {
                   public void keyPressed( KeyEvent e )
                   {
                       if( e.getKeyCode() == KeyEvent.VK_ENTER )
                       {
                           doRename( txt.getText() );
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