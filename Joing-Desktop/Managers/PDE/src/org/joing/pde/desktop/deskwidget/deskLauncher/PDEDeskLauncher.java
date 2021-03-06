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
import javax.swing.MenuElement;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import org.joing.kernel.api.desktop.DesktopManager;
import org.joing.kernel.api.desktop.StandardImage;
import org.joing.kernel.api.desktop.deskwidget.deskLauncher.DeskLauncher;
import org.joing.kernel.api.desktop.deskwidget.deskLauncher.DeskLauncherListener;
import org.joing.kernel.api.desktop.workarea.WorkArea;
import org.joing.pde.desktop.workarea.PDEWorkArea;
import org.joing.pde.desktop.deskwidget.PDEDeskWidget;
import org.joing.kernel.swingtools.JoingSwingUtilities;
import org.joing.pde.media.PDEColorSchema;
import org.joing.pde.swing.EventListenerList;
import org.joing.kernel.swingtools.ImageHighlightFilter;
import org.joing.kernel.swingtools.JRoundLabel;
import org.joing.kernel.swingtools.JRoundPanel;

/**
 * This is the base class for all DeskLaunchers: Applications and Folders.
 * 
 * @author Francisco Morero Peyrona
 */
public class PDEDeskLauncher extends PDEDeskWidget implements DeskLauncher
{
    private DeskLauncher.Type type;
    
    private String sTarget;
    private String sArguments;
    
    private boolean bSelected;
    
    // Swing variables
    private JRoundPanel   pnlAll;
    private IconComponent icon;
    private TextComponent text;
    
    private EventListenerList listenerList;    // NEXT: Cambiar esto por la de JComponent
    
    //------------------------------------------------------------------------//
    
    public PDEDeskLauncher()
    {
        listenerList = new EventListenerList();
        initGUI();
    }
    
    //------------------------------------------------------------------------//
    // DeskLauncher interface implementation
    
    public Type getType()
    {
        return type;
    }
    
    // Package scope
    void setType( Type type )
    {
        this.type = type;
    }
    
    public String getDescription()
    {
        return getToolTipText();
    }

    public void setDescription( String sDescription )
    {
        setToolTipText( sDescription );
    }
    
    public Image getImage()
    {
        return icon.getImage();
    }
    
    public void setImage( Image image )
    {
        icon.setImage( image );
    }
    
    public String getText()
    {
        return text.getText();
    }
    
    public void setText( String sText )
    {
        text.setText( sText );
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
        if( bSelected != bNewStatus )
            setSelected( bNewStatus, false );
    }
    
    /**
     * Has to be redefined by every subclasses.
     */
    public void launch()
    {
       JoingSwingUtilities.launch( getType(), getTarget(), getArguments() );
    }
    
    public String getTarget()
    {
        return sTarget;
    }

    public void setTarget( String sTarget )
    {
        if( sTarget != null )
        {
            sTarget = sTarget.trim();
            
            if( sTarget.length() == 0 )
                sTarget = null;
        }
        
        this.sTarget = sTarget;
    }
    
    public String getArguments()
    {
        return sArguments;
    }

    public void setArguments( String sArguments )
    {
        if( sArguments != null )
        {
            sArguments = sArguments.trim();
            
            if( sArguments.length() == 0 )
                sArguments = null;
        }
        
        this.sArguments = sArguments;
    }
    
    public void addLauncherListener( DeskLauncherListener ll )
    {
        listenerList.add( DeskLauncherListener.class, ll );
    }
    
    public void removeLauncherListener( DeskLauncherListener ll ) 
    {
        listenerList.remove( ll );
    }
    
    protected void fireSelection()
    {
        DeskLauncherListener[] al = listenerList.getListeners( DeskLauncherListener.class );
        
        for( int n = 0; n < al.length; n++ )
             al[n].selection( this );
    }
    
    protected void fireSelectionIncremental()
    {
        DeskLauncherListener[] al = listenerList.getListeners( DeskLauncherListener.class );
        
        for( int n = 0; n < al.length; n++ )
             al[n].selectionIncremental( this );
    }
    
