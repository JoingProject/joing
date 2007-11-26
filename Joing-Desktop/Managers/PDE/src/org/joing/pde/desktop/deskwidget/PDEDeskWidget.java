/*
 * PDEDeskWidget.java
 *
 * Created on 17 de septiembre de 2007, 19:05
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.joing.pde.desktop.deskwidget;

import java.awt.Point;
import javax.swing.JPopupMenu;
import org.joing.common.desktopAPI.deskwidget.DeskWidget;
import org.joing.pde.desktop.container.PDECanvas;

/**
 * A Deskgadget is every component shown in the dektop that can be dragged 
 * (can change its position by the user interaction).
 * <p>
 * DeskLaunchers and Desklets are subclased from here.
 *
 * @author Francisco Morero Peyrona
 */
public abstract class PDEDeskWidget extends PDECanvas implements DeskWidget
{
    /**
     * Creates a new instance of PDEDeskWidget
     */
    public PDEDeskWidget()
    {
        root.setGlassPane( new GlassPaneWidget( this ) );
    }
    
    public abstract void close();
    
    //------------------------------------------------------------------------//
    // Following methods from DeskWidget interface already exists in JPanel:
    // getName(), setName(...), getLocation(), setLocation(...), 
    
    protected void showPopup( Point ptWhere )
    {
        JPopupMenu popup = getComponentPopupMenu();
        
        if( popup != null && ! popup.isVisible() )
            popup.show( this, ptWhere.x, ptWhere.y );
    }    
}