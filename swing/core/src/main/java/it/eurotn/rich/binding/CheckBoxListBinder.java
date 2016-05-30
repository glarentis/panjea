package it.eurotn.rich.binding;

import it.eurotn.panjea.rich.factory.PanjeaComponentFactory;

import java.util.Map;

import javax.swing.JComponent;
import javax.swing.JList;

import org.springframework.binding.form.FormModel;
import org.springframework.richclient.form.binding.swing.AbstractListBinding;
import org.springframework.richclient.form.binding.swing.ListBinder;

/**
 * Crea una lista dove è possibile selezionare gli elementi tramite checkbox
 * 
 * @author giangi
 * 
 */
public class CheckBoxListBinder extends ListBinder {

    public CheckBoxListBinder() {
        this(null, new String[] { "selectableItems", "comparator", "renderer", "filter", "selectionMode" });
    }

    public CheckBoxListBinder(Class requiredSourceClass, String[] supportedContextKeys) {
        super(requiredSourceClass, supportedContextKeys);
    }

    public CheckBoxListBinder(String[] supportedContextKeys) {
        this(null, supportedContextKeys);
    }

    @Override
    protected JComponent createControl(Map context) {
        return ((PanjeaComponentFactory) getComponentFactory()).createCheckBoxList();
    }

    @Override
    protected AbstractListBinding createListBinding(JComponent control, FormModel formModel, String formPropertyPath) {

        return new CheckBoxListBinging((JList) control, formModel, formPropertyPath, getRequiredSourceClass());
    }
}
