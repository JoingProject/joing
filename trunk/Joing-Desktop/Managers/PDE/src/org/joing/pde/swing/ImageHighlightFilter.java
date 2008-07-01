/*
 * ImageHighlightFilter.java
 *
 * Created on 10 de febrero de 2007, 21:44
 *
 * (c) 2006 - Francisco Morero Peyrona
 *
 * License: {license}
 */

package org.joing.pde.swing;

import java.awt.image.RGBImageFilter;

/**
 *
 * @author Francisco Morero Peyrona
 */
public class ImageHighlightFilter extends RGBImageFilter
{
    private boolean brighter;
    private int     amount;
    
    //------------------------------------------------------------------------//
    
    /**
     * Filter to apply to images.
     * 
     * @param brighter  true to highlight and false to de-highlight.
     * @param percent   Percent to highlight or de-highlight.
     */
    public ImageHighlightFilter( boolean brighter, int percent )
    {
        if( percent > 100 ) percent = 100;
        if( percent <   0 ) percent =   0;
        
        this.brighter = brighter;
        this.amount   = 100 - percent;
        
        canFilterIndexColorModel = true;
    }

    public int filterRGB( int x, int y, int rgb )
    {
        int r = (rgb >> 16) & 0xff;
        int g = (rgb >>  8) & 0xff;
        int b = (rgb >>  0) & 0xff;

        if( brighter )
        {
            r = 255 - (((255 - r) * amount) / 100);
            g = 255 - (((255 - g) * amount) / 100);
            b = 255 - (((255 - b) * amount) / 100);
        }
        else
        {
            r = (r * amount) / 100;
            g = (g * amount) / 100;
            b = (b * amount) / 100;
        }
// TODO: Revisar esto, pero creo que como he añadido en el contructor el control
//       del paramaetro percent para que no salga de marge, esto ya no hace falta.
//        if( r < 0 )    r = 0;
//        if( r > 255 )  r = 255;
//        if( g < 0 )    g = 0;
//        if( g > 255 )  g = 255;
//        if( b < 0 )    b = 0;
//        if( b > 255 )  b = 255;
        
        return (rgb & 0xff000000) | (r << 16) | (g << 8) | (b << 0);
    }
}