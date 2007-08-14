/*
 * DefaultTaskBarPanel.java
 *
 * Created on 20 de junio de 2007, 08:57 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.joing.taskbar.impl;

import java.awt.FlowLayout;
import java.util.List;
import javax.swing.JComponent;
import javax.swing.JPanel;
import org.joing.desktop.api.Launcher;
import org.joing.taskbar.api.TaskBarObject;
import org.joing.taskbar.api.TaskBarPanel;

/**
 *
 * @author Mario Serrano Leones
 */
public class DefaultTaskBarPanel extends JPanel implements TaskBarPanel{
    
    /** Creates a new instance of DefaultTaskBarPanel */
    public DefaultTaskBarPanel() {
        setLayout(new FlowLayout(FlowLayout.LEFT));
        setOpaque(false);
    }

    public void addObject(TaskBarObject object) {
    }

    public List<TaskBarObject> getObjects() {
        return null;
    }

    public void addLauncher(Launcher launcher) {
    }
    
    public void addJComponent(JComponent jcomp){
        add(jcomp);
    }
    
}
