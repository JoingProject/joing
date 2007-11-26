/*
 * PDEDeskUserAppLauncher.java
 *
 * Created on 24 de septiembre de 2007, 20:01
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.joing.pde.desktop.deskwidget.deskLauncher;

import java.awt.Image;

/**
 * Launcher for those applications that user has stored in his HD space.
 * They are not shared: belong to owner of the HD personal space.
 * <p>
 * The other kind are those applications that are shared by more than one user.
 *  
 * @author Francisco Morero Peyrona
 */
public class PDEDeskUserAppLauncher extends PDEDeskLauncher
{
    private String   sCommand;
    private String[] asArgument;
    
    /**
     * Creates a new instance of PDEDeskUserAppLauncher
     */
    public PDEDeskUserAppLauncher()
    {
        super();
    }
    
    public PDEDeskUserAppLauncher( String sName )
    {
        super( sName );
    }
    
    public PDEDeskUserAppLauncher( String sName, Image image )
    {
        super( sName, image );
    }
    
    public String getCommand()
    {
        return this.sCommand;
    }

    public void setCommand( String sCommand )
    {
        this.sCommand = sCommand;
    }

    public String[] getArguments()
    {
        return this.asArgument;
    }

    public void setArguments( String[] asArgument )
    {
        this.asArgument = asArgument;
    }
}