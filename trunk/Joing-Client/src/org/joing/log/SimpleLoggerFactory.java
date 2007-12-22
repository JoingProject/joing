/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.joing.log;

import java.util.HashMap;
import java.util.Map;

/**
 * Simple Class to handle Loggers in use by the application.
 * 
 * @author Antonio Varela Lizardi <antonio@icon.net.mx>
 */
public class SimpleLoggerFactory {
    
    private static final Map<Object, Logger> loggers = 
            new HashMap<Object, Logger>();
    
    public static Logger getLogger(Object id) {
        
        synchronized (loggers) {
            Logger logger = loggers.get(id);
            if (logger == null) {
                logger = new Logger();
                loggers.put(id, logger);
            }
            
            return logger;
        }
    }
}
