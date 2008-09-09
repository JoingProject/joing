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

package org.joing.kernel.swingtools;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JCheckBox;

/**
 *
 * @author Francisco Morero Peyrona
 */
// TODO: Terminar esto y usarlo en los checkbox que sea necesario en JFilePropertiesPanel.java
public class JoingCheckBox extends JCheckBox
{
    private boolean bEditable = true;
    

    public JoingCheckBox( String text, Icon icon, boolean selected )
    {
        super( text, icon, selected );
    }

    public JoingCheckBox( String text, Icon icon )
    {
        super( text, icon );
    }

    public JoingCheckBox( String text, boolean selected )
    {
        super( text, selected );
    }

    public JoingCheckBox( Action a )
    {
        super( a );
    }

    public JoingCheckBox( String text )
    {
        super( text );
    }

    public JoingCheckBox( Icon icon, boolean selected )
    {
        super( icon, selected );
    }

    public JoingCheckBox( Icon icon )
    {
        super( icon );
    }

    public JoingCheckBox()
    {
    }
    
    public boolean isEditable()
    {
        return bEditable;
    }
    
    public void setEditable( boolean b )
    {
        bEditable = b;
        firePropertyChange( "editable", ! b, b );
    }
}