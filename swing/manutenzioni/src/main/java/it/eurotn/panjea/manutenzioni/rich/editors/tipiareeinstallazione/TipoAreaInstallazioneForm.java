package it.eurotn.panjea.manutenzioni.rich.editors.tipiareeinstallazione;

import javax.swing.JComponent;

import com.jgoodies.forms.layout.FormLayout;

import it.eurotn.panjea.manutenzioni.domain.documento.TipoAreaInstallazione;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormLayoutBuilder;
import it.eurotn.rich.form.PanjeaFormModelHelper;

public class TipoAreaInstallazioneForm extends PanjeaAbstractForm {

    private static final String FORM_ID = "tipoAreaInstallazioneForm";

    /**
     * Costruttore.
     */
    public TipoAreaInstallazioneForm() {
        super(PanjeaFormModelHelper.createFormModel(new TipoAreaInstallazione(), false, FORM_ID), FORM_ID);
    }

    @Override
    protected JComponent createFormControl() {
        final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
        FormLayout layout = new FormLayout("right:pref,4dlu,left:pref", "4dlu,default");
        PanjeaFormLayoutBuilder builder = new PanjeaFormLayoutBuilder(bf, layout);
        builder.setLabelAttributes("r, c");
        builder.setRow(2);
        builder.addPropertyAndLabel("descrizionePerStampa");
        return builder.getPanel();
    }

}