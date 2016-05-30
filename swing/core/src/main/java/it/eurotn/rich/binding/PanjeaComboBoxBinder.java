package it.eurotn.rich.binding;

import javax.swing.JComboBox;
import javax.swing.JComponent;

import org.springframework.binding.form.FormModel;
import org.springframework.richclient.form.binding.swing.AbstractListBinding;
import org.springframework.util.Assert;

public class PanjeaComboBoxBinder extends org.springframework.richclient.form.binding.swing.ComboBoxBinder {

    @Override
    protected AbstractListBinding createListBinding(JComponent control, FormModel formModel, String formPropertyPath) {
        Assert.isInstanceOf(JComboBox.class, control, formPropertyPath);
        return new PanjeaComboBoxBinding((JComboBox) control, formModel, formPropertyPath, getRequiredSourceClass());
    }
}
