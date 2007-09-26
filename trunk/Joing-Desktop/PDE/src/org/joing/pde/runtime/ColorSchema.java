/*
 * ColorSchema.java
 *
 * Created on 14 de febrero de 2007, 20:13
 *
 * (c) 2006 - Francisco Morero Peyrona
 *
 * License: {license}
 */

package org.joing.pde.runtime;

import java.awt.Color;

/**
 *
 * @author fmorero
 */
public class ColorSchema
{
    private static Color desktopBackground;
    private static Color taskbarBackground;
    
    private static Color desktopLauncherForegroundUnSelected;
    private static Color desktopLauncherForegroundSelected;
    private static Color desktopLauncherBackground;    // PDELauncher label is not opaque when unselected
        
    private static Color UserNameBackground;
    private static Color UserNameForeground;
    
    private static ColorSchema instance = null;
    
    //------------------------------------------------------------------------//
    
    public static ColorSchema getInstance()
    {
        if( instance == null )
        {
            synchronized( PDERuntime.class )
            {
                if( instance == null )
                     instance = new ColorSchema();
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
    
    // This method does not exists because in PDE, the unselected label is not opaque
    // public Color getDesktopLauncherBackgroundUnSelected()
    
    public Color getDesktopLauncherForegroundSelected()
    {
        return desktopLauncherForegroundSelected;
    }
    
    public Color getDesktopLauncherBackground()
    {
        return desktopLauncherBackground;
    }
   
    public Color getDesktopLauncherForegroundUnSelected()
    {
        return desktopLauncherForegroundUnSelected;
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
    
    private ColorSchema()
    {
        // @TODO esto en lugar de hacerlo así habría que leerlo de la configuración
        //       de usuario y cargar el conjunto de colores peferidos, 
        //       p.ej.: "dessert" o 2blue",...
        desktopBackground = new Color( 255, 255, 220 );
        taskbarBackground = new Color( 230, 230, 221 );
        
        desktopLauncherForegroundUnSelected = Color.black;
        desktopLauncherBackground           = new Color(  28,  55,  83 );
        desktopLauncherForegroundSelected   = Color.white;
        
        UserNameBackground = Color.darkGray;
        UserNameForeground = Color.white;
    }
}