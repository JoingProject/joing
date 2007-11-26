/*
 * Taskbar.java
 *
 * Created on 25 de abril de 2007, 11:56 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.joing.common.desktopAPI.taskbar;

import java.awt.Point;
import org.joing.common.desktopAPI.Closeable;
import org.joing.common.desktopAPI.DeskComponent;

/**
 *
 * @author Francisco Morero Peyrona
 */
public interface TaskBar extends Closeable
{
    public enum Orientation { TOP, BOTTOM, LEFT, RIGHT, FREE }
    
    void add( TaskBarPanel tbp );
    void add( TaskBarPanel tbp, Point pt );
    void add( DeskComponent dc );
    void add( DeskComponent dc, Point pt );
    void remove( TaskBarPanel tbp );
    void remove( DeskComponent dc );
    
    TaskBarPanel getSysTray();
    
    boolean isAutoHide();
    void setAutoHide( boolean autohide );
    
    Orientation getOrientation();
    void setOrientation( Orientation orientation );
    
    void addTaskBarListener( TaskBarListener tbl );
    void removeTaskBarListener( TaskBarListener tbl );
}