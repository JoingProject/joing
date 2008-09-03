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

package org.joing.common.dto.app;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Serializable;
import org.joing.common.exception.JoingServerAppException;

/**
 * This class has all the properties plus the content (normally the JAR) for
 * the application that the instance represents to.
 *
 * @author Francisco Morero Peyrona
 */
public class Application extends AppDescriptor implements Serializable
{
    private static final long serialVersionUID = 1L;    // TODO: cambiarlo usando: serialver -show
    
    private byte[] btContent = null;
    
    //------------------------------------------------------------------------//
    
    public InputStream getContent()
    {
        return new ByteArrayInputStream( btContent );
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Application other = (Application) obj;
        if (this.btContent != other.btContent && (this.btContent == null || !this.btContent.equals(other.btContent))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + (this.btContent != null ? this.btContent.hashCode() : 0);
        return hash;
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
    public Application() throws JoingServerAppException
    {
    }
    
    // Package scope: this content is injected by AppTransferer
    public void setContents( byte[] btContent )
    {
        this.btContent = btContent;      // TODO: hacer copia defensiva
    }    
}