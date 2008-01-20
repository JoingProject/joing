/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.joing.jvmm;

import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;
import org.joing.common.clientAPI.log.JoingLogger;
import org.joing.common.clientAPI.log.Levels;
import org.joing.common.clientAPI.log.Logger;
import org.joing.common.clientAPI.log.SimpleLoggerFactory;
import org.joing.common.clientAPI.runtime.Bridge2Server;
import org.joing.common.dto.app.Application;
import org.joing.jvmm.net.BridgeURLConnection;
import org.joing.jvmm.net.URLFormat;
import org.joing.runtime.bridge2server.ApplicationCache;

/**
 * Class loader needed to find Classes from the server bridge.
 * 
 * @author Antonio Varela Lizardi <antonio@icon.net.mx>
 */
public class BridgeClassLoader extends ClassLoader {

    private Bridge2Server bridge;

    private final ApplicationCache cache = new ApplicationCache();
    private final Logger logger = SimpleLoggerFactory.getLogger(JoingLogger.ID);

    private URL[] classPath;

    public BridgeClassLoader(Bridge2Server bridge, URL[] classPath) {
        this.bridge = bridge;
        this.classPath = classPath;
    }

    public BridgeClassLoader(Bridge2Server bridge, URL[] classPath,
            ClassLoader parent) {
        super(parent);
        this.bridge = bridge;
        this.classPath = classPath;
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {

        StringBuilder sb = new StringBuilder("Class Requested: ");
        sb.append(name);

        String className = convertName(name);

        byte[] b = loadBytes(className);

        if (b == null) {
            sb.append(" - Not Found.");
            logger.write(Levels.DEBUG_JVMM, sb.toString());
            throw new ClassNotFoundException("Unable to find Class" + name);
        }

        sb.append(" - Found.");
        logger.write(Levels.DEBUG_JVMM, sb.toString());

        try {
            return defineClass(name, b, 0, b.length);
        } catch (ClassFormatError cfe) {
            this.logger.debugJVMM("Exception Caught: {0}", cfe.getMessage());
            throw cfe;
        }
    }

    @Override
    protected URL findResource(String name) {

        StringBuilder sb = new StringBuilder();
        sb.append("Resource Requested: ").append(name);

        // Es necesario utilizar un metodo mas eficiente
        // para buscar un jar entry.
        try {
            URL tmpUrl = null;

            for (URL url : classPath) {
                BridgeURLConnection uc =
                        (BridgeURLConnection) url.openConnection();
                
                Application app = uc.getApplication();

                byte[] b = findInApp(name, app);

                if (b != null) {
                    tmpUrl = URLFormat.toURL(app, name);
                    break;
                }
            }

            if (tmpUrl == null) {
                sb.append(", not found.");
                logger.write(Levels.DEBUG_JVMM, sb.toString());
            } else {
                sb.append(", found.");
                logger.write(Levels.DEBUG_JVMM, sb.toString());
            }

            return tmpUrl;

        } catch (IOException ioe) {
            logger.write(Levels.CRITICAL,
                    "IOException caught in findResource: {0}",
                    ioe.getMessage());
            sb.append(", not found.");
            logger.write(Levels.DEBUG_JVMM, sb.toString());
            return null;
        }
    }

    // TODO: Terminar de implementar este metodo.
    @Override
    protected Enumeration<URL> findResources(String name) throws IOException {
        return super.findResources(name);
    }

    private byte[] loadBytes(String name) {

        byte[] b = null;

        try {
            for (URL url : classPath) {
                BridgeURLConnection buc =
                        (BridgeURLConnection) url.openConnection();
                Application app = buc.getApplication();
                b = findInApp(name, app);
                if (b != null) {
                    break;
                }
            }
        } catch (IOException ioe) {
            b = null;
        }

        return b;
    }

    /**
     * Find the entry bytes in the Jar file represented in App.
     * 
     * @param entryName Name of the class/resource.
     * @param app Application instance.
     * @return byte array with class data, or null.
     * @throws IOException generated by internal calls to JarInputStream.
     */
    public static byte[] findInApp(String entryName, Application app)
            throws IOException {

        byte[] b = null;

        // The byte array managed by Application represents a
        // jar file. We must look into the data for the specific
        // class data. 
        JarInputStream jar = new JarInputStream(app.getContent());

        boolean done = false;
        while (!done) {
            JarEntry entry = jar.getNextJarEntry();
            if (entry == null) {
                done = true;
                jar.closeEntry();
                continue;
            }

            if (entry.isDirectory()) {
                jar.closeEntry();
                continue;
            }

            if (entry.getName().equals(entryName) == false) {
                jar.closeEntry();
                continue;
            }

            int size = new Long(entry.getSize()).intValue();
            int cSize = new Long(entry.getCompressedSize()).intValue();
            b = new byte[size];
            jar.read(b, 0, size);

            done = true;
        }
        
        jar.close();

        return b;
    }

    private String convertName(String entryName) {

        StringBuilder sb = new StringBuilder(entryName.replace(".", "/"));
        sb.append(".class");
        return sb.toString();
    }

}
