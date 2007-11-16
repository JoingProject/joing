/*
 * AppEnvironment.java
 *
 * Created on 18 de octubre de 2007, 22:37
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.joing.common.dto.app;

/**
 *
 * @author Francisco Morero Peyrona
 */
public enum AppEnvironment
{
    JAVA_ALL   ( 0 ),
    
    JAVA_CLDC  ( 1 ),
    JAVA_CDC   ( 2 ),
    JAVA_SE    ( 3 ),
    JAVA_RUBY  ( 4 ),
    JAVA_GROOVY( 5 ),
    JAVA_PYTHON( 6 );
    
    //------------------------------------------------------------------------//
    
    private int nIndex;
    
    private AppEnvironment( int nIndex )
    { 
        this.nIndex = nIndex;
    }
    
    public int getIndex()
    { 
        return this.nIndex;
    }
}