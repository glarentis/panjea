/**
 * 
 */
package it.eurotn.rich.form.binding.swing;

import java.util.Map;

import javax.swing.*;
import javax.swing.JFormattedTextField.*;
import javax.swing.text.DefaultFormatterFactory;

import org.springframework.binding.form.FormModel;
import org.springframework.richclient.form.binding.Binding;
import org.springframework.richclient.form.binding.swing.*;
import org.springframework.util.Assert;

/**
 * *
 * 
 * @author Adry
 * @version 1.0, 24-mag-2006
 * 
 */
public class DynamicFormattedTextFieldBinder extends FormattedTextFieldBinder {

    private AbstractFormatter formatTextField = null;

    /**
     * @param requiredSourceClass
     */
    public DynamicFormattedTextFieldBinder(Class requiredSourceClass) {
        super(requiredSourceClass);
    }

    /**
     * 
     * @param requiredSourceClass
     * @param formatTextField
     */
    public DynamicFormattedTextFieldBinder(Class requiredSourceClass, AbstractFormatter formatTextField) {
        this(requiredSourceClass);
        this.formatTextField = formatTextField;
    }

    @Override
    protected JComponent createControl(Map context) {
        if (context.get(FORMATTER_FACTORY_KEY) == null) {
            return getComponentFactory().createFormattedTextField(new DefaultFormatterFactory(formatTextField));
        }
        return getComponentFactory()
                .createFormattedTextField((AbstractFormatterFactory) context.get(FORMATTER_FACTORY_KEY));

    }

    @Override
    protected Binding doBind(JComponent control, FormModel formModel, String formPropertyPath, Map context) {
        Assert.isTrue(control instanceof JFormattedTextField, "Control must be an instance of JFormattedTextField.");
        return new FormattedTextFieldBinding((JFormattedTextField) control, formModel, formPropertyPath,
                getRequiredSourceClass());
    }

}
