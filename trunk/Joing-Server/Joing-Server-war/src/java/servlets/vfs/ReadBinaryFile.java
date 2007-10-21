/*
 * ReadBinaryFile.java
 *
 * Created on 26 de junio de 2007, 15:47
 */

package servlets.vfs;

import servlets.JoingServerServletException;
import ejb.vfs.FileManagerLocal;
import java.io.*;
import javax.ejb.EJB;

import javax.servlet.*;
import javax.servlet.http.*;
import org.joing.common.dto.vfs.FileBinary;
import org.joing.common.exception.JoingServerException;

/**
 *
 * @author fmorero
 * @version
 */
public class ReadBinaryFile extends HttpServlet
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
            String     sSessionId = (String)  reader.readObject();
            int        nFileId    = (Integer) reader.readObject();
            FileBinary fileBinary = fileManagerBean.readBinaryFile( sSessionId, nFileId );
     
            if( fileBinary != null )
            {
                response.setHeader( "Content-Disposition", 
                                    "attachment; filename=" + fileBinary.getName() );
                response.setContentLength( (int) fileBinary.getSize() );
                writer.writeObject( fileBinary );
                writer.flush();
            }
            else
            {
                writer.writeObject( null );    // TODO: mirar qué hacer en estos casos
            }
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