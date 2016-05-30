package it.eurotn.rich.binding;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;

import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.ListCellRenderer;
import javax.swing.ListModel;
import javax.swing.UIManager;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.springframework.binding.form.FormModel;
import org.springframework.binding.value.ValueModel;
import org.springframework.binding.value.support.BufferedCollectionValueModel;
import org.springframework.context.MessageSource;
import org.springframework.richclient.application.ApplicationServicesLocator;
import org.springframework.richclient.form.binding.support.AbstractBinding;
import org.springframework.richclient.image.IconSource;

import com.jidesoft.list.AbstractDualListModel;
import com.jidesoft.list.DualList;
import com.jidesoft.list.DualListModel;

public class DualListBinding extends AbstractBinding {

    private class DualModelList extends AbstractDualListModel {

        private static final long serialVersionUID = 1719630128326550828L;

        private ArrayList<Object> content;

        /**
         * The default constructor.
         * 
         * @param content
         *            the content to set
         */
        public DualModelList(final Collection<?> content) {
            super();
            this.content = new ArrayList<Object>(content);
        }

        @Override
        public Object getElementAt(int index) {
            return content.get(index);
        }

        @Override
        public int getSize() {
            return content.size();
        }

        /**
         * @param content
         *            the content to set
         */
        public void setContent(Collection<Object> content) {
            this.content = new ArrayList<Object>(content);
        }

    }

