package it.eurotn.rich.binding;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.Collection;
import java.util.Map;

import javax.swing.JComponent;
import javax.swing.JPanel;

import org.springframework.binding.form.FormModel;
import org.springframework.richclient.form.binding.Binding;
import org.springframework.richclient.form.binding.support.AbstractBinder;

public class TableBinder extends AbstractBinder {

    /**
     * Costruttore.
     */
    public TableBinder() {
        super(Collection.class);
    }

    @Override
    protected JComponent createControl(Map map) {
        JPanel panel = getComponentFactory().createPanel(new BorderLayout());
        panel.setPreferredSize(new Dimension(50, 50));
        return panel;
    }

    @Override
    protected Binding doBind(JComponent jcomponent, FormModel formmodel, String formPropertyPath, Map map) {
        return null;
    }

}
