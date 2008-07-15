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

/**
 *
 * @author fmorero
 */
public interface AppBridge
{
    List<AppGroup> getAvailableForUser( AppGroupKey groupKey );
    
    List<AppGroup> getNotInstalledForUser( AppGroupKey groupKey );
    
    List<AppGroup> getInstalledForUser( AppGroupKey groupKey );
    
    boolean install( AppDescriptor app );
    
    boolean uninstall( AppDescriptor app );
    
    AppDescriptor getPreferredForType( String sFileExtension );
    
    /**
     * Abstract method to get an application from the Joing Server. The concrete
     * implementation must handle details about the preferred way to fetch
     * data from the server.
     * @param nAppId Application Id.
     * @return Application instance.
     */
    Application getApplication( int nAppId );
    
    Application getApplicationByName(String executableName);
    
    List<Application> getAvailableDesktops();
}