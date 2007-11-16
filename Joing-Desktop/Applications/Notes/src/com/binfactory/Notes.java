/*
 * (c) Telco Domótica S.L.
 * Todos los derechos reservados.
 * 
 * Creado el 03-sep-2005 a las 11:44:21
 */

package com.binfactory;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Element;
import javax.swing.text.JTextComponent;

import com.binfactory.client.desktop.DesktopDialog;
import com.binfactory.client.desktop.DesktopFrame;
import com.binfactory.client.runtime.Runtime;

/**
 * A simple editor similar to Windows NotePad.
 * 
 * @author Francisco Morero Peyrona
 */
public class Notes extends JPanel
{
    private int nCount = 1;    // Para "Noname1", "Noname2", ....
    
    private JTabbedPane tabs;
    private ToolBar     toolbar;
    private StatusBar   status;
    
    //------------------------------------------------------------------------//
    
    public Notes()
    {
        toolbar = new ToolBar();
        status  = new StatusBar();
        tabs    = new JTabbedPane();
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
    }
    
    public void addTab( String sName, String sText )
    {
        if( sName == null )
            sName = "Noname" + nCount++;
                
        Editor editor = new Editor( sText );
               editor.addCaretListener( new CaretListener()
               {
                   public void caretUpdate( CaretEvent e )
                   {
                       toolbar.updateButtons( Notes.this.getSelectedEditor() );
                       status.updateCaret( Notes.this.getSelectedEditor() );
                   }
               } );
               
        tabs.addTab( sName, Runtime.getIcon( this, "images/txt.png", 16,16 ), new JScrollPane( editor ) );
        tabs.setSelectedIndex( tabs.getTabCount() - 1 );
        
        SwingUtilities.invokeLater( new Runnable() 
        {
            public void run()
            {
                toolbar.updateButtons( Notes.this.getSelectedEditor() );
                Notes.this.getSelectedEditor().requestFocus();
            }
        } );
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
    
    private void closeSelectedEditor()
    {
        int nIndex = tabs.getSelectedIndex();
        
        tabs.removeTabAt( nIndex );
        
        if( tabs.getTabCount() > 0 )
            tabs.setSelectedIndex( nIndex - 1  );
        
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
            putValue( AbstractAction.SMALL_ICON       , Runtime.getIcon( this, "images/new.png" ) );
            putValue( AbstractAction.SHORT_DESCRIPTION, "Create a new empty tab" );
            putValue( AbstractAction.ACCELERATOR_KEY  , KeyStroke.getKeyStroke( new Integer( KeyEvent.VK_N ), InputEvent.CTRL_MASK ) );
        }

        public void actionPerformed( ActionEvent evt )
        {
            addTab( null, null );
            Notes.this.getSelectedEditor().requestFocus();
        }
    }
    
    private class Open extends AbstractAction
    {
        private Open()
        {
            super( "Open" );
            putValue( AbstractAction.SMALL_ICON       , Runtime.getIcon( this, "images/open.png" ) );
            putValue( AbstractAction.SHORT_DESCRIPTION, "Open existing file" );
            putValue( AbstractAction.ACCELERATOR_KEY  , KeyStroke.getKeyStroke( new Integer( KeyEvent.VK_O ), InputEvent.CTRL_MASK ) );
        }

        public void actionPerformed( ActionEvent evt )
        {
            // TODO hacerlo
            Notes.this.getSelectedEditor().requestFocus();
        }
    }
    
    private class Save extends AbstractAction
    {
        private Save()
        {
            super( "Save" );
            putValue( AbstractAction.SMALL_ICON       , Runtime.getIcon( this, "images/save.png" ) );
            putValue( AbstractAction.SHORT_DESCRIPTION, "Save file" );
            putValue( AbstractAction.ACCELERATOR_KEY  , KeyStroke.getKeyStroke( new Integer( KeyEvent.VK_S ), InputEvent.CTRL_MASK ) );
        }

        public void actionPerformed( ActionEvent evt )
        {
            // TODO hacerlo
            Notes.this.getSelectedEditor().requestFocus();
        }
    }
    
    private class SaveAs extends AbstractAction
    {
        private SaveAs()
        {
            super( "SaveAs" );
            putValue( AbstractAction.SMALL_ICON       , Runtime.getIcon( this, "images/save_as.png" ) );
            putValue( AbstractAction.SHORT_DESCRIPTION, "Save file with different name" );
        }

        public void actionPerformed( ActionEvent evt )
        {
            // TODO hacerlo
            Notes.this.getSelectedEditor().requestFocus();
        }
    }
    
    private class Print extends AbstractAction
    {
        private Print()
        {
            super( "Print" );
            putValue( AbstractAction.SMALL_ICON       , Runtime.getIcon( this, "images/print.png" ) );
            putValue( AbstractAction.SHORT_DESCRIPTION, "Print text" );
            putValue( AbstractAction.ACCELERATOR_KEY  , KeyStroke.getKeyStroke( new Integer( KeyEvent.VK_P ), InputEvent.CTRL_MASK ) );
        }

        public void actionPerformed( ActionEvent evt )
        {
            // TODO hacerlo
            Notes.this.getSelectedEditor().requestFocus();
        }
    }
    
