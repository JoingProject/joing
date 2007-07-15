/*
 * ReadTextFile.java
 *
 * Created on 26 de junio de 2007, 12:45
 */

package servlets.vfs;

import ejb.vfs.FileManagerLocal;
import ejb.vfs.FileText;
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
public class ReadTextFile extends HttpServlet
{
    @EJB
    private FileManagerLocal fileManagerBean;
    
    //------------------------------------------------------------------------//
    
    /** Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     */
    protected void processRequest( HttpServletRequest request, HttpServletResponse response )
              throws ServletException, IOException
    {
        ObjectInputStream  reader = new ObjectInputStream(  request.getInputStream()   );
        ObjectOutputStream writer = new ObjectOutputStream( response.getOutputStream() );
        
        try
        {
            // Read from client (desktop)
            String   sSessionId = (String)  reader.readObject();
            int      nFileId    = (Integer) reader.readObject();
            String   sEncoding  = (String)  reader.readObject();
            FileText fileText   = fileManagerBean.readTextFile( sSessionId, nFileId, sEncoding );
     
            if( fileText != null )
            {
                response.setContentType( "text/txt" );
                response.setContentLength( (int) fileText.getSize() );
                response.setCharacterEncoding( sEncoding );
                
                writer.writeObject( fileText );
                writer.flush();
            }
            else
            {
                writer.writeObject( null );    // TODO: mirar qué hacer en estos casos
            }
        }
        catch( ClassNotFoundException exc )
        {
            log( "Error in Servlet: "+ getClass().getName(), exc );
            // TODO: habría que enviar un error de vuelta, porque el cliente está a la espera
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