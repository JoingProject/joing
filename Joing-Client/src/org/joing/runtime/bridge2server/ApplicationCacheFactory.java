/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.joing.runtime.bridge2server;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Antonio Varela Lizardi <antonio@icon.net.mx>
 */
public class ApplicationCacheFactory {

    private static Map<Object, ApplicationCache> cache = 
            new HashMap<Object, ApplicationCache>();
    
    public static ApplicationCache getCache(Object id) {
        synchronized (cache) {
            ApplicationCache ac = cache.get(id);
            if (ac == null) {
                ac = new ApplicationCache();
                cache.put(id, ac);
            }
            return ac;
        }
    }
}
