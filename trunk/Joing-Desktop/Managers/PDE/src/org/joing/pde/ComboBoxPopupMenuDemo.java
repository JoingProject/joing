/*
 * Copyright (C) 2007, 2008 Join'g Team Members.  All Rights Reserved.
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
package org.joing.pde;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.plaf.basic.BasicComboPopup;

/**
 *
 * @author Francisco Morero Peyrona
 */
public class ComboBoxPopupMenuDemo extends JPanel
{
    BasicComboPopup myPopup;
    JComboBox petList;

    public ComboBoxPopupMenuDemo()
    {
        super( new BorderLayout() );
        String[] petStrings =
        {
            "Bird", "Cat", "Dog", "Rabbit", "Pig", "Fish", "Horse", "Cow", "Bee", "Skunk"
        };
        petList = new JComboBox( petStrings );
        petList.addActionListener( new ActionListener()
           {
               public void actionPerformed( ActionEvent e )
               {
                   System.out.println( petList.getSelectedItem() );		// do whatever here
                   myPopup.hide();
               }
           } );
        myPopup = new BasicComboPopup( petList );   // <---- a replacement for your standard JPopupMenu
        final JLabel lab = new JLabel( " " );
        lab.addMouseListener( new MouseAdapter()
          {
              public void mouseClicked( MouseEvent e )
              {
                  myPopup.show( lab, e.getX(), e.getY() );
              }
          } );
        add( "Center", lab );
        setPreferredSize( new Dimension( 200, 100 ) );
    }

    public static void main( String s[] )
    {
        JFrame frame = new JFrame( "ComboBoxDemo" );
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        frame.setContentPane( new ComboBoxPopupMenuDemo() );
        frame.pack();
        frame.setVisible( true );
    }
}