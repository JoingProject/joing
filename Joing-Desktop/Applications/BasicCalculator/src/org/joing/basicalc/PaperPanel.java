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
package org.joing.basicalc;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.util.Vector;

import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

/**
 * 
 * @author Francisco Morero Peyrona
 */
final class PaperPanel extends JScrollPane
{
    private Vector vLines   = new Vector();
    private JTable tblPaper = new JTable();

    //------------------------------------------------------------------------//
    
    PaperPanel()
    {
        init();
    }

    void addLine( String sLine )
    {
        Vector<String> vLine = new Vector<String>( 1 );
                       vLine.add( sLine );
                       
        ((DefaultTableModel) this.tblPaper.getModel()).addRow( vLine );
        
        // Makes selected row to be visible
        Rectangle rect = tblPaper.getCellRect( tblPaper.getRowCount() - 1, 0, true );
        getViewport().scrollRectToVisible( rect );
    }

    void reset()
    {
        ((DefaultTableModel) this.tblPaper.getModel()).setRowCount( 0 );
    }

    //------------------------------------------------------------------------//
    private void init()
    {
        getViewport().setBackground( new Color( 255, 255, 235 ) );
        getViewport().add( tblPaper );

        tblPaper.setModel( new dataModel() );
        tblPaper.setTableHeader( null );
        tblPaper.setBackground( getViewport().getBackground() );
        tblPaper.setForeground( Color.blue );
        tblPaper.setGridColor( new Color( 255, 74, 99 ) );
        tblPaper.setCellSelectionEnabled( false );
        tblPaper.setFocusable( false );
        tblPaper.setRowSelectionAllowed( false );
        tblPaper.setShowVerticalLines( false );
        tblPaper.setFont( new java.awt.Font( "Monospaced", 0, 12 ) );

        // Set horizontal alignment to right
        TableColumn col = tblPaper.getColumnModel().getColumn( 0 );

        DefaultTableCellRenderer cr = new DefaultTableCellRenderer();
        cr.setHorizontalAlignment( JLabel.RIGHT );
        col.setCellRenderer( cr );

        setPreferredSize( new Dimension( 110, 110 ) );
    }

    //-------------------------------------------------------------------------//
    // Table Model Inner Class
    //-------------------------------------------------------------------------//
    class dataModel extends DefaultTableModel
    {
        public dataModel()
        {
            Vector<String> vCols = new Vector<String>();      // Created just because DefaultTableModel requires it
            vCols.add( "Line" );

            setDataVector( vLines, vCols );
        }

        public boolean isCellEditable( int row, int col )
        {
            return false;
        }
    }
}
