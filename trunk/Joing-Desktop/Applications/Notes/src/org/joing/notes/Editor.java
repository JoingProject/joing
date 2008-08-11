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
package org.joing.notes;

import java.awt.Insets;

import javax.swing.JTextArea;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.undo.UndoManager;

/**
 * 
 * @author Francisco Morero Peyrona
 */
class Editor extends JTextArea
{
    private UndoManager undomanager;

    Editor()
    {
        this( null );
    }

    Editor( String sText )
    {
        super( sText );
        
        setLineWrap( true );
        setMargin( new Insets( 3, 3, 3, 3 ) );
        getDocument().addUndoableEditListener( new UndoableEditListener()
        {
            public void undoableEditHappened( UndoableEditEvent uee )
            {
                undomanager.addEdit( uee.getEdit() );
            }
        } );
        
        undomanager = new UndoManager();
        undomanager.setLimit( 999 );
    }

    boolean canUndo()
    {
        return undomanager.canUndo();
    }

    void undo()
    {
        undomanager.undo();
    }

    boolean canRedo()
    {
        return undomanager.canRedo();
    }
    
    void redo()
    {
        undomanager.redo();
    }
}