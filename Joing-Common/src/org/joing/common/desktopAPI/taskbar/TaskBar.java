/*
 * Taskbar.java
 *
 * Created on 25 de abril de 2007, 11:56 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.joing.common.desktopAPI.taskbar;

import org.joing.common.desktopAPI.Closeable;
import org.joing.common.desktopAPI.DeskComponent;

/**
 *
 * @author Francisco Morero Peyrona
 */
public interface TaskBar extends Closeable
{
    enum Orientation { TOP, BOTTOM, LEFT, RIGHT, FREE }
    
    void add( TaskBarPanel tbp );
    void add( TaskBarPanel tbp, int x, int y );
    void add( TaskBarComponent dc );
    void add( TaskBarComponent dc, int x, int y );
    void remove( TaskBarPanel tbp );
    void remove( TaskBarComponent dc );
    
    int  getWidth();
    int  getHeight();
    
    TaskBarComponent getSysTray();
    
    boolean isAutoHide();
    void setAutoHide( boolean autohide );
    
    Orientation getOrientation();
    void setOrientation( Orientation orientation );
    
    void addTaskBarListener( TaskBarListener tbl );
    void removeTaskBarListener( TaskBarListener tbl );
}