/*
 * Main.java
 *
 * Created on 17 de junio de 2007, 02:48 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package desktoptest;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.UIManager;
import net.infonode.gui.laf.InfoNodeLookAndFeel;
import net.infonode.gui.laf.InfoNodeLookAndFeelThemes;
import org.joing.desktop.api.Application;
import org.joing.desktop.api.Desktop;
import org.joing.desktop.api.Wallpaper;
import org.joing.desktop.enums.IconType;
import org.joing.desktop.impl.DefaultDesktop;
import org.joing.desktop.impl.DefaultLauncher;
import org.joing.desktop.impl.DefaultWallpaper;
import org.joing.taskbar.api.TaskBar;
import org.joing.taskbar.impl.DefaultTaskBar;
import org.joing.taskbar.impl.DefaultTaskBarPanel;

/**
 *
 * @author mario
 */
public class Main {
    
    /** Creates a new instance of Main */
    public Main() {
        
        JFrame frame = new JFrame("Desktop Test");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800,600);
        frame.setUndecorated(true);
        
        
        Desktop desk = new DefaultDesktop();
        createWallpaper(desk);        
        createLaunchers(desk);       
        createMenus(desk);
        
        Application app = new DemoApp();
        desk.getCurrentWorkArea().addApplication(app);
               
        frame.add((DefaultDesktop)desk);
        frame.setVisible(true);
    }

    private void createWallpaper(final Desktop desk) {
        Wallpaper wp = new DefaultWallpaper();
        wp.setSource(getClass().getResource("/desktoptest/abstract_003.jpg"));        
        desk.getCurrentWorkArea().setWallpaper(wp);
    }

    private void createLaunchers(final Desktop desk) {
        
        DefaultLauncher launcher = new DefaultLauncher("Equipo");
        launcher.setIconResource("/desktoptest/laptop.png",IconType.ICON32);
        launcher.setBounds(100,100,100,100);
        
        DefaultLauncher launcher2 = new DefaultLauncher("Documentos");
        launcher2.setIconResource("/desktoptest/folder_red.png",IconType.ICON32);
        launcher2.setBounds(100,220,100,100);
        
        desk.getCurrentWorkArea().addLauncher(launcher);
        desk.getCurrentWorkArea().addLauncher(launcher2);
    }

    private void createMenus(final Desktop desk) {
        
        TaskBar taskBar = desk.getTaskBars().get(1);
        JMenuBar mb = new JMenuBar();        
        JMenu menuApp = new JMenu("Aplicaciones");                
        menuApp.add("Accesorios");
        menuApp.add("Oficina");
        menuApp.add("Internet");
        menuApp.addSeparator();
        menuApp.add("Añadir / Quitar");
        menuApp.addSeparator();               
        mb.add(menuApp);
        
        JMenu menuLug = new JMenu("Lugares");        
        menuLug.add("Carpeta Persona");
        menuLug.add("Escritorio");
        menuLug.addSeparator();
        menuLug.add("Creador de DVD");        
        menuLug.add("SLAVE");
        menuLug.add("Red");
        menuLug.addSeparator();
        menuLug.add("Documentos Recientes");        
        mb.add(menuLug);
        
        JMenu menuSis = new JMenu("Sistema");
        menuSis.add("Preferencias");
        menuSis.add("Administracion");
        mb.add(menuSis);
        
        DefaultTaskBarPanel panel = new DefaultTaskBarPanel();
        panel.addJComponent(mb);         
        taskBar.addPanel(panel);
        
        DefaultTaskBar taskBar2 = (DefaultTaskBar) desk.getTaskBars().get(0);
        taskBar2.add(new JButton("WebPC"));
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {            
            
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        new Main();
    }
    
}
