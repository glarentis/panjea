/**
 *
 */
package it.eurotn.panjea.rich.pages;

import it.eurotn.panjea.rich.PanjeaLifecycleApplicationEvent;
import it.eurotn.rich.command.support.JecGlobalCommandIds;
import it.eurotn.rich.control.table.DefaultBeanTableModel;
import it.eurotn.rich.control.table.JideTableWidget;

import java.awt.BorderLayout;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.swing.JComponent;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ApplicationEventMulticaster;
import org.springframework.richclient.application.Application;
import org.springframework.richclient.application.PageComponentContext;
import org.springframework.richclient.application.event.LifecycleApplicationEvent;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.command.CommandGroup;
import org.springframework.richclient.settings.Settings;

import com.jidesoft.spring.richclient.docking.editor.OpenEditorEvent;

/**
 * Editor astratto derivato dall AbstractSearchResult che aggiunge una Jtable come struttura principale visualizzata
 * nell'editor, fornisce il supporto per interagire con la lista.
 * 
 * @author Leonardo
 */
public abstract class AbstractTableSearchResult<T> extends AbstractSearchResult<T>implements ApplicationListener {

    private class OpenEditorCommand extends ActionCommand {

        @Override
        protected void doExecuteCommand() {
            // creo e lancio un OpenEditorEvent con l'oggetto selezionato
            if (tableWidget.getSelectedObject() != null) {
                Object object = reloadObject(tableWidget.getSelectedObject());
                LifecycleApplicationEvent event = new OpenEditorEvent(object);
                Application.instance().getApplicationContext().publishEvent(event);
            }
        }

    }

    private static Logger logger = Logger.getLogger(AbstractTableSearchResult.class);

    private JComponent panel = null;

    private JideTableWidget<T> tableWidget = null;

    private OpenEditorCommand openEditorCommand;

    /**
     * Costruttore.
     */
    public AbstractTableSearchResult() {
        super();
    }

    @Override
    public void componentFocusGained() {
        super.componentFocusGained();
        logger.debug("---> component focus gainged della search result, passo il focus alla tabella");
        this.tableWidget.getTable().requestFocusInWindow();
    }

    /**
     * Metodo da sovrascrivere per effetturare personalizzazioni alla tabella creata.
     * 
     * @param table
     *            tabella
     */
    protected void configureTable(JideTableWidget<T> table) {

    }

    /**
     * crea il componente della tabella.
     * 
     * @return componente creato
     */
    protected JComponent createTable() {
        logger.debug("---> Enter createTable");

        DefaultBeanTableModel<T> tableModel = getTableModel();
        if (tableModel != null) {
            tableWidget = new JideTableWidget<T>(getId() + ".table", tableModel);
        } else {
            tableWidget = new JideTableWidget<T>(getId() + ".table", getColumnPropertyNames(), getObjectsClass());
        }
        tableWidget.setPropertyCommandExecutor(getOpenEditorCommand());
        logger.debug("---> Exit createTable");
        return tableWidget.getComponent();
    }

    /**
     * Elimina un singolo record dall'eventList se la cancellazione dell'oggetto va a buon fine
     * AbstractSearchResult.delete.
     * 
     * @return oggetto cancellato
     */
    @Override
    protected Object delete() {
        final T objToRemove = tableWidget.getSelectedObject();
        if (objToRemove == null) {
            return objToRemove;
        }
        Object deletedObj = doDelete(objToRemove);
        if (deletedObj != null) {
            tableWidget.removeRowObject(objToRemove);
        }
        return deletedObj;
    }

    @Override
    public void dispose() {
        panel = null;
        tableWidget = null;
        // rimozione dell'istanza corrente da ApplicationEventMulticaster
        ApplicationEventMulticaster applicationEvent = getApplicationEvent();
        if (applicationEvent != null) {
            applicationEvent.removeApplicationListener(this);
        }
        super.dispose();
    }

    /**
     * Metodo da implementare per l'eliminazione di un record dalla tabella.
     * 
     * @param objectToDelete
     *            oggetto da cancellare.
     * @return true se la cancellazione e' andata a buon fine, false altrimenti
     */
    protected abstract T doDelete(T objectToDelete);

    /**
     * Svuota e successivamente riempie l' eventList.
     * 
     * @param parameters
     *            la mappa di parametri per la ricerca
     */
    @Override
    protected void executeSearch(Map<String, Object> parameters) {
        logger.debug("---> Enter executeSearch");
        viewResults(getData(parameters));

        if (tableWidget.getRows().size() == 1) {
            getOpenEditorCommand().execute();
        }
        logger.debug("---> Exit executeSearch");
    }

    /**
     * Metodo da implementare per fornire il nome delle colonne della tabella.
     * 
     * @return String[] l'array col nome delle colonne
     */
    protected abstract String[] getColumnPropertyNames();

    /**
     * Metodo da implementare per fornire la collection da usare per l'eventList.
     * 
     * @param parameters
     *            i parametri usati per la ricerca
     * @return Collection di elementi
     */
    protected abstract Collection<T> getData(Map<String, Object> parameters);

