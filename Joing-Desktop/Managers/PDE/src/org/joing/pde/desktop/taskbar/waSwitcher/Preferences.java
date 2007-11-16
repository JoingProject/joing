/*
 * WorkAreaProperties.java
 *
 * Created on 3 de noviembre de 2007, 19:48
 */

package org.joing.pde.desktop.taskbar.waSwitcher;

import java.util.List;
import javax.swing.DefaultListModel;
import org.joing.common.desktopAPI.workarea.WorkArea;
import org.joing.pde.PDEManager;
import org.joing.pde.PDERuntime;

/**
 *
 * @author  fmorero
 */
class Preferences extends javax.swing.JPanel
{
    /** Creates new form WorkAreaProperties */
    public Preferences()
    {
        initComponents();
        
        DefaultListModel dlm = new DefaultListModel();
        lstNamesWA.setModel( dlm );
        
        List<WorkArea> lstWorAreas = PDEManager.getInstance().getDesktop().getWorkAreas();
        
        spnWA.getModel().setValue( lstWorAreas.size() );
        
        for( WorkArea wa : lstWorAreas )
            dlm.addElement( wa.getName() );
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        lblSpinner = new javax.swing.JLabel();
        spnWA = new javax.swing.JSpinner();
        jScrollPane1 = new javax.swing.JScrollPane();
        lstNamesWA = new javax.swing.JList();
        lblList = new javax.swing.JLabel();

        lblSpinner.setText("Number of WorkAreas");

        spnWA.setModel(new javax.swing.SpinnerNumberModel(1, 1, 7, 1));

        jScrollPane1.setViewportView(lstNamesWA);

        lblList.setText("WorkArea's names");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(lblSpinner)
                .addGap(18, 18, 18)
                .addComponent(spnWA, javax.swing.GroupLayout.DEFAULT_SIZE, 49, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addComponent(lblList)
                .addContainerGap(85, Short.MAX_VALUE))
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 191, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblSpinner)
                    .addComponent(spnWA, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblList)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 115, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblList;
    private javax.swing.JLabel lblSpinner;
    private javax.swing.JList lstNamesWA;
    private javax.swing.JSpinner spnWA;
    // End of variables declaration//GEN-END:variables
    
}