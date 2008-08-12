/*
 * GetAvailableLocales.java
 *
 * Created on 30 de junio de 2007, 11:48
 */

package org.joing.server.servlets.user;

import ejb.user.UserManagerLocal;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;

import javax.servlet.*;
import javax.servlet.http.*;
import org.joing.common.dto.user.Local;
import org.joing.common.exception.JoingServerException;
import org.joing.common.exception.JoingServerServletException;

/**
 *
 * @author Francisco Morero Peyrona
 * @version
 */
public class GetAvailableLocales extends HttpServlet
{
    @EJB
    private UserManagerLocal userManagerBean;
    
    //------------------------------------------------------------------------//
    
    /** Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
              throws ServletException, IOException
    {
        response.setContentType( "application/octet-stream" );
        
        ObjectInputStream  reader = new ObjectInputStream(  request.getInputStream()   );
        ObjectOutputStream writer = new ObjectOutputStream( response.getOutputStream() );
        
        try
        {
            // Read from client (desktop)
            String sSessionId = reader.readObject().toString();
            
            // Process request
            List<Local> locals = userManagerBean.getAvailableLocales( sSessionId );
            
            // Convert into interface type
            List<org.joing.common.dto.user.Local> listToSend = new ArrayList<org.joing.common.dto.user.Local>();
            
            for( Local l : locals )
                listToSend.add( l );
            
            // Write to Client (desktop)
            writer.writeObject( listToSend );
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
                try{ reader.close(); }catch( IOException exc ) { }
                
            if( writer != null )
                try{ writer.close(); }catch( IOException exc ) { }
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
