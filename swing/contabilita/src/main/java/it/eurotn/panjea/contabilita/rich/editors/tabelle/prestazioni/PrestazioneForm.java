package it.eurotn.panjea.contabilita.rich.editors.tabelle.prestazioni;

import javax.swing.JComponent;

import org.springframework.richclient.form.builder.FormLayoutFormBuilder;

import com.jgoodies.forms.layout.FormLayout;

import it.eurotn.panjea.contabilita.domain.Prestazione;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;

/**
 * @author fattazzo
 *
 */
public class PrestazioneForm extends PanjeaAbstractForm {

    public static final String FORM_ID = "prestazioneForm";

    /**
     * Costruttore.
     */
    public PrestazioneForm() {
        super(PanjeaFormModelHelper.createFormModel(new Prestazione(), false, FORM_ID), FORM_ID);
    }

    @Override
    protected JComponent createFormControl() {
        final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
        FormLayout layout = new FormLayout("right:pref,4dlu,fill:50dlu, 10dlu, right:pref,4dlu,fill:200dlu",
                "4dlu,default");
        FormLayoutFormBuilder builder = new FormLayoutFormBuilder(bf, layout);
        builder.setLabelAttributes("r, c");
        builder.setRow(2);

        builder.addPropertyAndLabel("codice", 1);
        builder.addPropertyAndLabel("descrizione", 5);

        return builder.getPanel();
    }

}
