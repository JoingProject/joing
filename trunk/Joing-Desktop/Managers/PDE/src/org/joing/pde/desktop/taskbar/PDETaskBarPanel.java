/*
 * PDETaskBarPanel.java
 *
 * Created on 15 de septiembre de 2007, 22:07
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.joing.pde.desktop.taskbar;

import java.awt.Component;
import java.awt.Graphics;
import javax.swing.BoxLayout;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import org.joing.common.desktopAPI.DeskComponent;
import org.joing.common.desktopAPI.taskbar.TaskBarPanel;
import org.joing.pde.swing.JToolBarHandle;

/**
 * The base class for most part of the widtgets that are shown in the TaskBar.
 * 
 * By default it has a BoxLayout on X_AXIS.
 * 
 * @author Francisco Morero Peyrona
 */
public class PDETaskBarPanel extends PDETaskBarComponent implements TaskBarPanel
{
    private static final String DONT_USE_ME = "Do not use me";
    
    private JToolBarHandle handle;
    
    //------------------------------------------------------------------------//
    
    /** Creates a new instance of PDETaskBarPanel */
    public PDETaskBarPanel()
    {
        handle = new JToolBarHandle( this );
        
        setBorder( new EmptyBorder( 0, handle.getPreferredSize().width + 2, 0, 2 ) );
        setLayout( new BoxLayout( this, BoxLayout.X_AXIS ) );
    }

    public void setBorder( Border border )
    {
        // I can not allow to modify border.
        // But I can not throw an exception because this method is invoked by Swing.
    }
    
    public void paintComponent( Graphics g )
    {
        super.paintComponent( g );
        handle.paintComponent( g );
    }
        
    //------------------------------------------------------------------------//
    // DeskContainer interface
    
    // Note: A TaskBarComponent is also a DeskComponent nad a DeskContainer also
    public void add( DeskComponent comp )
    {
        super.add( (Component) comp );
    }
    
    public void remove( DeskComponent comp )
    {
        super.remove( (Component) comp );
    }
    
    //------------------------------------------------------------------------//
    // TaskBarComponent interface
    
    public void onAbout()
    {
        // TODO: Hacerlo
    }
    
    public void onRemove()
    {
        // TODO: Hacerlo
    }
    
    public void onMove()
    {
        // TODO: Hacerlo
    }
    
    public void onPreferences()
    {
        // TODO: Hacerlo
    }
    
    //------------------------------------------------------------------------//
    // Closeable interface
    
    public void close()
    {
        
    }
    
    // Just to avoid accidental use of them  ---------------------------------------------------------
    public Component add( Component c )             { throw new IllegalAccessError(DONT_USE_ME); }
    public Component add( Component c, int n )      { throw new IllegalAccessError(DONT_USE_ME); }
    public Component add( String s, Component c )   { throw new IllegalAccessError(DONT_USE_ME); }
    public void add( Component c, Object o )        { throw new IllegalAccessError(DONT_USE_ME); }
    public void add( Component c, Object o, int n ) { throw new IllegalAccessError(DONT_USE_ME); }
    public void remove( Component c )               { throw new IllegalAccessError(DONT_USE_ME); }
    //------------------------------------------------------------------------------------------------
}