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
package org.joing;

import org.joing.runtime.bridge2server.*;
import ejb.app.AppDescriptor;
import ejb.session.LoginResult;
import ejb.user.User;
import javax.swing.JOptionPane;

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
        
        System.out.println( "Main started" );
        
        LoginResult result = b2s.getSessionBridge().login( "peyrona", "admin" );
        
        if( result != null && result.isLoginValid() )
        {
            String sId = b2s.getSessionId();
            
            JOptionPane.showMessageDialog( null, "Session ID = "+ sId );
            
            System.out.println("-------------------------------------------");
            User user = b2s.getUserBridge().getUser();
            System.out.println("User Account = "+ user.getAccount() );
            System.out.println("User Name    = "+ user.getFirstName() +" "+ user.getSecondName() );
            System.out.println("User Locale  = "+ user.getLocale() );
            System.out.println("-------------------------------------------");
            
            AppDescriptor app = b2s.getAppBridge().getPreferredForType( "txt" );
            if( app != null )
            {
                System.out.println("App Name  = "+ app.getName() );
                System.out.println("App Desc. = "+ app.getDescription() );
                System.out.println("-------------------------------------------");
            }
            
            b2s.getSessionBridge().logout();
        }
        else
        {
            if( ! result.isAccountValid() )
                JOptionPane.showMessageDialog( null, "Can't login: invalid account" );
            
            if( ! result.isPasswordValid() )
                JOptionPane.showMessageDialog( null, "Can't login: invalid password" );
        }
        
        System.out.println( "Main finished" );
    }
}