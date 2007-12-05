/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.joing.common.desktopAPI.pane;

import org.joing.common.desktopAPI.Selectable;

/**
 * Common interface to Frames and Dialogs.
 * 
 * @author Francisco Morero Peyrona
 */
public interface DeskWindow extends DeskPane, Selectable
{
    boolean isResizable();
    void    setResizable( boolean b );
    
    String getTitle();
    void   setTitle( String sTitle );
    
    byte[] getIcon();
    void   setIcon( byte[] image );
}