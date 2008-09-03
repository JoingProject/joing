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

package org.joing.common.dto.app;

/**
 * Used by AppGroup class to identify groups.
 * <p>
 * Note: needed for i18n.
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