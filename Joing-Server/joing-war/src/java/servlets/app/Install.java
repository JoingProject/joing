/*
 * Install.java
 *
 * Created on 30 de junio de 2007, 12:33
 */

package servlets.app;

import ejb.JoingServerException;
import ejb.app.AppDescriptor;
import ejb.app.ApplicationManagerLocal;
import ejb.vfs.JoingServerVFSException;
import java.io.*;
import java.net.*;
import javax.ejb.EJB;

import javax.servlet.*;
import javax.servlet.http.*;
import servlets.JoingServerServletException;

/**
 *
 * @author fmorero
 * @version
 */
public class Install extends HttpServlet
{
    @EJB()
    private ApplicationManagerLocal applicationManagerBean;
    
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
            String      sSessionId  = (String)      reader.readObject();
            AppDescriptor application = (AppDescriptor) reader.readObject();
            
            // Process request
            boolean bSuccess = applicationManagerBean.install( sSessionId, application );
            
            // Write to Client (desktop)
            writer.writeObject( bSuccess );
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
