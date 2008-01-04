/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.joing.server.servlets;

import ejb.app.ApplicationManagerLocal;
import java.io.*;
import java.net.*;

import javax.ejb.EJB;
import javax.servlet.*;
import javax.servlet.http.*;
import org.joing.common.dto.app.Application;
import org.joing.common.exception.JoingServerAppException;
import org.joing.common.exception.JoingServerServletException;
import org.joing.common.pkt.app.ApplicationReply;
import org.joing.common.pkt.app.ApplicationRequest;

/**
 *
 * @author antoniovl
 */
public class ApplicationServlet extends HttpServlet {

    @EJB
    private ApplicationManagerLocal applicationManagerBean;

    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/octet-stream");

        ObjectInputStream reader = new ObjectInputStream(request.getInputStream());
        ObjectOutputStream writer = new ObjectOutputStream(response.getOutputStream());

        ApplicationReply reply = new ApplicationReply();
        ApplicationRequest req = null;

        try {
            
            Object o = reader.readObject();
            
            if (o instanceof ApplicationRequest) {
                reply = process((ApplicationRequest)o);
            }
            
        } catch (ClassNotFoundException cnfe) {
            reply.setOk(false);
            reply.setReply(new JoingServerServletException(getClass(), cnfe.getMessage()));
        }
        
        writer.writeObject(reply);
        writer.flush();

        reader.close();
        writer.close();

    }

    protected ApplicationReply process(ApplicationRequest request) {

        ApplicationReply reply = new ApplicationReply();

        String sessionId = request.getSessionId();
        String name = request.getName();

        try {

            Application app =
                    this.applicationManagerBean.getApplicationByName(sessionId, name);
            reply.setOk(true);
            reply.setReply(app);

        } catch (JoingServerAppException jsae) {
            reply.setOk(false);
            reply.setReply(new JoingServerServletException(getClass(), jsae.getMessage()));
        }

        return reply;
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /** 
     * Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /** 
     * Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /** 
     * Returns a short description of the servlet.
     */
    public String getServletInfo() {
        return "Short description";
    }
    // </editor-fold>
}
