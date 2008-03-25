/*
 * Login.java
 *
 * Created on 10 de septiembre de 2007, 11:55
 */

package org.joing.applauncher;

import java.awt.Cursor;
import java.util.List;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import org.joing.common.clientAPI.runtime.AppBridge;
import org.joing.common.dto.session.LoginResult;
import org.joing.common.exception.JoingServerException;
import org.joing.common.clientAPI.runtime.Bridge2Server;
import org.joing.common.dto.app.AppDescriptor;
import org.joing.common.dto.app.Application;
import org.joing.jvmm.RuntimeFactory;

/**
 *
 * @author  Francisco Morero Peyrona
 */
public class Login extends JDialog {

    private int nTries = 0; // Number of failed tries to login
    private boolean bValid = false;

    //------------------------------------------------------------------------//
    /** Creates new form Login */
    public Login() {
        super((JFrame) null, true);

        initComponents();
        fillDesktopComboBox();

        getRootPane().setDefaultButton(btnOk);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        txtAccount.setText("peyrona@joing.org");
        txtPassword.setText("admin");
    }

    public boolean wasSuccessful() {
        return bValid;
    }

    public boolean fullScreen() {
        return chkFullScreen.isSelected();
    }

    public AppDescriptor getApplicationDescriptor() {
        String        sDesktop = (String) cmbDesktop.getSelectedItem();
        AppDescriptor appDesc  = (AppDescriptor) cmbDesktop.getClientProperty( sDesktop );

        return appDesc;
    }
    
    // TODO: Fix this. An application object contains the application data,
    // we need to store a simpler object.
    public Integer getDesktopApplicationId() {
        String desktop = (String)cmbDesktop.getSelectedItem();
        
        return (Integer)cmbDesktop.getClientProperty(desktop);
    }

    //------------------------------------------------------------------------//
    private void fillDesktopComboBox() {
        
        AppBridge           bridge     = RuntimeFactory.getPlatform().getBridge().getAppBridge();

        List<Application> desktops = bridge.getAvailableDesktops();
        
        for (Application app : desktops) {
            cmbDesktop.addItem(app.getName());
            cmbDesktop.putClientProperty(app.getName(), app.getId());
        }
        
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        javax.swing.JLabel lblAccount = new javax.swing.JLabel();
        javax.swing.JLabel lblPassword = new javax.swing.JLabel();
        txtAccount = new javax.swing.JTextField();
        txtPassword = new javax.swing.JPasswordField();
        lblLogo = new javax.swing.JLabel();
        btnOk = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();
        chkFullScreen = new javax.swing.JCheckBox();
        cmbDesktop = new javax.swing.JComboBox();
        javax.swing.JLabel lblDesktop = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Joing :: Login");
        setBackground(java.awt.SystemColor.window);
        setName("frmLogin"); // NOI18N
        setResizable(false);

        lblAccount.setText("Account");
        lblAccount.setFocusable(false);

        lblPassword.setText("Password");
        lblPassword.setFocusable(false);

        lblLogo.setBackground(new java.awt.Color(255, 255, 255));
        lblLogo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/joing/applauncher/resources/joing_logo.png"))); // NOI18N
        lblLogo.setFocusable(false);
        lblLogo.setOpaque(true);

        btnOk.setMnemonic('O');
        btnOk.setText("Ok");
        btnOk.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                onOk(evt);
            }
        });

        btnCancel.setMnemonic('C');
        btnCancel.setText("Cancel");
        btnCancel.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                onCancel(evt);
            }
        });

        chkFullScreen.setText("Use full-screen mode");
        chkFullScreen.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        chkFullScreen.setMargin(new java.awt.Insets(0, 0, 0, 0));

        lblDesktop.setText("Desktop");
        lblDesktop.setFocusable(false);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lblLogo, javax.swing.GroupLayout.DEFAULT_SIZE, 295, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(135, Short.MAX_VALUE)
                .addComponent(btnOk, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnCancel)
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblPassword)
                    .addComponent(lblAccount)
                    .addComponent(lblDesktop))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(chkFullScreen)
                    .addComponent(txtPassword, javax.swing.GroupLayout.DEFAULT_SIZE, 208, Short.MAX_VALUE)
                    .addComponent(txtAccount, javax.swing.GroupLayout.DEFAULT_SIZE, 208, Short.MAX_VALUE)
                    .addComponent(cmbDesktop, javax.swing.GroupLayout.Alignment.TRAILING, 0, 208, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(lblLogo)
                .addGap(19, 19, 19)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblAccount)
                    .addComponent(txtAccount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblPassword)
                    .addComponent(txtPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(11, 11, 11)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmbDesktop, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblDesktop))
                .addGap(17, 17, 17)
                .addComponent(chkFullScreen)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 21, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnCancel)
                    .addComponent(btnOk))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void onOk(java.awt.event.ActionEvent evt)//GEN-FIRST:event_onOk
    {//GEN-HEADEREND:event_onOk
        Cursor cursor = getRootPane().getCursor();
        getRootPane().setCursor(new Cursor(Cursor.WAIT_CURSOR));

        btnOk.setEnabled(false);
        btnCancel.setEnabled(false);

        try {
            Bridge2Server b2s = RuntimeFactory.getPlatform().getBridge();
            LoginResult result = b2s.getSessionBridge().login(txtAccount.getText(), String.valueOf(txtPassword.getPassword()));

            if (result.isLoginValid()) {
                bValid = true;
                dispose();
            } else {
                if (++nTries == 3) {
                    JOptionPane.showMessageDialog(Login.this, "Please check your account and password\nand try it later.");
                    dispose();
                } else {
                    String sMsg = "Can't login: ";

                    if (!result.isAccountValid()) {
                        sMsg += "invalid account";
                    }

                    if (!result.isPasswordValid()) {
                        if (!result.isAccountValid()) {
                            sMsg += " and password";
                        } else {
                            sMsg += "invalid password";
                        }
                    }

                    sMsg += ".\nPlease, try again.";
                    JOptionPane.showMessageDialog(Login.this, sMsg);
                }
            }
        } catch (JoingServerException exc) {
            // Can't invoke org.joing.runtime.Runtime.getRuntime().showException( ... )
            // because the desktop does not exists yet.
            JOptionPane.showMessageDialog(this, exc.getLocalizedMessage(), "Error during login", JOptionPane.ERROR_MESSAGE);
        } finally {
            getRootPane().setCursor(cursor);
            btnOk.setEnabled(true);
            btnCancel.setEnabled(true);
        }
        // }
    }//GEN-LAST:event_onOk

    private void onCancel(java.awt.event.ActionEvent evt)//GEN-FIRST:event_onCancel
    {//GEN-HEADEREND:event_onCancel
        dispose();
    }//GEN-LAST:event_onCancel

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnOk;
    private javax.swing.JCheckBox chkFullScreen;
    private javax.swing.JComboBox cmbDesktop;
    private javax.swing.JLabel lblLogo;
    private javax.swing.JTextField txtAccount;
    private javax.swing.JPasswordField txtPassword;
    // End of variables declaration//GEN-END:variables
}