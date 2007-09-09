/*
 * LauncherImpl.java
 *
 * Created on 9 de septiembre de 2007, 11:59
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.joing.pde.desktop;

import org.joing.desktop.api.Launcher;

/**
 *
 * @author Francisco Morero Peyrona
 */
public class LauncherImpl implements Launcher
{
    private String sName;
    private String sCommand;
    private String sArguments;
    private String sIconResource;
    
    //------------------------------------------------------------------------//
    
    /** Creates a new instance of LauncherImpl */
    public LauncherImpl()
    {
    }
    
    public String getName()
    {
        return sName;
    }
    
    public void setName( String sName )
    {
        this.sName = sName;
    }
    
    public String getCommand()
    {
        return sCommand;
    }
    
    public void setCommand( String sCommand )
    {
        this.sCommand = sCommand;
    }

    public String getArguments()
    {
        return sArguments;
    }
    
    public void setArguments( String args )
    {
        this.sArguments = args;
    }
    
    public String getIconResource()
    {
        return sIconResource;
    }

    public void setIconResource( String sIconResource )
    {
        this.sIconResource = sIconResource;
    }

    public boolean execute()
    {
        return false;
    }
    
    public Launcher clone()
    {
        Launcher clone = new LauncherImpl();
                 clone.setName( "Copy of "+ getName() );
                 clone.setCommand( getCommand() );
                 clone.setArguments( getArguments() );
                 clone.setIconResource( getIconResource() );
                 
        return clone;
    }
}