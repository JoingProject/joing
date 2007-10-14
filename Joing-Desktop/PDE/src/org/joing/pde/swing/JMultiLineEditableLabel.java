/*
 * JMultiLineEditableLabel.java
 *
 * Created on 12 de octubre de 2007, 16:56
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.joing.pde.swing;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import javax.swing.JTextArea;
import org.joing.api.desktop.Selectable;
import org.joing.pde.runtime.ColorSchema;

/**
 *
 * @author Francisco Morero Peyrona
 */
public class JMultiLineEditableLabel extends JTextArea implements Selectable
{
    private boolean bSelected;
    private int     nMaxLength;
    private Color   clrSelectedForeground;;
    private Color   clrUnSelectedForeground;
    private boolean bRound;
    
    //------------------------------------------------------------------------//
    
    /** Creates a new instance of JMultiLineEditableLabel */
    public JMultiLineEditableLabel()
    {
        setOpaque( false );
        setEditable( false );
        setMargin( new Insets( 2,3,2,3 ) );        
        setLineWrap( true );
        setWrapStyleWord( true );
        // TODO: mirarlos
        ///////setPreferredSize( new Dimension( PDEDeskLauncher.this.getPreferredSize().width + 6, nFONT_SIZE + 6 ) );
        ///////setMaximumSize( getPreferredSize() );
        ///////setMinimumSize( getPreferredSize() );
        
        setBackground( ColorSchema.getInstance().getDesktopLauncherBackground()  );  // Is not opaque
        setForeground( ColorSchema.getInstance().getDesktopLauncherForegroundUnSelected() );

        // Methods added by this class
        setMaxLenght( 255 );
        setSelectedForeground( ColorSchema.getInstance().getDesktopLauncherForegroundSelected() );
        setUnSelectedForeground( ColorSchema.getInstance().getDesktopLauncherForegroundUnSelected() );
        setSelected( false );
        setRound( true );
    }

    public void setText( String sText )
    {
        if( sText != null && sText.length() > getMaxLenght() )
            sText = sText.substring( 0, getMaxLenght() - 1 );
            
        super.setText( sText );
    }

    public boolean isSelected()
    {
        return bSelected;
    }

    public void setSelected( boolean b )
    {
        if( isSelected() != b )
        {
            bSelected  = b;
            setOpaque( b );
            // Not need to change background (can be visible or not, but it is always the same color)
            setForeground( (b ? clrSelectedForeground : clrUnSelectedForeground) );
            repaint();
        }
    }
    
    public void edit( boolean b )
    {
        setOpaque( b );
        setEditable( b );

        if( b )
        {
            selectAll();
            grabFocus();
        }
    }
    
    public int getMaxLenght()
    {
        return nMaxLength;
    }
    
    public void setMaxLenght( int nMaxLength )
    {
        this.nMaxLength = nMaxLength;
    }
    
    public Color getSelectedForeground()
    {
        return clrSelectedForeground;
    }
    
    public void setSelectedForeground( Color color )
    {
        this.clrSelectedForeground = color;
        setCaretColor( color );
    }
    
    public Color getUnSelectedForeground()
    {
        return clrUnSelectedForeground;
    }
    
    public void setUnSelectedForeground( Color color )
    {
        this.clrUnSelectedForeground = color;
    }
    
    public boolean isRound()
    {
        return bRound;
    }
    
    public void setRound( boolean bRound )
    {
        if( bRound != isRound() )
        {
            this.bRound = bRound;
            repaint();
        }
    }
    
    //------------------------------------------------------------------------//
    
    protected void paintComponent( Graphics g )
    {
        if( isRound() && isOpaque() )
        {
            int width  = getWidth();
            int height = getHeight();
         
            ((Graphics2D) g).setRenderingHint( RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON );
            g.setColor( getBackground() );
            g.fillRoundRect( 0, 0, width, height, 10, 10 );
        }
        
        // If not opaque, super paints again backgroud (makes it square)
        boolean bOpaque = isOpaque();
        setOpaque( false );
        super.paintComponent( g );
        setOpaque( bOpaque );
    }
}