/*
 * Copyright (C) 2007, 2008 Join'g Team Members. All Rights Reserved.
 * Join'g Team Members are listed at project's home page. By the time of 
 * writting this at: https://joing.dev.java.net/servlets/ProjectMemberList.
 *
 * This file is part of Join'g project: www.joing.org
 *
 * GNU Classpath is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the free
 * Software Foundation; either version 3, or (at your option) any later version.
 * 
 * GNU Classpath is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * GNU Classpath; see the file COPYING.  If not, write to the Free Software 
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */

package org.joing.kernel.jvmm.net;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import org.joing.kernel.api.bridge.Bridge2Server;
import org.joing.common.dto.app.Application;
import org.joing.kernel.jvmm.BridgeClassLoader;
import org.joing.kernel.jvmm.JarSearchResult;

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
        
        if (app == null) {
            throw new IOException("Could not fetch the application (got null).");
        }

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
