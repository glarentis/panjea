package it.eurotn.rich.binding;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JComponent;
import javax.swing.JPanel;

import org.springframework.binding.form.FormModel;
import org.springframework.binding.form.ValidatingFormModel;
import org.springframework.richclient.form.binding.support.CustomBinding;

import it.eurotn.panjea.parametriricerca.domain.Periodo;

public class PeriodoBinding extends CustomBinding {

    private ValidatingFormModel parentFormModel;
    // private ValidatingFormModel periodoFormModel;
    private PeriodoForm form;

    /**
     * @param rootPanel
     *            pannello dove inserire i componentih
     * @param formModel
     *            bindato
     * @param formPropertyPath
     *            legato al formmodel
     */
    public PeriodoBinding(final JPanel rootPanel, final FormModel formModel, final String formPropertyPath) {
        super(formModel, formPropertyPath, Periodo.class);
        parentFormModel = (ValidatingFormModel) formModel;
    }

    @Override
    protected JComponent doBindControl() {
        form = new PeriodoForm();
        form.setPeriodoChangeListerner(new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                controlValueChanged(evt.getNewValue());
            }
        });
        return form.getControl();
    }

    @Override
    protected void enabledChanged() {
    }

    @Override
    protected void readOnlyChanged() {
    }

    @Override
    protected void valueModelChanged(Object obj) {
        form.setFormObject(obj);
    }

}
