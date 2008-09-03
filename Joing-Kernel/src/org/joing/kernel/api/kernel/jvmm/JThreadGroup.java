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

package org.joing.kernel.api.kernel.jvmm;

import java.io.OutputStream;
import java.util.UUID;

/**
 * Thread Group Abstract Class.
 * 
 * @author Antonio Varela Lizardi <antonio@icon.net.mx>
 */
public abstract class JThreadGroup extends ThreadGroup {

    protected Thread disposer;

    public JThreadGroup() {
        super("JoingThreadGroup_" + UUID.randomUUID().toString());
    }
    
    public JThreadGroup(String name) {
        super(name);
    }
    
    public JThreadGroup(String name, Thread disposer, ThreadGroup parent) {
        super(parent, name);
        this.disposer = disposer;
    }
    
    public JThreadGroup(String name, ThreadGroup parent) {
        this(name, null, parent);
    }
    
    public JThreadGroup(Thread disposer) {
        this();
        this.disposer = disposer;
    }
    
    public JThreadGroup(Thread disposer, ThreadGroup parent) {
        super(parent, "JoingThreadGroup " + UUID.randomUUID().toString());
        this.disposer = disposer;
    }

    public void setDisposer(Thread disposer) {
        this.disposer = disposer;
    }

    public abstract OutputStream getOut();

    public abstract OutputStream getErr();

    /**
     * Closes this ThreadGroup calling the disposer thread.
     */
    public abstract void close();
    
}
