/*
 * GetUser.java
 *
 * Created on 30 de junio de 2007, 10:08
 */

package servlets.user;

import ejb.user.UserManagerLocal;
import java.io.*;
import javax.ejb.EJB;

import javax.servlet.*;
import javax.servlet.http.*;
import org.joing.common.dto.user.User;
import org.joing.common.exception.JoingServerException;
import org.joing.common.exception.JoingServerServletException;

/**
 *
 * @author fmorero
 * @version
 */
public class GetUser extends HttpServlet
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
            String sSessionId = (String) reader.readObject();
            
            // Process request
            User user = userManagerBean.getUser( sSessionId );
            
            // Write to Client (desktop)
            writer.writeObject( (org.joing.common.dto.user.User) user );
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