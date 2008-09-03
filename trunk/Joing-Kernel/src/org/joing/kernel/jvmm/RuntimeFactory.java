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

package org.joing.kernel.jvmm;

import org.joing.kernel.api.kernel.jvmm.Platform;

/**
 * Una de las desiciones importantes aqui será determinar que implementación
 * de Platform será la que se regresa. Esta desicion debera ser influida
 * por ejemplo por factores como la plataforma (JavaSE, JavaME, etc).
 * 
 * @author Antonio Varela Lizardi <antonio@icon.net.mx>
 */
public class RuntimeFactory {

    private static final Platform platform;

    static {
        platform = new PlatformImpl();
    }
    
    public RuntimeFactory() {
        if (platform == null) {
            throw new RuntimeException("Critical: Platform instance not initialized.");
        }
    }
    
    public static Platform getPlatform() {
        return platform;
    }
}
