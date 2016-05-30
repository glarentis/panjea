package it.eurotn.rich.binding.searchtext;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.SpringLayout;

import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.binding.convert.ConversionExecutor;
import org.springframework.binding.convert.ConversionService;
import org.springframework.binding.form.FormModel;
import org.springframework.binding.form.support.AbstractFormModel;
import org.springframework.binding.value.ValueModel;
import org.springframework.richclient.application.Application;
import org.springframework.richclient.command.ActionCommandExecutor;
import org.springframework.richclient.command.CommandGroup;
import org.springframework.richclient.factory.ComponentFactory;
import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.swing.JidePopupMenu;

import it.eurotn.locking.IDefProperty;
import it.eurotn.panjea.rich.factory.PanjeaComponentFactory;
import it.eurotn.panjea.rich.factory.PanjeaMenuFactory;
import it.eurotn.panjea.rich.factory.SearchObjectRegistry;
import it.eurotn.rich.SpringUtilities;
import it.eurotn.rich.control.table.DefaultBeanTableModel;
import it.eurotn.rich.control.table.JideTableWidget.TableType;
import it.eurotn.rich.search.AbstractSearchObject;

/**
 * Contiene i controlli del pannello ed è usato come controller tra le searchText e la tabella da
 * visualizzare.
 *
 * @author giangi
 */
public class SearchPanel extends JPanel implements PropertyChangeListener, FocusListener {

    private static final Logger LOGGER = Logger.getLogger(SearchPanel.class);

    private static final long serialVersionUID = 1L;

    private Map<String, SearchTextField> textFields;
    private AbstractSearchObject searchObject;
    private String formPropertyPath;
    private TableSearchText tableSearchText;
    private PopUpWithStatusBar popUpWithStatusBar;
    private FormModel formModel;
    private Map<String, Object> context;

    private ActionCommandExecutor propertyCommandExecutor;

    private CaricaTuttoCommand caricaTuttoCommand;

    private ClearCommand clearCommand;
    private MostraPopupCommand mostraPopupCommand;
    private NewObjectCommand newObjectCommand;
    private SelectAutoCommand selectAutoCommand;
    private TimeOutIncrementalSearch timeOutIncrementalSearch;

    private EditObjectCommand editObjectCommand;
    private SearchTextField focusableSearchTextField = null;

    private Map<String, String> parameters;

    /**
     * Classe che viene gestita dalla seachObject.
     */
    private Class<?> classSearchObject;

    private ConversionService conversionService = null;

    private JidePopupMenu popupMenu;

    private ConversionExecutor conversionExecutor = null;

    /**
     * Costruttore di default.
     */
    public SearchPanel() {
        super(new SpringLayout());
    }

