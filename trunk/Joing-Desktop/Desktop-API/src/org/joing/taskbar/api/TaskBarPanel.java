/*
 * TaskbarPanel.java
 *
 * Created on 25 de abril de 2007, 11:56 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.joing.taskbar.api;

import java.util.List;
import org.joing.desktop.api.Launcher;
import org.joing.desktop.enums.TaskBarOrientation;

/**
 *
 * @author mario
 */
public interface TaskBarPanel {
    
    public void addObject(TaskBarObject object);
    public void addLauncher(Launcher launcher);
    public List<TaskBarObject> getObjects();
}
