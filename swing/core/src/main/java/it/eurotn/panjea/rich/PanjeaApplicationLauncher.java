package it.eurotn.panjea.rich;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.Calendar;

import javax.swing.SwingUtilities;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.richclient.application.Application;
import org.springframework.richclient.application.splash.MonitoringSplashScreen;
import org.springframework.richclient.application.splash.ProgressSplashScreen;
import org.springframework.richclient.application.splash.SplashScreen;
import org.springframework.richclient.progress.ProgressMonitor;
import org.springframework.richclient.util.Assert;

import it.eurotn.panjea.merge.xml.PluginMerge;

/**
 * The main driver for a Spring Rich Client application.
 *
 * <p>
 * This class displays a configurable splash screen and launches a rich client {@link Application}. Both the splash
 * screen and the application to be launched are expected to be defined as beans, under the names
 * {@link #SPLASH_SCREEN_BEAN_ID} and {@link #APPLICATION_BEAN_ID} respectively, in one of the application contexts
 * provided to the constructor.
 * </p>
 *
 * <p>
 * For quick loading and display of the splash screen while the rest of the application is being initialized,
 * constructors are provided that take a separate startup context. The startup context will be searched for the
 * {@link #SPLASH_SCREEN_BEAN_ID} bean, which will then be displayed before the main application context is loaded and
 * the application launched. If no startup context is provided or it doesn't contain an appropriately named splash
 * screen bean, an attempt will be made to load a splash screen from the main application context. This can only happen
 * after the main application context has been loaded in its entirety so it is not the recommended approach for
 * displaying a splash screen.
 * </p>
 *
 * @author Keith Donald
 * @see Application
 */
public class PanjeaApplicationLauncher {

    private static final Class[] parameters = new Class[] { URL.class };

    /**
     * The name of the bean that defines the application's splash screen. * {@value}
     */

    /**
     * The name of the bean that defines the {@code Application} that this class will launch. * {@value}
     */
    public static final String APPLICATION_BEAN_ID = "application";

    private final Log logger = LogFactory.getLog(getClass());

    private ApplicationContext startupContext;

    private ProgressSplashScreen splashScreen;

    private ApplicationContext rootApplicationContext;

    private String[] args;

    /**
     * Launches the application defined by the Spring application context files at the provided classpath-relative
     * locations. The application context file specified by {@code startupContextPath} is loaded first to allow for
     * quick loading of the application splash screen. It is recommended that the startup context only contains the bean
     * definition for the splash screen and any other beans that it depends upon. Any beans defined in the startup
     * context will not be available to the main application once launched.
     *
     * @param startupContextPath
     *            The classpath-relative location of the startup context file. May be null or empty.
     * @param rootContextConfigLocations
     *            The classpath-relative locations of the main application context files.
     * @param argsCommandLine
     *            parametri da linea di comando
     */
    public PanjeaApplicationLauncher(final String[] argsCommandLine) {
        try {
            args = argsCommandLine;
            displaySplashScreen();
            setRootApplicationContext(loadRootApplicationContext(startupContext));
            launchMyRichClient();
        } catch (Exception e) {
            logger.error("PanjeaApplicationLauncher ", e);
        } finally {
            destroySplashScreen();
        }
    }

    private void creaFilesContext() {
        File file = new File("panjea-context.xml");
        if (!file.exists()) {
            PluginMerge merge = new PluginMerge();
            merge.merge();
        }
    }

