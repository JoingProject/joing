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

package org.joing.kernel.swingtools;

import java.awt.Color;
import java.awt.Component;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;
import javax.swing.JColorChooser;
import org.joing.kernel.api.desktop.DeskComponent;
import org.joing.kernel.api.desktop.pane.DeskDialog;

/**
 *
 * @author Francisco Morero Peyrona
 */
// TODO: Decirle al cargador de clases que use esta cuando se pida: JColorChooser
public class JoingColorChooser extends JColorChooser implements DeskComponent
{    
    public static Color showDialog( Component parent, String title, Color initialColor ) 
           throws HeadlessException
    {
        // FIXME: ponerla symchronized
        
        JColorChooser pane = new JColorChooser( initialColor != null ? initialColor : Color.white );

        ColorTracker okListener = new ColorTracker( pane );
        DeskDialog   dialog     = createDialog4Joing( parent, title, true, pane, okListener, null );
        
        org.joing.kernel.jvmm.RuntimeFactory.getPlatform().getDesktopManager().getDesktop().getActiveWorkArea().add( dialog );

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
        DeskDialog dialog = org.joing.kernel.jvmm.RuntimeFactory.getPlatform().getDesktopManager().getRuntime().createDialog();
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