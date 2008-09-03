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
package org.joing.notes;

import java.awt.Insets;

import java.io.File;
import javax.swing.JTextArea;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.undo.UndoManager;

/**
 * This class implements the editor functionality part of the application.
 * 
 * @author Francisco Morero Peyrona
 */
class Editor extends JTextArea
{
    private UndoManager undomanager;
    private File        file = null;
    
    //------------------------------------------------------------------------//
    
    Editor()
    {
        this( (String) null );
    }
    
    Editor( String sText )
    {
        super( sText );
        
        setLineWrap( true );
        setWrapStyleWord( true );
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
        ///bNewDoc = (sText == null);
    }
    
    /**
     * Get associated file or null.
     * 
     * @return Associated file or null
     */
    File getFile()
    {
        return file;
    }
    
    void setFile( File file )
    {
        this.file = file;
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