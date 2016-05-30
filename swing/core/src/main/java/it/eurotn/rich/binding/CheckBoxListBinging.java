package it.eurotn.rich.binding;

import javax.swing.JList;
import javax.swing.ListSelectionModel;

import org.springframework.binding.form.FormModel;
import org.springframework.richclient.form.binding.swing.ListBinding;

import com.jidesoft.swing.CheckBoxList;

public class CheckBoxListBinging extends ListBinding {

    private static final Object[] EMPTY_VALUES = new Object[0];

    boolean selectingValues;

    public CheckBoxListBinging(JList list, FormModel formModel, String formFieldPath, Class requiredSourceClass) {
        super(list, formModel, formFieldPath, requiredSourceClass);
    }

    @Override
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
                    ((CheckBoxList) getList()).addCheckBoxListSelectedIndex(valueIndexes[0]);
                } else {
                    for (int i : valueIndexes) {
                        ((CheckBoxList) getList()).addCheckBoxListSelectedIndex(valueIndexes[i]);
                    }
                    getList().setSelectedIndices(valueIndexes);
                }
                // update value model if selectedValues contain elements which where not found in the list model
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
