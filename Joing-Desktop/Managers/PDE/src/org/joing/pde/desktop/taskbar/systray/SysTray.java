/*
 * SysTray.java
 *
 * Created on 3 de octubre de 2007, 18:31
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.joing.pde.desktop.taskbar.systray;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import org.joing.pde.desktop.taskbar.PDETaskBarPanel;

/**
 * AKA: "Notification Area", "Taskbar Status Area", etc.<br>
 * I use the this name because is the one used by Sun.<br>
 * For more inforation about the proper name, visit:<br>
 * http://blogs.msdn.com/oldnewthing/archive/2003/09/10/54831.aspx<br>
 * and<br>
 * http://java.sun.com/developer/technicalArticles/J2SE/Desktop/javase6/systemtray/
 * <p>
 * In PDE it is mainly a PDETaskBarPanel that has a special name (SysTray) and can 
 * be accessed from the TaskBar.
 *
 * @author Francisco Morero Peyrona
 */
public class SysTray extends PDETaskBarPanel
{
    private static final int nHGAP = 2;
    
    //------------------------------------------------------------------------//
    
    /** Creates a new instance of SysTray */
    public SysTray()
    {
        setLayout( new FlowLayout( FlowLayout.LEADING, nHGAP, 0 ) );
    }
    
    public Dimension getMinimumSize()
    {
        int nWidth  = 0;
        int nHeight = 0;
        
        Component[] aComp = getComponents();
        
        for( int n = 0; n < aComp.length; n++ )
        {
            Dimension preferred = aComp[n].getPreferredSize();
            
            nWidth += preferred.width;
            nHeight = Math.max( nHeight, preferred.height );
        }
        
        nWidth += (aComp.length - 1) * nHGAP;
        nWidth += 10;  // TODO: el 10 es por el component que está a la izq ("||"): hacer esto bien, no así, a lo bestia
        
        return new Dimension( nWidth, nHeight );
    }
    
    public Dimension getMaximunSize()
    {
        return getMinimumSize();
    }

    public Dimension getPreferredSize()
    {
        return getMinimumSize();
    }
}