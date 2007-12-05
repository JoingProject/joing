/*
 * PDETaskBarPanel.java
 *
 * Created on 15 de septiembre de 2007, 22:07
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.joing.pde.desktop.taskbar;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import org.joing.common.desktopAPI.DeskComponent;
import org.joing.common.desktopAPI.DesktopManagerFactory;
import org.joing.common.desktopAPI.pane.DeskFrame;
import org.joing.common.desktopAPI.taskbar.TaskBarListener;
import org.joing.common.desktopAPI.taskbar.TaskBarPanel;
import org.joing.pde.PDEUtilities;
import org.joing.pde.desktop.container.PDEFrame;
import org.joing.pde.swing.JToolBarHandle;

/**
 * The base class for most part of the widtgets that are shown in the TaskBar.
 * 
 * @author Francisco Morero Peyrona
 */
public class PDETaskBarPanel 
       extends JPanel 
       implements MouseListener, 
                  TaskBarPanel
{
    private boolean        bLocked;
    private JToolBarHandle handle;
    
    // Popup menu items
    protected JMenuItem itemPreferences;
    protected JMenuItem itemAbout;
    protected JMenuItem itemRemove;
    protected JMenuItem itemMove;
    protected JMenuItem itemLock;
    
    //------------------------------------------------------------------------//
    
    /** Creates a new instance of PDETaskBarPanel */
    public PDETaskBarPanel()
    {
        bLocked    = false;
        handle     = new JToolBarHandle( this );
        add( handle, BorderLayout.WEST );
        
        setComponentPopupMenu( new CommonPopupMenu() );    // It is inherited by sub-components
        
        addMouseListener( this );
    }
    
    //------------------------------------------------------------------------//
    // PDETaskBarPanel interface
    
    public boolean isLocked()
    {
        return bLocked;
    }
    
    public void setLocked( boolean b )
    {
        bLocked = b; 
    }
    
    public void addTaskBarPanelListener( TaskBarListener tbl )
    {
        throw new UnsupportedOperationException( "Not supported yet." );
    }

    public void removeTaskBarPanelListener( TaskBarListener tbl )
    {
        throw new UnsupportedOperationException( "Not supported yet." );
    }
    
    //------------------------------------------------------------------------//
    
    // Mouse Listener is needed to show the popup. 
    // See: http://www.jguru.com/forums/view.jsp?EID=1239349
    public void mouseClicked( MouseEvent me )
    {
    }

    public void mousePressed(MouseEvent e)
    {
    }

    public void mouseReleased(MouseEvent e)
    {
    }

    public void mouseEntered(MouseEvent e)
    {
    }

    public void mouseExited(MouseEvent e)
    {
    }
    
    //------------------------------------------------------------------------//
    
    protected DeskComponent getAboutPanel()
    {
        // FIXME: Decidir qué hacer con esto
        return null;
    }
    
    protected DeskComponent getPreferencesPanel()
    {
        // FIXME: Decidir qué hacer con esto
        return null;
    }
    
    protected void onPreferencesChanged( DeskComponent prefs )
    {
        // FIXME: Decidir qué hacer con esto
    }
        
    //------------------------------------------------------------------------//
    
    private void onPreferences()
    {
        DeskComponent prefs = getPreferencesPanel();
        
        if( prefs != null )
            DesktopManagerFactory.getDM().getDesktop().getActiveWorkArea().add( new TheFrame( (Component) prefs ) );
    }
    
    private void onAbout()
    {
        DeskComponent about = getAboutPanel();
        
        if( about == null )
        {
            JPanel pnl = new JPanel();
                   pnl.add( new JLabel( "There is no information about this component." ) );
            about = (DeskComponent) pnl;
        }
        
        // Better to use a Frame than a Dialog (modaless: this is the way Gnome does it)
        DeskFrame frame = DesktopManagerFactory.getDM().getRuntime().createFrame();
                  frame.setTitle( "About" );
                  frame.add( about );
                  
        DesktopManagerFactory.getDM().getDesktop().getActiveWorkArea().add( frame );
    }
    
    private void onRemove()
    {   // TODO: Hacerlo
        throw new UnsupportedOperationException( "Operation not supported yet." );
    }
    
    private void onMove()
    {   // TODO: Hacerlo
        throw new UnsupportedOperationException( "Operation not supported yet." );
    }
    
    private void onLock()
    {
        bLocked = ! bLocked;
    }
    
    //------------------------------------------------------------------------//
    // INNER CLASS: Popup Menu
    //------------------------------------------------------------------------//
    private final class CommonPopupMenu extends JPopupMenu implements ActionListener
    { 
        private CommonPopupMenu()
        {   
            itemPreferences = new JMenuItem( "Preferences" );
            itemPreferences.addActionListener( this );
            itemPreferences.setIcon( PDEUtilities.getIcon( null, "properties.png", 16, 16 ) );
            add( itemPreferences );
            
            itemAbout = new JMenuItem( "About" );
            itemAbout.addActionListener( this );
            itemAbout.setIcon( PDEUtilities.getIcon( null, "info.png", 16, 16 ) );
            add( itemAbout );
            
            addSeparator();
            
            itemRemove = new JMenuItem( "Remove" );
            itemRemove.addActionListener( this );
            itemRemove.setIcon( PDEUtilities.getIcon( null, "remove.png", 16, 16 ) );
            add( itemRemove );
            
            itemMove = new JMenuItem( "Move" );
            itemMove.addActionListener( this );
            itemMove.setIcon( PDEUtilities.getIcon( null, "move.png", 16, 16 ) );
            itemMove.setEnabled( ! isLocked() );
            add( itemMove );
            
            itemLock = new JMenuItem( "Lock" );
            itemLock.addActionListener( this );
            itemLock.setIcon( PDEUtilities.getIcon( null, "lock.png", 16, 16 ) );
            add( itemLock );
        }
        
        public void actionPerformed( ActionEvent ae )
        {
            JMenuItem source = (JMenuItem) ae.getSource();
            
            if(      source == itemPreferences )  onPreferences();
            else if( source == itemAbout       )  onAbout();
            else if( source == itemRemove      )  onRemove();
            else if( source == itemMove        )  onMove();
            else if( source == itemLock        )  onLock();
        }
    }
    
    //------------------------------------------------------------------------//
    // INNER CLASS: Default empty frame with [Accept] and [Cancel] buttons
    //------------------------------------------------------------------------//
    
    private final class TheFrame extends PDEFrame
    {
        private JButton   btnAccept;
        private JButton   btnCancel;
        private Component contents;
        
        TheFrame( Component pnl )
        {
            contents  = pnl;
            btnAccept = new JButton( "Accept" );
            btnCancel = new JButton( "Cancel" );

            btnAccept.addActionListener( new ActionListener()
            {
                public void actionPerformed( ActionEvent ae )
                {
                    dispose();
                    onPreferencesChanged( (DeskComponent) TheFrame.this.contents );
                }
            } );
        
            btnCancel.addActionListener( new ActionListener() 
            {
                public void actionPerformed( ActionEvent ae )
                {
                    dispose();
                }
            } );
            
            javax.swing.GroupLayout layout = new javax.swing.GroupLayout( getContentPane() );
            getContentPane().setLayout(layout);
            layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(contents, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(btnAccept)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(btnCancel)))
                    .addContainerGap())
            );
            layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(contents, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGap(18, 18, 18)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnAccept)
                        .addComponent(btnCancel))
                    .addContainerGap())
            );
        }
    }

    public void add( DeskComponent dc )
    {
        add( (Component) dc );
    }

    public void remove( DeskComponent dc )
    {
        throw new UnsupportedOperationException( "Not supported yet." );
    }

    public void close()
    {
        throw new UnsupportedOperationException( "Not supported yet." );
    }
}