/*
 * Taskbar.java
 *
 * Created on 25 de abril de 2007, 11:56 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.joing.taskbar.api;

import java.util.List;
import org.joing.desktop.enums.TaskBarOrientation;

/**
 *
 * @author mario
 */
public interface TaskBar {
    
    public void addPanel(TaskBarPanel panel);
    public List<TaskBarPanel> getPanels();
    public void setExpanded(boolean expanded);
    public boolean isExpanded();
    public void setAutoHide(boolean autohide);
    public boolean isAutoHide();
    public void setHeightSize(int size);
    public int getHeightSize();
    public void setOrientation(TaskBarOrientation orientation);
    public TaskBarOrientation getOrientation();
    
}
