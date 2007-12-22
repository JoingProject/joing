/*
 * WorkAreaProperties.java
 *
 * Created on 8 de diciembre de 2007, 8:12
 */
package org.joing.pde.desktop.workarea;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.border.LineBorder;

/**
 *
 * @author  fmorero
 */
public class WorkAreaProperties extends javax.swing.JPanel
{
    /** Creates new form WorkAreaProperties */
    public WorkAreaProperties()
    {
        initComponents();
        
        // Cambio las JLabel por mis GradientLabel
        lblGradientHorizontal           = new GradientLabel( GradientType.HORZ );
        lblGradientVertical             = new GradientLabel( GradientType.VERT );
        lblGradientTopLeftToBottomRight = new GradientLabel( GradientType.TLBR );
        lblGradientTopRightToBottomLeft = new GradientLabel( GradientType.TRBL );
        
        initValues();
    }

    //------------------------------------------------------------------------//
    
    private void initValues()
    {   // TODO: leerlo de donde sea (fichero o el propio WorkArea)
        chkColorGradient.setSelected( false );
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
    
    public static void main( String[] as )
    {
        JFrame frame = new JFrame();
        frame.add( new WorkAreaProperties() );
        frame.pack();
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        frame.setVisible( true );
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
        lblImage = new javax.swing.JLabel();
        btnImage = new javax.swing.JButton();
        radImageCenter = new javax.swing.JRadioButton();
        radImageStretch = new javax.swing.JRadioButton();
        radImageMosaic = new javax.swing.JRadioButton();
        lblColor = new javax.swing.JLabel();
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

        lblName.setText("Name");

        lblImage.setText("Background image");

        radGroupImageStyle.add(radImageCenter);
        radImageCenter.setText("Center");

        radGroupImageStyle.add(radImageStretch);
        radImageStretch.setText("Stretch");

        radGroupImageStyle.add(radImageMosaic);
        radImageMosaic.setText("Mosaic");

        lblColor.setText("Background color");

        lblColorFrom.setText("From");

        lblColorTo.setText("To");

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
                .addContainerGap(20, Short.MAX_VALUE))
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

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblColor)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(lblImage)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(12, 12, 12)
                                .addComponent(btnImage, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(radImageMosaic)
                            .addComponent(radImageStretch)
                            .addComponent(radImageCenter)))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                            .addComponent(lblName)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(txtName))
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                            .addGap(12, 12, 12)
                            .addComponent(lblColorFrom)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(btnColorFrom)
                            .addGap(18, 18, 18)
                            .addComponent(lblColorTo)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(btnColorTo, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(18, 18, 18)
                            .addComponent(chkColorGradient))))
                .addGap(21, 21, 21))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnlGradientChooser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {btnColorFrom, btnColorTo});

        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblName)
                    .addComponent(txtName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(46, 46, 46)
                .addComponent(lblImage)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(radImageCenter)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(radImageStretch)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(radImageMosaic))
                    .addComponent(btnImage, javax.swing.GroupLayout.DEFAULT_SIZE, 78, Short.MAX_VALUE))
                .addGap(44, 44, 44)
                .addComponent(lblColor)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(btnColorFrom)
                    .addComponent(lblColorTo)
                    .addComponent(btnColorTo, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(chkColorGradient)
                    .addComponent(lblColorFrom))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(pnlGradientChooser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {btnColorFrom, btnColorTo});

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

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnColorFrom;
    private javax.swing.JButton btnColorTo;
    private javax.swing.JButton btnImage;
    private javax.swing.JCheckBox chkColorGradient;
    private javax.swing.JLabel lblColor;
    private javax.swing.JLabel lblColorFrom;
    private javax.swing.JLabel lblColorTo;
    private javax.swing.JLabel lblGradientHorizontal;
    private javax.swing.JLabel lblGradientTopLeftToBottomRight;
    private javax.swing.JLabel lblGradientTopRightToBottomLeft;
    private javax.swing.JLabel lblGradientVertical;
    private javax.swing.JLabel lblImage;
    private javax.swing.JLabel lblName;
    private javax.swing.JPanel pnlGradientChooser;
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