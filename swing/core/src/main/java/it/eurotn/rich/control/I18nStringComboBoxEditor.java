/**
 * 
 */
package it.eurotn.rich.control;

import java.awt.Component;
import java.awt.event.ActionListener;
import java.util.Locale;

import javax.swing.ComboBoxEditor;

import org.springframework.context.MessageSource;
import org.springframework.util.Assert;

/**
 * @author fattazzo
 * 
 */
public class I18nStringComboBoxEditor implements ComboBoxEditor {

    private Object current;

    private final MessageSource messageSource;

    private final ComboBoxEditor inner;

    public I18nStringComboBoxEditor(MessageSource messageSource, ComboBoxEditor editor) {
        Assert.notNull(editor, "Editor cannot be null");
        this.inner = editor;
        this.messageSource = messageSource;
    }

    @Override
    public void addActionListener(ActionListener l) {
        inner.addActionListener(l);
    }

    @Override
    public Component getEditorComponent() {
        return inner.getEditorComponent();
    }

    @Override
    public Object getItem() {
        return current;
    }

    @Override
    public void removeActionListener(ActionListener l) {
        inner.removeActionListener(l);
    }

    @Override
    public void selectAll() {
        inner.selectAll();
    }

    @Override
    @SuppressWarnings("unchecked")
    public void setItem(Object value) {
        current = value;
        if (value != null) {
            inner.setItem(messageSource.getMessage((String) value, new Object[] {}, Locale.getDefault()));
        } else {
            inner.setItem(null);
        }
    }

}
