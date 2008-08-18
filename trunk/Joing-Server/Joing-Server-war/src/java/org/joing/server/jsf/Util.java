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
package org.joing.server.jsf;

import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import javax.faces.context.FacesContext;

/**
 *
 *  <p>Utilities for use from the JavaserverFaces templates.</p>
 * 
 * @author Fernando Ortigosa
 */
public class Util {

    /**
     * <p>Many utilities will need the use of a "dummy" map mplementation, so
     * we can use the JSF "map trick" to "invoke" methods.</p>
     * 
     * <p>This map implementation overrides all methods but the get one, in
     * order to have a base class for easily creating map tricks</p>
     * 
     */
    private static abstract class DummyMap<K,V> implements Map<K, V> {

	public boolean isEmpty() {
	    return false;
	}

	public int size() {
	    throw new UnsupportedOperationException();
	}

	public boolean containsKey(Object key) {
	    return true;
	}

	public boolean containsValue(Object value) {
	    throw new UnsupportedOperationException();
	}

	public V put(K key, V value) {
	    throw new UnsupportedOperationException();
	}

	public V remove(Object key) {
	    throw new UnsupportedOperationException();
	}

	public void putAll(Map<? extends K, ? extends V> m) {
	    throw new UnsupportedOperationException();
	}

	public void clear() {
	    throw new UnsupportedOperationException();
	}

	public Set<K> keySet() {
	    throw new UnsupportedOperationException();
	}

	public Collection<V> values() {
	    throw new UnsupportedOperationException();
	}

	public Set<Entry<K, V>> entrySet() {
	    throw new UnsupportedOperationException();
	}
	
    }
    
    /**
     * <p>Map implementation where the get method returns the received
     * path as a server relative path, it's made by pre-pending the
     * request context to the path.</p>
     */
    private static class PathMap extends DummyMap<String, String> {

	public String get(Object key) {
	    
	    FacesContext ctx = FacesContext.getCurrentInstance();
	    
	    if(ctx == null) {
		throw new NullPointerException("FacesContext");
	    }
	    
	    String path = ctx.getExternalContext().getRequestContextPath();

	    String file = String.valueOf(key);
	    
	    if(!file.startsWith("/") && !path.endsWith("/")) {
		file = "/" + file;
	    }
	    
	    return path + file;
	    
	}
    }
    
    private Map<String, String> path = new PathMap();

    /**
     * Returns an utility map which calculates server relative paths.
     */
    public Map<String, String> getPath() {
	return path;
    }
}
