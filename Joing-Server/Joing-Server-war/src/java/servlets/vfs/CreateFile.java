/*
 * CreateFile.java
 *
 * Created on 1 de julio de 2007, 19:57
 */

package servlets.vfs;

import ejb.JoingServerException;
import servlets.JoingServerServletException;
import ejb.vfs.FileManagerLocal;
import ejb.vfs.JoingServerVFSException;
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
public class CreateFile extends HttpServlet
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
        response.setContentType( "application/octet-stream" );
        
        ObjectInputStream  reader = new ObjectInputStream(  request.getInputStream()   );
        ObjectOutputStream writer = new ObjectOutputStream( response.getOutputStream() );
        
        try
        {
            // Read from client (desktop)
            String       sSessionId = (String) reader.readObject();
            String       sPath      = (String) reader.readObject();
            String       sFileName  = (String) reader.readObject();
            ejb.vfs.FileDescriptor file = null;
            
            // Process request
            file = fileManagerBean.createFile( sSessionId, sPath, sFileName );
            
            // Write to Client (desktop)
            writer.writeObject( file );
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
