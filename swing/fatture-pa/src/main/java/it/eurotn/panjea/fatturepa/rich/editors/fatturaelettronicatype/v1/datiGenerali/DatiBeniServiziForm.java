package it.eurotn.panjea.fatturepa.rich.editors.fatturaelettronicatype.v1.datiGenerali;

import java.util.List;

import javax.swing.JComponent;

import org.springframework.binding.form.FormModel;
import org.springframework.richclient.form.builder.FormLayoutFormBuilder;

import com.jgoodies.forms.layout.FormLayout;

import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.rich.binding.TableEditableBinding;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.gov.fatturapa.sdi.fatturapa.v1.DatiRiepilogoType;
import it.gov.fatturapa.sdi.fatturapa.v1.DettaglioLineeType;

public class DatiBeniServiziForm extends PanjeaAbstractForm {

    private static final String FORM_ID = "datiBeniServiziFormV1";

    /**
     * Costruttore.
     *
     * @param formModel
     *            form model
     */
    public DatiBeniServiziForm(final FormModel formModel) {
        super(formModel, FORM_ID);
    }

    @Override
    protected JComponent createFormControl() {
        final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
        FormLayout layout = new FormLayout("left:pref:grow,4dlu,left:90dlu,10dlu,left:pref,4dlu,left:90dlu",
                "2dlu,default,2dlu,170dlu,2dlu,default,2dlu,70dlu");
        FormLayoutFormBuilder builder = new FormLayoutFormBuilder(bf, layout);
        builder.setLabelAttributes("r, c");

        builder.nextRow();
        builder.setRow(2);

        builder.addHorizontalSeparator("Dettaglio righe (2.2.1)", 7);
        builder.nextRow();

        DettaglioLineeTypeTableModel dettaglioLineeTypeTableModel = new DettaglioLineeTypeTableModel();
        TableEditableBinding<DettaglioLineeType> dettaglioLineeBinding = new TableEditableBinding<DettaglioLineeType>(
                getFormModel(), "datiBeniServizi.dettaglioLinee", List.class, dettaglioLineeTypeTableModel);
        builder.addBinding(dettaglioLineeBinding, 1, 4, 7, 1);
        builder.nextRow();

        builder.addHorizontalSeparator("Dati riepilogo (2.2.2)", 7);
        builder.nextRow();

        DatiRiepilogoTypeTableModel datiRiepilogoTypeTableModel = new DatiRiepilogoTypeTableModel();
        TableEditableBinding<DatiRiepilogoType> datiRiepilogoBinding = new TableEditableBinding<DatiRiepilogoType>(
                getFormModel(), "datiBeniServizi.datiRiepilogo", List.class, datiRiepilogoTypeTableModel);
        builder.addBinding(datiRiepilogoBinding, 1, 8, 7, 1);
        builder.nextRow();

        return builder.getPanel();
    }

}
