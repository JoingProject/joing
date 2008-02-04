/*
 * EditUser.java
 *
 * Created on 23 de noviembre de 2007, 10:44
 */
package org.joing.pde.misce.apps;

import javax.swing.JPanel;
import org.joing.common.desktopAPI.DeskComponent;
import org.joing.common.desktopAPI.pane.DeskFrame;
import org.joing.common.dto.user.User;

/**
 *
 * @author  fmorero
 */
public class EditUser extends JPanel implements DeskComponent
{
    /** Creates new form EditUser */
    private DeskFrame frame;
    private User      user;
    
    /** Creates new form EditUser */
    public EditUser()
    {
        initComponents();
        user = org.joing.jvmm.RuntimeFactory.getPlatform().getBridge().getUserBridge().getUser();
    }
    
    public void showFrame()
    {
        String sIcon = "user_"+ (user.isMale() ? "" : "fe") +"male";
        
        frame = org.joing.jvmm.RuntimeFactory.getPlatform().getDesktopManager().getRuntime().createFrame();
        frame.setTitle( "User Information" );
        frame.setIcon( org.joing.jvmm.RuntimeFactory.getPlatform().getDesktopManager().getRuntime().getStandardImage( sIcon, 20, 20 ) );
        frame.add( (DeskComponent) this );
        
        org.joing.jvmm.RuntimeFactory.getPlatform().getDesktopManager().getDesktop().getActiveWorkArea().add( frame );
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnAccpet = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();
        tabbedPanel = new javax.swing.JTabbedPane();
        pnlBasic = new javax.swing.JPanel();
        lblName = new javax.swing.JLabel();
        lblGender = new javax.swing.JLabel();
        txtFirstName = new javax.swing.JTextField();
        txtSecondName = new javax.swing.JTextField();
        radMale = new javax.swing.JRadioButton();
        radFemale = new javax.swing.JRadioButton();
        lblPassword = new javax.swing.JLabel();
        txtPassword1 = new javax.swing.JTextField();
        txtPassword2 = new javax.swing.JTextField();
        lblRetype = new javax.swing.JLabel();
        pnlAddress = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tctAddress = new javax.swing.JTextArea();
        lblAddress = new javax.swing.JLabel();
        lblCity = new javax.swing.JLabel();
        txtCity = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        txtZipCode = new javax.swing.JTextField();
        txtCountry = new javax.swing.JTextField();
        lblCountry = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtSate = new javax.swing.JTextField();
        pnlContact = new javax.swing.JPanel();
        lblEmailJoing = new javax.swing.JLabel();
        txtEmailJoing = new javax.swing.JTextField();
        lblEmailAnother = new javax.swing.JLabel();
        txtEmailAnother = new javax.swing.JTextField();
        lblWebPage = new javax.swing.JLabel();
        txtWebPage = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txtType1 = new javax.swing.JTextField();
        lblType1 = new javax.swing.JLabel();
        txtPhone1 = new javax.swing.JTextField();
        lblIM1 = new javax.swing.JLabel();
        txtIM1 = new javax.swing.JTextField();
        lblIM2 = new javax.swing.JLabel();
        txtIM2 = new javax.swing.JTextField();
        lblPhone2 = new javax.swing.JLabel();
        txtPhone2 = new javax.swing.JTextField();
        lblType2 = new javax.swing.JLabel();
        txtType2 = new javax.swing.JTextField();
        lblPhone3 = new javax.swing.JLabel();
        txtPhone3 = new javax.swing.JTextField();
        txtType3 = new javax.swing.JTextField();
        lblType3 = new javax.swing.JLabel();

        btnAccpet.setText("Accept");
        btnAccpet.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAccpetonAccept(evt);
            }
        });

        btnCancel.setText("Cancel");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelonCancel(evt);
            }
        });

        lblName.setText("Name");

        lblGender.setText("Gender");

        radMale.setText("Male");

        radFemale.setText("Female");

        lblPassword.setText("Password");

        lblRetype.setText("Re-type");

        javax.swing.GroupLayout pnlBasicLayout = new javax.swing.GroupLayout(pnlBasic);
        pnlBasic.setLayout(pnlBasicLayout);
        pnlBasicLayout.setHorizontalGroup(
            pnlBasicLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlBasicLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlBasicLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlBasicLayout.createSequentialGroup()
                        .addComponent(lblName)
                        .addGap(18, 18, 18)
                        .addComponent(txtFirstName, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtSecondName, javax.swing.GroupLayout.DEFAULT_SIZE, 211, Short.MAX_VALUE))
                    .addGroup(pnlBasicLayout.createSequentialGroup()
                        .addGroup(pnlBasicLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblPassword)
                            .addComponent(lblRetype))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pnlBasicLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtPassword1)
                            .addComponent(txtPassword2, javax.swing.GroupLayout.DEFAULT_SIZE, 153, Short.MAX_VALUE)))
                    .addGroup(pnlBasicLayout.createSequentialGroup()
                        .addComponent(lblGender)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(radMale)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(radFemale)))
                .addContainerGap())
        );
        pnlBasicLayout.setVerticalGroup(
            pnlBasicLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlBasicLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlBasicLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblName)
                    .addComponent(txtFirstName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtSecondName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlBasicLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblPassword)
                    .addComponent(txtPassword1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(5, 5, 5)
                .addGroup(pnlBasicLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblRetype)
                    .addComponent(txtPassword2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlBasicLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblGender)
                    .addComponent(radMale)
                    .addComponent(radFemale))
                .addContainerGap(150, Short.MAX_VALUE))
        );

        tabbedPanel.addTab("Basic", pnlBasic);

        tctAddress.setColumns(20);
        tctAddress.setRows(5);
        jScrollPane1.setViewportView(tctAddress);

        lblAddress.setText("Address");

        lblCity.setText("City");

        jLabel1.setText("ZIP Code");

        lblCountry.setText("Country");

        jLabel2.setText("State");

        javax.swing.GroupLayout pnlAddressLayout = new javax.swing.GroupLayout(pnlAddress);
        pnlAddress.setLayout(pnlAddressLayout);
        pnlAddressLayout.setHorizontalGroup(
            pnlAddressLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlAddressLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlAddressLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 385, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, pnlAddressLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlAddressLayout.createSequentialGroup()
                            .addGroup(pnlAddressLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addGroup(pnlAddressLayout.createSequentialGroup()
                                    .addComponent(lblCity)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(txtCity, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(pnlAddressLayout.createSequentialGroup()
                                    .addComponent(jLabel2)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(txtSate)))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(pnlAddressLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel1)
                                .addComponent(lblCountry))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(pnlAddressLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(txtZipCode)
                                .addComponent(txtCountry, javax.swing.GroupLayout.DEFAULT_SIZE, 103, Short.MAX_VALUE)))
                        .addComponent(lblAddress)))
                .addContainerGap())
        );
        pnlAddressLayout.setVerticalGroup(
            pnlAddressLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlAddressLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblAddress)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 166, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlAddressLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblCity)
                    .addComponent(txtCity, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1)
                    .addComponent(txtZipCode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(9, 9, 9)
                .addGroup(pnlAddressLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtSate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblCountry)
                    .addComponent(txtCountry, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        tabbedPanel.addTab("Address", pnlAddress);

        lblEmailJoing.setText("email (Join'g)");

        lblEmailAnother.setText("email (another)");

        lblWebPage.setText("Web page");

        jLabel3.setText("Phone 1");

        lblType1.setText("Type 1");

        lblIM1.setText("Instant message 1");

        lblIM2.setText("Instant message 2");

        lblPhone2.setText("Phone 2");

        lblType2.setText("Type 2");

        lblPhone3.setText("Phone 3");

        lblType3.setText("Type 3");

        javax.swing.GroupLayout pnlContactLayout = new javax.swing.GroupLayout(pnlContact);
        pnlContact.setLayout(pnlContactLayout);
        pnlContactLayout.setHorizontalGroup(
            pnlContactLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlContactLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlContactLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlContactLayout.createSequentialGroup()
                        .addComponent(lblEmailJoing)
                        .addGap(24, 24, 24)
                        .addComponent(txtEmailJoing, javax.swing.GroupLayout.DEFAULT_SIZE, 279, Short.MAX_VALUE))
                    .addGroup(pnlContactLayout.createSequentialGroup()
                        .addGroup(pnlContactLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblEmailAnother)
                            .addComponent(lblWebPage))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pnlContactLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtWebPage, javax.swing.GroupLayout.DEFAULT_SIZE, 274, Short.MAX_VALUE)
                            .addComponent(txtEmailAnother, javax.swing.GroupLayout.DEFAULT_SIZE, 274, Short.MAX_VALUE)))
                    .addGroup(pnlContactLayout.createSequentialGroup()
                        .addComponent(lblIM1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtIM1, javax.swing.GroupLayout.DEFAULT_SIZE, 254, Short.MAX_VALUE))
                    .addGroup(pnlContactLayout.createSequentialGroup()
                        .addComponent(lblIM2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtIM2, javax.swing.GroupLayout.DEFAULT_SIZE, 254, Short.MAX_VALUE))
                    .addGroup(pnlContactLayout.createSequentialGroup()
                        .addGroup(pnlContactLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addComponent(lblPhone2)
                            .addComponent(lblPhone3))
                        .addGap(3, 3, 3)
                        .addGroup(pnlContactLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtPhone1, javax.swing.GroupLayout.DEFAULT_SIZE, 175, Short.MAX_VALUE)
                            .addComponent(txtPhone2, javax.swing.GroupLayout.DEFAULT_SIZE, 175, Short.MAX_VALUE)
                            .addComponent(txtPhone3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 175, Short.MAX_VALUE))
                        .addGap(12, 12, 12)
                        .addGroup(pnlContactLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblType1, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(lblType2, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(lblType3, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pnlContactLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtType1, javax.swing.GroupLayout.DEFAULT_SIZE, 89, Short.MAX_VALUE)
                            .addComponent(txtType2)
                            .addComponent(txtType3))))
                .addContainerGap())
        );
        pnlContactLayout.setVerticalGroup(
            pnlContactLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlContactLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlContactLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblEmailJoing)
                    .addComponent(txtEmailJoing, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlContactLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblEmailAnother)
                    .addComponent(txtEmailAnother, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlContactLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblWebPage)
                    .addComponent(txtWebPage, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlContactLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblIM1)
                    .addComponent(txtIM1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlContactLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblIM2)
                    .addComponent(txtIM2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlContactLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txtType1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblType1)
                    .addComponent(txtPhone1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlContactLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblPhone2)
                    .addComponent(lblType2)
                    .addComponent(txtType2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtPhone2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlContactLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlContactLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lblPhone3)
                        .addComponent(txtType3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtPhone3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(lblType3))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        tabbedPanel.addTab("Contact", pnlContact);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 441, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                            .addComponent(btnAccpet)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(btnCancel))
                        .addComponent(tabbedPanel))
                    .addContainerGap()))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 381, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(tabbedPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 9, Short.MAX_VALUE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnAccpet)
                        .addComponent(btnCancel))
                    .addContainerGap()))
        );
    }// </editor-fold>//GEN-END:initComponents
    private void btnAccpetonAccept(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAccpetonAccept
        // TODO: Guardar los cambios en el servidor
        frame.close();
    }//GEN-LAST:event_btnAccpetonAccept

    private void btnCancelonCancel(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelonCancel
        frame.close();
    }//GEN-LAST:event_btnCancelonCancel
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAccpet;
    private javax.swing.JButton btnCancel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblAddress;
    private javax.swing.JLabel lblCity;
    private javax.swing.JLabel lblCountry;
    private javax.swing.JLabel lblEmailAnother;
    private javax.swing.JLabel lblEmailJoing;
    private javax.swing.JLabel lblGender;
    private javax.swing.JLabel lblIM1;
    private javax.swing.JLabel lblIM2;
    private javax.swing.JLabel lblName;
    private javax.swing.JLabel lblPassword;
    private javax.swing.JLabel lblPhone2;
    private javax.swing.JLabel lblPhone3;
    private javax.swing.JLabel lblRetype;
    private javax.swing.JLabel lblType1;
    private javax.swing.JLabel lblType2;
    private javax.swing.JLabel lblType3;
    private javax.swing.JLabel lblWebPage;
    private javax.swing.JPanel pnlAddress;
    private javax.swing.JPanel pnlBasic;
    private javax.swing.JPanel pnlContact;
    private javax.swing.JRadioButton radFemale;
    private javax.swing.JRadioButton radMale;
    private javax.swing.JTabbedPane tabbedPanel;
    private javax.swing.JTextArea tctAddress;
    private javax.swing.JTextField txtCity;
    private javax.swing.JTextField txtCountry;
    private javax.swing.JTextField txtEmailAnother;
    private javax.swing.JTextField txtEmailJoing;
    private javax.swing.JTextField txtFirstName;
    private javax.swing.JTextField txtIM1;
    private javax.swing.JTextField txtIM2;
    private javax.swing.JTextField txtPassword1;
    private javax.swing.JTextField txtPassword2;
    private javax.swing.JTextField txtPhone1;
    private javax.swing.JTextField txtPhone2;
    private javax.swing.JTextField txtPhone3;
    private javax.swing.JTextField txtSate;
    private javax.swing.JTextField txtSecondName;
    private javax.swing.JTextField txtType1;
    private javax.swing.JTextField txtType2;
    private javax.swing.JTextField txtType3;
    private javax.swing.JTextField txtWebPage;
    private javax.swing.JTextField txtZipCode;
    // End of variables declaration//GEN-END:variables
}