/*
 * Main.java
 * 
 * Created on 04-ago-2007, 16:41:53
 * 
 * Author: Francisco Morero Peyrona.
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or (at
 * your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 */
package org.joing.dockbar;

import java.awt.BorderLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.net.URL;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import org.joing.DockBar;

/**
 *
 * @author fmorero
 */
public class Main extends JFrame
{
    private DockBar   dockBar;
    private JCheckBox chkTable;
            
    public Main()
    {
        super( "DockBar Test" );
        
        dockBar  = new DockBar();
        dockBar.add( getIcon( "mycomputer.png" ) );
        dockBar.add( getIcon( "firefox.png" ) );
        dockBar.add( getIcon( "cdaudio.png" ) );
        dockBar.add( getIcon( "digikam.png" ) );
        dockBar.add( getIcon( "graphics.png" ) );
        dockBar.add( getIcon( "calc.png" ) );
        dockBar.add( getIcon( "karm.png" ) );
        dockBar.add( getIcon( "usbpendrive.png" ) );
        
        chkTable = new JCheckBox( "Show table" );
        chkTable.addItemListener( new ItemListener() 
            {
                public void itemStateChanged( ItemEvent ie )
                {                    
                    Main.this.dockBar.setTableVisible( ie.getStateChange() == ItemEvent.SELECTED  );
                    //System.out.println( ie.getStateChange() == ItemEvent.SELECTED );
                }
            }  );
        
        setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        
        getContentPane().add( chkTable, BorderLayout.NORTH );
        getContentPane().add( dockBar , BorderLayout.SOUTH );
        
        pack();
        setSize( 600, 400 );
    }
    
    private ImageIcon getIcon( String sName )
    {
        URL imgURL = getClass().getResource( "images/"+ sName );
        
        if( imgURL != null )
            return new ImageIcon( imgURL );
        else
            return null;
    }
    
    public static void main( String[] args )
    {
        SwingUtilities.invokeLater( new Runnable() 
        {
            public void run()
            {
                Main main = new Main();
                     main.setVisible( true );
            }
        } );
    }
}