package it.eurotn.panjea.fatturepa.rich.editors.fatturaelettronicatype.v1.datiGenerali;

import java.util.List;

import javax.swing.JComponent;

import org.springframework.richclient.form.builder.FormLayoutFormBuilder;

import com.jgoodies.forms.layout.FormLayout;

import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.rich.binding.TableEditableBinding;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;
import it.gov.fatturapa.sdi.fatturapa.v1.DatiDDTType;
import it.gov.fatturapa.sdi.fatturapa.v1.FatturaElettronicaBodyType;

public class DatiGeneraliForm extends PanjeaAbstractForm {

    private static final String FORM_ID = "datiGeneraliFormV1";

    /**
     * Costruttore.
     *
     */
    public DatiGeneraliForm() {
        super(PanjeaFormModelHelper.createFormModel(new FatturaElettronicaBodyType(), false, FORM_ID), FORM_ID);
    }

    @Override
    protected JComponent createFormControl() {
        final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
        FormLayout layout = new FormLayout("left:pref,4dlu,fill:80dlu,10dlu,left:pref,4dlu,fill:80dlu",
                "2dlu,default,2dlu,default,2dlu,default,2dlu,default,2dlu,default,2dlu,default,2dlu,default,2dlu,120dlu");
        FormLayoutFormBuilder builder = new FormLayoutFormBuilder(bf, layout);
        builder.setLabelAttributes("r, c");

        builder.nextRow();
        builder.setRow(2);

        builder.addHorizontalSeparator("Dati documento", 7);
        builder.nextRow();

        builder.addPropertyAndLabel("datiGenerali.datiGeneraliDocumento.tipoDocumento", 1, 4, 5);
        builder.nextRow();

        builder.addPropertyAndLabel("datiGenerali.datiGeneraliDocumento.numero", 1);
        builder.addPropertyAndLabel("datiGenerali.datiGeneraliDocumento.data", 5);
        builder.nextRow();

        builder.addPropertyAndLabel("datiGenerali.datiGeneraliDocumento.importoTotaleDocumento", 1);
        builder.addPropertyAndLabel("datiGenerali.datiGeneraliDocumento.arrotondamento", 5);
        builder.nextRow();

        builder.addPropertyAndLabel("datiGenerali.datiGeneraliDocumento.divisa", 1);
        builder.addPropertyAndLabel("datiGenerali.datiGeneraliDocumento.causale", 5);
        builder.nextRow();

        builder.addPropertyAndLabel("datiGenerali.datiGeneraliDocumento.art73");
        builder.nextRow();

        builder.addHorizontalSeparator("Dati ddt di riferimento", 7);
        builder.nextRow();

        DatiDDTTableModel datiDDTTableModel = new DatiDDTTableModel();
        TableEditableBinding<DatiDDTType> datiDDTBinding = new TableEditableBinding<DatiDDTType>(getFormModel(),
                "datiGenerali.datiDDT", List.class, datiDDTTableModel);
        builder.addBinding(datiDDTBinding, 1, 16, 7, 1);

        return builder.getPanel();
    }

}
