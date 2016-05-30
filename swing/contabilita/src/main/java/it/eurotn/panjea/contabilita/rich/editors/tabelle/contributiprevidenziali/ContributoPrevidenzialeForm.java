package it.eurotn.panjea.contabilita.rich.editors.tabelle.contributiprevidenziali;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.DecimalFormat;

import javax.swing.JComponent;
import javax.swing.JTextField;

import org.springframework.richclient.form.binding.Binding;
import org.springframework.richclient.form.builder.FormLayoutFormBuilder;

import com.jgoodies.forms.layout.FormLayout;

import it.eurotn.panjea.contabilita.domain.ContributoPrevidenziale;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;

/**
 * @author fattazzo
 *
 */
public class ContributoPrevidenzialeForm extends PanjeaAbstractForm {

    private class PercCaricoLavoratoreListener implements PropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            Double percAzienda = ((ContributoPrevidenziale) getFormObject()).getPercCaricoAzienda();

            percCaricoAziendaField.setText(new DecimalFormat("#,##0.00").format(percAzienda) + " %");
        }

    }

    public static final String FORM_ID = "contributoPrevidenzialeForm";

    private JTextField percCaricoAziendaField;

    /**
     * Costruttore.
     * 
     * @param contributoPrevidenziale
     *            contributo previdenziale
     */
    public ContributoPrevidenzialeForm(final ContributoPrevidenziale contributoPrevidenziale) {
        super(PanjeaFormModelHelper.createFormModel(contributoPrevidenziale, false, FORM_ID), FORM_ID);
    }

    @Override
    protected JComponent createFormControl() {
        final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
        FormLayout layout = new FormLayout("right:pref,4dlu,fill:50dlu, 10dlu, right:pref,4dlu,fill:50dlu",
                "4dlu,default");
        FormLayoutFormBuilder builder = new FormLayoutFormBuilder(bf, layout);
        builder.setLabelAttributes("r, c");
        builder.setRow(2);

        builder.addPropertyAndLabel("codice", 1);
        builder.nextRow();

        builder.addLabel("percContributiva", 1);
        Binding percContributivaBinding = bf.createBoundPercentageText("percContributiva");
        builder.addBinding(percContributivaBinding, 3);
        builder.nextRow();

        builder.addLabel("percCaricoLavoratore", 1);
        Binding percCaricoLavoratoreBinding = bf.createBoundPercentageText("percCaricoLavoratore");
        builder.addBinding(percCaricoLavoratoreBinding, 3);

        builder.addLabel("percCaricoAzienda", 5);
        percCaricoAziendaField = getComponentFactory().createTextField();
        percCaricoAziendaField.setHorizontalAlignment(JTextField.RIGHT);
        percCaricoAziendaField.setEditable(false);
        percCaricoAziendaField.setFocusable(false);
        builder.addComponent(percCaricoAziendaField, 7);

        getFormModel().getValueModel("percCaricoLavoratore").addValueChangeListener(new PercCaricoLavoratoreListener());

        return builder.getPanel();
    }

}
