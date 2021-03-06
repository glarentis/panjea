package it.eurotn.rich.control.table;

import it.eurotn.panjea.exceptions.PanjeaRuntimeException;
import it.eurotn.rich.control.table.editor.SearchContext;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.swing.BorderFactory;

import org.apache.log4j.Logger;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.richclient.application.ApplicationServicesLocator;
import org.springframework.rules.PropertyConstraintProvider;
import org.springframework.rules.Rules;
import org.springframework.rules.RulesSource;
import org.springframework.rules.constraint.property.PropertyConstraint;
import org.springframework.rules.reporting.BeanValidationResultsCollector;
import org.springframework.rules.reporting.PropertyResults;

import com.jidesoft.grid.CellStyle;
import com.jidesoft.grid.EditorContext;
import com.jidesoft.grid.NavigableTableModel;
import com.jidesoft.grid.StyleModel;

/**
 * Implementa la possibiltà di aggiunta della riga (oltre alla modifica).
 *
 * @author giangi
 * @version 1.0, 26/ott/2012
 *
 */
public class DefaultBeanEditableTableModel<T> extends DefaultBeanTableModel<T>
        implements StyleModel, NavigableTableModel {

    private static final long serialVersionUID = 1717803229894810522L;
    protected boolean isNewRow;
    private static CellStyle invalidDataStyle = new CellStyle();

    static {
        invalidDataStyle.setBorder(BorderFactory.createLineBorder(Color.red, 1));
    }

    private static Logger logger = Logger.getLogger(DefaultBeanEditableTableModel.class);
    private RulesSource rulesSource;

    {
        addNewEmptyRow();
        isNewRow = false;
    };

    /**
     *
     * @param modelId
     *            Id for this model, used to create column header message keys
     * @param columnPropertyNames
     *            Names of properties to show in the table columns
     * @param classe
     *            classe
     */
    public DefaultBeanEditableTableModel(final String modelId, final String[] columnPropertyNames,
            final Class<T> classe) {
        super(modelId, columnPropertyNames, classe);
    }

    /**
     * Crea una nuova riga nel modello.<br/>
     * Se esiste già una nuova riga non viene aggiunta.
     */
    public void addNewEmptyRow() {
        if (isNewRow) {
            return;
        }
        T newObject = createNewObject();
        if (newObject != null) {
            addObject(newObject);
            isNewRow = true;
        }
    }

    /**
     * Valida una proprietà di una riga in base alle {@link Rules}.
     *
     * @param row
     *            riga da validare
     * @param propertyName
     *            nome della proprietà da validare
     * @return true se la riga è valida
     */
    private boolean checkObject(int row, String propertyName) {
        T object = getElementAt(row);
        return checkObject(object, propertyName);
    }

    /**
     * Valida la proprietà di un oggettoin base alle {@link Rules}.
     *
     * @param object
     *            oggettoda validare
     * @param propertyName
     *            nome della proprietà da validare
     * @return true se la riga è valida
     */
    public boolean checkObject(T object, String propertyName) {
        PropertyResults results = null;
        Rules rules = null;
        Class<?> objectClass = object.getClass();
        if (object instanceof PropertyConstraintProvider) {
            PropertyConstraintProvider propertyConstraintProvider = (PropertyConstraintProvider) object;
            PropertyConstraint validationRule = propertyConstraintProvider.getPropertyConstraint(propertyName);
            results = checkRule(validationRule, object);
        } else {
            rules = getRulesSource().getRules(objectClass, (String) null);
            if (rules != null) {
                for (Iterator<?> i = rules.iterator(); i.hasNext();) {
                    PropertyConstraint validationRule = (PropertyConstraint) i.next();
                    if (validationRule.isDependentOn(propertyName)) {
                        results = checkRule(validationRule, object);
                    }
                }
            } else {
                logger.debug("No rules source has been configured; "
                        + "please set a valid reference to enable rules-based validation.");
            }
        }
        return results == null ? true : false;
    }

    /**
     * Valida una costraint.
     *
     * @param validationRule
     *            constraint da validare
     * @param objectToCheck
     *            oggetto da validare
     * @return risultato validazione
     */
    private PropertyResults checkRule(PropertyConstraint validationRule, Object objectToCheck) {
        BeanValidationResultsCollector resultsCollector = new BeanValidationResultsCollector(objectToCheck);
        PropertyResults results = resultsCollector.collectPropertyResults(validationRule);
        return results;
    }

    /**
     * Crea un nuovo oggetto da inserire nella nuova riga nella tabella editabile.
     *
     * @return oggetto creato tramite costruttore vuoto.
     */
    protected T createNewObject() {
        try {
            return classeObj.newInstance();
        } catch (Exception e) {
            logger.error("-->errore nell'istanziare un oggetto di classe " + classeObj.getName(), e);
            throw new PanjeaRuntimeException(e);
        }
    }

    @Override
    public CellStyle getCellStyleAt(int row, int column) {
        if (isAllowInsert() && row == getRowCount() - 1) {
            return null;
        }
        if (!checkObject(row, getColumnPropertyNames()[column])) {
            return invalidDataStyle;
        }
        return null;
    }

    @Override
    public List<T> getObjects() {
        List<T> objects = new ArrayList<T>(super.getObjects());
        if (!objects.isEmpty() && isAllowInsert()) {
            objects.remove(objects.size() - 1);
        }
        return objects;
    }

    /**
     *
     * @return ruleSource da utilizzare per le validazioni
     */
    private RulesSource getRulesSource() {
        if (rulesSource == null) {
            rulesSource = (RulesSource) ApplicationServicesLocator.services().getService(RulesSource.class);
        }
        return rulesSource;
    }

    /**
     * @return true per poter inserire record (quindi preparare il record nuovo con addNewEmpryRow)
     */
    protected boolean isAllowInsert() {
        return true;
    }

    /**
     * @return true per poter eliminare un record
     */
    protected boolean isAllowRemove() {
        return true;
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return true;
    }

    @Override
    public boolean isCellStyleOn() {
        return true;
    }

    /**
     *
     * @param object
     *            oggetto da controllare
     * @return true se sono sulla riga che sto inserendo
     */
    public boolean isInsertObject(T object) {
        if (!source.isEmpty()) {
            T lastObj = this.source.get(source.size() - 1);
            // devo controllare se è l'ultimo oggetto e se posso inserire una riga (quindi se ho la riga vuota finale)
            return lastObj == object && isAllowInsert();
        }
        return false;
    }

    @Override
    public boolean isNavigableAt(int row, int column) {
        return isCellEditable(row, column);
    }

    @Override
    public boolean isNavigationOn() {
        return true;
    }

    /**
     * @param object
     *            oggetto
     * @return <code>true</code> se l'oggetto è valido
     */
    public boolean isObjectValid(T object) {
        boolean valid = true;

        for (int i = 0; i < getColumnPropertyNames().length; i++) {
            valid = valid && checkObject(object, getColumnPropertyNames()[i]);
        }

        return valid;
    }

    /**
     * Verifica se l'oggetto alla posizione riga,colonna nuovo è uguale a quello presente in tabella. Mi può servire nel
     * caso in cui ho un oggetto (ad es. Lotto) e lo modifico, non cambia l'id, ma cambia una proprietà che devo
     * verificare manualmente senza cambiare l'equals dell'oggetto.
     *
     * @param value
     *            new value
     * @param row
     *            row
     * @param column
     *            column
     * @return true o false
     */
    protected boolean isRowObjectChanged(Object value, int row, int column) {
        Object oldValue = getValueAt(row, column);
        return value != null && value.equals(oldValue);
    }

    @Override
    public void removeObject(T object) {
        if (isAllowRemove()) {
            super.removeObject(object);
        }
    }

    @Override
    public void setRows(Collection<T> rows) {
        super.setRows(rows);
        if (isAllowInsert()) {
            addNewEmptyRow();
            isNewRow = false;
        }
    }

    @Override
    public void setValueAt(Object value, int row, int column) {
        if (isRowObjectChanged(value, row, column)) {
            return;
        }

        // Se modifico l'ultima riga ne genero una nuova;
        isNewRow = false;
        EditorContext editorContext = getEditorContextAt(row, column);
        if (editorContext instanceof SearchContext && ((SearchContext) editorContext).getBasePropertyPath() != null) {
            String baseProperty = ((SearchContext) editorContext).getBasePropertyPath();

            T element = getElementAt(row);
            BeanWrapper elementWrapper = new BeanWrapperImpl(element);
            elementWrapper.setPropertyValue(baseProperty, value);
        } else {
            super.setValueAt(value, row, column);
        }

        if (value != null && row == (getRowCount() - 1) && isAllowInsert()) {
            addNewEmptyRow();
        }
        fireTableDataChanged();
    }
}
