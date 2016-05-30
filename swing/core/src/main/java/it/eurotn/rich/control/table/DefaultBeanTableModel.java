package it.eurotn.rich.control.table;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.event.TableModelEvent;
import javax.swing.table.AbstractTableModel;

import org.apache.commons.beanutils.PropertyUtilsBean;
import org.apache.log4j.Logger;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.NullValueInNestedPathException;
import org.springframework.binding.form.FieldFaceSource;
import org.springframework.richclient.application.ApplicationServicesLocator;
import org.springframework.util.ClassUtils;

import com.jidesoft.converter.ConverterContext;
import com.jidesoft.grid.ContextSensitiveTableModel;
import com.jidesoft.grid.EditorContext;

import it.eurotn.panjea.rich.factory.navigationloader.NavigationLoaderContext;

public class DefaultBeanTableModel<T> extends AbstractTableModel implements ContextSensitiveTableModel {

    private static final long serialVersionUID = 3702641425343378792L;

    private static Logger logger = Logger.getLogger(DefaultBeanTableModel.class);
    // private BeanWrapper beanWrapper;

    private String[] columnLabels;

    private String[] columnPropertyNames;
    private Map<String, Integer> columnPropertyPosition;

    private final String modelId;

    private FieldFaceSource fieldFaceSource;

    protected Class<T> classeObj;

    protected List<T> source;

    private final HashMap<Integer, Class<?>> columnClasses = new HashMap<Integer, Class<?>>();

    private PropertyUtilsBean beanUtil;

    private Class<?> prototypeClass;

    /**
     * Constructor using the provided row data and column property names. The model Id will be set
     * from the class name of the given <code>beanClass</code>.
     *
     * @param beanClass
     *            .
     * @param rows
     *            .
     * @param columnPropertyNames
     *            .
     * @param classe
     *            .
     */
    public DefaultBeanTableModel(final Class<?> beanClass, final List<T> rows, final String[] columnPropertyNames,
            final Class<T> classe) {
        this(ClassUtils.getShortName(beanClass), rows, columnPropertyNames, classe);
    }

    /**
     *
     * @param modelId
     *            id modello .
     * @param rows
     *            .
     * @param columnPropertyNames
     *            .
     * @param classe
     *            .
     */
    public DefaultBeanTableModel(final String modelId, final List<T> rows, final String[] columnPropertyNames,
            final Class<T> classe) {
        this(modelId, rows, columnPropertyNames, classe, classe);
    }

    /**
     * Fully specified Constructor.
     *
     * @param rows
     *            The data for the model
     * @param columnPropertyNames
     *            Names of properties to show in the table columns
     * @param modelId
     *            Id for this model, used to create column header message keys
     * @param classe
     *            classe
     * @param prototypeClass
     *            .
     */
    public DefaultBeanTableModel(final String modelId, final List<T> rows, final String[] columnPropertyNames,
            final Class<T> classe, final Class<?> prototypeClass) {
        this.source = rows;
        this.prototypeClass = prototypeClass;
        this.modelId = modelId;
        this.columnPropertyNames = columnPropertyNames;
        this.classeObj = classe;
        // beanWrapper = new BeanWrapperImpl(prototypeClass);

        beanUtil = new PropertyUtilsBean();
    }

    /**
     *
     * @param modelId
     *            .
     * @param columnPropertyNames
     *            .
     * @param classe
     *            .
     */
    public DefaultBeanTableModel(final String modelId, final String[] columnPropertyNames, final Class<T> classe) {
        this(modelId, new ArrayList<T>(), columnPropertyNames, classe);
    }

    /**
     *
     * @param modelId
     *            .
     * @param columnPropertyNames
     *            .
     * @param classe
     *            .
     * @param prototypeClass
     *            .
     */
    public DefaultBeanTableModel(final String modelId, final String[] columnPropertyNames, final Class<T> classe,
            final Class<?> prototypeClass) {
        this(modelId, new ArrayList<T>(), columnPropertyNames, classe, prototypeClass);
    }

    /**
     *
     * @param object
     *            oggetto da aggiungere al modello
     */
    public void addObject(T object) {
        this.source.add(object);
        fireTableDataChanged();
    }

    /**
     *
     * @param objects
     *            oggetti da aggiungere al modello
     */
    public void addObjects(List<T> objects) {
        this.source.addAll(objects);
        fireTableDataChanged();
    }

