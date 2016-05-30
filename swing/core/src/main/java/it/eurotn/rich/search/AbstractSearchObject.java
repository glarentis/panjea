/**
 *
 */
package it.eurotn.rich.search;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.binding.convert.ConversionExecutor;
import org.springframework.binding.convert.ConversionService;
import org.springframework.binding.form.FormModel;
import org.springframework.richclient.application.Application;
import org.springframework.richclient.application.support.ApplicationServicesAccessor;
import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.command.CommandGroup;
import org.springframework.richclient.settings.Settings;
import org.springframework.richclient.util.RcpSupport;

import it.eurotn.locking.IDefProperty;
import it.eurotn.rich.binding.searchtext.SearchPanel;
import it.eurotn.rich.binding.searchtext.SearchTextField;
import it.eurotn.rich.dialog.DefaultTitledPageApplicationDialog;
import it.eurotn.rich.editors.IFormPageEditor;
import it.eurotn.rich.editors.IPageLifecycleAdvisor;

/**
 * Classe che configura una ricerca incrementale per il binding.<br/>
 * La classe incorpora sia il model che la view per il componente di ricerca.<br/>
 */
public abstract class AbstractSearchObject extends ApplicationServicesAccessor {

    public enum ElementNotFindAction {
        NULL_VALUE, EMPTY_INSTANCE, NEW
    }

    private static Logger logger = Logger.getLogger(AbstractSearchObject.class);

    private String id = null;

    protected SearchPanel searchPanel = null;
    private List<String> columns = null;
    private List<AbstractCommand> commands = null;
    private CommandGroup commandGroup = null;
    private Settings settings = null;

    protected String dialogPageId = null;
    /**
     * Definisce se e' possibile accedere all'oggetto della searchText per modifica/inserimento<br/>
     * vengono chiamati openDialogPage e openEditor; se viene iniettato dialogPageId risulta
     * disponibile la modifica via dialogPage.
     */
    private boolean editObject = false;

    /**
     *
     */
    public AbstractSearchObject() {
        super();
    }

    /**
     * Constructor.
     *
     * @param id
     *            id
     */
    public AbstractSearchObject(final String id) {
        this();
        this.id = id;
    }

    /**
     * Metodo per configurare la search text (nuova) se necessario dopo che i controlli sono stati
     * creati.
     *
     * @param searchTextField
     *            {@link SearchTextField}
     */
    public void configureSearchText(SearchTextField searchTextField) {

    }

    /**
     * Restituisce una nuova istanza per l'oggetto gestito dalla search object.
     *
     * @return nuova istanza
     */
    public Object createNewInstance() {
        Object object = null;
        try {
            object = searchPanel.getClassSearchObject().newInstance();
        } catch (Exception e) {
            logger.error("--> errore durante la creazione del nuovo oggetto per la classe "
                    + searchPanel.getClassSearchObject(), e);
            throw new RuntimeException(
                    "errore durante la creazione del nuovo oggetto per la classe " + searchPanel.getClassSearchObject(),
                    e);
        }
        return object;
    }

    /**
     *
     * @return lista della colonne da visualizzare. Se no viene eseguito un setColumns ritorno una
     *         sola colonna con header "Descrizione"
     */
    public List<String> getColumns() {
        return columns;
    }

    /**
     *
     * @return commandGroup con i comandi contenuti nella getCommands() e getCustomCommands
     */
    public CommandGroup getCommandGroup() {
        if (commandGroup == null) {
            commandGroup = new CommandGroup();
            for (Iterator<AbstractCommand> iter = getCommands().iterator(); iter.hasNext();) {
                Object element = iter.next();
                AbstractCommand command = (AbstractCommand) element;
                commandGroup.add(command);
            }
            commandGroup.addSeparator();
            if (getCustomCommands() != null && !getCustomCommands().isEmpty()) {
                for (Iterator<AbstractCommand> iter = getCustomCommands().iterator(); iter.hasNext();) {
                    Object element = iter.next();
                    AbstractCommand command = (AbstractCommand) element;
                    commandGroup.add(command);
                }
            }
        }
        return commandGroup;

    }

    /**
     * @return Returns the commands.
     */
    public List<AbstractCommand> getCommands() {
        logger.debug("--> Enter getCommands");
        if (commands == null) {
            commands = new ArrayList<AbstractCommand>();
        }
        logger.debug("--> Exit getCommands");
        return commands;
    }

    /**
     * Metodo astratto che restituisce i command customizzati per le sue specializzazioni.
     *
     * @return lista di comandi
     */
    public abstract List<AbstractCommand> getCustomCommands();

