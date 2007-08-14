/*
 * (c) Telco Domótica S.L.
 * Todos los derechos reservados.
 * 
 * Creado el 04-ago-2005 a las 18:18:50
 */

package org.joing.desktop.impl.util;

import java.awt.Color;
import java.awt.image.RGBImageFilter;

/**
 * Creates a <code>RGBImageFilter</code> that changes the image to the desired color.
 * <p>
 * Exmaple of use:
 * <pre>
 *    Image src = getImage("doc:///demo/images/duke/T1.gif");
 *    ColorFilter colorfilter = new ColorFilter(Color.color);  // whatever color
 *    Image img = createImage(new FilteredImageSource(src.getSource(), colorfilter));
 * </pre>
 * Reference:<br>
 *    http://java.sun.com/j2se/1.4.2/docs/api/java/awt/image/FilteredImageSource.html
 */
public class ImageColorFilter extends RGBImageFilter
{
    /**
     * The hue to which the image should changed.
     */
    private float hue;

    /**
     * Creates a new <code>ColorFilter</code> that change the image to the <code>Color</code> c.
     * 
     * @param c the <code>Color</code> to which the image should be changed
     */
    public ImageColorFilter( Color c )
    {
        float[] hsb = Color.RGBtoHSB( c.getRed(), c.getGreen(), c.getBlue(), null );
        hue = hsb[0];
        canFilterIndexColorModel = true;
    }

    public int filterRGB( int x, int y, int rgb )
    {
        // detect the transparency of the pixel
        int alpha = rgb & 0xff000000;
        // if the pixel is fully transparent make nothing
        if( alpha == 0 )
        {
            return rgb;
        }
        float[] hsb = Color.RGBtoHSB( (rgb >> 16) & 0xff, (rgb >> 8) & 0xff, rgb & 0xff, null );
        // change the pixel to the new color
        hsb[0] = hue;
        // add the transparency of the pixel and return it
        return (Color.HSBtoRGB( hsb[0], hsb[1], hsb[2] ) & 0xffffff) | alpha;
    }
}