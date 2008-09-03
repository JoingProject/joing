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

package org.joing.kernel.swingtools.filesystem.fsviewer.fstree;

import java.awt.BorderLayout;
import java.awt.Component;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.tree.TreeCellRenderer;

/**
 * Usefull when nodes representation is composed by an icon and a text.
 * <p>
 * By inheriting from this class, the icon and the text can be properly sat
 * using lblIcon and lblText components (both are protected).<p>
 * It would me more or less like this:
 * <pre>
 *  private final class MyCellRenderer extends FileTreeCellRenderer
    {
        public Component getTreeCellRendererComponent( JTree tree, Object value, boolean selected, 
                                                   boolean expanded, boolean leaf, int row, boolean hasFocus )
        {
            Object objUser = ((DefaultMutableTreeNode) value).getUserObject();
            
            lblText.setText( ((MyTypeCasting) objUser).getName() );
            lblText.setToolTipText( ((MyTypeCasting) objUser).getDescription() );
            lblIcon.setToolTipText( new ImageIcon( ((MyTypeCasting) objUser).getImage() ) );
            
            return super.getTreeCellRendererComponent( tree, value, selected, expanded, leaf, row, hasFocus );
        }
    }
 * </pre>
 * 
 * @author Francisco Morero Peyrona
 */
class FileTreeCellRenderer extends JPanel implements TreeCellRenderer
{
    private JLabel lblIcon;
    private JLabel lblText;
    
    //------------------------------------------------------------------------//
    
    public FileTreeCellRenderer()
    {
        super( new BorderLayout( 1,0 ) );
        setOpaque( false );
        add( lblIcon, BorderLayout.WEST   );
        add( lblText, BorderLayout.CENTER );
        
        lblIcon = new JLabel();
        lblText = new JLabel();
    }
    
    public Component getTreeCellRendererComponent( JTree tree, Object value, boolean selected, 
                                                   boolean expanded, boolean leaf, int row, boolean hasFocus )
    {
        lblText.setFont( tree.getFont() );
        lblText.setOpaque( selected );
        
        if( selected )
        {
            lblText.setForeground( UIManager.getColor( "Tree.selectionForeground" ) );
            lblText.setBackground( UIManager.getColor( "Tree.selectionBackground" ) );
        }
        else
        {
            lblText.setForeground( UIManager.getColor( "Tree.textForeground" ) );
        }
        
        if( hasFocus )
            lblText.setBorder( new LineBorder( UIManager.getColor( "Tree.selectionBorderColor" ), 1 ) );
        else
            lblText.setBorder( new EmptyBorder( 1,1,1,1 ) );
        
        return this;
    }
}