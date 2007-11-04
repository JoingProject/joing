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
import java.awt.FlowLayout;
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
import org.joing.pde.desktop.container.PDEFrame;
import org.joing.pde.runtime.PDERuntime;

/**
 * The base class for most part of the widtgets that are shown in the TaskBar.
 * 
 * @author Francisco Morero Peyrona
 */
public abstract class TaskPanel extends JPanel implements MouseListener
{
    private JLabel handle;
    
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
        handle.setVisible( b );
    }

    //------------------------------------------------------------------------//
    
    // Mouse Listener is needed to show the popup. 
    // See: http://www.jguru.com/forums/view.jsp?EID=1239349
    public void mouseClicked(MouseEvent me)
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
        
        if( pnlPrefs == null )
            return;
        
        JButton btnAccept = new JButton( "Accept" );
        JButton btnCancel = new JButton( "Cancel" );
        JPanel  pnlButton = new JPanel( new FlowLayout( FlowLayout.LEADING, 5, 0 ) );
        JPanel  pnlDialog = new JPanel( new BorderLayout( 0,6 ) );

        pnlButton.add( btnAccept );
        pnlButton.add( btnCancel );
        
        pnlDialog.add( pnlPrefs , BorderLayout.CENTER );
        pnlDialog.add( pnlButton, BorderLayout.SOUTH  );

        // Better to use a Frame than a Dialog (this is the way Gnome does it)
        PDEFrame frame = new PDEFrame( "Preferences" );
                 frame.setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );
                 frame.getContentPane().add( pnlDialog );

        PDERuntime.getRuntime().add( frame );
        // TODO: Al cerrar la dialog, si se hizo click en aceptar, procesar el panel de Porperties
    }
    
    private void onAbout()
    {
        JPanel pnlAbout = getAboutPanel();
        
        if( pnlAbout == null )
        {
            pnlAbout = new JPanel();
            pnlAbout.add( new JLabel( "There is no information bout this component." ) );
        }
        
        // Better to use a Frame than a Dialog (this is the way Gnome does it)
        PDEFrame frame = new PDEFrame( "About" );
                 frame.setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );
                 frame.getContentPane().add( pnlAbout );
                 
        PDERuntime.getRuntime().add( frame );
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
        private final static String PREFERENCES = "A";
        private final static String ABOUT       = "B";
        private final static String REMOVE      = "C";
        private final static String MOVE        = "D";
        
        //--------------------------------------------------------------------//
        
        private CommonPopupMenu()
        {
            JMenuItem item;
            
            item = new JMenuItem( "Preferences" );
            item.setActionCommand( PREFERENCES );
            item.addActionListener( this );
            add( item );
            
            addSeparator();
            
            item = new JMenuItem( "About" );
            item.setActionCommand( ABOUT );
            item.addActionListener( this );
            add( item );
            
            addSeparator();
            
            item = new JMenuItem( "Remove from panel" );
            item.setActionCommand( REMOVE );
            item.addActionListener( this );
            add( item );
            
            item = new JMenuItem( "Move" );
            item.setActionCommand( MOVE );
            item.addActionListener( this );
            add( item );
        }
        
        public void actionPerformed( ActionEvent ae )
        {
            String sCommand = ae.getActionCommand();
            
            if(      sCommand.equals( PREFERENCES ) )  onPreferences();
            else if( sCommand.equals( ABOUT       ) )  onAbout();
            else if( sCommand.equals( REMOVE      ) )  onRemove();
            else if( sCommand.equals( MOVE        ) )  onMove();
        }
    }
}