/*
 * UserBridgeDirectImpl.java
 *
 * Created on 18 de junio de 2007, 16:00
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

import java.util.List;
import org.joing.common.dto.user.Local;
import org.joing.common.dto.user.User;

// NEXT: Implementar esta clase

/**
 *
 *
 * @author Francisco Morero Peyrona
 */
public class UserBridgeSocketImpl
       extends BridgeSocketBaseImpl
       implements UserBridge
{   
    /** 
     * Creates a new instance of UserBridgeDirectImpl
     *
     * Package scope: only Runtime class can create instances of this class.
     */
    UserBridgeSocketImpl()
    {
        super();
    }

    public User getUser()
    {
        return null;
    }

    public User updateUser( User user )
    {
        return null;
    }

    public List<Local> getAvailableLocales()
    {
        return null;
    }
}