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
package org.joing.proxyconfig;

import java.net.Authenticator;
import java.net.PasswordAuthentication;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import org.joing.common.desktopAPI.DeskComponent;
import org.joing.common.desktopAPI.DesktopManager;
import org.joing.common.desktopAPI.pane.DeskFrame;

/**
 * A GUI to change configure Proxy.
 * 
 * @author Francisco Morero Peyrona
 */
public class ProxyConfig extends JPanel implements DeskComponent
{
    // TODO: Mirar aquí (para añadir más cosas al panel): 
    //       http://java.sun.com/j2se/1.5.0/docs/guide/net/properties.html
    private DeskFrame frame;
    
    /** Creates new form ProxyConfig */
    public ProxyConfig()
    {
        initComponents();
        initComponentsValues();
    }
    
    //------------------------------------------------------------------------//
    
    public void showInFrame()
    {
        DesktopManager dm   = org.joing.jvmm.RuntimeFactory.getPlatform().getDesktopManager();
        ImageIcon      icon = new ImageIcon( getClass().getResource( "proxy_config.png" ) );
        
        // Show this panel in a frame created by DesktopManager Runtime.
        frame = dm.getRuntime().createFrame();
        frame.setTitle( "Proxy Configuration" );
        frame.setIcon( icon.getImage() );
        frame.add( (DeskComponent) this );
        
        dm.getDesktop().getActiveWorkArea().add( frame );
    }
    
    public static void main( String[] asArg )
    {
        (new ProxyConfig()).showInFrame();;
    }
    
    //------------------------------------------------------------------------//
    
    private void initComponentsValues()
    {
        String sHttpHost = (String) System.getProperties().get( "http.proxyHost" );
        String sHttpPort = (String) System.getProperties().get( "http.proxyPort" );
        String sHttpExcl = (String) System.getProperties().get( "http.nonProxyHosts" );
        
        txtHttpHost.setText( sHttpHost );
        txtHttpPort.setText( sHttpPort );
        txtHttpExclude.setText( sHttpExcl );
        
        String sFtpHost = (String) System.getProperties().get( "ftp.proxyHost" );
        String sFtpPort = (String) System.getProperties().get( "ftp.proxyPort" );
        String sFtpExcl = (String) System.getProperties().get( "ftp.nonProxyHosts" );
        
        txtFtpHost.setText( sFtpHost );
        txtFtpPort.setText( sFtpPort );
        txtFtpExclude.setText( sFtpExcl );
        
        if( (sHttpHost != null && sHttpHost.length() > 0) || 
            (sHttpPort != null && sHttpPort.length() > 0) ||
            (sHttpExcl != null && sHttpExcl.length() > 0) || 
            (sFtpHost  != null && sFtpHost.length()  > 0) || 
            (sFtpPort  != null && sFtpPort.length()  > 0) ||
            (sFtpExcl  != null && sFtpExcl.length()  > 0) )
            radProxy.setSelected( true );
        else
            radDirect.setSelected( true );
        
        setComponentsStatus( radProxy.isSelected() );
    }
    
    private void setProxySettings()
    {
        System.getProperties().remove( "http.proxyHost"     );
        System.getProperties().remove( "http.proxyPort"     );
        System.getProperties().remove( "http.nonProxyHosts" );

        System.getProperties().remove( "ftp.proxyHost"     );
        System.getProperties().remove( "ftp.proxyPort"     );
        System.getProperties().remove( "ftp.nonProxyHosts" );
        
        Authenticator.setDefault( null );

        if( radProxy.isSelected() )
        {
            System.getProperties().put( "http.proxyHost", txtHttpHost.getText().trim() );
            System.getProperties().put( "http.proxyPort", txtHttpPort.getText().trim() );
            
            if( txtHttpExclude.getText().trim().length() > 0 )
                System.getProperties().put( "http.nonProxyHosts", txtHttpExclude.getText().trim() );
            
            System.getProperties().put( "ftp.proxyHost", txtFtpHost.getText().trim() );
            System.getProperties().put( "ftp.proxyPort", txtFtpPort.getText().trim() );
            
            if( txtFtpExclude.getText().trim().length() > 0 )
                System.getProperties().put( "ftp.nonProxyHosts", txtFtpExclude.getText().trim() );
         
            if( txtUser.getText().trim().length() > 0 )
            {
                String sUser = txtUser.getText().trim();
                String sPass = String.valueOf(txtPassword.getPassword());   // No trim in passwords
                Authenticator.setDefault( new TheAuthenticator( sUser, sPass ) );
            }
        }
    }
    
