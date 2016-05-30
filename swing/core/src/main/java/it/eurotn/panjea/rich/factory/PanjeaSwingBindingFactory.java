package it.eurotn.panjea.rich.factory;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.ComboBoxEditor;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.SwingConstants;
import javax.swing.plaf.metal.MetalComboBoxEditor;
import javax.swing.tree.TreeModel;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.support.PropertyComparator;
import org.springframework.binding.form.ConfigurableFormModel;
import org.springframework.binding.value.ValueModel;
import org.springframework.binding.value.support.ValueHolder;
import org.springframework.context.MessageSource;
import org.springframework.richclient.application.Application;
import org.springframework.richclient.form.AbstractFocussableForm;
import org.springframework.richclient.form.binding.Binder;
import org.springframework.richclient.form.binding.Binding;
import org.springframework.richclient.form.binding.swing.AbstractListBinder;
import org.springframework.richclient.form.binding.swing.ComboBoxBinder;
import org.springframework.richclient.form.binding.swing.SwingBindingFactory;
import org.springframework.richclient.list.BeanPropertyValueListRenderer;
import org.springframework.rules.closure.Closure;
import org.springframework.rules.constraint.Constraint;
import org.springframework.util.Assert;

import com.jgoodies.forms.layout.FormLayout;

import it.eurotn.panjea.anagrafica.domain.Importo;
import it.eurotn.rich.binding.CheckBoxListSelectableBinder;
import it.eurotn.rich.binding.CheckBoxTreeBinder;
import it.eurotn.rich.binding.CodeEditorBinder;
import it.eurotn.rich.binding.CodiceFiscaleBinder;
import it.eurotn.rich.binding.DatiGeograficiBinder;
import it.eurotn.rich.binding.DualListBinder;
import it.eurotn.rich.binding.EnumRadioButtonBinder;
import it.eurotn.rich.binding.FileChooser;
import it.eurotn.rich.binding.FileChooserBinder;
import it.eurotn.rich.binding.HTMLEditorBinder;
import it.eurotn.rich.binding.ImportoBinder;
import it.eurotn.rich.binding.JCalendarBinder;
import it.eurotn.rich.binding.PanjeaComboBoxBinder;
import it.eurotn.rich.binding.PercentageNumberBinder;
import it.eurotn.rich.binding.TableBinding;
import it.eurotn.rich.binding.TigerEnumCheckBoxListBinder;
import it.eurotn.rich.binding.codice.CodiceBinder;
import it.eurotn.rich.binding.searchtext.SearchTextBinder;
import it.eurotn.rich.control.I18nStringComboBoxEditor;
import it.eurotn.rich.control.I18nStringListCellRenderer;
import it.eurotn.rich.control.table.DefaultBeanTableModel;
import it.eurotn.rich.control.table.JideTableWidget;
import it.eurotn.rich.form.binding.swing.ReadOnlyComboBoxBinder;
import it.eurotn.rich.list.I18NBeanPropertyValueComboBoxEditor;
import it.eurotn.rich.list.I18NBeanPropertyValueListRenderer;

/**
 * Estensione di SwingBindingFactory che provvede a fornire dei metodi per la creazione dei binding dei componenti.
 *
 * @author adriano
 */
public class PanjeaSwingBindingFactory extends SwingBindingFactory {

    /**
     * Closure per l'editor della combobox, internazionalizza la rendered property.
     */
    protected static class I18NBeanPropertyEditorClosure implements Closure {

        private final String renderedProperty;

        /**
         * Costruttore di default.
         *
         * @param renderedProperty
         *            renderedProperty
         */
        public I18NBeanPropertyEditorClosure(final String renderedProperty) {
            this.renderedProperty = renderedProperty;
        }

        @Override
        public Object call(Object argument) {
            Assert.isInstanceOf(ComboBoxEditor.class, argument);
            return new I18NBeanPropertyValueComboBoxEditor((ComboBoxEditor) argument, renderedProperty);
        }

        /**
         * @return the rendered property name
         */
        String getRenderedProperty() {
            return renderedProperty;
        }

    }

    private static Logger logger = Logger.getLogger(PanjeaSwingBindingFactory.class);

    /**
     * Costruttore di default.
     *
     * @param formModel
     *            il form model con cui legare i componenti
     */
    public PanjeaSwingBindingFactory(final ConfigurableFormModel formModel) {
        super(formModel);
    }

    @Override
    public Binding createBinding(String formPropertyPath, Map context) {
        return super.createBinding(formPropertyPath, context);
    }

    /**
     * Crea il binding per un visualizzare una data.
     *
     * @see JCalendarBinder
     * @param formPropertyPath
     *            il path della proprietà nel form model
     * @param dateFormat
     *            il formato da visualizzare per la data
     * @param maskFormat
     *            la maschera da visualizzare nel componente
     * @return Binding
     */
    public Binding createBoundCalendar(String formPropertyPath, String dateFormat, String maskFormat) {
        JCalendarBinder binder = new JCalendarBinder();
        binder.setDateFormat(dateFormat);
        binder.setMaskFormat(maskFormat);
        Map<String, Object> context = new HashMap<String, Object>();
        Binding binding = binder.bind(getFormModel(), formPropertyPath, context);
        return binding;
    }

