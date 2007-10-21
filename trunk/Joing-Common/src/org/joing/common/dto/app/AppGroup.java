/*
 * AppGroup.java
 *
 * Created on 18 de octubre de 2007, 22:21
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.joing.common.dto.app;

/**
 *
 * @author Francisco Morero Peyrona
 */
public class AppGroup
{
    public final static int ALL         =  0;
    
    public final static int ACCESSORIES =  1;
    public final static int EDUCATION   =  2;
    public final static int GAMES       =  3;
    public final static int GRAPHICS    =  4;
    public final static int INTERNET    =  5;
    public final static int MULTIMEDIA  =  6;
    public final static int OFFICE      =  7;
    public final static int PROGRAMMING =  8;
    public final static int SYSTEM      =  9;
    public final static int OTHER       = 10;
    
    public final static int DESKTOP     = 99;   // Desktops: PDE, etc.
    
    public final static int[] ALL_EXCEPT_DESKTOPS = { 1,2,3,4,5,6,7,8,9,10 };
}