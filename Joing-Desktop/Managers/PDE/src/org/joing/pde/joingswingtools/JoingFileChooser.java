/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.joing.pde.joingswingtools;

import org.joing.runtime.vfs.*;
import java.awt.Component;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileSystemView;
import org.joing.common.desktopAPI.DeskComponent;
import org.joing.common.desktopAPI.DesktopManager;
import org.joing.common.desktopAPI.pane.DeskDialog;

/**
 * An implementation (by inheriting from JFileChooser) that can be used inside
 * a <code>org.joing.common.desktopAPI.pane.DeskWindow</code> (because it
 * implements <code>org.joing.common.desktopAPI.DeskComponent</code>).
 * 
 * @author Francisco Morero Peyrona.
 */
public class JoingFileChooser extends JFileChooser implements DeskComponent
{
    private int returnValue = ERROR_OPTION;
    
    //------------------------------------------------------------------------//
    
    public JoingFileChooser()
    {
        this( (File) null, (FileSystemView) null );
    }

    /**
     * Constructs a <code>JFileChooser</code> using the given path.
     * Passing in a <code>null</code>
     * string causes the file chooser to point to the user's default directory.
     * This default depends on the operating system. It is
     * typically the "My Documents" folder on Windows, and the user's
     * home directory on Unix.
     *
     * @param currentDirectoryPath  a <code>String</code> giving the path
     *				to a file or directory
     */
    public JoingFileChooser( String currentDirectoryPath )
    {
        this( currentDirectoryPath, (FileSystemView) null );
    }

    /**
     * Constructs a <code>JFileChooser</code> using the given <code>File</code>
     * as the path. Passing in a <code>null</code> file
     * causes the file chooser to point to the user's default directory.
     * This default depends on the operating system. It is
     * typically the "My Documents" folder on Windows, and the user's
     * home directory on Unix.
     *
     * @param currentDirectory  a <code>File</code> object specifying
     *				the path to a file or directory
     */
    public JoingFileChooser( File currentDirectory )
    {
        this( currentDirectory, (FileSystemView) null );
    }

    /**
     * Constructs a <code>JFileChooser</code> using the given
     * <code>FileSystemView</code>.
     */
    public JoingFileChooser( FileSystemView fsv )
    {
        this( (File) null, fsv );
    }

    /**
     * Constructs a <code>JFileChooser</code> using the given current directory
     * and <code>FileSystemView</code>.
     */
    public JoingFileChooser( File currentDirectory, FileSystemView fsv )
    {
        super( currentDirectory,
               ((fsv == null) ? JoingFileSystemView.getFileSystemView() : fsv) );
    }

    /**
     * Constructs a <code>JFileChooser</code> using the given current directory
     * path and <code>FileSystemView</code>.
     */
    public JoingFileChooser( String currentDirectoryPath, FileSystemView fsv )
    {
        super( currentDirectoryPath,
               ((fsv == null) ? JoingFileSystemView.getFileSystemView() : fsv) );
    }
    
    //------------------------------------------------------------------------//
    
    public void setFileSystemView( FileSystemView fsv )
    {
        if( fsv == null )
            fsv = JoingFileSystemView.getFileSystemView();
        
        super.setFileSystemView( fsv );
    }
    
    public int showDialog( Component parent, String approveButtonText )
           throws HeadlessException
    {
        DesktopManager dm     = org.joing.jvmm.RuntimeFactory.getPlatform().getDesktopManager();
        String         sTitle = getDialogTitle();
        
        if( sTitle == null )
            sTitle = getUI().getDialogTitle( this );
        
        if( approveButtonText != null )
        {
	    setApproveButtonText( approveButtonText );
	    setDialogType( CUSTOM_DIALOG );
	
        }
        
        final DeskDialog dialog = dm.getRuntime().createDialog();
                         dialog.setTitle( sTitle );
                         dialog.add( this );
                         dialog.setLocationRelativeTo( (DeskComponent) parent );
                         ((Component) dialog).setComponentOrientation( getComponentOrientation() );
        
	returnValue = ERROR_OPTION;
	rescanCurrentDirectory();
        
        // Add listener for accept and cancel events
        addActionListener( new ActionListener() 
        {
            public void actionPerformed( ActionEvent ae )
            {
                if( JFileChooser.APPROVE_SELECTION.equals( ae.getActionCommand() ) )
                    returnValue = JFileChooser.APPROVE_OPTION;
                else if( JFileChooser.CANCEL_SELECTION.equals( ae.getActionCommand() ) )
                    returnValue = JFileChooser.CANCEL_OPTION;
                
                dialog.close();
            }
        } );
        
        dm.getDesktop().getActiveWorkArea().add( dialog );
        
        firePropertyChange( "JFileChooserDialogIsClosingProperty", dialog, null );
        dialog.close();
	return returnValue;
    }
    
    //------------------------------------------------------------------------//
    
    protected void setup( FileSystemView fsv )
    {
        if( fsv == null )
            fsv = JoingFileSystemView.getFileSystemView();
        
        super.setup( fsv );
    }
}