    /**
     * Crea il binding per mostrare una lista sotto forma di checkbox list.
     *
     * @see CheckBoxListSelectableBinder
     * @param selectionFormProperty
     *            la proprietà nel form model che contiene la selezione
     * @param selectableItems
     *            la lista di oggetti disponibili per la check box list
     * @param rendered
     *            la proprietà visualizzata dell'oggetto contenuto nella check box
     * @param forceSelectMode
     *            tipo di selezione per la combo box (multipla o singola)
     * @return Binding
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public Binding createBoundCheckBoxList(String selectionFormProperty, Object selectableItems,
            ListCellRenderer<?> rendered, Integer forceSelectMode) {
        Map context = new HashMap();
        if (forceSelectMode != null) {
            context.put("selectionMode", forceSelectMode);
        }
        context.put("selectableItems", selectableItems);
        if (rendered != null) {
            context.put("renderer", rendered);
        }
        Binding binding = new CheckBoxListSelectableBinder().bind(getFormModel(), selectionFormProperty, context);
        // interceptBinding(binding);
        return binding;
    }

    /**
     * Crea il binding per mostrare una lista sotto forma di checkbox list.
     *
     * @see CheckBoxListSelectableBinder
     * @param selectionFormProperty
     *            la proprietà nel form model che contiene la selezione
     * @param selectableItems
     *            la lista di oggetti disponibili per la check box list
     * @param renderedProperty
     *            la proprietà visualizzata dell'oggetto contenuto nella check box
     * @param forceSelectMode
     *            tipo di selezione per la combo box (multipla o singola)
     * @return Binding
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public Binding createBoundCheckBoxList(String selectionFormProperty, Object selectableItems,
            String renderedProperty, Integer forceSelectMode) {
        Map context = new HashMap();
        if (forceSelectMode != null) {
            context.put("selectionMode", forceSelectMode);
        }
        context.put("selectableItems", selectableItems);
        if (renderedProperty != null) {
            context.put("renderer", new BeanPropertyValueListRenderer(renderedProperty));
            context.put("comparator", new PropertyComparator(renderedProperty, true, true));
        }
        Binding binding = new CheckBoxListSelectableBinder().bind(getFormModel(), selectionFormProperty, context);
        // interceptBinding(binding);
        return binding;
    }

    /**
     * Crea il binding per mostrare una lista sotto forma di checkbox tree.
     *
     * @see CheckBoxTreeBinder
     * @param formPropertyPath
     *            il path della proprietà nel form model
     * @param groupableProperties
     *            properietà che identifica i livelli da raggruppare
     * @param selectableItems
     *            valori da poter selezionare
     * @return Binding
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public Binding createBoundCheckBoxTree(String formPropertyPath, String[] groupableProperties,
            ValueHolder selectableItems) {
        Map context = new HashMap<String, Object>();
        context.put(CheckBoxTreeBinder.GROUPABLE_PROPERTIES, groupableProperties);
        context.put(CheckBoxTreeBinder.SELECTED_ITEM_HOLDER_KEY, selectableItems);
        return new CheckBoxTreeBinder().bind(getFormModel(), formPropertyPath, context);
    }

    /**
     *
     * @see CheckBoxTreeBinder
     * @param formPropertyPath
     *            il path della proprietà nel form model
     * @param model
     *            table model da utilizzare
     * @return Binding
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public Binding createBoundCheckBoxTree(String formPropertyPath, TreeModel model) {
        Map context = new HashMap<String, Object>();
        context.put(CheckBoxTreeBinder.TABLE_MODEL_KEY, model);
        return new CheckBoxTreeBinder().bind(getFormModel(), formPropertyPath, context);
    }

    /**
     * Crea il binding per mostrare il valore in un componente CodeEditor.
     *
     * @param formPropertyPath
     *            il path della proprietà nel form model
     * @param variabiliValueHolder
     *            lista di variabili utilizzabile nel code editor.
     * @return Binding
     */
    public Binding createBoundCodeEditor(String formPropertyPath, ValueHolder variabiliValueHolder) {
        return createBoundCodeEditor(formPropertyPath, variabiliValueHolder, false, false, true);
    }

