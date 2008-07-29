/*
 * JoingApplicationChooser.java
 *
 * Created on 3 de julio de 2008, 20:27
 */
package org.joing.pde.joingswingtools;

import org.joing.pde.joingswingtools.tree.TreeCellRendererSimple;
import java.awt.Component;
import java.awt.Image;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeSelectionModel;
import org.joing.common.desktopAPI.DeskComponent;
import org.joing.common.dto.app.AppDescriptor;
import org.joing.common.dto.app.AppGroup;
import org.joing.common.dto.app.AppGroupKey;

/**
 * A panel with a tree showing applications that user has access to.
 * 
 * @author  Francisco Morero Peyrona
 */
public class JoingApplicationChooser extends JPanel implements DeskComponent
{
    private JTree     tree;
    private JTextArea text;
    
    //------------------------------------------------------------------------//
    
    /** 
     * Creates new JoingApplicationChooser panel.
     */
    public JoingApplicationChooser()
    {
        initComponents();
        
        createTree();
        createText();
        
        split.setTopComponent( new JScrollPane( tree ) );
        split.setBottomComponent( new JScrollPane( text ) );
        split.setResizeWeight( 0.9d );
        
        tree.addTreeSelectionListener( new TreeSelectionListener()
            {
                public void valueChanged( TreeSelectionEvent tse )
                {
                    DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
                    String sDesc = null;
                    
                    if( node != null )
                    {
                        Object obj = node.getUserObject();
                        
                        if(      obj instanceof AppGroup      ) sDesc = ((AppGroup) obj).getDescription();
                        else if( obj instanceof AppDescriptor ) sDesc = ((AppDescriptor) obj).getDescription();
                    }
                    
                    text.setText( sDesc );
                }
           } );
    }
    
    /**
     * Return the <code>org.joing.common.dto.app.AppDescriptor</code> that
     * corresponds to selected node in the tree or <code>null</code> if no node 
     * is selected.
     * 
     * @return An instance of <code>AppDescriptor</code> or <code>null</code>.
     */
    public AppDescriptor getSelectedApplication()
    {
        DefaultMutableTreeNode nodeSelected = (DefaultMutableTreeNode) tree.getSelectionPath().getLastPathComponent();
        AppDescriptor appDesc = null;
        
        if( (nodeSelected != null) && (nodeSelected.getUserObject() instanceof AppDescriptor) )
            appDesc = (AppDescriptor) nodeSelected.getUserObject();
        
        return appDesc;
    }
    
    /**
     * Open a dialog showing in a tree all installed applications for current
     * user.
     * 
     * @return An instance of <code>org.joing.common.dto.app.AppDescriptor</code>
     *         if user selected an application or <code>null</code> if cancelled.
     */
    public static AppDescriptor showDialog()
    {
        AppDescriptor app = null;
        
        synchronized( JoingApplicationChooser.class )
        {
            JoingApplicationChooser atp = new JoingApplicationChooser();
        
            if( org.joing.jvmm.RuntimeFactory.getPlatform().getDesktopManager().getRuntime().
                    showAcceptCancelDialog( "Select Application", atp ) )
            {
                app = atp.getSelectedApplication();
            }
        }
        
        return app;
    }
    
    //------------------------------------------------------------------------//
    
    private void createText()
    {
        text = new JTextArea();
        text.setEditable( false );
        text.setLineWrap( true );
        text.setWrapStyleWord( true );
    }
    
    private void createTree()
    {
        boolean bRootVisible = false;
        
        List<AppGroup> lstGroups = org.joing.jvmm.RuntimeFactory.getPlatform().getBridge().getAppBridge().getInstalledForUser( AppGroupKey.ALL );
        
        DefaultMutableTreeNode root = new DefaultMutableTreeNode( "Installed Apps" );
        
        if( lstGroups != null )
        {
            for( AppGroup group : lstGroups )
            {
                if( group.getGroupKey() == AppGroupKey.DESKTOP )    // Don't show "Desktops" apps
                    break;
                
                DefaultMutableTreeNode node = new DefaultMutableTreeNode( group );
                
                root.add( node );
                
                List<AppDescriptor> appList = group.getApplications();
                
                for( AppDescriptor appDesc : appList )
                {
                    DefaultMutableTreeNode childNode = new DefaultMutableTreeNode( appDesc );
                    node.add( childNode );
                }
            }
        }
        else
        {
            root.setUserObject( "No apps for this user" );
            bRootVisible = true;
        }
        
        tree = new JTree( root );
        tree.setRootVisible( bRootVisible );
        tree.setShowsRootHandles( true );
        tree.setEditable( false );
        tree.setCellRenderer( new MyCellRenderer() );
        tree.getSelectionModel().setSelectionMode( TreeSelectionModel.SINGLE_TREE_SELECTION );
    }
    
    //------------------------------------------------------------------------//
    // INNER CLASS: MyCellRenderer
    //------------------------------------------------------------------------//
    
    private final class MyCellRenderer extends TreeCellRendererSimple
    {
        public Component getTreeCellRendererComponent( JTree tree, Object value, boolean selected, 
                                                   boolean expanded, boolean leaf, int row, boolean hasFocus )
        {
            Object objUser = ((DefaultMutableTreeNode) value).getUserObject();
            
            String sName = "";
            String sDesc = null;
            byte[] aByte = null;
            
            if( objUser instanceof AppGroup )
            {
                sName = ((AppGroup) objUser).getName();
                sDesc = ((AppGroup) objUser).getDescription();
                aByte = ((AppGroup) objUser).getIconPixel();
            }
            else if( objUser instanceof AppDescriptor )
            { 
                AppDescriptor appDesc = (AppDescriptor) objUser;
                
                sName = appDesc.getName() +" [Ver. "+ appDesc.getVersion() +"]";
                sDesc = appDesc.getDescription();
                aByte = appDesc.getIconPixel();
            }
            else if( objUser instanceof String )
            {
                sName = objUser.toString();
            }
            
            setText( sName );
            
            if( aByte != null )
            {
                ImageIcon icon = new ImageIcon( aByte );
            
                if( icon.getIconWidth() != 18 || icon.getIconHeight() != 18 )
                    icon.setImage( icon.getImage().getScaledInstance( 18, 18, Image.SCALE_SMOOTH ) );
                
                setIcon( icon );
            }
            else
            {
                setIcon( null );    // Must do it explicitly
            }
            
            return super.getTreeCellRendererComponent( tree, value, selected, expanded, leaf, row, hasFocus );
        }
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        split = new javax.swing.JSplitPane();

        split.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(split, javax.swing.GroupLayout.DEFAULT_SIZE, 311, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(split, javax.swing.GroupLayout.DEFAULT_SIZE, 371, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JSplitPane split;
    // End of variables declaration//GEN-END:variables
}