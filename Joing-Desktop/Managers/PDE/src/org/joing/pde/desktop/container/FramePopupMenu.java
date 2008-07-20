/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.joing.pde.desktop.container;

import java.awt.Container;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import org.joing.common.desktopAPI.pane.DeskFrame;
import org.joing.common.desktopAPI.workarea.WorkArea;

/**
 * PopupMenu for both: Frame and PDEFrame (JInterbalFrame).
 * This class is also used by FrameButton (in FrameList).
 * 
 * @author Francisco Morero Peyrona
 */
public class FramePopupMenu extends JPopupMenu
{
    private final static String WORK_AREA = "WORK_AREA";
    
    private Frame    frame  = null;
    private PDEFrame iframe = null;
    
    //------------------------------------------------------------------------//
    
    public FramePopupMenu( Frame frame )
    {
        this.frame = frame;
        init();
    }
    
    public FramePopupMenu( PDEFrame iframe )
    {
        this.iframe = iframe;
        init();
    }
    
    public Container getFrame()
    {
        return (frame == null) ? iframe : frame;
    }
    
    //------------------------------------------------------------------------//
    
    private void minimize()
    {
        if( getFrame() instanceof Frame )
        {
            Frame frm = (Frame) getFrame();
                  frm.setExtendedState( frm.getExtendedState() | Frame.ICONIFIED );
        }
        else
        {
            ((PDEFrame) getFrame()).setStatus( DeskFrame.Status.MINIMIZED );
        }
    }
    
    private void maximize()
    {
        if( getFrame() instanceof Frame )
        {
            Frame frm    = (Frame) getFrame();
                  frm.setExtendedState( frm.getExtendedState() | Frame.MAXIMIZED_BOTH );
        }
        else
        {
            ((PDEFrame) getFrame()).setStatus( DeskFrame.Status.MAXIMIZED );
        }
    }
    
    private void restore()
    {
        if( getFrame() instanceof Frame )
        {
            Frame frm = (Frame) getFrame();
                  frm.setExtendedState( frm.getExtendedState() | Frame.NORMAL );
        }
        else
        {
            ((PDEFrame) getFrame()).setStatus( DeskFrame.Status.RESTORED );
        }
    }
    
    private void close()
    {
        if( getFrame() instanceof Frame )
            ((Frame) getFrame()).dispose();
        else
            ((PDEFrame) getFrame()).dispose();
    }
    
    private void alwaysOnTop()
    {
        if( getFrame() instanceof Frame )
        {
            Frame frm = (Frame) getFrame();
                  frm.setAlwaysOnTop( ! frm.isAlwaysOnTop() );
        }
        else
        {
            PDEFrame frm = (PDEFrame) getFrame();
                     frm.setAlwaysOnTop( ! frm.isAlwaysOnTop() );
        }
    }
    
    private void toWorkArea( WorkArea waDestiny )
    {
        JOptionPane.showMessageDialog( null, "Option not yet implemented" );
        // TODO: Hacerlo bien: esto no funciona -->
        /*WorkArea waOrigin = org.joing.jvmm.RuntimeFactory.getPlatform().getDesktopManager().getDesktop().getActiveWorkArea();
        
        waOrigin.remove( (DeskComponent) getFrame() );
        waDestiny.add(   (DeskComponent) getFrame() );*/
    }
    
    private void init()
    {
        JMenuItem item;

        item = new JMenuItem( "Minimize" );
        item.setEnabled( frame != null ? frame.getExtendedState() != Frame.ICONIFIED : ! iframe.isIcon() );
        item.addActionListener( new ActionListener() 
        {
            public void actionPerformed( ActionEvent ae )  { minimize(); }
        } );
        add( item );

        item = new JMenuItem( "Maximize" );
        item.setEnabled( frame != null ? frame.getExtendedState() != Frame.MAXIMIZED_BOTH : ! iframe.isMaximum() );
        item.addActionListener( new ActionListener() 
        {
            public void actionPerformed( ActionEvent ae )  { maximize(); }
        } );
        add( item );

        item = new JMenuItem( "Restore" );
        item.setEnabled( frame != null ? frame.getExtendedState() != Frame.NORMAL : (iframe.isMaximum() || FramePopupMenu.this.iframe.isIcon()) );
        item.addActionListener( new ActionListener() 
        {
            public void actionPerformed( ActionEvent ae )  { restore(); }
        } );
        add( item );

        item = new JMenuItem( "Close" );
        item.addActionListener( new ActionListener() 
        {
            public void actionPerformed( ActionEvent ae )  { close(); }
        } );
        add( item );

        addSeparator();

        JCheckBoxMenuItem itemCheck = new JCheckBoxMenuItem( "Always on top" );
                          itemCheck.setEnabled( frame != null ? frame.isAlwaysOnTopSupported() : true );
                          itemCheck.setSelected( frame != null ? frame.isAlwaysOnTop() : iframe.isAlwaysOnTop() );
                          itemCheck.addActionListener( new ActionListener() 
                          {
                              public void actionPerformed( ActionEvent ae )  { alwaysOnTop(); }
                          } );                          
        add( itemCheck );

        List<WorkArea> lstWorkAreas = org.joing.jvmm.RuntimeFactory.getPlatform().getDesktopManager().getDesktop().getWorkAreas();
        WorkArea       waActive     = org.joing.jvmm.RuntimeFactory.getPlatform().getDesktopManager().getDesktop().getActiveWorkArea();

        if( lstWorkAreas.size() > 1 )
        {
            JMenu menu = new JMenu( "Move to desktop..." );

            for( WorkArea wa : lstWorkAreas )
            {
                item = new JMenuItem( wa.getName() );
                item.putClientProperty( WORK_AREA, wa );
                item.setEnabled( wa != waActive );
                item.addActionListener( new ActionListener() 
                {
                    public void actionPerformed( ActionEvent ae )  
                    {
                        JMenuItem item      = (JMenuItem) ae.getSource();
                        WorkArea  waDestiny = (WorkArea) item.getClientProperty( WORK_AREA ); 

                        toWorkArea( waDestiny );                        
                    }
                } );    

                menu.add( item );
            }

            add( menu );
        }
    }
}