    /**
     * Crea il binding per mostrare il valore in un componente CodeEditor.
     *
     * @param formPropertyPath
     *            il path della proprietà nel form model
     * @param variabiliValueHolder
     *            lista di variabili utilizzabile nel code editor.
     * @param useOnlyValueHolderVariabili
     *            se true usa solo le variabili contenute nel value holder
     * @param legendMapVariabili
     *            se <code>true</code> aggiunge il pulsante per la legenda delle variabili
     * @param multiline
     *            se true il code editor è multi linea altrimenti no
     * @return Binding
     */
    public Binding createBoundCodeEditor(String formPropertyPath, ValueHolder variabiliValueHolder,
            Boolean useOnlyValueHolderVariabili, Boolean legendMapVariabili, boolean multiline) {
        logger.debug("--> Enter createBoundCodeEditorEditor");
        Map<String, Object> context = new HashMap<String, Object>();
        context.put(CodeEditorBinder.VALUE_HOLDER_VARIABILI, variabiliValueHolder);
        context.put(CodeEditorBinder.USE_ONLY_HOLDER_VARIABILI, useOnlyValueHolderVariabili);
        context.put(CodeEditorBinder.LEGEND_MAP_VARIABLE, legendMapVariabili);
        context.put(CodeEditorBinder.MULTILINE_EDITOR, multiline);
        CodeEditorBinder codeEditorBinder = new CodeEditorBinder();
        Binding binding = codeEditorBinder.bind(getFormModel(), formPropertyPath, context);
        interceptBinding(binding);
        logger.debug("--> Exit createBoundCodeEditorEditor");
        return binding;
    }

    /**
     * Crea il binding per mostrare un valore in un componente codice.
     *
     * @param formPropertyPath
     *            il path della proprietà nel form model
     * @param editEnable
     *            se <code>true</code> rende possibile la modifica diretta del codice
     * @param required
     *            se <code>true</code> al codice verrà applicata la constraint required
     * @return Binding
     */
    public Binding createBoundCodice(String formPropertyPath, boolean editEnable, boolean required) {
        Map<String, Object> context = new HashMap<String, Object>();
        context.put(CodiceBinder.EDIT_ENABLE, editEnable);
        context.put(CodiceBinder.REQUIRED, required);
        return createBinding(JPanel.class, formPropertyPath, context);
    }

    /**
     * Custom Binding di Codice per la gestione di prefisso.
     *
     * @see CodiceBinder
     * @param formPropertyPath
     *            il path della proprietà nel form model
     * @param formProtocolloPropertyPath
     *            il path del protocollo per la proprietà nel form model
     * @param formValoreProtocolloPropertyPath
     *            il path del valore del protocollo utilizzato per la proprietà nel form model
     * @param formPatternPropertyPath
     *            il path del pattern di generazione del codice per la proprietà nel form model
     * @param additionalConstraints
     *            constraint addizionali
     * @return Binding
     */
    public Binding createBoundCodice(String formPropertyPath, String formProtocolloPropertyPath,
            String formValoreProtocolloPropertyPath, String formPatternPropertyPath,
            List<Constraint> additionalConstraints) {
        Map<String, Object> context = new HashMap<String, Object>();

        if (!StringUtils.isEmpty(formProtocolloPropertyPath)) {
            context.put(CodiceBinder.PROTOCOLLOFORMPROPERTY_KEY, formProtocolloPropertyPath);
        }
        if (!StringUtils.isEmpty(formValoreProtocolloPropertyPath)) {
            context.put(CodiceBinder.VALOREPROTOCOLLO_FORMPROPERTY_KEY, formValoreProtocolloPropertyPath);
        }
        if (!StringUtils.isEmpty(formPatternPropertyPath)) {
            context.put(CodiceBinder.PATTERN_FORMPROPERTY_KEY, formPatternPropertyPath);
        }
        if (additionalConstraints != null) {
            context.put(CodiceBinder.ADDITIONAL_CONSTRAINT, additionalConstraints);
        }
        Assert.notNull(context, "Context must not be null ");
        CodiceBinder binder = new CodiceBinder();
        Binding binding = binder.bind(getFormModel(), formPropertyPath, context);
        interceptBinding(binding);
        return binding;

    }

