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
import org.joing.common.dto.app.Application;
import org.joing.common.dto.app.AppsByGroup;

/**
 *
 * @author fmorero
 */
public interface AppBridge
{
    List<AppsByGroup> getAvailableForUser();
    
    List<AppsByGroup> getNotInstalledForUser();
    
    List<AppsByGroup> getInstalledForUser();
    
    boolean install( AppDescriptor app );
    
    boolean uninstall( AppDescriptor app );
    
    AppDescriptor getPreferredForType( String sFileExtension );
    
    Application getApplication( int nAppId );
}