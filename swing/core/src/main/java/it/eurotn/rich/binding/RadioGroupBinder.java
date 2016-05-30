package it.eurotn.rich.binding;

import java.awt.FlowLayout;
import java.util.Map;

import javax.swing.JComponent;
import javax.swing.JPanel;

import org.springframework.binding.form.FormModel;
import org.springframework.richclient.form.binding.Binding;
import org.springframework.richclient.form.binding.support.AbstractBinder;

public class RadioGroupBinder extends AbstractBinder {

    public static final String SELECTABLE_ITEMS_KEY = "selectableItems";

    public static final String DISPOSIZIONE = "disposizione";

    public RadioGroupBinder() {
        super(String.class, new String[] { "selectableItems", "disposizione" });
    }

    @Override
    protected JComponent createControl(Map map) {
        return new JPanel(new FlowLayout());
    }

    @Override
    protected Binding doBind(JComponent jcomponent, FormModel formmodel, String s, Map map) {
        return new RadioGroupBinding((JPanel) jcomponent, formmodel, s);
    }
}
