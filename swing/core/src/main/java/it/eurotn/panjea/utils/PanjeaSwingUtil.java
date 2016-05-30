package it.eurotn.panjea.utils;

import java.awt.Color;
import java.awt.Component;
import java.awt.DefaultKeyboardFocusManager;
import java.awt.EventQueue;
import java.awt.Insets;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Properties;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.text.JTextComponent;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.converters.BigDecimalConverter;
import org.apache.commons.beanutils.converters.DateConverter;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.binding.form.ValidatingFormModel;
import org.springframework.binding.form.support.DefaultFieldMetadata;
import org.springframework.binding.form.support.FormModelMediatingValueModel;
import org.springframework.binding.value.ValueModel;
import org.springframework.binding.value.support.ValueHolder;
import org.springframework.richclient.application.Application;
import org.springframework.richclient.security.ApplicationSecurityManager;
import org.springframework.richclient.util.RcpSupport;
import org.springframework.util.Assert;

import com.jidesoft.swing.JideSwingUtilities;
import com.toedter.calendar.JDateChooser;
import com.toedter.calendar.JTextFieldDateEditor;

import it.eurotn.panjea.exceptions.PanjeaRuntimeException;
import it.eurotn.panjea.sicurezza.JecPrincipalSpring;
import it.eurotn.rich.binding.PanjeaTextFieldDateEditor;
import it.eurotn.rich.binding.searchtext.SearchPanel;
import it.eurotn.rich.binding.searchtext.SearchTextField;
import it.eurotn.rich.components.htmleditor.HTMLEditorPane;
import it.eurotn.rich.settings.PropertiesSettingsFactory;

/**
 * Classe di utilita'.
 *
 * @author Leonardo
 */
public final class PanjeaSwingUtil extends JideSwingUtilities {

    public static class FocusBorder extends CompoundBorder {
        private static final long serialVersionUID = -1396250213346461982L;

        /**
         * Bordo da applicare al componente selezionato.
         *
         * @param paramBorder
         *            border del componente prima della selezione. Verrà utilizzato per risettarlo una volta che il
         *            componente perderà il focus.
         */
        public FocusBorder(final Border paramBorder) {
            super(BorderFactory.createLineBorder(Color.GREEN, 1), paramBorder);
        }

        @Override
        public Insets getBorderInsets(Component paramComponent) {
            return getInsideBorder().getBorderInsets(paramComponent);
        }

        @Override
        public Insets getBorderInsets(Component paramComponent, Insets paramInsets) {
            return getInsideBorder().getBorderInsets(paramComponent);
        }
    }

    private static class TraceFocusPropertyChangeListener implements PropertyChangeListener {
        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            final Component oldValue = (Component) evt.getOldValue();
            if (oldValue instanceof JComponent) {
                final Border oldBorder = ((JComponent) oldValue).getBorder();
                if (oldBorder instanceof FocusBorder) {
                    ((JComponent) oldValue).setBorder(((FocusBorder) oldBorder).getInsideBorder());
                }
            }

