/*
 * $Id: PDFImage.java,v 1.2 2007/12/20 18:17:41 rbair Exp $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */

package com.sun.pdfview;

import java.awt.Color;
import java.awt.Point;
import java.awt.Transparency;
import java.awt.color.ColorSpace;
import java.awt.color.ICC_ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.awt.image.ColorModel;
import java.awt.image.ComponentColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.IndexColorModel;
import java.awt.image.MultiPixelPackedSampleModel;
import java.awt.image.Raster;
import java.awt.image.SampleModel;
import java.awt.image.WritableRaster;
import java.io.IOException;
import java.util.Map;

import com.sun.pdfview.colorspace.IndexedColor;
import com.sun.pdfview.colorspace.PDFColorSpace;
import com.sun.pdfview.function.FunctionType0;

/**
 * Encapsulates a PDF Image
 */
public class PDFImage {
    /** the width of this image in pixels */
    private int width;
    
    /** the height of this image in pixels */
    private int height;
    
    /** the colorspace to interpret the samples in */
    private PDFColorSpace colorSpace;
    
    /** the number of bits per sample component */
    private int bpc;
    
    /** whether this image is a mask or not */
    private boolean imageMask = false;
    
    /** the SMask image, if any */
    private PDFImage sMask;
    
    /** the decode array */
    private float[] decode;
    
    /** the actual image data */
    private PDFObject imageObj;
    
    /** 
     * Create an instance of a PDFImage
     */
    protected PDFImage(PDFObject imageObj) {
        this.imageObj = imageObj;
    }
    
    /**
     * Read a PDFImage from an image dictionary and stream
     *
     * @param obj the PDFObject containing the image's dictionary and stream
     * @param resources the current resources
     */
    public static PDFImage createImage(PDFObject obj, Map resources) 
        throws IOException
    {
        // create the image
        PDFImage image = new PDFImage(obj);
        
        // get the width (required)
        PDFObject widthObj = obj.getDictRef("Width");
        if (widthObj == null) {
            throw new PDFParseException("Unable to read image width: " + obj);
        }
        image.setWidth(widthObj.getIntValue());
        
        // get the height (required)
        PDFObject heightObj = obj.getDictRef("Height");
        if (heightObj == null) {
            throw new PDFParseException("Unable to get image height: " + obj);
        }
        image.setHeight(heightObj.getIntValue());
        
        // figure out if we are an image mask (optional)
        PDFObject imageMaskObj = obj.getDictRef("ImageMask");
        if (imageMaskObj != null) {
            image.setImageMask(imageMaskObj.getBooleanValue());
        }
        
        // read the bpc and colorspace (required except for masks) 
        if (image.isImageMask()) {
            image.setBitsPerComponent(1);
            
            // create the indexed color space for the mask
            Color[] colors = { Color.WHITE, Color.BLACK };
            image.setColorSpace(new IndexedColor(colors));
        } else {
            // get the bits per component (required)
            PDFObject bpcObj = obj.getDictRef("BitsPerComponent");
            if (bpcObj == null) {
                throw new PDFParseException("Unable to get bits per component: " + obj);
            }
            image.setBitsPerComponent(bpcObj.getIntValue());
            
            // get the color space (required)
            PDFObject csObj = obj.getDictRef("ColorSpace");
            if (csObj == null) {
                throw new PDFParseException("No ColorSpace for image: " + obj);
            }
            
            PDFColorSpace cs = PDFColorSpace.getColorSpace(csObj, resources);
            image.setColorSpace(cs);
        }
        
        // read the decode array
        PDFObject decodeObj = obj.getDictRef("Decode");
        if (decodeObj != null) {
            PDFObject[] decodeArray = decodeObj.getArray();
            
            float[] decode = new float[decodeArray.length];
            for (int i = 0; i < decodeArray.length; i++) {
                decode[i] = decodeArray[i].getFloatValue();
            }
            
            image.setDecode(decode);
        }
       
        // read the soft mask
        PDFObject sMaskObj = obj.getDictRef("SMask");
        if (sMaskObj == null) {
            // try the explicit mask, if there is no SoftMask
            sMaskObj = obj.getDictRef("Mask");
        }
        
        if (sMaskObj != null) {
            PDFImage sMaskImage = PDFImage.createImage(sMaskObj, resources);
            image.setSMask(sMaskImage);
        }
        
        return image;
    }
    