    /**
     * Crea il binding per mostrare il codice fiscale in un componente personalizzato dalla possibilità di calcolarlo.
     *
     * @param formPropertyPath
     *            il path della proprietà nel form model
     * @param nome
     *            il nome per il calcolo del c.f.
     * @param cognome
     *            il cognome per il calcolo del c.f.
     * @param dataDiNascita
     *            la data di nascita per il calcolo del c.f.
     * @param comune
     *            il comune per il calcolo del c.f.
     * @param sesso
     *            il sesso per il calcolo del c.f.
     * @return Binding
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public Binding createBoundCodiceFiscale(String formPropertyPath, String nome, String cognome, String dataDiNascita,
            String comune, String sesso) {
        Map context = new HashMap<String, Object>();
        context.put(CodiceFiscaleBinder.NOME_KEY, nome);
        context.put(CodiceFiscaleBinder.COGNOME_KEY, cognome);
        context.put(CodiceFiscaleBinder.DATA_NASCITA_KEY, dataDiNascita);
        context.put(CodiceFiscaleBinder.COMUNE_KEY, comune);
        context.put(CodiceFiscaleBinder.SESSO_KEY, sesso);
        Assert.notNull(context, "Context must not be null");
        Binder binder = new CodiceFiscaleBinder();
        Binding binding = binder.bind(getFormModel(), formPropertyPath, context);
        interceptBinding(binding);
        return binding;
    }

    @Override
    public Binding createBoundComboBox(String formProperty) {
        return createBoundComboBox(formProperty, new HashMap());
    }

    private Binding createBoundComboBox(String formProperty, Map context) {
        Binder binder = new PanjeaComboBoxBinder();
        Binding binding = binder.bind(getFormModel(), formProperty, context);
        interceptBinding(binding);
        return binding;
    }

    @Override
    public Binding createBoundComboBox(String formProperty, Object selectableItems) {
        Map context = createContext("selectableItems", selectableItems);
        return createBoundComboBox(formProperty, context);
    }

    /**
     * Crea il binding per mostrare il valore in una combo box.
     *
     * @param formProperty
     *            il path della proprietà nel form model
     * @param selectableItems
     *            gli oggetti selezionabili per la combo box
     * @param installI18nRenderer
     *            decide se impostare l'internazionalizzazione per i valori della combo box
     * @return Binding
     */
    @SuppressWarnings("unchecked")
    public Binding createBoundComboBox(String formProperty, Object selectableItems, boolean installI18nRenderer) {
        Map<Object, Object> context = createContext(AbstractListBinder.SELECTABLE_ITEMS_KEY, selectableItems);
        if (installI18nRenderer) {
            context.put(ComboBoxBinder.RENDERER_KEY, new I18nStringListCellRenderer());
            MessageSource messageSource = (MessageSource) Application.services().getService(MessageSource.class);
            context.put(ComboBoxBinder.EDITOR_KEY,
                    new I18nStringComboBoxEditor(messageSource, new MetalComboBoxEditor()));
        }
        Binder binder = new PanjeaComboBoxBinder();
        Binding binding = binder.bind(getFormModel(), formProperty, context);
        interceptBinding(binding);
        return binding;
    }

    @Override
    public Binding createBoundComboBox(String formProperty, Object selectableItems, String renderedProperty) {
        Map context = createContext("selectableItems", selectableItems);
        context.put("renderer", new BeanPropertyValueListRenderer(renderedProperty));
        context.put("editor", new BeanPropertyEditorClosure(renderedProperty));
        context.put("comparator", new PropertyComparator(renderedProperty, true, true));
        return createBoundComboBox(formProperty, context);
    }

    @Override
    public Binding createBoundComboBox(String formProperty, String selectableItemsProperty,
            String renderedItemProperty) {
        return createBoundComboBox(formProperty, getFormModel().getValueModel(selectableItemsProperty),
                renderedItemProperty);
    }

    /**
     * Crea il binding per mostrare il valore in una combo box.
     *
     * @param formPropertyPath
     *            il path della proprietà nel form model
     * @param selectableItemsHolder
     *            gli oggetti selezionabili per la combo box
     * @param comparator
     *            il camparator per ordinare i valori della combo box
     * @return Binding
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public Binding createBoundComboBox(String formPropertyPath, ValueModel selectableItemsHolder,
            Comparator<Object> comparator) {
        Map context = createContext(AbstractListBinder.SELECTABLE_ITEMS_KEY, selectableItemsHolder);
        context.put(AbstractListBinder.COMPARATOR_KEY, comparator);
        Binder binder = new PanjeaComboBoxBinder();
        Binding binding = binder.bind(getFormModel(), formPropertyPath, context);
        interceptBinding(binding);
        return binding;
    }

    /**
     * Creo una checkBoxList da un tipo ENUM.
     *
     * @see TigerEnumCheckBoxListBinder
     * @param selectionFormProperty
     *            proprietà associata del bean
     * @param enumClass
     *            enum dove andare a prendere i valori per riempire la lsta
     * @param forceSelectMode
     *            costante della JList
     * @param layoutOrientation
     *            costante della JList per l'orientamento degli elementi della lista, horizontal o vertical, se null di
     *            default e' verticale
     * @return Binding
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public Binding createBoundEnumCheckBoxList(String selectionFormProperty, Class enumClass, Integer forceSelectMode,
            Integer layoutOrientation) {
        Map context = new HashMap();
        if (forceSelectMode != null) {
            context.put("selectionMode", forceSelectMode);
        }
        if (layoutOrientation != null) {
            context.put("layoutOrientation", layoutOrientation);
        }
        context.put("enumClass", enumClass);
        Binding binding = new TigerEnumCheckBoxListBinder().bind(getFormModel(), selectionFormProperty, context);
        interceptBinding(binding);
        return binding;
    }

    /**
     *
     * @param formPropertyPath
     *            proprietà da bindare
     * @param useFile
     *            utilizzare una proprietà File o una stringa contenente il path
     * @param chooserMode
     *            dir o file
     * @return binding
     */
    public Binding createBoundFileChooseBinding(String formPropertyPath, boolean useFile,
            FileChooser.FileChooserMode chooserMode) {
        FileChooserBinder binder = new FileChooserBinder();
        binder.setMode(chooserMode);
        binder.setUseFile(useFile);
        Binding binding = binder.bind(getFormModel(), formPropertyPath, new HashMap<String, Object>());
        interceptBinding(binding);
        return binding;
    }

