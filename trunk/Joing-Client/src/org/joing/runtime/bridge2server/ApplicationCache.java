/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.joing.runtime.bridge2server;

import java.util.HashMap;
import java.util.Map;
import org.joing.common.clientAPI.log.JoingLogger;
import org.joing.common.clientAPI.log.Levels;
import org.joing.common.clientAPI.log.Logger;
import org.joing.common.clientAPI.log.SimpleLoggerFactory;
import org.joing.common.dto.app.Application;

/**
 * This is the base for a complete cache implementation. The cache
 * must be shared with all AppBridge implementations.
 * 
 * @author Antonio Varela Lizardi <antonio@icon.net.mx>
 */
public class ApplicationCache {

    public static String ID = "JoingCache";
    private Map<Object, Application> cache =
            new HashMap<Object, Application>();
    private Logger logger = SimpleLoggerFactory.getLogger(JoingLogger.ID);

    public Application get(Object key) {
        Application app = null;
        synchronized (cache) {
            app = this.cache.get(key);
        }
        if (app != null) {
            logger.write(Levels.DEBUG_CACHE, "Cache Hit, appId={0}, executable={1}",
                    app.getId(), app.getExecutable());
        } else {
            logger.write(Levels.DEBUG_CACHE, "Cache Miss, key={0}",
                    key);
        }
        return app;

    }

    public void put(Object key, Application app) {
        logger.write(Levels.DEBUG_CACHE, "Cache Store, key={0}, executable={1}",
                key, app.getExecutable());
        synchronized (cache) {
            this.cache.put(key, app);
        }
    }
    
    public boolean isExpired(Object key) {
        // for the moment always returns false.
        return false;
    }
}
