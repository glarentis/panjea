package it.eurotn.panjea.vending.rich.editors.tipicomunicazione;

import javax.swing.JComponent;

import org.springframework.richclient.form.builder.FormLayoutFormBuilder;

import com.jgoodies.forms.layout.FormLayout;

import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.panjea.vending.domain.TipoComunicazione;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;

public class TipoComunicazioneForm extends PanjeaAbstractForm {

    private static final String FORM_ID = "tipoComunicazioneForm";

    /**
     * Costruttore.
     */
    public TipoComunicazioneForm() {
        super(PanjeaFormModelHelper.createFormModel(new TipoComunicazione(), false, FORM_ID), FORM_ID);
    }

    @Override
    protected JComponent createFormControl() {
        final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
        FormLayout layout = new FormLayout("right:pref,4dlu,left:70dlu,left:150dlu", "4dlu,default");
        FormLayoutFormBuilder builder = new FormLayoutFormBuilder(bf, layout);
        builder.setLabelAttributes("r, c");
        builder.setRow(2);

        builder.addPropertyAndLabel("codice", 1);
        builder.nextRow();

        builder.addPropertyAndLabel("descrizione", 1, 4, 2);
        builder.nextRow();

        builder.addPropertyAndLabel("comunicazioneAsl", 1);
        builder.nextRow();

        builder.addPropertyAndLabel("comunicazioneComuni", 1);
        builder.nextRow();

        return builder.getPanel();
    }

}
