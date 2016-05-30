package it.eurotn.panjea.vending.rich.editors.anagraficheasl;

import javax.swing.JComponent;

import com.jgoodies.forms.layout.FormLayout;

import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.panjea.vending.domain.AnagraficaAsl;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormLayoutBuilder;
import it.eurotn.rich.form.PanjeaFormModelHelper;

public class AnagraficaAslForm extends PanjeaAbstractForm {

    private static final String FORM_ID = "anagraficaAslForm";

    /**
     * Costruttore.
     */
    public AnagraficaAslForm() {
        super(PanjeaFormModelHelper.createFormModel(new AnagraficaAsl(), false, FORM_ID), FORM_ID);
    }

    @Override
    protected JComponent createFormControl() {
        final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
        FormLayout layout = new FormLayout("right:pref,4dlu,left:pref", "4dlu,default");
        PanjeaFormLayoutBuilder builder = new PanjeaFormLayoutBuilder(bf, layout);
        builder.setLabelAttributes("r, c");
        builder.setRow(2);

        builder.addPropertyAndLabel("codice");
        builder.nextRow();

        builder.addPropertyAndLabel("descrizione");
        builder.nextRow();

        builder.addPropertyAndLabel("indirizzo");
        builder.nextRow();

        return builder.getPanel();
    }

}