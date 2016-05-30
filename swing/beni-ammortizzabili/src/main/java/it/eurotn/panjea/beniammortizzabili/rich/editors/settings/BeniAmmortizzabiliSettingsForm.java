package it.eurotn.panjea.beniammortizzabili.rich.editors.settings;

import it.eurotn.panjea.beniammortizzabili2.domain.BeniSettings;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;

import javax.swing.JComponent;

import org.springframework.richclient.form.builder.FormLayoutFormBuilder;

import com.jgoodies.forms.layout.FormLayout;

public class BeniAmmortizzabiliSettingsForm extends PanjeaAbstractForm {

	private static final String FORM_ID = "beniAmmortizzabiliSettingsForm";

	/**
	 *
	 */
	public BeniAmmortizzabiliSettingsForm() {
		super(PanjeaFormModelHelper.createFormModel(new BeniSettings(), false, FORM_ID), FORM_ID);
	}

	@Override
	protected JComponent createFormControl() {
		final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
		FormLayout layout = new FormLayout("right:pref,4dlu,left:pref:grow", "4dlu,default");
		FormLayoutFormBuilder builder = new FormLayoutFormBuilder(bf, layout); // , new FormDebugPanelNumbered());
		builder.setLabelAttributes("r, c");
		builder.setRow(2);

		builder.addHorizontalSeparator("Simulazione beni ", 3);
		builder.nextRow();

		builder.addPropertyAndLabel("tipoRaggruppamentoACSimulazione");
		builder.nextRow();

		builder.addPropertyAndLabel("raggruppamentoACSimulazione");
		builder.nextRow();

		return builder.getPanel();
	}
}
