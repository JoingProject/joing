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
import java.net.MalformedURLException;
import java.net.URL;
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
import javax.swing.SwingWorker;
import org.joing.Main;
import org.joing.common.clientAPI.jvmm.App;
import org.joing.common.desktopAPI.DesktopManager;
import org.joing.common.dto.app.Application;
import org.joing.common.clientAPI.jvmm.AppManager;
import org.joing.common.clientAPI.jvmm.ApplicationExecutionException;
import org.joing.common.clientAPI.jvmm.JThreadGroup;
import org.joing.common.clientAPI.jvmm.Platform;
import org.joing.common.clientAPI.log.JoingLogger;
import org.joing.common.clientAPI.log.Levels;
import org.joing.common.clientAPI.log.Logger;
import org.joing.common.clientAPI.log.SimpleLoggerFactory;
import org.joing.common.clientAPI.runtime.AppBridge;
import org.joing.common.clientAPI.runtime.Bridge2Server;
import org.joing.jvmm.net.URLFormat;
import org.joing.runtime.bridge2server.Bridge2ServerImpl;

/**
 *
 * @author Antonio Varela Lizardi <antonio@icon.net.mx>
 */
class PlatformImpl implements Platform {

    private final Map<Object, ClassLoader> classLoaderCache = new HashMap<Object, ClassLoader>();
    private final AppManager appManager = new AppManagerImpl();
    private final AppContext context = new AppContext();
    private final Thread mainThread = Thread.currentThread();
    private final Bridge2Server bridge = new Bridge2ServerImpl();
    private DesktopManager desktopManager = null;
    private Properties clientProp = null;
    private boolean initialized = true;
    private String serverBaseUrl = null;
    private boolean autoHandlingExceptions = true;
    private final Logger logger = SimpleLoggerFactory.getLogger(JoingLogger.ID);
    private boolean sharedAWTContext = false;
    private boolean halted = false;

    public PlatformImpl() {

        clientProp = new Properties();

        // Looks in classpath
        InputStream is =
                Main.class.getClassLoader().getResourceAsStream("client.properties");

        if (is != null) {
            try {
                clientProp.load(is);
            } catch (IOException ioe) {
                logger.write(Levels.CRITICAL, "IOException: {0}",
                        ioe.getMessage());
                // At this point and with this implementation, an error getting
                // the clientProperties leaves us with no way to know the
                // servletUrl. We can not go further.
                throw new RuntimeException("Unable to find properties file.");
            }
        }

        //        getRuntime().init(clientProp);
        // establecer la URL del servlet aqui
        this.serverBaseUrl =
                clientProp.getProperty(RuntimeEnum.JOING_SERVER_URL.getKey());
        if (this.serverBaseUrl == null) {
            this.initialized = false;
        }

        this.sharedAWTContext = 
                Boolean.valueOf(clientProp.getProperty("sharedAWTContext", "false"));
        
    }

    @Override
    public Bridge2Server getBridge() {
        return this.bridge;
    }

    // temporal
    @Override
    public Properties getClientProp() {
        return clientProp;
    }

    @Override
    public boolean isInitialized() {
        return this.initialized;
    }

    @Override
    public String getServerBaseURL() {
        return this.serverBaseUrl;
    }

    @Override
    public boolean isAutoHandlingExceptions() {
        return this.autoHandlingExceptions;
    }

    @Override
    public void setAutoHandlingExceptions(boolean autoHandleExceptions) {
        this.autoHandlingExceptions = autoHandleExceptions;
    }

    @Override
    public void showException(Throwable exc) {
        showException("Error", exc);
    }

    @Override
    public void showException(String sTitle, Throwable exc) {
        exc.printStackTrace();

    /*  TODO: hacer q JShowException herede de JDesktopDialog y q actue en consecuencia:
     *  Nota: utilizar getLocalizedMessage()
    JShowException dialog = new JShowException( sTitle, exc );
    dialog.setLocationRelativeTo( getDesktop() );
    dialog.setVisible( true );*/
    }

