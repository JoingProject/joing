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

package org.joing.kernel.applauncher;

import java.awt.Color;
import java.awt.Cursor;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingWorker;
import javax.swing.border.LineBorder;
import org.joing.common.dto.session.LoginResult;
import org.joing.common.exception.JoingServerException;
import org.joing.common.dto.app.AppDescriptor;
import org.joing.common.dto.session.SystemInfo;
import org.joing.kernel.api.bridge.Bridge2Server;
import org.joing.kernel.api.kernel.log.JoingLogger;
import org.joing.kernel.api.kernel.log.SimpleLoggerFactory;

/**
 * Creates a loginf dialog.
 * 
 * @author  Francisco Morero Peyrona
 */
class Login extends JDialog
{
    private int     nTries    = 0;        // Number of failed tries to login
    private boolean bValid    = false;
    private JDialog wndSplash = null;     // Can't use JWindow: does not render properly via WebStart
    
    //------------------------------------------------------------------------//
    /** Creates new dialog Login */
    Login()
    {
        super( (JFrame) null, true );
        
        initComponents();
        fillDesktopComboBox();
        
        getRootPane().setDefaultButton( btnOk );
        setLocationRelativeTo( null );
        setDefaultCloseOperation( JDialog.DISPOSE_ON_CLOSE );
        setIconImage( (new ImageIcon( getClass().getResource( "resources/joing_icon.png" ) )).getImage() );
        
        if( System.getProperty( "JoingDebug" ) != null )
        {
            txtAccount.setText( "guest" );
            txtPassword.setText( "guest." );
        }
    }
    
    // NEXT: En lugar de boolean sería mejor devolver un LoginResult porque se da mucha más inf.
    boolean isLoginSuccessful()
    {
        return bValid;
    }
    
    boolean isFullScreenRequested()
    {
        return chkFullScreen.isSelected();
    }
    
    void disposeSplash()
    {
        if( wndSplash != null )
            wndSplash.dispose();
    }
    
    AppDescriptor getApplicationDescriptor()
    {
        String        sDesktop = (String) cmbDesktop.getSelectedItem();
        AppDescriptor appDesc  = (AppDescriptor) cmbDesktop.getClientProperty( sDesktop );
        
        return appDesc;
    }
    
    int getDesktopApplicationId()
    {
        return getApplicationDescriptor().getId();
    }
    
    //------------------------------------------------------------------------//
    
