/*
 * AppBridge.java
 *
 * Created on 18 de junio de 2007, 16:06
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.joing.common.clientAPI.runtime;

import java.util.List;
import org.joing.common.dto.app.AppDescriptor;
import org.joing.common.dto.app.Application;
import org.joing.common.dto.app.AppGroup;
import org.joing.common.dto.app.AppGroupKey;
import org.joing.common.exception.JoingServerAppException;

/**
 *
 * @author Francisco Morero Peyrona
 */
public interface AppBridge
{
    List<AppGroup> getAvailableForUser( AppGroupKey groupKey )
            throws JoingServerAppException;
    
    List<AppGroup> getNotInstalledForUser( AppGroupKey groupKey )
            throws JoingServerAppException;
    
    List<AppGroup> getInstalledForUser( AppGroupKey groupKey )
            throws JoingServerAppException;
    
    boolean install( AppDescriptor app )
            throws JoingServerAppException;
    
    boolean uninstall( AppDescriptor app )
            throws JoingServerAppException;
    
    AppDescriptor getPreferredForType( String sFileExtension )
            throws JoingServerAppException;
    
    /**
     * Abstract method to get an application from the Joing Server. The concrete
     * implementation must handle details about the preferred way to fetch
     * data from the server.
     * @param nAppId Application Id.
     * @return Application instance.
     */
    Application getApplication( int nAppId )
            throws JoingServerAppException;
    
    Application getApplicationByName( String executableName )
            throws JoingServerAppException;
    
    List<AppDescriptor> getAvailableDesktops()
            throws JoingServerAppException;
}