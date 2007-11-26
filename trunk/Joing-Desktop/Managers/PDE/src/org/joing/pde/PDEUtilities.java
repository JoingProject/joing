/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.joing.pde;

import java.awt.Component;
import java.util.List;
import org.joing.common.desktopAPI.DesktopManagerFactory;
import org.joing.common.desktopAPI.workarea.WorkArea;
import org.joing.pde.desktop.workarea.PDEWorkArea;

/**
 *
 * @author fmorero
 */
public class PDEUtilities
{
    /**
     * Get all objects in dekstop that match passed class,
     * or an empty <code>Vector</code> one if there is no one selected.
     * <p>
     * To select objects of all classes, simply pass <code>Component</code>
     */
    public List<Object> getOfType( Component container, Class clazz )
    {
        /* TODO: hcaerlo y que sea recursivo
        ArrayList<Object> list  = new ArrayList<Object>();                
        Component[]       aComp = getComponents();
        
        for( int n = 0; n < aComp.length; n++ )
        {
            if( clazz.isInstance( aComp[n] ) )
                list.add( aComp[n] );
        }
        
        return list;
        */
        return null;
    }
    
    //------------------------------------------------------------------------//
    
    public static WorkArea findWorkAreaFor( Component comp )
    {
        List<WorkArea> lstWA = DesktopManagerFactory.getDM().getDesktop().getWorkAreas();
        
        for( WorkArea wa : lstWA )
        {
            Component[] ac  = ((PDEWorkArea) wa).getComponents();
            
            for( int n = 0; n < ac.length; n++ )
            {
                if( ac[n] == comp )
                    return wa;
            }
        }
        
        return null;
    }
}
