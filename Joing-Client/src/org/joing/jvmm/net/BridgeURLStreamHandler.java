/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.joing.jvmm.net;

import java.io.IOException;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;
import org.joing.common.clientAPI.runtime.Bridge2Server;
import org.joing.jvmm.RuntimeFactory;

/**
 *
 * @author Antonio Varela Lizardi <antonio@icon.net.mx>
 */
public class BridgeURLStreamHandler extends URLStreamHandler {

    @Override
    protected URLConnection openConnection(URL u) throws IOException {
        
        String s = u.getFile();
        String proto = u.getProtocol();
        
        if (proto.startsWith("bridge2server")) {
            Bridge2Server bridge = RuntimeFactory.getPlatform().getBridge();

            return new BridgeURLConnection(u, bridge);
        }
        
        return u.openConnection();
    }

    @Override
    protected int getDefaultPort() {
        // Actually we don't use any port at all.
        return 65534;
    }

    @Override
    protected synchronized InetAddress getHostAddress(URL u) {
        // always returns localhost
        try {
            return InetAddress.getByName("127.0.0.1");
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    protected void parseURL(URL u, String spec, int start, int limit) {

        try {
            URI uri = new URI(spec);
            String proto = uri.getScheme();
            String file = uri.getSchemeSpecificPart();
            String query = uri.getQuery();
            String path = uri.getPath();
            String fragment = uri.getFragment();
            
            //setURL(u, proto, "", getDefaultPort(),
            //        null, null, path, query, fragment);
            setURL(u, proto, "", getDefaultPort(),
                    null, null, path, file, fragment);
            
        } catch (URISyntaxException use) {

        }
    }
}