    /**
     * Regresa el id único del Thread padre. El Thread Padre
     * es el que inició la ejecución de Platform.
     */
    @Override
    public long getMainThreadId() {
        return mainThread.getId();
    }
    
    // This property is for debugging purposes
    public final Thread getMainThread() {
        return this.mainThread;
    }

    @Override
    public AppManager getAppManager() {
        return appManager;
    }

    @Override
    public Map<Object, ClassLoader> getClassLoaderCache() {
        return classLoaderCache;
    }

    @Override
    public DesktopManager getDesktopManager() {
        return desktopManager;
    }

    @Override
    public void setDesktopManager(DesktopManager desktop) {
        desktopManager = desktop;
    }

    /**
     * Obtiene el JThreadGroup. Itera hacia arriba en la jerarquia.
     */
    @Override
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


    public void start(final int appId, final String[] args,
            final OutputStream out, final OutputStream err)
            throws ApplicationExecutionException {

        AppBridge appBridge = bridge.getAppBridge();

        Application application = appBridge.getApplication(appId);
        start(application, args, out, err);

    }

    /**
     * Convenience method for launching applications. Takes fewer arguments.
     * @param application Application object to Launch.
     * @param args Arguments, if any
     * @param out OutputStream object associated to the application
     * System.out property.
     * @param err OutputStream object associated to the application's 
     * System.err property.
     * @throws org.joing.common.clientAPI.jvmm.ApplicationExecutionException
     * @see org.joing.jvmm.Platform
     */
    public void start(final Application application, String[] args,
            OutputStream out, OutputStream err)
            throws ApplicationExecutionException {

        try {
            
            URL url = URLFormat.toURL(application);
            URL[] classPath = new URL[]{url};

            start(classPath, application, args, out, err);

        } catch (MalformedURLException mue) {
            throw new ApplicationExecutionException(mue.getMessage());
        }
    }

    /**
     * Base method for starting application. Invokes the BridgeClassLoader
     * in order to deal with bridge2server style URL format.
     * @param classPath Array of URL Objects with the class path for loading
     * classes and resources. Accepts URL's in bridge2server format, along with
     * the standard ones provided by the JVM implementation (http, file and 
     * jar).
     * @param application Application object to be Launched.
     * @param args Arguments for the application, if any.
     * @param out Outputstream object associated to the application's System.out
     * property.
     * @param err Outputstream object associated to the application's Syste.err
     * property.
     * @throws org.joing.common.clientAPI.jvmm.ApplicationExecutionException
     * @see org.joing.jvmm.net.URLFormat URLFormat
     * @see org.joing.jvmm.net.URLFormat#toURL URLFormat.toURL()
     */
    public void start(final URL[] classPath, final Application application,
            final String[] args, final OutputStream out, final OutputStream err)
            throws ApplicationExecutionException {

        BridgeClassLoader classLoader = null;
        Map<Object, ClassLoader> classLdrCache = getClassLoaderCache();

        synchronized (classLdrCache) {
            try {
                BridgeClassLoader loader =
                        (BridgeClassLoader) classLdrCache.get(classPath);
                if (loader == null) {
                    loader = new BridgeClassLoader(bridge, classPath,
                            Main.class.getClassLoader());
                    classLdrCache.put(classPath, loader);

                }
                classLoader = loader;
            } catch (Exception e) {
                throw new ApplicationExecutionException(e.getMessage());
            }
        }

        final String finalClassName = getMainClassName(application);

        logger.debugJVMM("Main Class: {0}", finalClassName);
        logger.debugJVMM("ClassLoader: {0}", classLoader);

        // Crea un JThreadGroup
        JThreadGroup threadGroup = new JThreadGroupImpl(out, err);

        final BridgeClassLoader finalClassLoader = classLoader;
        final String[] finalArgs =
                (args == null) ? new String[] {} : args;

        final Task executionTask =
                new Task(finalClassLoader, finalClassName, finalArgs);

        // Crea una Nueva aplicacion "nativa"
        final App app =
                new AppImpl(threadGroup, finalClassName, application);

        getAppManager().addApp(app);

        // Este thread es quien ejecuta la tarea.
        final ExecutionThread executionThread =
                new ExecutionThread(getAppManager(), app, executionTask);
        
        if (SwingUtilities.isEventDispatchThread()) {
             SwingWorker worker = new SwingWorker<Void, Void>() {

                @Override
                protected Void doInBackground() throws InterruptedException {
                    
                    if (sharedAWTContext) {
                        executionTask.run();
                    } else {
                        executionThread.start();
                    }
                    
                    return null;
                }
            };
            
            logger.debugJVMM("Starting new ExecutionThread with Id {0} " +
                    "and ThreadGroup {1} from a SwingWorker", executionThread.getId(),
                    executionThread.getThreadGroup().getName());
            worker.execute();
            
        } else {
            
            logger.debugJVMM("Starting new Standalone ExecutionThread with Id {0} " +
                    "and ThreadGroup {1}", executionThread.getId(),
                    executionThread.getThreadGroup().getName());

            executionThread.start();
        }

    }
    
    
    /**
     * Launches a joing app based on the appId.
     * @param appId
     * @throws org.joing.common.clientAPI.jvmm.ApplicationExecutionException
     */
    @Override
    public void start(int appId) throws ApplicationExecutionException {
        
        AppBridge appBridge = bridge.getAppBridge();

        Application app = appBridge.getApplication(appId);

        // En caso de error, no tengo idea de que lo está casando.
        if (app == null) {
            String msg = "Unable to get application from server.";
            logger.write(Levels.CRITICAL, msg);
            throw new ApplicationExecutionException(msg);
        }
        
        start(app, new String[] {}, System.out, System.err);
        
    }

