/*
 * ColorSchema.java
 *
 * Created on 14 de febrero de 2007, 20:13
 *
 * (c) 2006 - Francisco Morero Peyrona
 *
 * License: {license}
 */

package org.joing.pde.desktop.swing;

import java.awt.Color;

/**
 *
 * @author fmorero
 */
public class ColorSchema
{
    private static Color desktopBackground;
    private static Color taskbarBackground;
    
    private static Color desktopLauncherBackgroundUnSelected;
    private static Color desktopLauncherForegroundUnSelected;
    private static Color desktopLauncherBackgroundSelected;
    private static Color desktopLauncherForegroundSelected;
    
    private static Color UserNameBackground;
    private static Color UserNameForeground;
    
    
    private static ColorSchema instance = null;
    
    //------------------------------------------------------------------------//
    
    public static ColorSchema getInstance()
    {
        if( instance == null )
            instance = new ColorSchema();
        
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
    
    public Color getDesktopLauncherBackgroundUnSelected()
    {
        return desktopLauncherBackgroundUnSelected;
    }
    
    public Color getDesktopLauncherForegroundSelected()
    {
        return desktopLauncherForegroundSelected;
    }
    
    public Color getDesktopLauncherBackgroundSelected()
    {
        return desktopLauncherBackgroundSelected;
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
        //       de usuario y cargar el conjunto de colores preferidos, 
        //       p.ej.: "dessert" o 2blue",...
        desktopBackground = new Color( 22, 100, 160 );
        taskbarBackground = new Color( 244, 236, 221 );
    
        desktopLauncherBackgroundUnSelected = new Color(  60,  70, 100 );
        desktopLauncherForegroundUnSelected = Color.white;
        desktopLauncherBackgroundSelected   = new Color( 255, 235, 200 );
        desktopLauncherForegroundSelected   = Color.black;
        
        UserNameBackground = Color.darkGray;
        UserNameForeground = Color.white;
    }
}