    /**
     * Inizializza i componenti.
     *
     * @param property
     *            proprietà del formModel bindata
     * @param contextParam
     *            context del binding
     * @param formModelParam
     *            formModel del binding
     * @param mapParameters
     *            mapParameters
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public void configure(String property, final Map<String, Object> contextParam, final FormModel formModelParam,
            final Map<String, String> mapParameters) {

        textFields = new HashMap<String, SearchTextField>();
        this.parameters = mapParameters;
        this.formPropertyPath = property;
        this.formModel = formModelParam;
        this.context = contextParam;
        this.caricaTuttoCommand = new CaricaTuttoCommand();
        this.clearCommand = new ClearCommand(this);
        this.mostraPopupCommand = new MostraPopupCommand();
        this.newObjectCommand = new NewObjectCommand(this);
        this.editObjectCommand = new EditObjectCommand(this);
        this.selectAutoCommand = new SelectAutoCommand(this);
        this.timeOutIncrementalSearch = new TimeOutIncrementalSearch(this);

        // Recupero la classe associata alla searchObject
        classSearchObject = (Class<?>) context.get(SearchTextBinder.SEARCH_OBJECT_CLASS_KEY);
        if (classSearchObject == null) {
            classSearchObject = getClassFormPropertyPath();
        }

        // se non ho rendererKeys significa che sto bindando direttamente la
        // proprieta' che deve essere stringa
        String[] renderKeys = (String[]) contextParam.get(SearchTextBinder.RENDERER_KEY);
        if (renderKeys == null) {
            renderKeys = new String[] { null };
        }

        int columnPosition = 0;
        for (String renderKey : renderKeys) {
            SearchTextField field = createSearchTextField(contextParam);
            if (focusableSearchTextField == null) {
                focusableSearchTextField = field;
            }
            // mi creo un tablemodel per trovare
            // l'indice della colonna della renderKey. Devo
            // crearla e non posso chiamare una getTableSearchText() perchè
            // altrimenti viene chiamata una iniControl()
            // che da una serie di NPE su controlli non ancora creati e comunque
            // mi creerebbe tutti i controlli solo visualizzando la search text
            // nel form.
            // La renderKey potrebbe essere a null quindi in questo caso prendo
            // come ricerca la prima colonna. ( caso
            // della search della sede in cui viene visualizzata con
            // l'ObjectRendererManager ma la ricerca è sul codice
            // )
            if (renderKey != null) {
                DefaultBeanTableModel tableModel = new DefaultBeanTableModel<Object>("tmpModel",
                        getSearchObject().getColumns().toArray(new String[0]),
                        (Class<Object>) getSearchObject().createNewInstance().getClass());
                tableModel.getColumnCount();

                columnPosition = 0;
                if (tableModel.getColumnPropertyPosition().get(renderKey) != null) {
                    columnPosition = ((Integer) tableModel.getColumnPropertyPosition().get(renderKey));
                }
            }

            field.configure(formPropertyPath, formModelParam, renderKey, columnPosition, classSearchObject);
            textFields.put(renderKey, field);
            configurePopupMenu(field);
            add(field);
        }
        SpringUtilities.makeCompactGrid(this, // parent
                1, renderKeys.length, 0, 0, // initX, initY
                2, 0); // xPad, yPad

        addFocusListener(this);
    }

    /**
     * Configuro il popupMenu per una searchTextField.
     *
     * @param searchTextField
     *            searchTextField da configurare
     */
    protected void configurePopupMenu(SearchTextField searchTextField) {

        searchTextField.setCaricaTuttoCommand(caricaTuttoCommand);
        searchTextField.setClearCommand(clearCommand);
        searchTextField.setSelectAutoCommand(selectAutoCommand);
        searchTextField.setMostraPopUpCommand(mostraPopupCommand);
        searchTextField.setTimeOutIncrementalSearch(timeOutIncrementalSearch);

        searchTextField.getTextField().getInputMap().put(KeyStroke.getKeyStroke("F2"), mostraPopupCommand.getId());
        searchTextField.getTextField().getActionMap().put(mostraPopupCommand.getId(),
                mostraPopupCommand.getActionAdapter());

        // se e' predisposto l'edit dell'oggetto aggiungo edit e new command.
        if (getSearchObject().isEditObject()) {
            searchTextField.getTextField().getInputMap().put(KeyStroke.getKeyStroke("F3"), newObjectCommand.getId());
            searchTextField.getTextField().getActionMap().put(newObjectCommand.getId(),
                    newObjectCommand.getActionAdapter());

            searchTextField.getTextField().getInputMap().put(KeyStroke.getKeyStroke("F7"), editObjectCommand.getId());
            searchTextField.getTextField().getActionMap().put(editObjectCommand.getId(),
                    editObjectCommand.getActionAdapter());
        }

        // carica tutto command
        searchTextField.getTextField().getInputMap().put(KeyStroke.getKeyStroke("F4"), caricaTuttoCommand.getId());
        searchTextField.getTextField().getActionMap().put(caricaTuttoCommand.getId(),
                caricaTuttoCommand.getActionAdapter());

        // cancella command
        searchTextField.getTextField().getInputMap().put(KeyStroke.getKeyStroke("F8"), clearCommand.getId());
        searchTextField.getTextField().getActionMap().put(clearCommand.getId(), clearCommand.getActionAdapter());

        // aggiunge alla list di Command di SearchObject le ActionCommand
        // CaricaTuttoCommand , per esser aggiunto al
        // menu di popup
        if (popupMenu == null) {
            getSearchObject().getCommands().add(clearCommand);
            getSearchObject().getCommands().add(caricaTuttoCommand);
            getSearchObject().getCommands().add(selectAutoCommand);
            getSearchObject().getCommands().add(timeOutIncrementalSearch);
            if (searchObject.isOpenNewObjectManaged()) {
                getSearchObject().getCommands().add(newObjectCommand);
            }
            if (searchObject.isEditObject()) {
                getSearchObject().getCommands().add(editObjectCommand);
            }
            CommandGroup commandGroup = getSearchObject().getCommandGroup();
            popupMenu = (JidePopupMenu) commandGroup.createPopupMenu(new PanjeaMenuFactory());
            org.springframework.util.Assert.isInstanceOf(JidePopupMenu.class, popupMenu,
                    "popupMenu non e' instanza di JidePopupMenu");
            getSearchObject().configureSearchText(searchTextField);
        }
        searchTextField.setPopupMenu(popupMenu);
    }

