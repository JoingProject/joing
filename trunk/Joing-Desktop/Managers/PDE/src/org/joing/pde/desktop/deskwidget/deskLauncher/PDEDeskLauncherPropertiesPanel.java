/*
 * PDEDeskLauncherPropertiesPanel.java
 *
 * Created on 5 de diciembre de 2007, 17:06
 */
package org.joing.pde.desktop.deskwidget.deskLauncher;

import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.SwingUtilities;
import org.joing.common.desktopAPI.DeskComponent;
import org.joing.common.desktopAPI.StandardImage;
import org.joing.common.desktopAPI.deskwidget.deskLauncher.DeskLauncher;
import org.joing.common.dto.app.AppDescriptor;
import org.joing.pde.PDEUtilities;

/**
 *
 * @author  Francisco Morero Peyrona
 */
public class PDEDeskLauncherPropertiesPanel extends javax.swing.JPanel implements DeskComponent
{
    private PDEDeskLauncher launcher;
    
    //------------------------------------------------------------------------//
    
    /**
     * Invoked when creating a new launcher.
     * 
     * @param bIsDir true to create a dir and false to create a "normal" 
     *               launcher.
     */
    public PDEDeskLauncherPropertiesPanel( boolean bIsDir )
    {
        this( null );
        
        if( bIsDir )
            radDir.setSelected( true );
        else
            radApp.setSelected( true );
        
        onSelectionChanged();
    }
    
    /**
     * Invoked when editing properties of an existing launcher.
     * 
     * @param launcher Target launcher.
     */
    public PDEDeskLauncherPropertiesPanel( PDEDeskLauncher launcher )
    {
        this.launcher = launcher;
        
        initComponents();
        
        if( launcher != null )
        {
            txtName.setText( launcher.getText() );
            txtDescription.setText( launcher.getToolTipText() );
        }
        
        SwingUtilities.invokeLater( new Runnable() 
        {
            public void run()
            {
                txtName.requestFocusInWindow();
            }
        } );
    }
    
    /**
     * Creates a new launcher based on provided information.
     * 
     * @return A new Launcher.
     */
    public PDEDeskLauncher createLauncher()
    {        
        if( launcher == null )
            launcher = new PDEDeskLauncher();
        
        launcher.setText( txtName.getText() );
        launcher.setDescription( txtDescription.getText() );
        launcher.setImage( ((ImageIcon) btnIcon.getIcon()).getImage() );
        
        if( radApp.isSelected() )
        {
            launcher.setTarget( txtDirectory.getText() );
            launcher.setType( DeskLauncher.Type.DIRECTORY );
        }
        else
        {
            launcher.setTarget( txtApplication.getText() );
            launcher.setArguments( txtArguments.getText() );
            launcher.setType( DeskLauncher.Type.APPLICATION );
        }
        
        return launcher;
    }
    
    //------------------------------------------------------------------------//
    
