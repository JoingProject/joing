/*
 * PDEDeskJoingAppLauncher.java
 *
 * Created on 24 de septiembre de 2007, 20:15
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.joing.pde.desktop.deskwidget.deskLauncher;

import javax.swing.ImageIcon;
import org.joing.common.dto.app.AppDescriptor;

/**
 * Launcher for all applications asigned to the user in the Server).
 * They are shared by more than one user and are stored remotely.
 * <p>
 * The other kind are those apps that user can store in his personal HD space.
 * 
 * @author Francisco Morero Peyrona
 */
public class PDEDeskJoingAppLauncher extends PDEDeskLauncher
{
    private AppDescriptor appDescriptor;
    
    /**
     * Creates a new instance of PDEDeskJoingAppLauncher
     */
    public PDEDeskJoingAppLauncher()
    {
    }
    
    public PDEDeskJoingAppLauncher( AppDescriptor ad )
    {
        setApplicationDescriptor( ad );
    }
    
    public void setApplicationDescriptor( AppDescriptor ad )
    {
        // As AppDescriptor is inmutable (it has to be), modifications made to
        // this objects can not (and should not) be passed to the AppDescriptor.
        this.appDescriptor = ad;
        
        byte[] image = ad.getSVGIcon();
        
        if( image.length == 0 )
            image = ad.getPNGIcon();
        
        setName( ad.getName() );
        setDescription( ad.getDescription() );
        
        if( image.length > 0 )   // If (len == 0) => ther is no image defined
            setImage( new ImageIcon( image ).getImage() );
    }
    
    /**
     * Read-only
     */
    public String getExecutable()
    {
        if( appDescriptor != null )
            return appDescriptor.getExecutable();
        else
            return null;
    }

    /**
     * Read-only
     */
    public String[] getArguments()
    {
        if( appDescriptor != null )
            return appDescriptor.getArguments();
        else
            return null;
    }

    /**
     * Read-only
     */
    public String[] getFileTypes()
    {
        if( appDescriptor != null )
            return appDescriptor.getFileTypes();
        else
            return null;
    }
    
    /**
     * Read-only
     */
    public String getVersion()
    {
        if( appDescriptor != null )
            return appDescriptor.getVersion();
        else
            return null;
    }
    
    public boolean launch()
    {
        // TODO: llamar a lo de Antonio
        return false;
    }
}