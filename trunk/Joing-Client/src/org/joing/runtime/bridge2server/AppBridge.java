/*
 * AppBridge.java
 *
 * Created on 18 de junio de 2007, 16:06
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.joing.runtime.bridge2server;

import java.util.List;
import org.joing.common.dto.app.AppDescriptor;
import org.joing.common.dto.app.AppEnvironment;
import org.joing.common.dto.app.Application;
import org.joing.common.dto.app.AppGroup;
import org.joing.common.dto.app.AppGroupKey;

/**
 *
 * @author fmorero
 */
public interface AppBridge
{
    List<AppGroup> getAvailableForUser( AppEnvironment environ, AppGroupKey groupKey );
    
    List<AppGroup> getNotInstalledForUser( AppEnvironment environ, AppGroupKey groupKey );
    
    List<AppGroup> getInstalledForUser( AppEnvironment environ, AppGroupKey groupKey );
    
    boolean install( AppDescriptor app );
    
    boolean uninstall( AppDescriptor app );
    
    AppDescriptor getPreferredForType( String sFileExtension );
    
    Application getApplication( int nAppId );
}