/*
 * JFilePropertiesPanel.java
 *
 * Created on 14 de agosto de 2008, 9:13
 */
package org.joing.kernel.swingtools.filesystem.fsviewer;

import java.io.File;
import java.util.Date;
import javax.swing.SwingUtilities;
import org.joing.kernel.api.desktop.DeskComponent;
import org.joing.kernel.runtime.vfs.VFSFile;

/**
 * Properties panel for both java.io.File and VFSFile.
 * 
 * @author  Francisco Morero Peyrona
 */
public class JFilePropertiesPanel extends javax.swing.JPanel implements DeskComponent
{
    private File file = null;
    
    /** Creates new form JFilePropertiesPanel */
    public JFilePropertiesPanel()
    {
        this( null );
    }
    
    public JFilePropertiesPanel( File file )
    {
        initComponents();
        
        if( file != null )
            setFile( file );
    }
    
    public void setFile( File f )
    {
        this.file = f;
        
        SwingUtilities.invokeLater( new Runnable()
        {// TODO: marcar los lbl que pueden modificarse con un "*" como dice la nota a pié de panel
         //       y user JoingCheckBos en lugar de JCheckBox para poder utilizar setEditable(...), marcados con '///'
            public void run()
            {
                boolean bIsVFS = file instanceof VFSFile;
                
                String  sPath  = file.getAbsolutePath();
                        sPath  = sPath.substring( 0, sPath.length() - file.getName().length() );
                        
                String  sProvider = org.joing.kernel.jvmm.RuntimeFactory.getPlatform().getBridge().
                                        getSessionBridge().getSystemInfo().getName();
                        
                txtName.setText( file.getName() );
                txtPath.setText( sPath );
                chkDirectory.setSelected( file.isDirectory() );
                chkHidden.setSelected( file.isHidden() );
                chkReadable.setSelected( file.canRead() );
                chkModifiable.setSelected( file.canWrite() );      // Changed using setReadOnly(...) and by exec()
                chkExecutable.setSelected( file.canExecute() );
                txtSize.setText( file.length() +" bytes" );
                txtModified.setText( new Date( file.lastModified() ).toString() );   // Changed using setLastModified(...);
                
                if( bIsVFS )
                {
                    VFSFile vfs = (VFSFile) file;
                    txtAccount.setText( vfs.getAccount() );
                    txtOwner.setText( vfs.getOwner() );
                    txtProvider.setText( sProvider );
                    chkPublic.setSelected( vfs.isPublic() );
                    chkInTrashcan.setSelected( vfs.isInTrashcan() );
                    chkDeleteable.setSelected( vfs.isDeleteable() );
                    chkAlterable.setSelected( vfs.isAlterable() );
                    chkDuplicable.setSelected( vfs.isDuplicable() );
                    txtCreated.setText( new Date( vfs.getCreated() ).toString() );
                    txtAccessed.setText( new Date( vfs.getAccessed() ).toString() );
                    txtNotes.setText( vfs.getNotes() );    
                }
                
                // Following properties can be never changed (not by File neither by VFSFile)
                txtPath.setEnabled( true );       txtPath.setEditable( false );      lblPath.setEnabled( true );
                chkDirectory.setEnabled( true );  ///chkDirectory.setEditable( false );
                txtSize.setEnabled( true );       txtSize.setEditable( false );      lblSize.setEnabled( true );
                txtAccount.setEnabled( bIsVFS );  txtAccount.setEditable( false );   lblAccount.setEnabled( bIsVFS );
                txtProvider.setEnabled( bIsVFS ); txtProvider.setEditable( false );  lblProvider.setEnabled( bIsVFS );
                txtCreated.setEnabled( true );    txtCreated.setEditable( false );   lblCreated.setEnabled( bIsVFS );
                
                // Following are common properties.
                // Even if the control is disabled, their label will be enabled
                // Join'g will allow java.io.File modifications even if exec() has to be used)
                txtName.setEnabled( true );        txtName.setEditable( true );     lblName.setEnabled( true );
                chkHidden.setEnabled( true );      ///chkHidden.setEditable( true );
                chkReadable.setEnabled( true );    ///chkReadable.setEditable( true );
                chkModifiable.setEnabled( true );  ///chkModifiable.setEditable( true );
                chkExecutable.setEnabled( true );  ///chkExecutable.setEditable( true );
                txtModified.setEnabled( true );    txtModified.setEditable( true );  lblModified.setEnabled( true );
                
                // Following are VFSFile properties.
                // When the control is disabled, their label will be also disabled
                boolean bEditable = bIsVFS && ((VFSFile) file).isAlterable();
                
                txtOwner.setEnabled( bIsVFS );    txtOwner.setEnabled( bEditable );        lblOwner.setEnabled( bEditable );
                chkPublic.setEnabled( bIsVFS );      ///chkPublic.setEditable( bEditable );
                chkInTrashcan.setEnabled( bIsVFS );  ///chkInTrashcan.setEditable( bEditable );
                chkDeleteable.setEnabled( bIsVFS );  ///chkDeleteable.setEditable( bEditable );
                chkAlterable.setEnabled( bIsVFS );   ///chkAlterable.setEditable( bEditable );
                chkDuplicable.setEnabled( bIsVFS );  ///chkDuplicable.setEditable( bEditable );
                txtAccessed.setEnabled( bIsVFS );    txtAccessed.setEditable( bEditable );    lblAccessed.setEnabled( bEditable );
                txtNotes.setEnabled( bIsVFS );       txtNotes.setEditable( bEditable );       lblNotes.setEnabled( bEditable );
            }
        } );
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTextField1 = new javax.swing.JTextField();
        lblName = new javax.swing.JLabel();
        txtName = new javax.swing.JTextField();
        lblAccount = new javax.swing.JLabel();
        txtAccount = new javax.swing.JTextField();
        lblPath = new javax.swing.JLabel();
        txtPath = new javax.swing.JTextField();
        lblOwner = new javax.swing.JLabel();
        txtOwner = new javax.swing.JTextField();
        chkDirectory = new javax.swing.JCheckBox();
        chkReadable = new javax.swing.JCheckBox();
        chkHidden = new javax.swing.JCheckBox();
        chkPublic = new javax.swing.JCheckBox();
        chkInTrashcan = new javax.swing.JCheckBox();
        chkExecutable = new javax.swing.JCheckBox();
        chkModifiable = new javax.swing.JCheckBox();
        chkDeleteable = new javax.swing.JCheckBox();
        chkAlterable = new javax.swing.JCheckBox();
        chkDuplicable = new javax.swing.JCheckBox();
        lblCreated = new javax.swing.JLabel();
        txtCreated = new javax.swing.JTextField();
        lblModified = new javax.swing.JLabel();
        txtModified = new javax.swing.JTextField();
        lblAccessed = new javax.swing.JLabel();
        txtAccessed = new javax.swing.JTextField();
        lblSize = new javax.swing.JLabel();
        txtSize = new javax.swing.JTextField();
        spNote = new javax.swing.JScrollPane();
        txtNotes = new javax.swing.JTextArea();
        lblNotes = new javax.swing.JLabel();
        txtProvider = new javax.swing.JTextField();
        lblProvider = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();

        jTextField1.setText("jTextField1");

        lblName.setText("Name");

        lblAccount.setText("Account");

        lblPath.setText("Path");

        lblOwner.setText("Owner");

        chkDirectory.setText("Directory");

        chkReadable.setText("Readable");

        chkHidden.setText("Hidden");

        chkPublic.setText("Public");

        chkInTrashcan.setText("In trashcan");

        chkExecutable.setText("Executable");

        chkModifiable.setText("Content modifiable");

        chkDeleteable.setText("Deleteable");

        chkAlterable.setText("Attributes modifiable");

        chkDuplicable.setText("Duplicable");

        lblCreated.setText("Created");

        lblModified.setText("Modified");

        lblAccessed.setText("Accessed");

        lblSize.setText("Size");

        txtNotes.setColumns(20);
        txtNotes.setRows(5);
        spNote.setViewportView(txtNotes);

        lblNotes.setText("Notes");

        lblProvider.setText("Provider");

        jLabel1.setFont(jLabel1.getFont().deriveFont(jLabel1.getFont().getStyle() & ~java.awt.Font.BOLD, jLabel1.getFont().getSize()-1));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Attributes marked with * are editable");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblName)
                            .addComponent(lblPath)
                            .addComponent(lblOwner)
                            .addComponent(lblAccount)
                            .addComponent(lblProvider))
                        .addGap(21, 21, 21)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtProvider, javax.swing.GroupLayout.DEFAULT_SIZE, 266, Short.MAX_VALUE)
                            .addComponent(txtAccount, javax.swing.GroupLayout.DEFAULT_SIZE, 266, Short.MAX_VALUE)
                            .addComponent(txtPath, javax.swing.GroupLayout.DEFAULT_SIZE, 266, Short.MAX_VALUE)
                            .addComponent(txtName, javax.swing.GroupLayout.DEFAULT_SIZE, 266, Short.MAX_VALUE)
                            .addComponent(txtOwner, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 266, Short.MAX_VALUE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(73, 73, 73)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(chkExecutable)
                            .addComponent(chkInTrashcan)
                            .addComponent(chkPublic)
                            .addComponent(chkHidden)
                            .addComponent(chkDirectory))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 14, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(chkDeleteable, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(chkModifiable, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(chkAlterable, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(chkDuplicable, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(chkReadable)))
                    .addComponent(spNote, javax.swing.GroupLayout.DEFAULT_SIZE, 339, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblCreated)
                            .addComponent(lblModified)
                            .addComponent(lblAccessed))
                        .addGap(12, 12, 12)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtModified, javax.swing.GroupLayout.DEFAULT_SIZE, 266, Short.MAX_VALUE)
                            .addComponent(txtSize, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 266, Short.MAX_VALUE)
                            .addComponent(txtAccessed, javax.swing.GroupLayout.DEFAULT_SIZE, 266, Short.MAX_VALUE)
                            .addComponent(txtCreated, javax.swing.GroupLayout.DEFAULT_SIZE, 266, Short.MAX_VALUE)))
                    .addComponent(lblSize)
                    .addComponent(lblNotes, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 339, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblName)
                    .addComponent(txtName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblPath)
                    .addComponent(txtPath, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblAccount)
                    .addComponent(txtAccount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblOwner)
                    .addComponent(txtOwner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblProvider)
                    .addComponent(txtProvider, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(28, 28, 28)
                        .addComponent(chkHidden)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(chkPublic)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(chkInTrashcan)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(chkExecutable))
                    .addComponent(chkDirectory)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(chkReadable)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(chkModifiable)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(chkDeleteable)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(chkAlterable)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(chkDuplicable)))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblCreated)
                    .addComponent(txtCreated, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblModified)
                    .addComponent(txtModified, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblAccessed)
                    .addComponent(txtAccessed, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblSize)
                    .addComponent(txtSize, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblNotes)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(spNote, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 14, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox chkAlterable;
    private javax.swing.JCheckBox chkDeleteable;
    private javax.swing.JCheckBox chkDirectory;
    private javax.swing.JCheckBox chkDuplicable;
    private javax.swing.JCheckBox chkExecutable;
    private javax.swing.JCheckBox chkHidden;
    private javax.swing.JCheckBox chkInTrashcan;
    private javax.swing.JCheckBox chkModifiable;
    private javax.swing.JCheckBox chkPublic;
    private javax.swing.JCheckBox chkReadable;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JLabel lblAccessed;
    private javax.swing.JLabel lblAccount;
    private javax.swing.JLabel lblCreated;
    private javax.swing.JLabel lblModified;
    private javax.swing.JLabel lblName;
    private javax.swing.JLabel lblNotes;
    private javax.swing.JLabel lblOwner;
    private javax.swing.JLabel lblPath;
    private javax.swing.JLabel lblProvider;
    private javax.swing.JLabel lblSize;
    private javax.swing.JScrollPane spNote;
    private javax.swing.JTextField txtAccessed;
    private javax.swing.JTextField txtAccount;
    private javax.swing.JTextField txtCreated;
    private javax.swing.JTextField txtModified;
    private javax.swing.JTextField txtName;
    private javax.swing.JTextArea txtNotes;
    private javax.swing.JTextField txtOwner;
    private javax.swing.JTextField txtPath;
    private javax.swing.JTextField txtProvider;
    private javax.swing.JTextField txtSize;
    // End of variables declaration//GEN-END:variables
}
