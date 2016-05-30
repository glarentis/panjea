package it.eurotn.panjea.fatturepa.rich.editors.fatturaelettronicatype.v1;

import javax.swing.JComponent;

import org.springframework.richclient.form.builder.FormLayoutFormBuilder;

import com.jgoodies.forms.layout.FormLayout;

import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;
import it.gov.fatturapa.sdi.fatturapa.IFatturaElettronicaType;

public class AreaFatturaElettronicaForm extends PanjeaAbstractForm {

    private static final String FORM_ID = "areaFatturaElettronicaFormV1";

    /**
     * Costruttore.
     *
     * @param fatturaElettronicaType
     *            fattura elettronica
     */
    public AreaFatturaElettronicaForm(final IFatturaElettronicaType fatturaElettronicaType) {
        super(PanjeaFormModelHelper.createFormModel(fatturaElettronicaType, false, FORM_ID), FORM_ID);
    }

    @Override
    protected JComponent createFormControl() {
        final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
        FormLayout layout = new FormLayout("left:pref,4dlu,left:70dlu", "2dlu,default");
        FormLayoutFormBuilder builder = new FormLayoutFormBuilder(bf, layout);
        builder.setLabelAttributes("r, c");

        builder.nextRow();
        builder.setRow(2);

        builder.addPropertyAndLabel("fatturaElettronicaHeader.datiTrasmissione.progressivoInvio");
        builder.nextRow();

        builder.addPropertyAndLabel("fatturaElettronicaHeader.datiTrasmissione.formatoTrasmissione");
        builder.nextRow();

        builder.addPropertyAndLabel("fatturaElettronicaHeader.datiTrasmissione.codiceDestinatario");
        builder.nextRow();

        builder.addHorizontalSeparator("Contatto (1.1.5)", 3);
        builder.nextRow();

        builder.addPropertyAndLabel("fatturaElettronicaHeader.datiTrasmissione.contattiTrasmittente.telefono");
        builder.nextRow();

        builder.addPropertyAndLabel("fatturaElettronicaHeader.datiTrasmissione.contattiTrasmittente.email");
        builder.nextRow();

        getFormModel().getFieldMetadata("fatturaElettronicaHeader.datiTrasmissione.progressivoInvio").setReadOnly(true);
        getFormModel().getFieldMetadata("fatturaElettronicaHeader.datiTrasmissione.formatoTrasmissione")
                .setReadOnly(true);

        return builder.getPanel();
    }

}
