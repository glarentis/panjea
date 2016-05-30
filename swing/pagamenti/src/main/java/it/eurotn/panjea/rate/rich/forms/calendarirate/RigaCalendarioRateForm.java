package it.eurotn.panjea.rate.rich.forms.calendarirate;

import it.eurotn.panjea.rate.domain.RigaCalendarioRate;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;

import javax.swing.JComponent;

import org.springframework.richclient.form.builder.FormLayoutFormBuilder;

import com.jgoodies.forms.layout.FormLayout;

public class RigaCalendarioRateForm extends PanjeaAbstractForm {

	public static final String FORM_ID = "rigaCalendarioRateForm";

	/**
	 * Costruttore.
	 * 
	 */
	public RigaCalendarioRateForm() {
		super(PanjeaFormModelHelper.createFormModel(new RigaCalendarioRate(), false, FORM_ID), FORM_ID);
	}

	@Override
	protected JComponent createFormControl() {
		final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
		FormLayout layout = new FormLayout("left:pref, 4dlu, left:default,10dlu,left:pref, 4dlu, left:default",
				"4dlu,default,4dlu,default,4dlu,default,4dlu,default");
		FormLayoutFormBuilder builder = new FormLayoutFormBuilder(bf, layout); // ,
																				// new
																				// FormDebugPanel());

		builder.setLabelAttributes("r,c");

		builder.addPropertyAndLabel("dataIniziale", 1, 2);
		builder.addPropertyAndLabel("dataFinale", 5, 2);
		builder.addPropertyAndLabel("ripeti", 1, 4);
		builder.addPropertyAndLabel("dataAlternativa", 1, 6);
		builder.addPropertyAndLabel("note", 1, 8, 5);

		return builder.getPanel();
	}

}
