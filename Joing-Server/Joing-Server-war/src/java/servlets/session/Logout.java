/*
 * Logout.java
 *
 * Created on 30 de junio de 2007, 10:12
 */

package servlets.session;

import ejb.session.SessionManagerLocal;
import java.io.*;
import java.net.*;
import javax.ejb.EJB;

import javax.servlet.*;
import javax.servlet.http.*;

/**
 *
 * @author fmorero
 * @version
 */
public class Logout extends HttpServlet
{
    @EJB
    private SessionManagerLocal sessionManagerBean;
    
    //------------------------------------------------------------------------//
    
    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     *
     * @param request servlet request
     * @param response servlet response
     */
    protected void processRequest( HttpServletRequest request, HttpServletResponse response )
              throws ServletException, IOException
    {
        ObjectInputStream reader  = new ObjectInputStream( request.getInputStream() );
        
        try
        {
            String sSessionId = (String) reader.readObject();
         
            sessionManagerBean.logout( sSessionId );   
        }
        catch( ClassNotFoundException exc )
        {
            // As logout(...) does not return this exception do not need to be reported (throw)
            log( "Error in Servlet: "+ getClass().getName(), exc );
        }
        finally
        {
            if( reader != null )
                try{ reader.close(); }catch( IOException exc ) { }
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