    /**
     * @param contextParam
     *            contextParam
     * @return searchTextField configurato in base alla searchObject
     */
    private SearchTextField createSearchTextField(Map<String, Object> contextParam) {
        PanjeaComponentFactory componentFactory = (PanjeaComponentFactory) Application.services()
                .getService(ComponentFactory.class);
        SearchTextField result = null;
        if (contextParam.get(SearchTextBinder.SEARCH_TEXT_CLASS_KEY) != null) {
            try {
                result = (SearchTextField) BeanUtils.instantiateClass(
                        Class.forName((String) contextParam.get(SearchTextBinder.SEARCH_TEXT_CLASS_KEY)));
            } catch (Exception e) {
                LOGGER.error("-->errore nell'istanziare la classe" + SearchTextBinder.SEARCH_TEXT_CLASS_KEY, e);
                throw new RuntimeException(e);
            }
        } else {
            result = componentFactory.createSearchTextField();
        }
        return result;
    }

    @Override
    public void focusGained(FocusEvent event) {
        if (textFields.entrySet().iterator().hasNext()) {
            textFields.entrySet().iterator().next().getValue().getTextField().requestFocusInWindow();
        }
    }

    @Override
    public void focusLost(FocusEvent event) {
    }

    /**
     * @return caricaTuttoCommand
     */
    public CaricaTuttoCommand getCaricaTuttoCommand() {
        return caricaTuttoCommand;
    }

    /**
     * @return the formPropertyPath
     */
    public Class<?> getClassFormPropertyPath() {
        // Object value = ((AbstractFormModel) formModel).getFormObjectPropertyAccessStrategy()
        // .getPropertyValueModel(formPropertyPath).getValue();
        Object value = formModel.getValueModel(formPropertyPath).getValue();
        return value != null ? value.getClass() : formModel.getFieldMetadata(formPropertyPath).getPropertyType();
    }

    /**
     * @return the classSearchObject
     */
    public Class<?> getClassSearchObject() {
        return classSearchObject;
    }

    /**
     * @return the clearCommand
     */
    public ClearCommand getClearCommand() {
        return clearCommand;
    }

    /**
     * @return ConversionExecutor
     */
    public ConversionExecutor getConversionExecutor() {
        if (conversionExecutor == null) {
            conversionExecutor = getConversionService().getConversionExecutor(classSearchObject,
                    getClassFormPropertyPath());
        }
        return conversionExecutor;
    }

    /**
     * @return ConversionService
     */
    public ConversionService getConversionService() {
        if (conversionService == null) {
            conversionService = (ConversionService) Application.services().getService(ConversionService.class);
        }
        return conversionService;
    }

    /**
     * Ritorna la searchTextField che e' impostata per ricevere il focus.
     *
     * @return SearchTextField
     */
    public SearchTextField getFocusableSearchText() {
        return focusableSearchTextField;
    }

    /**
     * @return formModel
     */
    public FormModel getFormModel() {
        return formModel;
    }

    /**
     * @return Returns the formPropertyPath.
     */
    public String getFormPropertyPath() {
        return formPropertyPath;
    }

