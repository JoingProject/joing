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
package org.joing.pde.desktop.workarea;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;
import org.joing.common.desktopAPI.workarea.WorkArea;

/**
 *
 * @author  fmorero
 */
public class PDEWorkAreaProperties extends javax.swing.JPanel
{
    private WorkArea waParent;
    
    /** Creates new form PDEWorkAreaProperties */
    public PDEWorkAreaProperties( WorkArea waParent )
    {
        this.waParent = waParent;
        
        initComponents();
        
        // Cambio las JLabel por mis GradientLabel
        lblGradientHorizontal           = new GradientLabel( GradientType.HORZ );
        lblGradientVertical             = new GradientLabel( GradientType.VERT );
        lblGradientTopLeftToBottomRight = new GradientLabel( GradientType.TLBR );
        lblGradientTopRightToBottomLeft = new GradientLabel( GradientType.TRBL );
        
        initValues();
    }

    public void applyChanges()
    {
        // TODO: Aplicar cambios y escribir valores en el fichero de configuración
        
//        UIManager.LookAndFeelInfo lafi = (UIManager.LookAndFeelInfo) cmbLookAndFeel.getClientProperty( cmbLookAndFeel.getSelectedItem() );
//        String sSelectedLAFClassName = lafi.getClassName();
//        
//        if( ! UIManager.getLookAndFeel().getClass().getName().equals( sSelectedLAFClassName ) )
//        {
//            JFrame frmMain = (JFrame) SwingUtilities.getAncestorOfClass( JFrame.class, this );
//            try
//            { 
//                UIManager.setLookAndFeel( lafi.getClassName() );
//            } 
//            catch( Exception exc )
//            {
//                org.joing.jvmm.RuntimeFactory.getPlatform().getDesktopManager().getRuntime().showException( exc, "Error" );
//            }
//            SwingUtilities.updateComponentTreeUI( frmMain );
//            frmMain.pack();
//        }
    }
    
    //------------------------------------------------------------------------//
    
    private void initValues()
    {   // TODO: leer los valores iniciales del workarea (porque el fichero es manejado desde la clase que invoca a esta)
        UIManager.LookAndFeelInfo[] aLAF = UIManager.getInstalledLookAndFeels();
        
        for( UIManager.LookAndFeelInfo laf : aLAF  )
        {
            cmbLookAndFeel.addItem( laf.getName() );
            cmbLookAndFeel.putClientProperty( laf.getName(), laf );
        }
        
        chkColorGradient.setSelected( false );
        radGradientHorizontal.setSelected( true );
        chkColorGradientActionPerformed( null );
    }
    
    //------------------------------------------------------------------------//
    // INNER CLASS Special JLabel to paint gradient
    //------------------------------------------------------------------------//
    
    private enum GradientType { HORZ, VERT, TLBR, TRBL }
    
    final class GradientLabel extends JLabel
    {
        private GradientType type;
        
        GradientLabel( GradientType type )
        {
            this.type = type;
            setBorder( new LineBorder( Color.black ) );
            setFocusable( false );
            setSize( 40, 40 );
            setOpaque( true );
        }
        
        public void setEnabled( boolean b )
        {
            //setOpaque( b );
            super.setEnabled( b );
        }
        
