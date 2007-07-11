/*
 * AppBridge.java
 *
 * Created on 18 de junio de 2007, 16:06
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package net.java.joing.runtime.bridge2server;

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
    
    boolean install( Application app );
    
    boolean uninstall( Application app );
    
    Application getPreferredForType( String sFileExtension );
}