/*
 * SystemMonitorr.java
 *
 * Created on 24 de junio de 2007, 19:11
 */

package org.joing.applauncher.gui;

import javax.swing.JPanel;
import org.joing.applauncher.gui.memory.MemoryPanel;

/**
 *
 * @author  fmorero
 */
public class SystemMonitor extends javax.swing.JPanel
{
    private ProcessesPanel  processesPanel;
    private MemoryPanel     memoryPanel;
    private NetworkPanel    networkPanel;
    private FileSystemPanel fileSystemPanel;
    private LogPanel        logPanel;
    
    //------------------------------------------------------------------------//
    
    /** Creates new form SystemMonitorr */
    public SystemMonitor()
    {
        initComponents();
        
        processesPanel  = new ProcessesPanel();
        memoryPanel     = new MemoryPanel();
        networkPanel    = new NetworkPanel();
        fileSystemPanel = new FileSystemPanel();
        logPanel        = new LogPanel();
        
        tabbedPane.addTab( "Processes" , processesPanel  );
        tabbedPane.addTab( "Memory"    , memoryPanel     );
        tabbedPane.addTab( "Network"   , networkPanel    );
        tabbedPane.addTab( "FileSystem", fileSystemPanel );
        tabbedPane.addTab( "Log"       , logPanel        );
    }
    
    /**
     * Utility method. Should not be invoked outside Monitor class.
     */
    public void addLogMessage(String s) {
        logPanel.addLine(s);
    }
    
//    public static void main( String[] args )
//    {
//        SwingUtilities.invokeLater( new Runnable() 
//        {
//            public void run()
//            {
//                SystemMonitor sm = new SystemMonitor();
//                
//                JFrame frm = new JFrame( "Testing System Monitor" );
//                       frm.getContentPane().add( sm );
//                       frm.pack();
//                       frm.setLocationRelativeTo( null );
//                       frm.setVisible( true );
//                       frm.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
//            }
//        } );
//    }
    
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