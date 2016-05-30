package it.eurotn.panjea.manutenzioni.rich.editors.operatore.sostituzione;

import javax.swing.JComponent;

import com.jgoodies.forms.layout.FormLayout;

import it.eurotn.panjea.manutenzioni.rich.search.OperatoreSearchObject;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.rich.binding.searchtext.SearchPanel;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormLayoutBuilder;
import it.eurotn.rich.form.PanjeaFormModelHelper;

public class SostituzioneOperatoreForm extends PanjeaAbstractForm {

    /**
     * Costruttore.
     */
    public SostituzioneOperatoreForm() {
        super(PanjeaFormModelHelper.createFormModel(new SostituzioneOperatorePM(), false, "SostituzioneOperatoreForm"),
                "SostituzioneOperatoreForm");
    }

    @Override
    protected JComponent createFormControl() {
        final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
        FormLayout layout = new FormLayout("right:pref,4dlu,left:pref", "4dlu,default");
        PanjeaFormLayoutBuilder builder = new PanjeaFormLayoutBuilder(bf, layout);
        builder.setLabelAttributes("r, c");
        builder.setRow(2);

        builder.addLabel("operatore");
        SearchPanel searchOperatore = (SearchPanel) builder
                .addBinding(bf.createBoundSearchText("operatore", new String[] { "codice", "denominazione" }), 3);
        searchOperatore.getTextFields().get("codice").setColumns(10);
        searchOperatore.getTextFields().get("denominazione").setColumns(20);
        builder.nextRow();

        builder.addLabel("operatoreDaSostituire");
        searchOperatore = (SearchPanel) builder.addBinding(
                bf.createBoundSearchText("operatoreDaSostituire", new String[] { "codice", "denominazione" },
                        new String[] { "operatore.tecnico", "operatore.caricatore" }, new String[] {
                                OperatoreSearchObject.TECNICO_PARAM_KEY, OperatoreSearchObject.CARICATORE_PARAM_KEY }),
                3);
        searchOperatore.getTextFields().get("codice").setColumns(10);
        searchOperatore.getTextFields().get("denominazione").setColumns(20);
        builder.nextRow();

        builder.addPropertyAndLabel("tecnico");
        builder.nextRow();

        builder.addPropertyAndLabel("caricatore");
        builder.nextRow();

        return builder.getPanel();
    }

    @Override
    public void setFormObject(Object formObject) {
        super.setFormObject(formObject);

        getFormModel().getFieldMetadata("operatore").setReadOnly(true);

        getFormModel().getFieldMetadata("tecnico")
                .setReadOnly(!(boolean) getValueModel("operatore.tecnico").getValue());
        getFormModel().getFieldMetadata("caricatore")
                .setReadOnly(!(boolean) getValueModel("operatore.caricatore").getValue());
    }

}
