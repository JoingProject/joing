/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.joing.common.clientAPI.jvmm;

import java.util.List;

/**
 *
 * @author Antonio Varela Lizardi <antonio@icon.net.mx>
 */
public interface AppManager {

    void addAppListener(AppListener listener);
    void removeAppListener(AppListener listener);
    void addApp(App app);
    void removeApp(App app);
    
    void fireAppRemoved(App app) ;
    void fireAppAdded(App app);
    
    List<App> applications();
}
