/*
 * PDEDeskUserAppLauncher.java
 *
 * Created on 24 de septiembre de 2007, 20:01
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.joing.pde.desktop.workarea.desklet.deskLauncher;

import java.awt.Image;
import java.awt.Point;
import org.joing.api.desktop.workarea.desklet.deskLauncher.UserApplicationLauncher;
import org.joing.api.desktop.workarea.desklet.deskLauncher.LauncherEventListener;

/**
 *
 * @author Francisco Morero Peyrona
 */
public class PDEDeskUserAppLauncher 
       extends PDEDeskLauncher 
       implements UserApplicationLauncher
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
        super( sName, image, null );
    }
    
    public PDEDeskUserAppLauncher( String sName, Image image, String sDescription )
    {
        super( sName, image, sDescription );
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