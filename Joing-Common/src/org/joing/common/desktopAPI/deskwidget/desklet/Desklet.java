/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.joing.common.desktopAPI.deskwidget.desklet;

import org.joing.common.desktopAPI.deskwidget.DeskWidget;

/**
 * A desklet is small applications that is shown on the desktop.
 * 
 * Classes that implement this interface should create an skelton that desklets
 * can extends filling the empty methods.
 * In this way, the functionality relies on the base class.
 * 
 * @author Francisco Morero Peyrona
 */
public interface Desklet extends DeskWidget
{   // FIXME: hay que definir este interface y hacer que PDEDesklet lo implemente
    /*protected void onShow();
    protected void onGrow();
    protected void onReduce();
    
    protected void onClose();
    
    
    protected void onSetup();
    
    protected void onDrag();
    
    protected void toogleSizeButton();
    
    // For user custom buttons
    protected void add( PDEDeskletButton button );

    // For user custom buttons
    protected void remove( PDEDeskletButton button );
    
    // For standard buttons
    protected void remove( PDEDesklet.ToolBarButton btn );
     */
}