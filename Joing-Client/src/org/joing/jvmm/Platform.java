/*
 * Platform.java
 *
 * Created on Aug 5, 2007, 5:41:52 PM
 *
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.joing.jvmm;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.net.JarURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.UUID;
import java.util.jar.Attributes;
import java.util.jar.JarInputStream;
import java.util.jar.Manifest;
import javax.swing.SwingUtilities;
import org.joing.Main;
import org.joing.api.DesktopManager;
import org.joing.applauncher.Monitor;
import org.joing.common.dto.app.Application;
import org.joing.runtime.bridge2server.AppBridge;
import org.joing.runtime.bridge2server.Bridge2Server;

/**
 *
 * @author Antonio Varela Lizardi <antonio@icon.net.mx>
 */
public class Platform {

    private static final Platform instance = new Platform();

    private final Map<Object, ClassLoader> classLoaderCache = new HashMap<Object, ClassLoader>();

    private final AppManager appManager = new AppManager();

    private final AppContext context = new AppContext();

    private final Thread mainThread = Thread.currentThread();

    private final PlatformRuntime runtime = PlatformRuntime.getRuntime();

    private final Bridge2Server bridge = new Bridge2Server();

    private DesktopManager desktopManager = null;

    private Properties clientProp = null;

    private Platform() {
    }

    public static Platform getInstance() {
        return instance;
    }

    public PlatformRuntime getRuntime() {
        return this.runtime;
    }

    public Bridge2Server getBridge() {
        return this.bridge;
    }

    // temporal
    public Properties getClientProp() {
        return clientProp;
    }
    
    /**
     * init()
     * Inicializa la Plataforma WebPC.
     */
    public void init(Properties clientProp) {

        this.clientProp = clientProp;

        runtime.init(clientProp);
    }


    /**
     * Regresa el id único del Thread padre. El Thread Padre
     * es el que inició la ejecución de Platform.
     */
    public long getMainId() {
        return mainThread.getId();
    }

    public AppManager getAppManager() {
        return appManager;
    }

    public Map<Object, ClassLoader> getClassLoaderCache() {
        return classLoaderCache;
    }


    public DesktopManager getDesktopManager() {
        return desktopManager;
    }

    public void setDesktopManager(DesktopManager dm) {
        desktopManager = dm;
    }

    /**
     * Obtiene el JThreadGroup. Itera hacia arriba en la jerarquia.
     */
    public JThreadGroup getJThreadGroup() {
        ThreadGroup g = Thread.currentThread().getThreadGroup();
        while (g != null) {
            if (g instanceof JThreadGroup) {
                return (JThreadGroup) g;
            }
            g = g.getParent();
        }
        return null;
    }

    /**
     * Regresa el Contexto de Joing.
    public static AppContext getContext() {
    return context;
    }
    /**
     * <p>Ejecuta una Aplicacion que reside en el WebPcFileServlet con id idApp.
     * Si la aplicacion no requiere argumentos, especificar args como null.</p>
     *
     * @param idApp String con el ID de la Aplicacion.
     * @param mainClass String con el nombre de la Class que contiene el metodo main().
     * @args String array con los argumentos requeridos por la aplicaci�n.
     */
    public void start(String idApp, String mainClass, String[] args) throws PlatformException {

//        if (properties == null) {
//            throw new PlatformException("Platform not Initialized.");
//        }
//        String url = properties.getProperty("WebPc.FileServletUrl");
        String url = context.getFileServerUrl();

        if (url == null) {
            throw new PlatformException("WebPC FileServlet not Fount in Config.");
        }

        if (idApp == null) {
            throw new PlatformException("Must specifiy the Application ID.");
        }

        if (mainClass == null) {
            throw new PlatformException("Must specify the name of the Main Class.");
        }

        StringBuffer sb = new StringBuffer();
        sb.append(url);
        sb.append("?id=");
        sb.append(idApp);

        List<String> tmp = new ArrayList<String>();

        tmp.add("-classpath");
        tmp.add(sb.toString());
        tmp.add(mainClass);

        if (args != null) {
            for (String arg : args) {
                tmp.add(arg);
            }
        }

        String[] a = new String[tmp.size()];
        tmp.toArray(a);

        try {
            //Main.start(a, System.out, System.err);
            Platform.getInstance().start(idApp, a, System.out, System.err);
        } catch (Exception e) {
            throw new PlatformException(e.getMessage());
        }
    }