    /**
     * Create the text for the column headers. Use the model Id (if any) and the column property
     * name to generate a series of message keys. Resolve those keys using the configured message
     * source.
     *
     * @param propertyColumnNames
     * @return array of column header text
     */
    protected String[] createColumnNames(String[] propertyColumnNames) {
        columnPropertyPosition = new HashMap<String, Integer>();
        int size = propertyColumnNames.length;
        String[] columnNames = new String[size];
        for (int i = 0; i < size; i++) {
            columnNames[i] = getFieldFaceSource()
                    .getFieldFace(propertyColumnNames[i].replaceAll("\\[0\\]", ""), getModelId()).getLabelInfo()
                    .getText();
            columnPropertyPosition.put(columnPropertyNames[i], i);
        }
        return columnNames;
    }

    @Override
    public Class<?> getCellClassAt(int row, int column) {
        return getColumnClass(column);
    }

    /**
     *
     * @return classe gestita dal tabele model
     */
    public Class<T> getClassObj() {
        return classeObj;
    }

    @Override
    public Class<?> getColumnClass(int column) {
        Integer columnKey = new Integer(column);
        Class<?> cls = columnClasses.get(columnKey);
        String propertyName = getColumnPropertyNames()[column];
        if (cls == null) {
            if (propertyName.contains(".")) {
                String[] innerPropertyName = propertyName.split("\\.");
                cls = getPropertyClass(prototypeClass, innerPropertyName[0]);
                for (int i = 1; i < innerPropertyName.length; i++) {
                    cls = getPropertyClass(cls, innerPropertyName[i]);
                }
            } else {
                PropertyDescriptor[] descs = beanUtil.getPropertyDescriptors(prototypeClass);
                for (PropertyDescriptor propertyDescriptor : descs) {
                    if (propertyName.equalsIgnoreCase(propertyDescriptor.getName())) {
                        cls = propertyDescriptor.getPropertyType();
                        break;
                    }
                }
            }
        }

        // If we found something, then put it in the cache. If not, return
        // Object.
        if (cls != null) {
            columnClasses.put(columnKey, cls);
        } else {
            cls = Object.class;
        }

        return cls;
    }

    @Override
    public int getColumnCount() {
        return getColumnLabels().length;
    }

    protected String[] getColumnLabels() {
        if (columnLabels == null) {
            columnLabels = createColumnNames(columnPropertyNames);
        }
        return columnLabels;
    }

    @Override
    public String getColumnName(int column) {
        if (column < 0 || column > getColumnLabels().length) {
            logger.warn("Colonna nel table model non presente. Indice colonna: " + column);
            return "";
        }
        return getColumnLabels()[column];
    }

    /**
     *
     * @return nome delle colonne
     */
    public String[] getColumnPropertyNames() {
        return columnPropertyNames;
    }

    /**
     * @return Returns the columnPropertyPosition.
     */
    public Map<String, Integer> getColumnPropertyPosition() {
        return columnPropertyPosition;
    }

