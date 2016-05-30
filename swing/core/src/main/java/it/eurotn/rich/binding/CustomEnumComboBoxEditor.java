/**
 * 
 */
package it.eurotn.rich.binding;

import java.awt.Component;
import java.awt.event.ActionListener;

import javax.swing.ComboBoxEditor;

import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.util.Assert;

/**
 * @author Leonardo
 */
public class CustomEnumComboBoxEditor implements ComboBoxEditor {

    private Object current;

    private MessageSourceAccessor messageSourceAccessor;

    private ComboBoxEditor inner;

    public CustomEnumComboBoxEditor(MessageSourceAccessor messageSourceAccessor, ComboBoxEditor editor) {
        Assert.notNull(editor, "Editor cannot be null");
        this.inner = editor;
        this.messageSourceAccessor = messageSourceAccessor;
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
            Enum valueEnum = (Enum) value;
            Class<? extends Enum> valueClass = valueEnum.getClass();
            inner.setItem(messageSourceAccessor.getMessage(valueClass.getName() + "." + valueEnum.name()));
        } else {
            inner.setItem(null);
        }
    }

}
