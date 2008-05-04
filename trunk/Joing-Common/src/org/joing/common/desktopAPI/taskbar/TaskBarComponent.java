/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.joing.common.desktopAPI.taskbar;

import org.joing.common.desktopAPI.DeskComponent;

/**
 *
 * @author Francisco Morero Peyrona
 */
public interface TaskBarComponent extends DeskComponent
{
    boolean isLocked();
    void    setLocked( boolean b );
    
    void onPreferences();
    void onAbout();
    void onRemove();      // Remove from parent
    void onMove();
    
    void addTaskBarListener( TaskBarListener tbl );
    void removeTaskBarListener( TaskBarListener tbl );
}