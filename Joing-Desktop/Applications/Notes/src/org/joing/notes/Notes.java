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

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Element;
import javax.swing.text.JTextComponent;
import org.joing.common.desktopAPI.DeskComponent;
import org.joing.common.desktopAPI.DesktopManager;
import org.joing.common.desktopAPI.StandardImage;
import org.joing.common.desktopAPI.pane.DeskDialog;
import org.joing.common.desktopAPI.pane.DeskFrame;
import org.joing.common.dto.vfs.VFSFile4IO;
import org.joing.runtime.swap.JoingFileChooser;
import org.joing.runtime.vfs.VFSFile;
import org.joing.swingtools.JoingPanel;

/**
 * A simple plain text editor similar to Windows NotePad.
 * <p>
 * This class deals with the user interface part of the application.
 * 
 * @author Francisco Morero Peyrona
 */
public class Notes extends JPanel implements DeskComponent
{
    private int nCount = 1;    // Para "Noname1", "Noname2", ....
    
    private DesktopManager desktop = org.joing.jvmm.RuntimeFactory.getPlatform().getDesktopManager();
    private File           file    = null;   // Sat after the user saves for 1st time the document
    private VFSFile4IO     vfs4IO  = null;   // FIXME: Esta es una solución temp hasta que VFSFile4IO herede de VFSFile
    private ToolBar        toolbar = new ToolBar();
    private JTabbedPane    tabs    = new JTabbedPane();
    private StatusBar      status  = new StatusBar();
    
    //------------------------------------------------------------------------//
    
    public Notes()
    {
        tabs.addChangeListener( new ChangeListener() 
            {
                public void stateChanged( ChangeEvent e )
                {
                    Editor editor = Notes.this.getSelectedEditor(); 
                 
                    Notes.this.toolbar.updateButtons( editor );
                    Notes.this.status.updateCaret( editor );
                }
            } );
        
        setPreferredSize( new Dimension( 640,480 ) );
        
        setLayout( new BorderLayout() );
        add( toolbar, BorderLayout.NORTH  );
        add( tabs   , BorderLayout.CENTER );
        add( status , BorderLayout.SOUTH  );
        
        toolbar.updateButtons( null );
    }
    
    public void addEditor()
    {
        addEditor( (String) null, (String) null );
    }
    
    public void addEditor( String sName, String sText )
    {
        if( sName == null || sName.length() == 0 )
            sName = "Noname" + nCount++;
        
        _addEditor( sName, sText );
    }
    
    public void showInFrame()
    {
        // Show this panel in a frame created by DesktopManager Runtime.
        DeskFrame frame = desktop.getRuntime().createFrame();
                  frame.setTitle( "Notes" );
                  frame.setIcon( getIcon( "notes" ).getImage() );
                  frame.add( this );

        desktop.getDesktop().getActiveWorkArea().add( frame );
    }
    
    //------------------------------------------------------------------------//
    
    private Editor getSelectedEditor()
    {
        Editor      editor = null;
        JScrollPane sp     = (JScrollPane) tabs.getSelectedComponent();
        
        if( sp != null )
            editor = (Editor)  sp.getViewport().getView();
        
        return editor;
    }
    
    private void open()
    {
        InputStreamReader isr = null;

        try
        {
            if( file instanceof VFSFile )
            {
                vfs4IO = org.joing.jvmm.RuntimeFactory.getPlatform().getBridge().
                            getFileBridge().getFile( ((VFSFile) file).getFileDescriptor() );
                isr = vfs4IO.getCharReader();
            }
            else
            {
                isr = new FileReader( file );
            }

            // As text files are normally small, we read it up in one step
            char[] chr  = new char[ (int) file.length() ];
            isr.read( chr );
            _addEditor( file.getName(), String.valueOf( chr ) );
        }
        catch( Exception exc )
        {
            desktop.getRuntime().showException( exc, "Error opening file" );
        }
        finally
        {
            if( isr != null )
                try{ isr.close(); } catch( IOException exc ) { /* Nothing to do */ }
        }
    }
    
