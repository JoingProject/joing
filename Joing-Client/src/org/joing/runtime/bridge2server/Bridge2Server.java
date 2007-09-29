/*
 * Bridge2Server.java
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

import org.joing.jvmm.Platform;

/**
 *
 *
 * @author Francisco Morero Peyrona
 */
public class Bridge2Server
{
    private static final int VIA_SERVLETS = 1;
    private static final int VIA_SOCKETS   = 2;
    
    private static Bridge2Server instance   = null;
    
    private int    nVia       = 0;
    private String sSessionId = null;
    
    // Note: SessionBridge.class is used only at begining and ending of session:
    //       it is not loaded permanently in memory to save resources (memory).
    //       Something similar happens with UserBridge.class (it's used rarely).
    
    private AppBridge  app  = null;  // Loaded permanently for speed
    private VFSBridge  vfs  = null;  // Loaded permanently for speed
    
    //------------------------------------------------------------------------//
    
    public static Bridge2Server getInstance()
    {
        if( instance == null )
            instance = new Bridge2Server();
        
        return instance;
    }
    
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
    
    public AppBridge getAppBridge()
    {
        if( app == null )
        {
            switch( nVia )
            {
                case VIA_SERVLETS:
                    app = new AppBridgeServletImpl();
                    break;

                case VIA_SOCKETS:
                    app = new AppBridgeSocketImpl();
                    break;
            }
        }
        
        return this.app;
    }
    
    public VFSBridge getFileBridge()
    {
        if( vfs == null )
        {
            switch( nVia )
            {
                case VIA_SERVLETS:
                    vfs = new VFSBridgeServletImpl();
                    break;

                case VIA_SOCKETS:
                    vfs = new VFSBridgeSocketImpl();
                    break;
            }
        }
        
        return this.vfs;
    }
    
    public String getSessionId()
    {
        return sSessionId;
    }
    
    //------------------------------------------------------------------------//
    // PACKAGE SCOPE
    
    void setSessionId( String sSessionId )
    {
        this.sSessionId = sSessionId;
    }
    
    //------------------------------------------------------------------------//
    //
    
    public Bridge2Server()
    {
//        if (Thread.currentThread().getId() != Platform.getInstance().getMainId()) {
//            throw new RuntimeException("Only Platform's Main Thread is authorized to instantiate Bridge2Server.");
//        }
        
        /* NEXT: Aquí se pone el código para decidir si va a utilizar la 
                 implementación directa (sockets) o la de Servlets */
        nVia = VIA_SERVLETS;
    }
}