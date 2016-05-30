package it.eurotn.rich.control.table.editor;

import java.awt.Component;

import javax.swing.CellEditor;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JTable;

import org.jdesktop.swingx.autocomplete.ComboBoxCellEditor;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.richclient.application.Application;
import org.springframework.richclient.factory.ComponentFactory;
import org.springframework.richclient.list.TextValueListRenderer;

public class EnumContextSensitiveEditorFactory extends AbstractCellEditorFactory {

    private class EnumCellEditor extends ComboBoxCellEditor {
        public class EnumListRenderer extends TextValueListRenderer {

            private static final long serialVersionUID = 3593401761377549423L;

            private MessageSourceAccessor messageSourceAccessor;

            /**
             * Costruttore.
             */
            public EnumListRenderer() {
                this.messageSourceAccessor = (MessageSourceAccessor) Application.services()
                        .getService(MessageSourceAccessor.class);
            }

            @SuppressWarnings("rawtypes")
            @Override
            protected String getTextValue(Object value) {
                if (value == null) {
                    return "Nessuno";
                }
                Enum<?> valueEnum = (Enum<?>) value;
                Class<? extends Enum> valueClass = valueEnum.getClass();
                return messageSourceAccessor.getMessage(valueClass.getName() + "." + valueEnum.name());
            }

        }

        private static final long serialVersionUID = -6469527574524217974L;

        @SuppressWarnings("rawtypes")
        private JComboBox comboBox;

        private Class<? extends Enum> classEditor;

        private boolean insertEmptyValue = false;

        /**
         * Costruttore.
         *
         * @param jComboBox
         *            combobox dell'editor
         *
         */
        @SuppressWarnings({ "unchecked", "rawtypes" })
        public EnumCellEditor(final JComboBox comboBox) {
            super(comboBox);
            this.comboBox = comboBox;
            comboBox.setRenderer(new EnumListRenderer());
        }

        /**
         *
         * @param value
         *            oggetto per recuperare la classe dell'ENUM
         */
        @SuppressWarnings({ "rawtypes", "unchecked" })
        protected void fillCombo(Object value) {
            DefaultComboBoxModel<Enum> model = new DefaultComboBoxModel<>();
            Class<? extends Enum> enumPropertyType = classEditor;
            if (enumPropertyType == null) {
                enumPropertyType = (Class<Enum>) value.getClass();
            }

            if (insertEmptyValue) {
                model.addElement(null);
            }

            for (Enum element : enumPropertyType.getEnumConstants()) {
                model.addElement(element);
            }
            this.comboBox.setModel(model);
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row,
                int column) {
            if (value != null || classEditor != null) {
                fillCombo(value);
            }
            return super.getTableCellEditorComponent(table, value, isSelected, row, column);
        }

        /**
         *
         * @param classRenderer
         *            setta la classe dell'editor
         */
        public void setClass(Class<? extends Enum> classEditor) {
            this.classEditor = classEditor;
        }

        /**
         * @param insertEmptyValue
         *            the insertEmptyValue to set
         */
        public void setInsertEmptyValue(boolean insertEmptyValue) {
            this.insertEmptyValue = insertEmptyValue;
        }
    }

    private Class<? extends Enum> classEditor;

    private boolean insertEmptyValue = false;

    /**
     * Costruttore.
     */
    public EnumContextSensitiveEditorFactory() {
        super();
    }

    /**
     * Costruttore.
     *
     * @param classEditor
     *            class editor
     * @param insertEmptyValue
     *            allow empty value
     */
    public EnumContextSensitiveEditorFactory(Class<? extends Enum> classEditor, boolean insertEmptyValue) {
        super();
        this.classEditor = classEditor;
        this.insertEmptyValue = insertEmptyValue;
    }

    @Override
    public CellEditor create() {
        ComponentFactory componentFactory = (ComponentFactory) Application.services()
                .getService(ComponentFactory.class);
        EnumCellEditor cellEditor = new EnumCellEditor(componentFactory.createComboBox());
        if (classEditor != null) {
            cellEditor.setClass(classEditor);
        }
        cellEditor.setInsertEmptyValue(insertEmptyValue);
        return cellEditor;
    }

    public void setClass(Class<? extends Enum> classEditor) {
        this.classEditor = classEditor;
    }

    /**
     * @param insertEmptyValue
     *            the insertEmptyValue to set
     */
    public void setInsertEmptyValue(boolean insertEmptyValue) {
        this.insertEmptyValue = insertEmptyValue;
    }
}
