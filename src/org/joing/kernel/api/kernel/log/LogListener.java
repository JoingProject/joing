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

package org.joing.kernel.api.kernel.log;

/**
 * Public interface for the Event Logger.
 * The concrete implementation must add all relevant info, like
 * timestamps and log level details.
 * 
 * @author Antonio Varela Lizardi <antonio@icon.net.mx>
 */
public interface LogListener {
    
    /**
     * Writes the message specified in the argument msg. The concrete 
     * implementation can add relevant aditional info, like timestamps.
     * @param level Level of the message. If the level is not listed in the
     * Logger class, this method will be ignored.
     * @param msg String with the message.
     */
    void write(Levels level, String msg);
    
}
