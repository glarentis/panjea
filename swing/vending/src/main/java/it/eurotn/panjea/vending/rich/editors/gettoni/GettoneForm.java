package it.eurotn.panjea.vending.rich.editors.gettoni;

import javax.swing.JComponent;

import com.jgoodies.forms.layout.FormLayout;

import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.panjea.vending.domain.Gettone;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormLayoutBuilder;
import it.eurotn.rich.form.PanjeaFormModelHelper;

public class GettoneForm extends PanjeaAbstractForm {

    private static final String FORM_ID = "gettoneForm";

    /**
     * Costruttore.
     */
    public GettoneForm() {
        super(PanjeaFormModelHelper.createFormModel(new Gettone(), false, FORM_ID), FORM_ID);
    }

    @Override
    protected JComponent createFormControl() {
        final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
        FormLayout layout = new FormLayout("right:pref,4dlu,fill:50dlu,left:100dlu", "4dlu,default");
        PanjeaFormLayoutBuilder builder = new PanjeaFormLayoutBuilder(bf, layout);
        builder.setLabelAttributes("r, c");
        builder.setRow(2);

        builder.addPropertyAndLabel("codice");
        builder.nextRow();

        builder.addPropertyAndLabel("descrizione", 1, 2, 1);
        builder.nextRow();

        builder.addPropertyAndLabel("valore");
        builder.nextRow();

        return builder.getPanel();
    }

}