    /**
     * chiude lo spash.
     */
    private void destroySplashScreen() {
        if (splashScreen != null) {
            logger.debug("Closing splash screen...");

            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    splashScreen.dispose();
                    splashScreen = null;
                }
            });
        }
    }

    /**
     * Searches the given bean factory for a {@link SplashScreen} defined with the bean name
     * {@link #SPLASH_SCREEN_BEAN_ID} and displays it, if found.
     *
     * @param beanFactory
     *            The bean factory that is expected to contain the splash screen bean definition. Must not be null.
     */
    private void displaySplashScreen() {
        splashScreen = new ProgressSplashScreen();
        splashScreen.setShadowBorder(true);
        splashScreen.setImageResourcePath(new ClassPathResource("/it/eurotn/panjea/resources/images/logo.png"));
        logger.debug("Displaying application splash screen...");
        try {
            SwingUtilities.invokeAndWait(new Runnable() {
                @Override
                public void run() {
                    PanjeaApplicationLauncher.this.splashScreen.splash();
                }
            });
        } catch (Exception e) {
            logger.error("EDT threading issue while showing splash screen", e);
            throw new RuntimeException("EDT threading issue while showing splash screen", e);
        }
    }

    /**
     * Launches the rich client application. If no startup context has so far been provided, the main application
     * context will be searched for a splash screen to display. The main application context will then be searched for
     * the {@link Application} to be launched, using the bean name {@link #APPLICATION_BEAN_ID}. If the application is
     * found, it will be started.
     */
    private void launchMyRichClient() {
        final PanjeaApplication application;

        try {
            application = (PanjeaApplication) rootApplicationContext.getBean(APPLICATION_BEAN_ID, Application.class);
            application.setArgs(args);
        } catch (NoSuchBeanDefinitionException e) {
            logger.error("A single bean definition with id " + APPLICATION_BEAN_ID + ", of type "
                    + Application.class.getName() + " must be defined in the main application context", e);
            throw new IllegalArgumentException("A single bean definition with id " + APPLICATION_BEAN_ID + ", of type "
                    + Application.class.getName() + " must be defined in the main application context", e);
        }

        try {
            // To avoid deadlocks when events fire during initialization of some swing components
            // Possible to do: in theory not a single Swing component should be created (=modified)
            // in the launcher
            // thread...
            SwingUtilities.invokeAndWait(new Runnable() {
                @Override
                public void run() {
                    application.start();
                    application.getActiveWindow().getControl().setAlwaysOnTop(true);
                    application.getActiveWindow().getControl().toFront();
                    application.getActiveWindow().getControl().requestFocus();
                    application.getActiveWindow().getControl().setAlwaysOnTop(false);

                }
            });
        } catch (InterruptedException e) {
            logger.warn("Application start interrupted", e);
        } catch (InvocationTargetException e) {
            logger.error("Application start thrown an exception", e);
            Throwable cause = e.getCause();
            throw new IllegalStateException("Application start thrown an exception: " + cause.getMessage(), cause);
        } catch (Exception e) {
            logger.error("Application start thrown an exception", e);
            throw new IllegalStateException("Application start thrown an exception", e);
        }
        logger.debug("Launcher thread exiting...");

    }

    /**
     * Returns an {@code ApplicationContext}, loaded from the bean definition files at the classpath-relative locations
     * specified by {@code configLocations}.
     *
     * <p>
     * If a splash screen has been created, the application context will be loaded with a bean post processor that will
     * notify the splash screen's progress monitor as each bean is initialized.
     * </p>
     *
     * @param configLocations
     *            The classpath-relative locations of the files from which the application context will be loaded.
     * @param messageSource
     *            message source per I18N
     *
     * @return The main application context, never null.
     */
    private ApplicationContext loadRootApplicationContext(MessageSource messageSource) {
        long startTime = Calendar.getInstance().getTimeInMillis();

        creaFilesContext();

        AbstractApplicationContext applicationContext = null;
        // try {
        // String context = FileUtils.readFileToString(new File("panjea-context.xml"));
        // System.out.println(context);
        // applicationContext = new InMemoryXmlApplicationContext(context);
        // } catch (IOException e) {
        // // TODO Auto-generated catch block
        // e.printStackTrace();
        // }

        // Try with resources... so nice!
        // List<String> contexts = new ArrayList<>();
        // try (DirectoryStream<Path> ds = Files.newDirectoryStream(Paths.get("."), "panjea*-context.xml")) {
        // for (Path path : ds) {
        // contexts.add(path.toString());
        // }
        // } catch (IOException e) {
        // e.printStackTrace();
        // }
        // String[] contextArray = new String[contexts.size()];
        // for (int i = 0; i < contexts.size(); i++) {
        // contextArray[i] = contexts.get(i);
        // }

        String panjeaContextPath = "./panjea-context.xml";
        String panjeaPagesContextPath = "./panjea-pages-context.xml";
        String panjeaSecurityContextPath = "./panjea-security-context.xml";
        String panjeaSearchContextPath = "./panjea-search-context.xml";
        String panjeaMenuContextPath = "./panjea-menu-context.xml";

        String[] panjeaStartContextPath = new String[] { panjeaContextPath, panjeaPagesContextPath,
                panjeaSecurityContextPath, panjeaSearchContextPath, panjeaMenuContextPath };

        applicationContext = new FileSystemXmlApplicationContext(panjeaStartContextPath, false);
        // applicationContext = new ClassPathXmlApplicationContext(
        // "//home/giangi/Progetti/workspace/panjea-desktop/panjea-context.xml");
        System.out.println("Tempo partenza:" + (Calendar.getInstance().getTimeInMillis() - startTime));
        startTime = Calendar.getInstance().getTimeInMillis();
        if (splashScreen instanceof MonitoringSplashScreen)

        {
            final ProgressMonitor tracker = ((MonitoringSplashScreen) splashScreen).getProgressMonitor();
            applicationContext.addBeanFactoryPostProcessor(
                    new PanjeaProgressMonitoringBeanFactoryPostProcessor(tracker, messageSource));
        }
        applicationContext.refresh();
        System.out.println("Tempo partenza:" + (Calendar.getInstance().getTimeInMillis() - startTime));
        return applicationContext;

    }

    /**
     * @param context
     *            application context di spring.
     */
    private void setRootApplicationContext(ApplicationContext context) {
        Assert.notNull(context, "The root rich client application context is required");
        this.rootApplicationContext = context;
    }

}
