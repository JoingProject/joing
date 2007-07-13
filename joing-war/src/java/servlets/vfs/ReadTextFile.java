/*
 * ReadTextFile.java
 *
 * Created on 26 de junio de 2007, 12:45
 */

package servlets.vfs;

import ejb.vfs.FileManagerLocal;
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
        ObjectInputStream reader = new ObjectInputStream( request.getInputStream() );
        BufferedReader    in     = null;
        PrintWriter       out    = null;
        
        try
        {
            // Read from client (desktop)
            String       sSessionId = (String)  reader.readObject();
            int          nFileId    = (Integer) reader.readObject();
            String       sEncoding  = (String)  reader.readObject();
            java.io.File fNative    = fileManagerBean.getNativeFile( sSessionId, nFileId, false );
            
            if( fNative != null && fNative.exists() )
            {
                response.setContentType( "text/txt" );
                response.setContentLength( (int) fNative.length() );
                response.setCharacterEncoding( sEncoding );
                // TODO: ¿es esto necesario? -> response.setHeader( "Content-Disposition", "attachment; filename=" + sFileName );
                
                FileInputStream   fis = new FileInputStream( fNative );
                InputStreamReader isw = new InputStreamReader( fis, sEncoding );
                
                in  = new BufferedReader( isw );
                out = response.getWriter();
                
                // Stream to the requester.
                char[] cBuffer = new char[ 1024*4 ];
                int    length  = 0;
                
                while( (length = in.read( cBuffer )) != -1 )                    
                    out.write( cBuffer, 0, length );
                
                out.flush();
            }
        }
        catch( ClassNotFoundException exc )
        {
            log( "Error in Servlet: "+ getClass().getName(), exc );
        }
        finally
        {
            if( reader != null )
                try{ reader.close(); } catch( IOException exc ) { }
            
            if( in != null )
                try{ in.close(); } catch( IOException exc ) { }
            
            out.close();
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