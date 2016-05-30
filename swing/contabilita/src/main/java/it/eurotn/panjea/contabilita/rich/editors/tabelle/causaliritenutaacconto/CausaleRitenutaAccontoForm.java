package it.eurotn.panjea.contabilita.rich.editors.tabelle.causaliritenutaacconto;

import javax.swing.JComponent;

import org.springframework.richclient.form.binding.Binding;
import org.springframework.richclient.form.builder.FormLayoutFormBuilder;

import com.jgoodies.forms.layout.FormLayout;

import it.eurotn.panjea.contabilita.domain.CausaleRitenutaAcconto;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;

/**
 * @author fattazzo
 *
 */
public class CausaleRitenutaAccontoForm extends PanjeaAbstractForm {

    public static final String FORM_ID = "causaleRitenutaAccontoForm";

    /**
     * Costruttore.
     */
    public CausaleRitenutaAccontoForm() {
        super(PanjeaFormModelHelper.createFormModel(new CausaleRitenutaAcconto(), false, FORM_ID), FORM_ID);
    }

    @Override
    protected JComponent createFormControl() {
        final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
        FormLayout layout = new FormLayout(
                "right:pref,4dlu,fill:50dlu, 10dlu, right:pref,4dlu,fill:50dlu,left:default:grow", "4dlu,default");
        FormLayoutFormBuilder builder = new FormLayoutFormBuilder(bf, layout);
        builder.setLabelAttributes("r, c");
        builder.setRow(2);

        builder.addPropertyAndLabel("codice", 1);
        builder.addPropertyAndLabel("descrizione", 5, 2, 2);
        builder.nextRow();

        builder.addLabel("percentualeAliquota", 1);
        Binding percentualeAliquotaBinding = bf.createBoundPercentageText("percentualeAliquota");
        builder.addBinding(percentualeAliquotaBinding, 3);
        builder.addLabel("percentualeImponibile", 5);
        Binding percentualeImponibileBinding = bf.createBoundPercentageText("percentualeImponibile");
        builder.addBinding(percentualeImponibileBinding, 7);
        builder.nextRow();

        builder.addPropertyAndLabel("tributo", 1);
        builder.addPropertyAndLabel("sezione", 5);

        return builder.getPanel();
    }

}
