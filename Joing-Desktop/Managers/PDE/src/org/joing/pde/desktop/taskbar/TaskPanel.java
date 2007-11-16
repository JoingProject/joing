/*
 * TaskPanel.java
 *
 * Created on 15 de septiembre de 2007, 22:07
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.joing.pde.desktop.taskbar;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import org.joing.pde.PDEManager;
import org.joing.pde.desktop.container.PDEFrame;

/**
 * The base class for most part of the widtgets that are shown in the TaskBar.
 * 
 * @author Francisco Morero Peyrona
 */
public abstract class TaskPanel extends JPanel implements MouseListener
{
    private JLabel handle;
    
    // Popup menu items
    protected JMenuItem itemPreferences;
    protected JMenuItem itemAbout;
    protected JMenuItem itemRemove;
    protected JMenuItem itemMove;
    
    //------------------------------------------------------------------------//
    
    /** Creates a new instance of TaskPanel */
    public TaskPanel()
    {
        handle = new JLabel( "||" );
        
        setOpaque( false );
        addMouseListener( this );
        add( handle, BorderLayout.EAST );
        setComponentPopupMenu( new CommonPopupMenu() );    // It is inherited by sub-components
    }
    
    public boolean isHandleVisible()
    {
        return handle.isVisible();
    }

    public void setHandleVisible( boolean b )
    {
        if( b != isHandleVisible() )
        {
            if( b )
                add( handle, BorderLayout.EAST );
            else
                remove( handle );    // If I do not remove it a horrible blank space remains in the component
            
            handle.setVisible( b );
        }
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
    
    protected abstract JPanel getAboutPanel();
    
    protected abstract JPanel getPreferencesPanel();
    
    protected abstract void onPreferencesChanged( JPanel pnlPrefs );
        
    //------------------------------------------------------------------------//
    
    private void onPreferences()
    {
        JPanel pnlPrefs = getPreferencesPanel();
        
        if( pnlPrefs != null )
        {
            PDEFrame frame = new TheFrame( pnlPrefs );
                     frame.pack();
                     frame.center();
                     frame.setVisible( true );
            PDEManager.getInstance().getDesktop().add( frame );
        }
    }
    
    private void onAbout()
    {
        JPanel pnlAbout = getAboutPanel();
        
        if( pnlAbout == null )
        {
            pnlAbout = new JPanel();
            pnlAbout.add( new JLabel( "There is no information about this component." ) );
        }
        
        // Better to use a Frame than a Dialog (this is the way Gnome does it)
        PDEFrame frame = new PDEFrame( "About" );
                 frame.setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );
                 frame.getContentPane().add( pnlAbout );
                 frame.pack();
                 frame.center();
                 frame.setVisible( true );
        PDEManager.getInstance().getDesktop().add( frame );
    }
    
    private void onRemove()
    {   // TODO: Hacerlo
        throw new UnsupportedOperationException( "Operation not supported yet." );
    }
    
    private void onMove()
    {   // TODO: Hacerlo
        throw new UnsupportedOperationException( "Operation not supported yet." );
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
            itemPreferences.setIcon( PDEManager.getInstance().getRuntime().getIcon( null, "properties.png", 16, 16 ) );
            add( itemPreferences );
            
            itemAbout = new JMenuItem( "About" );
            itemAbout.addActionListener( this );
            itemAbout.setIcon( PDEManager.getInstance().getRuntime().getIcon( null, "info.png", 16, 16 ) );
            add( itemAbout );
            
            addSeparator();
            
            itemRemove = new JMenuItem( "Remove" );
            itemRemove.addActionListener( this );
            itemRemove.setIcon( PDEManager.getInstance().getRuntime().getIcon( null, "remove.png", 16, 16 ) );
            add( itemRemove );
            
            itemMove = new JMenuItem( "Move" );
            itemMove.addActionListener( this );
            itemMove.setIcon( PDEManager.getInstance().getRuntime().getIcon( null, "move.png", 16, 16 ) );
            add( itemMove );
        }
        
        public void actionPerformed( ActionEvent ae )
        {
            JMenuItem source = (JMenuItem) ae.getSource();
            
            if(      source == itemPreferences )  onPreferences();
            else if( source == itemAbout       )  onAbout();
            else if( source == itemRemove      )  onRemove();
            else if( source == itemMove        )  onMove();
        }
    }
    
    //------------------------------------------------------------------------//
    // INNER CLASS: Default empty frame with [Accept] and [Cancel] buttons
    //------------------------------------------------------------------------//
    
    private final class TheFrame extends PDEFrame
    {
        private JButton btnAccept;
        private JButton btnCancel;
        private JPanel  pnlContents;
        
        TheFrame( JPanel pnl )
        {
            pnlContents = pnl;
            btnAccept   = new JButton( "Accept" );
            btnCancel   = new JButton( "Cancel" );

            btnAccept.addActionListener( new ActionListener()
            {
                public void actionPerformed( ActionEvent ae )
                {
                    dispose();
                    onPreferencesChanged( TheFrame.this.pnlContents );
                }
            } );
        
            btnCancel.addActionListener( new ActionListener() 
            {
                public void actionPerformed( ActionEvent ae )
                {
                    dispose();
                }
            } );
            
            setDefaultCloseOperation( javax.swing.WindowConstants.DISPOSE_ON_CLOSE );

            javax.swing.GroupLayout layout = new javax.swing.GroupLayout( getContentPane() );
            getContentPane().setLayout(layout);
            layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(pnlContents, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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
                    .addComponent(pnlContents, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGap(18, 18, 18)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnAccept)
                        .addComponent(btnCancel))
                    .addContainerGap())
            );
        }
    }
}