    /**
     * Crea il binding per mostrare il valore in uncomponente HTML; è possibile modificare l'html o il codice sorgente.
     *
     * @see HTMLEditorBinder
     * @param formPropertyPath
     *            il path della proprietà nel form model
     * @return Binding
     */
    public Binding createBoundHTMLEditor(String formPropertyPath) {
        logger.debug("--> Enter createBoungHTMLEditor");
        Map<String, Object> context = new HashMap<String, Object>();
        HTMLEditorBinder editorBinder = new HTMLEditorBinder();
        Binding binding = editorBinder.bind(getFormModel(), formPropertyPath, context);
        interceptBinding(binding);
        logger.debug("--> Exit createBoungHTMLEditor");
        return binding;
    }

    /**
     * Binding della proprietà di tipo Importo con il component ImportoTextField.
     *
     * @param formPropertyPath
     *            il path della proprietà nel form model
     * @param dataCambioPath
     *            il path della proprietà della data valuta. Se cambia la data valuta ricalcola il tasso.
     * @return Binding
     */
    public Binding createBoundImportoTassoCalcolatoTextField(String formPropertyPath, String dataCambioPath) {
        Map<String, Object> context = new HashMap<String, Object>();
        ImportoBinder binder = new ImportoBinder();
        context.put(ImportoBinder.DATA_CAMBIO_PATH, dataCambioPath);
        Binding binding = binder.bind(getFormModel(), formPropertyPath, context);
        interceptBinding(binding);
        return binding;
    }

    /**
     * Crea il binding per l'oggetto {@link Importo}. <br>
     * Utilizza la valuta che trova nel value model.
     *
     * @param formPropertyPath
     *            il path della proprietà nel form model
     * @return Binding
     */
    public Binding createBoundImportoTextField(String formPropertyPath) {
        Map<String, Object> context = new HashMap<String, Object>();
        ImportoBinder binder = new ImportoBinder();
        Binding binding = binder.bind(getFormModel(), formPropertyPath, context);
        interceptBinding(binding);
        return binding;
    }

    /**
     * Binding della proprietà di tipo Importo con il component ImportoTextField.
     *
     * @param formPropertyPath
     *            il path della proprietà nel form model
     * @param importoRiferimentoPath
     *            il path della proprietà importo di riferimento. Il binding aggiorna il proprio tasso e codice valuta
     *            in base a questo valueModel
     * @return Binding
     */
    public Binding createBoundImportoTextField(String formPropertyPath, String importoRiferimentoPath) {
        Map<String, Object> context = new HashMap<String, Object>();
        ImportoBinder binder = new ImportoBinder();
        context.put(ImportoBinder.IMPORTO_RIFERIMENTO_PATH_KEY, importoRiferimentoPath);
        Binding binding = binder.bind(getFormModel(), formPropertyPath, context);
        interceptBinding(binding);
        return binding;
    }

    /**
     * Binding della proprietà di tipo Importo con il component ImportoTextField.
     *
     * @param formPropertyPath
     *            il path della proprietà nel form model
     * @param importoRiferimentoPath
     *            il path della proprietà importo di riferimento. Il binding aggiorna il proprio tasso e codice valuta
     *            in base a questo valueModel
     * @param numeroDecimaliRiferimentoPath
     *            path per la proprietà che indica il numero di decimali oer l'importo.
     * @return Binding
     */
    public Binding createBoundImportoTextField(String formPropertyPath, String importoRiferimentoPath,
            String numeroDecimaliRiferimentoPath) {
        Map<String, Object> context = new HashMap<String, Object>();
        ImportoBinder binder = new ImportoBinder();
        if (importoRiferimentoPath != null) {
            context.put(ImportoBinder.IMPORTO_RIFERIMENTO_PATH_KEY, importoRiferimentoPath);
        }
        context.put(ImportoBinder.NUMERO_DECIMALI_PATH_KEY, numeroDecimaliRiferimentoPath);
        Binding binding = binder.bind(getFormModel(), formPropertyPath, context);
        interceptBinding(binding);
        return binding;
    }

    /**
     * Crea il binding per mostrare il valore in un campo formattato come valore percentuale.
     *
     * @param formPropertyPath
     *            il path della proprietà nel form model
     * @return Binding
     */
    public Binding createBoundPercentageText(String formPropertyPath) {
        logger.debug("--> Enter createBoundPercentageText");

        PercentageNumberBinder binder = (PercentageNumberBinder) Application.instance().getApplicationContext()
                .getBean("percentageNumberBinder");

        Map<String, Object> context = new HashMap<String, Object>();
        Binding binding = binder.bind(getFormModel(), formPropertyPath, context);
        interceptBinding(binding);
        logger.debug("--> Exit createBoundPercentageText");
        return binding;
    }

    /**
     * Crea il binding del componente di ricerca SearchTextField.
     *
     * @param formPropertyPath
     *            dell'attributo da ricercare
     * @param renderedProperties
     *            attributo della property da reinderizzare all'interno della TextField
     * @return binding
     */
    public Binding createBoundSearchText(String formPropertyPath, String[] renderedProperties) {
        Map<String, Object> context = new HashMap<String, Object>();
        SearchTextBinder binder = new SearchTextBinder(JTextField.class);
        context.put(SearchTextBinder.RENDERER_KEY, renderedProperties);
        Binding binding = binder.bind(getFormModel(), formPropertyPath, context);
        interceptBinding(binding);
        logger.debug("--> Exit createBoundSearchText");
        return binding;
    }

