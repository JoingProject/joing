package com.binfactory;

import java.awt.Color;
import java.awt.Component;
import java.awt.DefaultKeyboardFocusManager;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.KeyEventDispatcher;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

final class KeyPanel extends JPanel
{
	private JButton btn7          = new StandardButton( "7" );
	private JButton btn8          = new StandardButton( "8" );
	private JButton btn9          = new StandardButton( "9" );
	private JButton btn4          = new StandardButton( "4" );
	private JButton btn5          = new StandardButton( "5" );
	private JButton btn6          = new StandardButton( "6" );
	private JButton btn1          = new StandardButton( "1" );
	private JButton btn2          = new StandardButton( "2" );
	private JButton btn3          = new StandardButton( "3" );
	private JButton btn0          = new StandardButton( "0" );
	private JButton btnDecSep     = new StandardButton( "." );
	private JButton btnCopy       = new StandardButton( null );
	private JButton btnClearEntry = new StandardButton( "CE" );
	private JButton btnClear      = new StandardButton( "C" );
	private JButton btnAdd        = new StandardButton( "+" );
	private JButton btnSubstract  = new StandardButton( "-" );
	private JButton btnMultiply   = new StandardButton( "*" );
	private JButton btnDivide     = new StandardButton( "/" );
	private JButton btnBack       = new StandardButton( null );
	private JButton btnPercent    = new StandardButton( "%" );
	private JButton btnSign       = new StandardButton( "+/-" );
	private JButton btnEnter      = new StandardButton( "=" );
    
    //------------------------------------------------------------------------//
    
	KeyPanel()
	{
		jbInit();
        createKeyListener();
	}
    
    public void addActionListener( ActionListener l )
    {
        listenerList.add( ActionListener.class, l );
    }
    
    public void removeActionListener( ActionListener l )
    {
        listenerList.remove( ActionListener.class, l );
    }
    
	protected void fireActionPerformed( ActionEvent e )
	{
	    // Guaranteed to return a non-null array
        Object[] listeners = listenerList.getListenerList();
        
        // Process the listeners last to first, notifying those that are interested in this event
        for( int i = listeners.length - 2; i >= 0; i -= 2 )
        {
            if( listeners[i] == ActionListener.class )
                ((ActionListener) listeners[i + 1]).actionPerformed( e );
        }
	}

    //------------------------------------------------------------------------//
    
	// Keyboard support
    private void createKeyListener()
    {
        DefaultKeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(
            new KeyEventDispatcher()
            {
                public boolean dispatchKeyEvent( KeyEvent ke )
                {
                    final String sBasicKeys = "0123456789.+-*/%=";
                    boolean bConsumed = false;
                    
                    if( ke.getID() == KeyEvent.KEY_TYPED )
                    {
                        char c = ke.getKeyChar();
                        
                        if( sBasicKeys.indexOf( c ) > -1 )
                        {
                            executeButtonFromCommand( String.valueOf( c ) );
                            bConsumed = true;
                        }
                        else
                        {
                            switch( c )
                            {
                                case ','                   : executeButtonFromCommand( "."  ); break;
                                case KeyEvent.VK_ENTER     : executeButtonFromCommand( "="  ); break;
                                case KeyEvent.VK_BACK_SPACE: executeButtonFromCommand( "BS" ); break;
                                case KeyEvent.VK_ESCAPE    : executeButtonFromCommand( "C"  ); break;  // clear
                            }
                        }
                    }
                    else if( ke.isActionKey() )
                    {
                        if( ke.getKeyCode() == KeyEvent.VK_F10 )
                            executeButtonFromCommand( "CE" );   // Clear Entry
                        else if( ke.getKeyCode() == KeyEvent.VK_F6 )
                            executeButtonFromCommand( "+/-" );  // Sign changer
                    }
                    else
                    {
                        if( ke.isControlDown() && ke.getKeyCode() == 'C' && ke.getID() == KeyEvent.KEY_RELEASED ) 
                            executeButtonFromCommand( "CY" );   // Copy
                    }
                    
                    return bConsumed;   // Key event not processed
                }
            } ); 
    }
    
    private void executeButtonFromCommand( String sCommand )
    {
        Component[] aComp = getComponents();
        
        // Both ',' and '.' are accepted as decimal separator
        if( sCommand.equals( "," ) )
            sCommand = ".";
        
        for( int n = 0; n < aComp.length; n++ )
        {
            if( aComp[n] instanceof JButton && ((JButton) aComp[n]).getActionCommand().equals( sCommand ) )
            {
                ((JButton) aComp[n]).doClick( 130 );
                break;
            }
        }
    }
    
