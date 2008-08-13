/* 
 * Copyright (C) 2007, 2008 Join'g Team Members.  All Rights Reserved.
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
package org.joing.images.images;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;

import javax.swing.JComponent;
import javax.swing.JViewport;

/**
 * 
 * @author Francisco Morero Peyrona
 */
public class WImage extends JComponent
{
    private Image     image       = null;
    private boolean   bStretch    = false;
    private int       nRotate     = 0;
    private Dimension dimOriginal = null;
    private Dimension dimCurrent  = null; // No puedo usar getSize() porque al estar este comp dentro del
                                          // JScrollPane (quien está en un BorderLayout), este comp toma
                                          // siempre la dimensión de su padre: al final de la ventana.
    //------------------------------------------------------------------------//
    
    public WImage()
    {
        this( (Image) null ); 
    }
    
    public WImage( Image image )
    {
        if( image != null )
        {
            this.image       = image;
            this.dimOriginal = new Dimension( image.getWidth( this ), image.getHeight( this ) );
            this.dimCurrent  = (Dimension) this.dimOriginal.clone();
        }
    }

    public Dimension getOriginalSize()
    {
        return this.dimOriginal;
    }
    
    public Dimension getCurrentSize()
    {
        return this.dimCurrent;
    }

    public Dimension getPreferredSize()
    {
        return this.dimCurrent;
    }
    
    /**
     * 
     * @return Zoomed status as percentage.
     */
    public int getZoom()
    {
        return this.dimCurrent.width * 100 / this.dimOriginal.width;   // Lo mismo da coger el with o heigth
    }
    
    public void incrZoom( int nPercent )
    {
        if( nPercent == 0 )
        {
            this.dimCurrent = (Dimension) this.dimOriginal.clone();
        }
        else
        {
            Double incr = 1 + (nPercent / 100d);
            this.dimCurrent.width  *= incr ;
            this.dimCurrent.height *= incr;
        }
            
        this.bStretch = false;    // Hay q quitar es stretched        
        repaint();
    }
    
    /**
     * No es q no pueda incrementar su tamaño, es q no se aconseja q se haga
     * @return
     */
    public boolean canDecreaseZoom()
    {
        return this.dimCurrent.width  > 10
               &&
               this.dimCurrent.height > 10;
    }
    
    /**
     * No es q no pueda decrementar su tamaño, es q no se aconseja q se haga
     * @return
     */
    public boolean canIncreaseZoom()
    {
        return (getZoom() <= 1500);
    }
    
    boolean isStretched()
    {
        return this.bStretch;
    }
    
    /**
     * 
     * @param b  <code>true</code> Stretch, <code>false</code> current size.
     */
    public void setStretched( boolean b )
    {
        this.bStretch   = b;
        this.dimCurrent = getSize();   // Hay q ponerlo tb aquí porque la sig lin (repaint()) se pone
        repaint();                     // en una thread y este método vuelve, y entonces se llama a
    }                                  // ToolBar.updateButtons(), pero dimCurrent aún no ha cambiado,
                                       // por lo q el btn de "1:1" no se pone enabled aunq debiera.
    public int getRotation()
    {
       return this.nRotate;
    }
    
    public void incrRotation( int nGrades )
    {
        this.nRotate += nGrades;
        this.nRotate  = nRotate % 360;
        repaint();
    }
    
    //------------------------------------------------------------------------//

    protected void paintComponent( Graphics g )
    {
        super.paintComponent( g );
        
        if( this.image == null )
            return;
         
        if( this.bStretch )                  // Hay q ponerlo aqui porque cuando la ventana se resiza
            this.dimCurrent = getSize();     // llama a layout.revalidate() (o algo así) quien llama 
                                             // al paint(...) (osea, aquí) de todos los componentes.
        Graphics2D g2d = (Graphics2D) g;
        int        nX  = (getWidth()  - this.dimCurrent.width)  / 2;
        int        nY  = (getHeight() - this.dimCurrent.height) / 2;
         
        AffineTransform tx = new AffineTransform();
         
        if( getRotation() !=  0 )
        {
            tx.rotate( Math.toRadians( getRotation() ), nX+this.dimCurrent.width/2, nY+this.dimCurrent.height/2 );
            g2d.transform( tx );
        }
         
        g2d.drawImage( this.image, nX, nY, this.dimCurrent.width, this.dimCurrent.height, this );
        
        // Para q aparezcan y desaparezcan las barras del JScrollPane
        Component parent = getParent();
        
        if( parent instanceof JViewport )
            parent.invalidate();
    }
}