    /**
     * crea il binding del componente di ricerca SearchTextField.
     *
     * @param formPropertyPath
     *            dell'attributo da ricercare
     * @param renderedProperties
     *            attributo della property da reinderizzare all'interno della TextField
     * @param hasNewCommand
     *            indica se visualizzare il comando per la creazione di un nuovo oggetto
     * @return Binding
     */
    @Deprecated
    public Binding createBoundSearchText(String formPropertyPath, String[] renderedProperties, boolean hasNewCommand) {
        return createBoundSearchText(formPropertyPath, renderedProperties, null, null, hasNewCommand, Class.class);
    }

    /**
     * Crea il binding del componente di ricerca SearchTextField.
     *
     * @param formPropertyPath
     *            dell'attributo da ricercare
     * @param renderedProperties
     *            attributo della property da reinderizzare all'interno della TextField
     * @param clazz
     *            classe associata alla searchObject da utilizzare
     * @return binding
     */
    public Binding createBoundSearchText(String formPropertyPath, String[] renderedProperties, Class<?> clazz) {
        Map<String, Object> context = new HashMap<String, Object>();
        SearchTextBinder binder = new SearchTextBinder(JTextField.class);
        context.put(SearchTextBinder.RENDERER_KEY, renderedProperties);
        context.put(SearchTextBinder.SEARCH_OBJECT_CLASS_KEY, clazz);
        Binding binding = binder.bind(getFormModel(), formPropertyPath, context);
        interceptBinding(binding);
        logger.debug("--> Exit createBoundSearchText");
        return binding;
    }

    /**
     * Crea il binding del componente di ricerca SearchTextField specificandone le proprietà su cui basare i filtri per
     * la ricerca.
     *
     * @param formPropertyPath
     *            dell'attributo da ricercare
     * @param renderedProperties
     *            attributo della property da renderizzare all'interno della TextField
     * @param filterPropertyPaths
     *            properties path degli attributi su cui basare i filtri per la ricerca (path basato sul formModel del
     *            form)
     * @param filterNames
     *            name identificati di filterPropertyPaths utilizzati per recuperarne il valore
     * @return binding SearchText
     */
    public Binding createBoundSearchText(String formPropertyPath, String[] renderedProperties,
            String[] filterPropertyPaths, String[] filterNames) {
        return createBoundSearchText(formPropertyPath, renderedProperties, filterPropertyPaths, filterNames,
                (Class<?>) null);
    }

    /**
     * Crea il binding del componente di ricerca SearchTextField specificandone le proprietà su cui basare i filtri per
     * la ricerca.
     *
     * @param formPropertyPath
     *            dell'attributo da ricercare
     * @param renderedProperties
     *            attributo della property da reinderizzare all'interno della TextField
     * @param filterPropertyPaths
     *            properties path degli attributi su cui basare i filtri per la ricerca,altrimenti utilizzare il
     *            prefisso FIXED_VALUE per indicare un valore fisso.
     * @param filterNames
     *            name identificati di filterPropertyPaths utilizzati per recuperarne il valore
     * @param hasNewCommand
     *            indica se visualizzare il comando per la creazione di un nuovo oggetto
     * @return Binding
     */
    @Deprecated
    public Binding createBoundSearchText(String formPropertyPath, String[] renderedProperties,
            String[] filterPropertyPaths, String[] filterNames, boolean hasNewCommand) {
        return createBoundSearchText(formPropertyPath, renderedProperties, filterPropertyPaths, filterNames,
                hasNewCommand, Class.class);
    }

    /**
     * Crea il binding del componente di ricerca SearchTextField specificandone le proprietà su cui basare i filtri per
     * la ricerca.
     *
     * @param formPropertyPath
     *            dell'attributo da ricercare
     * @param renderedProperties
     *            attributo della property da reinderizzare all'interno della TextField
     * @param filterPropertyPaths
     *            properties path degli attributi su cui basare i filtri per la ricerca,altrimenti utilizzare il
     *            prefisso FIXED_VALUE per indicare un valore fisso.
     * @param filterNames
     *            name identificati di filterPropertyPaths utilizzati per recuperarne il valore
     * @param hasNewCommand
     *            indica se visualizzare il comando per la creazione di un nuovo oggetto
     * @param searchRegistryClass
     *            indica la classe della chiave della mappa del registro del componente di ricerca
     * @return Binding
     */
    @SuppressWarnings("rawtypes")
    @Deprecated
    public Binding createBoundSearchText(String formPropertyPath, String[] renderedProperties,
            String[] filterPropertyPaths, String[] filterNames, boolean hasNewCommand, Class searchRegistryClass) {
        logger.debug("--> Enter createBoundSearchText");
        Map<String, Object> context = new HashMap<String, Object>();
        Binder binder = new SearchTextBinder(JTextField.class);
        context.put(SearchTextBinder.RENDERER_KEY, renderedProperties);
        context.put(SearchTextBinder.FILTERPROPERTYPATH_KEY, filterPropertyPaths);
        context.put(SearchTextBinder.FILTERNAME_KEY, filterNames);
        Binding binding = binder.bind(getFormModel(), formPropertyPath, context);
        interceptBinding(binding);
        logger.debug("--> Exit createBoundSearchText");
        return binding;
    }

