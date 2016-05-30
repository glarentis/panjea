package it.eurotn.rich.binding;

import java.awt.BorderLayout;
import java.util.Map;

import javax.swing.*;

import org.springframework.binding.form.FormModel;
import org.springframework.richclient.form.binding.Binding;
import org.springframework.richclient.form.binding.support.AbstractBinder;

public class CodiceFiscaleBinder extends AbstractBinder {

    public static final String NOME_KEY = "nome";
    public static final String COGNOME_KEY = "cognome";
    public static final String DATA_NASCITA_KEY = "data_nascita";
    public static final String COMUNE_KEY = "comune";
    public static final String SESSO_KEY = "sesso";

    public CodiceFiscaleBinder() {
        super(String.class, new String[] { NOME_KEY, COGNOME_KEY, DATA_NASCITA_KEY, COMUNE_KEY, SESSO_KEY });
    }

    @Override
    protected JComponent createControl(Map context) {
        JPanel panel = getComponentFactory().createPanel(new BorderLayout());
        panel.add(getComponentFactory().createTextField(), BorderLayout.CENTER);
        JButton button = getComponentFactory().createButton("");
        button.setFocusable(false);
        panel.add(button, BorderLayout.EAST);
        return panel;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected Binding doBind(JComponent control, FormModel formModel, String formPropertyPath, Map context) {
        org.springframework.util.Assert.isTrue(control instanceof JPanel, "Control must be an instance of JPanel.");
        return new CodiceFiscaleBinding((JPanel) control, formModel, formPropertyPath, context);
    }

}
