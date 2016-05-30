package it.eurotn.panjea.magazzino.rich.editors.listinoprezzi;

import javax.swing.JComponent;

import com.jgoodies.forms.layout.FormLayout;

import it.eurotn.panjea.magazzino.manager.listinoprezzi.ParametriListinoPrezzi;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormLayoutBuilder;
import it.eurotn.rich.form.PanjeaFormModelHelper;

public class ListinoPrezziParametriForm extends PanjeaAbstractForm {

    private static final String FORM_ID = "listinoPrezziParametriForm";

    /**
     * Costruttore.
     */
    public ListinoPrezziParametriForm() {
        super(PanjeaFormModelHelper.createFormModel(new ParametriListinoPrezzi(), false, FORM_ID), FORM_ID);
    }

    @Override
    protected JComponent createFormControl() {
        final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
        FormLayout layout = new FormLayout("right:pref,4dlu,fill:pref,10dlu,right:pref,4dlu,fill:150dlu",
                "4dlu,default");
        PanjeaFormLayoutBuilder builder = new PanjeaFormLayoutBuilder(bf, layout);
        builder.setLabelAttributes("r, c");
        builder.setRow(2);
        builder.addPropertyAndLabel("data");
        builder.addPropertyAndLabel("numRecordInPagina", 5);
        builder.nextRow();
        builder.addLabel("articolo");
        builder.addBinding(bf.createBoundSearchText("articoloPartenza", new String[] { "codice", "descrizione" }), 3, 5,
                1);
        builder.nextRow();
        builder.addLabel("entita");
        builder.addBinding(bf.createBoundSearchText("entita", new String[] { "codice", "anagrafica.denominazione" }), 3,
                5, 1);
        return builder.getPanel();
    }

}
