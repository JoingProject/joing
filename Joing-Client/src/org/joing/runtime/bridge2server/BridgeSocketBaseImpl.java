/*
 * BridgeDirectBaseImpl.java
 *
 * Created on 28 de junio de 2007, 11:29
 *
 * Copyright (C) 2007 Francisco Morero Peyrona
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or (at
 * your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 */

package org.joing.runtime.bridge2server;

// NEXT: Implementar esta clase
import org.joing.common.clientAPI.jvmm.Platform;
import org.joing.jvmm.RuntimeFactory;

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