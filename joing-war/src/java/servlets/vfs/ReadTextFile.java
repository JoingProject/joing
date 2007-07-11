/*
 * ReadTextFile.java
 *
 * Created on 26 de junio de 2007, 12:45
 */

package servlets.vfs;

import ejb.vfs.FileManagerRemote;
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
    private FileManagerRemote fileManagerBean;
    
    //------------------------------------------------------------------------//
    
    /** Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     */
    protected void processRequest( HttpServletRequest request, HttpServletResponse response )
              throws ServletException, IOException
    {
        String sSessionId = request.getParameter( "sessionId" );
        String sFileId    = request.getParameter( "fileId"    );
        String sFileName  = request.getParameter( "fileName"  );
        String sEncoding  = request.getParameter( "encoding"  );
        
        try
        {
            int nFileId = Integer.parseInt( sFileId );
            
            BufferedReader br = fileManagerBean.readTextFile( sSessionId, nFileId, sEncoding );
            
            if( br != null )
            {
                response.setContentType( "text/txt" );
                response.setContentLength( fNative.length() );
                response.setCharacterEncoding( sEncoding );
                response.setHeader( "Content-Disposition", "attachment; filename=" + sFileName );
                
                PrintWriter out = response.getWriter();
                
                // Stream to the requester.
                char[] cBuffer = new char[ 1024*4 ];
                int    length  = 0;
                
                while( (length = br.read( cBuffer )) != -1 )                    
                    out.write( cBuffer, 0, length );
                
                out.flush();
                out.close();
                br.close();
            }
        }
        catch( Exception exc )
        {
            getServletContext().log( "Exception occurred ", exc );
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