	private void jbInit()
	{
		btnCopy.setToolTipText( "Copy [Ctrl-C]" );
		btnCopy.setActionCommand( "CY" );
		btnCopy.setIcon( new ImageIcon( getClass().getResource( "images/copy.png" ) ) );
		
		btnClearEntry.setForeground( Color.red );
        btnClearEntry.setToolTipText( "F10" );
        
        btnClear.setForeground( Color.red );
		btnClear.setToolTipText( "Esc" );
        
        btnAdd.setFont( new Font( "Monospaced", Font.BOLD, 12 ) );

		btnSubstract.setFont( new Font( "Monospaced", Font.BOLD, 12 ) );

        btnMultiply.setFont( new Font( "Monospaced", Font.BOLD, 12 ) );

        btnDivide.setFont( new Font( "Monospaced", Font.BOLD, 12 ) );

        btnBack.setFont( new Font( "Monospaced", Font.BOLD, 12 ) );
        btnBack.setToolTipText( "Backspace" );
        btnBack.setActionCommand( "BS" );
        btnBack.setIcon( new ImageIcon( getClass().getResource( "images/back.gif" ) ) );

        btnPercent.setFont( new Font( "Monospaced", Font.BOLD, 12 ) );
		
		btnSign.setFont( new Font( "Monospaced", Font.PLAIN, 9 ) );
        btnSign.setToolTipText( "Sign [Ctrl-F6]" );

        btnEnter.setFont( new Font( "Monospaced", Font.BOLD, 12 ) );
        
        setLayout( new GridBagLayout() );
        add(btn7,  new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
				,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(3, 3, 3, 3), 0, 0));
		add(btn8, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0
				,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(3, 3, 3, 3), 0, 0));
		add(btn9, new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0
				,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(3, 3, 3, 3), 0, 0));
		add(btn4, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0
				,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(3, 3, 3, 3), 0, 0));
		add(btn5, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0
				,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(3, 3, 3, 3), 0, 0));
		add(btn6, new GridBagConstraints(2, 2, 1, 1, 0.0, 0.0
				,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(3, 3, 3, 3), 0, 0));
		add(btn1, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0
				,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(3, 3, 3, 3), 0, 0));
		add(btn2, new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0
				,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(3, 3, 3, 3), 0, 0));
		add(btn3, new GridBagConstraints(2, 3, 1, 1, 0.0, 0.0
				,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(3, 3, 3, 3), 0, 0));
		add(btn0, new GridBagConstraints(0, 4, 2, 1, 0.0, 0.0
				,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(3, 3, 3, 3), 41, 0));
		add(btnDecSep, new GridBagConstraints(2, 4, 1, 1, 0.0, 0.0
				,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(3, 3, 3, 3), 0, 0));
		add(btnCopy, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
				,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(3, 3, 10, 3), 0, 0));
		add(btnClearEntry, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
				,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(3, 3, 10, 3), 0, 0));
		add(btnClear, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0
				,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(3, 3, 10, 3), 0, 0));
		add(btnAdd, new GridBagConstraints(3, 4, 1, 1, 0.0, 0.0
				,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(3, 9, 3, 3), 0, 0));
		add(btnSubstract, new GridBagConstraints(3, 3, 1, 1, 0.0, 0.0
				,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(3, 9, 3, 3), 0, 0));
		add(btnMultiply, new GridBagConstraints(3, 2, 1, 1, 0.0, 0.0
				,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(3, 9, 3, 3), 0, 0));
		add(btnDivide, new GridBagConstraints(3, 1, 1, 1, 0.0, 0.0
				,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(3, 9, 3, 3), 0, 0));
		add(btnBack, new GridBagConstraints(3, 0, 2, 1, 0.0, 0.0
				,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(3, 10, 10, 3), 0, 0));
		add(btnPercent, new GridBagConstraints(4, 1, 1, 1, 0.0, 0.0
				,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(3, 3, 3, 3), 0, 0));
		add(btnSign, new GridBagConstraints(4, 2, 1, 1, 0.0, 0.0
				,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(3, 3, 3, 3), 0, 0));
		add(btnEnter, new GridBagConstraints(4, 3, 1, 2, 0.0, 0.0
				,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(3, 3, 3, 3), 0, 40));
	}
    
    //------------------------------------------------------------------------//
    // INNER CLASSES
    //------------------------------------------------------------------------//
    
    private final class StandardButton extends JButton
    {
        private StandardButton( String sText )
        {
            super( sText );

            setActionCommand( sText );
            setMaximumSize( new Dimension( 61, 61 ) );
            setMinimumSize( new Dimension( 21, 21 ) );
            setPreferredSize( new Dimension( 31, 31 ) );
            setFocusPainted( false );
            setMargin( new Insets( 4, 4, 4, 4 ) );
            addActionListener( new java.awt.event.ActionListener()
            {
                public void actionPerformed( ActionEvent ae )
                {
                    KeyPanel.this.fireActionPerformed( ae );
                }
            } );
        }
    } 
}