package it.eurotn.panjea.ordini.rich.editors.ordiniimportati;

import it.eurotn.panjea.ordini.util.parametriricerca.ParametriRicercaOrdiniImportati;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;

import javax.swing.JComponent;

import org.springframework.richclient.form.builder.FormLayoutFormBuilder;

import com.jgoodies.forms.layout.FormLayout;

public class ParametriImportazioneOrdiniForm extends PanjeaAbstractForm {

	private static final String FORM_ID = "parametriImportazioneOrdiniForm";

	/**
	 * Costruttore.
	 * 
	 */
	public ParametriImportazioneOrdiniForm() {
		super(PanjeaFormModelHelper.createFormModel(new ParametriRicercaOrdiniImportati(), false, FORM_ID), FORM_ID);
	}

	@Override
	protected JComponent createFormControl() {
		final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
		FormLayout layout = new FormLayout("right:pref,4dlu,left:default", "2dlu,default");
		FormLayoutFormBuilder builder = new FormLayoutFormBuilder(bf, layout); // ,
																				// new
																				// FormDebugPanel());
		builder.setLabelAttributes("r, c");

		builder.addPropertyAndLabel("provenienza", 1, 2);

		return builder.getPanel();
	}

}
