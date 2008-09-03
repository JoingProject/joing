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

package org.joing.kernel.runtime.bridge2server;

import java.util.HashMap;
import java.util.Map;
import org.joing.kernel.api.kernel.log.JoingLogger;
import org.joing.kernel.api.kernel.log.Levels;
import org.joing.kernel.api.kernel.log.Logger;
import org.joing.kernel.api.kernel.log.SimpleLoggerFactory;
import org.joing.common.dto.app.Application;

/**
 * This is the base for a complete cache implementation. The cache
 * must be shared with all AppBridge implementations.
 * 
 * @author Antonio Varela Lizardi <antonio@icon.net.mx>
 */
class ApplicationCache {

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
