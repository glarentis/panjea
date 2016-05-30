package it.eurotn.rich.control.table.editor;

import it.eurotn.panjea.utils.PanjeaSwingUtil;
import it.eurotn.rich.binding.searchtext.SearchTextBinder;
import it.eurotn.rich.form.PanjeaFormModelHelper;

import java.util.HashMap;
import java.util.Map.Entry;

import org.springframework.binding.form.support.DefaultFieldMetadata;
import org.springframework.binding.form.support.DefaultFormModel;
import org.springframework.binding.form.support.FormModelMediatingValueModel;
import org.springframework.binding.value.ValueModel;
import org.springframework.binding.value.support.ValueHolder;

import com.jidesoft.grid.EditorContext;

public class SearchContext extends EditorContext {

    private class CustomValue {

        private String propertyPath;
        private Object value;

        /**
         * Costruttore.
         * 
         * @param propertyPath
         *            nome della proprietà da utilizzare
         * @param value
         *            valore della proprietà
         */
        public CustomValue(final String propertyPath, final Object value) {
            super();
            this.propertyPath = propertyPath;
            this.value = value;
        }

        /**
         * @return Returns the filterKey.
         */
        public String getPropertyPath() {
            return propertyPath;
        }

        /**
         * @return Returns the value.
         */
        public Object getValue() {
            return value;
        }

    }

    private static final long serialVersionUID = 7463427115580869533L;
    private String[] rendererProperties;
    private String searchTextClass;
    private Class<?> searchObjectClassKey;
    private HashMap<String, String> propertyFilter;
    private HashMap<String, CustomValue> propertyFilterValue;

    private String basePropertyPath = null;

    /**
     * 
     * Costruttore.
     * 
     */
    public SearchContext() {
        super("searchContext");
    }

    /**
     * 
     * Costruttore.
     * 
     * @param rendererProperties
     *            .
     */
    public SearchContext(final String rendererProperties) {
        super("searchContext");
        this.rendererProperties = new String[] { rendererProperties };
    }

    /**
     * 
     * Costruttore.
     * 
     * @param rendererProperties
     *            .
     * @param basePropertyPath
     *            .
     */
    public SearchContext(final String rendererProperties, final String basePropertyPath) {
        this(rendererProperties);
        this.basePropertyPath = basePropertyPath;
    }

    /**
     * Aggiunge una proprietà al contesto utilizzata nella searchObject.
     * 
     * @param propertyFilterPath
     *            nome del filtro di ricerca per la proprietà
     * @param propertyPath
     *            nome della proprietà da utilizzare
     */
    public void addPropertyFilter(String propertyFilterPath, String propertyPath) {
        if (propertyFilter == null) {
            propertyFilter = new HashMap<String, String>();
        }
        propertyFilter.put(propertyFilterPath, propertyPath);
    }

    /**
     * Aggiunge una proprietà al contesto utilizzata nella searchObject.
     * 
     * @param propertyFilterPath
     *            nome del filtro di ricerca per la proprietà
     * @param propertyPath
     *            nome della proprietà da utilizzare
     * @param value
     *            valore della proprietà
     */
    public void addPropertyFilterValue(String propertyFilterPath, String propertyPath, Object value) {
        if (propertyFilterValue == null) {
            propertyFilterValue = new HashMap<String, CustomValue>();
        }
        propertyFilterValue.put(propertyFilterPath, new CustomValue(propertyPath, value));
    }

    /**
     * Crea il form model dell'oggetto. Vengono aggiunti automaticamente tutti i value model delle proprietà fittizie.
     * 
     * @param object
     *            oggetto
     * @return form model creato
     */
    public DefaultFormModel createFormModel(Object object) {
        DefaultFormModel formModel = (DefaultFormModel) PanjeaFormModelHelper
                .createFormModel(PanjeaSwingUtil.cloneObject(object), false, "rowTableEditable");

        if (getPropertyFilterValue() != null) {
            // aggiungo le proprietà al form model
            // Aggiungo come parametri il nome della proprietà e lo imposto anche come path.
            for (Entry<String, CustomValue> elementProperty : getPropertyFilterValue().entrySet()) {
                ValueModel valueModelProperty = new ValueHolder(elementProperty.getValue().getValue());
                DefaultFieldMetadata metaData = new DefaultFieldMetadata(formModel,
                        new FormModelMediatingValueModel(valueModelProperty), Object.class, true, null);
                formModel.add(elementProperty.getValue().getPropertyPath(), valueModelProperty, metaData);
                // aggiungo la proprietà finta alle filter property tanto ora è presente come proprietà nel form model
                addPropertyFilter(elementProperty.getKey(), elementProperty.getValue().getPropertyPath());
            }
        }
        return formModel;
    }

    /**
     * @return Returns the basePropertyPath.
     */
    public String getBasePropertyPath() {
        return basePropertyPath;
    }

    /**
     * 
     * @return binding da associare alla serachObject.
     */
    public HashMap<String, Object> getBindingContext() {
        HashMap<String, Object> context = new HashMap<String, Object>();
        if (rendererProperties != null) {
            context.put(SearchTextBinder.RENDERER_KEY, this.rendererProperties);
        }

        if (searchTextClass != null) {
            context.put(SearchTextBinder.SEARCH_TEXT_CLASS_KEY, this.searchTextClass);
        }

        if (searchObjectClassKey != null) {
            context.put(SearchTextBinder.SEARCH_OBJECT_CLASS_KEY, searchObjectClassKey);
        }

        if (!getPropertyFilter().isEmpty()) {
            context.put(SearchTextBinder.FILTERPROPERTYPATH_KEY, propertyFilter.values().toArray());
            context.put(SearchTextBinder.FILTERNAME_KEY, propertyFilter.keySet().toArray());
        }
        return context;
    }

    /**
     * 
     * @return proprietà da utilizzare nella searchObject.
     */
    public HashMap<String, String> getPropertyFilter() {
        if (propertyFilter == null) {
            propertyFilter = new HashMap<String, String>();
        }
        return propertyFilter;
    }

    /**
     * @return Returns the propertyFilterValue.
     */
    public HashMap<String, CustomValue> getPropertyFilterValue() {
        return propertyFilterValue;
    }

    /**
     * @return Returns the searchObjectClassKey.
     */
    public Class<?> getSearchObjectClassKey() {
        return searchObjectClassKey;
    }

    /**
     * @return Returns the searchTextClass.
     */
    public String getSearchTextClass() {
        return searchTextClass;
    }

    /**
     * @param searchObjectClassKey
     *            The searchObjectClassKey to set.
     */
    public void setSearchObjectClassKey(Class<?> searchObjectClassKey) {
        this.searchObjectClassKey = searchObjectClassKey;
    }

    /**
     * @param searchTextClass
     *            The searchTextClass to set.
     */
    public void setSearchTextClass(String searchTextClass) {
        this.searchTextClass = searchTextClass;
    }
}