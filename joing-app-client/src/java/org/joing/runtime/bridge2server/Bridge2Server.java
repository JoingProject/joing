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

import javax.naming.Context;


/**
 *
 *
 * @author Francisco Morero Peyrona
 */
public class Bridge2Server
{
    private static Bridge2Server instance = null;

    // Note: SessionBridge.class is used only at begining and ending of session:
    //       it is not loaded permanently in memory to save resources (memory).
    //       Something similar happens with UserBridge.class (it's used rarely).
    
    private AppBridge  app  = null;  // Loaded permanently for speed
    private VFSBridge  vfs  = null;  // Loaded permanently for speed
    
    private Context context = null;
    
    private String  sSessionId = null;
    
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
        
        if( this.context == null )
            sb = new SessionBridgeServletImpl();
        else
            sb = new SessionBridgeDirectImpl( this.context );
        
        return sb;
    }
    
    public UserBridge getUserBridge()
    {
        UserBridge ub = null;
            
        if( this.context == null )
            ub = new UserBridgeServletImpl();
        else
            ub = new UserBridgeDirectImpl( this.context );
        
        return ub;
    }
    
    public AppBridge getAppBridge()
    {
        if( this.app == null )
        {
            if( this.context == null )
                this.app = new AppBridgeServletImpl();
            else
                this.app = new AppBridgeDirectImpl( this.context );
        }
        
        return this.app;
    }
    
    public VFSBridge getFileBridge()
    {
        if( this.vfs == null )
        {
            if( this.context == null )
                this.vfs = new VFSBridgeServletImpl();
            else
                this.vfs = new VFSBridgeDirectImpl( this.context );
        }
        
        return this.vfs;
    }
    
    public String getSessionId()
    {
        return this.sSessionId;
    }
    
    //------------------------------------------------------------------------//
    // PACKAGE SCOPE
    
    void setSessionId( String sSessionId )
    {
        this.sSessionId = sSessionId;
    }
    
    //------------------------------------------------------------------------//
    // PRIVATE SCOPE
    
    private Bridge2Server()
    {
        /* TODO: revisar esto: hay que mirar si se puede conectar directamente
        Hashtable env = new Hashtable();
                  //Creo que esto no hace falta --> env.put( Context.INITIAL_CONTEXT_FACTORY, "org.jnp.interfaces.NamingContextFactory" );
                  //env.put( Context.PROVIDER_URL, "iiop://localhost/" ); 
                  env.put( Context.PROVIDER_URL, "jnp://localhost/" ); 
        try
        {
            //this.context = new InitialContext( env );
            this.context = new InitialContext();
        }
        catch( Exception exc )
        {
            // TODO: Reportarlo vía notificación de errores del desktop
            exc.printStackTrace();
        }*/
    }
}