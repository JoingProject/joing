package org.joing.server.jsf;

import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import javax.faces.context.FacesContext;

/**
 *
 * @author Fernando
 */
public class Util {

    private static abstract class DummyMap<K,V> implements Map<K, V> {

	public int size() {
	    throw new UnsupportedOperationException("Not supported yet.");
	}

	public boolean isEmpty() {
	    return false;
	}

	public boolean containsKey(Object key) {
	    return true;
	}

	public boolean containsValue(Object value) {
	    throw new UnsupportedOperationException("Not supported yet.");
	}

	public V put(K key, V value) {
	    throw new UnsupportedOperationException("Not supported yet.");
	}

	public V remove(Object key) {
	    throw new UnsupportedOperationException("Not supported yet.");
	}

	public void putAll(Map<? extends K, ? extends V> m) {
	    throw new UnsupportedOperationException("Not supported yet.");
	}

	public void clear() {
	    throw new UnsupportedOperationException("Not supported yet.");
	}

	public Set<K> keySet() {
	    throw new UnsupportedOperationException("Not supported yet.");
	}

	public Collection<V> values() {
	    throw new UnsupportedOperationException("Not supported yet.");
	}

	public Set<Entry<K, V>> entrySet() {
	    throw new UnsupportedOperationException("Not supported yet.");
	}
	
    }
    
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
    public Map<String, String> getPath() {
	return path;
    }
}
