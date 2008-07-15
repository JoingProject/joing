/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.joing.yace;

import java.awt.Component;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

/**
 *
 * @author fmorero
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