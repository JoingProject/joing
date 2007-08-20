/*
 * GetRoots.java
 * 
 * Created on 20-ago-2007, 12:07:50
 * 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package servlets.vfs;

import ejb.JoingServerException;
import ejb.vfs.ListManagerLocal;
import java.io.*;
import java.net.*;
import java.util.List;
import javax.ejb.EJB;

import javax.servlet.*;
import javax.servlet.http.*;
import servlets.JoingServerServletException;

/**
 *
 * @author fmorero
 */
public class GetRoots extends HttpServlet
{
    @EJB
    private ListManagerLocal listManagerBean;
    
    /** 
    * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
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
            String sSessionId = (String) reader.readObject();
            List<ejb.vfs.FileDescriptor> roots = null;
            
            // Process request
            roots = listManagerBean.getRoots( sSessionId );
            
            // Write to Client (desktop)
            writer.writeObject( roots );
            writer.flush();
        }
        catch( ClassNotFoundException exc )
        {
            log( "Error in Servlet: "+ getClass().getName(), exc );
            throw new JoingServerServletException( getClass(), exc );
        }
        catch( JoingServerException exc )
        {
            writer.writeObject( exc );
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
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        processRequest(request, response);
    } 

    /** 
    * Handles the HTTP <code>POST</code> method.
    * @param request servlet request
    * @param response servlet response
    */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        processRequest(request, response);
    }

    /** 
    * Returns a short description of the servlet.
    */
    public String getServletInfo() {
        return "Short description";
    }
    // </editor-fold>
}
