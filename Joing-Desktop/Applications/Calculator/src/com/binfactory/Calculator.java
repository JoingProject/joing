/*
 * (c) Telco Domótica S.L.
 * Todos los derechos reservados.
 * 
 * Creado el 11-ago-2005 a las 2:50:13
 */

package com.binfactory;

import java.awt.BorderLayout;

import com.binfactory.client.desktop.DesktopFrame;
import com.binfactory.client.runtime.Runtime;

public class Calculator extends DesktopFrame
{
    public Calculator()
    {
        super( "Calculator" );
        
        setFrameIcon( Runtime.getIcon( this, "calculator.png", 22,22 ) );
        
        setLayout( new BorderLayout( 0, 8 ) );
        add( new CalculatorPanel() );
        
        pack();
        
        Runtime.getDesktop().add( this );
    }
    
    public static void main( String[] asArg )
    {
        new Calculator();
    }
}