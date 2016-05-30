package it.eurotn.rich.list;

import java.awt.Component;
import java.awt.event.ActionListener;
import java.util.Locale;

import javax.swing.ComboBoxEditor;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.context.MessageSource;
import org.springframework.richclient.application.Application;

public class I18NBeanPropertyValueComboBoxEditor implements ComboBoxEditor {

    private final BeanWrapper beanWrapper = new BeanWrapperImpl();

    private Object current;

    private final ComboBoxEditor innerEditor;

    private final String renderedProperty;

    private final MessageSource messageSource;

    /**
     * Constructs a new <code>BeanPropertyValueComboBoxEditor</code> instance. The <code>toString</code> method is used
     * to render the items.
     *
     * @param editor
     *            the <code>ComboBoxEditor</code> to use internally
     */
    public I18NBeanPropertyValueComboBoxEditor(final ComboBoxEditor editor) {
        this(editor, null);
    }

    /**
     * Constructs a new <code>BeanPropertyValueComboBoxEditor</code> instance.
     *
     * @param innerEditor
     *            the <code>ComboBoxEditor</code> which is used to render the value of the property
     * @param renderedProperty
     *            the property used to render the items
     */
    public I18NBeanPropertyValueComboBoxEditor(final ComboBoxEditor innerEditor, final String renderedProperty) {
        this.innerEditor = innerEditor;
        this.renderedProperty = renderedProperty;
        messageSource = (MessageSource) Application.services().getService(MessageSource.class);
    }

    /**
     * Should only be used if the innerEditor will be set later.
     *
     * @param renderedProperty
     *            the property used to render the items
     */
    public I18NBeanPropertyValueComboBoxEditor(final String renderedProperty) {
        this(null, renderedProperty);
    }

    @Override
    public void addActionListener(ActionListener listener) {
        innerEditor.addActionListener(listener);
    }

    @Override
    public Component getEditorComponent() {
        return innerEditor.getEditorComponent();
    }

    @Override
    public Object getItem() {
        return current;
    }

    /**
     * @return the property name
     */
    public String getPropertyName() {
        return this.renderedProperty;
    }

    @Override
    public void removeActionListener(ActionListener listener) {
        innerEditor.removeActionListener(listener);
    }

    @Override
    public void selectAll() {
        innerEditor.selectAll();
    }

    @Override
    public void setItem(Object item) {
        current = item;
        if (item == null) {
            innerEditor.setItem("");
        } else {
            beanWrapper.setWrappedInstance(item);
            if (renderedProperty != null) {
                String value = messageSource.getMessage(String.valueOf(beanWrapper.getPropertyValue(getPropertyName())),
                        new Object[] {}, Locale.getDefault());
                innerEditor.setItem(String.valueOf(value));
            } else {
                innerEditor.setItem(String.valueOf(item));
            }
        }
    }
}
