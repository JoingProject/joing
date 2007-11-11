/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.joing.pde.misce.apps.memon;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.border.LineBorder;
import org.joing.pde.desktop.container.PDEFrame;
import org.joing.pde.desktop.deskwidget.desklet.PDEDesklet;
import org.joing.pde.runtime.PDERuntime;

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
        setDescription( "Memory Monitor" );
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