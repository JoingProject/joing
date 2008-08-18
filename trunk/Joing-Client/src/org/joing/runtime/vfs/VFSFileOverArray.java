/*
 * Copyright (C) 2007, 2008 Join'g Team Members. All Rights Reserved.  * Join'g Team Members are listed at project's home page. By the time of   * writting this at: https://joing.dev.java.net/servlets/ProjectMemberList.
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

package org.joing.runtime.vfs;

import org.joing.common.dto.vfs.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Vector;
import org.joing.common.CallBackable;

/**
 *
 * @author Francisco Morero Peyrona
 */
public class VFSFileOverArray extends VFSFile implements VFSFile4IO
{
    private byte[] abContent = new byte[0];
    // TODO:  cambiar la linea sig. por --> private EventListenerList listenerlist = new EventListenerList();
    private Vector<CallBackable> vListeners = new Vector<CallBackable>();
    
    //------------------------------------------------------------------------//
    
    public InputStreamReader getCharReader() throws IOException
    {
        return getCharReader( null );
    }
    
    public OutputStreamWriter getCharWriter() throws IOException
    {
        return getCharWriter( null );
    }
    
    public InputStreamReader getCharReader( String sCharsetName ) throws IOException
    {
        if( sCharsetName == null || sCharsetName.length() == 0 )
            return new InputStreamReader( getByteReader() );
        else
            return new InputStreamReader( getByteReader(), sCharsetName );
    }
    
    public OutputStreamWriter getCharWriter( String sCharsetName ) throws IOException
    {
        if( sCharsetName == null || sCharsetName.length() == 0 )
            return new OutputStreamWriter( getByteWriter() );
        else
            return new OutputStreamWriter( getByteWriter(), sCharsetName );
    }
    
    public InputStream getByteReader() throws IOException
    {
        if( abContent == null )
            throw new IOException( "Error accessing file '"+ getAbsolutePath() +"'" );
        else
            return (new ByteArrayInputStream( abContent ));
    }
    
    public OutputStream getByteWriter() throws IOException
    {
        return new MyOutputStream();
    }
    
    /**
     * 
     * @param l
     */
    public void addCallBackListener( CallBackable l )
    {
        vListeners.add( l );
    }
    
    /**
     * 
     * @param l
     */
    public void removeCallBackListener( CallBackable l )
    {
        vListeners.remove( l );
    }
    
    /**
     * 
     * @return
     */
    public byte[] intern()
    {
        byte[] ab = new byte[ abContent.length ];
        System.arraycopy( abContent, 0, ab, 0, abContent.length );   // Defensive copy
        return ab;
    }
    
    protected void flush()
    {
        for( CallBackable cb : vListeners )
            cb.execute( this );
    }
    
    //------------------------------------------------------------------------//
    // NEXT: El constructor debiera ser package (o al menos protected) pero está en Common
    
    /**
     * Class constructor (this class is a DTO).
     * <p>
     * For security and encapsulation reasons, the constructor should have 
     * package scope. But it is impossible as Java OOP is defined. Therefore,
     * this constructor should not be used from applications.
     */
    public VFSFileOverArray( FileDescriptor fd, byte[] ab )
    {
        super( fd );
        abContent = new byte[ ab.length ];
        System.arraycopy( ab, 0, abContent, 0, ab.length );   // Defensive copy
    }
    
    //------------------------------------------------------------------------//
    // INNER CLASS: An OutputStream that informs when it is closed.
    //------------------------------------------------------------------------//
    private final class MyOutputStream extends ByteArrayOutputStream
    {
        private boolean bClosed = false;
        
        public void flush() throws IOException
        {
            VFSFileOverArray.this.abContent = super.toByteArray();
            VFSFileOverArray.this.flush();
        }
        
        public void close() throws IOException
        {
            if( ! bClosed )
            {
                flush();
                super.close();   // Not needed (does nothing): placed just for clarity
                bClosed = true;
            }
        }
        
        protected void finalize() throws Throwable
        {
            close();
            super.finalize();
        }
    }
}