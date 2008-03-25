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
import javax.swing.JComponent;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.PopupFactory;
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
public class PDEDialog extends PDEWindow implements DeskDialog
{
    private Component focusOwner;
    
    //------------------------------------------------------------------------//
    
    public PDEDialog()
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
        // FIXME: Al menos con el JoingFileChooser no funciona el HeavyWeightPopup
        //        Se puede mirar su uso en JOptionPane
        // PopupFactory.forceHeavyWeightPopupKey == "__force_heavy_weight_popup__"
        // But I have to use the String because the variuable has package scope
        ((JComponent) dc).putClientProperty( "__force_heavy_weight_popup__", Boolean.TRUE );
        
        getContentPane().add( (Component) dc );
    }
    
    //------------------------------------------------------------------------//
    // Closeable interface
    
    public void close()
    {
        setVisible( false );
        dispose(); // dispose() can't be at the InternalFrameAdapter because goes into infinite loop
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
    }
    
    //------------------------------------------------------------------------//
    
    // Use reflection to get Container.startLWModal.
    private synchronized void goModal( boolean bModal )
    {
        try
        {
            String sPrefix = (bModal ? "start" : "stop" );
            
            Object obj = AccessController.doPrivileged( new ModalPrivilegedAction( Container.class, sPrefix + "LWModal" ) );
            
            if( obj != null )
                ((Method) obj).invoke( this, (Object[]) null );
        }
        catch( IllegalAccessException ex )
        {
        }
        catch( IllegalArgumentException ex )
        {
        }
        catch( InvocationTargetException ex )
        {
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