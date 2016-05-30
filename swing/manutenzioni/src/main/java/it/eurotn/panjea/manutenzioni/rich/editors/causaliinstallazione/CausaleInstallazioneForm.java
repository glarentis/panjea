package it.eurotn.panjea.manutenzioni.rich.editors.causaliinstallazione;

import javax.swing.JComponent;

import com.jgoodies.forms.layout.FormLayout;

import it.eurotn.panjea.manutenzioni.domain.CausaleInstallazione;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormLayoutBuilder;
import it.eurotn.rich.form.PanjeaFormModelHelper;

public class CausaleInstallazioneForm extends PanjeaAbstractForm {

    private static final String FORM_ID = "causaleInstallazioneForm";

    /**
     * Costruttore.
     */
    public CausaleInstallazioneForm() {
        super(PanjeaFormModelHelper.createFormModel(new CausaleInstallazione(), false, FORM_ID), FORM_ID);
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
        builder.addPropertyAndLabel("tipoInstallazione");
        builder.nextRow();
        builder.addPropertyAndLabel("ordinamento");
        return builder.getPanel();
    }

}