    /**
     * Crea il binding del componente di ricerca SearchTextField specificandone le proprietà su cui basare i filtri per
     * la ricerca.
     *
     * @param formPropertyPath
     *            dell'attributo da ricercare
     * @param renderedProperties
     *            attributo della property da renderizzare all'interno della TextField
     * @param filterPropertyPaths
     *            properties path degli attributi su cui basare i filtri per la ricerca (path basato sul formModel del
     *            form)
     * @param filterNames
     *            name identificati di filterPropertyPaths utilizzati per recuperarne il valore
     * @param clazz
     *            classe associata alla searchObject da utilizzare
     * @return binding SearchText
     */
    public Binding createBoundSearchText(String formPropertyPath, String[] renderedProperties,
            String[] filterPropertyPaths, String[] filterNames, Class<?> clazz) {
        logger.debug("--> Enter createBoundSearchText");
        Map<String, Object> context = new HashMap<String, Object>();
        Binder binder = new SearchTextBinder(JTextField.class);
        context.put(SearchTextBinder.SEARCH_OBJECT_CLASS_KEY, clazz);
        context.put(SearchTextBinder.RENDERER_KEY, renderedProperties);
        context.put(SearchTextBinder.FILTERPROPERTYPATH_KEY, filterPropertyPaths);
        context.put(SearchTextBinder.FILTERNAME_KEY, filterNames);
        Binding binding = binder.bind(getFormModel(), formPropertyPath, context);
        interceptBinding(binding);
        logger.debug("--> Exit createBoundSearchText");
        return binding;
    }

    /**
     * Crea il binding del componente di ricerca SearchTextField specificandone le proprietà su cui basare i filtri per
     * la ricerca.
     *
     * @param formPropertyPath
     *            dell'attributo da ricercare
     * @param renderedProperties
     *            attributo della property da renderizzare all'interno della TextField
     * @param filterPropertyPaths
     *            properties path degli attributi su cui basare i filtri per la ricerca (path basato sul formModel del
     *            form)
     * @param filterNames
     *            name identificati di filterPropertyPaths utilizzati per recuperarne il valore
     * @param searchTextClass
     *            la classe da istanziare come componente del searchPanel
     * @return binding SearchText
     */
    public Binding createBoundSearchText(String formPropertyPath, String[] renderedProperties,
            String[] filterPropertyPaths, String[] filterNames, String searchTextClass) {
        logger.debug("--> Enter createBoundSearchText");
        Map<String, Object> context = new HashMap<String, Object>();
        Binder binder = new SearchTextBinder(JTextField.class);
        ((SearchTextBinder) binder).setSearchTextClass(searchTextClass);
        context.put(SearchTextBinder.RENDERER_KEY, renderedProperties);
        context.put(SearchTextBinder.FILTERPROPERTYPATH_KEY, filterPropertyPaths);
        context.put(SearchTextBinder.FILTERNAME_KEY, filterNames);
        Binding binding = binder.bind(getFormModel(), formPropertyPath, context);
        interceptBinding(binding);
        logger.debug("--> Exit createBoundSearchText");
        return binding;
    }

    @Override
    public Binding createBoundShuttleList(String selectionFormProperty, Object selectableItems) {
        return createBoundShuttleList(selectionFormProperty, new ValueHolder(selectableItems), null);
    }

    /**
     * Crea il binding per la shuttle list.
     *
     * @param selectionFormProperty
     *            selectionFormProperty
     * @param selectableItemsHolder
     *            value holder degli elementi selezionabili, se è un refreshableValueHolder, viene aggiunto un command
     *            di refresh
     * @param renderedProperty
     *            renderedProperty
     * @return dualListBinding
     */
    @Override
    public Binding createBoundShuttleList(String selectionFormProperty, ValueModel selectableItemsHolder,
            String renderedProperty) {
        Map<?, ?> context = DualListBinder.createBindingContext(getFormModel(), selectionFormProperty,
                selectableItemsHolder, renderedProperty);
        DualListBinder dualListBinder = new DualListBinder();
        Binding binding = dualListBinder.bind(getFormModel(), selectionFormProperty, context);
        interceptBinding(binding);
        return binding;
    }

    /**
     * Crea il binding dei dati geografici.
     *
     * @param formPropertyPath
     *            formPropertyPath
     * @param firstColumnDef
     *            firstColumnDef
     * @return Binding
     */
    public Binding createDatiGeograficiBinding(String formPropertyPath, String firstColumnDef) {
        return createDatiGeograficiBinding(formPropertyPath, firstColumnDef, true);
    }

