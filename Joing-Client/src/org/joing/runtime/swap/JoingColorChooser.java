/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.joing.runtime.swap;

import java.awt.Color;
import java.awt.Component;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;
import javax.swing.JColorChooser;
import org.joing.common.desktopAPI.DeskComponent;
import org.joing.common.desktopAPI.pane.DeskDialog;

/**
 *
 * @author Francisco Morero Peyrona
 */
public class JoingColorChooser extends JColorChooser implements DeskComponent
{    
    public static Color showDialog( Component parent, String title, Color initialColor ) 
           throws HeadlessException
    {
        // FIXME: ponerla symchronized
        
        JColorChooser pane = new JColorChooser( initialColor != null ? initialColor : Color.white );

        ColorTracker okListener = new ColorTracker( pane );
        DeskDialog   dialog     = createDialog4Joing( parent, title, true, pane, okListener, null );
        
        org.joing.jvmm.RuntimeFactory.getPlatform().getDesktopManager().getDesktop().getActiveWorkArea().add( dialog );

        return okListener.getColor();
    }
    
    public static Color showDialog( DeskComponent parent, String title, Color initialColor ) 
           throws HeadlessException
    {
        return showDialog( (Component) parent, title, initialColor );
    }
    
    //------------------------------------------------------------------------//
    
    private static DeskDialog createDialog4Joing( Component parent, String title, boolean modal, 
                                                 JColorChooser chooserPane, 
                                                 ActionListener okListener, ActionListener cancelListener  )
           throws HeadlessException 
    {        
        DeskDialog dialog = org.joing.jvmm.RuntimeFactory.getPlatform().getDesktopManager().getRuntime().createDialog();
                   dialog.setTitle( title );
                   dialog.add( (DeskComponent) chooserPane );
                   dialog.setLocationRelativeTo( (DeskComponent) parent );
                   ((Component) dialog).setComponentOrientation( chooserPane.getComponentOrientation() );
                   
        return dialog;
    }
    
    //------------------------------------------------------------------------//
    // INNER CLASS
    //------------------------------------------------------------------------//
    
    private static class ColorTracker implements ActionListener, Serializable
    {
        private JColorChooser chooser;
        private Color         color;

        public ColorTracker( JColorChooser c )
        {
            chooser = c;
        }

        public void actionPerformed( ActionEvent ae )
        {
            color = chooser.getColor();
        }

        public Color getColor()
        {
            return color;
        }
    }
}