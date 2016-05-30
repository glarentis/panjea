package it.eurotn.panjea.vending.rich.editors.sistemielettronici;

import javax.swing.JComponent;

import com.jgoodies.forms.layout.FormLayout;

import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.panjea.vending.domain.SistemaElettronico;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormLayoutBuilder;
import it.eurotn.rich.form.PanjeaFormModelHelper;

public class SistemaElettronicoForm extends PanjeaAbstractForm {

    private static final String FORM_ID = "sistemaElettronicoForm";

    /**
     * Costruttore.
     */
    public SistemaElettronicoForm() {
        super(PanjeaFormModelHelper.createFormModel(new SistemaElettronico(), false, FORM_ID), FORM_ID);
    }

    @Override
    protected JComponent createFormControl() {
        final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
        FormLayout layout = new FormLayout(
                "right:pref,4dlu,fill:60dlu,fill:20dlu,10dlu,right:pref,4dlu,fill:60dlu,fill:60dlu", "4dlu,default");
        PanjeaFormLayoutBuilder builder = new PanjeaFormLayoutBuilder(bf, layout);
        builder.setLabelAttributes("r, c");
        builder.setRow(2);
        builder.addPropertyAndLabel("codice");
        builder.addPropertyAndLabel("descrizione", 6, 2, 1);
        builder.nextRow();
        builder.addPropertyAndLabel("tipo", 1, 2, 1);
        builder.nextRow();
        builder.addPropertyAndLabel("cassetta");
        builder.addPropertyAndLabel("resto", 6);
        builder.nextRow();
        builder.addPropertyAndLabel("chiave");
        builder.addPropertyAndLabel("caricaChiave", 6);
        builder.nextRow();
        builder.addPropertyAndLabel("tipoComunicazione", 1, 2, 1);
        builder.nextRow();
        builder.addPropertyAndLabel("rx");
        builder.addPropertyAndLabel("tx", 6);
        builder.nextRow();
        builder.addPropertyAndLabel("baud", 1, 2, 1);
        builder.addPropertyAndLabel("start9600", 6);
        return builder.getPanel();
    }

}