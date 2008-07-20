/*
 * TaskBarListener.java
 * 
 * Created on 11-sep-2007, 1:41:56
 * 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.joing.common.desktopAPI.taskbar;

import org.joing.common.desktopAPI.DeskComponent;
import org.joing.common.desktopAPI.DeskContainer;

/**
 *
 * @author Francisco Morero Peyrona
 */
public interface TaskBarListener
{    
    public void componentAdded( DeskComponent dc );
    public void componentRemoved( DeskComponent dc );
    public void containerAdded( DeskContainer dc );
    public void containerRemoved( DeskContainer dc );
}