    protected Object getColumnValue(Object row, int column) {
        if (row == null) {
            return "";
        }
        if (row.getClass().getName().equals(String.class.getName())) {
            return row;
        }
        try {
            return beanUtil.getProperty(row, columnPropertyNames[column]);
        } catch (NullValueInNestedPathException nvinp) {
            if (logger.isDebugEnabled()) {
                logger.debug("--> valore null...ritorno stringa vuota");
            }
        } catch (NullPointerException npe) {
            // Ogni tanto sull'esportazione genera un errore (npe con una
            // suppressWarning). Però dai test
            // l'esportazione va buon fine. Quindi trappo e non faccio nulla.
            logger.warn("Errore nella column Value. Generato probabilmente dal'esportazione");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    @Override
    public ConverterContext getConverterContextAt(int row, int column) {
        return null;
    }

    @Override
    public EditorContext getEditorContextAt(int row, int column) {
        return null;
    }

    /**
     *
     * @param index
     *            indice
     * @return riga all'indice index
     */
    public T getElementAt(int index) {
        return source.get(index);
    }

    protected FieldFaceSource getFieldFaceSource() {
        if (fieldFaceSource == null) {
            fieldFaceSource = (FieldFaceSource) ApplicationServicesLocator.services().getService(FieldFaceSource.class);
        }
        return fieldFaceSource;
    }

    /**
     * Get the model Id.
     *
     * @return model Id
     */
    public String getModelId() {
        return modelId;
    }

    /**
     * Restituisce gli ulteriori context da utilizzare per i navigation loaders.
     *
     * @param row
     *            riga
     * @param column
     *            colonna
     * @return context
     */
    public NavigationLoaderContext[] getNavigationLoadersContextAt(int row, int column) {
        return null;
    }

    /**
     *
     * @param index
     *            indice
     * @return riga all'indice index
     */
    public T getObject(int index) {

        return this.source.get(index);
    }

    /**
     *
     * @return righe del modello
     */
    public List<T> getObjects() {
        return this.source;
    }

    private Class<?> getPropertyClass(Class<?> clazz, String nomeProp) {
        Class<?> cls = null;
        try {
            if (nomeProp.contains("[0]")) {
                String nomePropPlain = nomeProp.replaceAll("\\[0\\]", "");
                BeanWrapper innerBeanWrapper = new BeanWrapperImpl(clazz);
                cls = innerBeanWrapper.getPropertyType(nomeProp);
                BeanInfo info = Introspector.getBeanInfo(clazz);
                for (PropertyDescriptor propertyDescriptor : info.getPropertyDescriptors()) {
                    if (propertyDescriptor.getName().equals(nomePropPlain)) {
                        cls = propertyDescriptor.getPropertyType();
                        if (Collection.class.isAssignableFrom(cls)) {
                            Field field = clazz.getDeclaredField(nomePropPlain);
                            cls = field.getType();
                            Type genericFieldType = field.getGenericType();
                            if (genericFieldType instanceof ParameterizedType) {
                                ParameterizedType type = (ParameterizedType) genericFieldType;
                                Type[] fieldArgTypes = type.getActualTypeArguments();
                                for (Type fieldArgType : fieldArgTypes) {
                                    cls = (Class<?>) fieldArgType;
                                }
                            }
                        }
                    }
                }

            } else {
                PropertyDescriptor[] descs = beanUtil.getPropertyDescriptors(clazz);
                for (PropertyDescriptor propertyDescriptor : descs) {
                    if (nomeProp.equalsIgnoreCase(propertyDescriptor.getName())) {
                        cls = propertyDescriptor.getPropertyType();
                        break;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cls;
    }

    @Override
    public int getRowCount() {
        return source.size();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return getColumnValue(this.source.get(rowIndex), columnIndex);
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }

    /**
     *
     * @param object
     *            rimuove l'oggetto dal table model
     */
    public void removeObject(T object) {
        this.source.remove(object);
        fireTableDataChanged();
    }

    protected Object setColumnValue(Object row, Object value, int column) {
        String beanPropertyName = columnPropertyNames[column];
        try {
            beanUtil.setProperty(row, beanPropertyName, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // beanWrapper.setWrappedInstance(row);
        // beanWrapper.setPropertyValue(columnPropertyNames[column], value);
        return row;
    }

    protected void setFieldFaceSource(FieldFaceSource fieldFaceSource) {
        this.fieldFaceSource = fieldFaceSource;
    }

    /**
     *
     * @param object
     *            oggetto da aggiornare/aggiungere
     */
    public void setObject(T object) {
        int index = this.source.indexOf(object);
        if (index >= 0) {
            setObject(object, index);
        } else {
            addObject(object);
        }
    }

    /**
     * Setta l'oggetto nel table model, sostituendolo a quello presente nell'index Index.
     *
     * @param object
     *            oggetto da settare
     * @param index
     *            indice d
     */
    public void setObject(T object, int index) {
        this.source.set(index, object);
        TableModelEvent event = new TableModelEvent(this);
        fireTableChanged(event);
    }

    /**
     * setta le righe nel table model rimuovendo eventuali righe precedenti.
     *
     * @param rows
     *            righe da settare nel tableModel
     */
    public void setRows(Collection<T> rows) {
        this.source = new ArrayList<T>(rows);
        TableModelEvent event = new TableModelEvent(this);
        fireTableChanged(event);
    }

    @Override
    public void setValueAt(Object editedValue, int row, int column) {
        // get the object being edited from the source list
        final T baseObject = source.get(row);

        // tell the table format to set the value based
        setColumnValue(baseObject, editedValue, column);
    }
}