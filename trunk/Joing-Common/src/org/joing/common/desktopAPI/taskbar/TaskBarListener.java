/*
 * TaskBarListener.java
 * 
 * Created on 11-sep-2007, 1:41:56
 * 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.joing.common.desktopAPI.taskbar;

import java.util.EventListener;
import org.joing.common.desktopAPI.DeskComponent;
import org.joing.common.desktopAPI.DeskContainer;

/**
 *
 * @author fmorero
 */
public interface TaskBarListener extends EventListener
{    
    public void componentAdded( DeskComponent dc );
    public void componentRemoved( DeskComponent dc );
    public void componentAdded( DeskContainer dc );
    public void componentRemoved( DeskContainer dc );
}