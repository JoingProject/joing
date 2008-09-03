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

import java.net.MalformedURLException;
import java.net.URL;
import org.joing.common.dto.app.Application;

/**
 * Class with utility methods to build bridge2server style URL's. The URL 
 * format is:
 * <ol>
 *  <li>
 *   bridge2server:/!/?appId=NN
 *  </li>
 *  <li>
 *   bridge2server:/!/?appId=NN#/path/to/file
 *  </li>
 *  </ol>
 * The NN value in the query part indicates the appId value found in 
 * Application.getId(). The optional part following the pound signs indicates 
 * a file within the enclosed jar file. The former format it's used by 
 * ClassLoader.findResource().
 * @author Antonio Varela Lizardi <antonio@icon.net.mx>
 */
public class URLFormat {

    /**
     * Builds an URL object from the Application and extra parameter.
     * @param application Application referred.
     * @param entry Entry within the enclosed jar. If this argument is
     * supplied with null value, the method will build an URL pointing to
     * the Jar file. 
     * @return URL Object.
     * @throws java.net.MalformedURLException
     */
    public static URL toURL(Application application, String entry)
            throws MalformedURLException {

        //StringBuilder sb = new StringBuilder("bridge2server:/!/?appId=");
        StringBuilder sb = new StringBuilder("bridge2server:appId=");

        sb.append(application.getId());
        
        if (entry != null) {
            sb.append("#").append(entry);
        }

        return new URL(sb.toString());
    }
    
    
    public static URL toURL(Application application) 
            throws MalformedURLException {
        return toURL(application, null);
    }
    
}
