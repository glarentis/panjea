package it.eurotn.rich.binding;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import org.springframework.binding.form.FormModel;
import org.springframework.binding.value.ValueModel;
import org.springframework.binding.value.support.RefreshableValueHolder;
import org.springframework.richclient.form.binding.support.CustomBinding;

public class RadioGroupBinding extends CustomBinding {

    JPanel pannello;
    ValueModel listItems;

    protected RadioGroupBinding(JPanel pannello, FormModel formModel, String formPropertyPath) {
        super(formModel, formPropertyPath, String.class);
        this.pannello = pannello;
    }

    @Override
    protected JComponent doBindControl() {
        List items = (List) listItems.getValue();
        pannello.removeAll();
        for (int i = 0; i < items.size(); i++) {
            JRadioButton button = getComponentFactory().createRadioButton((String) items.get(i));
            button.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent actionevent) {

                }
            });
            pannello.add(button);
        }
        return null;
    }

    @Override
    protected void enabledChanged() {

    }

    public ValueModel getListItems() {
        return listItems;
    }

    @Override
    protected void readOnlyChanged() {

    }

    public void setListItems(ValueModel listItems) {
        this.listItems = listItems;
    }

    @Override
    protected void valueModelChanged(Object obj) {
        if (listItems instanceof RefreshableValueHolder) {
            ((RefreshableValueHolder) listItems).refresh();
        }
        doBindControl();
    }

}
