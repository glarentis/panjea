package it.eurotn.panjea;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Locale;
import java.util.Properties;

import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

import com.jidesoft.plaf.LookAndFeelFactory;

import it.eurotn.panjea.rich.PanjeaApplicationLauncher;
import it.eurotn.panjea.rich.PanjeaFilterFactoryManager;
import it.eurotn.panjea.shortcut.ShortCutWindows;

public class PanjeaStart {

    public static final String JAVA_NAMING_PROVIDER_URL = "java_naming_provider_url";
    public static final String SHOW_LICENCE = "ShowLicence";
    public static final String ULTIMA_AZIENDA_LOGGATA = "ultimaAziendaLoggata";
    public static final String LOOK_AND_FEEL_STYLE_WINDOWS = "look_and_feel_style_windows";
    public static final String LOOK_AND_FEEL_STYLE_LINUX = "look_and_feel_style_linux";
    public static final String JAVA_NAMING_FACTORY_URL_PKGS = "java_naming_factory_url_pkgs";
    public static final String JAVA_NAMING_FACTORY_INITIAL = "java_naming_factory_initial";
    public static final String NAME_PANJEAUSER_PROPERTIES = "panjeauser.properties";
    public static final String NAME_AUTH_CONF = "auth.conf";
    public static final String LOCALE_LANGUAGE_KEY = "localeLanguage";
    public static final String LOCALE_COUNTRY_KEY = "localeCountry";

    private static Logger logger = Logger.getLogger(PanjeaStart.class);
    protected Properties properties;

    /**
     * Aggiunge al properties le proprietà mancanti.
     *
     * @param properties
     *            la properties a cui aggiungere le proprietà mancanti
     * @return Properties con le proprietà aggiunte
     */
    protected void addMissingProperties() {
        if (properties.getProperty(JAVA_NAMING_FACTORY_INITIAL) == null) {
            properties.setProperty(JAVA_NAMING_FACTORY_INITIAL, "org.jnp.interfaces.NamingContextFactory");
        }
        if (properties.getProperty(JAVA_NAMING_FACTORY_URL_PKGS) == null) {
            properties.setProperty(JAVA_NAMING_FACTORY_URL_PKGS, "org.jboss.naming:org.jnp.interfaces");
        }
        if (properties.getProperty(LOOK_AND_FEEL_STYLE_LINUX) == null) {
            properties.setProperty(LOOK_AND_FEEL_STYLE_LINUX, "com.jgoodies.looks.plastic.Plastic3DLookAndFeel");
        }
        if (properties.getProperty(LOOK_AND_FEEL_STYLE_WINDOWS) == null) {
            properties.setProperty(LOOK_AND_FEEL_STYLE_WINDOWS, "com.jgoodies.looks.windows.WindowsLookAndFeel");
        }
        if (properties.getProperty(ULTIMA_AZIENDA_LOGGATA) == null) {
            properties.setProperty(ULTIMA_AZIENDA_LOGGATA, "");
        }
        if (properties.getProperty(SHOW_LICENCE) == null) {
            properties.setProperty(SHOW_LICENCE, "true");
        }
        if (properties.getProperty(LOCALE_LANGUAGE_KEY) == null) {
            properties.setProperty(LOCALE_LANGUAGE_KEY, Locale.getDefault().getLanguage());
        }
        if (properties.getProperty(LOCALE_COUNTRY_KEY) == null) {
            properties.setProperty(LOCALE_COUNTRY_KEY, Locale.getDefault().getCountry());
        }
    }

