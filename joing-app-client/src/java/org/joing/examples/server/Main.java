/*
 * Main.java
 * 
 * Created on 02-ago-2007, 17:16:11
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
package org.joing.examples.server;

import org.joing.examples.server.vfs.VFSPanel;
import org.joing.examples.server.session.SessionPanel;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;

/**
 *
 * @author fmorero
 */
public class Main extends JFrame
{
    private JTabbedPane  tabPane;
    private SessionPanel pnlSession;
    private VFSPanel     pnlVFS;
    
    public Main()
    {
        super( "Operations with Join'g Server" );
        
        tabPane    = new JTabbedPane();
        pnlSession = new SessionPanel();
        pnlVFS     = new VFSPanel();
        
        tabPane.addTab( "Login", pnlSession );
        tabPane.addTab( "VFS"  , pnlVFS     );
        
        setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        getContentPane().add( tabPane );
        
        pack();
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