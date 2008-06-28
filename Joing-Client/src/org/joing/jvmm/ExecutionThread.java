/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.joing.jvmm;

import javax.swing.SwingUtilities;
import org.joing.common.clientAPI.jvmm.App;
import org.joing.common.clientAPI.jvmm.AppManager;
import org.joing.common.clientAPI.jvmm.JThreadGroup;
import org.joing.common.clientAPI.log.JoingLogger;
import org.joing.common.clientAPI.log.Logger;
import org.joing.common.clientAPI.log.SimpleLoggerFactory;

/**
 *
 * @author Antonio Varela Lizardi <antonio@icon.net.mx>
 */
public class ExecutionThread extends Thread {

    private AppManager appManager;
    private App application;
    private ExecutionTask executionTask;
    final private Logger logger = SimpleLoggerFactory.getLogger(JoingLogger.ID);

    public ExecutionThread(AppManager appManager,
            App application, ExecutionTask executionTask) {
        super(application.getThreadGroup(), "JoingExecutionThread " + 
                application.getThreadGroup().getName());
        this.appManager = appManager;
        this.application = application;
        this.executionTask = executionTask;
    }

    @Override
    public void run() {

        // createNewAppContext will grab the current thread group,
        // me need to supply an aditional thread group for the
        // disposer
        ThreadGroup currentThreadGroup = getThreadGroup();
        ThreadGroup dtg = new ThreadGroup(currentThreadGroup,
                "DisposerThreadGroup for " + 
                application.getThreadGroup().getName());
        DisposerThread disposerThread = new DisposerThread(dtg);
        
        sun.awt.AppContext appContext = 
                sun.awt.SunToolkit.createNewAppContext();
        
        disposerThread.setDisposerTask(
                new DisposerTask(appManager, application, appContext)
        );
        
//        DisposerTask disposerTask =
//                new DisposerTask(appManager, application, appContext);
//        
//        DisposerThread disposerThread =
//                new DisposerThread(dtg, disposerTask);
        
        //application.getThreadGroup().setDisposer(disposerThread);
        ThreadGroup tg = getThreadGroup();
        if (tg instanceof JThreadGroup) {
            JThreadGroup jtg = (JThreadGroup)tg;
            jtg.setDisposer(disposerThread);
        } else {
            application.getThreadGroup().setDisposer(disposerThread);
        }

        try {
            // invokeAndWait necesita un Runnable como parámetro, aunque
            // en realidad no lo ejecuta como un Thread. El Runnable se
            // coloca en el EDT (Event Dispatch Thread) y si el objeto
            // es Runnable, simplemente invoca el metodo run().
            ///SwingUtilities.invokeAndWait(executionTask);
            executionTask.run();


        } catch (Exception e) {
            appManager.removeApp(application);
            application.getThreadGroup().close();
            StringBuilder sb = new StringBuilder();
            sb.append("Exception caught in ExecutionThread for App '{0}; ");
            logger.critical(sb.toString(), application.getApplication().getName());
            logger.critical("StackTrace will follow.");
            logger.printStackStrace(e);
        }
    }
}
