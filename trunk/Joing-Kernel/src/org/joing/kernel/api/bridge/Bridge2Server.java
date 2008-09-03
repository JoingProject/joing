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

package org.joing.kernel.api.bridge;

/**
 * This is the connnector with Join'g server side.<br>
 * For simplicity and clearness, it is grouped by funcionality.
 * 
 * @author Francisco Morero Peyrona
 */
public interface Bridge2Server
{
    /**
     * Bridge to server for actions related to Sessions.
     * @return A bridge to server for actions related to Sessions.
     */
    SessionBridge getSessionBridge();

    /**
     * Bridge to server for actions related to Users.
     * @return A bridge to server for actions related to Users.
     */
    UserBridge getUserBridge();

    /**
     * Bridge to server for actions related to Applications.
     * @return A bridge to server for actions related to Applications.
     */
    AppBridge getAppBridge();

    /**
     * Bridge to server for actions related to Files.
     * @return A bridge to server for actions related to Files.
     */
    VFSBridge getFileBridge();
}