    /**
     * Ritorna la classe degli oggetti gestiti.
     * 
     * @return class
     */
    protected abstract Class<T> getObjectsClass();

    /**
     * @return the openEditorCommand
     */
    public OpenEditorCommand getOpenEditorCommand() {
        if (openEditorCommand == null) {
            openEditorCommand = new OpenEditorCommand();
        }

        return openEditorCommand;
    }

    /**
     * ritorna la mappa di parametri da utilizzare nel metodo getData, legato al metodo per caricare la collection da
     * usare per il riempimento della tabella.
     * 
     * @return Map
     */
    protected abstract Map<String, Object> getParameters();

    @Override
    protected CommandGroup getPopupCommandGroup() {
        logger.debug("---> Enter getPopupCommandGroup");
        CommandGroup commandGroup = getCommandGroup(getId() + ".popupMenu");
        logger.debug("---> Exit getPopupCommandGroup " + commandGroup);
        return commandGroup;
    }

    /**
     * Crea il control di questo editor visualizzando una tabella con la lista definita dalla classe estesa.
     * 
     * @return JComponent
     */
    @Override
    protected JComponent getSearchControl() {
        logger.debug("---> Enter getSearchControl");
        if (panel == null) {
            panel = getComponentFactory().createPanel(new BorderLayout());
            panel.add(createTable(), BorderLayout.CENTER);
            configureTable(tableWidget);
        }
        logger.debug("---> Exit getSearchControl");
        return panel;
    }

    /**
     * @return oggetti selezionati
     */
    public List<T> getSelectedObjects() {
        return tableWidget.getSelectedObjects();
    }

    /**
     * Restituisce il table model che la tabella dovrà utilizzare. Se il metodo non viene implementato nella classe
     * derivata la tabella verrà creata utilizzando un table model standard usando come colonne da visualizzare quelle
     * restituite dal metodo {@link AbstractTableSearchResult#getColumnPropertyNames()}
     * 
     * @return table model
     */
    protected DefaultBeanTableModel<T> getTableModel() {
        return null;
    }

    /**
     * @return the tableWidget
     */
    public JideTableWidget<T> getTableWidget() {
        return tableWidget;
    }

    @Override
    public boolean isDirty() {
        return false;
    }

    /**
     * Intercetto gli eventi creato modificato e cancellato aggiornando cos� la lista di elementi della tablella.
     * 
     * @param applicationEvent
     *            evento dell'applicazione
     */

    @SuppressWarnings("unchecked")
    @Override
    public void onApplicationEvent(ApplicationEvent applicationEvent) {

        // se l'evento e' del ciclo di vita dell'applicazione
        if (applicationEvent instanceof PanjeaLifecycleApplicationEvent) {

            // nel caso in cui utilizzo eventi particolari ad es di chiusura
            // applicazione, dove non passo un obj
            // rilevante e quindi passo una stringa; in questi casi non serve
            // procedere oltre nel controllo dell'evento
            if (applicationEvent.getSource() instanceof String) {
                return;
            }

            // il ciclo di vita dell'applicazione per recuperare il tipo
            PanjeaLifecycleApplicationEvent le = (PanjeaLifecycleApplicationEvent) applicationEvent;
            Object eventObject = applicationEvent.getSource();

            // TODO ogni volta che viene aperta una search result viene aggiunta una nuova istanza
            // all'ApplicationEventMulticaster e quindi rimangono delle istanze non più utilizzate che vengono comunque
            // notificate, table widget in queste istanze è null
            if (getObjectsClass().getName().equals(eventObject.getClass().getName()) && tableWidget != null) {
                if (le.getEventType().equals(LifecycleApplicationEvent.CREATED)
                        || le.getEventType().equals(LifecycleApplicationEvent.MODIFIED)) {
                    tableWidget.replaceOrAddRowObject((T) eventObject, (T) eventObject, null);
                } else if (le.getEventType().equals(LifecycleApplicationEvent.DELETED)) {
                    tableWidget.removeRowObject((T) eventObject);
                }
            }
        }

    }

    @Override
    protected void registerSearchResultCommandsExecutors(PageComponentContext context) {
        context.register(JecGlobalCommandIds.DELETE, getDeleteCommand());
    }

    /**
     * metodo utilizzato per ricaricare l'oggetto da aprire nell'editor.
     * 
     * @param object
     *            object
     * @return oggetto ricaricato
     */
    public abstract Object reloadObject(T object);

    /**
     * Ricarica le impostazioni salvate per la pagina.
     * 
     * @param settings
     *            settings
     */
    @Override
    public void restoreState(Settings settings) {
        tableWidget.restoreState(settings);
    }

    /**
     * Salva le impostazioni della pagina.
     * 
     * @param settings
     *            settings
     */
    @Override
    public void saveState(Settings settings) {
        tableWidget.saveState(settings);
    }

    @Override
    public void viewResults(Collection<T> results) {
        tableWidget.setRows(results);
        tableWidget.getTable().requestFocusInWindow();
    }
}