    /**
     * Utility method to start applications. Fetches the Application from
     * the server, saving a Jar file locally and executing it from there.
     * The method has hard coded parameters about the local temp dir.
     * @param appId
     * @throws org.joing.common.clientAPI.jvmm.ApplicationExecutionException
     * @deprecated Will be removed anytime soon,
     */
    public void startFromTmpDir(int appId) throws ApplicationExecutionException {

        AppBridge appBridge = bridge.getAppBridge();

        Application app = appBridge.getApplication(appId);

        // En caso de error, no tengo idea de que lo está casando.
        if (app == null) {
            String msg = "Unable to get application from server.";
            logger.write(Levels.CRITICAL, msg);
            throw new ApplicationExecutionException(msg);
        }

        try {

            JarInputStream jis = new JarInputStream(app.getContent());
            String mainClass = getMainClassName(jis);

            if (mainClass != null) {
                logger.write(Levels.DEBUG_JVMM, 
                        "Main Class found in Manifest: {0}", mainClass);
            }

            UUID uuid = UUID.randomUUID();
            String tmpDir = clientProp.getProperty("LocalTempDir");
            if( tmpDir == null )                               // Antonio, he añadido esto porque en Linux
                tmpDir = System.getProperty("java.io.tmpdir"); // cuando tmpDir == null, crea ficheros raros (peyrona)
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

            String[] args = argLst.toArray(new String[]{});

            start(app, args, System.out, System.err);

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

    @Override
    public String getMainClassName(Application application) {

        try {
            JarInputStream jis = new JarInputStream(application.getContent());
            return getMainClassName(jis);
        } catch (IOException ioe) {
            logger.write(Levels.CRITICAL, "IOException in getMainClassName: {0}",
                    ioe.getMessage());
            return null;
        }
    }

    /**
     * Halts the system - now
     */
    @Override
    public void halt() {
        //Runtime.getRuntime().exit(0); // fix this
        this.halted = true;
        this.mainThread.interrupt();
    }
    
    @Override
    public boolean isHalted() {
        return this.halted;
    }

    /**
     * Issues a Clean Shutdown.
     */
    @Override
    public void shutdown() {
        if (getDesktopManager() != null) {
            getDesktopManager().shutdown();  // Needed to close DesktopManager gently.
        }
        halt(); // fix this.
    }
    
}
