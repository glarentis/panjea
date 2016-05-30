/*
 * Copyright 2005 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package it.eurotn.rich.binding;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;

import javax.swing.ListCellRenderer;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.springframework.binding.convert.ConversionExecutor;
import org.springframework.binding.form.FormModel;
import org.springframework.richclient.form.binding.swing.AbstractListBinding;
import org.springframework.util.Assert;

import com.jidesoft.swing.CheckBoxList;

/**
 * A binding for the JIDE check box list with selectable component.
 * 
 * This is a copy, paste and modification from the Spring RCP ListBinding class.
 * 
 * @author Jonny Wray
 * 
 */
public class CheckBoxListSelectableBinding extends AbstractListBinding {

    // JIDE CheckBoxList difference. Change based on check boxes changing, not
    // list selection
    private class CheckBoxListener implements ListSelectionListener {
        @Override
        public void valueChanged(ListSelectionEvent e) {
            // se questo evento e' parte di una serie, associati alla stessa
            // modifica
            // non devo eseguire nessuna operazione
            if (e != null && e.getValueIsAdjusting()) {
                return;
            }
            if (!selectingValues) {
                updateSelectedItemsFromSelectionModel();
            }
        }
    }

    protected class ValueModelListener implements PropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            updateSelectedItemsFromValueModel();
        }

    }

    private static final Object[] EMPTY_VALUES = new Object[0];

    private final PropertyChangeListener valueModelListener = new ValueModelListener();

    private final ListSelectionListener checkBoxListener = new CheckBoxListener();

    private ConversionExecutor conversionExecutor;

    private boolean selectingValues;

    /**
     * Costruttore.
     * 
     * @param list
     *            {@link CheckBoxList}
     * @param formModel
     *            form model
     * @param formFieldPath
     *            path della proprietà da bindare
     * @param requiredSourceClass
     *            requiredSourceClass
     */
    public CheckBoxListSelectableBinding(final CheckBoxList list, final FormModel formModel, final String formFieldPath,
            final Class requiredSourceClass) {
        super(list, formModel, formFieldPath, requiredSourceClass);
    }

    /**
     * Converts the given values into a type that matches the fieldType.
     * 
     * @param selectedValues
     *            the selected values
     * @return the value which can be assigned to the type of the field
     */
    protected Object convertSelectedValues(Object[] selectedValues) {
        return getPropertyConversionExecutor().execute(selectedValues);
    }

    /**
     * @param values
     * @return
     */
    @SuppressWarnings("unchecked")
    protected int[] determineValueIndexes(Object[] values) {
        int[] indexes = new int[values.length];
        if (values.length == 0) {
            return indexes;
        }

        Collection lookupValues = new HashSet(Arrays.asList(values));
        ListModel model = getList().getModel();
        int i = 0;
        for (int index = 0, size = model.getSize(); index < size && !lookupValues.isEmpty(); index++) {
            if (lookupValues.remove(model.getElementAt(index))) {
                indexes[i++] = index;
            }
        }
        int[] result;
        if (i != values.length) {
            result = new int[i];
            System.arraycopy(indexes, 0, result, 0, i);
        } else {
            result = indexes;
        }
        return result;
    }

    @Override
    protected void doBindControl(ListModel bindingModel) {
        CheckBoxList list = getList();
        list.setName(getFormModel().getId() + "." + getProperty());
        list.setModel(bindingModel);
        list.setBackground(UIManager.getColor("JPanel.background"));
        // JIDE CheckBoxListdifference: check box listener rather than list
        // selection
        list.getCheckBoxListSelectionModel().addListSelectionListener(checkBoxListener);
        // list.addItemListener(checkBoxListener);
        getValueModel().addValueChangeListener(valueModelListener);
        if (!isPropertyConversionExecutorAvailable() && getSelectionMode() != ListSelectionModel.SINGLE_SELECTION) {
            if (logger.isWarnEnabled()) {
                logger.warn("Selection mode for list field " + getProperty() + " forced to single selection."
                        + " If multiple selection is needed use a collection type (List, Collection, Object[])"
                        + " or provide a suitable converter to convert Object[] instances to property type "
                        + getPropertyType());
            }
            setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        }
        updateSelectedItemsFromValueModel();
    }

    @Override
    protected ListModel getDefaultModel() {
        return getList().getModel();
    }

    public CheckBoxList getList() {
        return (CheckBoxList) getComponent();
    }

    /**
     * Returns a conversion executor which converts a value of the given sourceType into the fieldType
     * 
     * @param sourceType
     *            the sourceType
     * @return true if a converter is available, otherwise false
     * 
     * @see #getPropertyType()
     */
    protected ConversionExecutor getPropertyConversionExecutor() {
        if (conversionExecutor == null) {
            conversionExecutor = getConversionService().getConversionExecutor(Object[].class, getPropertyType());
        }
        return conversionExecutor;
    }

    public ListCellRenderer getRenderer() {
        return getList().getCellRenderer();
    }

    public int getSelectionMode() {
        return getList().getSelectionMode();
    }

    protected boolean isPropertyConversionExecutorAvailable() {
        try {
            getConversionService().getConversionExecutor(Object[].class, getPropertyType());
        } catch (IllegalArgumentException e) {
            return false;
        }
        return true;
    }

    public void setRenderer(ListCellRenderer renderer) {
        getList().setCellRenderer(renderer);
    }

    public void setSelectionMode(int selectionMode) {
        Assert.isTrue(ListSelectionModel.SINGLE_SELECTION == selectionMode || isPropertyConversionExecutorAvailable());
        getList().setSelectionMode(selectionMode);
    }

    @SuppressWarnings("unchecked")
    protected void updateSelectedItemsFromSelectionModel() {
        if (getSelectionMode() == ListSelectionModel.SINGLE_SELECTION) {
            // JIDE CheckBoxList difference: get selected from CheckBoxList
            // method
            // Object singleValue = getList().getSelectedObjects()[0];
            Object singleValue = getList().getCheckBoxListSelectedValue();
            Class propertyType = getPropertyType();
            if (singleValue == null || propertyType.isAssignableFrom(singleValue.getClass())) {
                getValueModel().setValueSilently(singleValue, valueModelListener);
            } else {
                getValueModel().setValueSilently(convertValue(singleValue, propertyType), valueModelListener);
            }
        } else {
            // JIDE CheckBoxList difference: get selected from CheckBoxList
            // method
            Object[] values = getList().getCheckBoxListSelectedValues(); // getList().getSelectedObjects();
            getValueModel().setValueSilently(convertSelectedValues(values), valueModelListener);
        }
    }

    /**
     * Updates the selection model with the selected values from the value model.
     */
    protected void updateSelectedItemsFromValueModel() {
        Object value = getValue();
        Object[] selectedValues = EMPTY_VALUES;
        if (value != null) {
            selectedValues = (Object[]) convertValue(value, Object[].class);
        }

        // flag is used to avoid a round trip while we are selecting the values
        selectingValues = true;
        try {
            ListSelectionModel selectionModel = getList().getSelectionModel();
            selectionModel.setValueIsAdjusting(true);
            try {
                int[] valueIndexes = determineValueIndexes(selectedValues);
                int selectionMode = getSelectionMode();
                if (selectionMode == ListSelectionModel.SINGLE_SELECTION && valueIndexes.length > 1) {
                    getList().setCheckBoxListSelectedIndex(valueIndexes[0]);
                } else {
                    getList().setCheckBoxListSelectedIndices(valueIndexes);
                }

                // update value model if selectedValues contain elements which
                // where not found in the list model
                // elements
                if (valueIndexes.length != selectedValues.length && !isReadOnly() && isEnabled()
                        || (selectionMode == ListSelectionModel.SINGLE_SELECTION && valueIndexes.length > 1)) {
                    updateSelectedItemsFromSelectionModel();
                }
            } finally {
                selectionModel.setValueIsAdjusting(false);
            }
        } finally {
            selectingValues = false;
        }
    }
}