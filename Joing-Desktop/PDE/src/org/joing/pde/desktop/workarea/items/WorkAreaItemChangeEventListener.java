/*
 * WDesktopComponentChangeEventListener.java
 *
 * Created on 10 de febrero de 2007, 19:27
 *
 * (c) 2006 - Francisco Morero Peyrona
 *
 * License: {license}
 */

package org.joing.pde.desktop.workarea.items;

import java.util.EventListener;

/**
 * 
 * @author Francisco Morero Peyrona
 */
public interface WorkAreaItemChangeEventListener extends EventListener
{
    public abstract void deskComponentChanged( WorkAreaItemChangeEvent evt );
}