    private void save()
    {
        OutputStreamWriter osw = null;

        try
        {
            if( file instanceof VFSFile )
                osw = vfs4IO.getCharWriter();
            else
                osw = new FileWriter( file );
            
            osw.write( getSelectedEditor().getText() );
        }
        catch( IOException exc )
        {
            desktop.getRuntime().showException( exc, "Error opening file" );
        }
        finally
        {
            if( osw != null )
                try{ osw.close(); } catch( IOException exc ) { /* Nothing to do */ }
        }
            
        sendFocusToSelectedEditor();
    }
    
    private void _addEditor( final String sName, final String sText )
    {
        SwingUtilities.invokeLater( new Runnable() 
        {
            public void run()
            {
                Editor editor = new Editor( sText );
                       editor.addCaretListener( new CaretListener()
                       {
                           public void caretUpdate( CaretEvent e )
                           {
                               toolbar.updateButtons( Notes.this.getSelectedEditor() );
                               status.updateCaret( Notes.this.getSelectedEditor() );
                           }
                       } );
                       
                tabs.addTab( sName, new JScrollPane( editor ) );
                tabs.setSelectedIndex( tabs.getTabCount() - 1 );
                
                toolbar.updateButtons( Notes.this.getSelectedEditor() );
                Notes.this.getSelectedEditor().requestFocus();
            }
        } );
    }
    
    private void closeSelectedEditor()
    {
        int nIndex = tabs.getSelectedIndex();
        
        tabs.removeTabAt( nIndex );
        
        if( tabs.getTabCount() > 0 )
        {
            if( nIndex == 0 )
                tabs.setSelectedIndex( 0 );
            else    
                tabs.setSelectedIndex( nIndex - 1 );
        }
        
        toolbar.updateButtons( getSelectedEditor() );
    }
    
    //------------------------------------------------------------------------//
    // ACTIONS: INNER CLASSES
    //------------------------------------------------------------------------//
    
    private class New extends AbstractAction
    {
        private New()
        {
            super( "New" );
            putValue( AbstractAction.SMALL_ICON       , new ImageIcon( desktop.getRuntime().getImage( StandardImage.NEW, 22, 22 ) ) );
            putValue( AbstractAction.SHORT_DESCRIPTION, "Create a new empty tab" );
            putValue( AbstractAction.ACCELERATOR_KEY  , KeyStroke.getKeyStroke( new Integer( KeyEvent.VK_N ), InputEvent.CTRL_MASK ) );
        }

        public void actionPerformed( ActionEvent evt )
        {
            addEditor( null, null );
            sendFocusToSelectedEditor();
        }
    }
    
    private class Open extends AbstractAction
    {
        private Open()
        {
            super( "Open" );
            putValue( AbstractAction.SMALL_ICON       , getIcon( "open" ) );
            putValue( AbstractAction.SHORT_DESCRIPTION, "Open existing file" );
            putValue( AbstractAction.ACCELERATOR_KEY  , KeyStroke.getKeyStroke( new Integer( KeyEvent.VK_O ), InputEvent.CTRL_MASK ) );
        }
        
        public void actionPerformed( ActionEvent evt )
        {
            JoingFileChooser jfc = new JoingFileChooser();
            
            if( jfc.showDialog( Notes.this ) == JoingFileChooser.APPROVE_OPTION )
            {
                Notes.this.file = jfc.getSelectedFile();
                open();
            }
        }
    }
    
    private class Save extends AbstractAction
    {
        private Save()
        {
            super( "Save" );
            putValue( AbstractAction.SMALL_ICON       , getIcon( "save" ) );
            putValue( AbstractAction.SHORT_DESCRIPTION, "Save file" );
            putValue( AbstractAction.ACCELERATOR_KEY  , KeyStroke.getKeyStroke( new Integer( KeyEvent.VK_S ), InputEvent.CTRL_MASK ) );
        }

