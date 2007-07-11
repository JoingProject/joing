/*
 * TestServer.java
 *
 * Created on 11-jul-2007, 9:27:18
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
package net.java.joing;

import ejb.user.User;
import javax.swing.JOptionPane;
import net.java.joing.runtime.bridge2server.Bridge2Server;

/**
 *
 * @author fmorero
 */
public class TestServer
{
    public TestServer()
    {
    }
    
    public static void main( String[] args )
    {
        Bridge2Server b2s = Bridge2Server.getInstance();
        
        System.out.println("Main started");
        
        if( b2s.getSessionBridge().login( "peyrona", "admin" ) )
        {
            String sId = b2s.getSessionId();
            
            JOptionPane.showMessageDialog( null, "Session ID = "+ sId );
        
            System.out.println("-------------------------------------------");
            User user = b2s.getUserBridge().getUser();
            System.out.println("User Account = "+ user.getAccount() );
            System.out.println("User Name    = "+ user.getFirstName() +" "+ user.getSecondName() );
            System.out.println("User Locale  = "+ user.getLocale() );
            System.out.println("-------------------------------------------");
            //Application app = b2s.getAppBridge().getPreferredForType( "txt" );
            //System.out.println("App Name  = "+ app.getName() );
            //System.out.println("App Desc. = "+ app.getDescription() );
            b2s.getSessionBridge().logout();
        }
        else
        {
            JOptionPane.showMessageDialog( null, "Can't login" );
        }
        
        System.out.println("Main finished");
    }
}