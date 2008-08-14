/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.joing.swingtools.filesystem.fsviewer.fstree;

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