/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.joing.pde.joingswingtools;

import java.awt.LayoutManager;
import javax.swing.JPanel;
import org.joing.common.desktopAPI.DeskComponent;

/**
 * A convenience class to be used instead of javax-swing.JPanel when a method in
 * Join'g expects to recieve an instance of org.joing.common.desktopAPI.DeskComponent.
 * 
 * @author Francisco Morero Peyrona
 */
public class JoingPanel extends JPanel implements DeskComponent
{
    public JoingPanel()
    {
        super();
    }
    
    public JoingPanel( boolean isDoubleBuffered )
    {
        super( isDoubleBuffered );
    }

    public JoingPanel( LayoutManager layout )
    {
        super( layout );
    }

    public JoingPanel( LayoutManager layout, boolean isDoubleBuffered )
    {
        super( layout, isDoubleBuffered );
    }
}