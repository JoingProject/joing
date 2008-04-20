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
import org.joing.common.desktopAPI.workarea.WorkArea;
import org.joing.pde.PDEUtilities;
import org.joing.pde.desktop.deskwidget.deskLauncher.PDEDeskLauncher;
import org.joing.pde.desktop.deskwidget.deskLauncher.PDEDeskLauncherPropertiesPanel;
import org.joing.pde.misce.images.ImagesFactory;

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
        
        add( createMenuItem( "Create folder"  , "folder"    , "NEW_FOLDER"   ) );
        add( createMenuItem( "Create launcher", "launcher"  , "NEW_LACUNHER" ) );
        addSeparator();
        add( createMenuItem( "Align to grid"  , "grid"      , "TOGGLE_ALIGN" ) );
        addSeparator();
        add( createMenuItem( "Properties"     , "properties", "PROPERTIES"   ) );
        
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

    private JMenuItem createMenuItem( String sText, String sIconName, String sCommand )
    {
        JMenuItem item = new JMenuItem( sText );
                  item.setActionCommand( sCommand );
                  item.addActionListener( this );
      
        if( sIconName != null )
        {
            ImageIcon icon = null;

            if( sIconName.equals( "grid" ) )
                icon = PDEUtilities.getIcon( this, "images/"+ sIconName, 16, 16 );
            else
                icon = PDEUtilities.getStandardIcon( ImagesFactory.getIcon( sIconName ), 16, 16 );

            item.setIcon( icon );
        }
        
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
        
        if( PDEUtilities.showBasicDialog( "Create new Launcher", panel ) )
        {
            PDEDeskLauncher launcher = panel.retrieveLauncher();
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
        PDEWorkAreaProperties panel = new PDEWorkAreaProperties( waParent );
        
        if( PDEUtilities.showBasicDialog( "WorkArea Preferences", panel ) )
        {
            // TODO: Actualizarlo y salvarlo a fichero
        }
    }
}