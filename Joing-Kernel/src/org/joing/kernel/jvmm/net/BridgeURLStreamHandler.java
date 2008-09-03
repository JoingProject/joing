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

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;
import java.util.Properties;
import org.joing.kernel.api.bridge.Bridge2Server;
import org.joing.kernel.jvmm.RuntimeEnum;

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
            Bridge2Server bridge = org.joing.kernel.jvmm.RuntimeFactory.getPlatform().getBridge();

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
    protected synchronized InetAddress getHostAddress(URL u) 
    {
        try
        {
            InputStream is = getClass().getResourceAsStream( "/client.properties" );
            Properties  pp = new Properties();
                        pp.load( is );
                        is.close();
                        
            String sHost = pp.getProperty( RuntimeEnum.JOING_SERVER_URL.getKey() );
            URL    url   = new URL( sHost );
            
            return InetAddress.getByName( url.getHost() );
        }
        catch( Exception e )
        {
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
