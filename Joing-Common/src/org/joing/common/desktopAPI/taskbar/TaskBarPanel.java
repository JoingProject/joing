/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.joing.common.desktopAPI.taskbar;

import java.awt.Point;
import org.joing.common.desktopAPI.DeskComponent;
import org.joing.common.desktopAPI.DeskContainer;

/**
 *
 * @author Francisco Morero Peyrona
 */
public interface TaskBarPanel extends DeskContainer
{
    void add( DeskComponent dc );
    void add( DeskComponent dc, Point pt );
    void remove( DeskComponent dc );
    
    boolean isLocked();
    void    setLocked( boolean b );
    
    void addTaskBarPanelListener( TaskBarListener tbl );
    void removeTaskBarPanelListener( TaskBarListener tbl );
}