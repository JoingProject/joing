/*
 * Main.java
 *
 * Created on 10-jul-2007, 12:28:15
 *
 * Author: Francisco Morero Peyrona.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or (at
 * your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 */
package org.joing;

import org.joing.applauncher.Bootstrap;

/**
 *
 * @author fmorero
 * @author Antonio Varela Lizardi <antonio@icon.net.mx>
 */
public class Main {

    public Main() {
    }

    public static void main(String[] args) {

//        args = new String[0];  // TODO: quitarlo
//        
//        Properties clientProps = new Properties();
//        // arg 1 can be the properties file.
//        if (args.length == 1) {
//            try {
//                File propFile = new File(args[0]);
//                if (propFile.exists() == false) {
//                    System.err.println("Could not find the specified properties file.");
//                    return;
//                }
//
//                FileInputStream fis = new FileInputStream(propFile);
//                clientProps.load(fis);
//            } catch (FileNotFoundException fnfe) {
//                System.err.println("FileNotFoundException caught: " + fnfe.getMessage());
//                return;
//            } catch (IOException ioe) {
//                System.err.println("IOException caught: " + ioe.getMessage());
//                return;
//            }
//        }

        // Init no forma parte del contrato, por eso invocamos directamente
        // una instancia de PlatformImpl. Cuando quede resuelto el asunto
        // de las ClientProperties se debera eliminar esto la llamada a
        // platform.init(clientProps).
        //final Platform platform = RuntimeFactory.getPlatform();
//        final Platform platform = (Platform)RuntimeFactory.getPlatform();
//        platform.init(clientProps);

        Bootstrap.init();
    }
}
