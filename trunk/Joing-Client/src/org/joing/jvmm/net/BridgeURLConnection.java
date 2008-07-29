/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.joing.jvmm.net;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import org.joing.common.clientAPI.runtime.Bridge2Server;
import org.joing.common.dto.app.Application;
import org.joing.jvmm.BridgeClassLoader;
import org.joing.jvmm.JarSearchResult;

/**
 *
 * @author Antonio Varela Lizardi <antonio@icon.net.mx>
 */
public class BridgeURLConnection extends URLConnection {

    private Bridge2Server bridge;

    private BridgeURLConnection(URL url) {
        super(url);
    }

    public BridgeURLConnection(URL url, Bridge2Server bridge) {
        super(url);
        this.bridge = bridge;
    }

    @Override
    public void connect() throws IOException {

        if (this.bridge == null) {
            throw new IOException("Bridge2Server null.");
        }

    }

    /**
     * 
     * @return InputStream object.
     * @throws java.io.IOException
     */
    @Override
    public InputStream getInputStream() throws IOException {

        connect();

        Application app = getApplication();

        if (app.getContent() == null) {
            throw new IOException("Null Data in Application.");
        }

        String name = url.getRef();
        if ((name == null) || (name.equals(""))) {
            // No reference. We should return the jar's 
            // content.
            return app.getContent();
        }

        JarSearchResult jsr = 
                BridgeClassLoader.findInJar(name, app.getContent(), false);
        
        if (jsr.isFound() == false) {
            throw new IOException("Data entry not found in Application.");
        }
        
        return new ByteArrayInputStream(jsr.getBytes());
    }

    @Override
    public Object getContent() throws IOException {
        return super.getContent();
    }

    /**
     * Gets the appId of the application pointed by the URL. This method doesn't
     * fetches anything from the net or the bridge, just extracts the data from
     * the URL.
     * @param url URL in bridge2server format.
     * @return Integer value with the id
     * @throws java.io.IOException
     */
    public static synchronized Integer getAppId(URL url) throws IOException {
        String q = url.getQuery();
        
        if (q == null) {
            throw new IOException("AppId missing in query string");
        }
        
        String[] s = q.split("=");
        
        if (s.length != 2) {
            throw new IOException("Malformed Query String.");
        }
        
        return Integer.parseInt(s[1]);
    }

    public Integer getAppId() throws IOException {
        return getAppId(this.url);
    }

    public Application getApplication() {
        try {
            Integer i = getAppId();
            return this.bridge.getAppBridge().getApplication(i);
        } catch (IOException ioe) {
            return null;
        }
    }
}
