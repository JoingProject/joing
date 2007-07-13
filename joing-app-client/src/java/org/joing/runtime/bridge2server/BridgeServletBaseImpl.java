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

/**
 *
 *
 * @author Francisco Morero Peyrona
 */
public class BridgeServletBaseImpl
{
    // SESSION
    static final String SESSION_LOGIN  = "session/Login";
    static final String SESSION_LOGOUT = "session/Logout";
    
    // USER
    static final String USER_GET_USER    = "user/GetUser";
    static final String USER_UPDATE_USER = "user/UpdateUser";
    static final String USER_LOCALS      = "user/GetAvailableLocales";
    
    // APPLICATIONS
    static final String APP_GET_AVAILABLES    = "app/GetAvailableForUser";
    static final String APP_GET_NOT_INSTALLED = "app/GetNotInstalledForUser";
    static final String APP_GET_INSTALLED     = "app/GetInstalledForUser";
    static final String APP_INSTALL           = "app/Install";
    static final String APP_UNINSTALL         = "app/Uninstall";
    static final String APP_GET_PREFERRED     = "app/GetPreferredForType";
    
    // VFS
    static final String VFS_GET_FILE     = "vfs/GetFile";
    static final String VFS_CREATE_DIR   = "vfs/CreateDirectory";
    static final String VFS_CREATE_FILE  = "vfs/CreateFile";
    
    static final String VFS_UPDATE       = "vfs/UpdateFile";
    static final String VFS_COPY         = "vfs/Copy";
    static final String VFS_MOVE         = "vfs/Move";
    static final String VFS_TRASHCAN     = "vfs/Trashcan";
    static final String VFS_DELETE       = "vfs/Delete";
    
    static final String VFS_GET_CHILDS   = "vfs/GetChilds";
    static final String VFS_GET_BY_NOTES = "vfs/GetByNotes";
    static final String VFS_GET_TRASHCAN = "vfs/GetTrashcan";
    
    // CLASS VARIABLES
    private String sBaseURL;
    
    //------------------------------------------------------------------------//
    
    /**
     * Creates a new instance of BridgeServletBaseImpl.
     *
     * Package scope: only Runtime class can create instances of this class.
     */
    BridgeServletBaseImpl()
    {
        sBaseURL = org.joing.runtime.Runtime.getRuntime().getServerBaseURL();
        
        if( ! sBaseURL.endsWith( "/" ) )
            sBaseURL += "/";
    }
    
    //------------------------------------------------------------------------//
    // INNER CLASSES
    //------------------------------------------------------------------------//
    
    /**
     * Allows to easily communicate with a Servlet.
     */
    protected class Channel
    {
        private URLConnection      connServer;
        private ObjectOutputStream writer;
        private ObjectInputStream  reader;
        private boolean            bUseSSL;
        
        protected Channel( String sServletName )
                  throws MalformedURLException, IOException
        {
            this.bUseSSL = false;    // TODO: leer este dato de algún sitio de la app y 
                                     //       utilizarlo: crear uno u otro tipo de conexión
            
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
        
        protected void write( Object o ) throws IOException
        {
            if( bUseSSL )
                writeSSL( o );
            else
                _write( o );
        }
        
        protected Object read() throws IOException, ClassNotFoundException
        {
            if( bUseSSL )
                return readSSL();
            else
                return _read();
        }

        protected void close()
        {
            if( writer != null )
                try{ writer.close(); }catch( IOException exc ) {  }
                
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
            
            if( writer != null )
                try{ writer.close(); } catch( Exception exc ){ }
            
            if( reader != null )
                try{ reader.close(); } catch( Exception exc ){ }
        }
        
        //--------------------------------------------------------------------//
        
        private void _write( Object o ) throws IOException
        {
            // IO streams must be init here: can't be done in constructor
            if( writer == null )
                writer = new ObjectOutputStream( connServer.getOutputStream() );
            
            writer.writeObject( o );
            writer.flush();
        }
        
        private Object _read() throws IOException, ClassNotFoundException
        {
            // IO streams must be init here: can't be done in constructor
            if( reader == null )
                reader = new ObjectInputStream( connServer.getInputStream() );
            
            return reader.readObject();
        }
        
        // TODO: enviar vía SSL
        private void writeSSL( Object o ) throws IOException
        {
            _write( o );
        }

        // TODO: recibir vía SSL
        private Object readSSL() throws IOException, ClassNotFoundException
        {
            return _read();
        }
    }
}