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
package org.joing.server.jsf;

import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.ValueExpression;
import javax.faces.application.Application;
import javax.faces.context.FacesContext;

/**
 *
 * @author Fernando
 */
public abstract class ManagedBean {
    
    /**
     * Returns the current context instance;
     * 
     * @throws IllegalStateException if not invoked from within a JSF context
     */
    protected FacesContext context() {
	FacesContext ctx = FacesContext.getCurrentInstance();
	
	if(ctx == null) {
	    throw new IllegalStateException(
		"This method must be invoked within the JSF context");
	}
	
	return ctx;
    }
    
    /**
     * Evaluates the EL expression
     * 
     * @param expression EL expression to evaluate
     * 
     * @return Value of the expression
     * 
     */
    protected <T>T el(String expression) {
	FacesContext ctx = context();
	
	Application app = ctx.getApplication();
	
	ELContext elctx = ctx.getELContext();
	
	ExpressionFactory ef = app.getExpressionFactory();
	
	String ex = String.format("#{%s}", expression);
	
	ValueExpression ve = ef.createValueExpression(elctx, ex, Object.class);
	
	return (T)ve.getValue(elctx);
    }
    
}
