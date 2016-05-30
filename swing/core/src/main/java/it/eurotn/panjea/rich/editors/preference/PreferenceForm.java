package it.eurotn.panjea.rich.editors.preference;

import javax.swing.JComponent;

import org.springframework.richclient.form.builder.FormLayoutFormBuilder;

import com.jgoodies.forms.layout.FormLayout;

import it.eurotn.panjea.anagrafica.domain.Preference;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;

public class PreferenceForm extends PanjeaAbstractForm {

    private static final String FORM_ID = "preferenceForm";

    /**
     * Costruttore.
     *
     */
    public PreferenceForm() {
        super(PanjeaFormModelHelper.createFormModel(new Preference(), false, FORM_ID), FORM_ID);
    }

    @Override
    protected JComponent createFormControl() {
        final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
        FormLayout layout = new FormLayout("left:pref,4dlu,fill:200dlu", "4dlu,default");
        FormLayoutFormBuilder builder = new FormLayoutFormBuilder(bf, layout);
        builder.setLabelAttributes("r, c");
        builder.setRow(2);
        builder.addPropertyAndLabel("chiave");
        builder.nextRow();
        builder.addPropertyAndLabel("valore");
        builder.nextRow();
        builder.addPropertyAndLabel("nomeUtente");
        return builder.getPanel();
    }

}
