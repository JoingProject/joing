/*
 * YAFE.java
 *
 * Created on 1 de julio de 2008, 21:06
 */
package org.joing.pde.apps.YAFE;

import java.awt.Event;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import org.joing.common.desktopAPI.DeskComponent;
import org.joing.common.desktopAPI.DesktopManager;
import org.joing.common.desktopAPI.pane.DeskFrame;
import org.joing.common.desktopAPI.StandardImage;

/**
 * YAFE: Yet Another File Explorer.
 * 
 * @author  Francisco Morero Peyrona
 */
public class YAFE extends javax.swing.JPanel implements DeskComponent
{
    private JoingFileSystemTreeEditable tree;
    private Table table;
    
    //------------------------------------------------------------------------//
    
    public YAFE()
    {
        tree  = new JoingFileSystemTreeEditable();
        table = new Table();
        
        initComponents();
        initToolBarAndPopupMenu();
        
        split.setLeftComponent( new JScrollPane( tree ) );
        split.setRightComponent( new JScrollPane( table ) );
        split.setResizeWeight( .3d );
        
        showInFrame();
    }
        
    //------------------------------------------------------------------------//
    
    private void showInFrame()
    {
        DesktopManager dm   = org.joing.jvmm.RuntimeFactory.getPlatform().getDesktopManager();
        ImageIcon      icon = new ImageIcon( getClass().getResource( "images/yafe.png" ) );
        
        if( dm == null )
        {
            javax.swing.JFrame frame = new javax.swing.JFrame();
                               frame.setDefaultCloseOperation( javax.swing.JFrame.EXIT_ON_CLOSE );
                               frame.add( this );
                               frame.pack();
                               frame.setVisible( true );
        }
        else
        {
            // Show this panel in a frame created by DesktopManager Runtime.
            DeskFrame frame = dm.getRuntime().createFrame();
                      frame.setTitle( "Yet Another File Explorer" );
                      frame.setIcon( icon.getImage() );
                      frame.add( (DeskComponent) this );
                      
            dm.getDesktop().getActiveWorkArea().add( frame );
        }
    }
    
    private void initToolBarAndPopupMenu()
    {
        Action[]   actions = getActions();
        JPopupMenu popup   = new JPopupMenu();
        
        
        for( int n = 0; n < actions.length; n++ )
        {
            if( actions[n] == null )
            {
                toolbar.add( new JToolBar.Separator() );
                popup.add( new JPopupMenu.Separator() );
            }
            else
            {
                toolbar.add( actions[n] );
                popup.add( actions[n] );
            }
        }
        
        tree.setComponentPopupMenu( popup );
    }
    
