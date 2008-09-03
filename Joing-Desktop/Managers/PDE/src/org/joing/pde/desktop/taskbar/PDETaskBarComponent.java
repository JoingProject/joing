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
package org.joing.pde.desktop.taskbar;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import org.joing.kernel.api.desktop.StandardImage;
import org.joing.kernel.api.desktop.taskbar.TaskBarComponent;
import org.joing.kernel.api.desktop.taskbar.TaskBarListener;
import org.joing.pde.desktop.PDEDeskComponent;

/**
 * Base class for all TaskBar components.
 * 
 * @author Francisco Morero Peyrona
 */
public abstract class PDETaskBarComponent extends PDEDeskComponent implements TaskBarComponent
{
    // TODO: Las subclases de esta clase puede acceder a los items del popup directamente porque son protected.
    //       Sin embargo, hay que implementar un mecanismo para que componentes de terceros puedan servir tanto
    //       en PDE como en otros desktops.
    
    private boolean bLocked;
    
    // Popup menu items
    protected JMenuItem itemPreferences;
    protected JMenuItem itemAbout;
    protected JMenuItem itemRemove;
    protected JMenuItem itemMove;
    protected JMenuItem itemLock;
    
    //------------------------------------------------------------------------//
    
    public PDETaskBarComponent()
    {
        bLocked = false;
        setComponentPopupMenu( new CommonPopupMenu() );    // It is inherited by sub-components
    }
    
    //------------------------------------------------------------------------//
    // PDETaskBarComponent interface
    
    public boolean isLocked()
    {
        return bLocked;
    }
    
    public void setLocked( boolean b )
    {
        bLocked = b;
        itemLock.setText( b ? "Unlock" : "Lock" );
        itemMove.setEnabled( ! b );
    }
    
    public void addTaskBarListener( TaskBarListener tbl )
    {
        throw new UnsupportedOperationException( "Not supported yet." );
    }

    public void removeTaskBarListener( TaskBarListener tbl )
    {
        throw new UnsupportedOperationException( "Not supported yet." );
    }
    
    public abstract void onAbout();
    public abstract void onRemove();
    public abstract void onMove();
    public abstract void onPreferences();
    
    //------------------------------------------------------------------------//
    // INNER CLASS: Popup Menu
    //------------------------------------------------------------------------//
    private final class CommonPopupMenu extends JPopupMenu implements ActionListener
    { 
        private CommonPopupMenu()
        {   
            itemPreferences = new JMenuItem( "Preferences" );
            itemPreferences.addActionListener( this );
            itemPreferences.setIcon( new ImageIcon( org.joing.kernel.jvmm.RuntimeFactory.getPlatform().getDesktopManager().getRuntime().getImage( StandardImage.PROPERTIES, 16, 16 ) ) );
            add( itemPreferences );
            
            itemAbout = new JMenuItem( "About" );
            itemAbout.addActionListener( this );
            itemAbout.setIcon( new ImageIcon( org.joing.kernel.jvmm.RuntimeFactory.getPlatform().getDesktopManager().getRuntime().getImage( StandardImage.INFO, 16, 16 ) ) );
            add( itemAbout );
            
            addSeparator();
            
            itemRemove = new JMenuItem( "Remove" );
            itemRemove.addActionListener( this );
            itemRemove.setIcon( new ImageIcon( org.joing.kernel.jvmm.RuntimeFactory.getPlatform().getDesktopManager().getRuntime().getImage( StandardImage.REMOVE, 16, 16 ) ) );
            add( itemRemove );
            
            itemMove = new JMenuItem( "Move" );
            itemMove.addActionListener( this );
            itemMove.setIcon( new ImageIcon( org.joing.kernel.jvmm.RuntimeFactory.getPlatform().getDesktopManager().getRuntime().getImage( StandardImage.MOVE, 16, 16 ) ) );
            itemMove.setEnabled( ! isLocked() );
            add( itemMove );
            
            itemLock = new JMenuItem( isLocked() ? "Unlock" : "Lock" );
            itemLock.addActionListener( this );
            itemLock.setIcon( new ImageIcon( org.joing.kernel.jvmm.RuntimeFactory.getPlatform().getDesktopManager().getRuntime().getImage( StandardImage.LOCK, 16, 16 ) ) );
            add( itemLock );
        }
        
        public void actionPerformed( ActionEvent ae )
        {
            JMenuItem source = (JMenuItem) ae.getSource();
            
            if(      source == itemPreferences )  onPreferences();
            else if( source == itemAbout       )  onAbout();
            else if( source == itemRemove      )  onRemove();
            else if( source == itemMove        )  onMove();
            else if( source == itemLock        )  setLocked( ! isLocked() );
        }
    }
}