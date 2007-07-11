/*
 * ProcessesPanel.java
 *
 * Created on 24 de junio de 2007, 17:16
 */

package net.java.joing.jvmm.gui;

import java.awt.Rectangle;
import java.util.Vector;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import net.java.joing.jvmm.AppLauncherListener;
import net.java.joing.jvmm.AppStartedEvent;
import net.java.joing.jvmm.AppStatusChangedEvent;
import net.java.joing.jvmm.AppStoppedEvent;

/**
 *
 * Note: This class has package scope, therefore it can be instantiated only by
 * SystemMonitor.
 * 
 * @author  fmorero
 */
class ProcessesPanel extends JPanel implements AppLauncherListener
{
    private JTable tblProcesses;
    
    /** Creates new form ProcessesPanel */
    ProcessesPanel()
    {
        initComponents();
        
        tblProcesses = new JTable();
        tblProcesses.setModel( new ThisTableModel() );
        tblProcesses.setSelectionMode( ListSelectionModel.SINGLE_SELECTION );
        tblProcesses.setRowSelectionAllowed( true );
        tblProcesses.setColumnSelectionAllowed( false );
        
        spTableProcesses.setViewportView( tblProcesses );
    }
    
    //------------------------------------------------------------------------//
    // AppLauncherListener interface methods
    
    public void appStarted( AppStartedEvent ase )
    {
        ((ThisTableModel) tblProcesses.getModel()).addRow( ase.getPID(), ase.getName() );
        
        selectRow( tblProcesses.getRowCount() - 1 );
    }
    
    public void appStopped( AppStoppedEvent ase )
    {
        ((ThisTableModel) tblProcesses.getModel()).delRow( ase.getPID() );
        
        if( tblProcesses.getSelectedRow() == -1 )
            selectRow( tblProcesses.getRowCount() - 1 );
    }
    
    public void appStatusChanged( AppStatusChangedEvent ase )
    {
        ((ThisTableModel) tblProcesses.getModel()).changeRow(  ase.getPID(), ase.getCPU(), ase.getMemory() );
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
        int nPID = (Integer) tblProcesses.getModel().getValueAt( nRow, 0 );
        
        // TODO: esto se puede implementar de muchas maneras, pero la más elegante
        //       a mi modo de ver es que desde aquí se llame al API del AppLauncher.
        //       Algo así:
        //           AppLauncher.getInstance().killProcess( nPID );
        //       De todos modos, si quieres, Anotnio, lo comentamos por tlf.
    }
    
    //------------------------------------------------------------------------//
    // INNER CLASSES
    //------------------------------------------------------------------------//
    
    private Vector<Vector> vRows = new Vector<Vector>();  // It is not elegant but works
                
    private class ThisTableModel extends DefaultTableModel
    {
        private String[] asColName = { "ID", "Process", "% CPU", "Memory" };
        
        @Override
        public String getColumnName( int nCol )
        {
            return asColName[ nCol ];
        }
        
        @Override
        public int getRowCount()
        {
            return vRows.size();
        }
        
        @Override
        public int getColumnCount()
        {
            return asColName.length;
        }
        
        @Override
        public Object getValueAt( int nRow, int nCol )
        {
            return vRows.get( nRow ).get( nCol );
        }
        
        @Override
        public boolean isCellEditable( int nRow, int nCol )
        {
            return false;
        }
        
        @Override
        public void setValueAt( Object value, int nRow, int nCol )
        {
            vRows.get( nRow ).set( nCol, value );
            fireTableCellUpdated( nRow, nCol );
        }
        
        @Override
        public Class getColumnClass( int nCol )
        {
            if( getRowCount() > 0 )
                return getValueAt( 0, nCol ).getClass();
            else
                return Object.class;
        }
        
        private void addRow( int nPID, String sAppName )
        {
            Vector<Object> v = new Vector<Object>();
                           v.add( nPID );
                           v.add( sAppName );
                           v.add( 0 );
                           v.add( 0 );
                   
            vRows.add( v );
            fireTableRowsInserted( vRows.size(), vRows.size() );
        }
        
        private void delRow( int nPID )
        {
            int nRow = getRowFromPID( nPID );
            
            if( nRow != -1 )
            {
                vRows.remove( nRow );
                fireTableRowsDeleted( nRow, nRow );                
            }
        }
        
        private void changeRow( int nPID, int nCPU, int nMemory )
        {
            int nRow = getRowFromPID( nPID );
            
            if( nRow != -1 )
            {
                Vector v = vRows.get( nRow );
                       v.set( 2, nCPU );
                       v.set( 3, nMemory );
                       
                fireTableRowsUpdated( nRow, nRow );
            }
        }
        
        private int getRowFromPID( int nPID )
        {
            for( int n = 0; n < getRowCount(); n++ )
            {
                if( ((Integer) getValueAt( n, 0 )) == nPID  )
                    return n;
            }
            
            return -1;
        }
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        btnKillProcess = new javax.swing.JButton();
        spTableProcesses = new javax.swing.JScrollPane();

        btnKillProcess.setMnemonic('K');
        btnKillProcess.setText("Kill Process");
        btnKillProcess.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnKillProcessActionPerformed(evt);
            }
        });

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
    // End of variables declaration//GEN-END:variables
    
}