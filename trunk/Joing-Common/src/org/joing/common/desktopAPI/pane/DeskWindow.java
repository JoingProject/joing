/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.joing.common.desktopAPI.pane;

import java.awt.Image;

/**
 * Common interface to Frames and Dialogs.
 * 
 * @author Francisco Morero Peyrona
 */
public interface DeskWindow extends DeskPane
{
    boolean isSelected();
    void    setSelected( boolean b );
    
    boolean isResizable();
    void    setResizable( boolean b );
    
    String getTitle();
    void   setTitle( String sTitle );
    
    Image getIcon();
    void  setIcon( Image icon );
}