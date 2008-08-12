/*
 * BridgeServletBaseImpl.java
 *
 * Created on 28 de junio de 2007, 11:33
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

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.rmi.RemoteException;
import org.joing.common.exception.JoingServerException;
import org.joing.common.clientAPI.jvmm.Platform;
import org.joing.common.clientAPI.log.JoingLogger;
import org.joing.common.clientAPI.log.SimpleLoggerFactory;
import org.joing.jvmm.RuntimeFactory;

/**
 *
 *
 * @author Francisco Morero Peyrona
 */
public class BridgeServletBaseImpl
{
    // SESSION
    protected static final String SESSION_LOGIN  = "session/Login";
    protected static final String SESSION_LOGOUT = "session/Logout";
    
    // USER
    protected static final String USER_GET_USER    = "user/GetUser";
    protected static final String USER_UPDATE_USER = "user/UpdateUser";
    protected static final String USER_LOCALS      = "user/GetAvailableLocales";
    
    // APPLICATIONS
    protected static final String APP_GET_AVAILABLES    = "app/GetAvailableForUser";
    protected static final String APP_GET_NOT_INSTALLED = "app/GetNotInstalledForUser";
    protected static final String APP_GET_INSTALLED     = "app/GetInstalledForUser";
    protected static final String APP_INSTALL           = "app/Install";
    protected static final String APP_UNINSTALL         = "app/Uninstall";
    protected static final String APP_GET_PREFERRED     = "app/GetPreferredForType";
    protected static final String APP_GET_APPLICATION   = "app/GetApplication";
    protected static final String APP_SERVLET = "ApplicationServlet";
    
    // VFS
    protected static final String VFS_GET_FILE_DESCRIPTOR = "vfs/GetFileDescriptor";
    protected static final String VFS_CREATE_DIR          = "vfs/CreateDirectory";
    protected static final String VFS_CREATE_FILE         = "vfs/CreateFile";
    protected static final String VFS_GET_FILE            = "vfs/ReadFileToArray";
    protected static final String VFS_WRITE_FILE          = "vfs/WriteFileFromArray";
    protected static final String VFS_UPDATE              = "vfs/UpdateFile";
    protected static final String VFS_COPY                = "vfs/Copy";
    protected static final String VFS_MOVE                = "vfs/Move";
    protected static final String VFS_TRASHCAN            = "vfs/Trashcan";
    protected static final String VFS_DELETE              = "vfs/Delete";
    protected static final String VFS_GET_ROOTS           = "vfs/GetRoots";
    protected static final String VFS_GET_CHILDS          = "vfs/GetChilds";
    protected static final String VFS_GET_BY_NOTES        = "vfs/GetByNotes";
    protected static final String VFS_GET_TRASHCAN        = "vfs/GetTrashcan";
    
    // CLASS VARIABLES
    protected Platform platform;
    
    private String sBaseURL;
    
    //------------------------------------------------------------------------//
    
    /**
     * Creates a new instance of BridgeServletBaseImpl.
     *
     * Package scope: only Bridge2Server class can create instances of this class.
     */
    BridgeServletBaseImpl()
    {
        platform = RuntimeFactory.getPlatform();
        sBaseURL = platform.getServerBaseURL();
        
        if( ! sBaseURL.endsWith( "/" ) )
            sBaseURL += "/";
        
        sBaseURL += "servlets/";
    }
    
    //------------------------------------------------------------------------//
    // INNER CLASSES
    //------------------------------------------------------------------------//
    
    /**
     * Allows to easily communicate with a Servlet.
     */
    protected class Channel    // TODO: hcaer que pueda ir tb vía SSL
    {
        private URLConnection      connServer;
        private ObjectOutputStream writer;
        private ObjectInputStream  reader;
        
        protected Channel( String sServletName )
                  throws JoingServerException
        {
            try
            {
                URL url = new URL( BridgeServletBaseImpl.this.sBaseURL + sServletName );

                connServer = url.openConnection();

                // Inform the connection that we will send output and accept input
                connServer.setDoInput( true );
                connServer.setDoOutput( true );

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
            super.finalize();
            close();
        }
        
        //--------------------------------------------------------------------//
        
        private void handle( Exception exc ) throws JoingServerException
        {
            JoingServerException e;

            if( exc instanceof JoingServerException )
            {
                e = (JoingServerException) exc;
            }
            else
            {
                String sMsg = exc.getLocalizedMessage();
                
                if( sMsg == null || sMsg.length() == 0 )
                    sMsg = exc.getMessage();
                
                e = new JoingServerException( "External error: "+ sMsg, exc );
            }
            
            throw e;
        }
    }
}