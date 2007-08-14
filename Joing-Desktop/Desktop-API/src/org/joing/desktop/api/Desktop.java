/*
 * Desktop.java
 *
 * Created on 25 de abril de 2007, 11:54 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.joing.desktop.api;

import java.util.List;
import org.joing.taskbar.api.TaskBar;

/**
 *
 * @author Mario Serrano
 */
public interface Desktop {    
  
    public List<WorkArea> getWorkAreas();
    public void addWorkArea(WorkArea workarea);
    
    public List<TaskBar> getTaskBars();
    public void addTaskBar(TaskBar taskbar);  
    
    public WorkArea getCurrentWorkArea();
    
    //TODO: Faltan muchas cosas
}
