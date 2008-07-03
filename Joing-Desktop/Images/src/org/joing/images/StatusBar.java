/*
 * (c) Telco Domótica S.L.
 * Todos los derechos reservados.
 * 
 * Creado el 06-sep-2005 a las 23:11:22
 */

package org.joing.images;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

class StatusBar extends Box
{
    private JLabel lblHelp, lblSize, lblRotation, lblScale;

    public StatusBar()
    {
        super( BoxLayout.X_AXIS );
        
        Dimension dimScreen = Toolkit.getDefaultToolkit().getScreenSize();
        Font      font      = new Font( "Dialog", Font.PLAIN, 10 );

        lblHelp = new JLabel( " ", SwingConstants.LEADING );
        lblHelp.setFont( font );
        lblHelp.setPreferredSize( new Dimension( (int) (0.6 * dimScreen.width), 22 ) );
        lblHelp.setBorder( BorderFactory.createLoweredBevelBorder() );
        add( lblHelp, null );

        lblSize = new JLabel( " ", SwingConstants.LEADING );
        lblSize.setFont( font );
        lblSize.setPreferredSize( new Dimension( (int) (0.2 * dimScreen.width), 22 ) );
        lblSize.setBorder( BorderFactory.createLoweredBevelBorder() );
        add( lblSize, null );
        
        lblScale = new JLabel( " ", SwingConstants.LEADING );
        lblScale.setFont( font );
        lblScale.setPreferredSize( new Dimension( (int) (0.1 * dimScreen.width), 22 ) );
        lblScale.setBorder( BorderFactory.createLoweredBevelBorder() );
        add( lblScale, null );
        
        lblRotation = new JLabel( " ", SwingConstants.LEADING );
        lblRotation.setFont( font );
        lblRotation.setPreferredSize( new Dimension( (int) (0.1 * dimScreen.width), 22 ) );
        lblRotation.setBorder( BorderFactory.createLoweredBevelBorder() );
        add( lblRotation, null );
        
        setHelp( null );
        setOriginalSize( null );
    }
    
    void setHelp( JComponent comp )
    {
        String sHelp = " ";
        
        if( comp != null )
            sHelp += comp.getToolTipText();
        
        lblHelp.setText( sHelp );
    }
    
    void setOriginalSize( WImage image )
    {
        Dimension dimSize = new Dimension();
        
        if( image != null )
            dimSize = image.getOriginalSize();
        
        lblSize.setText(  " Size: "+ dimSize.width +" x "+ dimSize.height +" Píxeles" );
    }
    
    void setRotation( WImage image )
    {
        int nRotation = 0;
        
        if( image != null )
            nRotation = image.getRotation();
        
        lblRotation.setText( " Rotation: "+ nRotation + "º" );
    }
    
    void setScale( WImage image )
    {
        int nScale = 0;
        
        if( image != null )
            nScale = image.getZoom();
        
        lblScale.setText( " Scale: "+ nScale +"%" );
    }
}