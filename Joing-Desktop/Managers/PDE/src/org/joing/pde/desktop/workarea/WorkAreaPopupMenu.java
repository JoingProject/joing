/*
 * WorkAreaPopupMenu.java
 *
 * Created on 29 de septiembre de 2007, 15:08
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.joing.pde.desktop.workarea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import org.joing.common.desktopAPI.DesktopFactory;
import org.joing.common.desktopAPI.workarea.WorkArea;

/**
 * 
 * @author Francisco Morero Peyrona
 */
class WorkAreaPopupMenu extends JPopupMenu implements ActionListener
{
    private WorkArea waParent;
    
    WorkAreaPopupMenu( WorkArea waParent )
    {
        this.waParent = waParent;
        
        add( createMenuItem( "Create folder"  , "folder"    , "NEW_FOLDER"   ) );
        add( createMenuItem( "Create launcher", "launcher"  , "NEW_LACUNHER" ) );
        addSeparator();
        add( createMenuItem( "Align to grid"  , "grid"      , "TOGGLE_ALIGN" ) );
        addSeparator();
        add( createMenuItem( "Properties"     , "properties", "PROPERTIES"   ) );

        List<WorkArea> lstWorkAreas = DesktopFactory.getDM().getDesktop().getWorkAreas();
        WorkArea       waActive     = DesktopFactory.getDM().getDesktop().getActiveWorkArea();
        
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
                icon = DesktopFactory.getDM().getRuntime().getIcon( this, "images/"+ sIconName +".png", 16, 16 );
            else
                icon = DesktopFactory.getDM().getRuntime().getIcon( null, sIconName +".png", 16, 16 );

            item.setIcon( icon );
        }
        
        return item;
    }

    public void actionPerformed( ActionEvent ae )
    {
        JMenuItem menuItem = (JMenuItem) ae.getSource();
        String    sCommand = menuItem.getActionCommand();
        
        if(      sCommand.equals( "SWITCH_WORKAREA" ) ) switchToWorkArea( menuItem );
        // TODO: implementarlos todos
        else if( sCommand.equals( "NEW_FOLDER"      ) ) createFolder();
        else if( sCommand.equals( "NEW_LACUNHER"    ) ) createLauncher();
        else if( sCommand.equals( "TOGGLE_ALIGN"    ) ) toggleAlign();
        else if( sCommand.equals( "PROPERTIES"      ) ) editProperties();
    }
    
    private void switchToWorkArea( JMenuItem item )
    {
        WorkArea waTarget = (WorkArea) item.getClientProperty( "WORK_AREA" );
        DesktopFactory.getDM().getDesktop().setActiveWorkArea( waTarget ); 
    }
    
    private void createFolder()
    {
        // TODO: hacerlo
        DesktopFactory.getDM().getRuntime().showMessage( "Option not yet implemented" );
    }
    
    private void createLauncher()
    {
        // TODO: hacerlo
        DesktopFactory.getDM().getRuntime().showMessage( "Option not yet implemented" );
    }
    
    private void toggleAlign()
    {
        // TODO: hacerlo
        DesktopFactory.getDM().getRuntime().showMessage( "Option not yet implemented" );
    }
    
    private void editProperties()
    {
        // TODO: hacerlo
        DesktopFactory.getDM().getRuntime().showMessage( "Option not yet implemented" );
    }
}