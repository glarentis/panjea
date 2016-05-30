package it.eurotn.rich.binding;

import it.eurotn.panjea.parametriricerca.domain.Periodo;

import java.util.Map;

import javax.swing.JComponent;
import javax.swing.JPanel;

import org.springframework.binding.form.FormModel;
import org.springframework.richclient.form.binding.Binding;
import org.springframework.richclient.form.binding.support.AbstractBinder;

public class PeriodoBinder extends AbstractBinder {

    /**
     * 
     * Costruttore.
     */
    public PeriodoBinder() {
        super(Periodo.class);
    }

    @Override
    protected JComponent createControl(@SuppressWarnings("rawtypes") Map map) {
        return new JPanel();
    }

    @Override
    protected Binding doBind(JComponent jcomponent, FormModel formModel, String formPropertyPath,
            @SuppressWarnings("rawtypes") Map map) {
        return new PeriodoBinding((JPanel) jcomponent, formModel, formPropertyPath);
    }

}
