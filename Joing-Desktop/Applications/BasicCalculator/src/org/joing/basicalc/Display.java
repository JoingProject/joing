package org.joing.basicalc;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Insets;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;

/**
 * This class acts as a calculator display.
 * <p>
 * It has seberal methdos to perform needed actions: clear(), getAmount(), ...
 *
 * Copyright (c) CHS 2001
 * @author Francisco Morero
 * @version 1.0
 */
class Display extends JPanel
{
	private JTextField   txt = new JTextField();
	private NumberFormat nf  =  NumberFormat.getInstance();
	private char         cDec;   // Decimal separator

	/**
	 * Zero argumento constructor.
	 */
	Display()
	{
		jbInit();

        this.nf.setMaximumFractionDigits( 0 );
		this.nf.setMaximumFractionDigits( 4 );

		DecimalFormatSymbols dfs = new DecimalFormatSymbols();
		this.cDec   = dfs.getDecimalSeparator();
	}

	/**
	 * Copy the amount in the display to the clipboard.
	 */
	void copy()
	{
		this.txt.selectAll();
		this.txt.copy();
		this.txt.setSelectionStart( 0 );
		this.txt.setSelectionEnd( 0 );
	}

	/**
	 * Return display's content padded with needed '0' (after decimal separator)
	 * to the amount determined by this.nf.getMaximumFractionDigits()
	 *
	 * @return Display's content padded with needed '0'.
	 */
	String getTextPadded()
	{
		String sText   = this.txt.getText();
		int    nDec;
		int    nMaxDec = this.nf.getMaximumFractionDigits();

		if( sText.indexOf( this.cDec ) == -1 )
		{
			nDec = nMaxDec;
			sText += this.cDec;
		}
		else
		{
			nDec = nMaxDec - sText.substring( sText.indexOf( this.cDec ) + 1 ).length();
		}

		for( int n = 0; n < nDec; n++ )
			sText += '0';

		return sText;
	}

	/**
	 * Return display's amount as double.
	 *
	 * @return display's amount as double.
	 */
	double getAmount()
	{
		double nRet = 0.0;

		try
		{
			nRet = this.nf.parse( this.txt.getText() ).doubleValue();
		}
		catch( ParseException exc )
		{
			exc.printStackTrace();
		}

		return nRet;
	}

	/**
	 * Clears display and set a new amount using passed parameter.
	 *
	 * @param cDigit New amount to be displayed.
	 */
	void setAmount( char cDigit )
	{
		setAmount( Double.parseDouble( String.valueOf( cDigit ) ) );
	}

	/**
	 * Clears display and set a new amount using passed parameter.
	 *
	 * @param nAmount New amount to be displayed.
	 */
	void setAmount( double nAmount )
	{
		this.txt.setText( this.nf.format( nAmount ) );
	}

	/**
	 * Add a new digit to the current displayed amount.
	 *
	 * @param cKey Digit to be added.
	 */
	void addDigit( char cKey )
	{
		if( cKey == '.' || cKey == ',' )
		{
			if( this.txt.getText().indexOf( this.cDec ) == -1 )
				this.txt.setText( this.txt.getText() + this.cDec );
		}
		else
		{
			// Si se está introduciendo la parte decimal de la cantidad
			// hay q permitir poner ceros a la derecha, 'else', podemos
			// hacer el 'setAmount'.
			if( this.txt.getText().indexOf( this.cDec ) > -1 && cKey == '0' )
			{
				if( ( this.txt.getText().length() - this.txt.getText().indexOf(this.cDec) ) < 3 )
					this.txt.setText( this.txt.getText() + '0' );
			}
			else
			{
				try
				{
					setAmount( this.nf.parse( this.txt.getText() + cKey ).doubleValue() );
				}
				catch( ParseException exc )
				{
					exc.printStackTrace();
				}
			}
		}
	}

	/**
	 * Removes last added digit from displayed amount.
	 */
	void delete()
	{
		String sText = this.txt.getText();

		sText = sText.substring( 0, sText.length() - 1 );

		if( sText.length() == 0 || (sText.length() == 1 && sText.charAt( 0 ) == this.cDec ) )
		{
			this.txt.setText( "0" );
		}
		else
		{
			try
			{
				setAmount( this.nf.parse( sText.toString() ).doubleValue() );
			}
			catch( ParseException exc )
			{
				exc.printStackTrace();
			}
		}
	}

	/**
	 * Set display to 0.
	 */
	void reset()
	{
		this.txt.setText( "0" );
	}

    //------------------------------------------------------------------------//
    
	private void jbInit()
	{
		txt.setBackground( Color.white );
        txt.setFont( new java.awt.Font( "Monospaced", 1, 14 ) );
        txt.setBorder( new CompoundBorder( new EtchedBorder( EtchedBorder.RAISED ), new EmptyBorder( 2,2,2,2 ) ) );
        txt.setDisabledTextColor( Color.white );
        txt.setEditable( false );
        txt.setMargin( new Insets( 3, 9, 3, 9 ) );
        txt.setText( "0" );
        txt.setHorizontalAlignment( SwingConstants.RIGHT );

        setLayout( new BorderLayout() );
        setBorder( BorderFactory.createEmptyBorder( 10, 10, 10, 10 ) );
        add( txt, BorderLayout.CENTER );
	}
}