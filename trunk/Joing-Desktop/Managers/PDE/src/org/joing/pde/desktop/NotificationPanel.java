/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.joing.pde.desktop;

import org.joing.pde.*;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import org.joing.common.desktopAPI.StandardImage;
import org.joing.common.desktopAPI.workarea.WorkArea;
import org.joing.pde.desktop.container.PDECanvas;

/**
 * An information panel indicating that a connection with server is in progress.
 * 
 * Note: Package scope to be used only by PDEManager (in fact it should be an
 * innner class of PDEManager but I prefer to place it in a separate file).
 * 
 * @author Francisco Morero Peyrona.
 */
class NotificationPanel extends PDECanvas
{
    public NotificationPanel()
    {
        this( null );
    }
    
    public NotificationPanel( String sMessage )
    {
        this( sMessage, null );
    }
    
    public NotificationPanel( String sMessage, Image icon )
    {
        this( sMessage, icon, false );
    }
    
    public NotificationPanel( String sMessage, Image icon, boolean bShowAnimation )
    {
        JLabel lblMessage = new JLabel();
        JLabel lblIcon    = new JLabel();
        JPanel panel      = new JPanel( new BorderLayout( 8,6 ) );
        
        if( sMessage == null )
            sMessage = "Operation in progress";
        
        lblMessage.setText( sMessage );
        lblMessage.setHorizontalTextPosition( JLabel.CENTER );
        
        if( icon == null )
            icon = PDEUtilities.getDesktopManager().getRuntime().getImage( StandardImage.NOTIFICATION );
        
        lblIcon.setIcon( new ImageIcon( icon ) );
        
        panel.setOpaque( false );
        panel.setBorder( new EmptyBorder( 3,5,3,5 ) );
        panel.add( lblMessage, BorderLayout.CENTER );
        panel.add( lblIcon   , BorderLayout.WEST   );
        
        if( bShowAnimation )
        {
            JProgressBar progress = new JProgressBar();
                         progress.setIndeterminate( true );
            panel.add( progress, BorderLayout.SOUTH  );    
        }
        
        setTranslucency( 12 );
        setBorder( new LineBorder( Color.darkGray, 2 ) );
        setBounds();
        add( panel );
    }
    
    public void animate()
    {
        // TODO: Hacer la animación
    }
    
    //------------------------------------------------------------------------//
    
    // Calculates size and postion
    private void setBounds()
    {
        WorkArea wa = PDEUtilities.getDesktopManager().getDesktop().getActiveWorkArea();
        
        setSize( 205, 70 );    // FIXME: Buscar su tamaño, en lugar de 180,70
        setLocation( wa.getWidth() - getWidth(), wa.getHeight() - getHeight() - 5 );   // NEXT: no sé por qué tengo que hace rel -5
    }
}