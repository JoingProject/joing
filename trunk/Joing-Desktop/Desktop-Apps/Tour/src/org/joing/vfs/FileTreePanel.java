/*
 * JFileTreePanel.java
 *
 * Created on 04-ago-2007, 14:28:17
 *
 * Author: Francisco Morero Peyrona.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or (at
 * your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 */
package org.joing.vfs;

import java.awt.BorderLayout;
import java.awt.Component;
import java.io.File;
import java.util.*;
import javax.swing.*;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeNode;
import org.joing.runtime.JoingFileSystemView;

/**
 * @author Kirill Grouchnikov
 */
public class FileTreePanel extends JPanel
{
    private static JoingFileSystemView fsView;
    
    private JTree   tree;
    private boolean bShowHidden;

    /**
     * Creates the file tree panel.
     */
    public FileTreePanel()
    {
        bShowHidden = true;
        fsView      = JoingFileSystemView.getFileSystemView();
        
        File[] roots = fsView.getRoots();
        FileTreeNode rootTreeNode = new FileTreeNode( roots );
        
        tree = new JTree( rootTreeNode );
        tree.setCellRenderer( new FileTreeCellRenderer() );
        tree.setRootVisible( false );
        
        setLayout( new BorderLayout() );
        add( tree, BorderLayout.CENTER );
    }

    public boolean isShowHidden()
    {
        return bShowHidden;
    }

    public void setShowHidden( boolean bShowHidden )
    {
        this.bShowHidden = bShowHidden;
    }
    
    //------------------------------------------------------------------------//
    // INNER CLASSES
    //------------------------------------------------------------------------//
    
    /**
     * Renderer for the file tree.
     *
     * @author Kirill Grouchnikov
     */
    private class FileTreeCellRenderer extends DefaultTreeCellRenderer
    {
        // Icon cache to speed the rendering.
        private Map<String, Icon> mapIconCache = new HashMap<String, Icon>();
        
        // Root name cache to speed the rendering.
        private Map<File, String> mapRootNameCache = new HashMap<File, String>();

        /**
         * (non-Javadoc)
         *
         * @see javax.swing.tree.DefaultTreeCellRenderer#getTreeCellRendererComponent(javax.swing.JTree,
         *      java.lang.Object, boolean, boolean, boolean, int, boolean)
         */
        @Override
        public Component getTreeCellRendererComponent( JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus )
        {
            FileTreeNode node = (FileTreeNode) value;
            File         file = node.file;
            String       name = "";
            
            if( file != null )
            {
                if( node.isFileSystemRoot )
                {
                    name = this.mapRootNameCache.get( file );
                    
                    if( name == null )
                    {
                        name = fsView.getSystemDisplayName( file );
                        this.mapRootNameCache.put( file, name  );
                    }
                }
                else
                {
                    name = file.getName();
                }
            }
            
            JLabel result = (JLabel) super.getTreeCellRendererComponent(
                                            tree, name, sel, expanded, leaf, row, hasFocus);
            if( file != null )
            {
                Icon icon = this.mapIconCache.get( name  );
                
                if( icon == null )
                {
                    icon = fsView.getSystemIcon( file );
                    this.mapIconCache.put( name, icon  );
                }
                
                result.setIcon( icon );
            }
            
            return result;
        }
    }

    //------------------------------------------------------------------------//
    // INNER CLASS
    //------------------------------------------------------------------------//
    
    /**
     * A node in the file tree.
     *
     * @author Kirill Grouchnikov
     */
    private class FileTreeNode implements TreeNode
    {
        private File     file;
        private File[]   children;
        private TreeNode parent;
        private boolean  isFileSystemRoot;

        /**
         * Creates a new file tree node.
         *
         * @param file
         *            Node file
         * @param isFileSystemRoot
         *            Indicates whether the file is a file system root.
         * @param parent
         *            Parent node.
         */
        private FileTreeNode( File file, boolean isFileSystemRoot, TreeNode parent )
        {
            this.file             = file;
            this.isFileSystemRoot = isFileSystemRoot;
            this.parent           = parent;
            
            if( file.isDirectory() )
                this.children = FileTreePanel.fsView.getFiles( file, FileTreePanel.this.isShowHidden() );
            
            if( this.children == null )
                this.children = new File[0];
        }

        /**
         * Creates a new file tree node.
         *
         * @param children
         *            Children files.
         */
        private FileTreeNode( File[] children )
        {
            this.file = null;
            this.parent = null;
            this.children = children;
        }

        /**
         *
         * @see javax.swing.tree.TreeNode#children()
         */
        public Enumeration<?> children()
        {
            final int elementCount = this.children.length;
            
            return new Enumeration<File>()
            {
                int count = 0;

                /**
                 *
                 * @see java.util.Enumeration#hasMoreElements()
                 */
                public boolean hasMoreElements()
                {
                    return this.count < elementCount;
                }

                /**
                 *
                 * @see java.util.Enumeration#nextElement()
                 */
                public File nextElement()
                {
                    if( this.count < elementCount )
                        return FileTreeNode.this.children[this.count++];
                    
                    throw new NoSuchElementException( "Vector Enumeration" );
                }
            };
        }

        /**
         *
         * @see javax.swing.tree.TreeNode#getAllowsChildren()
         */
        public boolean getAllowsChildren()
        {
            return true;
        }

        /**
         *
         * @see javax.swing.tree.TreeNode#getChildAt(int)
         */
        public TreeNode getChildAt( int childIndex )
        {
            return new FileTreeNode( this.children[childIndex], this.parent == null, this );
        }

        /**
         *
         * @see javax.swing.tree.TreeNode#getChildCount()
         */
        public int getChildCount()
        {
            return this.children.length;
        }

        /**
         *
         * @see javax.swing.tree.TreeNode#getIndex(javax.swing.tree.TreeNode)
         */
        public int getIndex( TreeNode node )
        {
            FileTreeNode ftn = (FileTreeNode) node;
            
            for( int n = 0; n < this.children.length; n++  )
            {
                if( ftn.file.equals( this.children[n]  ) )
                    return n;
            }
            
            return -1;
        }

        /**
         *
         * @see javax.swing.tree.TreeNode#getParent()
         */
        public TreeNode getParent()
        {
            return this.parent;
        }

        /**
         *
         * @see javax.swing.tree.TreeNode#isLeaf()
         */
        public boolean isLeaf()
        {
            return this.getChildCount() == 0;
        }
    }
}