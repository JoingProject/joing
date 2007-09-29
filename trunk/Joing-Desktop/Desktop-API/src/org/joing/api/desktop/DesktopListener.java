/*
 * DesktopListener.java
 * 
 * Created on 11-sep-2007, 0:57:04
 * 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.joing.api.desktop;

import org.joing.api.desktop.workarea.WorkArea;
import java.util.EventListener;
import org.joing.api.desktop.taskbar.TaskBar;

/**
 *
 * @author Francisco Morero Peyrona
 */
public interface DesktopListener extends EventListener
{
    /**
     * Invoked when a WorkArea has been added to the Desktop.
     */
    public void workAreaAdded( WorkArea wa );
    
    /**
     * Invoked when a WorkArea has been removed from the Desktop.
     */
    public void workAreaRemoved( WorkArea wa );
    
    /**
     * Invoked when selecetd (active) WorkArea changes.
     */  
    public void workAreaSelected( WorkArea waPrevious, WorkArea waCurrent );

    /**
     * Invoked when a TaskBar has been added to the Desktop.
     */
    public void taskBarAdded( TaskBar tb );

    /**
     * Invoked when a TaskBar has been removed from the Desktop.
     */    
    public void taskBarRemoved( TaskBar tb );
}