    protected void fireLaunched()
    {
        DeskLauncherListener[] al = listenerList.getListeners( DeskLauncherListener.class );
        
        for( int n = 0; n < al.length; n++ )
             al[n].launched( this );
    }
    
    //------------------------------------------------------------------------//
    
    /**
     * When selection is incremental, 2 events are fire: the 1st one
     * indicating that the DeskLauncher was selected and the 2nd one
     * indicating that it is an incremental selection
     */
    // This method is called from this.GlassPaneMouseListner::mousePressed(...)
    // and from this::setSelected( boolean )
    protected void setSelected( boolean bNewStatus, boolean bIncremental )
    {
        if( bIncremental )   // Works as a toggle
            bNewStatus = ! isSelected();
        
        if( bNewStatus != isSelected() )
        {
            text.setSelected( bNewStatus );
            bSelected = bNewStatus;
            
            if( bIncremental )
                fireSelectionIncremental();
            else
                fireSelection();
        }
    }
    
    //------------------------------------------------------------------------//
    // ACTIONS
    // TODO: Creo que estas actions no pertencen al DeskLauncher, 
    //       sino a la WorkArea: hay que pensar en ello.
    //       Estas son lanzadas desde el popup y quizás lo que deberían hacer es 
    //       lanzar un evento indicando que quieren ser borradas y el evento 
    //       capturarlo el container (WorkArea)
    public void delete()
    {
        DesktopManager dm = org.joing.kernel.jvmm.RuntimeFactory.getPlatform().getDesktopManager();
        
        if( dm.getRuntime().showYesNoDialog( "Delete launcher", 
                                             "Are you sure you want to delete it?\n"+
                                             "(deleted objects can not be recovered)" ) )
        {
            WorkArea workArea = (PDEWorkArea) SwingUtilities.getAncestorOfClass( PDEWorkArea.class, this );
     
            dm.getRuntime().showMessageDialog( null, "Option not yet implemented" );
            /// workArea.remove( this );
                
            // There is not a fireLauncherDeleted() because it can be resolved   
            // by adding a listener to the WorkArea
        }
    }
    
    public void toTrashcan()
    {
        DesktopManager dm = org.joing.kernel.jvmm.RuntimeFactory.getPlatform().getDesktopManager();
        
        if( dm.getRuntime().showYesNoDialog( "Send launcher to trashcan", 
                                             "Are you sure you want to send it to trashcan?" ) )
        {
            
            PDEWorkArea workArea = (PDEWorkArea) SwingUtilities.getAncestorOfClass( PDEWorkArea.class, this );
                      /// hacerlo --> workArea.remove( this );
                        
            // TODO: mandarlo a la papelera e implementar el fire
            // fireLauncherToTrashcan( this );
            dm.getRuntime().showMessageDialog( null, "Option not yet implemented" );
        }
    }
    
    public void editProperties()
    {
        PDEDeskLauncherPropertiesPanel panel = new PDEDeskLauncherPropertiesPanel( this );
        
        if( org.joing.kernel.jvmm.RuntimeFactory.getPlatform().getDesktopManager().getRuntime().showAcceptCancelDialog( "Launcher Properties", panel ) )
            panel.createLauncher();
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
        // Sizes must be initialized prior to components (they use them)
        setMinimumSize(   new Dimension( 66, 66 ) );
        setMaximumSize(   new Dimension( 96,188 ) );
        setPreferredSize( new Dimension( 76, 48+11+2+10 ) );   // icon + font + vgap + border(5+5)

        // Initialising components
        icon = new IconComponent();
        
        text = new TextComponent();
        text.setFont( text.getFont().deriveFont( Font.BOLD, 11f ) );
        /* NEXT: mirar cómo hacer esto
        int nPixelsWidth = SwingUtilities.computeStringWidth( text.getFontMetrics( text.getFont() ), "ABC" ) / 3;   // This line must be after setFont(...)
        text.setColumns( PDEDeskLauncher.this.getPreferredSize().width / nPixelsWidth );*/
        
        pnlAll = new JRoundPanel();
        pnlAll.setOpaque( false );
        pnlAll.setBackground( PDEColorSchema.getInstance().getDeskLauncherTextBackground() );
        pnlAll.setTransparency( 85 );
        pnlAll.setLayout( new BorderLayout( 0,2 ) );
        pnlAll.setBorder( new EmptyBorder( 7,7,7,7 ) );
        pnlAll.add( icon, BorderLayout.CENTER );
        pnlAll.add( text, BorderLayout.SOUTH  );
        
        // Initializes this
        setComponentPopupMenu( new ThisPopupMenu() );
        setBounds( 0,
                   0,
                   (int) getPreferredSize().getWidth(), 
                   (int) getPreferredSize().getHeight() );
        
        add( pnlAll );
        
        SwingUtilities.invokeLater( new Runnable()    // RootPane is null until runtime
        {
            public void run()
            {
                PDEDeskLauncher.this.getRootPane().setGlassPane( new GlassPaneDeskLauncher( PDEDeskLauncher.this ) );
            }
        });
    }

