/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.joing.common.clientAPI.jvmm;

import java.io.OutputStream;
import java.net.URL;
import java.util.Map;
import java.util.Properties;
import org.joing.common.desktopAPI.DesktopManager;
import org.joing.common.clientAPI.runtime.Bridge2Server;
import org.joing.common.dto.app.Application;

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
    
    String getMainClassName(Application application);
    void start(final int appId) throws ApplicationExecutionException;
    void start(final int appId, String[] args, OutputStream out, OutputStream err) 
            throws ApplicationExecutionException;
    void start(final Application application, String[] args, OutputStream out, 
            OutputStream err) throws ApplicationExecutionException;
    /**
     * Base method for starting application. 
     * @param classPath Array of URL Objects with the class path for loading
     * classes and resources. 
     * @param application Application object to be Launched.
     * @param args Arguments for the application, if any.
     * @param out Outputstream object associated to the application's System.out
     * property.
     * @param err Outputstream object associated to the application's Syste.err
     * property.
     * @throws org.joing.common.clientAPI.jvmm.ApplicationExecutionException
     * @see org.joing.jvmm.net.URLFormat URLFormat
     * @see org.joing.jvmm.net.URLFormat#toURL URLFormat.toURL()
     */
    void start(final URL[] classPath, final Application application,
            final String[] args, final OutputStream out, final OutputStream err)
            throws ApplicationExecutionException;
    
}
