/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.joing.pde.swing;

import java.awt.LayoutManager;
import javax.swing.JPanel;
import org.joing.common.desktopAPI.DeskComponent;

/**
 *
 * @author Francisco Morero Peyrona
 */
public class PDEDeskComponent extends JPanel implements DeskComponent
{
    public PDEDeskComponent()
    {    
    }
    
    public PDEDeskComponent( LayoutManager lm )
    {
        super( lm );
    }
}