        public void actionPerformed( ActionEvent evt )
        {
            if( file == null )
            {
                // TODO: Abrir un JoingFileChooser para Save
                JOptionPane.showMessageDialog( Notes.this, "Option not yet implemented." );
            }
            
            if( file != null )   // Can be null if user cancels JoingFileChooser            
                save();
        }
    }
    
    private class Print extends AbstractAction
    {
        private Print()
        {
            super( "Print" );
            putValue( AbstractAction.SMALL_ICON       , new ImageIcon( desktop.getRuntime().getImage( StandardImage.PRINT, 22, 22 ) ) );
            putValue( AbstractAction.SHORT_DESCRIPTION, "Print text" );
            putValue( AbstractAction.ACCELERATOR_KEY  , KeyStroke.getKeyStroke( new Integer( KeyEvent.VK_P ), InputEvent.CTRL_MASK ) );
        }

        public void actionPerformed( ActionEvent evt )
        {
            // TODO hacerlo
            JOptionPane.showMessageDialog( Notes.this, "Option not yet implemented" );
            sendFocusToSelectedEditor();
        }
    }
    
    private class Undo extends AbstractAction
    {
        private Undo()
        {
            super( "Undo" );
            putValue( AbstractAction.SMALL_ICON       , getIcon( "undo" ) );
            putValue( AbstractAction.SHORT_DESCRIPTION, "Undo last action" );
            putValue( AbstractAction.ACCELERATOR_KEY  , KeyStroke.getKeyStroke( new Integer( KeyEvent.VK_Z ), InputEvent.CTRL_MASK ) );
        }

        public void actionPerformed( ActionEvent evt )
        {
            if( Notes.this.getSelectedEditor().canUndo() )
                Notes.this.getSelectedEditor().undo();
            
            sendFocusToSelectedEditor();
        }
    }
    
    private class Redo extends AbstractAction
    {
        private Redo()
        {
            super( "Redo" );
            putValue( AbstractAction.SMALL_ICON       , getIcon( "redo" ) );
            putValue( AbstractAction.SHORT_DESCRIPTION, "Redo last action" );
            putValue( AbstractAction.ACCELERATOR_KEY  , KeyStroke.getKeyStroke( new Integer( KeyEvent.VK_Y ), InputEvent.CTRL_MASK ) );
        }

        public void actionPerformed( ActionEvent evt )
        {
            if( Notes.this.getSelectedEditor().canRedo() )
                Notes.this.getSelectedEditor().redo();
            
            sendFocusToSelectedEditor();
        }
    }
    
    private class Cut extends AbstractAction
    {
        private Cut()
        {
            super( "Cut" );
            putValue( AbstractAction.SMALL_ICON       , new ImageIcon( desktop.getRuntime().getImage( StandardImage.CUT, 22, 22 ) ) );
            putValue( AbstractAction.SHORT_DESCRIPTION, "Cut selected text   Ctrl-X" );
        }

        public void actionPerformed( ActionEvent evt )
        {
            Notes.this.getSelectedEditor().cut();
            sendFocusToSelectedEditor();
        }
    }
    
    private class Copy extends AbstractAction
    {
        private Copy()
        {
            super( "Copy" );
            putValue( AbstractAction.SMALL_ICON       , new ImageIcon( desktop.getRuntime().getImage( StandardImage.COPY, 22, 22 ) ) );
            putValue( AbstractAction.SHORT_DESCRIPTION, "Copy selected text   Ctrl-C" );
        }

        public void actionPerformed( ActionEvent evt )
        {
            Notes.this.getSelectedEditor().copy();
            sendFocusToSelectedEditor();
        }
    }
    
    private class Paste extends AbstractAction
    {
        private Paste()
        {
            super( "Paste" );
            putValue( AbstractAction.SMALL_ICON       , new ImageIcon( desktop.getRuntime().getImage( StandardImage.PASTE, 22, 22 ) ) );
            putValue( AbstractAction.SHORT_DESCRIPTION, "Paste text from clipboard   Ctrl-V" );
        }

