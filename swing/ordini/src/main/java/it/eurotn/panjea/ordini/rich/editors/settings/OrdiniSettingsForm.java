package it.eurotn.panjea.ordini.rich.editors.settings;

import javax.swing.JComponent;

import org.springframework.richclient.form.builder.FormLayoutFormBuilder;

import com.jgoodies.forms.layout.FormLayout;

import it.eurotn.panjea.ordini.domain.OrdiniSettings;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;

public class OrdiniSettingsForm extends PanjeaAbstractForm {

    private static final String FORM_ID = "ordiniSettingsForm";

    /**
     * costruttore.
     */
    public OrdiniSettingsForm() {
        super(PanjeaFormModelHelper.createFormModel(new OrdiniSettings(), false, FORM_ID), FORM_ID);
    }

    @Override
    protected JComponent createFormControl() {
        final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
        FormLayout layout = new FormLayout("right:pref,4dlu,left:pref", "4dlu,default");
        FormLayoutFormBuilder builder = new FormLayoutFormBuilder(bf, layout); // , new
                                                                               // FormDebugPanelNumbered());
        builder.setLabelAttributes("r, c");
        builder.setRow(2);

        builder.addPropertyAndLabel("creazioneMissioniAbilitata", 1);
        builder.nextRow();

        builder.addPropertyAndLabel("selezioneMissioniDaEvadereManuale", 1);
        builder.nextRow();

        builder.addPropertyAndLabel("attributiMissioni", 1);
        builder.nextRow();

        return builder.getPanel();
    }
}
