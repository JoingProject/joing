/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.joing.jvmm;

import java.lang.reflect.Method;
import org.joing.common.clientAPI.log.JoingLogger;
import org.joing.common.clientAPI.log.Levels;
import org.joing.common.clientAPI.log.Logger;
import org.joing.common.clientAPI.log.SimpleLoggerFactory;

/**
 * Execution Task, must be executed by SwingUtilites.invokeAndWait().
 * 
 * @author Antonio Varela Lizardi <antonio@icon.net.mx>
 */
public class ExecutionTask implements Runnable {

    private BridgeClassLoader classLoader;
    private String mainClassName;
    private String args[];
    
    private Logger logger = SimpleLoggerFactory.getLogger(JoingLogger.ID);

    public ExecutionTask(final BridgeClassLoader classLoader, 
            final String mainClassName, final String[] args) {
        this.classLoader = classLoader;
        this.mainClassName = mainClassName;
        this.args = args;
    }

    public void run() {
        try {
            Class c = classLoader.loadClass(mainClassName);
            Method main = c.getMethod("main", new Class[]{String[].class});
            main.setAccessible(true);
            main.invoke(null, new Object[]{args});
        } catch (Exception e) {
            logger.write(Levels.CRITICAL, "Error in ExecutionTask: {0}",
                    e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }
}
