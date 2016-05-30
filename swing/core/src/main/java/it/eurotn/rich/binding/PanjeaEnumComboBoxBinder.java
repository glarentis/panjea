package it.eurotn.rich.binding;

import javax.swing.JComboBox;
import javax.swing.JComponent;

import org.springframework.binding.form.FormModel;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.richclient.form.binding.swing.AbstractListBinding;
import org.springframework.richclient.form.binding.swing.EnumComboBoxBinder;

public class PanjeaEnumComboBoxBinder extends EnumComboBoxBinder {

    @Override
    protected AbstractListBinding createListBinding(JComponent control, FormModel formModel, String formPropertyPath) {
        PanjeaComboBoxBinding binding = new PanjeaComboBoxBinding((JComboBox) control, formModel, formPropertyPath,
                getRequiredSourceClass());
        binding.setSelectableItems(createEnumSelectableItems(formModel, formPropertyPath));
        MessageSourceAccessor messageSourceAccessor = getMessages();
        binding.setRenderer(new EnumListRenderer(messageSourceAccessor));
        binding.setEditor(new EnumComboBoxEditor(messageSourceAccessor, binding.getEditor()));
        return binding;
    }

}
