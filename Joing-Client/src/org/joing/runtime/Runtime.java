/*
 * Runtime.java
 *
 * Created on 26 de junio de 2007, 10:35
 *
 * Copyright (C) 2007 Francisco Morero Peyrona
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or (at
 * your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 */

package org.joing.runtime;

import javax.swing.JOptionPane;

/**
 * The Runtime class.
 * <p>
 * Contains miscellaneous methods used intensively and extensively through all 
 * WebPC.
 * 
 * @author Francisco Morero Peyrona
 */
public final class Runtime
{
    // Created here to avoid synchronization problems.
    // See: http://javaboutique.internet.com/tutorials/singleton/index2.html
    private static Runtime instance = new Runtime();
    
    private String  sServerBaseURL        = null;
    private boolean bAutoHandleExceptions = true;
    
    //-------------------------------------------------------------------------//
    
    /**
     * Get class singleton
     * @return The sole instance of this class
     */
    public static Runtime getRuntime()
    {        
        return instance;
    }
    
    private Runtime()
    {
    }
    
    //-------------------------------------------------------------------------//
    
    public String getServerBaseURL()
    {
        if( sServerBaseURL == null )
            sServerBaseURL = "http://127.0.0.1:8080/joing-war/";  // TODO: averiguarlo apropiadamente
        
        return sServerBaseURL;
    }

    public boolean isAutoHandlingExceptions()
    {
        return bAutoHandleExceptions;
    }

    public void setAutoHandlingExceptions( boolean bAutoHandleExceptions )
    {
        this.bAutoHandleExceptions = bAutoHandleExceptions;
    }
    
    //-------------------------------------------------------------------------//
    // Show exception and messages
    //-------------------------------------------------------------------------//
    /**
     * Shows an exception in a dialog.
     * 
     * @param exc Exception to be shown
     */
    public void showException( Throwable exc )
    {
        showException( "Error", exc );
    }
    
    /**
     * Shows an exception in a dialog.
     * 
     * @param sTitle Title for the dialog
     * @param exc    Exception to be shown
     */
    public void showException( String sTitle, Throwable exc )
    {
        exc.printStackTrace();

        /*  TODO: hacer q JShowException herede de JDesktopDialog y q actue en consecuencia:
         *  Nota: utilizar getLocalizedMessage()
        JShowException dialog = new JShowException( sTitle, exc );
                       dialog.setLocationRelativeTo( getDesktop() );
                       dialog.setVisible( true );*/
    }
    
    /**
     * Shows a message in a dialog with title "Information" and an OK button.
     * 
     * @param sMessage Message to be shown
     */
    public void showMessage( String sMessage )
    {
        showMessage( "Information", sMessage );
    }

    /**
     * Shows a message in a dialog and an OK button.
     * 
     * @param sTitle   Dialog window title (if <code>null</code> or empty string passed, title will be empty)
     * @param sMessage Message to be shown
     */
    public void showMessage( String sTitle, String sMessage )
    {
        /*Cursor cursor = getCursor();
        
        setCursor( Cursor.DEFAULT_CURSOR );

        JDesktopDialog dd = new JDesktopDialog( sTitle, new JOptionPane( sMessage, JOptionPane.INFORMATION_MESSAGE ) );
                       dd.pack();
                       dd.setVisible( true );
        
        setCursor( cursor.getType() );*/
    }

    /**
     * Shows a confimation modal dialog.
     * 
     * @param sTitle    Dialog window title (if <code>null</code> or empty string passed, title will be empty)
     * @param sMessage  Message to ask confirmation about
     * @return <code>true</code> if 'OK' button pressed, otherwise ('CANCEL' button or close dialog) return <code>false</code>
     */
    public boolean confirmDialog( String sTitle, String sMessage )
    {
        return commonDialog( sTitle, sMessage, JOptionPane.OK_CANCEL_OPTION );
    }

    /**
     * Shows a confimation modal dialog.
     * 
     * @param sTitle    Dialog window title (if <code>null</code> or empty string passed, title will be empty)
     * @param sMessage  Message to ask confirmation about
     * @return <code>true</code> if 'YES' button pressed, otherwise ('NO' button or close dialog) return <code>false</code>
     */
    public boolean yesOrNoDialog( String sTitle, String sMessage )
    {
        return commonDialog( sTitle, sMessage, JOptionPane.YES_NO_OPTION );
    }
    
    //------------------------------------------------------------------------//
    
    // This private is used by confirmDialog() and yesOrNoDialog()
    private boolean commonDialog( String sTitle, String sMessage, int nOptions )
    {
        /*Cursor cursor = getCursor();
        
        setCursor( Cursor.DEFAULT_CURSOR );

        JDesktopDialog dd = new JDesktopDialog( sTitle, new JOptionPane( sMessage, nOptions, JOptionPane.YES_NO_OPTION ) );
                       dd.pack();
                       dd.setVisible( true );

        setCursor( cursor.getType() );
        
        int nOption = (Integer) dd.getValue();
        
        return ((nOption == JOptionPane.OK_OPTION) || (nOption == JOptionPane.YES_OPTION));*/
        return true;
    }
}