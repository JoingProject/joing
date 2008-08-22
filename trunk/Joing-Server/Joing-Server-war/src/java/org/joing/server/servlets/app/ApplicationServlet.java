/*
 * Copyright (C) 2007, 2008 Join'g Team Members. All Rights Reserved.
 * Join'g Team Members are listed at project's home page. By the time of 
 * writting this at: https://joing.dev.java.net/servlets/ProjectMemberList.
 *
 * This file is part of Join'g project: www.joing.org
 *
 * GNU Classpath is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the free
 * Software Foundation; either version 3, or (at your option) any later version.
 * 
 * GNU Classpath is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * GNU Classpath; see the file COPYING.  If not, write to the Free Software 
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */
package org.joing.server.servlets.app;

import org.joing.server.ejb.app.ApplicationManagerLocal;
import java.io.*;

import java.util.List;
import javax.ejb.EJB;
import javax.servlet.*;
import javax.servlet.http.*;
import org.joing.common.dto.app.AppDescriptor;
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

        ObjectInputStream reader  = new ObjectInputStream(request.getInputStream());
        ObjectOutputStream writer = new ObjectOutputStream(response.getOutputStream());

        ApplicationReply reply = new ApplicationReply();

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
        int code = request.getCode();
        Application app = null;
        
        try {
            switch (code) {
                case ApplicationRequest.APP_BY_NAME:
                    app =applicationManagerBean.getApplicationByName(sessionId, name);
                    reply.setReply(app);
                    reply.setOk(true);
                    break;
                case ApplicationRequest.AVAILABLE_DESKTOPS:
                    List<AppDescriptor> appList = applicationManagerBean.getAvailableDesktops();
                    reply.setReply(appList);
                    reply.setOk(true);
                    break;
                default:
                    reply.setOk(false);
                    reply.setReply(new JoingServerServletException(getClass(), "Unsupported Op Code"));
                    break;
            }

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