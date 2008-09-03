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
package org.joing.server.servlets.vfs;

import org.joing.server.ejb.vfs.FileManagerLocal;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.joing.common.dto.vfs.VFSFileBase;
import org.joing.common.exception.JoingServerException;
import org.joing.common.exception.JoingServerServletException;

/**
 *
 * @author fmorero
 */
public class WriteFileFromArray extends HttpServlet
{
    @EJB()
    private FileManagerLocal fileManagerBean;
    
    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     */
    protected void processRequest( HttpServletRequest request, HttpServletResponse response )
            throws ServletException, IOException
    {
        response.setContentType( "application/octet-stream" );
        
        ObjectInputStream  reader = new ObjectInputStream(  request.getInputStream()   );
        ObjectOutputStream writer = new ObjectOutputStream( response.getOutputStream() );
        
        try
        {
            String sSessionId = (String)  reader.readObject();
            int    nFileId    = (Integer) reader.readObject();
            byte[] ab         = (byte[])  reader.readObject();
            
            ByteArrayInputStream bais = new ByteArrayInputStream( ab );
            VFSFileBase          file = fileManagerBean.writeFile( sSessionId, nFileId, bais );
            
            writer.writeObject( file );
            writer.flush();
        }
        catch( JoingServerException exc )
        {
            writer.writeObject( exc );
            writer.flush();
        }
        catch( Exception exc )
        {
            log( "Error in Servlet: "+ getClass().getName(), exc );
            // Makes the exception to be contained into a JoingServerServletException
            JoingServerServletException jsse = new JoingServerServletException( getClass(), exc );
            writer.writeObject( jsse );
            writer.flush();
        }
        finally
        {
            if( reader != null )
                try{ reader.close(); } catch( IOException exc ) { }
                
            if( writer != null )
                try{ writer.close(); } catch( IOException exc ) { }
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /** 
     * Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     */
    protected void doGet( HttpServletRequest request, HttpServletResponse response )
            throws ServletException, IOException
    {
        processRequest( request, response );
    }

    /** 
     * Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     */
    protected void doPost( HttpServletRequest request, HttpServletResponse response )
            throws ServletException, IOException
    {
        processRequest( request, response );
    }

    /** 
     * Returns a short description of the servlet.
     */
    public String getServletInfo()
    {
        return "Short description";
    }// </editor-fold>
}