        public void paintComponent( Graphics g )
        {
            super.paintComponent( g );
            
            if( isOpaque() )
            {
                Graphics2D g2d = (Graphics2D) g;
                           g2d.setPaint( new GradientPaint( 1, 1, Color.BLUE,
                                                 getWidth()-2, getHeight()-2, Color.RED ) );

                switch( type )
                {
                    case HORZ: break;
                    case VERT: break;
                    case TLBR: break;
                    case TRBL: break;
                }
                
                g2d.fillRect( 1, 1, getWidth()-2, getHeight()-2 );   // Has 1 pixel border
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

        radGroupImageStyle = new javax.swing.ButtonGroup();
        radGroupGradient = new javax.swing.ButtonGroup();
        lblName = new javax.swing.JLabel();
        txtName = new javax.swing.JTextField();
        pnlImage = new javax.swing.JPanel();
        btnImage = new javax.swing.JButton();
        radImageCenter = new javax.swing.JRadioButton();
        radImageStretch = new javax.swing.JRadioButton();
        radImageMosaic = new javax.swing.JRadioButton();
        pnlColor = new javax.swing.JPanel();
        lblColorFrom = new javax.swing.JLabel();
        btnColorFrom = new javax.swing.JButton();
        lblColorTo = new javax.swing.JLabel();
        btnColorTo = new javax.swing.JButton();
        chkColorGradient = new javax.swing.JCheckBox();
        pnlGradientChooser = new javax.swing.JPanel();
        radGradientHorizontal = new javax.swing.JRadioButton();
        lblGradientHorizontal = new javax.swing.JLabel();
        radGradientVertical = new javax.swing.JRadioButton();
        lblGradientVertical = new javax.swing.JLabel();
        radGradientTopLeftToBottomRight = new javax.swing.JRadioButton();
        lblGradientTopLeftToBottomRight = new javax.swing.JLabel();
        radGradientTopRightToBottomLeft = new javax.swing.JRadioButton();
        lblGradientTopRightToBottomLeft = new javax.swing.JLabel();
        lblLookAndFeel = new javax.swing.JLabel();
        cmbLookAndFeel = new javax.swing.JComboBox();

        lblName.setText("Name");

        pnlImage.setBorder(javax.swing.BorderFactory.createTitledBorder(" Background Image "));

        radGroupImageStyle.add(radImageCenter);
        radImageCenter.setText("Center");

        radGroupImageStyle.add(radImageStretch);
        radImageStretch.setText("Stretch");

        radGroupImageStyle.add(radImageMosaic);
        radImageMosaic.setText("Mosaic");

        javax.swing.GroupLayout pnlImageLayout = new javax.swing.GroupLayout(pnlImage);
        pnlImage.setLayout(pnlImageLayout);
        pnlImageLayout.setHorizontalGroup(
            pnlImageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlImageLayout.createSequentialGroup()
                .addGap(132, 132, 132)
                .addGroup(pnlImageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(radImageMosaic)
                    .addComponent(radImageStretch)
                    .addComponent(radImageCenter))
                .addContainerGap(131, Short.MAX_VALUE))
            .addGroup(pnlImageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(pnlImageLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(btnImage, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(227, Short.MAX_VALUE)))
        );
        pnlImageLayout.setVerticalGroup(
            pnlImageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlImageLayout.createSequentialGroup()
                .addContainerGap(14, Short.MAX_VALUE)
                .addComponent(radImageCenter)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(radImageStretch)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(radImageMosaic)
                .addContainerGap())
            .addGroup(pnlImageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(pnlImageLayout.createSequentialGroup()
                    .addGap(11, 11, 11)
                    .addComponent(btnImage, javax.swing.GroupLayout.DEFAULT_SIZE, 82, Short.MAX_VALUE)
                    .addGap(11, 11, 11)))
        );

        pnlColor.setBorder(javax.swing.BorderFactory.createTitledBorder(" Background Color "));

        lblColorFrom.setText("From");

        btnColorFrom.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnColorActionPerformed(evt);
            }
        });

        lblColorTo.setText("To");

        btnColorTo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnColorActionPerformed(evt);
            }
        });

        chkColorGradient.setText("Gradient");
        chkColorGradient.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkColorGradientActionPerformed(evt);
            }
        });

        radGroupGradient.add(radGradientHorizontal);

        lblGradientHorizontal.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        lblGradientHorizontal.setFocusable(false);

        radGroupGradient.add(radGradientVertical);

        lblGradientVertical.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        lblGradientVertical.setFocusable(false);

        radGroupGradient.add(radGradientTopLeftToBottomRight);

        lblGradientTopLeftToBottomRight.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        lblGradientTopLeftToBottomRight.setFocusable(false);

        radGroupGradient.add(radGradientTopRightToBottomLeft);

        lblGradientTopRightToBottomLeft.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        lblGradientTopRightToBottomLeft.setFocusable(false);

        javax.swing.GroupLayout pnlGradientChooserLayout = new javax.swing.GroupLayout(pnlGradientChooser);
        pnlGradientChooser.setLayout(pnlGradientChooserLayout);
        pnlGradientChooserLayout.setHorizontalGroup(
            pnlGradientChooserLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlGradientChooserLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(radGradientHorizontal)
                .addGap(3, 3, 3)
                .addComponent(lblGradientHorizontal, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(radGradientVertical)
                .addGap(1, 1, 1)
                .addComponent(lblGradientVertical, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(radGradientTopLeftToBottomRight)
                .addGap(6, 6, 6)
                .addComponent(lblGradientTopLeftToBottomRight, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(radGradientTopRightToBottomLeft)
                .addGap(4, 4, 4)
                .addComponent(lblGradientTopRightToBottomLeft, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(23, Short.MAX_VALUE))
        );
        pnlGradientChooserLayout.setVerticalGroup(
            pnlGradientChooserLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlGradientChooserLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                .addComponent(radGradientHorizontal)
                .addComponent(radGradientVertical)
                .addComponent(lblGradientVertical, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(lblGradientTopLeftToBottomRight, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(lblGradientHorizontal, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(radGradientTopLeftToBottomRight)
                .addComponent(radGradientTopRightToBottomLeft)
                .addComponent(lblGradientTopRightToBottomLeft, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout pnlColorLayout = new javax.swing.GroupLayout(pnlColor);
        pnlColor.setLayout(pnlColorLayout);
        pnlColorLayout.setHorizontalGroup(
            pnlColorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlGradientChooser, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(pnlColorLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblColorFrom)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnColorFrom)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblColorTo)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnColorTo, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(chkColorGradient)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pnlColorLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {btnColorFrom, btnColorTo});

        pnlColorLayout.setVerticalGroup(
            pnlColorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlColorLayout.createSequentialGroup()
                .addContainerGap(14, Short.MAX_VALUE)
                .addGroup(pnlColorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(btnColorFrom)
                    .addComponent(lblColorTo)
                    .addComponent(btnColorTo, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(chkColorGradient)
                    .addComponent(lblColorFrom))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(pnlGradientChooser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pnlColorLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {btnColorFrom, btnColorTo});

        lblLookAndFeel.setText("L&F");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(pnlImage, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(pnlColor, javax.swing.GroupLayout.DEFAULT_SIZE, 346, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblName)
                            .addComponent(lblLookAndFeel))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cmbLookAndFeel, 0, 295, Short.MAX_VALUE)
                            .addComponent(txtName, javax.swing.GroupLayout.DEFAULT_SIZE, 295, Short.MAX_VALUE))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblName)
                    .addComponent(txtName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblLookAndFeel)
                    .addComponent(cmbLookAndFeel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 18, Short.MAX_VALUE)
                .addComponent(pnlImage, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlColor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents
    private void chkColorGradientActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkColorGradientActionPerformed
        boolean bSelected = chkColorGradient.isSelected();

        lblColorTo.setEnabled( bSelected );
        btnColorTo.setEnabled( bSelected );

        lblGradientHorizontal.setOpaque( bSelected );
        ((GradientLabel) lblGradientHorizontal).repaint();
        lblGradientVertical.setOpaque( bSelected );
        lblGradientVertical.repaint();
        lblGradientTopLeftToBottomRight.setOpaque( bSelected );
        lblGradientTopLeftToBottomRight.repaint();
        lblGradientTopRightToBottomLeft.setOpaque( bSelected );
        lblGradientTopRightToBottomLeft.repaint();

        radGradientHorizontal.setEnabled( bSelected );
        radGradientVertical.setEnabled( bSelected );
        radGradientTopLeftToBottomRight.setEnabled( bSelected );
        radGradientTopRightToBottomLeft.setEnabled( bSelected );
    }//GEN-LAST:event_chkColorGradientActionPerformed

    private void btnColorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnColorActionPerformed
// TODO: Hacerlo
//        JColorChooser color = new JColorChooser();
//        
//        ((JButton) evt.getSource()).setBackground( ... );
    }//GEN-LAST:event_btnColorActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnColorFrom;
    private javax.swing.JButton btnColorTo;
    private javax.swing.JButton btnImage;
    private javax.swing.JCheckBox chkColorGradient;
    private javax.swing.JComboBox cmbLookAndFeel;
    private javax.swing.JLabel lblColorFrom;
    private javax.swing.JLabel lblColorTo;
    private javax.swing.JLabel lblGradientHorizontal;
    private javax.swing.JLabel lblGradientTopLeftToBottomRight;
    private javax.swing.JLabel lblGradientTopRightToBottomLeft;
    private javax.swing.JLabel lblGradientVertical;
    private javax.swing.JLabel lblLookAndFeel;
    private javax.swing.JLabel lblName;
    private javax.swing.JPanel pnlColor;
    private javax.swing.JPanel pnlGradientChooser;
    private javax.swing.JPanel pnlImage;
    private javax.swing.JRadioButton radGradientHorizontal;
    private javax.swing.JRadioButton radGradientTopLeftToBottomRight;
    private javax.swing.JRadioButton radGradientTopRightToBottomLeft;
    private javax.swing.JRadioButton radGradientVertical;
    private javax.swing.ButtonGroup radGroupGradient;
    private javax.swing.ButtonGroup radGroupImageStyle;
    private javax.swing.JRadioButton radImageCenter;
    private javax.swing.JRadioButton radImageMosaic;
    private javax.swing.JRadioButton radImageStretch;
    private javax.swing.JTextField txtName;
    // End of variables declaration//GEN-END:variables
}
