/*
 * Copyright (c) 2002 CHS Data Systems GmbH, All Rights Reserved.
 *
 * Permission to use, copy and modify; but NOT to distribute this source code
 * either for NON-COMMERCIAL or COMMERCIAL purposes without the explicit
 * written permission of CHS Data Systems GmbH.
 *
 * Chs makes no representations or warranties about the suitability of
 * the software, either express or implied, including but not limited
 * to the implied warranties of merchantability, fitness for a
 * particular purpose, or non-infringement. Chs shall not be liable for
 * any damages suffered by licensee as a result of using, modifying or
 * distributing this software or its derivatives.
 */
package com.binfactory;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JSplitPane;

/**
 * This class implements a simple calculator pane.
 *
 * Copyright (c) CHS 2001
 * @author Francisco Morero
 * @version 1.0
 */
class CalculatorPanel extends JSplitPane implements ActionListener
{
	private SimpleCalculator calc  = new SimpleCalculator();
    private PaperPanel       paper = new PaperPanel();

	/**
	 * Zero argument constructor
	 */
	public CalculatorPanel()
	{
         jbInit();
         
         this.calc.addActionListener( this );
	}

	/**
	 * Forced by implementing ActionListener
	 * @param op action event
	 */
	// I use this event to save creating a new event type (OperationPerformedEvent)
	public void actionPerformed( ActionEvent op )
	{
		String sCmd = op.getActionCommand();

		if( sCmd.equals( "RESET" ) )
			this.paper.reset();
		else
			this.paper.addLine( sCmd );
	}

	private void jbInit()
	{
		setLeftComponent( paper );
		setRightComponent( calc );
		setDividerLocation(110);
		setDividerSize( 4 );
	}
}