    //------------------------------------------------------------------------//
    // INNER CLASSES
    //------------------------------------------------------------------------//
    
    private final class IconComponent extends JLabel
    {
        private Image image;     // Image object: used to avoid image corruption (using the filter)
        
        private IconComponent()
        {
            setHorizontalAlignment( JLabel.CENTER );
            setBorder( new EmptyBorder( 0, (getPreferredSize().width - 48) / 2,
                                        0, (getPreferredSize().width - 48) / 2 ) );  // Border to center the icon
            setImage( null );
        }
        
        public Image getImage()
        {
            return image;
        }
        
        public void setImage( Image image )
        {
            ImageIcon imgIcon;
            
            this.image = image;
            
            if( image == null )
                image = org.joing.kernel.jvmm.RuntimeFactory.getPlatform().getDesktopManager().getRuntime().getImage( StandardImage.LAUNCHER );
            
            imgIcon = new ImageIcon( image );

            if( imgIcon.getIconWidth() != 48 || imgIcon.getIconHeight() != 48 )
                imgIcon.setImage( imgIcon.getImage().getScaledInstance( 48, 48, Image.SCALE_SMOOTH ) );
                
            setIcon( imgIcon );
        }
        
        private void setHighlighted( boolean b )
        {
            if( image != null )   // Can be null only if an problem occurred finding or loading the image
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
    
    private final class TextComponent extends JRoundLabel
    {
        private TextComponent()
        {
            setText( "Noname" );
            setOpaque( false );
            setBackground( PDEColorSchema.getInstance().getDeskLauncherTextBackground() );
        }
        
        private void setSelected( boolean b )
        {
            Color clrFore = b ? PDEColorSchema.getInstance().getDeskLauncherTextForegroundSelected() :
                                PDEColorSchema.getInstance().getDeskLauncherTextForegroundUnSelected();
                    
            setForeground( clrFore );
            setOpaque( b );
        }
    }
    
    //------------------------------------------------------------------------//
    // El popup del DesktopLauncher
    
    private class ThisPopupMenu extends JPopupMenu implements ActionListener
    {
        private ThisPopupMenu()
        {
            add( createMenuItem( "Open"       , StandardImage.LAUNCHER  , "OPEN"       ) );
            addSeparator();
            add( createMenuItem( "To trashcan", StandardImage.TRASHCAN  , "TRASHCAN"   ) );
            add( createMenuItem( "Delete"     , StandardImage.DELETE    , "DELETE"     ) );
            addSeparator();
            add( createMenuItem( "Properties" , StandardImage.PROPERTIES, "PROPERTIES" ) );
        }
        
        private JMenuItem createMenuItem( String sText, StandardImage icon, String sCommand )
        {
            JMenuItem item = new JMenuItem( sText );
                      item.setActionCommand( sCommand );
                      item.addActionListener( this );
                      
            if( icon != null )
                item.setIcon( new ImageIcon( org.joing.kernel.jvmm.RuntimeFactory.getPlatform().getDesktopManager().getRuntime().getImage( icon, 16, 16 ) ) );
            
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
            else if( sCommand.equals( "PROPERTIES" ) )  PDEDeskLauncher.this.editProperties();
        }
    }
}