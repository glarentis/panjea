package it.eurotn.rich.binding;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JComponent;
import javax.swing.JTextField;

import org.springframework.richclient.form.builder.FormLayoutFormBuilder;

import com.jgoodies.forms.layout.FormLayout;

import it.eurotn.panjea.parametriricerca.domain.Periodo;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModel;
import it.eurotn.rich.form.PanjeaFormModelHelper;
import it.eurotn.util.PanjeaEJBUtil;

public class PeriodoForm extends PanjeaAbstractForm implements PropertyChangeListener {

    private JComponent dataInizialeComponent;
    private JComponent dataFinaleComponent;
    private JComponent numeroGiorniComponent;

    private PropertyChangeListener periodoChangeListerner;

    /**
     * Costruttore.
     */
    public PeriodoForm() {
        super(PanjeaFormModelHelper.createFormModel(new Periodo(), false, "PeriodoForm"), "PeriodoForm");
    }

    @Override
    protected JComponent createFormControl() {
        final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
        FormLayout layout = new FormLayout("right:pref,4dlu,left:pref,4dlu,left:pref", "default");
        FormLayoutFormBuilder builder = new FormLayoutFormBuilder(bf, layout);
        builder.setLabelAttributes("r, c");
        builder.addProperty("tipoPeriodo");
        dataInizialeComponent = builder.addProperty("dataIniziale", 3);
        dataFinaleComponent = builder.addProperty("dataFinale", 5);
        numeroGiorniComponent = builder.addProperty("numeroGiorni", 3);
        ((JTextField) numeroGiorniComponent).setColumns(5);
        Periodo periodo = (Periodo) getFormObject();
        dataInizialeComponent.setVisible(periodo.getStrategy().isAllowedSelectDate());
        dataFinaleComponent.setVisible(periodo.getStrategy().isAllowedSelectDate());
        numeroGiorniComponent.setVisible(periodo.getStrategy().isAllowedNumGiorni());

        getValueModel("tipoPeriodo").addValueChangeListener(this);
        getValueModel("dataIniziale").addValueChangeListener(this);
        getValueModel("dataFinale").addValueChangeListener(this);
        getValueModel("numeroGiorni").addValueChangeListener(this);
        return builder.getPanel();
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (((PanjeaFormModel) getFormModel()).isAdjustingMode()) {
            return;
        }

        ((PanjeaFormModel) getFormModel()).setAdjustingMode(true);
        commit();
        Periodo periodo = (Periodo) getFormObject();
        dataInizialeComponent.setVisible(periodo.getStrategy().isAllowedSelectDate());
        dataFinaleComponent.setVisible(periodo.getStrategy().isAllowedSelectDate());
        numeroGiorniComponent.setVisible(periodo.getStrategy().isAllowedNumGiorni());

        if (periodoChangeListerner != null) {
            periodoChangeListerner
                    .propertyChange(new PropertyChangeEvent(this, "periodo", null, PanjeaEJBUtil.cloneObject(periodo)));
        }
        setFormObject(PanjeaEJBUtil.cloneObject(periodo));
        // getFormModel().getValueModel("tipoPeriodo").setValueSilently(periodo.getTipoPeriodo(), this);
        // getFormModel().getValueModel("numeroGiorni").setValueSilently(periodo.getNumeroGiorni(), this);
        // getFormModel().getValueModel("dataIniziale").setValueSilently(periodo.getDataIniziale(), this);
        // getFormModel().getValueModel("dataFinale").setValueSilently(periodo.getDataFinale(), this);
        ((PanjeaFormModel) getFormModel()).setAdjustingMode(false);
    }

    @Override
    public void setFormObject(Object formObject) {
        super.setFormObject(formObject);
    }

    /**
     * @param periodoChangeListerner
     *            the periodoChangeListerner to set
     */
    public void setPeriodoChangeListerner(PropertyChangeListener periodoChangeListerner) {
        this.periodoChangeListerner = periodoChangeListerner;
    }

}