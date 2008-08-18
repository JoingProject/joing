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
package org.joing.pde.media;

import org.joing.pde.*;
import org.joing.common.desktopAPI.ColorSchema;
import java.awt.Color;

/**
 *
 * @author Francisco Morero Peyrona
 */
public class PDEColorSchema implements ColorSchema
{
    private static Color desktopBackground;
    private static Color taskbarBackground;
    
    private static Color deskLauncherTextForegroundUnSelected;
    private static Color deskLauncherTextForegroundSelected;
    private static Color deskLauncherTextBackground;    // PDELauncher label is not opaque when unselected
        
    private static Color UserNameBackground;
    private static Color UserNameForeground;
    
    private static PDEColorSchema instance = null;
    
    //------------------------------------------------------------------------//
    
    public static PDEColorSchema getInstance()
    {
        if( instance == null )
        {
            synchronized( PDERuntime.class )
            {
                if( instance == null )
                     instance = new PDEColorSchema();
            }
        }
        
        return instance;
    }
    
    public Color getDesktopBackground()
    {
        return desktopBackground;
    }
    
    public Color getTaskBarBackground()
    {
        return taskbarBackground;
    }
    
    // This is not used in PDE because in PDE the label is not opaque
    public Color getDeskLauncherBackgroundUnSelected()
    {
        return null;
    }
    
    // This is not used in PDE because in PDE the label is not opaque
    public Color getDeskLauncherBackgroundSelected()
    {
        return null;
    }
    
    public Color getDeskLauncherTextForegroundSelected()
    {
        return deskLauncherTextForegroundSelected;
    }
    
    public Color getDeskLauncherTextForegroundUnSelected()
    {
        return deskLauncherTextForegroundUnSelected;
    }
    
    public Color getDeskLauncherTextBackground()
    {
        return deskLauncherTextBackground;
    }
   
    public Color getUserNameBackground()
    {
        return UserNameBackground;
    }
    
    public Color getUserNameForeground()
    {
        return UserNameForeground;
    }
    
    //------------------------------------------------------------------------//
    // Singleton Design Pattern
    
    private PDEColorSchema()
    {
        // NEXT esto en lugar de hacerlo así habría que leerlo de la configuración
        //      de usuario y cargar el conjunto de colores preferidos, 
        //      p.ej.: "dessert" o "blue",...
        desktopBackground = new Color( 244, 250, 252 );
        taskbarBackground = new Color( 230, 230, 221 );
        
        deskLauncherTextBackground           = new Color( 128, 145, 153 );
        deskLauncherTextForegroundUnSelected = Color.black;
        deskLauncherTextForegroundSelected   = Color.white;
        
        UserNameBackground = Color.darkGray;
        UserNameForeground = Color.white;
    }
}