    private void setComponentsStatus( boolean bOnOff )
    {
        lblFtpExclude.setEnabled( bOnOff );
        lblFtpHost.setEnabled( bOnOff );
        lblFtpPort.setEnabled( bOnOff );
        lblHttpExclude.setEnabled( bOnOff );
        lblHttpHost.setEnabled( bOnOff );
        lblHttpPort.setEnabled( bOnOff );
        lblPassword.setEnabled( bOnOff );
        lblUser.setEnabled( bOnOff );
        
        pnlFTP.setEnabled( bOnOff );
        pnlHTTP.setEnabled( bOnOff );
        
        txtFtpExclude.setEnabled( bOnOff );
        txtFtpHost.setEnabled( bOnOff );
        txtFtpPort.setEnabled( bOnOff );
        txtHttpExclude.setEnabled( bOnOff );
        txtHttpHost.setEnabled( bOnOff );
        txtHttpPort.setEnabled( bOnOff );
        txtPassword.setEnabled( bOnOff );
        txtUser.setEnabled( bOnOff );
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        radGroup = new javax.swing.ButtonGroup();
        radDirect = new javax.swing.JRadioButton();
        radProxy = new javax.swing.JRadioButton();
        btnAccept = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();
        lblUser = new javax.swing.JLabel();
        txtUser = new javax.swing.JTextField();
        lblPassword = new javax.swing.JLabel();
        txtPassword = new javax.swing.JPasswordField();
        pnlHTTP = new javax.swing.JPanel();
        lblHttpHost = new javax.swing.JLabel();
        lblHttpPort = new javax.swing.JLabel();
        txtHttpHost = new javax.swing.JTextField();
        lblHttpExclude = new javax.swing.JLabel();
        txtHttpExclude = new javax.swing.JTextField();
        txtHttpPort = new javax.swing.JTextField();
        pnlFTP = new javax.swing.JPanel();
        lblFtpHost = new javax.swing.JLabel();
        lblFtpPort = new javax.swing.JLabel();
        txtFtpHost = new javax.swing.JTextField();
        lblFtpExclude = new javax.swing.JLabel();
        txtFtpExclude = new javax.swing.JTextField();
        txtFtpPort = new javax.swing.JTextField();

        radGroup.add(radDirect);
        radDirect.setText("Direct connection to Internet");
        radDirect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                radDirectActionPerformed(evt);
            }
        });

        radGroup.add(radProxy);
        radProxy.setText("Proxy connection to Internet");
        radProxy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                radProxyActionPerformed(evt);
            }
        });

        btnAccept.setText("Accept");
        btnAccept.setToolTipText("zxcvb");
        btnAccept.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAcceptActionPerformed(evt);
            }
        });

        btnCancel.setText("Cancel");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });

        lblUser.setText("User");

        lblPassword.setText("Password");

        pnlHTTP.setBorder(javax.swing.BorderFactory.createTitledBorder(" HTTP "));

        lblHttpHost.setText("Host");

        lblHttpPort.setText("Port");

        lblHttpExclude.setText("Exclude");

        javax.swing.GroupLayout pnlHTTPLayout = new javax.swing.GroupLayout(pnlHTTP);
        pnlHTTP.setLayout(pnlHTTPLayout);
        pnlHTTPLayout.setHorizontalGroup(
            pnlHTTPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlHTTPLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlHTTPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlHTTPLayout.createSequentialGroup()
                        .addComponent(lblHttpHost)
                        .addGap(28, 28, 28)
                        .addComponent(txtHttpHost, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(40, 40, 40)
                        .addComponent(lblHttpPort)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtHttpPort, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnlHTTPLayout.createSequentialGroup()
                        .addComponent(lblHttpExclude)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtHttpExclude, javax.swing.GroupLayout.DEFAULT_SIZE, 298, Short.MAX_VALUE)))
                .addContainerGap())
        );
        pnlHTTPLayout.setVerticalGroup(
            pnlHTTPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlHTTPLayout.createSequentialGroup()
                .addGroup(pnlHTTPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE, false)
                    .addComponent(lblHttpHost)
                    .addComponent(txtHttpHost, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtHttpPort, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblHttpPort))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnlHTTPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblHttpExclude)
                    .addComponent(txtHttpExclude, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        pnlFTP.setBorder(javax.swing.BorderFactory.createTitledBorder(" FTP"));

        lblFtpHost.setText("Host");

        lblFtpPort.setText("Port");

        lblFtpExclude.setText("Exclude");

        javax.swing.GroupLayout pnlFTPLayout = new javax.swing.GroupLayout(pnlFTP);
        pnlFTP.setLayout(pnlFTPLayout);
        pnlFTPLayout.setHorizontalGroup(
            pnlFTPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlFTPLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlFTPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlFTPLayout.createSequentialGroup()
                        .addComponent(lblFtpHost)
                        .addGap(28, 28, 28)
                        .addComponent(txtFtpHost, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 40, Short.MAX_VALUE)
                        .addComponent(lblFtpPort)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtFtpPort, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnlFTPLayout.createSequentialGroup()
                        .addComponent(lblFtpExclude)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtFtpExclude, javax.swing.GroupLayout.DEFAULT_SIZE, 298, Short.MAX_VALUE)))
                .addContainerGap())
        );
        pnlFTPLayout.setVerticalGroup(
            pnlFTPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlFTPLayout.createSequentialGroup()
                .addGroup(pnlFTPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblFtpHost)
                    .addComponent(txtFtpHost, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtFtpPort, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblFtpPort))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnlFTPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblFtpExclude)
                    .addComponent(txtFtpExclude, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(13, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(304, Short.MAX_VALUE)
                .addComponent(btnAccept)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnCancel)
                .addGap(18, 18, 18))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(16, 16, 16)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(radDirect)
                                .addComponent(radProxy))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                            .addGap(32, 32, 32)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(lblUser)
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(lblPassword)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(txtPassword, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 144, Short.MAX_VALUE)
                                        .addComponent(txtUser, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addGap(159, 159, 159))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                            .addGap(12, 12, 12)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(pnlHTTP, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(pnlFTP, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                    .addGap(16, 16, 16)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(372, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnAccept)
                    .addComponent(btnCancel))
                .addContainerGap())
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(15, 15, 15)
                    .addComponent(radDirect)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(radProxy)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(pnlHTTP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(pnlFTP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(18, 18, 18)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lblUser)
                        .addComponent(txtUser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblPassword))
                    .addContainerGap(47, Short.MAX_VALUE)))
        );
    }// </editor-fold>//GEN-END:initComponents
    private void radDirectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_radDirectActionPerformed
        setComponentsStatus( false );
    }//GEN-LAST:event_radDirectActionPerformed

    private void radProxyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_radProxyActionPerformed
        setComponentsStatus( true );
    }//GEN-LAST:event_radProxyActionPerformed

    private void btnAcceptActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAcceptActionPerformed
        setProxySettings();
        frame.close();
    }//GEN-LAST:event_btnAcceptActionPerformed

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        frame.close();
    }//GEN-LAST:event_btnCancelActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAccept;
    private javax.swing.JButton btnCancel;
    private javax.swing.JLabel lblFtpExclude;
    private javax.swing.JLabel lblFtpHost;
    private javax.swing.JLabel lblFtpPort;
    private javax.swing.JLabel lblHttpExclude;
    private javax.swing.JLabel lblHttpHost;
    private javax.swing.JLabel lblHttpPort;
    private javax.swing.JLabel lblPassword;
    private javax.swing.JLabel lblUser;
    private javax.swing.JPanel pnlFTP;
    private javax.swing.JPanel pnlHTTP;
    private javax.swing.JRadioButton radDirect;
    private javax.swing.ButtonGroup radGroup;
    private javax.swing.JRadioButton radProxy;
    private javax.swing.JTextField txtFtpExclude;
    private javax.swing.JTextField txtFtpHost;
    private javax.swing.JTextField txtFtpPort;
    private javax.swing.JTextField txtHttpExclude;
    private javax.swing.JTextField txtHttpHost;
    private javax.swing.JTextField txtHttpPort;
    private javax.swing.JPasswordField txtPassword;
    private javax.swing.JTextField txtUser;
    // End of variables declaration//GEN-END:variables

   //-------------------------------------------------------------------------//
   // INNER CLASS: MyAuthenticator
   //-------------------------------------------------------------------------//
   private final static class TheAuthenticator extends Authenticator
   {
      private final PasswordAuthentication pa;

      public TheAuthenticator( String sUser, String sPassword )
      {
         this.pa = new PasswordAuthentication( sUser, sPassword.toCharArray() );
      }

      protected PasswordAuthentication getPasswordAuthentication()
      {
         return this.pa;
      }
   }
}