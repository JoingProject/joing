/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.joing.common.desktopAPI;

/**
 *
 * @author fmorero
 */
public class DesktopManagerFactory
{
    private static DesktopManager dm = null;
    
    public static void init( DesktopManager deskmgr )
    {
        if( dm == null )
            dm = deskmgr;
        else
            throw new IllegalAccessError( "DesktopManager already exists: use the static method to access it." );
    }
    
    /**
     * Short name
     * @return
     */
    public static DesktopManager getDM()
    {
        return dm;
    }
    
    /**
     * Long name
     * @return
     */
    public static DesktopManager getDesktopManager()
    {
        return dm;
    }
}