    private Action[] getActions()
    {
        ImageIcon iconHome = new ImageIcon( getClass().getResource( "images/home.png" ) );
        ImageIcon iconLoad = new ImageIcon( getClass().getResource( "images/reload.png" ));
        
        AbstractAction actHome = new MyAction( "Home", iconHome.getImage(), 'H' )
        {
            public void actionPerformed( ActionEvent ae )
            {
                tree.goTo( new File( System.getProperty( "user.home" ) ) );
            }
        };
        
        AbstractAction actReload = new MyAction( "Reload", iconLoad.getImage(), 'L' )
        {
            public void actionPerformed( ActionEvent ae )
            {
                if( (ae.getModifiers() & ActionEvent.SHIFT_MASK) != 0 )
                    tree.reloadAll();
                else
                    tree.reloadSelected();
            }
        };
        
        AbstractAction actNewDir = new MyAction( "New folder", StandardImage.FOLDER, 'F' )
        {
            public void actionPerformed( ActionEvent ae )
            {
                try
                {
                    tree.createDir();
                }
                catch( IOException exc )
                {
                    org.joing.jvmm.RuntimeFactory.getPlatform().getDesktopManager().getRuntime().showException( exc, null );
                }
            }
        };
        
        AbstractAction actNewFile = new MyAction( "New file", StandardImage.NEW, 'I' )
        {
            public void actionPerformed( ActionEvent ae )
            {
                tree.createFile();
            }
        };
        
        AbstractAction actCut = new MyAction( "Cut", StandardImage.CUT, 'X' )
        {
            public void actionPerformed( ActionEvent ae )
            {
                tree.cut();
            }
        };

        AbstractAction actCopy = new MyAction( "Copy", StandardImage.COPY, 'C' )
        {
            public void actionPerformed( ActionEvent ae )
            {
                tree.copy();
            }
        };
        
        AbstractAction actPaste = new MyAction( "Paste", StandardImage.PASTE, 'V' )
        {
            public void actionPerformed( ActionEvent ae )
            {
                if( tree.paste() )
                    org.joing.jvmm.RuntimeFactory.getPlatform().getDesktopManager().getRuntime().showMessageDialog(
                            "Error pasteing", "One or more selected entities could not be pasted." );
            }
        };
        
        AbstractAction actRename = new MyAction( "Rename", StandardImage.PROPERTIES, 'R' )
        {
            public void actionPerformed( ActionEvent ae )
            {
                if( ! tree.rename() )
                    org.joing.jvmm.RuntimeFactory.getPlatform().getDesktopManager().getRuntime().showMessageDialog(
                            "Error renaming", "Selected entity could not be deleted." );
            }
        };
        
        AbstractAction actDelete = new MyAction( "Delete", StandardImage.DELETE, 'D' )
        {
            public void actionPerformed( ActionEvent ae )
            {
                if( ! tree.delete() )
                    org.joing.jvmm.RuntimeFactory.getPlatform().getDesktopManager().getRuntime().showMessageDialog(
                            "Error deleting", "One or more selected files or directories could not be deleted." );
            }
        };
        
        return (new Action[] { actHome, actReload, null, actNewDir, actNewFile, null, 
                              actCut, actCopy, actPaste, null, actRename, actDelete });
    }
            
    //------------------------------------------------------------------------//
    // INNER CLASS
    //------------------------------------------------------------------------//
    
    private abstract class MyAction extends AbstractAction
    {
        private MyAction( String sText, StandardImage img, int character )
        {
            this( sText, 
                  org.joing.jvmm.RuntimeFactory.getPlatform().getDesktopManager().getRuntime().getImage( img ),
                  character );
        }
        
        private MyAction( String sText, Image image, int character )
        {
            super( sText );
            
            KeyStroke ks = KeyStroke.getKeyStroke( character, Event.CTRL_MASK );
                    
            putValue( SMALL_ICON, new ImageIcon( image.getScaledInstance( 18, 18, Image.SCALE_SMOOTH ) ) );
            putValue( LARGE_ICON_KEY, new ImageIcon( image.getScaledInstance( 24, 24, Image.SCALE_SMOOTH ) ) );
            putValue( ACCELERATOR_KEY, ks );
            putValue( TOOL_TIP_TEXT_KEY, sText +" ["+ ks.toString() +"]" );
        }
    }
        
    //------------------------------------------------------------------------//
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        split = new javax.swing.JSplitPane();
        pnlBreadcrumb = new javax.swing.JPanel();
        toolbar = new javax.swing.JToolBar();

        pnlBreadcrumb.setBackground(new java.awt.Color(253, 212, 169));

        javax.swing.GroupLayout pnlBreadcrumbLayout = new javax.swing.GroupLayout(pnlBreadcrumb);
        pnlBreadcrumb.setLayout(pnlBreadcrumbLayout);
        pnlBreadcrumbLayout.setHorizontalGroup(
            pnlBreadcrumbLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 598, Short.MAX_VALUE)
        );
        pnlBreadcrumbLayout.setVerticalGroup(
            pnlBreadcrumbLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 34, Short.MAX_VALUE)
        );

        toolbar.setRollover(true);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(toolbar, javax.swing.GroupLayout.DEFAULT_SIZE, 598, Short.MAX_VALUE)
            .addComponent(pnlBreadcrumb, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(split, javax.swing.GroupLayout.DEFAULT_SIZE, 598, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(toolbar, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlBreadcrumb, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(split, javax.swing.GroupLayout.DEFAULT_SIZE, 302, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel pnlBreadcrumb;
    private javax.swing.JSplitPane split;
    private javax.swing.JToolBar toolbar;
    // End of variables declaration//GEN-END:variables
}