    /**
     * Chiamata per effettuare la ricerca.
     *
     * @param fieldSearch
     *            proprietà filtrata
     * @param valueSearch
     *            valore cercato...corrisponde al testo immesso nel textFiled
     * @return risultati della ricerca
     */
    public abstract List<?> getData(String fieldSearch, String valueSearch);

    /**
     *
     * @return id
     */
    public String getId() {
        return id;
    }

    /**
     * @return Returns the settings.
     */
    public Settings getSettings() {
        return settings;
    }

    /**
     * metodo finalizzato alla conversione dell'eventuale oggetto di ricerca nel suo corrispondente
     * e completo ValueObject.
     *
     * @param object
     *            oggetto da convertire
     * @return ValueObject oggetto convertito
     */
    public Object getValueObject(Object object) {
        logger.debug("--> Enter getValueObject");
        return object;
    }

    /**
     * @return the editObject
     */
    public boolean isEditObject() {
        return editObject;
    }

    /**
     * @return <code>true</code> se la search gestisce l'apertura di un nuovo oggetto
     */
    public boolean isOpenNewObjectManaged() {
        return true;
    }

    /**
     * Metodo finalizzato all'eventualità di controlli sul form model. Mi devo preoccupare di
     * avvertire l'utente nel caso il controllo ritorni un valore false e quindi venga bloccato l'
     * inserimento di un certo dato.
     *
     * @param formModel
     *            il form model contente il componente di ricerca.
     * @param selectedObject
     *            l'oggetto selezionato della lista risultati nella popup ricerca
     * @return true di default, il check è andato a buon fine, false per bloccare
     */
    public boolean isSelectedObjectValid(FormModel formModel, Object selectedObject) {
        return true;
    }

    /**
     * Dato l'oggetto apro il dialogo se trovo dialogPageId avvalorato.
     *
     * @param object
     *            l'oggettto da impostare alla dialog page del dialogo
     */
    public void openDialogPage(Object object) {
        if (dialogPageId != null && object != null && object instanceof IDefProperty) {
            IFormPageEditor dialogPage = (IFormPageEditor) RcpSupport.getBean(dialogPageId);
            // converto l'oggetto nel formModel con l'oggetto gestito dalla
            // dialogpage
            Class<?> targetClass = dialogPage.getForm().getFormObject().getClass();
            Class<?> sourceClass = object.getClass();

            ConversionService conversionService = (ConversionService) Application.services()
                    .getService(ConversionService.class);
            ConversionExecutor conversionExecutor = conversionService.getConversionExecutor(sourceClass, targetClass);
            object = conversionExecutor.execute(object);

            if (searchPanel != null) {
                dialogPage.addPropertyChangeListener(IPageLifecycleAdvisor.OBJECT_CHANGED, searchPanel);
            }
            DefaultTitledPageApplicationDialog dialog = new DefaultTitledPageApplicationDialog((IDefProperty) object,
                    null, dialogPage);
            dialog.showDialog();

            if (searchPanel != null) {
                dialogPage.removePropertyChangeListener(IPageLifecycleAdvisor.OBJECT_CHANGED, searchPanel);
            }
        }
    }

    /**
     * Di default non esegue nessuna operazione, predisposto per aprire nell'editor il parametro
     * objecct.<br/>
     * <br/>
     * <code>
     * LifecycleApplicationEvent event = new OpenEditorEvent("tabelleEditor");<br/>
     * Application.instance().getApplicationContext().publishEvent(event);
     * </code>
     *
     * @param object
     *            l'oggettto su cui eseguire l'operazione voluta
     */
    public void openEditor(Object object) {

    }

    /**
     * setta la lista dei columns.
     *
     * @param columns
     *            colonne da visualizzare nella tabella dei risultati
     */
    public void setColumns(List<String> columns) {
        this.columns = columns;
    }

    /**
     * @param dialogPageId
     *            the dialogPageId to set
     */
    public void setDialogPageId(String dialogPageId) {
        this.dialogPageId = dialogPageId;
    }

    /**
     * @param editObject
     *            the editObject to set
     */
    public void setEditObject(boolean editObject) {
        this.editObject = editObject;
    }

    /**
     *
     * @param searchPanel
     *            searchPanel che contiene il searchObject
     */
    public void setSearchPanel(SearchPanel searchPanel) {
        this.searchPanel = searchPanel;
    }

    /**
     *
     * @param settings
     *            settings per poter salvare/recuperare i settings a livello utente.
     */
    public void setSettings(Settings settings) {
        this.settings = settings;
    }
}
