/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.joing.jvmm;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.joing.common.clientAPI.log.JoingLogger;
import org.joing.common.clientAPI.log.Logger;
import org.joing.common.clientAPI.log.SimpleLoggerFactory;

/**
 * Execution Task.
 * 
 * @author Antonio Varela Lizardi <antonio@icon.net.mx>
 */
public class Task implements Runnable {

    private BridgeClassLoader classLoader;
    private String mainClassName;
    private String args[];
    
    private Logger logger = SimpleLoggerFactory.getLogger(JoingLogger.ID);

    public Task(final BridgeClassLoader classLoader, 
            final String mainClassName, final String[] args) {
        this.classLoader = classLoader;
        this.mainClassName = mainClassName;
        this.args = args;
    }

    public void run() {
        try {
            
            Class c = classLoader.loadClass(mainClassName);
            
            Method main = c.getMethod("main", new Class[] {String[].class});
            main.setAccessible(true);
            
            // main is static, the argument will be ignored
            main.invoke(c, new Object[]{args});
            
        } catch (ClassNotFoundException cnfe) {
            logger.critical("Class '{0}' not found.", mainClassName);
            throw new ExecutionTaskException("ClassNotFoundException", cnfe);
        } catch (InvocationTargetException ite) {
            logger.critical("InvocationTargetExcepion: {0}", ite.getMessage());
            logger.critical("Cause: {0}",
                    ite.getCause() != null ? ite.getCause().getMessage() : 
                        "null");
            logger.critical("StackTrace will follow.");
            logger.printStackTrace(ite);
            throw new ExecutionTaskException("InvocationTargetException", ite);
        } catch (NullPointerException npe) {
            logger.critical("NullPointerException: {0}", npe.getMessage());
            throw new ExecutionTaskException(npe.getMessage(), npe);
        } catch (Exception e) {
            logger.critical("Error in ExecutionTask: {0}", e.getMessage());
            logger.critical("Cause: {0}",
                    e.getCause() != null ? e.getCause().getMessage() : "null");
            throw new ExecutionTaskException(e.getMessage(), e);
        }
    }
}
