/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.joing.jvmm;

import org.joing.common.jvmm.Platform;

/**
 * Una de las desiciones importantes aqui será determinar que implementación
 * de Platform será la que se regresa. Esta desicion debera ser influida
 * por ejemplo por factores como la plataforma (JavaSE, JavaME, etc).
 * 
 * @author antoniovl
 */
public class RuntimeFactory {

    private static final Platform platform;

    static {
        platform = new PlatformImpl();
    }
    
    public RuntimeFactory() {
        if (platform == null) {
            throw new RuntimeException("Platform was not initialized.");
        }
    }
    
    public static Platform getPlatform() {
        return platform;
    }
}
