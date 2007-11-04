/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.joing.pde.desktop.taskbar.waSwitcher;

import java.awt.Color;
import java.awt.event.MouseEvent;
import javax.swing.JPanel;
import javax.swing.event.MouseInputAdapter;
import org.joing.api.desktop.Desktop;
import org.joing.api.desktop.Selectable;
import org.joing.pde.desktop.workarea.PDEWorkArea;
import org.joing.pde.runtime.PDERuntime;

/**
 * Each one of the small boxes that represente WorkAreas spaces.
 * 
 * @author Francisco Morero Peyrona
 */
class Map extends JPanel implements Selectable
{
    // TODO: estos colores tienen que ser parte del sistema de colores
    private static Color clrSelected   = Color.pink;
    private static Color clrUnSelected = Color.gray;
    
    private PDEWorkArea workArea;
    
    //------------------------------------------------------------------------//
    
    Map( PDEWorkArea wa )
    {
        workArea = wa;
        
        setToolTipText( workArea.getName() );
        setOpaque( true );
        setBackground( clrUnSelected );
        setInheritsPopupMenu( true );
        
        addMouseListener( new MouseInputAdapter()
        {
            public void mouseClicked( MouseEvent me )
            {
                if( me.getButton() == MouseEvent.BUTTON1 )
                {
                    Desktop desktop = PDERuntime.getRuntime().getDesktopManager().getDesktop();
                            desktop.setActiveWorkArea( Map.this.workArea );
                }
            }
        } );
    }

    public boolean isSelected()
    {
        return getBackground().equals( clrSelected );   // Saving one variable (bSelected)
    }
    
    public void setSelected( boolean b )
    {
        if( b != isSelected() )
            setBackground( (b ? clrSelected  : clrUnSelected) );
    }
    
    public int hashCode()
    {
        int hash = 7;    // TODO: hacerlo bien
        return hash;
    }
    
    public boolean equals( Object obj )
    {
        return (obj instanceof Map) && (((Map) obj).workArea == workArea);
    }
    
    //------------------------------------------------------------------------//
    
    PDEWorkArea getTarget()
    {
        return workArea;
    }
}