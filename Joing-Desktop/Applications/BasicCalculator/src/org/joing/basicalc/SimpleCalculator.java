package org.joing.basicalc;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.text.NumberFormat;

import javax.swing.JButton;
import javax.swing.JPanel;

/**
 * Implements a simple calculator.
 * <p>
 * It used by Calculator class (a JSplitPane) to show the right pane.
 *
 * Copyright (c) CHS 2001
 * @author Francisco Morero / Antonio Bernal
 * @version 1.0
 */
class SimpleCalculator extends JPanel implements ActionListener
{
	private final int nPERCENT = 99999;    // It does not exists among the KeyEvent.VK_

	private KeyPanel keys    = new KeyPanel();
	private Display  display = new Display();

	private boolean bStarted   = false;    // true after first digit is entered
	private double  nTotal     = 0.0;
	private char    cOperator  = ' ';
	private double  nOperand   = 0.0;
	private boolean bEqual     = false;
	private boolean bShowEqual = false;

	//-------------------------------------------------------------------------//

	public SimpleCalculator()
	{
            init();

            keys.addActionListener( this );
	}

	public void actionPerformed( ActionEvent evt )
	{
            JButton btn   = (JButton) evt.getSource();
            String  sText = btn.getText();
            int     nKey  = -1;
            char    cKey  = ' ';

            if( sText == null || sText.length() == 0 )    // Button has not text (i.e. copy btn)
                     sText = btn.getActionCommand();

            if(      sText.equals( "0" ) )    { nKey = KeyEvent.VK_0;          cKey = '0'; }
            else if( sText.equals( "1" ) )    { nKey = KeyEvent.VK_1;          cKey = '1'; }
            else if( sText.equals( "2" ) )    { nKey = KeyEvent.VK_2;          cKey = '2'; }
            else if( sText.equals( "3" ) )    { nKey = KeyEvent.VK_3;          cKey = '3'; }
            else if( sText.equals( "4" ) )    { nKey = KeyEvent.VK_4;          cKey = '4'; }
            else if( sText.equals( "5" ) )    { nKey = KeyEvent.VK_5;          cKey = '5'; }
            else if( sText.equals( "6" ) )    { nKey = KeyEvent.VK_6;          cKey = '6'; }
            else if( sText.equals( "7" ) )    { nKey = KeyEvent.VK_7;          cKey = '7'; }
            else if( sText.equals( "8" ) )    { nKey = KeyEvent.VK_8;          cKey = '8'; }
            else if( sText.equals( "9" ) )    { nKey = KeyEvent.VK_9;          cKey = '9'; }
            else if( sText.equals( "." ) )    { nKey = KeyEvent.VK_PERIOD;     cKey = '.'; }
            else if( sText.equals( "CY" ) )   { nKey = KeyEvent.VK_COPY;       cKey = ' '; }
            else if( sText.equals( "CE" ) )   { nKey = KeyEvent.VK_F10;        cKey = ' '; }
            else if( sText.equals( "C" ) )    { nKey = KeyEvent.VK_ESCAPE;     cKey = ' '; }
            else if( sText.equals( "BS" ) )   { nKey = KeyEvent.VK_BACK_SPACE; cKey = ' '; }
            else if( sText.equals( "%" ) )    { nKey = nPERCENT;               cKey = '%'; }
            else if( sText.equals( "/" ) )    { nKey = KeyEvent.VK_DIVIDE;     cKey = '/'; }
            else if( sText.equals( "*" ) )    { nKey = KeyEvent.VK_MULTIPLY;   cKey = '*'; }
            else if( sText.equals( "-" ) )    { nKey = KeyEvent.VK_SUBTRACT;   cKey = '-'; }
            else if( sText.equals( "+" ) )    { nKey = KeyEvent.VK_ADD;        cKey = '+'; }
            else if( sText.equals( "+/-" ) )  { nKey = KeyEvent.VK_F9;         cKey = ' '; }
            else if( sText.equals( "=" ) )    { nKey = KeyEvent.VK_ENTER;      cKey = '='; }

            processKey( nKey, cKey );
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
    
    private void processKey( int nKey, char cKey )
    {
                      switch( nKey )
        {
            case KeyEvent.VK_0:
            case KeyEvent.VK_1:
            case KeyEvent.VK_2:
            case KeyEvent.VK_3:
            case KeyEvent.VK_4:
            case KeyEvent.VK_5:
            case KeyEvent.VK_6:
            case KeyEvent.VK_7:
            case KeyEvent.VK_8:
            case KeyEvent.VK_9:
                if( !this.bStarted )
                {
                    this.bStarted = true;
                    this.display.setAmount( cKey );
                }
                else
                {
                    this.display.addDigit( cKey );
                }
                break;

            case KeyEvent.VK_BACK_SPACE:
                if( this.bStarted )   //Solo borra si se han introducido digitos
                {
                    this.display.delete();
                }
                break;

            case KeyEvent.VK_ADD:
                if( this.bStarted || this.bEqual )   // No se puede empezar introduciendo un operador
                {
                    perform( '+', this.display.getAmount() );
                    this.bEqual = false;
                }
                break;

            case KeyEvent.VK_SUBTRACT:
                if( this.bStarted || this.bEqual )   // No se puede empezar introduciendo un operador
                {
                    perform( '-', this.display.getAmount() );
                    this.bEqual = false;
                }
                break;

            case KeyEvent.VK_MULTIPLY:
                if( this.bStarted || this.bEqual )   // No se puede empezar introduciendo un operador
                {
                    perform( '*', this.display.getAmount() );
                    this.bEqual = false;
                }
                break;

            case KeyEvent.VK_DIVIDE:
                if( this.bStarted || this.bEqual )   // No se puede empezar introduciendo un operador
                {
                    perform( '/', this.display.getAmount() );
                    this.bEqual = false;
                }
                break;

            case KeyEvent.VK_PERIOD:
                this.display.addDigit( cKey );
                break;

            case nPERCENT:
                if( this.bStarted || this.bEqual )   // No se puede empezar introduciendo un operador
                {
                    perform( '%', this.display.getAmount() );
                    this.bEqual = false;
                }
                break;

            case KeyEvent.VK_F9:        // +/-
                if( this.bStarted )   // No se puede empezar introduciendo un operador
                {
                    this.display.setAmount( (this.display.getAmount() * -1) );
                }
                break;

            case KeyEvent.VK_ESCAPE:      // C
                reset();
                fireActionPerformed( new ActionEvent( this, hashCode(), "RESET" ) );
                break;

            case KeyEvent.VK_F10:      // CE
                if( this.bStarted && this.cOperator != ' ' )
                {
                    this.display.setAmount( 0 );
                }
                break;

            case KeyEvent.VK_ENTER:    // =
                this.bShowEqual = true;
                if( this.bStarted || !this.bEqual )   // No se puede empezar introduciendo un operador
                {
                    perform( this.cOperator, this.display.getAmount() );
                    this.bEqual = true;
                }
                else if( this.bEqual )
                {
                    this.bEqual = false;
                    perform( this.cOperator, this.nOperand );
                    this.bEqual = true;
                }
                break;

            case KeyEvent.VK_COPY:
                this.display.copy();
        }
    }

    /**
     * Performs an operation
     * @param cOp
     */
    private void perform( char cOp , double nOper )
    {
    NumberFormat nf = NumberFormat.getInstance();
                 nf.setMinimumFractionDigits( 2 );
                 nf.setMaximumFractionDigits( 4 );

        if( this.bShowEqual )
        {
                fireActionPerformed( new ActionEvent( this, hashCode(), nf.format( nOper ) +" =" ) );
                this.bShowEqual = false;
        }
        else
        {
                fireActionPerformed( new ActionEvent( this, hashCode(), nf.format( nOper ) +' '+ cOp ) );
        }

        this.nOperand = nOper;

        if( this.cOperator != ' ' && this.bEqual == false )
        {
            switch( this.cOperator )
            {
                case '+':
                        this.nTotal += nOper;
                        break;

                case '-':
                        this.nTotal -= nOper;
                        break;

                case '*':
                        this.nTotal *= nOper;
                        break;

                case '/':
                        this.nTotal /= nOper;
                        break;

                case '%':
                        this.nTotal = ( this.nTotal / 100 ) * nOper;
                        break;
            }

            this.cOperator = cOp;
            this.display.setAmount( this.nTotal );
            this.bStarted  = false;
        }
        else
        {
            this.nTotal    = this.display.getAmount();
            this.cOperator = cOp;
            this.bStarted  = false;
        }
    }

    private void reset()
    {
        this.bStarted  = false;
        this.nTotal    = 0.0;
        this.cOperator = ' ';
        this.bEqual    = false;
        this.nOperand  = 0.0;
        this.display.reset();
    }

    private void init()
    {
        setLayout( new BorderLayout() );

        add( keys   , BorderLayout.CENTER );
        add( display, BorderLayout.NORTH );
    }
}