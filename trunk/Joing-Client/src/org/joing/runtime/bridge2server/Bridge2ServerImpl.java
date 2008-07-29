/*
 * Bridge2ServerImpl.java
 *
 * Created on 18 de junio de 2007, 13:23
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

import org.joing.common.clientAPI.runtime.AppBridge;
import org.joing.common.clientAPI.runtime.Bridge2Server;
import org.joing.common.clientAPI.runtime.SessionBridge;
import org.joing.common.clientAPI.runtime.UserBridge;
import org.joing.common.clientAPI.runtime.VFSBridge;

/**
 * Communications from Client to Server.
 * 
 * Note: Classes can not be loaded permanently in memory to avoid synchronize
 * modifier. In this way, each time a communication will happen, a new instance
 * will be created.
 * 
 * @author Francisco Morero Peyrona
 */
public class Bridge2ServerImpl implements Bridge2Server
{
    private static final int VIA_SERVLETS = 1;
    private static final int VIA_SOCKETS  = 2;
    
    private int nVia = 0;
    
    //------------------------------------------------------------------------//
    
    @Override
    public SessionBridge getSessionBridge()
    {
        SessionBridge sb = null;
        
        switch( nVia )
        {
            case VIA_SERVLETS:
                sb = new SessionBridgeServletImpl();
                break;
                
            case VIA_SOCKETS:
                sb = new SessionBridgeSocketImpl();
                break;
        }

        return sb;
    }
    
    @Override
    public UserBridge getUserBridge()
    {
        UserBridge ub = null;
        
        switch( nVia )
        {
            case VIA_SERVLETS:
                ub = new UserBridgeServletImpl();
                break;
                
           case VIA_SOCKETS:
                ub = new UserBridgeSocketImpl();
                break;
        }
        
        return ub;
    }
    
    @Override
    public AppBridge getAppBridge()
    {
        AppBridge app = null;
        
        switch( nVia )
        {
            case VIA_SERVLETS:
                app = new AppBridgeServletImpl();
                break;

            case VIA_SOCKETS:
                app = new AppBridgeSocketImpl();
                break;
        }
        
        return app;
    }
    
    @Override
    public VFSBridge getFileBridge()
    {
        VFSBridge vfs = null;
        
        switch( nVia )
        {
            case VIA_SERVLETS:
                vfs = new VFSBridgeServletImpl();
                break;

            case VIA_SOCKETS:
                vfs = new VFSBridgeSocketImpl();
                break;
        }
        
        return vfs;
    }
    
    //------------------------------------------------------------------------//
    
    // TODO: El constructor de Bridge2ServerImpl debiera ser package scope.
    public Bridge2ServerImpl()
    {
//        if (Thread.currentThread().getId() != Platform.getInstance().getMainId()) {
//            throw new RuntimeException("Only Platform's Main Thread is authorized to instantiate Bridge2Server.");
//        }
        
        /* NEXT: Aquí se pone el código para decidir si va a utilizar la 
                 implementación directa (sockets) o la de Servlets o cualquier otra */
        nVia = VIA_SERVLETS;
    }
}