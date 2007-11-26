/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.joing.pde.misce.desklets.memon;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import javax.swing.border.LineBorder;
import org.joing.pde.desktop.deskwidget.desklet.PDEDesklet;

/**
 * DeskletApplet example: show memory graph.
 * 
 * @author Francisco Morero Peyrona
 */
public class MemMon extends PDEDesklet
{
    private MemoryMonitor memon;
    
    //------------------------------------------------------------------------//
    
    public MemMon()
    {
        setBackground( Color.black );
        setBorder( new LineBorder( Color.gray ) );
        setMinimumSize(   new Dimension(  80, 60 ) );
        setMaximumSize(   new Dimension( 800,600 ) );
        setPreferredSize( new Dimension( 160,120 ) );
    }
    
    public void onShow()
    {
        memon = new MemoryMonitor();
        add( memon, BorderLayout.CENTER );
        validate();
    }
    
    public void onGrow()
    {
        // TODO ¿hacerlo?
        toogleSizeButton();
    }
    
    public void onSetup()
    {
        // todo: hacerlo
    }
    
    public void onClose()
    {
        remove( memon );
    }
}