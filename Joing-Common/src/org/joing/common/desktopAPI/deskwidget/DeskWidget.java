/*
 * DeskWidget.java
 *
 * Created on 18 de septiembre de 2007, 0:13
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.joing.common.desktopAPI.deskwidget;

import org.joing.common.desktopAPI.pane.DeskCanvas;

/**
 * The base class for a kind of desktop gadgets: DeskLaunchers and DeskLets.
 * 
 * @author Francisco Morero Peyrona
 */
public interface DeskWidget extends DeskCanvas
{
    String getDescription();
    void   setDescription( String sDescription );
    
    // TODO: añadir aquí los métodos necesarios cuando tenga bien implementados
    //       los desk widgets (faltan los pijos).
}