        public void actionPerformed( ActionEvent evt )
        {
            Notes.this.getSelectedEditor().paste();
            sendFocusToSelectedEditor();
        }
    }
    
    private class Close extends AbstractAction
    {
        private Close()
        {
            super( "Close" );
            putValue( AbstractAction.SMALL_ICON       , getIcon( "close_tab" ) );
            putValue( AbstractAction.SHORT_DESCRIPTION, "Close selected tab" );
        }

        public void actionPerformed( ActionEvent evt )
        {
            closeSelectedEditor();
        }
    }
    
    private class About extends AbstractAction
    {
        private About()
        {
            super( "About" );
            putValue( AbstractAction.SMALL_ICON       , new ImageIcon( desktop.getRuntime().getImage( StandardImage.INFO, 22, 22 ) ) );
            putValue( AbstractAction.SHORT_DESCRIPTION, "About this application" );
        }

        public void actionPerformed( ActionEvent evt )
        {            
            JLabel     label  = new JLabel( "<html><body><b><div align=\"center\">Notes</b><p>"+
                                            "<p>A simple text editor."+
                                            "<p>Developed by<br><i>Francisco Morero Peyrona</i></p></div></body></html>" );
            
            JoingPanel panel  = new JoingPanel( new BorderLayout( 10,10 ) );
                       panel.add( new JLabel( getIcon( "notes" ) ), BorderLayout.WEST );
                       panel.add( label, BorderLayout.CENTER );
                       panel.setBorder( new EmptyBorder( 7,7,7,7 ) );
                       
            DeskDialog dialog = desktop.getRuntime().createDialog();
                       dialog.setTitle( "About Notes" );
                       dialog.setIcon( getIcon( "notes" ).getImage() );
                       dialog.add( (DeskComponent) panel );
                       
            desktop.getDesktop().getActiveWorkArea().add( dialog );
        }
    }
    
    private void sendFocusToSelectedEditor()
    {
        SwingUtilities.invokeLater( new Runnable()
        {
            public void run()
            {
                Notes.this.getSelectedEditor().requestFocus();
            }
        } );
    }
    
    //------------------------------------------------------------------------//
    // INNER CLASSES
    //------------------------------------------------------------------------//
    
    // La toolbar
    private final class ToolBar extends JToolBar
    {
        private JButton save   = createButton( new Save()   );
        private JButton print  = createButton( new Print()  );
        private JButton undo   = createButton( new Undo()   );
        private JButton redo   = createButton( new Redo()   );
        private JButton cut    = createButton( new Cut()    );
        private JButton copy   = createButton( new Copy()   );
        private JButton paste  = createButton( new Paste()  );
        private JButton close  = createButton( new Close()  );
        
        private ToolBar()
        {
            setRollover( true );
            
            add( createButton( new New()  ) );
            add( createButton( new Open() ) );
            add( save );
            add( print );
            addSeparator();
            add( undo );
            add( redo );
            addSeparator();
            add( cut );
            add( copy );
            add( paste );
            addSeparator();
            add( close );
            addSeparator();
            add( createButton( new About() ) );
        }
        
        private void updateButtons( Editor text )
        {
            if( text != null )
            {
                boolean bSelection = text.getSelectedText() != null;
                
                this.save.getAction().setEnabled( true );
                this.print.getAction().setEnabled( true );
                
                this.cut.getAction().setEnabled( bSelection );
                this.copy.getAction().setEnabled( bSelection );
                this.paste.getAction().setEnabled( true );
                
                this.undo.getAction().setEnabled( text.canUndo() );
                this.redo.getAction().setEnabled( text.canRedo() );
                
                this.close.getAction().setEnabled( true );
            }
            else
            {
                this.save.getAction().setEnabled( false );
                this.print.getAction().setEnabled( false );
                this.undo.getAction().setEnabled( false );
                this.redo.getAction().setEnabled( false );
                this.cut.getAction().setEnabled( false );
                this.copy.getAction().setEnabled( false );
                this.paste.getAction().setEnabled( false );
                this.close.getAction().setEnabled( false );
            }
        }
        
