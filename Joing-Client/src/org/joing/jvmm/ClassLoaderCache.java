/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.joing.jvmm;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Antonio Varela Lizardi <antonio@icon.net.mx>
 */
public class ClassLoaderCache {

    private final Map<Object, ClassLoader> classLoaderCache =
            new HashMap<Object, ClassLoader>();
    
    public ClassLoader get(Object id) {
        synchronized (classLoaderCache) {
            return classLoaderCache.get(id);
        }
    }

    public void put(Object id, ClassLoader classLoader) {
        synchronized (classLoaderCache) {
            classLoaderCache.put(id, classLoader);
        }
    }
}
