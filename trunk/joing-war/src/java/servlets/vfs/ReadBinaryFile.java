/*
 * ReadBinaryFile.java
 *
 * Created on 26 de junio de 2007, 15:47
 */

package servlets.vfs;

import java.io.*;
import java.net.*;

import javax.servlet.*;
import javax.servlet.http.*;

/**
 *
 * @author fmorero
 * @version
 */
public class ReadBinaryFile extends HttpServlet
{
    private final static int nBUFFER_SIZE = 1024 * 4;
    
    /** Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException
    {
        
        /* TODO: hacerlo
        response.setHeader( "Content-Disposition", "attachment; filename=" + sFileName );
                
                ServletOutputStream sos = response.getOutputStream();
                
                // Stream to the requester.
                byte[] bBuffer = new byte[ 1024*4 ];
                int    length  = 0;
                
                while( (length = br.read( bBuffer )) != -1 )                    
                    sos.write( bBuffer, 0, length );
                
                sos.flush();
                sos.close();
                br.close();
        
        response.setContentType("text/html;charset=UTF-8");*/
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