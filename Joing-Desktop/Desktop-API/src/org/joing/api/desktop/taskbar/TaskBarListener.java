/*
 * TaskBarListener.java
 * 
 * Created on 11-sep-2007, 1:41:56
 * 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.joing.api.desktop.taskbar;

import java.awt.Component;
import java.util.EventListener;

/**
 *
 * @author fmorero
 */
public interface TaskBarListener extends EventListener
{    
    public void componentAdded( Component component );
    public void componentRemoved( Component component );
}