/*
 * Copyright (C) 2007, 2008 Join'g Team Members.  All Rights Reserved.
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

package org.joing.common.dto.vfs;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Vector;
import org.joing.common.CallBackable;

/**
 *
 * @author Francisco Morero Peyrona
 */
public class FileOverArray extends File4IO
{
    private byte[] abContent = new byte[0];
    // TODO:  cambiar la linea sig. por --> private EventListenerList listenerlist = new EventListenerList();
    private Vector<CallBackable> vListeners = new Vector<CallBackable>();
    
    //------------------------------------------------------------------------//
    
    public InputStream getReader() throws IOException
    {
        if( abContent == null )
            throw new IOException( "Error accessing file '"+ getAbsolutePath() +"'" );
        else
            return (new ByteArrayInputStream( abContent ));
    }
    
    public OutputStream getWriter() throws IOException
    {
        return new MyOutputStream();
    }
    
    public void addCallBackListener( CallBackable l )
    {
        vListeners.add( l );
    }

    public void removeCallBackListener( CallBackable l )
    {
        vListeners.remove( l );
    }
    
    protected void fireStreamClosed()
    {
        for( CallBackable cb : vListeners )
            cb.execute( this );
    }
    
    //------------------------------------------------------------------------//
    // NEXT: El constructor debiera ser package (o al menos protected)
    
    /**
     * Class constructor (this class is a DTO).
     * <p>
     * For security and encapsulation reasons, the constructor should have 
     * package scope. But it is impossible as Java OOP is defined. Therefore,
     * this constructor should not be used from applications.
     */
    public FileOverArray( InputStream is )
    {    
        byte[] abBuffer = new byte[ 1024*16 ];
        int    nReaded  = 0;
        
        try
        {
            while( nReaded != -1 )
            {
                nReaded = is.read( abBuffer );

                if( nReaded != -1 )
                {
                    // Create a temp array with enought space for btContent and new bytes readed
                    byte[] abTemp = new byte[ abContent.length + nReaded ];
                    // Copy btContent to new array
                    System.arraycopy( abContent, 0, abTemp, 0, abContent.length );
                    // Copy readed bytes at tail of new array
                    System.arraycopy( abBuffer, 0, abTemp, abContent.length, nReaded );
                    // Changes btContent reference to temp array
                    abContent = abTemp;
                }
            }
            
            is.close();
        }
        catch( IOException exc )
        {
            abContent = null;
        }
    }
    
    //------------------------------------------------------------------------//
    // INNER CLASS: An output stream that informs when it is closed.
    //------------------------------------------------------------------------//
    private final class MyOutputStream extends ByteArrayOutputStream
    {
        public void close() throws IOException
        {
            FileOverArray.this.abContent = toByteArray();
            FileOverArray.this.fireStreamClosed();
            super.close();   // Not needed: just for clarity
        }
        
        protected void finalize() throws Throwable
        {
            close();
            super.finalize();
        }
    }
}