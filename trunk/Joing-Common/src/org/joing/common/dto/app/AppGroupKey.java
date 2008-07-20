/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.joing.common.dto.app;

/**
 *
 * @author Francisco Morero Peyrona
 */
public enum AppGroupKey
{
    UNKNOWN    (-1 ),
    ALL        ( 0 ),

    ACCESSORIES( 1 ),
    EDUCATION  ( 2 ),
    GAMES      ( 3 ),
    GRAPHICS   ( 4 ),
    INTERNET   ( 5 ),
    MULTIMEDIA ( 6 ),
    OFFICE     ( 7 ),
    PROGRAMMING( 8 ),
    SYSTEM     ( 9 ),
    OTHER      ( 10),

    DESKTOP    ( 99);   // Desktops: PDE, etc.
    
    //------------------------------------------------------------------------//
    
    private int nIndex;
    
    private AppGroupKey( int nIndex )
    { 
        this.nIndex = nIndex;
    }
    
    public int getIndex()
    { 
        return this.nIndex;
    }
    
    public static AppGroupKey inverse( int nIndex )
    {
        AppGroupKey nRet = UNKNOWN;
        
        switch( nIndex )
        {
            case  0: nRet = ALL;         break;
            case  1: nRet = ACCESSORIES; break;
            case  2: nRet = EDUCATION;   break;
            case  3: nRet = GAMES;       break;
            case  4: nRet = GRAPHICS;    break;
            case  5: nRet = INTERNET;    break;
            case  6: nRet = MULTIMEDIA;  break;
            case  7: nRet = OFFICE;      break;
            case  8: nRet = PROGRAMMING; break;
            case  9: nRet = SYSTEM;      break;
            case 10: nRet = OTHER;       break;
            case 99: nRet = DESKTOP;     break;
        }
        
        return nRet;
    }
}