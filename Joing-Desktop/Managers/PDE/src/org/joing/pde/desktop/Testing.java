package org.joing.pde.desktop;

import java.awt.BorderLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.joing.common.desktopAPI.desktop.Desktop;
import org.joing.common.desktopAPI.workarea.Wallpaper;
import org.joing.pde.desktop.container.PDEFrame;
import org.joing.pde.desktop.deskwidget.deskLauncher.PDEDeskLauncher;
import org.joing.pde.desktop.workarea.PDEWallpaper;

// JUST FOR TESTING
import org.joing.runtime.swap.JoingColorChooser;
import org.joing.runtime.swap.JoingFileChooser;

//------------------------------------------------------------------------//

class Testing
{
    private static Desktop desktop = org.joing.jvmm.RuntimeFactory.getPlatform().getDesktopManager().getDesktop();
    
    public static void createTestComponents()
    {
        SwingUtilities.invokeLater( new Runnable()
        {
            public void run()
            {
//                wallPapers();
//                launchers();
//                InternalFrames();
//                translucency();
//                desklets();
//                fileChooser();
//                colorChooser();
            }
        } );
    }
    
    //------------------------------------------------------------------------//
    
    private static void wallPapers()
    {
        Wallpaper wp0 = desktop.getWorkAreas().get( 0 ).getWallpaper();
        
        if( wp0 == null )
            wp0 = new PDEWallpaper();
        
        wp0.setImage( new ImageIcon( "/home/fmorero/Imágenes/iconos/duke/starwars.png" ).getImage() );
        desktop.getWorkAreas().get( 0 ).setWallpaper( wp0 );
        
        Wallpaper wp1 = desktop.getWorkAreas().get( 1 ).getWallpaper();
        
        if( wp1 == null )
            wp1 = new PDEWallpaper();
        
        wp1.setImage( new ImageIcon( "/home/fmorero/Imágenes/reflections.jpg" ).getImage() );
        desktop.getWorkAreas().get( 1 ).setWallpaper( wp1 );
    }
    
    private static void launchers()
    {
        PDEDeskLauncher launcher = new PDEDeskLauncher();
                        launcher.setText( "Launcher test" );
                        launcher.setLocation( 20,80 );
        org.joing.jvmm.RuntimeFactory.getPlatform().getDesktopManager().getDesktop().getWorkAreas().get( 0 ).add( launcher );
    }
    
    private static void InternalFrames()
    {
        PDEFrame frame = (PDEFrame) org.joing.jvmm.RuntimeFactory.getPlatform().getDesktopManager().getRuntime().createFrame();
                 frame.setTitle( "Frame" );
                 frame.add( new JTextArea( "Soy un Frame" ) );
        desktop.getActiveWorkArea().add( frame );
    }
    
    private static void translucency()
    {
        JSlider slrTranslucency = new JSlider( JSlider.HORIZONTAL, 0, 100, 0 );
                slrTranslucency.setMajorTickSpacing( 10 );
                slrTranslucency.setPaintLabels( true );
                slrTranslucency.setPaintTicks( true );
                slrTranslucency.addChangeListener( new ChangeListener() 
                {
                    public void stateChanged( ChangeEvent ce )
                    {
                        JSlider slr = (JSlider) ce.getSource();
                        
                        PDEFrame frm = (PDEFrame) SwingUtilities.getAncestorOfClass( PDEFrame.class, slr );
                                 frm.setTranslucency( slr.getValue() );
                    }
                } );
                    
        PDEFrame frm = new PDEFrame();
                 frm.setTitle( "Example Join'g Frame" );
                 frm.add( new JLabel( "Translucency" ), BorderLayout.NORTH );
                 frm.add( slrTranslucency, BorderLayout.SOUTH );
                 frm.setBounds( 380, 200, 600, 580 );
        org.joing.jvmm.RuntimeFactory.getPlatform().getDesktopManager().getDesktop().getWorkAreas().get( 0 ).add( frm, false ); // Do not autoarrange
    }
    
    private static void desklets()
    {/*
        NasaPhoto nasa = new NasaPhoto();
                  nasa.setBounds( 10,350, nasa.getPreferredSize().width, nasa.getPreferredSize().height );
        wa.add( nasa );
        
        JLabel lblCanvas = new JLabel( "<html><h2>Soy un canvas.</h2>Y esto es <u>texto <font color=\"#0066CC\">HTML</font></u>.</h3></html>" );
        PDECanvas canvas = new PDECanvas();
                  canvas.add( lblCanvas );
                  canvas.setBounds( 330, 130, 240, 80 );
        wa.add( canvas );*/
    }
    
    private static void fileChooser()
    {
        JoingFileChooser jfc = new JoingFileChooser();
        
        //int nSelection = jfc.showDialog( null, null );
        int nSelection = jfc.showSaveDialog( null );
        
        if( nSelection == JoingFileChooser.APPROVE_OPTION )
            System.out.println( jfc.getSelectedFile().getClass().getName() +" ["+ jfc.getSelectedFile() +"]");
    }
    
    private static void colorChooser()
    {
        JoingColorChooser jcc = new JoingColorChooser();
        
        //Color color = jcc.showDialog( (Component) null, "Joing Color Chooser", null );
        
        
    }
}