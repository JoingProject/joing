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