            final Component newValue = (Component) evt.getNewValue();
            if (newValue instanceof JComponent) {
                Border oldBorder = ((JComponent) newValue).getBorder();
                if (oldBorder == null) {
                    oldBorder = new EmptyBorder(0, 0, 0, 0);
                }
                if (!(oldBorder instanceof FocusBorder)) {
                    ((JComponent) newValue).setBorder(new FocusBorder(oldBorder));
                }

                // seleziono tutto il testo se si tratta di un text component
                if (newValue instanceof JTextComponent) {
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            ((JTextComponent) newValue).selectAll();
                        }
                    });
                }
            }
        }
    }

    private static final Logger LOGGER = Logger.getLogger(PanjeaSwingUtil.class);

    private static TraceFocusPropertyChangeListener traceFocusListener;

    static {
        traceFocusListener = new TraceFocusPropertyChangeListener();
    }

    /**
     * Aggiunge una proprietà fittizzia al form model
     *
     * @param value
     *            valore da aggiungere
     * @param formModel
     *            formmodel
     * @param clazz
     *            classe
     * @param propertyName
     *            property name
     * @param readOnly
     *            true creala proprietà in sola lettura
     */
    public static void addValueModelToForm(Object value, ValidatingFormModel formModel, Class<? extends Object> clazz,
            String propertyName, boolean readOnly) {
        final ValueModel valueModel = new ValueHolder(value);
        final DefaultFieldMetadata metaData = new DefaultFieldMetadata(formModel,
                new FormModelMediatingValueModel(valueModel), clazz, readOnly, null);
        formModel.add(propertyName, valueModel, metaData);
    }

    /**
     * Rimuovo le eccezioni di Runtime aggiunte da eventuali Rollback nella parte server e rilancio una
     * {@link PanjeaRuntimeException}.
     *
     * @param exc
     *            eccezione da controllare
     */
    public static void checkAndThrowException(Throwable exc) {
        if (!(exc instanceof RuntimeException)) {
            throw new PanjeaRuntimeException(exc);
        } else {
            throw (RuntimeException) exc;
        }
    }

    /**
     * Deep clone di un oggetto.
     *
     * @param oldObj
     *            oggetto da clonare
     * @return oggetto clonato
     */
    public static Object cloneObject(Object oldObj) {
        ObjectOutputStream oos = null;
        ObjectInputStream ois = null;
        try {
            final ByteArrayOutputStream bos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(bos);
            // serialize and pass the object
            oos.writeObject(oldObj);
            oos.flush();
            final ByteArrayInputStream bin = new ByteArrayInputStream(bos.toByteArray());
            ois = new ObjectInputStream(bin);
            // return the new object
            return ois.readObject();
        } catch (final Exception e) {
            LOGGER.error("-->Exception in ObjectCloner ", e);
            return null;
        } finally {
            try {
                if (oos != null) {
                    oos.close();
                }
                if (ois != null) {
                    ois.close();
                }
            } catch (final IOException e) {
                LOGGER.error("--> IOEXception nella clone dell'oggetto ", e);
            }
        }
    }

    /**
     * Metodo di utilita' per compilare un pattern da una espressione regolare.
     *
     * @param pattern
     *            la regExp da compilare
     * @return Pattern o null se c'e' un errore di sintassi nella regExp
     */
    public static Pattern compilePattern(String pattern) {
        try {
            return Pattern.compile(pattern, Pattern.CASE_INSENSITIVE);
        } catch (final PatternSyntaxException e) {
            LOGGER.warn("--> Attenzione! controllare il valore inserito nell'input ricerca/filtra della tabella: "
                    + e.getMessage());
            return null;
        }
    }

    /**
     * Concatena i 2 array passati come parametro.
     *
     * @param firstArray
     *            primo array
     * @param secondArray
     *            secondo array
     * @return array concatenato
     */
    public static String[] concatArray(String[] firstArray, String[] secondArray) {
        final String[] result = new String[firstArray.length + secondArray.length];
        System.arraycopy(firstArray, 0, result, 0, firstArray.length);
        System.arraycopy(secondArray, 0, result, firstArray.length, secondArray.length);

        return result;
    }

    /**
     *
     * @param destinazione
     *            bean di destinazione
     * @param origine
     *            bean di origine
     */
    public static void copyProperties(Object destinazione, Object origine) {
        BeanUtilsBean.getInstance().getConvertUtils().register(new BigDecimalConverter(null), BigDecimal.class);
        BeanUtilsBean.getInstance().getConvertUtils().register(new DateConverter(null), Date.class);
        try {
            BeanUtils.copyProperties(destinazione, origine);
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Ritorna il primo compomente focusable.
     *
     * @param components
     *            componenti nel quale cercare il componente al quale dare il focus
     * @return primo componente al quale dare il focus
     */
    public static Component findComponentFocusable(Component[] components) {
        for (final Component component : components) {
            LOGGER.debug("--> COMPONENT " + component);
            if (component instanceof JTextField) {
                return component;
            } else if (component instanceof SearchTextField) {
                return ((SearchTextField) component).getTextField();
            } else if (component instanceof it.eurotn.rich.binding.searchtext.SearchPanel) {
                return (((SearchPanel) component).getFocusableSearchText()).getTextField();
            } else if (component instanceof JTextArea) {
                return component;
            } else if (component instanceof HTMLEditorPane) {
                return ((HTMLEditorPane) component).getWysEditor();
            } else if (component instanceof JDateChooser) {
                return (PanjeaTextFieldDateEditor) ((JDateChooser) component).getDateEditor();
            } else if (component instanceof JTabbedPane) {
                final Component focusableComponent = findComponentFocusable(((JComponent) component).getComponents());
                if (focusableComponent != null) {
                    return focusableComponent;
                }
            } else if (component instanceof JComponent) {
                final Component focusableComponent = findComponentFocusable(((JComponent) component).getComponents());
                if (focusableComponent != null) {
                    return focusableComponent;
                }
            }
        }
        return null;
    }

    /**
     * Funzione per formattare una Date in una stringa formattata secondo il pattern dd/MM/yyyy.
     *
     * @param date
     *            la data da formattare
     * @return la stringa che rappresenta la data ricevuta da formattare
     */
    public static String getFormattedDate(Date date) {
        final DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        return format.format(date);
    }

    /**
     *
     * @return home di panjea per i file di settings.
     */
    public static Path getHome() {
        final PropertiesSettingsFactory settingsFactory = RcpSupport.getBean(PropertiesSettingsFactory.BEAN_ID);
        return settingsFactory.getHome();
    }

    /**
     * Date due collezioni old e new, questo metodo recupera gli elementi da eliminare nella old collection per avere la
     * new collection.
     *
     * @param oldCollection
     *            la collection old
     * @param newCollection
     *            la collection new
     * @return oldCollection - newCollection
     */
    @SuppressWarnings("rawtypes")
    public static Collection getItemsToDelete(Collection oldCollection, Collection newCollection) {
        LOGGER.debug("---> Enter getItemsToDelete oldCollection-newCollection");
        LOGGER.debug("---> oldCollection " + oldCollection);
        LOGGER.debug("---> newCollection " + newCollection);
        if (oldCollection == null) {
            return new ArrayList();
        }
        final Collection result = CollectionUtils.subtract(oldCollection, newCollection);
        LOGGER.debug("---> Exit getItemsToDelete");
        return result;
    }

    /**
     * Date due collezioni old e new, questo metodo recupera gli elementi da aggiungere nella old collection per avere
     * la new collection.
     *
     * @param oldCollection
     *            la collection old
     * @param newCollection
     *            la collection new
     * @return newCollection - oldCollection
     */
    @SuppressWarnings("rawtypes")
    public static Collection getItemsToInsert(Collection oldCollection, Collection newCollection) {
        LOGGER.debug("---> Enter getItemsToInsert newCollection-oldCollection");
        LOGGER.debug("---> oldCollection " + oldCollection);
        LOGGER.debug("---> newCollection " + newCollection);
        if (oldCollection == null) {
            return newCollection;
        }

        final Collection result = CollectionUtils.subtract(newCollection, oldCollection);
        LOGGER.debug("---> Exit getItemsToInsert");
        return result;
    }

    /**
     *
     * @return url del server impostato durante l'installazioen. NB:Il contesto di Spring può non essere inizializzato.
     */
    public static String getServerUrl() {
        try {
            Path path = Paths.get(PanjeaSwingUtil.class.getProtectionDomain().getCodeSource().getLocation().toURI());
            if (path.endsWith("bin")) {
                path = Paths.get(System.getProperty("user.home")).resolve(".panjeaDev");
            } else {
                path = path.getParent();
            }
            path = path.resolve("server.properties");
            final Properties prop = new Properties();
            try {
                prop.load(new FileInputStream(path.toFile()));
                return prop.get("java_naming_provider_url").toString();
            } catch (final IOException e) {
                LOGGER.error("-->errore nel lettere il file server.properties", e);
                throw new RuntimeException(e);
            }
        } catch (final URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Recupera l'utente corrente dall'applicationSecurityManager.
     *
     * @return JecPrincipal
     */
    public static JecPrincipalSpring getUtenteCorrente() {
        final ApplicationSecurityManager appSec = (ApplicationSecurityManager) Application.services()
                .getService(ApplicationSecurityManager.class);
        return (JecPrincipalSpring) appSec.getAuthentication();
    }

    /**
     * Cerca il primo componente che può accettare il focus e lo richiede, se e' un {@link JTextField} seleziona tutto
     * il contenuto del componente in modo da allinearsi al comportamento dell'applicazione che ha installato il
     * {@link PanjeaSelectAllFormComponentInterceptorFactory}.
     *
     * @param components
     *            i componenti da scorrere per trovare un {@link Component} focusable
     */
    public static void giveFocusToComponent(Component[] components) {
        final Component componentFocusable = findComponentFocusable(components);
        if (componentFocusable != null && componentFocusable instanceof JComponent) {
            ((JComponent) componentFocusable).requestFocusInWindow();
            // se e' un text field seleziono tutto il valore
            if (componentFocusable instanceof JTextField) {
                ((JTextField) componentFocusable).selectAll();
            }
            if (componentFocusable instanceof JTextPane) {
                ((JTextPane) componentFocusable).selectAll();
            }
            if (componentFocusable instanceof JTextFieldDateEditor) {
                ((JTextFieldDateEditor) componentFocusable).selectAll();
            }
        }
    }

    /**
     *
     * @param permesso
     *            permesso da testare
     * @return true se l'utente corrente ha il permesso richiesto
     */
    public static boolean hasPermission(String permesso) {

        final ApplicationSecurityManager appSec = (ApplicationSecurityManager) Application.services()
                .getService(ApplicationSecurityManager.class);
        return appSec.isUserInRole(permesso);
    }

    /**
     * Aggiunge all'applicazione il messaggio indicato nel glasspane della finestra principale.
     *
     * @param message
     *            messaggio
     */
    public static void lockScreen(String message) {
        Application.instance().getActiveWindow().getControl().setGlassPane(
                new JecGlassPane(Application.instance().getActiveWindow().getControl().getContentPane(), message));
        Application.instance().getActiveWindow().getControl().getGlassPane().setVisible(true);
    }

    /**
     * Rimuove alcuni tag HTML dal testo (HTML|<|>|b|u|font).
     *
     * @param htmlText
     *            testo dal quale rimuovere i tag
     * @return testo con i tag rimossi
     */
    public static String removeHtml(String htmlText) {
        return htmlText.replaceAll("(?i)</?(HTML|font*|b|u|i)\\b[^>]*>", "");
    }

    /**
     * Rimuove la selezione di tutto il testo sul focus
     */
    public static void removeTraceFocusAndSelectAll() {
        DefaultKeyboardFocusManager.getCurrentKeyboardFocusManager().removePropertyChangeListener("focusOwner",
                traceFocusListener);
        DefaultKeyboardFocusManager.getCurrentKeyboardFocusManager().removePropertyChangeListener("permanentFocusOwner",
                traceFocusListener);
        DefaultKeyboardFocusManager.getCurrentKeyboardFocusManager().removePropertyChangeListener("activeWindow",
                traceFocusListener);
    }

    /**
     * Reallocates an array with a new size, and copies the contents of the old array to the new array.
     *
     * @param oldArray
     *            the old array, to be reallocated.
     * @param newSize
     *            the new array size.
     * @return A new array with the same contents.
     */
    public static Object resizeArray(Object oldArray, int newSize) {
        final int oldSize = java.lang.reflect.Array.getLength(oldArray);
        final Class<?> elementType = oldArray.getClass().getComponentType();
        final Object newArray = java.lang.reflect.Array.newInstance(elementType, newSize);
        final int preserveLength = Math.min(oldSize, newSize);
        if (preserveLength > 0) {
            System.arraycopy(oldArray, 0, newArray, 0, preserveLength);
        }
        return newArray;
    }

    /**
     * Ensures the given runnable is executed in the event dispatcher thread and waits until executin is completed.
     *
     * @param runnable
     *            the runnable.
     *
     * @see #runInEventDispatcherThread(Runnable, Boolean)
     */
    public static void runInEventDispatcherThread(Runnable runnable) {

        PanjeaSwingUtil.runInEventDispatcherThread(runnable, Boolean.TRUE);
    }

    /**
     * Ensures the given runnable is executed into the event dispatcher thread.
     *
     * @param runnable
     *            the runnable.
     * @param wait
     *            whether should wait until execution is completed.
     */
    public static void runInEventDispatcherThread(Runnable runnable, Boolean wait) {

        Assert.notNull(runnable, "runnable");
        Assert.notNull(wait, "wait");

        if (EventQueue.isDispatchThread()) {
            runnable.run();
        } else if (wait) {
            try {
                EventQueue.invokeAndWait(runnable);
            } catch (final Exception e) {
                PanjeaSwingUtil.checkAndThrowException(e);
            }
        } else {
            EventQueue.invokeLater(runnable);
        }
    }

    /**
     * Setta il bordo al componente selezionato e se il componente è istanza di {@link JTextComponent} seleziona tutto
     * il testo.
     */
    public static void traceFocusAndSelectAll() {
        DefaultKeyboardFocusManager.getCurrentKeyboardFocusManager().addPropertyChangeListener("focusOwner",
                traceFocusListener);
        DefaultKeyboardFocusManager.getCurrentKeyboardFocusManager().addPropertyChangeListener("permanentFocusOwner",
                traceFocusListener);
        DefaultKeyboardFocusManager.getCurrentKeyboardFocusManager().addPropertyChangeListener("activeWindow",
                traceFocusListener);
    }

    /**
     * Rimuove della finestra principale il contenuto del glasspane e lo nasconde.
     */
    public static void unlockScreen() {
        Application.instance().getActiveWindow().getControl().getGlassPane().setVisible(false);
    }

    /**
     * Costruttore privato essendo una classe di utilità.
     */
    private PanjeaSwingUtil() {
        super();
    }

}