    /**
     * Restituisce la mappa contenente l'elenco delle proprietà dei filtri e il loro valore.
     *
     * @return mappa dei parametri
     */
    public Map<String, Object> getMapParameters() {
        Map<String, Object> map = new HashMap<String, Object>();

        for (Entry<String, String> parameter : parameters.entrySet()) {
            map.put(parameter.getKey(), formModel.getValueModel(parameter.getValue()).getValue());
        }
        return map;
    }

    /**
     *
     * @param selectedRow
     *            riga selezionata
     * @return oggetto contenuto nella riga
     */
    public Object getObjectAt(int selectedRow) {
        return getTableSearchText().getRows().get(selectedRow);
    }

    /**
     * @return parametri configurati
     */
    public Map<String, String> getParameters() {
        return parameters;
    }

    /**
     * @return popup
     */
    public PopUpWithStatusBar getPopUp() {
        initControl();
        return popUpWithStatusBar;
    }

    /**
     * @return the propertyCommandExecutor
     */
    public ActionCommandExecutor getPropertyCommandExecutor() {
        return propertyCommandExecutor;
    }

    /**
     * @return searchObject
     */
    public AbstractSearchObject getSearchObject() {
        if (searchObject == null) {
            SearchObjectRegistry searchObjectRegistry = (SearchObjectRegistry) RcpSupport
                    .getBean("searchObjectFactory");
            org.springframework.util.Assert.notNull(searchObjectRegistry,
                    "Factorybean dei bean di ricerca non trovato");

            searchObject = searchObjectRegistry.getSearchObject(classSearchObject);
            searchObject.setSearchPanel(this);
        }
        return searchObject;
    }

    /**
     * @return tableSearchText
     */
    public TableSearchText getTableSearchText() {
        initControl();
        return tableSearchText;
    }

    /**
     * @return the textFields
     */
    public Map<String, SearchTextField> getTextFields() {
        return textFields;
    }

    /**
     * @return valuemodel della proprietà.
     */
    public ValueModel getValueModel() {
        return formModel.getValueModel(formPropertyPath);
    }

    /**
     * init dei controlli.
     */
    @SuppressWarnings("unchecked")
    private void initControl() {
        // Creo la widgetTable
        if (tableSearchText == null) {
            tableSearchText = new TableSearchText(getSearchObject(),
                    (Class<Object>) getSearchObject().createNewInstance().getClass());
            tableSearchText.getComponent();
            tableSearchText.setTableType(TableType.GROUP);

            popUpWithStatusBar = new PopUpWithStatusBar(tableSearchText,
                    textFields.get(textFields.keySet().iterator().next()));
            tableSearchText.setPropertyCommandExecutor(propertyCommandExecutor);
        }
    }

    /**
     * Istanzia tutte le proprietà nested di quella configurata per la search
     *
     */
    private void instantiateNullValueInNestedPropertyPath() {
        String[] splitProp = formPropertyPath.split("\\.");

        String prop = "";

        try {
            for (int i = 0; i < splitProp.length - 1; i++) {
                prop = prop + splitProp[i];
                if (formModel.getValueModel(prop).getValue() == null) {
                    Class<?> propClass = ((AbstractFormModel) formModel).getFieldMetadata(prop).getPropertyType();
                    formModel.getValueModel(prop).setValue(propClass.newInstance());
                    prop = prop + ".";
                } else {
                    break;
                }
            }
        } catch (Exception e) {
            LOGGER.error("--> Non riesco a instanziare la nested propery " + prop, e);
            e.printStackTrace();
        }
    }

    /**
     * @return <code>true</code> se i controlli sono creati
     */
    public boolean isControlCreated() {
        return tableSearchText != null;
    }