    protected Path getPanjeaDir() {
        try {
            Path path = Paths.get(PanjeaStart.class.getProtectionDomain().getCodeSource().getLocation().toURI());
            if (path.toString().contains("panjea-desktop")) {
                return Paths.get(System.getProperty("user.home")).resolve(".panjeaDev");
            }
            return path.getParent();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    protected void init() {
        // copio il file properties nella cartella locale.
        // Questo per mantenere la configurazione del client durante il passaggio da web start al
        // client locale
        Path homeWebStart = Paths.get(System.getProperty("user.home")).resolve(".panjea");
        if (Files.exists(homeWebStart)) {
            try {
                // uso FileUtils di apache perchè Files.copy delle librerie nio non copiano i files
                // contenuti nella
                // directory ( vedi javadoc )
                FileUtils.copyDirectory(homeWebStart.toFile(), getPanjeaDir().toFile());

                // una volta copiata rinomino la vecchia directory così alla prossima partenza non
                // verrà più considerata
                // ma verranno comunque conservati i vecchi file
                homeWebStart.toFile().renameTo(new File(homeWebStart.toString() + "Old"));
            } catch (IOException e) {
                logger.error("-->errore nel copiare il file di properties dalla vecchia home", e);
            }
        }
    }

    /**
     * Inizializzazione di language e country dell'applicazione.
     */
    protected void initLocale() {
        logger.debug("--> Enter initLocale");
        String language = properties.getProperty(LOCALE_LANGUAGE_KEY, Locale.getDefault().getLanguage());
        String country = properties.getProperty(LOCALE_COUNTRY_KEY, Locale.getDefault().getCountry());
        Locale.setDefault(new Locale(language, country));
    }

    /**
     * Inizialzza log4j.
     */
    protected void initLog() {
        try {
            BasicConfigurator.configure();
            URL url = Panjea.class.getResource("/it/eurotn/panjea/log4j.xml");
            BasicConfigurator.resetConfiguration();
            DOMConfigurator.configure(url);
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(Panjea.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
    }

    private void installShortcut() {
        if (System.getProperty("os.name").toLowerCase().startsWith("windows")) {
            ShortCutWindows shortCutWindows = new ShortCutWindows();
            shortCutWindows.create(getPanjeaDir());
        }
    }

    /**
     * Crea il file user.properties nella directory home/.panjea.
     */

    protected void readProperties() {
        Path propertiesName = getPanjeaDir().resolve(NAME_PANJEAUSER_PROPERTIES);
        properties = new Properties();
        if (Files.exists(propertiesName)) {
            try {
                properties.load(new FileInputStream(getPanjeaDir().resolve(NAME_PANJEAUSER_PROPERTIES).toString()));
            } catch (IOException e) {
                logger.error("-->impossibile leggere il file properties", e);
                System.exit(-1);
            }
        } else {
            File userProperties = propertiesName.toFile();
            try {
                userProperties.createNewFile();
                logger.debug("--> Creato il file " + userProperties.getCanonicalPath());
                properties.load(new FileInputStream(userProperties));
                // aggiunge le proprietà mancanti o tutte nel caso in cui sia appena stato creato il
                // file
                addMissingProperties();
                // salva le properties
                properties.store(new FileOutputStream(userProperties), null);
                if (propertiesName.toString().contains("panjea-desktop")) {
                    Files.copy(userProperties.toPath(), userProperties.toPath().getParent().getParent().resolve("src")
                            .resolve(userProperties.getName()));
                }
            } catch (IOException e) {
                logger.error("--> Errore durante la creazione del file " + userProperties.getName(), e);
            }
        }
    }

    /**
     * avvia panjea.
     *
     * @param args
     *            argomenti dalla linea di comando
     */
    public void start(String[] args) {
        init();
        readProperties();
        initLog();
        new PanjeaUpdater(getPanjeaDir()).checkAndApply();

        com.jidesoft.utils.Lm.verifyLicense("EUROPA Computer Srl", "Panjea", "AZofLv:n0sbzPOs9WSqk5Axpz.kuVT13");
        Locale.setDefault(Locale.ITALIAN);
        LookAndFeelFactory.installDefaultLookAndFeelAndExtension();
        PanjeaFilterFactoryManager.initPanjeaFilterFactory();
        UIManager.getDefaults().put("Resizable.resizeBorder", new EmptyBorder(0, 0, 0, 0));

        installShortcut();

        new PanjeaApplicationLauncher(args);

    }
}
