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
package org.joing.pde.swing;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import org.joing.pde.desktop.container.PDEDialog;

/**
 * Shows a modal dialog.
 * 
 * @author Francisco Morero Peyrona
 */
public class DialogAcceptCancel extends PDEDialog
{
    public static final int ACCEPTED  = 1;
    public static final int CANCELLED = 0;
    
    private boolean bExitWithAccept = false;
    private JButton btnAccept       = new JButton();
    private JButton btnCancel       = new JButton();
    
    //------------------------------------------------------------------------//

    public DialogAcceptCancel( String sTitle, JComponent content )
    {
        super();

        btnAccept.setText( "Accept" );
        btnAccept.addActionListener( new ActionListener()
        {
            public void actionPerformed( ActionEvent ae )
            {
                DialogAcceptCancel.this.bExitWithAccept = true;
                DialogAcceptCancel.this.close();
            }
        } );

        btnCancel.setText( "Cancel" );
        btnCancel.addActionListener( new ActionListener()
        {
            public void actionPerformed( ActionEvent ae )
            {
                DialogAcceptCancel.this.close();
            }
        } );

        JPanel  pnlButtons = new JPanel( new FlowLayout( FlowLayout.TRAILING, 5, 0 ) );
                pnlButtons.setBorder( new EmptyBorder( 0, 10, 10, 10 ) );
                pnlButtons.add( btnAccept );
                pnlButtons.add( btnCancel );
        JPanel  pnlContent = new JPanel( new BorderLayout() );
                pnlContent.add( content   , BorderLayout.CENTER );
                pnlContent.add( pnlButtons, BorderLayout.SOUTH  );

        getContentPane().add( pnlContent, BorderLayout.CENTER );
        setDefaultCloseOperation( JInternalFrame.DISPOSE_ON_CLOSE );
        getRootPane().setDefaultButton( btnAccept );
        setTitle( (sTitle == null) ? "" : sTitle );
    }

    public void setAcceptText( String sAcceptText )
    {
        if( sAcceptText != null )
            btnAccept.setText( sAcceptText );
    }

    public void setCancelText( String sCancelText )
    {
        if( sCancelText != null )
            btnCancel.setText( sCancelText );
    }
    
    public int getExitStatus()
    {
        return (bExitWithAccept ? ACCEPTED : CANCELLED);
    }
}