    private class ListSelectedItemsHolderChangeListener implements PropertyChangeListener {
        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            setSelectedValue(this);
        }
    }

    /**
     * Inner class to mediate the list selection events into calls to {@link #updateSelectionHolderFromList}.
     */
    private class ListSelectedValueMediator implements ListSelectionListener {

        private final PropertyChangeListener valueChangeHandler;

        /**
         * Default constructor.
         */
        public ListSelectedValueMediator() {
            valueChangeHandler = getListSelectedItemsHolderChangeListener();
            selectedItemsHolder.addValueChangeListener(valueChangeHandler);
        }

        @Override
        public void valueChanged(ListSelectionEvent e) {
            if (!e.getValueIsAdjusting()) {
                updateSelectionHolderFromList(valueChangeHandler);
            }
        }
    }

    private static final String[] commands = new String[] { DualList.COMMAND_MOVE_RIGHT, DualList.COMMAND_MOVE_LEFT,
            DualList.COMMAND_MOVE_ALL_RIGHT, DualList.COMMAND_MOVE_ALL_LEFT, DualList.COMMAND_MOVE_UP,
            DualList.COMMAND_MOVE_DOWN, DualList.COMMAND_MOVE_TO_TOP, DualList.COMMAND_MOVE_TO_BOTTOM, };

    private DualList list = null;

    private DualModelList model = null;

    private ValueModel selectedItemsHolder = null;

    private ValueModel selectableItemsHolder = null;

    private Comparator comparator = null;

    private Class selectedItemType = null;

    private Class concreteSelectedType = null;

    private String formId = null;

    private ListSelectedValueMediator listSelectedValueMediator = null;

    private ListSelectedItemsHolderChangeListener listSelectedItemsHolderChangeListener;

    /**
     * Construct a binding.
     * 
     * @param list
     *            ShuttleList to bind
     * @param formModel
     *            The form model holding the bound property
     * @param formPropertyPath
     *            Path to the property to bind
     */
    public DualListBinding(final DualList list, final FormModel formModel, final String formPropertyPath) {
        super(formModel, formPropertyPath, null);
        this.list = list;
    }

    /**
     * Compare two collections for equality using the configured comparator. Element order must be the same for the
     * collections to compare equal.
     * 
     * @param a1
     *            First collection to compare
     * @param a2
     *            Second collection to compare
     * @return boolean true if they are equal
     */
    protected boolean collectionsEqual(Collection a1, Collection a2) {
        if (a1 != null && a2 != null && a1.size() == a2.size()) {
            // Loop over each element and compare them using our comparator
            Iterator iterA1 = a1.iterator();
            Iterator iterA2 = a2.iterator();
            while (iterA1.hasNext()) {
                if (!equalByComparator(iterA1.next(), iterA2.next())) {
                    return false;
                }
            }
        } else if (a1 == null && a2 == null) {
            return true;
        }
        return false;
    }

    /**
     * @return the created model
     */
    private DualListModel createModel() {
        if (model != null) {
            return model;
        }
        if (selectableItemsHolder != null) {
            model = new DualModelList((Collection<?>) selectableItemsHolder.getValue());
        } else {
            model = new DualModelList(new ArrayList<Object>());
        }
        return model;
    }

    @Override
    protected JComponent doBindControl() {
        list.setModel(createModel());
        if (selectedItemsHolder != null) {
            setSelectedValue(null);
            model.addListSelectionListener(getListSelectedValueMediator());
        }
        return list;
    }

    @Override
    protected void enabledChanged() {
        list.setEnabled(isEnabled() && !isReadOnly());
    }

    /**
     * Using the configured comparator (or equals if not configured), determine if two objects are equal.
     * 
     * @param o1
     *            Object to compare
     * @param o2
     *            Object to compare
     * @return boolean true if objects are equal
     */
    private boolean equalByComparator(Object o1, Object o2) {
        return comparator == null ? o1.equals(o2) : comparator.compare(o1, o2) == 0;
    }

    /**
     * @return the concrete selected type
     */
    protected Class getConcreteSelectedType() {
        if (concreteSelectedType == null) {
            if (isSelectedItemACollection()) {
                concreteSelectedType = BufferedCollectionValueModel.getConcreteCollectionType(getSelectedItemType());
            } else if (isSelectedItemAnArray()) {
                concreteSelectedType = getSelectedItemType().getComponentType();
            }
        }
        return concreteSelectedType;
    }

    /**
     * Using the application services, check for: formId.shuttleList.edit if not there shuttleList.edit.
     * 
     * @return an Icon
     */
    private Icon getEditIcon() {
        final IconSource iconSource = (IconSource) ApplicationServicesLocator.services().getService(IconSource.class);
        // @TODO find the form Id.
        Icon icon = null;

        if (getFormId() != null) {
            icon = iconSource.getIcon(getFormId() + ".shuttleList.edit");
        }

        if (icon == null) {
            icon = iconSource.getIcon("shuttleList.edit");
        }
        return icon;
    }

    /**
     * Look for Edit Text by searching the ApplicationServices for: formId.shuttleList.editText if nothing try
     * shuttleList.editText.
     * 
     * @return string
     */
    private String getEditIconText() {
        return getMsgText("shuttleList.editText", "Edit...");
    }

    /**
     * @return Returns the formId.
     */
    protected String getFormId() {
        return formId;
    }

    /**
     * @return listSelectedItemsHolderChangeListener
     */
    private ListSelectedItemsHolderChangeListener getListSelectedItemsHolderChangeListener() {
        if (listSelectedItemsHolderChangeListener == null) {
            listSelectedItemsHolderChangeListener = new ListSelectedItemsHolderChangeListener();
        }
        return listSelectedItemsHolderChangeListener;
    }

    /**
     * @return ListSelectedValueMediator
     */
    private ListSelectedValueMediator getListSelectedValueMediator() {
        if (listSelectedValueMediator == null) {
            listSelectedValueMediator = new ListSelectedValueMediator();
        }
        return listSelectedValueMediator;
    }

    /**
     * MsgText to get.
     * 
     * @param key
     *            key
     * @param defaultMsg
     *            defaultMsg
     * @return MsgText
     */
    private String getMsgText(String key, String defaultMsg) {
        final MessageSource messageSource = (MessageSource) ApplicationServicesLocator.services()
                .getService(MessageSource.class);
        String text = null;

        if (getFormId() != null) {
            if (getProperty() != null) {
                text = messageSource.getMessage(getFormId() + "." + getProperty() + "." + key, null, null, null);
            }

            if (text == null) {
                text = messageSource.getMessage(getFormId() + "." + key, null, null, null);
            }
        }

        if (text == null) {
            text = messageSource.getMessage(key, null, defaultMsg, null);
        }

        return text;
    }

    /**
     * @return ListCellRenderer
     */
    public ListCellRenderer getRenderer() {
        return list.getCellRenderer();
    }

    /**
     * @return SelectedItemType
     */
    protected Class getSelectedItemType() {
        if (this.selectedItemType == null && this.selectedItemsHolder != null
                && this.selectedItemsHolder.getValue() != null) {
            setSelectedItemType(this.selectedItemsHolder.getValue().getClass());
        }

        return this.selectedItemType;
    }

    /**
     * @return SourceLabel
     */
    private String getSourceLabel() {
        return getMsgText("shuttleList.sourceList.label", null);
    }

    /**
     * IndexOf.
     * 
     * @param o
     *            o
     * @return int
     */
    protected int indexOf(final Object o) {
        final ListModel listModel = list.getModel();
        final int size = listModel.getSize();
        for (int i = 0; i < size; i++) {
            if (equalByComparator(o, listModel.getElementAt(i))) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Return an array of indices in the selectableItems for each element in the provided set. The set can be either a
     * Collection or an Array.
     * 
     * @param itemSet
     *            Either an array or a Collection of items
     * @return array of indices of the elements in itemSet within the selectableItems
     */
    protected int[] indicesOf(final Object itemSet) {
        int[] ret = null;

        if (itemSet instanceof Collection) {
            Collection collection = (Collection) itemSet;
            ret = new int[collection.size()];
            int i = 0;
            for (Iterator iter = collection.iterator(); iter.hasNext(); i++) {
                ret[i] = indexOf(iter.next());
            }
        } else if (itemSet == null) {
            ret = new int[0];
        } else if (itemSet.getClass().isArray()) {
            Object[] items = (Object[]) itemSet;
            ret = new int[items.length];
            for (int i = 0; i < items.length; i++) {
                ret[i] = indexOf(items[i]);
            }
        } else {
            throw new IllegalArgumentException("itemSet must be eithe an Array or a Collection");
        }

        return ret;
    }

    /**
     * @return selectedItemACollection
     */
    protected boolean isSelectedItemACollection() {
        return getSelectedItemType() != null && Collection.class.isAssignableFrom(getSelectedItemType());
    }

    /**
     * Determine if the selected item type can be multi-valued (is a collection or an array.
     * 
     * @return boolean <code>true</code> if the <code>selectedItemType</code> is a Collection or an Array.
     */
    protected boolean isSelectedItemAnArray() {
        Class itemType = getSelectedItemType();
        return itemType != null && itemType.isArray();
    }

    @Override
    protected void readOnlyChanged() {
        list.setEnabled(isEnabled() && !isReadOnly());
        list.getOriginalListPane().setVisible(isEnabled() && !isReadOnly());
        for (final String command : commands) {
            list.setButtonVisible(command, isEnabled() && !isReadOnly());
        }
        if (list.isEnabled()) {
            list.getSelectedList().setBackground(list.getOriginalList().getBackground());
        } else {
            list.getSelectedList().setBackground(UIManager.getColor("ComboBox.disabledBackground"));
        }
    }

    /**
     * @param comparator
     *            comparator
     */
    public void setComparator(Comparator comparator) {
        this.comparator = comparator;
    }

    /**
     * @param formId
     *            The formId to set.
     */
    protected void setFormId(String formId) {
        this.formId = formId;
    }

    /**
     * @param model
     *            the model to set
     */
    public void setModel(DualModelList model) {
        this.model = model;
    }

    /**
     * @param renderer
     *            the renderer to set
     */
    public void setRenderer(ListCellRenderer renderer) {
        list.setCellRenderer(renderer);
        list.setSelectedCellRenderer(renderer);
    }

    /**
     * @param selectableItemsHolder
     *            selectableItemsHolder
     */
    @SuppressWarnings("unchecked")
    public void setSelectableItemsHolder(ValueModel selectableItemsHolder) {
        this.selectableItemsHolder = selectableItemsHolder;
        if (model != null) {
            model.setContent((Collection<Object>) selectableItemsHolder.getValue());
            if (selectedItemsHolder != null) {
                setSelectedValue(getListSelectedItemsHolderChangeListener());
            }
        }
    }

    /**
     * @param selectedItemsHolder
     *            selectedItemsHolder
     */
    public void setSelectedItemsHolder(ValueModel selectedItemsHolder) {
        this.selectedItemsHolder = selectedItemsHolder;
    }

    /**
     * @param selectedItemType
     *            selectedItemType
     */
    public void setSelectedItemType(final Class selectedItemType) {
        this.selectedItemType = selectedItemType;
    }

    /**
     * Set selected value.
     * 
     * @param silentValueChangeHandler
     *            silentValueChangeHandler
     */
    protected void setSelectedValue(final PropertyChangeListener silentValueChangeHandler) {
        final int[] indices = indicesOf(selectedItemsHolder.getValue());
        list.clearSelection();
        for (int i : indices) {
            model.addSelectionInterval(i, i);
        }
        // The selection may now be different than what is reflected in
        // collection property if this is SINGLE_INTERVAL_SELECTION, so
        // modify if needed...
        updateSelectionHolderFromList(silentValueChangeHandler);
    }

    /**
     * Update Selection Holder From List.
     * 
     * @param silentValueChangeHandler
     *            silentValueChangeHandler
     */
    protected void updateSelectionHolderFromList(final PropertyChangeListener silentValueChangeHandler) {
        final Object[] selected = list.getSelectedValues();

        try {
            // In order to properly handle buffered forms, we will
            // create a new collection to hold the new selection.
            final Collection newSelection = (Collection) getConcreteSelectedType().newInstance();
            if (selected != null && selected.length > 0) {
                for (int i = 0; i < selected.length; i++) {
                    newSelection.add(selected[i]);
                }
            }

            // Only modify the selectedItemsHolder if the selection is
            // actually changed.
            final Collection oldSelection = (Collection) selectedItemsHolder.getValue();
            if (oldSelection == null || oldSelection.size() != newSelection.size()
                    || !collectionsEqual(oldSelection, newSelection)) {
                if (silentValueChangeHandler != null) {
                    selectedItemsHolder.setValueSilently(newSelection, silentValueChangeHandler);
                } else {
                    selectedItemsHolder.setValue(newSelection);
                }
            }
        } catch (InstantiationException e1) {
            throw new RuntimeException("Unable to instantiate new concrete collection class for new selection.", e1);
        } catch (IllegalAccessException e1) {
            throw new RuntimeException(e1);
        }
    }
}
