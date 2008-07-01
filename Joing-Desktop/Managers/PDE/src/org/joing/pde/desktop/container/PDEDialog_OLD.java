/*
 * Copyright (C) Francisco Morero Peyrona. All rights reserved.
 *
 * This software is published under the terms of Open Source
 * License version 1.1, a copy of which has been included with this
 * distribution in the License.txt file.
 */

package org.joing.pde.desktop.container;

import java.awt.Component;
import java.awt.Container;
import java.awt.KeyboardFocusManager;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.AccessController;
import java.security.PrivilegedAction;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JInternalFrame;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import org.joing.common.desktopAPI.DeskComponent;
import org.joing.common.desktopAPI.pane.DeskDialog;

/**
 * 
 * 
 * Note: this class uses two undocumented methods in java.awt.Container class to 
 * start/stop modal state.
 * 
 * @author Francisco Morero Peyrona
 */
public class PDEDialog_OLD extends PDEWindow implements DeskDialog
{
    private Component focusOwner;
    
    //------------------------------------------------------------------------//
    
    public PDEDialog_OLD()
    {           // resizable, closable, maximizable, minimizable
        super( "", true,      true,     false,       false );
        
        setDefaultCloseOperation( JInternalFrame.DISPOSE_ON_CLOSE );
        
        addInternalFrameListener( new InternalFrameAdapter()
        {
            public void internalFrameClosing( InternalFrameEvent e )
            {   // dispose() can't be called here because goes into infinite loop
                goModal( false );
            }
        } );
        
        // See: http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=6506360
        // PopupFactory.forceHeavyWeightPopupKey == "__force_heavy_weight_popup__"
        // But I have to use the String because the variable has package scope
        putClientProperty( new Object()
           {
               public boolean equals( Object anObject )
               {
                   return anObject instanceof StringBuffer && "__force_heavy_weight_popup__".equals( anObject.toString() );
               }
           }, true );
    }
    
    //------------------------------------------------------------------------//
    // DeskDialog Interface Implementation
    
    public boolean isModal()
    {
        return true;
    }
    
    //------------------------------------------------------------------------//
    // Container interface
    
    public void add( DeskComponent dc )
    {
        getContentPane().add( (Component) dc );
    }
    
    //------------------------------------------------------------------------//
    // Special methods used by PDE only (could be not needed in other implementations)
    
    public void startModal()
    {
        focusOwner = KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner();
        
        // Since all input will be blocked until this dialog is dismissed,
        // make sure its parent containers are visible first (this component
        // is tested below).  This is necessary for JApplets, because
        // because an applet normally isn't made visible until after its
        // start() method returns -- if this method is called from start(),
        // the applet will appear to hang while an invisible modal frame
        // waits for input.
	if( isVisible() && (! isShowing()) )
        {
            Container parent = getParent();
            
            while( parent != null )
            {
                if( parent.isVisible() == false )
                    parent.setVisible( true );
                
                parent = parent.getParent();
            }
        }
        
        goModal( true );
    }
    
    public void stopModal()
    {
        goModal( false );
        
        if( getParent() instanceof JInternalFrame )
        {
            try
            {
                ((JInternalFrame) getParent()).setSelected( true );
            }
            catch( java.beans.PropertyVetoException e )
            {
            }
        }
        
        if( focusOwner != null && focusOwner.isShowing() )
            focusOwner.requestFocus();
        
        try
        {
            setClosed( true );
        }
        catch( java.beans.PropertyVetoException exc )
        {
        }
        
        setVisible( false );
    }
    
    //------------------------------------------------------------------------//
    
    // Use reflection to get Container.startLWModal.start | stop
    private synchronized void goModal( boolean bModal )
    {
        try
        {
            String sPrefix = (bModal ? "start" : "stop" );
            
            Object obj = null;
                    
            obj = AccessController.doPrivileged( new ModalPrivilegedAction( Container.class, sPrefix + "LWModal" ) );
            
            if( obj != null )
                ((Method) obj).invoke( this, (Object[]) null );
        }
        catch( IllegalAccessException exc )
        {
            exc.printStackTrace();
        }
        catch( IllegalArgumentException exc )
        {
            exc.printStackTrace();
        }
        catch( InvocationTargetException exc )
        {
            exc.printStackTrace();
        }
    }
    
    //------------------------------------------------------------------------//
    // INNER CLASS
    //------------------------------------------------------------------------//
    
    private static final class ModalPrivilegedAction implements PrivilegedAction
    {
        private Class clazz;
        private String methodName;

        public ModalPrivilegedAction( Class clazz, String methodName )
        {
            this.clazz = clazz;
            this.methodName = methodName;
        }

        public Object run()
        {
            Method method = null;
            
            try
            {
                method = clazz.getDeclaredMethod( methodName, (Class[]) null );
            }
            catch( NoSuchMethodException ex )
            {
            }
            
            if( method != null )
            {
                method.setAccessible( true );
            }
            
            return method;
        }
    }
}