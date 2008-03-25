/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.joing.common.desktopAPI.pane;

import java.awt.Image;
import org.joing.common.desktopAPI.DeskComponent;
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
    
    Image getIcon();
    void  setIcon( Image image );
    
    void setLocationRelativeTo( DeskComponent parent );
}