/*
 * GetChilds.java
 *
 * Created on 1 de julio de 2007, 18:03
 */

package org.joing.server.servlets.vfs;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import org.joing.common.exception.JoingServerServletException;
import ejb.vfs.ListManagerLocal;
import java.util.List;
import javax.ejb.EJB;

import javax.servlet.*;
import javax.servlet.http.*;
import org.joing.common.dto.vfs.FileDescriptor;
import org.joing.common.exception.JoingServerException;

/**
 *
 * @author fmorero
 * @version
 */
public class GetChilds extends HttpServlet
{
    @EJB
    private ListManagerLocal listManagerBean;
    
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
            String               sSessionId = (String) reader.readObject();
            Object               o2ndParam  =          reader.readObject();
            List<FileDescriptor> files      = null;
            
            // Process request
            if( o2ndParam instanceof Integer )
            {
                Integer nFileDirId = (Integer) o2ndParam;
                
                files = listManagerBean.getChilds( sSessionId, nFileDirId );
            }
            else
            {
                String sDirPath = (String) o2ndParam;
                
                files = listManagerBean.getChilds( sSessionId, sDirPath );
            }
            
            // Write to Client (desktop)
            writer.writeObject( files );
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
