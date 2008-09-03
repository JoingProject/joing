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
package org.joing.pde.desktop.deskwidget.deskLauncher;

import java.awt.Image;
import java.io.File;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.SwingUtilities;
import org.joing.kernel.api.desktop.DeskComponent;
import org.joing.kernel.api.desktop.StandardImage;
import org.joing.kernel.api.desktop.deskwidget.deskLauncher.DeskLauncher;
import org.joing.common.dto.app.AppDescriptor;
import org.joing.kernel.runtime.vfs.VFSFile;
import org.joing.kernel.swingtools.JoingApplicationChooser;
import org.joing.kernel.swingtools.filesystem.JoingFileChooser;
import org.joing.kernel.swingtools.filesystem.JoingFileChooserPreviewImage;
import org.joing.kernel.swingtools.filesystem.JoingFolderChooser;

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
        
        (bIsDir ? radDir : radApp).setSelected( true );
        
        onSelectionChanged();        // Makes enable, dissable and default icon
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

            if( launcher.getType() == DeskLauncher.Type.DIRECTORY )
            {
                radDir.setSelected( true );
                txtDirectory.setText( launcher.getTarget() );
            }
            else
            {
                radApp.setSelected( true );
                txtApplication.setText( launcher.getTarget() );
                txtArguments.setText( launcher.getArguments() );
            }
            
            onSelectionChanged();    // Makes enable, dissable and default icon
            setIcon4IconButton( launcher.getImage() );   // Last because onSelectionChanged() changes it
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
        String sCaption = (txtName.getText().length() > 0) ? txtName.getText() :
                          (txtApplication.getText().length() > 0) ? txtApplication.getText() : txtDirectory.getText();
        
        if( launcher == null )
            launcher = new PDEDeskLauncher();
        
        launcher.setText( sCaption );
        launcher.setDescription( txtDescription.getText() );
        launcher.setImage( ((ImageIcon) btnIcon.getIcon()).getImage() );
        
        if( radApp.isSelected() )
        {
            launcher.setTarget( txtApplication.getText() );
            launcher.setArguments( txtArguments.getText() );
            launcher.setType( DeskLauncher.Type.APPLICATION );
        }
        else
        {
            launcher.setTarget( txtDirectory.getText() );
            launcher.setType( DeskLauncher.Type.DIRECTORY );
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
        
        StandardImage si    = (radApp.isSelected() ? StandardImage.LAUNCHER : StandardImage.FOLDER);
        Image         image = org.joing.kernel.jvmm.RuntimeFactory.getPlatform().getDesktopManager().getRuntime().getImage( si, 48, 48 );
        setIcon4IconButton( image );
    }
    
    private void setIcon4IconButton( Image image )
    {
        btnIcon.setIcon( new ImageIcon( image.getScaledInstance( 48,48, Image.SCALE_SMOOTH ) ) );
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
                onSelectIcon(evt);
            }
        });

        lblText.setText("Name");

        lblDescription.setText("Description");

        pnlApp.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        lblApplication.setText("App. ID");

        lblArguments.setText("Arguments");

        btnSelectApp.setText("...");
        btnSelectApp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                onSelectApplication(evt);
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
                    .addComponent(radApp)
                    .addGroup(pnlAppLayout.createSequentialGroup()
                        .addGroup(pnlAppLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblArguments)
                            .addComponent(lblApplication))
                        .addGap(14, 14, 14)
                        .addGroup(pnlAppLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pnlAppLayout.createSequentialGroup()
                                .addComponent(txtApplication, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnSelectApp))
                            .addComponent(txtArguments, javax.swing.GroupLayout.DEFAULT_SIZE, 306, Short.MAX_VALUE))))
                .addContainerGap())
        );
        pnlAppLayout.setVerticalGroup(
            pnlAppLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlAppLayout.createSequentialGroup()
                .addComponent(radApp)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlAppLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblApplication)
                    .addComponent(txtApplication, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSelectApp))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlAppLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblArguments)
                    .addComponent(txtArguments, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pnlDirectory.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        btnSelectDirectory.setText("...");
        btnSelectDirectory.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                onSelectFolder(evt);
            }
        });

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

    private void onSelectIcon(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_onSelectIcon
        JoingFileChooserPreviewImage jfcpi = new JoingFileChooserPreviewImage();
        JoingFileChooser             jfc = new JoingFileChooser();
                                     jfc.setAccessory( jfcpi );
                                     jfc.addChoosableFileFilter( JoingFileChooserPreviewImage.getFilter() );
                                     jfc.addPropertyChangeListener( jfcpi );
                                     
        if( jfc.showDialog( this ) == JFileChooser.APPROVE_OPTION )
        {
            File file = jfc.getSelectedFile();
            setIcon4IconButton( new ImageIcon( file.getAbsolutePath() ).getImage() );
        }
}//GEN-LAST:event_onSelectIcon

private void onSelectApplication(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_onSelectApplication
        AppDescriptor app = JoingApplicationChooser.showDialog();
        
        if( app != null )
        {
            txtName.setText( app.getName() );
            txtDescription.setText( app.getDescription() );
            txtApplication.setText( Integer.toString( app.getId() ) );
            
            if( app.getIconPixel().length > 0 )
            {
                Image image = new ImageIcon( app.getIconPixel() ).getImage(); 
                setIcon4IconButton( image );
            }
        }
}//GEN-LAST:event_onSelectApplication

private void onSelectFolder(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_onSelectFolder
    File fFolder = JoingFolderChooser.showDialog();
    
    if( fFolder != null )
    {
        String sPath = fFolder.getAbsolutePath();
        txtName.setText( sPath );
        txtDescription.setText( (fFolder instanceof VFSFile ? "Remote: " : "Local: ") + sPath );
        txtDirectory.setText( sPath );
    }
}//GEN-LAST:event_onSelectFolder

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