    /**
     * Get the image that this PDFImage generates.
     *
     * @return a buffered image containing the decoded image data
     */
    public BufferedImage getImage() {
        try {
            BufferedImage bi = (BufferedImage) imageObj.getCache();
        
            if (bi == null) {
                // parse the stream data into an actual image
                bi = parseData(imageObj.getStream());
                imageObj.setCache(bi);
            }
       
            return bi;
        } catch (IOException ioe) {
            System.out.println("Error reading image");
            ioe.printStackTrace();
            return null;
        }
    }
    
    /**
     * Parse the image stream into a buffered image.  Note that this is
     * guaranteed to be called after all the other setXXX methods have been 
     * called.
     */
    protected BufferedImage parseData(byte[] data) {
        // create the data buffer
        DataBuffer db = new DataBufferByte(data, data.length);
       
        // pick a color model, based on the number of components and
        // bits per component
        ColorModel cm = getColorModel();
        
        // create a compatible raster
        SampleModel sm = cm.createCompatibleSampleModel(getWidth(), getHeight());
        WritableRaster raster =
            Raster.createWritableRaster(sm, db, new Point(0, 0));
  
        /* 
         * Workaround for a bug on the Mac -- a class cast exception in
         * drawImage() due to the wrong data buffer type (?)
         */
        BufferedImage bi = null;
        if (cm instanceof IndexColorModel) {
            IndexColorModel icm = (IndexColorModel) cm;
            
            // choose the image type based on the size
            int type = BufferedImage.TYPE_BYTE_BINARY;
            if (getBitsPerComponent() == 8) {
                type = BufferedImage.TYPE_BYTE_INDEXED;
            }
            
            // create the image with an explicit indexed color model.
            bi = new BufferedImage(getWidth(), getHeight(), type, icm);
            
            // set the data explicitly as well
            bi.setData(raster);
        } else {
            bi = new BufferedImage(cm, raster, true, null);
        }
        
        // hack to avoid *very* slow conversion
        ColorSpace cs = cm.getColorSpace();
        ColorSpace rgbCS = ColorSpace.getInstance(ColorSpace.CS_sRGB);
        if (!isImageMask() && cs instanceof ICC_ColorSpace && !cs.equals(rgbCS)) {
            ColorConvertOp op = new ColorConvertOp(cs, rgbCS, null);
            
            BufferedImage converted = new BufferedImage(getWidth(), 
                getHeight(), BufferedImage.TYPE_INT_ARGB);
                                            
            bi = op.filter(bi, converted);
        }
        
        // add in the alpha data supplied by the SMask, if any
        PDFImage sMaskImage = getSMask();
        if (sMaskImage != null) {
            BufferedImage si = sMaskImage.getImage();
            
            BufferedImage outImage = new BufferedImage(getWidth(),
                getHeight(), BufferedImage.TYPE_INT_ARGB);
            
            int[] srcArray = new int[width];
            int[] maskArray = new int[width];
           
            for (int i = 0; i < height; i++) {
                bi.getRGB(0, i, width, 1, srcArray, 0, width);
                si.getRGB(0, i, width, 1, maskArray, 0, width);
                
                for (int j = 0; j < width; j++) {
                    int ac = 0xff000000;
                    
                    maskArray[j] = ((maskArray[j] & 0xff) << 24) | (srcArray[j] & ~ac);
                }
                
                outImage.setRGB(0, i, width, 1, maskArray, 0, width);
            }
            
            bi = outImage;
        }
        
        return (bi);
    }
    
    /**
     * Get the image's width
     */
    public int getWidth() {
        return width;
    }
    
    /**
     * Set the image's width
     */
    protected void setWidth(int width) {
        this.width = width;
    }
    
    /**
     * Get the image's height
     */
    public int getHeight() {
        return height;
    }
    
    /**
     * Set the image's height
     */
    protected void setHeight(int height) {
        this.height = height;
    }
    
    /**
     * Get the colorspace associated with this image, or null if there
     * isn't one
     */
    protected PDFColorSpace getColorSpace() {
        return colorSpace;
    }
        
    /**
     * Set the colorspace associated with this image
     */
    protected void setColorSpace(PDFColorSpace colorSpace) {
        this.colorSpace = colorSpace;
    }
    
    /**
     * Get the number of bits per component sample
     */
    protected int getBitsPerComponent() {
        return bpc;
    }
    
    /**
     * Set the number of bits per component sample
     */
    protected void setBitsPerComponent(int bpc) {
        this.bpc = bpc;
    }
    
    /**
     * Return whether or not this is an image mask
     */
    public boolean isImageMask() {
        return imageMask;
    }
    
    /**
     * Set whether or not this is an image mask
     */
    public void setImageMask(boolean imageMask) {
        this.imageMask = imageMask;
    }
    
    /** 
     * Return the soft mask associated with this image
     */
    public PDFImage getSMask() {
        return sMask;
    }
    
