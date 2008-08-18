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
package ejb.vfs;

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
    FileText() 
    {
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
    
    void setContents( char[] chContent )
    {
        this.chContent = chContent;    // TODO: hacer copia defensiva
    }
    
    // TODO: este metodo no es usado: mirar dónde usarlo
    void setEnconding( String sEncoding )
    {
        this.sEncoding = sEncoding;
    }
    
    // TODO: este metodo no es usado: mirar dónde usarlo
    void setMimetype( String sMimeType )
    {
        this.sMimeType = sMimeType;
    }
}