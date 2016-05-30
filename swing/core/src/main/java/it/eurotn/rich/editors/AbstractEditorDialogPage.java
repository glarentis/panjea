package it.eurotn.rich.editors;

import java.awt.Color;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.ejb.EJBAccessException;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import org.apache.log4j.Logger;
import org.springframework.richclient.application.PageComponentContext;
import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.command.TargetableActionCommand;
import org.springframework.richclient.dialog.DialogPage;
import org.springframework.richclient.factory.AbstractControlFactory;
import org.springframework.richclient.progress.BusyIndicator;
import org.springframework.richclient.progress.ProgressMonitor;
import org.springframework.richclient.settings.Settings;
import org.springframework.richclient.settings.support.Memento;

import com.jidesoft.spring.richclient.docking.editor.AbstractEditor;

import it.eurotn.rich.command.support.JecGlobalCommandIds;
import it.eurotn.rich.dialog.JecCompositeDialogPage;

/**
 * Editor che gestisce una JecCompositeDialogPage.
 *
 * @author Aracno,Giangi
 * @version 1.0, 27-apr-2006,26-ott
 */
public abstract class AbstractEditorDialogPage extends AbstractEditor implements PropertyChangeListener, Memento {

    private final class CompositeDialogPagePropertyListener implements PropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent propertychangeevent) {
            LOGGER.debug("-->Cambiata la proprieta del compositeDialogPage: " + propertychangeevent.getPropertyName());
            if (JecCompositeDialogPage.PAGE_ACTIVE_PROPERTY.equals(propertychangeevent.getPropertyName())) {
                if (propertychangeevent.getNewValue() instanceof IEditorCommands) {
                    // Registro i globalCommand nel context
                    IEditorCommands pageEditor = (IEditorCommands) propertychangeevent.getNewValue();
                    getContext().register(JecGlobalCommandIds.NEW, pageEditor.getEditorNewCommand());
                    getContext().register(JecGlobalCommandIds.EDIT, pageEditor.getEditorLockCommand());
                    getContext().register(JecGlobalCommandIds.UNDO_MODEL, pageEditor.getEditorUndoCommand());
                    getContext().register(JecGlobalCommandIds.SAVE, pageEditor.getEditorSaveCommand());
                    getContext().register(JecGlobalCommandIds.DELETE, pageEditor.getEditorDeleteCommand());
                    getContext().register(JecGlobalCommandIds.PRINT, null);
                }

                // Aggiorno i globalCommand.
                // Normalmente vengono aggiornati sul focus dell'editor
                // pero' cambiando activePage non cambio editor
                for (Iterator<?> i = getActiveWindow().getSharedCommands(); i.hasNext();) {
                    TargetableActionCommand globalCommand = (TargetableActionCommand) i.next();
                    globalCommand.setCommandExecutor(getContext().getLocalCommandExecutor(globalCommand.getId()));
                }
            }
            // aggiorno l'oggetto dell'editor se e' cambiata la proprieta'
            // JecCompositeDialogPage.CURRENT_OBJECT_CHANGED_PROPERTY
            if (JecCompositeDialogPage.CURRENT_OBJECT_CHANGED_PROPERTY.equals(propertychangeevent.getPropertyName())) {
                AbstractEditorDialogPage.this.currentObject = propertychangeevent.getNewValue();
                AbstractEditorDialogPage.this.fireCurrentObjectPropertyChange();
            }
        }
    }

    private class NextActivePageCommand extends AbstractCommand {
        @Override
        public void execute() {
            compositeDialogPage.handleGoToNextPage();
        }
    }

    private class PrevActivePageCommand extends AbstractCommand {
        @Override
        public void execute() {
            compositeDialogPage.handleGoToPreviousPage();
        }
    }

    public static final String EDITOR_OBJECT_CHANGE = "editorObjectChange";

    private static final Logger LOGGER = Logger.getLogger(AbstractEditorDialogPage.class);

    private Object currentObject; // oggetto editato
    private CompositeDialogPagePropertyListener compositeDialogPagePropertyListener;
    protected JecCompositeDialogPage compositeDialogPage = null;
    protected boolean enabledOnOpen = false;// indica se l'editor deve essere
    // modificabile sull'apertura
    protected boolean allowClosing = true;
    private List<String> pages = new ArrayList<String>();
    private String id;
    private String dialogPageId;
    private final AbstractControlFactory factory = new AbstractControlFactory() {

        @Override
        public JComponent createControl() {
            return AbstractEditorDialogPage.this.createControl();
        }
    };
    private PrevActivePageCommand prevActivePageCommand;
    private NextActivePageCommand nextActivePageCommand;

    private List<PropertyChangeListener> currentObjectChangeListeners;
    private List<PropertyChangeListener> displayNameChangeListeners;

    /**
     * @param listener
     *            listener to add
     */
    public void addCurrentObjectChangeListeners(PropertyChangeListener listener) {
        if (currentObjectChangeListeners == null) {
            currentObjectChangeListeners = new ArrayList<PropertyChangeListener>();
        }
        currentObjectChangeListeners.add(listener);
    }

    /**
     * @param listener
     *            listener to add
     */
    public void addDisplayNameChangeListeners(PropertyChangeListener listener) {
        if (displayNameChangeListeners == null) {
            displayNameChangeListeners = new ArrayList<PropertyChangeListener>();
        }
        displayNameChangeListeners.add(listener);
        addPropertyChangeListener("displayName", listener);
    }

    /**
     * @return indica se l'editor contiene pagine dirty
     */
    @Override
    public boolean canClose() {
        return compositeDialogPage.canClose();
    }

    @Override
    public void close() {
    }

    /**
     * Giro il focus alla JecCompositeDialogPage.
     */
    @Override
    public void componentFocusGained() {
        LOGGER.debug("--> Enter componentFocusGained");
        compositeDialogPage.setActivePage(compositeDialogPage.getActivePage());
        compositeDialogPage.componentFocusGained();
        LOGGER.debug("--> Exit componentFocusGained");
    }

    /**
     * Restituisce la compositeDialogPage che decide come verranno gestite le varie DialogPage.
     *
     * @return compositeDialogPage
     */
    protected abstract JecCompositeDialogPage createCompositeDialogPage();

    /**
     * Creo i componenti dell'editor.
     *
     * @return controlli
     */
    public JComponent createControl() {
        BusyIndicator.showAt(getActiveWindow().getControl());

        JComponent component = null;
        try {
            initCompositeDialogPage();
            component = compositeDialogPage.getControl();
            component.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        } catch (RuntimeException e) {
            if (e.getCause() instanceof EJBAccessException) {
                component = new JTextField("PERMESSESSI NON SUFFICIENTI PER VISUALIZZARE LA PAGINA");
                ((JTextField) component).setHorizontalAlignment(SwingConstants.CENTER);
            } else {
                component = new JTextField("Errore nel creare i controlli per la pagina " + getId() + "\n" + e);
                ((JTextField) component).setHorizontalAlignment(SwingConstants.CENTER);
            }
            LOGGER.error("--Errore nel creare i controlli per la composite Page " + compositeDialogPage.getId(), e);
        } finally {
            BusyIndicator.clearAt(getActiveWindow().getControl());
        }
        return component;
    }

    @Override
    public void dispose() {
        LOGGER.debug("-->ENTER dispose");
        super.dispose();

        // Rimuovo dal context eventuali comandi registrati
        getContext().register(JecGlobalCommandIds.NEW, null);
        getContext().register(JecGlobalCommandIds.EDIT, null);
        getContext().register(JecGlobalCommandIds.UNDO_MODEL, null);
        getContext().register(JecGlobalCommandIds.SAVE, null);
        getContext().register(JecGlobalCommandIds.DELETE, null);
        getContext().register(JecGlobalCommandIds.PRINT, null);
        getContext().register(JecGlobalCommandIds.NEXT_PAGE, null);
        getContext().register(JecGlobalCommandIds.PREV_PAGE, null);

        for (Iterator<?> i = getActiveWindow().getSharedCommands(); i.hasNext();) {
            TargetableActionCommand globalCommand = (TargetableActionCommand) i.next();
            globalCommand.setCommandExecutor(null);
        }

        if (currentObjectChangeListeners != null) {
            currentObjectChangeListeners.clear();
            currentObjectChangeListeners = null;
        }

        if (displayNameChangeListeners != null) {
            for (PropertyChangeListener propertyChangeListener : displayNameChangeListeners) {
                removePropertyChangeListener("displayName", propertyChangeListener);
            }
            displayNameChangeListeners.clear();
            displayNameChangeListeners = null;
        }

        compositeDialogPage.removePropertyChangeListener(compositeDialogPagePropertyListener);
        compositeDialogPagePropertyListener = null;
        compositeDialogPage.dispose();
        LOGGER.debug("-->EXIT dispose");
    }

    /**
     * Fire object changed event.
     */
    public void fireCurrentObjectPropertyChange() {
        if (currentObjectChangeListeners != null) {
            for (PropertyChangeListener listener : currentObjectChangeListeners) {
                listener.propertyChange(new PropertyChangeEvent(this,
                        JecCompositeDialogPage.CURRENT_OBJECT_CHANGED_PROPERTY, null, this.currentObject));
            }
        }
    }

    /**
     * @return composite dialog page.
     */
    public JecCompositeDialogPage getCompositeDialogPage() {
        return compositeDialogPage;
    }

    /**
     * Restituisco i componenti dell'Editor creati tramite il @link AbstractControlFactory.
     *
     * @return componenti creati
     */
    @Override
    public JComponent getControl() {
        return factory.getControl();
    }

    /**
     * @return dialog page id
     */
    public String getDialogPageId() {
        return dialogPageId;
    }

    /**
     * @return dialog pages
     */
    public List<DialogPage> getDialogPages() {
        return compositeDialogPage.getDialogPages();
    }

    /**
     * Devo ritornare il mio riferimento currentObject che aggiorno quando la jecComposite si aggiorna il suo
     * currentObject e non l'editorObject che non viene aggiornato in questo caso per evitare set ciclici tra editor e
     * composite e pagine.
     *
     * @return Object
     */
    @Override
    public Object getEditorInput() {
        return currentObject;
    }

    @Override
    public String getId() {
        return id;
    }

    /**
     * Restituice il command per settare come pagina attiva la pagina successiva a quella attuale, prendendo come ordine
     * quello di inserimento, delegando cmq l'operazione alla compositeDialogPage.
     *
     * @return command
     */
    private AbstractCommand getNextActivePageCommand() {
        if (nextActivePageCommand == null) {
            nextActivePageCommand = new NextActivePageCommand();
        }

        return nextActivePageCommand;
    }

    /**
     * Restituice il command per settare come pagina attiva la pagina precedente a quella attuale, prendendo come ordine
     * quello di inserimento, delegando cmq l'operazione alla compositeDialogPage.
     *
     * @return command
     */
    private AbstractCommand getPrevActivePageCommand() {
        if (prevActivePageCommand == null) {
            prevActivePageCommand = new PrevActivePageCommand();
        }

        return prevActivePageCommand;
    }

    protected void initCompositeDialogPage() {
        LOGGER.debug("--> Enter initCompositeDialogPage");
        compositeDialogPage = createCompositeDialogPage();
        compositeDialogPage.setCurrentObject(currentObject);
        LOGGER.debug("Creata la compositeDialogPage " + compositeDialogPage.hashCode());
        compositeDialogPage.setEnabled(enabledOnOpen);
        compositeDialogPagePropertyListener = new CompositeDialogPagePropertyListener();
        // Mi registro al cambio di una proprietà
        compositeDialogPage.addPropertyChangeListener(compositeDialogPagePropertyListener);

        // Setto le pagine passate come id
        for (String idPage : pages) {
            LOGGER.debug("-->Aggiungo la pagina " + idPage + " alla JecCompositePage " + compositeDialogPage);
            compositeDialogPage.addPage(idPage);
        }
        LOGGER.debug("--> Exit initCompositeDialogPage");
    }

    /**
     * setto l'oggetto che l'editor deve visualizzare/modificare.
     *
     * @param editorObject
     *            oggetto gestito dall'editor
     */
    @Override
    public void initialize(Object editorObject) {
        LOGGER.debug("--> Enter initialize " + editorObject);
        currentObject = editorObject;
        // se i controlli sono già stati creati, ho quindi false la proprietà
        // closeAndReopenEditor di WorkspaceView,
        if (compositeDialogPage != null) {
            compositeDialogPage.setCurrentObject(currentObject);
        }
        LOGGER.debug("--> Exit initialize");
    }

    @Override
    public boolean isDirty() {
        return compositeDialogPage.isDirty();
    }

    /**
     * @return Returns the isEnabledOnOpen.
     */
    public boolean isEnabledOnOpen() {
        return enabledOnOpen;
    }

    /**
     * Definisce se l'editor supporta la modifica del layout. In caso verrà inserito sulla tab dell'editor il command
     * ripristinaLayout che chiama il restoreLayout dell'editor
     *
     * @return true se l'editor supporta la modifica del layout
     */
    public boolean isLayoutSupported() {
        return compositeDialogPage.isLayoutSupported();
    }

    /**
     *
     * @return stato del layout
     */
    public boolean isLockLayout() {
        return compositeDialogPage.isLockLayout();
    }

    /**
     * Da sovrascrivere quando si deve bloccare/sbloccare la possibilità di modificare un layout se supportato dalla
     * composite.
     *
     * @param lock
     *            true o false per bloccare o sbloccare il layout
     */
    public void lockLayout(boolean lock) {
        compositeDialogPage.lockLayout(lock);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
    }

    /**
     * Sovrascrivo il metodo per registrare i commands per cambiare la pagina attiva le diverse disponibili della
     * jecCompositeDialogPage contenuta nell'editor.
     *
     * @param context
     */
    @Override
    protected void registerLocalCommandExecutors(PageComponentContext context) {
        getContext().register(JecGlobalCommandIds.NEXT_PAGE, getNextActivePageCommand());
        getContext().register(JecGlobalCommandIds.PREV_PAGE, getPrevActivePageCommand());
    }

    /**
     * Transfer focus to the first page.
     */
    public void requestFocusForFirstPage() {
        compositeDialogPage.handleGoToFirstPage();
    }

    /**
     * Da sovrascrivere quando si deve ripristinare un layout se supportato dalla composite.
     */
    public void restoreLayout() {
        compositeDialogPage.restoreLayout();
    }

    /**
     * Delega alla composite le operazioni di restore del contenuto dell'editor.
     *
     * @param settings
     *            settings
     */
    @Override
    public void restoreState(Settings settings) {
        compositeDialogPage.restoreState(settings);
    }

    /**
     * Metodo non utilizzato ereditato dall'interfaccia.
     */
    @Override
    public void save(ProgressMonitor progressmonitor) {
    }

    /**
     * Delega alla composite le operazioni di salvataggio del contenuto dell'editor.
     *
     * @param settings
     *            settings
     */
    @Override
    public void saveState(Settings settings) {
        compositeDialogPage.saveState(settings);
    }

    /**
     * @param paramId
     *            dialog page id
     */
    public void setDialogPageId(String paramId) {
        this.dialogPageId = paramId;
    }

    /**
     * @param enabledOnOpen
     *            the enabledOnOpen to set
     */
    public void setEnabledOnOpen(boolean enabledOnOpen) {
        this.enabledOnOpen = enabledOnOpen;
    }

    /**
     * @param id
     *            the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @param paramPages
     *            the pages to set
     */
    public void setIdPages(List<String> paramPages) {
        this.pages = paramPages;
    }
}
