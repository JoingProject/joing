/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.joing.jvmm.net;

import java.net.URLStreamHandler;
import java.net.URLStreamHandlerFactory;

/**
 * URLStreamFactory for internal URL format.
 * 
 * @author Antonio Varela Lizardi <antonio@icon.net.mx>
 */
public class JoingURLStreamHandlerFactory implements URLStreamHandlerFactory {

    public URLStreamHandler createURLStreamHandler(String protocol) {
        
        BridgeURLStreamHandler handler = null;
        
        if (protocol.equals("bridge2server")) {
            handler = new BridgeURLStreamHandler();
        }
        
        return handler;
    }

}
