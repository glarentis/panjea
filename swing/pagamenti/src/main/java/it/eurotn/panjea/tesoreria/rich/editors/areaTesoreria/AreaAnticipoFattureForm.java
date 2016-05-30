package it.eurotn.panjea.tesoreria.rich.editors.areaTesoreria;

import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.panjea.tesoreria.domain.AreaAnticipoFatture;
import it.eurotn.panjea.tesoreria.rich.forms.AreaTesoreriaForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;

import javax.swing.JComponent;

import org.springframework.richclient.form.builder.FormLayoutFormBuilder;

import com.jgoodies.forms.layout.FormLayout;

public class AreaAnticipoFattureForm extends AreaTesoreriaForm {

	private static final String FORM_ID = "areaAnticipoFattureForm";

	/**
	 * Costruttore.
	 */
	public AreaAnticipoFattureForm() {
		super(PanjeaFormModelHelper.createFormModel(new AreaAnticipoFatture(), false, FORM_ID), FORM_ID);
	}

	@Override
	protected JComponent createFormControl() {
		final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
		FormLayout layout = new FormLayout("right:pref,15dlu,left:pref,10dlu,left:pref",
				"2dlu, default, 2dlu, default:grow");
		FormLayoutFormBuilder builder = new FormLayoutFormBuilder(bf, layout);// , new FormDebugPanel()
		builder.setLabelAttributes("r,c");

		JComponent formControl = super.createFormControl();
		builder.addComponent(formControl, 1, 2, 1, 3);
		builder.addPropertyAndLabel("dataScadenzaAnticipoFatture", 3);

		return builder.getPanel();
	}

}
