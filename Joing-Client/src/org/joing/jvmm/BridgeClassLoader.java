/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.joing.jvmm;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
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

    private static final int BLOCKSIZE = 8192;
    
    private Bridge2Server bridge;

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

        try {
            // Parent delegation
            return super.findClass(name);
        } catch (ClassNotFoundException cnfe) {
            
        }
        
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
        logger.debugJVMM(sb.toString());

        try {
            return defineClass(name, b, 0, b.length);
        } catch (ClassFormatError cfe) {
            this.logger.debugJVMM("Exception Caught: {0}", cfe.getMessage());
            throw cfe;
        }
    }

    @Override
    protected URL findResource(String name) {

        // Parent delegation
        URL tmpUrl = super.findResource(name);
        if (tmpUrl != null) {
            return tmpUrl;
        }
        
        StringBuilder sb = new StringBuilder();
        sb.append("Resource Requested: ").append(name);
        JarSearchResult jsr = null;

        // Es necesario utilizar un metodo mas eficiente
        // para buscar un jar entry.
        try {

            for (URL url : classPath) {
                URLConnection uc = url.openConnection();
                
                byte[] b = null;
                Application app = null;
                
                if (uc instanceof BridgeURLConnection) {
                    BridgeURLConnection buc = (BridgeURLConnection)uc;
                    app = buc.getApplication();
                    jsr = findInJar(name, app.getContent(), true);
                    if (jsr.isFound()) {
                        tmpUrl = URLFormat.toURL(app, name);
                        break;
                    }
                } else {
                    jsr = findInJar(name, uc.getInputStream(), true);
                    if (jsr.isFound()) {
                        StringBuilder jurl = new StringBuilder("jar:");
                        jurl.append(uc.getURL().toString());
                        jurl.append("!/").append(name);
                        tmpUrl = new URL(jurl.toString());
                        break;
                    }
                }
            }

            if (tmpUrl == null) {
                sb.append(", not found.");
                logger.debugJVMM(sb.toString());
            } else {
                sb.append(", found.");
                logger.debugJVMM(sb.toString());
            }

            return tmpUrl;

        } catch (IOException ioe) {
            logger.critical("IOException caught in findResource: {0}",
                    ioe.getMessage());
            sb.append(", not found.");
            logger.debugJVMM(sb.toString());
            return null;
        }
    }

    // TODO: Terminar de implementar este metodo.
    @Override
    protected Enumeration<URL> findResources(String name) throws IOException {
        
        List<URL> urlList = null;

        // Parent delegation
        try {
            Enumeration<URL> e = super.findResources(name);
            urlList = Collections.list(e);
        } catch (IOException ioe) {

        }

        if ((urlList != null) && (urlList.size() > 0)) {
            return Collections.enumeration(urlList);
        }
         
        urlList = new ArrayList<URL>();
        JarSearchResult jsr = null;

        try {

            for (URL url : classPath) {
                URLConnection uc = url.openConnection();
                
                if (uc instanceof BridgeURLConnection) {
                    BridgeURLConnection buc = (BridgeURLConnection)uc;
                    Application app = buc.getApplication();
                    jsr = findInJar(name, app.getContent(), true);
                    if (jsr.isFound()) {
                        urlList.add(URLFormat.toURL(app, name));
                    }
                } else {
                    jsr = findInJar(name, uc.getInputStream(), true);
                    if (jsr.isFound()) {
                        StringBuilder jurl = new StringBuilder("jar:");
                        jurl.append(uc.getURL().toString());
                        jurl.append("!/").append(name);
                        urlList.add(new URL(jurl.toString()));
                    }
                }
            }

            return Collections.enumeration(urlList);

        } catch (IOException ioe) {
            logger.critical("IOException caught in findResource: {0}",
                    ioe.getMessage());
            return null;
        }
        
    }

    /**
     * Load resource data from the internally stored Class Path.
     * @param name Name of the resource.
     * @return Array of bytes.
     */
    private byte[] loadBytes(String name) {

        byte[] b = null;
        JarSearchResult jsr = null;

        try {
            for (URL url : classPath) {
                URLConnection uc = url.openConnection();
                if (uc instanceof BridgeURLConnection) {
                    BridgeURLConnection buc =
                            (BridgeURLConnection) url.openConnection();
                    Application app = buc.getApplication();
                    jsr = findInJar(name, app.getContent(), false);
                } else {
                    jsr = findInJar(name, uc.getInputStream(), false);
                }
                
                if (jsr.isFound()) {
                    b = jsr.getBytes();
                    break;
                }
            }
        } catch (IOException ioe) {
            logger.debugJVMM("IOException in loadBytes(): {0}",
                    ioe.getMessage());
            b = null;
        }

        return b;
    }

    
    /**
     * Find the entry bytes in the Jar file supplied in the argument.
     * 
     * @param entryName Name of the class/resource.
     * @param input InputStream with the raw data. This InputStream
     * can be obtained with URLConnection.openStream() or with the
     * Application internal InputStream represented by Application.getContent().
     * @param findOnly If true, it wont load the resource data in the
     * result. Useful with findResource(), because it only needs to return
     * a URL instead a full stream of bytes.
     * @return JarSearchResult instance.
     * @throws IOException generated by internal calls to JarInputStream.
     */
    public static JarSearchResult findInJar(String entryName, 
            InputStream input, boolean findOnly) throws IOException {

        JarSearchResult jsr = new JarSearchResult(entryName, null, false);

        // The byte array managed by Application represents a
        // jar file. We must look into the data for the specific
        // class data. 
        JarInputStream jar = new JarInputStream(input);

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

            if (findOnly == false) {
                boolean readDone = false;
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                while (!readDone) {
                    byte[] tmp = new byte[BLOCKSIZE];
                    int r = jar.read(tmp);
                    if (r == -1) {
                        readDone = true;
                    } else {
                        baos.write(tmp, 0, r);
                    }
                }

                byte[] b = baos.toByteArray();
                jsr = new JarSearchResult(entryName, b, true);
            } else {
                jsr = new JarSearchResult(entryName, null, true);
            }
            
            jar.closeEntry();
            done = true;
        }
        
        jar.close();

        return jsr;
    }

    private String convertName(String entryName) {

        StringBuilder sb = new StringBuilder(entryName.replace(".", "/"));
        sb.append(".class");
        return sb.toString();
    }

}