    private class Undo extends AbstractAction
    {
        private Undo()
        {
            super( "Undo" );
            putValue( AbstractAction.SMALL_ICON       , Runtime.getIcon( this, "images/undo.png" ) );
            putValue( AbstractAction.SHORT_DESCRIPTION, "Undo last action" );
            putValue( AbstractAction.ACCELERATOR_KEY  , KeyStroke.getKeyStroke( new Integer( KeyEvent.VK_Z ), InputEvent.CTRL_MASK ) );
        }

        public void actionPerformed( ActionEvent evt )
        {
            if( Notes.this.getSelectedEditor().canUndo() )
                Notes.this.getSelectedEditor().undo();
            
            Notes.this.getSelectedEditor().requestFocus();
        }
    }
    
    private class Redo extends AbstractAction
    {
        private Redo()
        {
            super( "Redo" );
            putValue( AbstractAction.SMALL_ICON       , Runtime.getIcon( this, "images/redo.png" ) );
            putValue( AbstractAction.SHORT_DESCRIPTION, "Redo last action" );
            putValue( AbstractAction.ACCELERATOR_KEY  , KeyStroke.getKeyStroke( new Integer( KeyEvent.VK_Y ), InputEvent.CTRL_MASK ) );
        }

        public void actionPerformed( ActionEvent evt )
        {
            if( Notes.this.getSelectedEditor().canRedo() )
                Notes.this.getSelectedEditor().redo();
            
            Notes.this.getSelectedEditor().requestFocus();
        }
    }
    
    private class Cut extends AbstractAction
    {
        private Cut()
        {
            super( "Cut" );
            putValue( AbstractAction.SMALL_ICON       , Runtime.getIcon( this, "images/cut.png" ) );
            putValue( AbstractAction.SHORT_DESCRIPTION, "Cut selected text   Ctrl-X" );
        }

        public void actionPerformed( ActionEvent evt )
        {
            Notes.this.getSelectedEditor().cut();
            Notes.this.getSelectedEditor().requestFocus();
        }
    }
    
    private class Copy extends AbstractAction
    {
        private Copy()
        {
            super( "Copy" );
            putValue( AbstractAction.SMALL_ICON       , Runtime.getIcon( this, "images/copy.png" ) );
            putValue( AbstractAction.SHORT_DESCRIPTION, "Copy selected text   Ctrl-C" );
        }

        public void actionPerformed( ActionEvent evt )
        {
            Notes.this.getSelectedEditor().copy();
            Notes.this.getSelectedEditor().requestFocus();
        }
    }
    
    private class Paste extends AbstractAction
    {
        private Paste()
        {
            super( "Paste" );
            putValue( AbstractAction.SMALL_ICON       , Runtime.getIcon( this, "images/paste.png" ) );
            putValue( AbstractAction.SHORT_DESCRIPTION, "Paste text from clipboard   Ctrl-V" );
        }

        public void actionPerformed( ActionEvent evt )
        {
            Notes.this.getSelectedEditor().paste();
            Notes.this.getSelectedEditor().requestFocus();
        }
    }
    
    private class Close extends AbstractAction
    {
        private Close()
        {
            super( "Close" );
            putValue( AbstractAction.SMALL_ICON       , Runtime.getIcon( this, "images/close_tab.png" ) );
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
            putValue( AbstractAction.SMALL_ICON       , Runtime.getIcon( this, "images/about.png" ) );
            putValue( AbstractAction.SHORT_DESCRIPTION, "About this application" );
        }

        public void actionPerformed( ActionEvent evt )
        {
            JLabel        label  = new JLabel( "<html><body><b><div align=\"center\">Notes</b><p>"+
                                               "A simple text editor.</div>"+
                                               "<p>Developed by <i>Francisco Morero Peyrona</i></p></body></html>" );
            JPanel        panel  = new JPanel( new BorderLayout( 10,10 ) );
                          panel.add( new JLabel( Runtime.getIcon( this, "images/notes.png" ) ), BorderLayout.WEST );
                          panel.add( label, BorderLayout.CENTER );
            DesktopDialog dialog = new DesktopDialog( "About", panel, new String[] { "Ok" }, 0 );
                          dialog.setVisible( true );
        }
    }
    
    //------------------------------------------------------------------------//
    // INNER CLASSES
    //------------------------------------------------------------------------//
    
    // La toolbar
    private final class ToolBar extends JToolBar
    {
        private JButton save   = createButton( new Save()   );
        private JButton saveAs = createButton( new SaveAs() );
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
            add( saveAs );
            addSeparator();
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
                this.saveAs.getAction().setEnabled( true );
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
                this.saveAs.getAction().setEnabled( false );
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
    
    public static void main( String[] asArg )
    {
        Notes notes = new Notes();

        DesktopFrame df = new DesktopFrame( "Notes" );
                     df.setFrameIcon( Runtime.getIcon( notes, "images/notes.png", 16,16 ) );
                     df.add( notes );
                     df.pack();
        
        Runtime.getDesktop().add( df );
        df.center();   // After it was added to the desktop
        
        /*javax.swing.JDesktopPane desktop = new javax.swing.JDesktopPane();
                                 desktop.add( df, new Integer( 2 ) );
                                 
        df.setLocation( 180,90 );
        df.setVisible( true );
                     
        javax.swing.JFrame frame = new javax.swing.JFrame( "NotePad Test" );
                           frame.setContentPane( desktop );
                           frame.setSize( 1024, 768 );
                           frame.setDefaultCloseOperation( javax.swing.JFrame.EXIT_ON_CLOSE );
                           frame.setVisible( true );*/
    }
}