/*
 * Application.java
 * 
 * Created on 31-jul-2007, 18:39:54
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
package ejb.app;

import ejb.Constant;
import ejb.JoingServerException;
import ejb.vfs.FileSystemTools;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 *
 * @author fmorero
 */
public class Application extends AppDescriptor
{
    private byte[] btContent;
    
    public Application( ApplicationEntity _Application ) 
           throws JoingServerAppException
    {
        super( _Application );
        setContents();
    }
    
    public InputStream getContent()
    {        
        return new ByteArrayInputStream( btContent );
    }
    
    //------------------------------------------------------------------------//
    
    private void setContents()
    {
        java.io.File fApp = FileSystemTools.getApplication( getExecutable() );
        
        btContent = new byte[ (int) fApp.length() ];
        
        try
        {
            FileInputStream fis  = new FileInputStream( fApp );
                            fis.read( btContent, 0, btContent.length );
        }
        catch( RuntimeException exc )
        {
            if( ! (exc instanceof JoingServerException) )
            {
                Constant.getLogger().throwing( getClass().getName(), "setContents()", exc );
                exc = new JoingServerAppException( JoingServerException.ACCESS_DB, exc );
            }

            throw exc;
        }
        catch( IOException exc )
        {
            Constant.getLogger().throwing( getClass().getName(), "setContents()", exc );
            throw new JoingServerAppException( JoingServerException.ACCESS_NFS, exc );
        }
    }
}