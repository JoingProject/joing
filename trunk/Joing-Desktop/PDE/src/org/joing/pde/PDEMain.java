/*
 * PDEMain.java
 * 
 * Created on Sep 27, 2007, 11:53:09 PM
 * 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.joing.pde;

/**
 * Hack (Marranada) para pruebas con el Desktop.
 * 
 * @author Antonio Varela Lizardi <antonio@icon.net.mx>
 */
public class PDEMain {

    public PDEMain() {
    }

    public static void main(String[]args) {
        
        boolean showInFrame = true;
        
        if (args.length == 1) {
            if ("showInFrame=false".equalsIgnoreCase(args[0])) {
                showInFrame = false;
            }
        }
        PDEManager manager = new PDEManager();
        if (showInFrame) {
            manager.showInFrame();
        } else {
            manager.showInFullScreen();
        }
    }
}