    private void fillDesktopComboBox()
    {
        btnOk.setEnabled( false );   // Can't be enabled until combobox is filled
        btnOk.setText( "Wait" );
        
        // This method is executed in a separated thread to increase speed
        SwingWorker sw = new SwingWorker<Void,Void>()
        {
            private SystemInfo          sysinfo  = null;
            private List<AppDescriptor> desktops = null;
            
            protected Void doInBackground() throws Exception
            {
                Bridge2Server bridge = org.joing.kernel.jvmm.RuntimeFactory.getPlatform().getBridge();
                
                try
                {
                    sysinfo = bridge.getSessionBridge().getSystemInfo();
                    desktops = bridge.getAppBridge().getAvailableDesktops();
                }
                catch( Exception exc )
                {
                    SimpleLoggerFactory.getLogger( JoingLogger.ID ).critical( "Error accessing Server", exc.getMessage() );
                }
                
                return null;
            }
            
            protected void done()
            {
                if( sysinfo == null )
                {
                    JOptionPane.showMessageDialog( Login.this, "Error: sever seems to be out of service.\nCan't continue." );
                    Login.this.dispose();
                }
                else
                {
                    String sText = txtAccount.getText() +"@"+ sysinfo.getName();
                    int    nPos  = sText.length() - sysinfo.getName().length() - 1;
                    txtAccount.setText( sText );
                    txtAccount.setCaretPosition( Math.max( 0, Math.min( sText.length(), nPos ) ) );
                    
                    if( desktops == null )    // An exception happened in doInBackground()
                    {
                        JOptionPane.showMessageDialog( Login.this, "Error: no desktop available.\nCan't continue." );
                        Login.this.dispose();
                    }
                    else
                    {
                        for( AppDescriptor desk : desktops )
                        {
                            Login.this.cmbDesktop.addItem( desk.getName() );
                            Login.this.cmbDesktop.putClientProperty( desk.getName(), desk );
                        }

                        btnOk.setText( "Ok" );
                        btnOk.setEnabled( true );
                    }
                }
            }
        };
        sw.execute();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

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
        setTitle("Join'g :: Login");
        setBackground(java.awt.SystemColor.window);
        setName("frmLogin"); // NOI18N
        setResizable(false);

        lblAccount.setText("Account");
        lblAccount.setFocusable(false);

        lblPassword.setText("Password");
        lblPassword.setFocusable(false);

        lblLogo.setBackground(new java.awt.Color(255, 255, 255));
        lblLogo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/joing/kernel/applauncher/resources/joing_logo.png"))); // NOI18N
        lblLogo.setBorder(javax.swing.BorderFactory.createLineBorder(java.awt.Color.white, 4));
        lblLogo.setFocusable(false);
        lblLogo.setOpaque(true);

        btnOk.setText("Ok");
        btnOk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                onOk(evt);
            }
        });

        btnCancel.setText("Cancel");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                onCancel(evt);
            }
        });

        chkFullScreen.setText("Use full-screen mode");
        chkFullScreen.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));

        lblDesktop.setText("Desktop");
        lblDesktop.setFocusable(false);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblPassword)
                    .addComponent(lblAccount))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(23, 23, 23)
                        .addComponent(txtAccount, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtPassword, javax.swing.GroupLayout.DEFAULT_SIZE, 171, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(124, Short.MAX_VALUE)
                .addComponent(btnOk, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnCancel)
                .addContainerGap())
            .addComponent(lblLogo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblDesktop)
                .addGap(30, 30, 30)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(chkFullScreen)
                    .addComponent(cmbDesktop, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {btnCancel, btnOk});

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {cmbDesktop, txtAccount, txtPassword});

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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblDesktop)
                    .addComponent(cmbDesktop, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(chkFullScreen)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 28, Short.MAX_VALUE)
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
        getRootPane().setCursor( new Cursor( Cursor.WAIT_CURSOR ) );

        btnOk.setEnabled( false );
        btnCancel.setEnabled( false );
        
        try
        {
            LoginResult result = org.joing.kernel.jvmm.RuntimeFactory.getPlatform().getBridge().
                                     getSessionBridge().login( txtAccount.getText(), String.valueOf( txtPassword.getPassword() ) );
            
            if( result.isLoginValid() )
            {
                bValid = true;
                dispose();
                
                wndSplash = new JDialog();
                wndSplash.add( new LaunchingDesktopSplash() );
                // TODO: Arreglar esto
                wndSplash.setTitle( "Launching PDE..." );
                ///wndSplash.setUndecorated( true );
                wndSplash.setAlwaysOnTop( true );
                wndSplash.pack();
                wndSplash.setLocationRelativeTo( null );
                wndSplash.setVisible( true );
            }
            else
            {
                if( ++nTries == 3 )
                {
                    JOptionPane.showMessageDialog( Login.this, "Please check your account and password\nand try it later." );
                    dispose();
                }
                else
                {
                    String sMsg = "Can't login: ";

                    if( ! result.isAccountValid() )
                    {
                        sMsg += "invalid account";
                    }

                    if( ! result.isPasswordValid() )
                    {
                        if( ! result.isAccountValid() )
                        {
                            sMsg += " and password";
                        }
                        else
                        {
                            sMsg += "invalid password";
                        }
                    }

                    sMsg += ".\nPlease, try again.";
                    JOptionPane.showMessageDialog( Login.this, sMsg );
                }
            }
        }
        catch( JoingServerException exc )
        {
            // Can't invoke org.joing.runtime.Runtime.getRuntime().showException( ... )
            // because the desktop does not exists yet.
            JOptionPane.showMessageDialog( this, exc.toString(), "Error during login", JOptionPane.ERROR_MESSAGE );
        }
        finally
        {
            getRootPane().setCursor( cursor );
            btnOk.setEnabled( true );
            btnCancel.setEnabled( true );
        }
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

    //------------------------------------------------------------------------//
    // INNER CLASS: LaunchingDesktopSplash
    //------------------------------------------------------------------------//
    private final class LaunchingDesktopSplash extends JPanel
    {
        private javax.swing.JLabel jLabel2;
        private javax.swing.JProgressBar progressBar;
        private javax.swing.JLabel lblDesktopMangerName;
        private javax.swing.JLabel lblJoingLogo;
        
        public LaunchingDesktopSplash()
        {
            initComponents();
            setBackground( Color.white );
            setBorder( new LineBorder( Color.black, 2 ) );
        }
        
        private void initComponents() 
        {
        lblJoingLogo = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        lblDesktopMangerName = new javax.swing.JLabel();
        progressBar = new javax.swing.JProgressBar();
        progressBar.setIndeterminate(true);

        lblJoingLogo.setIcon(new javax.swing.ImageIcon(getClass().getResource("resources/joing_logo.png")));

        jLabel2.setText("Launching:");

        lblDesktopMangerName.setText("PDE");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(progressBar, javax.swing.GroupLayout.DEFAULT_SIZE, 278, Short.MAX_VALUE)
                    .addComponent(lblJoingLogo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblDesktopMangerName, javax.swing.GroupLayout.DEFAULT_SIZE, 203, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblJoingLogo)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(lblDesktopMangerName))
                .addGap(18, 18, 18)
                .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }
    }
}