    private void onSelectionChanged()
    {
        lblApplication.setEnabled( radApp.isSelected() );
        txtApplication.setEnabled( radApp.isSelected() );
        lblArguments.setEnabled( radApp.isSelected() );
        txtArguments.setEnabled( radApp.isSelected() );
        btnSelectApp.setEnabled( radApp.isSelected() );
        
        lblDirectory.setEnabled( radDir.isSelected() );
        txtDirectory.setEnabled( radDir.isSelected() );
        btnSelectDirectory.setEnabled( radDir.isSelected() );
        
        Image image = PDEUtilities.getDesktopManager().getRuntime().getImage( (radApp.isSelected() ? StandardImage.LAUNCHER : StandardImage.FOLDER) );
        btnIcon.setIcon( new ImageIcon( image ) );
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        radButtonGroup = new javax.swing.ButtonGroup();
        txtName = new javax.swing.JTextField();
        txtDescription = new javax.swing.JTextField();
        btnIcon = new javax.swing.JButton();
        lblText = new javax.swing.JLabel();
        lblDescription = new javax.swing.JLabel();
        pnlApp = new javax.swing.JPanel();
        lblApplication = new javax.swing.JLabel();
        lblArguments = new javax.swing.JLabel();
        txtArguments = new javax.swing.JTextField();
        txtApplication = new javax.swing.JTextField();
        btnSelectApp = new javax.swing.JButton();
        radApp = new javax.swing.JRadioButton();
        pnlDirectory = new javax.swing.JPanel();
        txtDirectory = new javax.swing.JTextField();
        btnSelectDirectory = new javax.swing.JButton();
        radDir = new javax.swing.JRadioButton();
        lblDirectory = new javax.swing.JLabel();

        btnIcon.setFocusPainted(false);
        btnIcon.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnIconActionPerformed(evt);
            }
        });

        lblText.setText("Name");

        lblDescription.setText("Description");

        pnlApp.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        lblApplication.setText("ID or Name");

        lblArguments.setText("Arguments");

        btnSelectApp.setText("...");
        btnSelectApp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                onSelectAppButton(evt);
            }
        });

        radButtonGroup.add(radApp);
        radApp.setText("Application");
        radApp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                radAppActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlAppLayout = new javax.swing.GroupLayout(pnlApp);
        pnlApp.setLayout(pnlAppLayout);
        pnlAppLayout.setHorizontalGroup(
            pnlAppLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlAppLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlAppLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlAppLayout.createSequentialGroup()
                        .addComponent(lblArguments)
                        .addGap(14, 14, 14)
                        .addComponent(txtArguments, javax.swing.GroupLayout.DEFAULT_SIZE, 306, Short.MAX_VALUE))
                    .addComponent(radApp)
                    .addGroup(pnlAppLayout.createSequentialGroup()
                        .addComponent(lblApplication)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtApplication, javax.swing.GroupLayout.DEFAULT_SIZE, 276, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnSelectApp)))
                .addContainerGap())
        );
        pnlAppLayout.setVerticalGroup(
            pnlAppLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlAppLayout.createSequentialGroup()
                .addComponent(radApp)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlAppLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblApplication)
                    .addComponent(btnSelectApp)
                    .addComponent(txtApplication, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlAppLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblArguments)
                    .addComponent(txtArguments, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pnlDirectory.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        btnSelectDirectory.setText("...");

        radButtonGroup.add(radDir);
        radDir.setText("Directory");
        radDir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                radDirActionPerformed(evt);
            }
        });

        lblDirectory.setText("Path");

        javax.swing.GroupLayout pnlDirectoryLayout = new javax.swing.GroupLayout(pnlDirectory);
        pnlDirectory.setLayout(pnlDirectoryLayout);
        pnlDirectoryLayout.setHorizontalGroup(
            pnlDirectoryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlDirectoryLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlDirectoryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlDirectoryLayout.createSequentialGroup()
                        .addComponent(radDir)
                        .addGap(321, 321, 321))
                    .addGroup(pnlDirectoryLayout.createSequentialGroup()
                        .addComponent(lblDirectory)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtDirectory, javax.swing.GroupLayout.DEFAULT_SIZE, 320, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnSelectDirectory)
                        .addContainerGap())))
        );
        pnlDirectoryLayout.setVerticalGroup(
            pnlDirectoryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlDirectoryLayout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addComponent(radDir)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlDirectoryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnSelectDirectory)
                    .addComponent(lblDirectory)
                    .addComponent(txtDirectory, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pnlDirectory, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pnlApp, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnIcon, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblDescription)
                            .addComponent(lblText))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtName, javax.swing.GroupLayout.DEFAULT_SIZE, 251, Short.MAX_VALUE)
                            .addComponent(txtDescription, javax.swing.GroupLayout.DEFAULT_SIZE, 251, Short.MAX_VALUE))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblText)
                            .addComponent(txtName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblDescription)
                            .addComponent(txtDescription, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(btnIcon, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(pnlApp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(pnlDirectory, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void radAppActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_radAppActionPerformed
        onSelectionChanged();
    }//GEN-LAST:event_radAppActionPerformed

    private void radDirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_radDirActionPerformed
        onSelectionChanged();
    }//GEN-LAST:event_radDirActionPerformed

    private void btnIconActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnIconActionPerformed
        // TODO: hacerlo -> Seleccionar una imagen desde archivo
    }//GEN-LAST:event_btnIconActionPerformed

private void onSelectAppButton(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_onSelectAppButton
        AppDescriptor app = PDEUtilities.selectApplication();
        
        if( app != null )
            txtApplication.setText( Integer.toString( app.getId() ) );
}//GEN-LAST:event_onSelectAppButton

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnIcon;
    private javax.swing.JButton btnSelectApp;
    private javax.swing.JButton btnSelectDirectory;
    private javax.swing.JLabel lblApplication;
    private javax.swing.JLabel lblArguments;
    private javax.swing.JLabel lblDescription;
    private javax.swing.JLabel lblDirectory;
    private javax.swing.JLabel lblText;
    private javax.swing.JPanel pnlApp;
    private javax.swing.JPanel pnlDirectory;
    private javax.swing.JRadioButton radApp;
    private javax.swing.ButtonGroup radButtonGroup;
    private javax.swing.JRadioButton radDir;
    private javax.swing.JTextField txtApplication;
    private javax.swing.JTextField txtArguments;
    private javax.swing.JTextField txtDescription;
    private javax.swing.JTextField txtDirectory;
    private javax.swing.JTextField txtName;
    // End of variables declaration//GEN-END:variables
}