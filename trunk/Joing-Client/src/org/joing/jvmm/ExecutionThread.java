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
import org.joing.common.clientAPI.log.Levels;
import org.joing.common.clientAPI.log.Logger;
import org.joing.common.clientAPI.log.SimpleLoggerFactory;

/**
 *
 * @author antoniovl
 */
public class ExecutionThread extends Thread {

    private AppManager appManager;
    private App application;
    private JThreadGroup threadGroup;
    private ExecutionTask executionTask;
    final private Logger logger = SimpleLoggerFactory.getLogger(JoingLogger.ID);

    public ExecutionThread(JThreadGroup threadGroup, AppManager appManager,
            App application, ExecutionTask executionTask) {
        this.threadGroup = threadGroup;
        this.appManager = appManager;
        this.application = application;
        this.executionTask = executionTask;
    }

    public void run() {
        final sun.awt.AppContext appContext =
                sun.awt.SunToolkit.createNewAppContext();

        DisposerTask disposerTask =
                new DisposerTask(appManager, application, appContext);
        DisposerThread disposerThread =
                new DisposerThread(disposerTask);
        threadGroup.setDisposer(disposerThread);

        try {
            // invokeAndWait necesita un Runnable como parámetro, aunque
            // en realidad no lo ejecuta como un Thread. El Runnable se
            // coloca en el EDT (Event Dispatch Thread) y si el objeto
            // es Runnable, simplemente invoca el metodo run().
            SwingUtilities.invokeAndWait(executionTask);

        } catch (Exception e) {
            threadGroup.close();
            logger.write(Levels.CRITICAL, "Exception Caught in ExecutionThread: {0}",
                    dumpStackTrace(e.getStackTrace()));
        }
    }
    
    private String dumpStackTrace(StackTraceElement[] elements) {
        StringBuffer sb = new StringBuffer();
        for (StackTraceElement element : elements) {
            sb.append(element.toString()).append("\n");
        }
        return sb.toString();
    }
}