    /**
     * Ejecuta los argumentos contenidos en el arreglo.
     * @param id UUID de la aplicacion en el WebPcFileServer.
     * @param argv Arreglo de String con los argumentos para la aplicacion.
     * @param out OutputStream para salida std.
     * @param err OutputStream para stderr
     */
    public void start(final String id, final String[] argv, OutputStream out, OutputStream err) throws Exception {

        String mainClass = null;
        String[] appArgs = new String[0]; // un array de 1?
        ClassLoader classLoader = null;

        StringBuffer sb = new StringBuffer();
        sb.append(context.getFileServerUrl());
        sb.append("?id=");
        sb.append(id);

        URL[] urlArray = urlClassPath(sb.toString());
        URLClassLoader loader;

        synchronized (classLoaderCache) {
            // Busca el Mapping
            loader = (URLClassLoader) classLoaderCache.get(urlArray);
            if (loader == null) {
                // No existe, crea el ClassLoader y lo guarda.
                loader = new URLClassLoader(urlArray, Main.class.getClassLoader());
                classLoaderCache.put(urlArray, loader);
            }
        }

        classLoader = loader;
        appArgs = argv;
        mainClass = mainClassName(sb.toString());


        Monitor.log("main class: " + mainClass);
        Monitor.log("ClassLoader: " + classLoader);
        //System.out.println("main class: " + mainClass);
        //System.out.println("ClassLoader: " + classLoader);
        // Declaracion del Thread Disposer
        final Runnable[] runner = new Runnable[1];
        final Thread disposer = new Thread(new Runnable() {

            public void run() {
                Runnable r = runner[0];
                runner[0] = null;
                System.out.println("diposer running: " + r);
                if (r != null) {
                    r.run();
                }
            }
        });

        final ClassLoader finalClassLoader = classLoader;
        final String[] finalArgs = appArgs;

        // Crea un JThreadGroup
        final JThreadGroup threadGroup = new JThreadGroup(out, err);
        // Asigna el Disposer. Supongo que necesita en este momento el Disposer,
        // y por esa razon se declara antes y de esta manera tan confusa.
        threadGroup.setDisposer(disposer);

        final String finalClassName = mainClass;

        // Crea una Nueva aplicacion "nativa"
        final AppImpl app = new AppImpl(appManager, threadGroup, finalClassName);
        appManager.addApp(app);

        Thread thread = new Thread(threadGroup, new Runnable() {

            public void run() {
                final sun.awt.AppContext appContext = sun.awt.SunToolkit.createNewAppContext();
                // Este es el Thread que eventualmente ejecuta el Disposer
                runner[0] = new Runnable() {

                    public void run() {
                        appManager.removeApp(app);
                        System.out.println("disposing app context: ");
                        appContext.dispose();
                    }
                };
                try {
                    // invokeAndWait necesita un Runnable como par�metro, aunque
                    // en realidad no lo ejecuta como un Thread. El Runnable se
                    // coloca en el EDT (Event Dispatch Thread) y si el objeto
                    // es Runnable, simplemente invoca el metodo run().
                    SwingUtilities.invokeAndWait(new Runnable() {

                        public void run() {
                            try {
                                Class c = finalClassLoader.loadClass(finalClassName);
                                Method main = c.getMethod("main", new Class[]{String[].class});
                                //Method main = finalClassLoader.loadClass(finalClassName).getMethod("main", new Class[]{String[].class});
                                main.invoke(null, new Object[]{finalArgs});
                            } catch (Exception e) {
                                e.printStackTrace();
                                threadGroup.close(); // aqui se usa el disposer
                            }
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    /*
     * Convierte un String con un ClassPath en un Arreglo de URL
     * para ser usado por el URLClassLoader.
     * ClassPath puede ser una serie de path permitidos, separados
     * por ;
     **/
    public URL[] urlClassPath(final String classPath) throws PlatformException {

        // Con el tokenizer divide el classpath en sus elementos.
        StringTokenizer st = new StringTokenizer(classPath, ";");
        List<URL> urls = new LinkedList<URL>();

        try {
            while (st.hasMoreTokens()) {
                String urlString = st.nextToken();
                URL u;
                if (!urlString.startsWith("http:") && !urlString.startsWith("file:") && !urlString.startsWith("jar:")) {
                    // Es un archivo ubicado en el disco local
                    File f = new File(urlString).getCanonicalFile();
                    u = f.toURI().toURL();
                } else {
                    u = new URL(urlString);
                }
                urls.add(u);
            }
        } catch (IOException ioe) {
            throw new PlatformException(ioe.getMessage());
        }

        URL[] urlArray = new URL[urls.size()];
        urls.toArray(urlArray); // convierte en Array
        urls = null;
        st = null;
        return urlArray;
    }

    /**
     * Obtiene el main class.
     */
    private String mainClassName(String jarUrl) throws PlatformException {

        try {

            URL url = new URL("jar", "", jarUrl + "!/");
            JarURLConnection juc = (JarURLConnection) url.openConnection();
            Attributes attr = juc.getMainAttributes();

            if (attr == null) {
                throw new PlatformException("Unable to find Jar Attributes.");
            }

            String className = attr.getValue(Attributes.Name.MAIN_CLASS);

            if (className == null) {
                throw new PlatformException("Unable to find Main Class.");
            }

            url = null;
            juc = null;
            attr = null;

            return className;
        } catch (MalformedURLException mue) {
            throw new PlatformException(mue.getMessage());
        } catch (IOException ioe) {
            throw new PlatformException(ioe.getMessage());
        }
    }

    /**
     * Ejecuta los argumentos contenidos en el arreglo.
     */
    public void start(final String[] argv, OutputStream out, OutputStream err) throws Exception {

        String mainClassName = null;
        String[] appArgs = new String[0]; // un array de 1?
        ClassLoader classLoader = null;
        final Platform platform = Platform.getInstance();

        for (int i = 0; i < argv.length; i++) {

            if (argv[i].equals("-classpath")) {
                // El argumento fue el string classpath, el siguiente
                // argumento corresponde a la URL separada por ;
                String cp = argv[i + 1];
                // Con el tokenizer divide el classpath en sus elementos.
                StringTokenizer izer = new StringTokenizer(cp, ";");
                List<URL> urls = new LinkedList<URL>();

                while (izer.hasMoreTokens()) {
                    String urlString = izer.nextToken();
                    URL u;
                    if (!urlString.startsWith("http:") && !urlString.startsWith("file:") && !urlString.startsWith("jar:")) {
                        // Es un archivo ubicado en el disco local
                        File f = new File(urlString).getCanonicalFile();
                        u = f.toURI().toURL();
                    } else {
                        u = new URL(urlString);
                    }
                    urls.add(u);
                }

                URL[] urlArray = new URL[urls.size()];
                urls.toArray(urlArray); // convierte en Array
                URLClassLoader loader;

                // En esta parte se lleva una especie de cache que mapea
                // un URL[] con un URLClassLoader. Si no se encuentra en el
                // Map, entonces crear el ClassLoader con el mismo de Main
                // y agrega el registro al cache.
                Map<Object, ClassLoader> classLoaderCache = platform.getClassLoaderCache();
                synchronized (classLoaderCache) {
                    // Busca el Mapping
                    loader = (URLClassLoader) classLoaderCache.get(urlArray);
                    if (loader == null) {
                        // No existe, crea el ClassLoader y lo guarda.
                        loader = new URLClassLoader(urlArray, Main.class.getClassLoader());
                        classLoaderCache.put(urlArray, loader);
                    }
                }
                classLoader = loader;
                i++; // se brinca el nombre de la Main Class
            } else if (!argv[i].startsWith("-")) {
                mainClassName = argv[i];
                // Copia los argumentos en caso de haberlos.
                if (i + 1 < argv.length) {
                    appArgs = new String[argv.length - i + 1];
                    int k = 0;
                    for (int j = i + 1; j < argv.length; j++, k++) {
                        appArgs[k] = argv[j];
                    }
                }
                break;
            }
        }

        System.out.println("main class: " + mainClassName);
        System.out.println("ClassLoader: " + classLoader);

        // Declaracion del Thread Disposer
        final Runnable[] runner = new Runnable[1];
        final Thread disposer = new Thread(new Runnable() {

            public void run() {
                Runnable r = runner[0];
                runner[0] = null;
                System.out.println("diposer running: " + r);
                if (r != null) {
                    r.run();
                }
            }
        });

        final ClassLoader finalClassLoader = classLoader;
        final String[] finalArgs = appArgs;

        // Crea un JThreadGroup
        final JThreadGroup threadGroup = new JThreadGroup(out, err);
        // Asigna el Disposer. Supongo que necesita en este momento el Disposer,
        // y por esa razon se declara antes y de esta manera tan confusa.
        threadGroup.setDisposer(disposer);

        final String finalClassName = mainClassName;

        // Crea una Nueva aplicacion "nativa"
        final AppImpl app = new AppImpl(platform.getAppManager(), threadGroup, finalClassName);
        platform.getAppManager().addApp(app);

        Thread thread = new Thread(threadGroup, new Runnable() {

            public void run() {
                final sun.awt.AppContext appContext = sun.awt.SunToolkit.createNewAppContext();
                // Este es el Thread que eventualmente ejecuta el Disposer
                runner[0] = new Runnable() {

                    public void run() {
                        platform.getAppManager().removeApp(app);
                        System.out.println("disposing app context: ");
                        appContext.dispose();
                    }
                };
                try {
                    // invokeAndWait necesita un Runnable como parámetro, aunque
                    // en realidad no lo ejecuta como un Thread. El Runnable se
                    // coloca en el EDT (Event Dispatch Thread) y si el objeto
                    // es Runnable, simplemente invoca el metodo run().
                    SwingUtilities.invokeAndWait(new Runnable() {

                        public void run() {
                            try {
                                Class c = finalClassLoader.loadClass(finalClassName);
                                Method main = c.getMethod("main", new Class[]{String[].class});
                                //Method main = finalClassLoader.loadClass(finalClassName).getMethod("main", new Class[]{String[].class});
                                main.setAccessible(true);
                                main.invoke(null, new Object[]{finalArgs});
                            } catch (Exception e) {
                                e.printStackTrace();
                                threadGroup.close(); // aqui se usa el disposer
                            }
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }


    public void start(int appId) throws ApplicationExecutionException {

        AppBridge appBridge = bridge.getAppBridge();

        Application app = appBridge.getApplication(appId);

        // En caso de error, no tengo idea de que lo está casando.
        if (app == null) {
            // Platform no deberia interactuar directamente con el GUI
            Monitor.log("Ocurró un Error al Cargar la Aplicación.");
            throw new ApplicationExecutionException("Ocurrió un Error al Cargar la Aplicación.");
        }

        try {
            
            JarInputStream jis = new JarInputStream(app.getContent());
            String mainClass = getMainClassName(jis);

            if (mainClass != null) {
                Monitor.log("Main Class found in Manifest: " + mainClass);
            }

            UUID uuid = UUID.randomUUID();
            String tmpDir = clientProp.getProperty("LocalTempDir");
            tmpDir = (tmpDir == null) ? "C:\\Temp\\Joing" : tmpDir;

            if (tmpDir.endsWith("\\") == false) {
                tmpDir += "\\";
            }

            String fileName = uuid.toString() + ".jar";
            String jarName = tmpDir + fileName;
            FileOutputStream fos = new FileOutputStream(jarName);

            boolean done = false;
            InputStream is = app.getContent();
            while (!done) {
                int av = is.available();
                byte[] b = new byte[av];
                int n = is.read(b);
                if (n < 0) {
                    done = true;
                } else {
                    fos.write(b);
                }
            }
            fos.close();

            List<String> argLst = new ArrayList<String>();
            tmpDir = clientProp.getProperty("FileUrlPrefix");
            tmpDir = (tmpDir == null) ? "file://" : tmpDir;
            
            argLst.add("-classpath");
            argLst.add(tmpDir + fileName + "!/");
            argLst.add(mainClass);
            
            String[] args = argLst.toArray(new String[] {});

            start(args, System.out, System.err);
            
        } catch (Exception ioe) {
            throw new ApplicationExecutionException(ioe.getMessage());
        }
    }

    /**
     * Gets the Main Class name from a jar's Manifest. This Method does not
     * takes the responsability of closing the InputSream.
     *
     * @param jarInput JarInputStream object.
     * @return String with the Main Class stated in Manifest, null otherwise.
     */
    protected String getMainClassName(JarInputStream jarInput) {

        Manifest manifest = jarInput.getManifest();
        Attributes attributes = manifest.getMainAttributes();
        String mainClass = attributes.getValue("Main-Class");

        return mainClass;
    }

    /**
     * Halts the system - now
     */
    public void halt() {
        Runtime.getRuntime().exit(0); // fix this
    }

    /**
     * Issues a Clean Shutdown.
     */
    public void shutdown() {
        halt(); // fix this.
    }
}
