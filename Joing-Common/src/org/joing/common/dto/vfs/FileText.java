/*
 * FileText.java
 * 
 * Created on 15-jul-2007, 11:34:49
 * 
 * Author: Francisco Morero Peyrona.
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

package org.joing.common.dto.vfs;

import java.io.CharArrayReader;
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;

/**
 * A subclass of FileDescritor that contains the native file contents 
 * being the native file a text file.
 * 
 * @author Francisco Morero Peyrona
 */
public class FileText extends FileDescriptor implements Serializable
{
    private char[] chContent = "".toCharArray();
    private String sEncoding = "UTF-8";
    private String sMimeType = "text/plain";
    
    //------------------------------------------------------------------------//
    
    public Reader getContent()
    {
        CharArrayReader reader = new CharArrayReader( chContent );
        
        return reader;
    }
    
    public void setContent( Writer writer )
    {
        //writer.
    }
    
    public String getEncoding()
    {
        return sEncoding;
    }
    
    public String getMimeType()
    {
        return sMimeType;
    }
    
    //------------------------------------------------------------------------//
    // NEXT: Los siguientes métodos debieran ser package (o al menos protected)
    
    /**
     * Class constructor (this class is a DTO).
     * <p>
     * For security and encapsulation reasons, the constructor has package scope:
     * only the Manager EJB can create them.<br>
     * If any other part of the application would need to create for example an
     * empty instance of this class, then a method can be added to the Manager
     * EJB (this method can return an empty instance).
     */
    public FileText() 
    {
    }
    
    public void setContent( char[] chContent )
    {
        this.chContent = new char[ chContent.length ];
        
        System.arraycopy( chContent, 0, this.chContent, 0, chContent.length );
    }
    
    // TODO: este metodo no es usado: mirar dónde usarlo
    public void setEnconding( String sEncoding )
    {
        this.sEncoding = sEncoding;
    }
    
    // TODO: este metodo no es usado: mirar dónde usarlo
    public void setMimetype( String sMimeType )
    {
        this.sMimeType = sMimeType;
    }
}