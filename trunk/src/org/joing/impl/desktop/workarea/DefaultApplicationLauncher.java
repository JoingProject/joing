/*
 * DefaultApplicationLauncher.java
 * 
 * Created on 11-sep-2007, 14:11:57
 * 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.joing.impl.desktop.workarea;

import org.joing.api.desktop.workarea.desklet.deskLauncher.UserApplicationLauncher;
import org.joing.impl.desktop.workarea.DefaultLauncher;

/**
 *
 * @author Francisco Morero Peyrona
 */
public class DefaultApplicationLauncher 
       extends DefaultLauncher 
       implements UserApplicationLauncher
{
    private String   sCommand;
    private String[] asArgument;
    
    //------------------------------------------------------------------------//
    
    public DefaultApplicationLauncher()
    {
        super();
    }
    
    public String getCommand()
    {
        return sCommand;
    }
    
    public void setCommand( String sCommand )
    {
        this.sCommand = sCommand;
    }

    public String[] getArguments()
    {
        return asArgument;
    }
    
    public void setArguments( String[] args )
    {
        this.asArgument = args;
    }
    
    public UserApplicationLauncher clone()
    {
        // TODO: hacerlo 
        
        /*Launcher clone = new DefaultLauncher();
                 clone.setName( "Copy of "+ getName() );
                 clone. getCommand() );
                 clone.setArguments( getArguments() );
                 clone.setIconResource( getIconResource() );
                 
        return clone;*/
        return null;
    }
}