/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.joing.pde;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import org.joing.common.desktopAPI.workarea.WorkArea;
import org.joing.pde.desktop.container.PDECanvas;
import org.joing.pde.misce.images.ImagesFactory;

/**
 * An information panel indicating that a connection with server is in progress.
 * 
 * Note: Package scope to be used only by PDEManager (in fact it should be an
 * innner class of PDEManager but I prefer to place it in a separate file).
 * 
 * @author Francisco Morero Peyrona.
 */
class ConnServerInfoPanel extends PDECanvas
{
    public ConnServerInfoPanel()
    {
        this( null );
    }
    
    public ConnServerInfoPanel( String sMessage )
    {
        this( sMessage, null );
    }
    
    public ConnServerInfoPanel( String sMessage, Image icon )
    {
        JLabel       lblMessage = new JLabel();
        JLabel       lblIcon    = new JLabel();
        JProgressBar progress   = new JProgressBar();
        JPanel       panel      = new JPanel( new BorderLayout( 8,6 ) );
        
        if( sMessage == null )
            sMessage = "Accessing Join'g Server";
        
        lblMessage.setText( sMessage );
        lblMessage.setHorizontalTextPosition( JLabel.CENTER );
        
        lblIcon.setIcon( (icon == null) ? PDEUtilities.getStandardIcon( ImagesFactory.Icon.CONN_SERVER ) :
                                          new ImageIcon( icon ) );
        
        progress.setIndeterminate( true );
        
        panel.setOpaque( false );
        panel.setBorder( new EmptyBorder( 3,5,3,5 ) );
        panel.add( lblMessage, BorderLayout.CENTER );
        panel.add( lblIcon   , BorderLayout.WEST   );
        panel.add( progress  , BorderLayout.SOUTH  );
        
        setTranslucency( 12 );
        setBorder( new LineBorder( Color.darkGray, 2 ) );
        setBounds();
        add( panel );
    }
    
    //------------------------------------------------------------------------//
    // TODO: Hacer la animación
    // Calculates size and postion
    private void setBounds()
    {
        WorkArea wa = org.joing.jvmm.RuntimeFactory.getPlatform().getDesktopManager().getDesktop().getActiveWorkArea();
        
        setSize( 205, 70 );    // TODO: Buscar su tamaño, en lugar de 180,70
        setLocation( wa.getWidth() - getWidth(), wa.getHeight() - getHeight() - 5 );   // NEXT: no sé por qué tengo que hace rel -5
    }
}