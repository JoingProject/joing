/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.joing.jvmm;

import org.joing.common.clientAPI.jvmm.App;
import org.joing.common.clientAPI.jvmm.AppManager;
import org.joing.common.clientAPI.log.JoingLogger;
import org.joing.common.clientAPI.log.Levels;
import org.joing.common.clientAPI.log.Logger;
import org.joing.common.clientAPI.log.SimpleLoggerFactory;

/**
 * Simple Task for disposing an application.
 * 
 * @author Antonio Varela Lizardi <antonio@icon.net.mx>
 */
public class DisposerTask implements Runnable {

    private AppManager appManager;
    private App application;
    private sun.awt.AppContext appContext;
    private final Logger logger = 
            SimpleLoggerFactory.getLogger(JoingLogger.ID);

    public DisposerTask(AppManager appManager, App application) {
        this.appManager = appManager;
        this.application = application;
        this.appContext = 
                sun.awt.SunToolkit.createNewAppContext();
    }
    
    public void run() {
        logger.write(Levels.DEBUG, "Removing application '{0}'.",
                application.getMainClassName());
        appManager.removeApp(application);
        logger.write(Levels.DEBUG, "Disposing AppContext.");
        appContext.dispose();
    }

}