    /**
     * Verifica se l'oggetto passato è diverso rispetto a quello presente nel value model della
     * search text.
     *
     * @param objectToSelect
     *            l'oggetto di cui verificare la differenza
     * @return true se l'oggetto è diverso, false altrimenti
     */
    public boolean isObjectChanged(Object objectToSelect) {
        // devo passare attraverso il conversion service per fare il confronto
        // tra l'oggetto che ho selezionato e l'oggetto nel value model
        Object objToSelect = getSearchObject().getValueObject(objectToSelect);
        objToSelect = getConversionExecutor().execute(objToSelect);
        return !objToSelect.equals(getValueModel().getValue());
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        selectObject(evt.getNewValue());
    }

    /**
     * @param objectSelected
     *            seleziona l'oggetto e lo inserisce nel formmodel.
     */
    public void selectObject(Object objectSelected) {
        // definisce se posso selezionare l'elemento scelto dalla lista di risultati
        boolean canSelect = getSearchObject().isSelectedObjectValid(formModel, objectSelected);
        if (!canSelect) {
            for (SearchTextField field : textFields.values()) {
                field.revert();
                field.getTextField().moveCaretPosition(0);
            }
            return;
        }
        // se non ho oggetti selezionati oppure non posso selezionare l'elemento
        // preparo un oggetto vuoto
        if (objectSelected == null) {
            for (SearchTextField searchTextField : getTextFields().values()) {
                searchTextField.setText("");
            }
        }
        if (objectSelected != null) {
            objectSelected = getSearchObject().getValueObject(objectSelected);
        }

        // Per poter modificare l'oggetto nella tabella di ricerca prima di
        // settarlo nel value model
        objectSelected = getConversionExecutor().execute(objectSelected);

        try {
            instantiateNullValueInNestedPropertyPath();
            formModel.getValueModel(formPropertyPath).setValue(objectSelected);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (objectSelected != null) {

            validateAndtransferFocus(objectSelected);
        }
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        for (SearchTextField searchTextField : textFields.values()) {
            searchTextField.getTextField().setEnabled(enabled);
            // searchTextField.setBackground(searchTextField.getTextField().getBackground());
        }
        caricaTuttoCommand.setEnabled(enabled);
        clearCommand.setEnabled(enabled);
        newObjectCommand.setEnabled(enabled);
    }

    /**
     * @param propertyCommandExecutor
     *            the propertyCommandExecutor to set
     */
    public void setPropertyCommandExecutor(ActionCommandExecutor propertyCommandExecutor) {
        this.propertyCommandExecutor = propertyCommandExecutor;
    }

    /**
     * Setta il read only degli elementi del searchPanel.
     *
     * @param readOnly
     *            readOnly value
     */
    public void setReadOnly(boolean readOnly) {
        for (SearchTextField searchTextField : textFields.values()) {
            searchTextField.getTextField().setEditable(!readOnly);
            searchTextField.setBackground(searchTextField.getTextField().getBackground());
        }
        caricaTuttoCommand.setEnabled(!readOnly);
        clearCommand.setEnabled(!readOnly);
        newObjectCommand.setEnabled(!readOnly);
    }

    /**
     * Passa il focus al componente successivo se necessario.
     *
     * @param objectSelected
     *            oggetto selezionato
     */
    protected void validateAndtransferFocus(Object objectSelected) {
        // se c'è un oggetto selezionato vado ad individuare l'ultima text field del pannello e
        // sposto il focus sul
        // prossimo componente.
        boolean transferFocus = objectSelected != null && !"".equals(objectSelected);
        // Se ho la proprietà createEmptyValueOnNullValue a true allora non avrò un oggetto a null
        // ma un wrapper del
        // null object. Questo succede solitamente se ho un oggetto che implementa la IDefProperty
        if (objectSelected instanceof IDefProperty) {
            transferFocus = ((IDefProperty) objectSelected).getId() != null;
        }
        if (transferFocus) {
            String[] renderKeys = (String[]) context.get(SearchTextBinder.RENDERER_KEY);

            if (renderKeys == null) {
                // textFields.get(null).getTextField().requestFocusInWindow();
                textFields.get(null).getTextField().transferFocus();
            } else {
                // textFields.get(renderKeys[0]).getTextField().requestFocusInWindow();
                textFields.get(renderKeys[renderKeys.length - 1]).getTextField().transferFocus();
            }
        }
    }
}