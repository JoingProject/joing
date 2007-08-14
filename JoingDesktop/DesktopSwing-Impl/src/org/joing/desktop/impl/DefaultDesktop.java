/*
 * DesktopImpl.java
 *
 * Created on 25 de abril de 2007, 12:19 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.joing.desktop.impl;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.util.LinkedList;
import java.util.List;
import javax.swing.JPanel;
import org.joing.desktop.api.Desktop;
import org.joing.desktop.api.DesktopManager;
import org.joing.desktop.api.WorkArea;
import org.joing.desktop.enums.TaskBarOrientation;
import org.joing.taskbar.api.TaskBar;
import org.joing.taskbar.impl.DefaultTaskBar;

/**
 *
 * @author mario
 */
public class DefaultDesktop extends JPanel implements Desktop{
    
    private List<WorkArea> workAreas;
    private List<TaskBar> taskBars;
    private WorkArea currentWorkArea;
    private JPanel workAreasContainer;
    
    /** Creates a new instance of DesktopImpl */
    public DefaultDesktop() {
        setLayout(new BorderLayout());
        
        workAreas = new LinkedList<WorkArea>();
        taskBars = new LinkedList<TaskBar>();
        workAreasContainer = new JPanel(new CardLayout());
        add(workAreasContainer,BorderLayout.CENTER);
        
        initDesktop();
        
        DesktopManager.setDesktop(this);
    }
    
    public List<WorkArea> getWorkAreas() {
        return workAreas;
    }
    
    public void addWorkArea(WorkArea workarea) {
        if(workarea!=null && workarea instanceof DefaultWorkArea){
            workAreas.add(workarea);
            add((DefaultWorkArea)workarea);
            currentWorkArea=workarea;
        }
    }
    
    public List<TaskBar> getTaskBars() {
        return taskBars;
    }
    
    public void addTaskBar(TaskBar taskbar) {
        if(taskbar!=null && taskbar instanceof DefaultTaskBar){
            taskBars.add(taskbar);
            
            DefaultTaskBar swingTB = (DefaultTaskBar) taskbar;            
            switch(taskbar.getOrientation()){
                case BOTTOM: add(swingTB,BorderLayout.SOUTH); break;
                case TOP: add(swingTB,BorderLayout.NORTH); break;
                case LEFT: add(swingTB,BorderLayout.WEST); break;
                case RIGHT: add(swingTB,BorderLayout.EAST); break;
            }
        }
    }
    
    public WorkArea getCurrentWorkArea() {
        return currentWorkArea;
    }

    private void initDesktop() {
        TaskBar taskBar = new DefaultTaskBar();        
        addTaskBar(taskBar);
        TaskBar taskBar2 = new DefaultTaskBar();
        taskBar2.setOrientation(TaskBarOrientation.TOP);
        addTaskBar(taskBar2);
        
        WorkArea workArea = new DefaultWorkArea();
        addWorkArea(workArea);
    }
    
}
