/*
 * Runtime.java
 *
 * Created on 14 de febrero de 2007, 14:22
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.joing;

import java.awt.Cursor;
import java.net.URL;
import javax.swing.ImageIcon;

/**
 *
 * @author fmorero
 */
public interface Runtime
{
    void      showException( Throwable exc, String sTitle );
    void      showMessage( String sMessage );
    void      showMessage( String sTitle, String sMessage );
    boolean   confirmDialog( String sTitle, String sMessage );
    boolean   yesOrNoDialog( String sTitle, String sMessage );
    ImageIcon getIcon( Object invokerClass, String s );
    ImageIcon getIcon( Object invokerClass, String s, int nWidth, int nHeight );
    void      play( URL urlSound );
    Cursor    getCursor();
    void      setCursor( int nCursorType );
}