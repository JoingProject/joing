/*
 * MemoryPanel.java
 *
 * Created on 9 de septiembre de 2007, 2:17
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.joing.applauncher.gui.memory;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JPanel;

/**
 *
 * @author Francisco Morero Peyrona
 */
public class MemoryPanel extends JPanel
{
    private MemoryMonitor memon;
    
    /** Creates a new instance of MemoryPanel */
    public MemoryPanel()
    {
        super( new BorderLayout() );
        
        memon = new MemoryMonitor();
        add( memon, BorderLayout.CENTER );
        
        JButton btnGC = new JButton( "Garbage Collector" );
                btnGC.addActionListener( new ActionListener()
                {
                    public void actionPerformed(ActionEvent e)
                    {
                        System.gc();
                    }
                } );
        
        JPanel pnlButtons = new JPanel( new FlowLayout( FlowLayout.TRAILING ) );
               pnlButtons.add( btnGC );
               
        add( pnlButtons, BorderLayout.SOUTH );
    }
}