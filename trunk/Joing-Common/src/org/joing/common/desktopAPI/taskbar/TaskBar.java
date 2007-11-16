/*
 * Taskbar.java
 *
 * Created on 25 de abril de 2007, 11:56 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.joing.common.desktopAPI.taskbar;

import java.awt.Component;
import java.awt.Point;
import java.util.List;
import org.joing.common.desktopAPI.enums.TaskBarOrientation;

/**
 *
 * @author mario
 */
public interface TaskBar
{    
    public Component       add( Component component );
    public Component       add( Component component, Point pt );
    public void            remove( Component component );
    public List<Component> getOfType( Class clazz );
    
    public boolean isAutoHide();
    public void setAutoHide( boolean autohide );
    
    public TaskBarOrientation getOrientation();
    public void setOrientation( TaskBarOrientation orientation );
    
    public void addTaskBarListener( TaskBarListener tbl );
    public void removeTaskBarListener( TaskBarListener tbl );
}