    /**
     * Set the soft mask image
     */
    protected void setSMask(PDFImage sMask) {
        this.sMask = sMask;
    }
    
    /**
     * Get the decode array
     */
    protected float[] getDecode() {
        return decode;
    }
    
    /**
     * Set the decode array
     */
    protected void setDecode(float[] decode) {
        this.decode = decode;
    }
    
    /**
     * get a Java ColorModel consistent with the current color space,
     * number of bits per component and decode array
     * 
     * @param bpc the number of bits per component
     */
    private ColorModel getColorModel() {
        PDFColorSpace cs = getColorSpace();
        
        if (cs instanceof IndexedColor) {
            IndexedColor ics = (IndexedColor) cs;
            
            byte[] components = ics.getColorComponents();
            int num = ics.getCount();
            
            // process the decode array
            if (decode != null) {
                byte[] normComps = new byte[components.length];
               
                // move the components array around
                for (int i = 0; i < num; i++) {
                    byte[] orig = new byte[1];
                    orig[0] = (byte) i;
                    
                    float[] res = normalize(orig, null, 0);
                    int idx = (int) res[0];
                    
                    normComps[i * 3] = components[idx * 3];
                    normComps[(i * 3) + 1] = components[(idx * 3) + 1];
                    normComps[(i * 3) + 2] = components[(idx * 3) + 2];
                }
                
                components = normComps;
            }
            
            // make sure the size of the components array is 2 ^ numBits
            // since if it's not, Java will complain
            int correctCount = 1 << getBitsPerComponent();
            if (correctCount < num) {
                byte[] fewerComps = new byte[correctCount * 3];
                
                System.arraycopy(components, 0, fewerComps, 0, correctCount * 3);
                
                components = fewerComps;
                num = correctCount;
            }
            
            return new IndexColorModel(getBitsPerComponent(), num, components, 
                                       0, false);
        } else {
            int[] bits = new int[cs.getNumComponents()];
            for (int i = 0; i < bits.length; i++) {
                bits[i] = getBitsPerComponent();
            }
            
            return new DecodeComponentColorModel(cs.getColorSpace(), bits);
        }
    }
   
    /**
     * Normalize an array of values to match the decode array
     */
    private float[] normalize(byte[] pixels, float[] normComponents, 
        int normOffset)
    {
        if (normComponents == null) {
            normComponents = new float[normOffset + pixels.length];
        }
        
        float[] decodeArray = getDecode();
        
        for (int i = 0; i < pixels.length; i++) {
            int val = pixels[i] & 0xff;
            int pow = ((int) Math.pow(2, getBitsPerComponent())) - 1;
            float ymin = decodeArray[i * 2];
            float ymax = decodeArray[(i * 2) + 1];
            
            normComponents[normOffset + i] =
                FunctionType0.interpolate(val, 0, pow, ymin, ymax);
        }
        
        return normComponents;
    }
    
    /**
     * A wrapper for ComponentColorSpace which normalizes based on the 
     * decode array.
     */
    class DecodeComponentColorModel extends ComponentColorModel {
        public DecodeComponentColorModel(ColorSpace cs, int[] bpc) {
            super (cs, bpc, false, false, Transparency.OPAQUE, 
                   DataBuffer.TYPE_BYTE);
            
            if (bpc != null) {
                pixel_bits = bpc.length * bpc[0];
            }
        }
        
        @Override public SampleModel createCompatibleSampleModel(int width, int height) {
            // workaround -- create a MultiPixelPackedSample models for 
            // single-sample, less than 8bpp color models
            if (getNumComponents() == 1 && getPixelSize() < 8) {
                return new MultiPixelPackedSampleModel(getTransferType(), 
                                                       width, 
                                                       height, 
                                                       getPixelSize());
            } 
            
            return super.createCompatibleSampleModel(width, height);
        }
        
        @Override public boolean isCompatibleRaster(Raster raster) {
            if (getNumComponents() == 1 && getPixelSize() < 8) {
                SampleModel sm = raster.getSampleModel();
                
                if (sm instanceof MultiPixelPackedSampleModel) {
                    return (sm.getSampleSize(0) == getPixelSize());
                } else {
                    return false;
                }
            }
            
            return super.isCompatibleRaster(raster);
        }
        
        @Override public float[] getNormalizedComponents(Object pixel,
            float[] normComponents, int normOffset)
        {
            if (getDecode() == null) {
                return super.getNormalizedComponents(pixel, normComponents,
                                                     normOffset);
            }
                    
            return normalize((byte[]) pixel, normComponents, normOffset);
        }
    }
}
