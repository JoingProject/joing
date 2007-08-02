/*
 * AppBridge.java
 *
 * Created on 18 de junio de 2007, 16:06
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.joing.runtime.bridge2server;

import ejb.app.AppDescriptor;
import ejb.app.Application;
import ejb.app.AppsByGroup;
import java.util.List;

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