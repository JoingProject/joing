/*
 * PDEWorkAreaPopupMenu.java
 *
 * Created on 29 de septiembre de 2007, 15:08
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.joing.pde.desktop.workarea;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import org.joing.common.desktopAPI.StandardImage;
import org.joing.common.desktopAPI.workarea.WorkArea;
import org.joing.pde.desktop.deskwidget.deskLauncher.PDEDeskLauncher;
import org.joing.pde.desktop.deskwidget.deskLauncher.PDEDeskLauncherPropertiesPanel;
import org.joing.swingtools.JoingSwingUtilities;
import org.joing.pde.swing.FrameAcceptCancel;

/**
 * 
 * @author Francisco Morero Peyrona
 */
class PDEWorkAreaPopupMenu extends JPopupMenu implements ActionListener
{
    private WorkArea waParent;
    private Point    ptWhere;
    
    //------------------------------------------------------------------------//
    
    PDEWorkAreaPopupMenu( WorkArea waParent, Point ptWhere )
    {
        this.waParent = waParent;
        this.ptWhere  = ptWhere;
        
        add( createMenuItem( "Create folder"  , StandardImage.FOLDER    , "NEW_FOLDER"   ) );
        add( createMenuItem( "Create launcher", StandardImage.LAUNCHER  , "NEW_LACUNHER" ) );
        addSeparator();
        add( createMenuItem( "Align to grid"  , null                    , "TOGGLE_ALIGN" ) );
        addSeparator();
        add( createMenuItem( "Preferences"    , StandardImage.PROPERTIES, "PROPERTIES"   ) );
        
        List<WorkArea> lstWorkAreas = org.joing.jvmm.RuntimeFactory.getPlatform().getDesktopManager().getDesktop().getWorkAreas();
        WorkArea       waActive     = org.joing.jvmm.RuntimeFactory.getPlatform().getDesktopManager().getDesktop().getActiveWorkArea();
        
        if( lstWorkAreas.size() > 1 )
        {
            JMenu menu = new JMenu( "Switch to desktop..." );

            for( WorkArea wa : lstWorkAreas )
            {
                JMenuItem item = new JMenuItem( wa.getName() );
                          item.setActionCommand( "SWITCH_WORKAREA" );
                          item.addActionListener( this );
                          item.putClientProperty( "WORK_AREA", wa );
                          item.setEnabled( wa != waActive );

                menu.add( item );
            }

            add( menu );
        }
    }

    private JMenuItem createMenuItem( String sText, StandardImage image, String sCommand )
    {
        JMenuItem item = new JMenuItem( sText );
                  item.setActionCommand( sCommand );
                  item.addActionListener( this );
      
        ImageIcon icon = null;
        
        if( image != null )
            icon = new ImageIcon( org.joing.jvmm.RuntimeFactory.getPlatform().getDesktopManager().getRuntime().getImage( image, 16, 16 ) );
        else
            icon = JoingSwingUtilities.getIcon( this, "images/grid", 16, 16 );
                  
        item.setIcon( icon );
        
        return item;
    }

    public void actionPerformed( ActionEvent ae )
    {
        JMenuItem menuItem = (JMenuItem) ae.getSource();
        String    sCommand = menuItem.getActionCommand();
        
        if(      sCommand.equals( "SWITCH_WORKAREA" ) ) switchToWorkArea( menuItem );
        else if( sCommand.equals( "NEW_FOLDER"      ) ) createLauncher( true );
        else if( sCommand.equals( "NEW_LACUNHER"    ) ) createLauncher( false );
        else if( sCommand.equals( "TOGGLE_ALIGN"    ) ) toggleAlign();
        else if( sCommand.equals( "PROPERTIES"      ) ) editProperties();
    }
    
    private void switchToWorkArea( JMenuItem item )
    {
        WorkArea waTarget = (WorkArea) item.getClientProperty( "WORK_AREA" );
        org.joing.jvmm.RuntimeFactory.getPlatform().getDesktopManager().getDesktop().setActiveWorkArea( waTarget ); 
    }
    
    private void createLauncher( boolean bDir )
    {
        PDEDeskLauncherPropertiesPanel panel = new PDEDeskLauncherPropertiesPanel( bDir );
        
        if( org.joing.jvmm.RuntimeFactory.getPlatform().getDesktopManager().getRuntime().showAcceptCancelDialog( "Create new Launcher", panel ) )
        {
            PDEDeskLauncher launcher = panel.createLauncher();
                            launcher.setLocation( ptWhere );
                            
            org.joing.jvmm.RuntimeFactory.getPlatform().getDesktopManager().getDesktop().getActiveWorkArea().add( launcher );
        }
    }
    
    private void toggleAlign()
    {
        // TODO: hacerlo
        org.joing.jvmm.RuntimeFactory.getPlatform().getDesktopManager().getRuntime().showMessageDialog( null, "Option not yet implemented" );
    }
    
    private void editProperties()
    {
        FrameAcceptCancel frame = new FrameAcceptCancel( "WorkArea Preferences", 
                                                         new PDEWorkAreaProperties( waParent ) )
        {
            public void onAccept()
            {
                org.joing.jvmm.RuntimeFactory.getPlatform().getDesktopManager().getRuntime().showMessageDialog( null, "Option not yet implemented" );
                // TODO: Actualizarlo
                super.onAccept();
            }
        };
        
        frame.setResizable( false );
        org.joing.jvmm.RuntimeFactory.getPlatform().getDesktopManager().getDesktop().getActiveWorkArea().add( frame );
    }
}