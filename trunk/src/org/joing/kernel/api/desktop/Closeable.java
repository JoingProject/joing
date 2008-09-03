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

package org.joing.kernel.api.desktop;

/**
 * Indicates that the object has operations to do before being closed.
 * <p>
 * Components that implement this interface will be notified when the desktop is 
 * going to be closed. Having in this way an opportunity to perform the 
 * house-keeping.
 * 
 * @author Francisco Morero Peyrona
 */
public interface Closeable
{    
    /** 
     * Informs to the object that it has to close.
     * <p>
     * It gives an opportunity to the object to make the house-keeping 
     * (auto-clean).
     * <p>
     * Containers traverse recursively all its components invoking close().
     * <p>
     * Desktop elements either will detach themselves from their container 
     * (Desktop) or the Desktop has to know when the element is about to be
     * closed to detach them. In any case, it is not programmer resposability to
     * manually detach elements from Desktop.
     */ 
    void close();
}