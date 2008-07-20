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
package org.joing.yace;

import java.awt.Component;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

/**
 *
 * @author Francico Morero Peyrona
 */
class UserTreeCellRenderer extends TreeCellRendererSimple
{
    private static ImageIcon iconUserConnected;
    private static ImageIcon iconUserDisconnected;
    private static ImageIcon iconUserBusy;
    
    UserTreeCellRenderer()
    {
        if( iconUserBusy == null )
        {
            iconUserConnected    = new ImageIcon( getClass().getResource( "images/status_connected.png" ) );
            iconUserDisconnected = new ImageIcon( getClass().getResource( "images/status_disconnected.png" ) );
            iconUserBusy         = new ImageIcon( getClass().getResource( "images/status_busy.png" ) );
            
            iconUserConnected    = new ImageIcon( iconUserConnected.getImage().getScaledInstance( 18, 18, Image.SCALE_SMOOTH ) );
            iconUserDisconnected = new ImageIcon( iconUserDisconnected.getImage().getScaledInstance( 18, 18, Image.SCALE_SMOOTH ) );
            iconUserBusy         = new ImageIcon( iconUserBusy.getImage().getScaledInstance( 18, 18, Image.SCALE_SMOOTH ) );
        }
    }
    
    public Component getTreeCellRendererComponent( JTree tree, Object value, boolean selected, 
                                               boolean expanded, boolean leaf, int row, boolean hasFocus )
    {
        int nStatus = new Double( Math.random() * 3 ).intValue();
        
        Object objUser  = ((DefaultMutableTreeNode) value).getUserObject();
        String sText    = (String) objUser;
        String sToolTip = sText + "@joing.org [" + (nStatus == 0 ? "Connected" : (nStatus == 1 ? "Disconnected" : "Busy")) + "]";
        
        lblText.setText( (String) objUser );
        lblText.setToolTipText( sToolTip );
        
        if( ((DefaultMutableTreeNode) value).isRoot() )
        {
            lblIcon.setIcon( null );
            lblIcon.setToolTipText( null );
        }
        else
        {
            lblIcon.setIcon( (nStatus == 0 ? iconUserConnected : (nStatus == 1 ? iconUserDisconnected : iconUserBusy)) );
            lblIcon.setToolTipText( sToolTip );
        }
        
        return super.getTreeCellRendererComponent( tree, value, selected, expanded, leaf, row, hasFocus );
    }
}