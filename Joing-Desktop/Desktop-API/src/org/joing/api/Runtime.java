/*
 * Runtime.java
 *
 * Created on 14 de febrero de 2007, 14:22
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.joing.api;

import java.awt.Cursor;
import java.net.URL;
import javax.swing.ImageIcon;
import org.joing.api.desktop.Desktop;

/**
 *
 * @author fmorero
 */
public interface Runtime
{
    public void      showException( Throwable exc, String sTitle );
    public void      showMessage( String sMessage );
    public void      showMessage( String sTitle, String sMessage );
    public boolean   confirmDialog( String sTitle, String sMessage );
    public boolean   yesOrNoDialog( String sTitle, String sMessage );
    public ImageIcon getIcon( Object invokerClass, String s );
    public ImageIcon getIcon( Object invokerClass, String s, int nWidth, int nHeight );
    public void      play( URL urlSound );
    public Cursor    getCursor();
    public void      setCursor( int nCursorType );
}