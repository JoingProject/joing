/*
 * (c) Telco Domótica S.L.
 * Todos los derechos reservados.
 * 
 * Creado el 05-sep-2005 a las 20:16:50
 */

package org.joing.notes;

import java.awt.Insets;

import javax.swing.JTextArea;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.undo.UndoManager;

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