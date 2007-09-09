/*
 * WorkAreaLauncher.java
 *
 * Created on 10 de febrero de 2007, 21:01
 *
 * (c) 2006 - Francisco Morero Peyrona
 *
 * License: {license}
 */

package org.joing.pde.desktop.workarea.items;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.image.FilteredImageSource;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;
import javax.swing.MenuElement;
import javax.swing.border.EmptyBorder;
import org.joing.desktop.api.Launcher;
import org.joing.pde.RuntimeImpl;
import org.joing.pde.desktop.LauncherImpl;
import org.joing.pde.desktop.swing.ColorSchema;
import org.joing.pde.desktop.swing.ImageHighlightFilter;
import org.joing.pde.desktop.swing.JRoundLabel;

/**
 * This is a "visual" Launcher: placed on the desk
 * 
 * @author Francisco Morero Peyrona
 */
public class WorkAreaLauncher 
       extends WorkAreaComponent
{
    private Launcher launcher;
    
    //------------------------------------------------------------------------//
    
    public WorkAreaLauncher()
    {
        this( new LauncherImpl() );
    }

    public WorkAreaLauncher( Launcher launcher )
    {
        super();
        
        if( launcher.getName() == null )
            launcher.setName( "No name" ); 
        
        if( launcher.getIconResource() == null )
            launcher.setIconResource( "images/launcher.png" );
        
        setLauncher( launcher );
    }
    
    //------------------------------------------------------------------------//
    
    public Launcher getLauncher()
    {
        return launcher;
    }
    
    public void setLauncher( Launcher launcher )
    {
        Image image = new ImageIcon( launcher.getIconResource() ).getImage();
        
        if( image.getWidth( this ) != 48 || image.getHeight( this ) != 48 )
            image = image.getScaledInstance( 48, 48, Image.SCALE_SMOOTH );
        
        setImage( image );
        setText( launcher.getName() );
        repaint();
        
        this.launcher = launcher;
    }
    
    //------------------------------------------------------------------------//
    // METODOS DEFINIDOS EN LA SUPER CLASS
    
    public Object clone()
    {
        return new WorkAreaLauncher( ((LauncherImpl) launcher).clone() );
    }
    
    public void open()
    {
        launcher.execute();
        super.open();
    }
    
    public void delete()
    {
        /*if( Runtime.getRuntime().confirmDialog( "Delete launcher", "Are you sure you want to delete it?" ) )
        {
            StartMenuWorker.getInstance().removeLauncher( appDesc );
            super.delete();
        }*/
    }
    
    public void remove()
    {
        /*if( Runtime.confirmDialog( "Remove launcher", "Are you sure you want to remove it?" ) )
        {
            // TODO mandarlo a la papelera
            StartMenuWorker.getInstance().removeLauncher( appDesc );
            super.remove();
        }*/
    }
    
    public void showProperties()
    {
        /*DialogProperties dlg = new DialogProperties();
                         dlg.pack();
                         dlg.setVisible( true );
        
        Launcher newLauncher = getLauncher().clone();
                 newLauncher.setName( dlg.txtName.getText() );
                 newLauncher.setJarName( dlg.txtLaunchs.getText() );
                 newLauncher.setIcon( (ImageIcon) dlg.btnIcon.getIcon() );
        
        dlg.dispose();
        
        StartMenuWorker.getInstance().changeLauncher( getLauncher(), newLauncher );

        super.showProperties();*/
    }
    
    @Override
    protected void doRename( String sNewName )
    {
        /*Launcher newLauncher = getLauncher().clone();
                 newLauncher.setName( sNewName );
        
        StartMenuWorker.getInstance().changeLauncher( getLauncher(), newLauncher );
                        
        getLauncher().setName( sNewName );
        this.label.setText( sNewName );*/
    }
    
    //------------------------------------------------------------------------//
    // INNER CLASSES
    //------------------------------------------------------------------------//
    
    // El popup del DesktopLauncher
    private class PopupMenu extends JPopupMenu implements ActionListener
    {
        private PopupMenu()
        {
            add( createMenuItem( "Open"       , "launcher"   , OPEN   ) );
            addSeparator();
            add( createMenuItem( "To trashcan", "to_trashcan", REMOVE ) );
            add( createMenuItem( "Delete"     , "delete"     , DELETE ) );
            addSeparator();
            add( createMenuItem( "Rename"     , null         , RENAME ) );
            addSeparator();
            add( createMenuItem( "Properties" , "properties" , PROPS  ) );
        }
        
        private JMenuItem createMenuItem( String sText, String sIconName, String sCommand )
        {
            JMenuItem item = new JMenuItem( sText );
                      item.setActionCommand( sCommand );
                      item.addActionListener( this );
                      
            if( sIconName != null )
                item.setIcon( RuntimeImpl.getRuntime().getIcon( this, "images/"+ sIconName +".png", 16, 16 ) );
            
            return item;
        }
        
        public void show( Component invoker, int x, int y )
        {
            MenuElement[] me = getSubElements();
            
            ((JMenuItem) me[0]).setEnabled( getLauncher().getCommand() != null );
            
            super.show( invoker, x, y );
        }
        
        public void actionPerformed( ActionEvent ae )
        {
            String sCommand = ((JMenuItem) ae.getSource()).getActionCommand();
            
            if(      sCommand.equals( OPEN   ) )  WorkAreaLauncher.this.open();
            else if( sCommand.equals( DELETE ) )  WorkAreaLauncher.this.delete();
            else if( sCommand.equals( REMOVE ) )  WorkAreaLauncher.this.remove();
            else if( sCommand.equals( RENAME ) )  WorkAreaLauncher.this.rename();
            else if( sCommand.equals( PROPS  ) )  WorkAreaLauncher.this.showProperties();
        }
    }
    
    //------------------------------------------------------------------------//
    
    /*private final class DialogProperties extends DesktopDialog
    {
        private JTextField txtName    = new JTextField( WorkAreaLauncher.this.getLauncher().getName() );
        private JTextField txtLaunchs = new JTextField( WorkAreaLauncher.this.getLauncher().getJarName() );
        private JButton    btnIcon    = new JButton( WorkAreaLauncher.this.icon.getIcon() );
        
        private DialogProperties()
        { // TODO se ve feo y el botón para elegir icono no hace nada
            super( "Desktop Launcher Properties" );
            
            JPanel pnl = new JPanel( new GridLayout( 3, 2, 0, 8 ) );
                   pnl.add( new JLabel( "Name" ) );
                   pnl.add( txtName );
                   pnl.add( new JLabel( "Launchs" ) );
                   pnl.add( txtLaunchs );
                   pnl.add( new JLabel( "Icon" ) );
                   pnl.add( btnIcon );
            
            JOptionPane op = new JOptionPane( pnl, JOptionPane.PLAIN_MESSAGE, 
                                              JOptionPane.OK_CANCEL_OPTION, null );
            setPane( op );
            pack();
        }
    }*/
}