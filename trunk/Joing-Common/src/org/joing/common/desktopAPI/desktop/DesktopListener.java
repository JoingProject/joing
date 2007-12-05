/*
 * DesktopListener.java
 * 
 * Created on 11-sep-2007, 0:57:04
 * 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.joing.common.desktopAPI.desktop;

import org.joing.common.desktopAPI.workarea.WorkArea;
import org.joing.common.desktopAPI.taskbar.TaskBar;

/**
 *
 * @author Francisco Morero Peyrona
 */
public interface DesktopListener
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