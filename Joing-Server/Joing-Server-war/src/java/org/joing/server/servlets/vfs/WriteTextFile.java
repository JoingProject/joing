/*
 * WriteTextFile.java
 *
 * Created on 1 de julio de 2007, 20:55
 */

package org.joing.server.servlets.vfs;

import org.joing.common.exception.JoingServerException;
import org.joing.common.exception.JoingServerServletException;
import ejb.vfs.FileManagerLocal;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import javax.ejb.EJB;

import javax.servlet.*;
import javax.servlet.http.*;
import org.joing.common.dto.vfs.FileDescriptor;
import org.joing.common.dto.vfs.FileText;

/**
 *
 * @author Francisco Morero Peyrona
 * @version
 */
public class WriteTextFile extends HttpServlet
{
    @EJB()
    private FileManagerLocal fileManagerBean;
    
    /** Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
              throws ServletException, IOException
    {
        // Response is binary because the answer is a serialized object
        response.setContentType( "application/octet-stream" );
        
        ObjectInputStream  reader = new ObjectInputStream(  request.getInputStream()   );
        ObjectOutputStream writer = new ObjectOutputStream( response.getOutputStream() );
        
        try
        {
            // Read from client (desktop)
            String         sSessionId = (String)   reader.readObject();
            FileText       fileText   = (FileText) reader.readObject();
            FileDescriptor fileDescr  = fileManagerBean.writeTextFile( sSessionId, fileText );
            
            // Write to Client (desktop) the result of the operation
            writer.writeObject( fileDescr );
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
    /** Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException
    {
        processRequest(request, response);
    }
    
    /** Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException
    {
        processRequest(request, response);
    }
    
    /** Returns a short description of the servlet.
     */
    public String getServletInfo()
    {
        return "Short description";
    }
    // </editor-fold>
}