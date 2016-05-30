package it.eurotn.panjea.rich.editors.stampe.nuovo;

import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;

import javax.swing.JComponent;

import org.springframework.richclient.form.builder.FormLayoutFormBuilder;

import com.jgoodies.forms.layout.FormLayout;

public class NuovoLayoutStampaForm extends PanjeaAbstractForm {

    public static final String FORM_ID = "nuovoLayoutStampaForm";

    /**
     * Costruttore.
     *
     * @param layoutStampaPM
     *            layout
     */
    public NuovoLayoutStampaForm(final LayoutStampaPM layoutStampaPM) {
        super(PanjeaFormModelHelper.createFormModel(layoutStampaPM, false, FORM_ID), FORM_ID);
    }

    @Override
    protected JComponent createFormControl() {
        final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
        FormLayout layout = new FormLayout("right:pref,4dlu,fill:pref", "4dlu,default");
        FormLayoutFormBuilder builder = new FormLayoutFormBuilder(bf, layout); // , new FormDebugPanel());
        builder.setLabelAttributes("r, c");

        builder.addPropertyAndLabel("reportName", 1, 10);

        return builder.getPanel();
    }

    @Override
    protected Object createNewObject() {

        return super.createNewObject();
    }

}