    /**
     * Crea il binding dei dati geografici.
     *
     * @param formPropertyPath
     *            formPropertyPath
     * @param firstColumnDef
     *            firstColumnDef
     * @param showNazioneControls
     *            visualizza o nasconde la nazione
     * @return Binding
     */
    public Binding createDatiGeograficiBinding(String formPropertyPath, String firstColumnDef,
            boolean showNazioneControls) {
        Binder binder = new DatiGeograficiBinder();
        Map<String, Object> context = new HashMap<String, Object>();
        context.put(DatiGeograficiBinder.FORM_FIRST_COLUMN_DEF, firstColumnDef);
        context.put(DatiGeograficiBinder.SHOW_NAZIONE_CONTROLS, showNazioneControls);
        Binding binding = binder.bind(getFormModel(), formPropertyPath, context);
        interceptBinding(binding);
        return binding;
    }

    /**
     * Crea il binding per mostrare il valore del form model in radio button.
     *
     * @param requiredSourceClass
     *            la classe enum
     * @param formPropertyPath
     *            il path della proprietà nel form model
     * @return Binding
     */
    public Binding createEnumRadioButtonBinding(Class<?> requiredSourceClass, String formPropertyPath) {
        return createEnumRadioButtonBinding(requiredSourceClass, formPropertyPath, SwingConstants.HORIZONTAL);
    }

    /**
     * Create radio button binding for the form property with either vertical or horizontal layout.
     *
     * @param requiredSourceClass
     *            the enum property class
     * @param formProperty
     *            the form property to bind
     * @param layout
     *            The value of the layout argument can be one of <code>SwingConstants.VERTICAL</code> or
     *            <code>SwingConstants.HORIZONTAL</code> which is default
     * @return the radio button binding for the property
     */
    public Binding createEnumRadioButtonBinding(Class<?> requiredSourceClass, String formProperty, int layout) {
        EnumRadioButtonBinder radioButtonBinder = new EnumRadioButtonBinder(requiredSourceClass, layout);
        Map<String, Object> context = new HashMap<String, Object>();
        Binding binding = radioButtonBinder.bind(getFormModel(), formProperty, context);
        interceptBinding(binding);
        return binding;
    }

    /**
     * Binda una proprietà creando una combo box non editabile e internazionalizzando il suo valore.
     *
     * @param formProperty
     *            proprietà da bindare
     * @param selectableItemsHolder
     *            lista di oggetti da inserire nella combo box
     * @param renderedProperty
     *            proprietà da renderizzare e internazionalizzare
     * @return Binding
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public Binding createI18NBoundComboBox(String formProperty, ValueModel selectableItemsHolder,
            String renderedProperty) {
        Map context = createContext(AbstractListBinder.SELECTABLE_ITEMS_KEY, selectableItemsHolder);
        context.put(ComboBoxBinder.RENDERER_KEY, new I18NBeanPropertyValueListRenderer(renderedProperty,
                (MessageSource) Application.services().getService(MessageSource.class)));
        context.put(ComboBoxBinder.EDITOR_KEY, new I18NBeanPropertyEditorClosure(renderedProperty));
        context.put(AbstractListBinder.COMPARATOR_KEY, new PropertyComparator(renderedProperty, true, true));
        Binder binder = new ReadOnlyComboBoxBinder();
        Binding binding = binder.bind(getFormModel(), formProperty, context);
        return binding;
    }

    /**
     * Crea una tabella bindata al form model.
     *
     * @param formProperty
     *            proprietà da bindare
     * @param width
     *            larghezza della tabella in dlu (il layout utilizzato è il {@link FormLayout}
     * @param tableModel
     *            tableModel da utilizzare
     * @param formInserimento
     *            form per inserire delle righe nella tabella. Se presente verrà anche inserito il pulsante per
     *            eliminare le righe selezionate
     * @return binding
     */
    @SuppressWarnings("rawtypes")
    public Binding createTableBinding(String formProperty, int width, DefaultBeanTableModel tableModel,
            AbstractFocussableForm formInserimento) {
        return createTableBinding(formProperty, width, tableModel, formInserimento, null);
    }

    /**
     * Crea una tabella bindata al form model.
     *
     * @param formProperty
     *            proprietà da bindare
     * @param width
     *            larghezza della tabella in dlu (il layout utilizzato è il {@link FormLayout}
     * @param tableModel
     *            tableModel da utilizzare
     * @param formInserimento
     *            form per inserire delle righe nella tabella. Se presente verrà anche inserito il pulsante per
     *            eliminare le righe selezionate
     * @param tableWidget
     *            widget della tabella creato precedentemente.
     * @return binding
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public Binding createTableBinding(String formProperty, int width, DefaultBeanTableModel tableModel,
            AbstractFocussableForm formInserimento, JideTableWidget tableWidget) {
        TableBinding<T> binding = new TableBinding<T>(getFormModel(), formProperty, width);
        binding.setTableModel(tableModel);
        binding.setFormInsert(formInserimento);
        binding.setTableWidget(tableWidget);
        interceptBinding(binding);
        return binding;
    }

}
