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

// NEXT: Implementar esta clase
import org.joing.kernel.api.kernel.jvmm.Platform;
import org.joing.kernel.jvmm.RuntimeFactory;

/**
 *
 *
 * @author Francisco Morero Peyrona
 */
public class BridgeSocketBaseImpl
{   
    protected Platform platform = RuntimeFactory.getPlatform();
    
    /** 
     * Creates a new instance of BridgeDirectBaseImpl 
     *
     * Package scope: only Runtime class can create instances of this class.
     */
    BridgeSocketBaseImpl()
    {
    }
}