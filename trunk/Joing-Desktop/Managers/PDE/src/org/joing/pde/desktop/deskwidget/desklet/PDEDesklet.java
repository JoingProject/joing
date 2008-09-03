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
package org.joing.pde.desktop.deskwidget.desklet;

import java.awt.Component;
import java.awt.Dimension;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import org.joing.kernel.api.desktop.Closeable;
import org.joing.kernel.api.desktop.workarea.WorkArea;
import org.joing.pde.desktop.deskwidget.PDEDeskWidget;
import org.joing.kernel.swingtools.JoingSwingUtilities;

/**
 * This is the base class to create DeskApplets: (normally) small applications
 * that are shown on the desktop.
 * <p>
 * There are two ways to create a DeskApplet:
 * <ul>
 * <li>
 * Inheriting from this class. Which allows to use all events.
 * </li>
 * <li>
 * Creating an instance of this class and inserting the Desklet into a JPanel:<br>
 * <pre>
 * MyPanel myPanel = new MyPanel();
 * PDEDeskApplet deskApplet = new PDEDeskApplet();
 *               deskApplet.add( myPanel );
 *               deskApplet.setBounds( 10,10, myPanel.getPreferredSize().width, 
 *                                            myPanel.getPreferredSize().height );
 * DesktopManagerFactory.getDesktopManager().getDesktop().getActiveWorkArea().add( deskApplet );
 * </pre>
 * </li>
 * </ul>
 * @author Francisco Morero Peyrona
 */
public abstract class PDEDesklet extends PDEDeskWidget implements Closeable
{// TODO: Esta clase y el interface que la representa hay que planteárselos enteritos
    private DeskAppletToolBar toolBar;
    
    public PDEDesklet()
    {
        init();
    }
    
    public Dimension getMinimumSize()
    {
        Dimension dimMin = toolBar.getMinimumSize();
                  dimMin.width *= 2;
        
        return dimMin;
    }
    
    public void close()
    {
        onClose();
    }
    
    // TODO: Mirar aquí 
    // http://msdn.microsoft.com/msdnmag/issues/07/08/SideBar/default.aspx?loc=es
    //------------------------------------------------------------------------//
    
    protected void onShow()
    {
        // Empty
    }
    
    protected void onHide()
    {
        // Empty
    }
    
    protected void onGrow()
    {
        // Empty
    }
    
    protected void onReduce()
    {
        // Empty
    }
    
    /**
     * As this class just removes the DeskApplet from Subclasses should overwrite this method
     */
    protected void onClose()
    {
        WorkArea wa = JoingSwingUtilities.findWorkAreaFor( this );
                 wa.remove( this );
        ((Component) wa).repaint( getX(), getY(), getWidth(), getHeight() );
    }
    
    protected void onSetup()
    {
        // Empty
    }
    
    protected void toogleSizeButton()
    {
        // TODO; mirar esto --> toolBar.onSize();
    }
    
    // For user custom buttons
    protected void add( PDEDeskletButton button )
    {
        toolBar.add( button );
    }

    // For user custom buttons
    protected void remove( PDEDeskletButton button )
    {
        toolBar.remove( button );
    }
    
    // For standard buttons
    protected void remove( PDEDesklet.ToolBarButton btn )
    {
        toolBar.remove( btn );
    }
    
    //------------------------------------------------------------------------//
    
    void setToolBarVisible( boolean b )
    {
        toolBar.setVisible( b );
    }
    
    //------------------------------------------------------------------------//
    
    private void init()
    {
        toolBar = new DeskAppletToolBar( this );
        Dimension dimTB = toolBar.getMinimumSize();
        toolBar.setBounds( 0, 0, dimTB.width, dimTB.height );
        toolBar.setVisible( false );
        
        // Has to be added to root pane
        add( toolBar );
        setComponentZOrder( toolBar, 0 );
        ///////setGlassPane( new GlassPaneDesklet( this ) );
        
        addAncestorListener( new AncestorListener()
        {
            public void ancestorAdded(   AncestorEvent ae ) { PDEDesklet.this.onShow(); }
            public void ancestorMoved(   AncestorEvent ae ) {}
            public void ancestorRemoved( AncestorEvent ae ) { PDEDesklet.this.onHide(); }
        } );
    }

    //------------------------------------------------------------------------//
    // INNER CLASS: Enumeration
    //------------------------------------------------------------------------//

    public enum ToolBarButton
    {
        SIZE, CLOSE, SETUP;
    }
}