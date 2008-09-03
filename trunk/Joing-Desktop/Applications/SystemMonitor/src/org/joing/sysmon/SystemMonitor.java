/*
 * Copyright (C) 2007, 2008 Join'g Team Members. All Rights Reserved.
 * Join'g Team Members are listed at project's home page. By the time of 
 * writting this at: https://joing.dev.java.net/servlets/ProjectMemberList.
 *
 * This file is part of Join'g project: www.joing.org
 *
 * GNU Classpath is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the free
 * Software Foundation; either version 3, or (at your option) any later version.
 * 
 * GNU Classpath is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * GNU Classpath; see the file COPYING.  If not, write to the Free Software 
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */
package org.joing.sysmon;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import org.joing.kernel.api.desktop.DeskComponent;
import org.joing.kernel.api.desktop.DesktopManager;
import org.joing.kernel.api.desktop.pane.DeskFrame;
import org.joing.sysmon.memory.MemoryPanel;

/**
 * A graphical way to monitorize Join'g and the applications launched.
 * 
 * @author  Francisco Morero Peyrona
 */
public class SystemMonitor extends JPanel implements DeskComponent
{
    private ProcessesPanel  processesPanel;
    private MemoryPanel     memoryPanel;
    private NetworkPanel    networkPanel;
    private FileSystemPanel fileSystemPanel;
    private PropertiesPanel propertiesPanel;
    private LogPanel        logPanel;
    private AboutPanel      aboutPanel;
    
    //------------------------------------------------------------------------//
    
    /** Creates new form SystemMonitorr */
    public SystemMonitor()
    {
        initComponents();
        
        processesPanel  = new ProcessesPanel();
        memoryPanel     = new MemoryPanel();
        networkPanel    = new NetworkPanel();
        fileSystemPanel = new FileSystemPanel();
        propertiesPanel = new PropertiesPanel();
        logPanel        = new LogPanel();
        aboutPanel      = new AboutPanel();
        
        tabbedPane.addTab( "Processes" , processesPanel  );
        tabbedPane.addTab( "Memory"    , memoryPanel     );
        tabbedPane.addTab( "Network"   , networkPanel    );
        tabbedPane.addTab( "FileSystem", fileSystemPanel );
        tabbedPane.addTab( "Properties", propertiesPanel );
        tabbedPane.addTab( "Log"       , logPanel        );
        tabbedPane.addTab( "About"     , aboutPanel      );
    }
    //------------------------------------------------------------------------//
    
    public static void main( String[] asArg )
    {
        (new SystemMonitor()).showInFrame();
    }
    
    public void showInFrame()
    {
        DesktopManager dm   = org.joing.kernel.jvmm.RuntimeFactory.getPlatform().getDesktopManager();
        ImageIcon      icon = new ImageIcon( getClass().getResource( "system_monitor.png" ) );

        // Show this panel in a frame created by DesktopManager Runtime.
        DeskFrame frame = dm.getRuntime().createFrame();
                  frame.setTitle( "System Monitor" );
                  frame.setIcon( icon.getImage() );
                  frame.add( (DeskComponent) SystemMonitor.this );

        dm.getDesktop().getActiveWorkArea().add( frame );
    }
    
    //------------------------------------------------------------------------//
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {

        tabbedPane = new javax.swing.JTabbedPane();

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(tabbedPane, javax.swing.GroupLayout.DEFAULT_SIZE, 376, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(tabbedPane, javax.swing.GroupLayout.DEFAULT_SIZE, 276, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents
        
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTabbedPane tabbedPane;
    // End of variables declaration//GEN-END:variables
}