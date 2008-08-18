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

import java.awt.Rectangle;
import java.util.Vector;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import org.joing.common.clientAPI.jvmm.App;
import org.joing.common.clientAPI.jvmm.AppListener;
        
/**
 *
 * Note: This class has package scope, to be instantiated only by SystemMonitor.
 *
 * @author  Francisco Morero Peyrona
 */
class ProcessesPanel extends JPanel implements AppListener
{
    /** Creates new form ProcessesPanel */
    ProcessesPanel()
    {
        initComponents();
        
        tblProcesses.setModel( new ThisTableModel() );
        tblProcesses.setSelectionMode( ListSelectionModel.SINGLE_SELECTION );
        tblProcesses.setRowSelectionAllowed( true );
        tblProcesses.setColumnSelectionAllowed( false );
        
        spTableProcesses.setViewportView( tblProcesses );
        
        // Add AppManager Listener
        org.joing.jvmm.RuntimeFactory.getPlatform().getAppManager().addAppListener( this );
    }
    
    //------------------------------------------------------------------------//
    // AppListener interface methods
    
    public void applicationAdded( App app )
    {
        ((ThisTableModel) tblProcesses.getModel()).addRow( app );
        selectRow( tblProcesses.getRowCount() - 1 );
    }

    public void applicationRemoved( App app )
    {
        ((ThisTableModel) tblProcesses.getModel()).delRow( app );
        
        if( tblProcesses.getSelectedRow() == -1 && tblProcesses.getRowCount() > 0 )
            selectRow( 0 );
    }
    
    //------------------------------------------------------------------------//
    
    private void selectRow( int nRow )
    {
        tblProcesses.setRowSelectionInterval( nRow, nRow );
        
        // Makes selected row to be visible
        Rectangle rect = tblProcesses.getCellRect( nRow, 0, true );
        spTableProcesses.getViewport().scrollRectToVisible( rect );
    }

    // Called from this.btnKillProcessActionPerformed(...)
    private void onKillProcess()
    {
        int nRow = tblProcesses.getSelectedRow();
        
        if( nRow != -1 )
        {
            App app  = vRows.get( nRow );
            org.joing.jvmm.RuntimeFactory.getPlatform().getAppManager().removeApp( app );
        }
    }
    
    //------------------------------------------------------------------------//
    // INNER CLASSES
    //------------------------------------------------------------------------//
    
    private Vector<App> vRows = new Vector<App>();  // It is not elegant but works
                
    private class ThisTableModel extends DefaultTableModel
    {
        private String[] asColName = { "ID", "Name", "Thread Group" };
        
        public String getColumnName( int nCol )
        {
            return asColName[ nCol ];
        }
        
        public int getRowCount()
        {
            return vRows.size();
        }
        
        public int getColumnCount()
        {
            return asColName.length;
        }
        
        public Object getValueAt( int nRow, int nCol )
        {
            Object ret = null;
            
            switch( nCol )
            {
                case 0: ret = vRows.get( nRow ).getApplication().getId();   break;
                case 1: ret = vRows.get( nRow ).getApplication().getName(); break;
                case 2: ret = vRows.get( nRow ).getThreadGroup().getName(); break;
            }
            
            return ret;
        }
        
        public boolean isCellEditable( int nRow, int nCol )
        {
            return false;
        }
        
        public void setValueAt( Object value, int nRow, int nCol )
        {
            // Table cells are read-only
        }
        
        public Class getColumnClass( int nCol )
        {
            Class clazz = Object.class;
            
            switch( nCol )
            {
                case 0: clazz = Integer.class; break;
                case 1: clazz = String.class;  break;
                case 2: clazz = String.class;  break;
            }
            
            return clazz;
        }
        
        private void addRow( App app )
        {                   
            vRows.add( app );
            fireTableRowsInserted( vRows.size(), vRows.size() );
        }
        
        private void delRow( App app )
        {
            int nRow = vRows.indexOf( app );
            
            if( nRow != -1 )
            {
                vRows.remove( nRow );
                fireTableRowsDeleted( nRow, nRow );                
            }
        }
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnKillProcess = new javax.swing.JButton();
        spTableProcesses = new javax.swing.JScrollPane();
        tblProcesses = new javax.swing.JTable();

        btnKillProcess.setMnemonic('K');
        btnKillProcess.setText("Kill Process");
        btnKillProcess.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnKillProcessActionPerformed(evt);
            }
        });

        tblProcesses.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        spTableProcesses.setViewportView(tblProcesses);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(spTableProcesses, javax.swing.GroupLayout.DEFAULT_SIZE, 376, Short.MAX_VALUE)
                    .addComponent(btnKillProcess, javax.swing.GroupLayout.Alignment.TRAILING))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(spTableProcesses, javax.swing.GroupLayout.DEFAULT_SIZE, 239, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnKillProcess)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

private void btnKillProcessActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnKillProcessActionPerformed
    onKillProcess();
}//GEN-LAST:event_btnKillProcessActionPerformed
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnKillProcess;
    private javax.swing.JScrollPane spTableProcesses;
    private javax.swing.JTable tblProcesses;
    // End of variables declaration//GEN-END:variables
}