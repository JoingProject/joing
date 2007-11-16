/*
 * FrameButtonToolTip.java
 *
 * Created on 16 de septiembre de 2007, 21:09
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.joing.pde.desktop.taskbar.frameslist;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import javax.swing.JToolTip;
import org.joing.pde.PDEManager;

class FrameButtonToolTip extends JToolTip
{
    private Component toShow;
    
    public FrameButtonToolTip( Component toShow )
    {
        this.toShow = toShow;
    }
    
    public Dimension getPreferredSize()
    {
        PDEManager pm  = PDEManager.getInstance();
        Dimension  dim = pm.getDesktop().getSize();
                   dim = new Dimension( dim.width / 7, dim.height / 7 );
        return dim;
    }
    
    public Dimension getMinimumSize()
    {
        return getPreferredSize();
    }
    
    public Dimension getMaximumSize()
    {
        return getPreferredSize();
    }
    
    public void paintComponent( Graphics g )
    {
        BufferedImage bi = new BufferedImage( toShow.getWidth(), toShow.getHeight(), BufferedImage.TYPE_INT_RGB );
        toShow.paint( bi.createGraphics() );    // createGraphics() is preferred over getGraphics(). See JavaDoc.
        g.drawImage( bi.getScaledInstance( getWidth(), getHeight(), Image.SCALE_SMOOTH ), 0, 0, this );
    }
}