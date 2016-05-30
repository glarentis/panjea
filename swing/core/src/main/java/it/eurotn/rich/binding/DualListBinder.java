package it.eurotn.rich.binding;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Font;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import javax.swing.AbstractButton;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.UIManager;

import org.springframework.beans.support.PropertyComparator;
import org.springframework.binding.form.FormModel;
import org.springframework.binding.value.ValueModel;
import org.springframework.binding.value.support.RefreshableValueHolder;
import org.springframework.binding.value.support.ValueHolder;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.form.binding.Binding;
import org.springframework.richclient.form.binding.support.AbstractBinder;
import org.springframework.richclient.form.binding.swing.ShuttleListBinder;
import org.springframework.richclient.form.binding.swing.ShuttleListBinding;
import org.springframework.richclient.list.BeanPropertyValueListRenderer;
import org.springframework.richclient.util.RcpSupport;
import org.springframework.util.Assert;

import com.jidesoft.list.DualList;

public class DualListBinder extends AbstractBinder {

    private static class DualListRenderer extends DefaultListCellRenderer {

        private static final long serialVersionUID = 7315281816889235059L;

        @SuppressWarnings("rawtypes")
        @Override
        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected,
                boolean cellHasFocus) {
            Component component = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            if (isSelected) {
                Font font = component.getFont();
                component.setFont(font.deriveFont(font.getStyle() | Font.BOLD));
                component.setBackground((Color) UIManager.getDefaults().get("List.selectionBackground"));
            }
            return component;
        }
    }

    private class RefreshableActionCommand extends ActionCommand {

        private RefreshableValueHolder refreshableValueHolder;

        /**
         * Costruttore.
         * 
         * @param refreshableValueHolder
         *            refreshableValueHolder
         */
        public RefreshableActionCommand(final RefreshableValueHolder refreshableValueHolder) {
            super("refreshCommand");
            RcpSupport.configure(this);
            this.refreshableValueHolder = refreshableValueHolder;
        }

        @Override
        protected void doExecuteCommand() {
            if (refreshableValueHolder instanceof RefreshableValueHolder) {
                refreshableValueHolder.refresh();
                binding.setSelectableItemsHolder(refreshableValueHolder);
            }
        }

        @Override
        protected void onButtonAttached(AbstractButton button) {
            super.onButtonAttached(button);
            button.setText("");
        }
    }

    public static final String SELECTABLE_ITEMS_HOLDER_KEY = "selectableItemsHolder";

    public static final String SELECTED_ITEMS_HOLDER_KEY = "selectedItemHolder";

    public static final String SELECTED_ITEM_TYPE_KEY = "selectedItemType";

    public static final String MODEL_KEY = "model";

    public static final String FORM_ID = "formId";

    public static final String COMPARATOR_KEY = "comparator";

    public static final String RENDERER_KEY = "renderer";

    /**
     * Utility method to construct the context map used to configure instances of {@link ShuttleListBinding} created by
     * this binder.
     * <p>
     * Binds the values specified in the collection contained within <code>selectableItemsHolder</code> to a
     * {@link org.springframework.richclient.components.ShuttleList}, with any user selection being placed in the form
     * property referred to by <code>selectionFormProperty</code>. Each item in the list will be rendered by looking up
     * a property on the item by the name contained in <code>renderedProperty</code>, retrieving the value of the
     * property, and rendering that value in the UI.
     * <p>
     * Note that the selection in the bound list will track any changes to the <code>selectionFormProperty</code>. This
     * is especially useful to preselect items in the list - if <code>selectionFormProperty</code> is not empty when the
     * list is bound, then its content will be used for the initial selection.
     * 
     * @param formModel
     *            formModel
     * @param selectionFormProperty
     *            form property to hold user's selection. This property must be a <code>Collection</code> or array type.
     * @param selectableItemsHolder
     *            <code>ValueModel</code> containing the items with which to populate the list.
     * @param renderedProperty
     *            the property to be queried for each item in the list, the result of which will be used to render that
     *            item in the UI. May be null, in which case the selectable items will be rendered as strings.
     * @return context map binding
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static Map createBindingContext(FormModel formModel, String selectionFormProperty,
            ValueModel selectableItemsHolder, String renderedProperty) {

        final Map context = new HashMap(4);

        context.put(ShuttleListBinder.FORM_ID, formModel.getId());

        final ValueModel selectionValueModel = formModel.getValueModel(selectionFormProperty);
        context.put(SELECTED_ITEMS_HOLDER_KEY, selectionValueModel);

        final Class selectionPropertyType = formModel.getFieldMetadata(selectionFormProperty).getPropertyType();
        if (selectionPropertyType != null) {
            context.put(SELECTED_ITEM_TYPE_KEY, selectionPropertyType);
        }

        context.put(SELECTABLE_ITEMS_HOLDER_KEY, selectableItemsHolder);

        if (renderedProperty != null) {
            context.put(RENDERER_KEY, new BeanPropertyValueListRenderer(renderedProperty));
            context.put(COMPARATOR_KEY, new PropertyComparator(renderedProperty, true, true));
        } else {
            context.put(RENDERER_KEY, new DualListRenderer());
        }

        return context;
    }

    private boolean showEditButton = true;
    private boolean refreshAdded = false;
    private RefreshableActionCommand refreshableActionCommand = null;
    private DualListBinding binding = null;

    /**
     * Constructor.
     */
    public DualListBinder() {
        super(null, new String[] { SELECTABLE_ITEMS_HOLDER_KEY, SELECTED_ITEMS_HOLDER_KEY, SELECTED_ITEM_TYPE_KEY,
                MODEL_KEY, COMPARATOR_KEY, RENDERER_KEY, FORM_ID });
    }

    /**
     * Constructor allowing the specification of additional/alternate context keys. This is for use by derived classes.
     * 
     * @param supportedContextKeys
     *            Context keys supported by subclass
     */
    protected DualListBinder(final String[] supportedContextKeys) {
        super(null, supportedContextKeys);
    }

    /**
     * Apply the values from the context to the specified binding.
     * 
     * @param bindingParam
     *            Binding to update
     * @param context
     *            Map of context values to apply to the binding
     */
    @SuppressWarnings("rawtypes")
    protected void applyContext(DualListBinding bindingParam, Map context) {
        if (context.containsKey(SELECTABLE_ITEMS_HOLDER_KEY)) {
            bindingParam.setSelectableItemsHolder((ValueModel) context.get(SELECTABLE_ITEMS_HOLDER_KEY));
        }
        if (context.containsKey(SELECTED_ITEMS_HOLDER_KEY)) {
            bindingParam.setSelectedItemsHolder((ValueModel) context.get(SELECTED_ITEMS_HOLDER_KEY));
        }
        if (context.containsKey(RENDERER_KEY)) {
            bindingParam.setRenderer((ListCellRenderer) context.get(RENDERER_KEY));
        }
        if (context.containsKey(COMPARATOR_KEY)) {
            bindingParam.setComparator((Comparator) context.get(COMPARATOR_KEY));
        }
        if (context.containsKey(SELECTED_ITEM_TYPE_KEY)) {
            bindingParam.setSelectedItemType((Class) context.get(SELECTED_ITEM_TYPE_KEY));
        }
        if (context.containsKey(FORM_ID)) {
            bindingParam.setFormId((String) context.get(FORM_ID));
        }
    }

    @SuppressWarnings("rawtypes")
    @Override
    protected JComponent createControl(Map context) {
        final ValueHolder valueHolder = (ValueHolder) context.get(SELECTABLE_ITEMS_HOLDER_KEY);
        if (valueHolder instanceof RefreshableValueHolder) {
            refreshableActionCommand = new RefreshableActionCommand((RefreshableValueHolder) valueHolder);
        }
        refreshAdded = false;

        DualList shuttleList = new DualList() {

            private static final long serialVersionUID = 4759849320625044084L;

            @Override
            protected Container createButtonPanel() {
                Container buttonPanel = super.createButtonPanel();
                // Ho dovuto aggiungere una variabile per vedere se ho già aggiunto il command di refresh in modo da
                // aggungerlo solo al pannello di commands della lista a sinistra. Il metodo createButtonPanel viene
                // chiamato per entrambi i pannelli commands delle liste, ma non ho l'accesso differenziato al pannello
                // sinistro e destro.
                if (refreshableActionCommand != null && !refreshAdded) {
                    AbstractButton button = refreshableActionCommand.createButton();
                    button.setName(refreshableActionCommand.getActionCommand());
                    button.setCursor(Cursor.getDefaultCursor());
                    buttonPanel.add(button);
                    refreshAdded = true;
                }
                return buttonPanel;
            }
        };

        return shuttleList;
    }

    @SuppressWarnings("rawtypes")
    @Override
    protected Binding doBind(JComponent control, FormModel formModel, String formPropertyPath, Map context) {
        Assert.isTrue(control instanceof DualList, formPropertyPath);
        binding = new DualListBinding((DualList) control, formModel, formPropertyPath);
        applyContext(binding, context);
        return binding;
    }

    /**
     * @param showEditButton
     *            the showEditButton to set
     */
    public void setShowEditButton(boolean showEditButton) {
        this.showEditButton = showEditButton;
    }

}
