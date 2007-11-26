/*
 * PDEDeskLauncher.java
 * 
 * Created on 11-sep-2007, 21:41:50
 * 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.joing.pde.desktop.deskwidget.deskLauncher;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.FilteredImageSource;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.MenuElement;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import org.joing.common.desktopAPI.DesktopManagerFactory;
import org.joing.common.desktopAPI.deskwidget.deskLauncher.Launcher;
import org.joing.common.desktopAPI.deskwidget.deskLauncher.LauncherEvent;
import org.joing.common.desktopAPI.deskwidget.deskLauncher.LauncherEventListener;
import org.joing.common.desktopAPI.workarea.WorkArea;
import org.joing.pde.desktop.workarea.PDEWorkArea;
import org.joing.pde.desktop.deskwidget.PDEDeskWidget;
import org.joing.pde.ColorSchema;
import org.joing.pde.swing.ImageHighlightFilter;
import org.joing.pde.swing.JRoundPanel;

/**
 * This is the base class for all DeskLaunchers: Applications and Folders.
 * 
 * @author Francisco Morero Peyrona
 */
public class PDEDeskLauncher extends PDEDeskWidget implements Launcher
{
    // Launcher interface variables
    private Image   image;
    private boolean bSelected;
    
    // Swing variables
    private JRoundPanel   pnlAll;
    private IconComponent icon;
    private TextComponent text;
    
    //------------------------------------------------------------------------//
    
    public PDEDeskLauncher()
    {
        this( "No name" );
    }
    
    public PDEDeskLauncher( String sName )
    {
        this( sName, null );
    }
    
    public PDEDeskLauncher( String sName, Image image )
    {
        if( image == null )
            image = DesktopManagerFactory.getDM().getRuntime().getIcon( null, "launcher.png" ).getImage();
        
        initGUI();
        
        setName( sName );
        setImage( image );
    }
    
    public void close()
    {
        // Nothing to do
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
//        if( bNewStatus != isSelected() )
//        {
//            // FIXME: En lugar de buscar el padre e invocar sus métodos, sería 
//            //        más elegante (y más acorde con Java) lanzar simplemente
//            //        el evento y que el padre lo escuche y reaccione
//            if( PDEDeskLauncher.this.getParent() != null && 
//                PDEDeskLauncher.this.getParent() instanceof PDEWorkArea )
//            {
//                PDEWorkArea wa = (PDEWorkArea) PDEDeskLauncher.this.getParent();
//
//                if( ! bIncremental )
//                    wa.setSelected( Component.class, false );  // Deselects all
//                else
//                    wa.moveToFront( PDEDeskLauncher.this );
//            }
//
//            setSelected( bNewStatus );
//            
//            // When selection is incremental, 2 events are fired: the 1st one
//            // indicating that the Launcher was selected and the 2nd one 
//            // indicating that it is an incremental selection
//            if( bIncremental )
//                fireSelectionIncrementalEvent( new LauncherEvent( this, bNewStatus ) );
//        }
    }
    
    //------------------------------------------------------------------------//
    // ACTIONS
    // TODO: Creo que estas actions no pertencen al DeskLauncher, 
    //       sino a la WorkArea: hay que pensar en ello.
    //       Estas son lanzadas desde el popup y quizás lo que deberían hacer es 
    //       llamar al container (WorkArea)
    public void delete()
    {
        if( DesktopManagerFactory.getDM().getRuntime().confirmDialog( "Delete launcher", 
                                                   "Are you sure you want to delete it?\n"+
                                                   "(deleted objects can not be recovered)" ) )
        {
            WorkArea workArea = (PDEWorkArea) SwingUtilities.getAncestorOfClass( PDEWorkArea.class, this );
                     // FIXME: hacerlo --> workArea.remove( this );
                
            // There is not a fireLauncherDeleted() because it can be resolved   
            // by adding a listner to the WorkArea
        }
    }
    
    public void toTrashcan()
    {
        if( DesktopManagerFactory.getDM().getRuntime().confirmDialog( "Send launcher to trashcan", 
                                                   "Are you sure you want to send it to trashcan?" ) )
        {
            
            PDEWorkArea workArea = (PDEWorkArea) SwingUtilities.getAncestorOfClass( PDEWorkArea.class, this );
                        // FIXME: hacerlo --> workArea.remove( this );
                        
            // TODO: mandarlo a la papelera e implementar el fire
            // fireLauncherToTrashcan( this );
            DesktopManagerFactory.getDM().getRuntime().showMessage( "Option not yet implemented" );
        }
    }
    
    public void rename()
    {
        text.setEditable( true );
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
        
        text = new TextComponent();
        text.setFont( text.getFont().deriveFont( Font.BOLD, 11f ) );
        /* TODO: mirar cómo hacer esto
        int nPixelsWidth = SwingUtilities.computeStringWidth( text.getFontMetrics( text.getFont() ), "ABC" ) / 3;   // This line must be after setFont(...)
        text.setColumns( PDEDeskLauncher.this.getPreferredSize().width / nPixelsWidth );*/
        
        pnlAll = new JRoundPanel();
        pnlAll.setOpaque( false );
        pnlAll.setBackground( ColorSchema.getInstance().getDeskLauncherTextBackground() );
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
        setGlassPane( new GlassPaneDeskLauncher( this ) );
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
    
    private final class TextComponent extends JScrollPane
    {
        private JTextPane text;
        
        private TextComponent()
        {
            text = new JTextPane();
            text.setBorder( null );
            text.setBackground( ColorSchema.getInstance().getDeskLauncherTextBackground() );
            setSelected( false );
            
            StyledDocument doc = text.getStyledDocument();
            //  Set alignment to be centered for all paragraphs
            MutableAttributeSet standard = new SimpleAttributeSet();
            StyleConstants.setAlignment( standard, StyleConstants.ALIGN_CENTER );
            doc.setParagraphAttributes( 0, 0, standard, true );
            
            setViewportView( text );
            setHorizontalScrollBarPolicy( ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER );
            setVerticalScrollBarPolicy( ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER );
            setOpaque( false );
            setBorder( null );
            getViewport().setOpaque( false );
        }
        
        private void setText( String sText )
        {
            text.setText( sText );
        }
        
        private void setEditable( boolean b )
        {
            if( b != text.isEditable() )
            {
                if( b )
                {
                    text.setEditable( true );
                    text.selectAll();
                    text.requestFocusInWindow();
                }
                else
                {
                    text.setCaretPosition( 0 );
                    text.setEditable( false );
                }
            }
        }
        
        private void setSelected( boolean b )
        {
            Color clrFore = b ? ColorSchema.getInstance().getDeskLauncherTextForegroundSelected() :
                                ColorSchema.getInstance().getDeskLauncherTextForegroundUnSelected();
                    
            // TODO: así no funciona -> text.setForeground( Color.white );
            //       hay que hacerlo como se hace para centrar el texto: usando estilos
            
            text.setOpaque( b );
            text.repaint();
            
            if( ! b )
                text.setEditable( false );    // Just in case it was editing
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
            add( createMenuItem( "To trashcan", "trashcan"   , "TRASHCAN"   ) );
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
                item.setIcon( DesktopManagerFactory.getDM().getRuntime().getIcon( null, sIconName +".png", 16, 16 ) );
            
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