/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.joing.common.jvmm;

import java.util.Map;
import java.util.Properties;
import org.joing.common.api.DesktopManager;
import org.joing.common.runtime.Bridge2Server;

/**
 *
 * @author Antonio Varela Lizardi <antonio@icon.net.mx>
 */
public interface Platform {

    void halt();
    void shutdown();
    
//    PlatformRuntime getRuntime();
    Bridge2Server getBridge();
    
    // Temporal.
    Properties getClientProp();
    
    boolean isInitialized();
    String getServerBaseURL();
    boolean isAutoHandlingExceptions();
    void setAutoHandlingExceptions( boolean bAutoHandleExceptions );
    
    void showException( Throwable exc );
    void showException( String sTitle, Throwable exc );
    
    long getMainThreadId();
    JThreadGroup getJThreadGroup();
    Map<Object, ClassLoader> getClassLoaderCache();
    AppManager getAppManager();
    DesktopManager getDesktopManager();
    void setDesktopManager(DesktopManager desktop);
    
    void start(int appId) throws ApplicationExecutionException;
    
}
