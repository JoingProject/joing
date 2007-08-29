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
package ejb.vfs;

import ejb.Constant;
import java.io.BufferedReader;
import java.io.CharArrayReader;
import java.io.IOException;

/**
 * A subclass of FileDescritor that contains the native file contents 
 * being the native file a text file.
 * 
 * @author Francisco Morero Peyrona
 */
public class FileText extends FileDescriptor
{
    private char[] chContent;
    private String sEncoding;
    private String sMimeType;
    
    /**
     * Class constructor (this class is a DTO).
     * <p>
     * For security and encapsulation reasons, the constructor has package scope:
     * only the Manager EJB can create them.<br>
     * If any other part of the application would need to create for example an
     * empty instance of this class, then a method can be added to the Manager
     * EJB (this method can return an empty instance).
     */
    FileText( FileEntity _file ) 
    {
        super( _file );
    }

    public BufferedReader getContent()
    {
        CharArrayReader reader = new CharArrayReader( chContent );
        BufferedReader  br     = new BufferedReader( reader );
        
        return br;
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
    // PACKAGE SCOPE
    
    /**
     * 
     * 
     * @param BufferedReader
     */
    void setContents( BufferedReader br )
    {
        chContent = new char[ (int) getSize() ];
        
        try
        {
            br.read( chContent, 0, chContent.length );
        }
        catch( IOException exc )
        {
            Constant.getLogger().throwing( getClass().getName(), "setContents(...)", exc );
        }
    }
    
    void setEnconding( String sEncoding )
    {
        this.sEncoding = sEncoding;
    }
    
    void setMimetype( String sMimeType )
    {
        this.sMimeType = sMimeType;
    }
}