        private JButton createButton( Action action )
        {
            JButton button = new JButton( action );
                    button.setFocusPainted( false );
                    button.setText( null );
                    button.setOpaque( false );
                    button.setBorderPainted( false );
                    button.addMouseListener( new MouseAdapter() 
                        {
                            public void mouseEntered( MouseEvent me )
                            {
                                JButton btn = (JButton) me.getSource();
                                        btn.setBorderPainted( true );
                                status.setHelp( btn );
                            }

                            public void mouseExited( MouseEvent me )
                            {
                                JButton btn = (JButton) me.getSource();
                                        btn.setBorderPainted( false );
                                status.setHelp( null );
                            }
                        } );
            
            KeyStroke keyStroke = (KeyStroke) action.getValue( AbstractAction.ACCELERATOR_KEY );
            
            if( keyStroke != null )
            {
                button.getInputMap( JComponent.WHEN_IN_FOCUSED_WINDOW ).put( keyStroke, "action" );
                button.getActionMap().put( "action",
                    new AbstractAction()
                    {
                        public void actionPerformed( java.awt.event.ActionEvent ae )
                        {
                             ((JButton) ae.getSource()).doClick();
                        }
                    }
                );
            }
                    
            return button;
        }
    }
    
    // La status bar
    private final class StatusBar extends Box
    {
        private JLabel lblHelp, lblCaret;

        public StatusBar()
        {
            super( BoxLayout.X_AXIS );
            
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

            // Add the JLabel displaying the button's tooltip.
            lblHelp = new JLabel( "", SwingConstants.LEADING );
            lblHelp.setFont( lblHelp.getFont().deriveFont( Font.PLAIN ) );
            lblHelp.setPreferredSize( new Dimension( (int) (0.85 * screenSize.width), 22 ) );
            lblHelp.setBorder( BorderFactory.createLoweredBevelBorder() );
            add( lblHelp, null );

            // Add the JLabel displaying the line and colum caret position.
            lblCaret = new JLabel( "", SwingConstants.LEADING );
            lblCaret.setFont( lblCaret.getFont().deriveFont( Font.PLAIN ) );
            lblCaret.setPreferredSize( new Dimension( (int) (0.15 * screenSize.width), 22 ) );
            lblCaret.setBorder( BorderFactory.createLoweredBevelBorder() );
            add( lblCaret, null );
            
            setHelp( null );
            updateCaret( null );
        }
        
        private void setHelp( JComponent comp )
        {
            String sHelp = " ";      // Sino, se ve fatal
            
            if( comp != null )
                sHelp += comp.getToolTipText();   
            
            lblHelp.setText( sHelp );
        }
        
        void updateCaret( Editor text )
        {
            int nLin = 1;
            int nCol = 1;
             
            if( text != null )
            {
                int nCaret = text.getCaretPosition();
                
                try
                {
                    nLin = text.getLineOfOffset( nCaret )   + 1;
                    nCol = getColumnAtCaret( text, nCaret ) + 1;
                }
                catch( BadLocationException exc )
                {
                }
            }
            
            lblCaret.setText( " Lin "+ nLin +"  Col "+ nCol );
        }
        
        private int getColumnAtCaret( JTextComponent component, int nCaret )
        {
            Element root      = component.getDocument().getDefaultRootElement();
            int     line      = root.getElementIndex( nCaret );
            int     lineStart = root.getElement( line ).getStartOffset();
     
            return nCaret - lineStart;
        }
    }
 
    //------------------------------------------------------------------------//
    
    private ImageIcon getIcon( String sName )
    {
        return new ImageIcon( getClass().getResource( "images/"+ sName +".png" ) ); 
    }
    
    //------------------------------------------------------------------------//
    
    public static void main( String[] asArg )
    {
        (new Notes()).showInFrame();
    }
}