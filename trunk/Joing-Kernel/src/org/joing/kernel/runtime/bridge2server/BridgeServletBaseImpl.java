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

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;
import org.joing.common.exception.JoingServerException;
import org.joing.kernel.api.kernel.jvmm.Platform;
import org.joing.kernel.api.kernel.log.JoingLogger;
import org.joing.kernel.api.kernel.log.SimpleLoggerFactory;
import org.joing.kernel.jvmm.RuntimeFactory;

/**
 *
 *
 * @author Francisco Morero Peyrona
 */
public class BridgeServletBaseImpl
{
    // SESSION
    protected static final String SESSION_LOGIN             = "session/Login";
    protected static final String SESSION_LOGOUT            = "session/Logout";
    protected static final String SESSION_IS_VALID_PASSWORD = "session/IsValidPassword";
    protected static final String SESSION_GET_SYSTEM_INFO   = "session/GetSystemInfo";
    
    // USER
    protected static final String USER_GET_USER             = "user/GetUser";
    protected static final String USER_UPDATE_USER          = "user/UpdateUser";
    protected static final String USER_LOCALS               = "user/GetAvailableLocales";
    
    // APPLICATIONS
    protected static final String APP_GET_AVAILABLES        = "app/GetAvailableForUser";
    protected static final String APP_GET_NOT_INSTALLED     = "app/GetNotInstalledForUser";
    protected static final String APP_GET_INSTALLED         = "app/GetInstalledForUser";
    protected static final String APP_INSTALL               = "app/Install";
    protected static final String APP_UNINSTALL             = "app/Uninstall";
    protected static final String APP_GET_PREFERRED         = "app/GetPreferredForType";
    protected static final String APP_GET_APPLICATION       = "app/GetApplication";
    protected static final String APP_SERVLET               = "app/ApplicationServlet";
    
    // VFS
    protected static final String VFS_GET_FILE              = "vfs/GetFile";
    protected static final String VFS_CREATE_DIR            = "vfs/CreateDirectory";
    protected static final String VFS_CREATE_FILE           = "vfs/CreateFile";
    protected static final String VFS_READ_FILE_TO_ARRAY    = "vfs/ReadFileToArray";
    protected static final String VFS_WRITE_FILE_FROM_ARRAY = "vfs/WriteFileFromArray";
    protected static final String VFS_UPDATE                = "vfs/UpdateFile";
    protected static final String VFS_COPY                  = "vfs/Copy";
    protected static final String VFS_MOVE                  = "vfs/Move";
    protected static final String VFS_TO_TRASHCAN           = "vfs/ToTrashcan";
    protected static final String VFS_DELETE                = "vfs/Delete";
    protected static final String VFS_GET_ROOTS             = "vfs/GetRoots";
    protected static final String VFS_GET_CHILDREN          = "vfs/GetChildren";
    protected static final String VFS_GET_BY_NOTES          = "vfs/GetByNotes";
    protected static final String VFS_GET_TRASHCAN          = "vfs/GetTrashcan";
    
    // CLASS VARIABLES ----------------------//
    
    private String sBaseURL;
    
    //------------------------------------------------------------------------//
    
    /**
     * Creates a new instance of BridgeServletBaseImpl.
     *
     * Package scope: only Bridge2Server class can create instances of this class.
     */
    BridgeServletBaseImpl()
    {
        Platform platform = RuntimeFactory.getPlatform();
        sBaseURL = platform.getServerBaseURL();
        
        if( ! sBaseURL.endsWith( "/" ) )
            sBaseURL += "/";
        
        sBaseURL += "joing/servlets/";
    }
    
    //------------------------------------------------------------------------//
    // INNER CLASSES
    //------------------------------------------------------------------------//
    
    /**
     * Allows to easily communicate with a Servlet.
     */
    protected class Channel    // TODO: hacer que tb funcione vía SSL
    {
        private HttpURLConnection  connServer;
        private ObjectOutputStream writer;
        private ObjectInputStream  reader;
        
        protected Channel( String sServletName )
        {
            this( sServletName, true, true );
        }
        
        protected Channel( String sServletName, boolean bDoInput, boolean bDoOutput )
                  throws JoingServerException
        {
            try
            {
                URL url = new URL( BridgeServletBaseImpl.this.sBaseURL + sServletName );
                
                connServer = (HttpURLConnection) url.openConnection();
                connServer.setRequestMethod( "POST" );
                
                // Inform the connection that we will send output and accept input
                connServer.setDoInput( bDoInput );
                connServer.setDoOutput( bDoOutput );
                
                // Don't use a cached version of URL connection.
                connServer.setUseCaches( false );
                connServer.setDefaultUseCaches( false );
                
                // Specify the content type
                connServer.setRequestProperty( "Content-Type", "application/octet-stream" );
                connServer.connect();
            }
            catch( MalformedURLException exc )
            {
                handle( exc );
            }
            catch( IOException exc )
            {
                handle( exc );
            }
        }
        
        protected void write( Object o ) throws JoingServerException
        {
            try
            {
                // IO streams must be init here: can't be done in constructor
                if( writer == null )
                    writer = new ObjectOutputStream( connServer.getOutputStream() );
                    
                writer.writeObject( o );
                writer.flush();
            }
            catch( IOException exc )
            {
                handle( exc );
            }
        }
        
        protected Object read() throws JoingServerException
        {
            Object obj = null;
            
            try
            {
                // IO streams must be init here: can't be done in constructor
                if( reader == null )
                    reader = new ObjectInputStream( connServer.getInputStream() );

                obj = reader.readObject();

                // antoniovl: This is added to support RemoteException without
                // change in the handle() method.
                if (obj instanceof RemoteException)
                    obj = new JoingServerException(((RemoteException)obj).getMessage());
                
                if( obj instanceof JoingServerException )
                    handle( (JoingServerException) obj );
            }
            catch( IOException exc )
            {
                handle( exc );
            }
            catch( ClassNotFoundException exc )
            {
                SimpleLoggerFactory.getLogger( JoingLogger.ID ).critical( "This exception should not happen", exc.getException() );
            }
            
            return obj;
        }

        protected void close()
        {
            if( writer != null )
                try{ writer.close(); }catch( IOException exc ) { }
                
            if( reader != null )
                try{ reader.close(); }catch( IOException exc ) { }
        }

        /**
         * Users of this class should explicitly invoke this.close(), but just
         * in case it is forgotten, it is done by the destructor.
         */
        @Override
        protected void finalize() throws Throwable
        {
            close();
            super.finalize();
        }
        
        //--------------------------------------------------------------------//
        
        private void handle( Exception exc ) throws JoingServerException
        {
            JoingServerException e;

            if( exc instanceof JoingServerException )
                e = (JoingServerException) exc;
            else
                e = new JoingServerException( "External error", exc );
            
            throw e;
        }
    }
}