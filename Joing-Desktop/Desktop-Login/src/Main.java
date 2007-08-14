import javax.swing.JFrame;
import org.joing.desktop.login.LoginGUIManager;
import org.joing.desktop.login.LoginListener;
import org.joing.desktop.login.WelcomePanel;
/*
 * Main.java
 *
 * Created on 30 de junio de 2007, 06:20 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

/**
 *
 * @author Mario Serrano
 */
public class Main {
    
    /** Creates a new instance of Main */
    public Main() {
        JFrame frame = new JFrame("Login");
        frame.setSize(800,600);
        frame.add(LoginGUIManager.createWelcomePanel());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        
        LoginGUIManager.setLoginListener(new MyLoginListener());
        
    }
    public static void main(String[] args) {
        new Main();
    }
    
    class MyLoginListener implements LoginListener{
       
        public void onLogin(String username, String password) {
            System.out.println("Login user...");
            System.out.println("Username is "+username);
            System.out.println("Password is "+password);
            System.out.println("Invoking AppLauncher...");
            System.out.println("Fuck off.");
        }
        
    }
}
