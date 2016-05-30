package it.eurotn.panjea.tesoreria.rich.editors.anticipifattura;

import it.eurotn.panjea.partite.util.ParametriRicercaRate;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;

import javax.swing.JComponent;

import org.springframework.richclient.form.builder.FormLayoutFormBuilder;

import com.jgoodies.forms.layout.FormLayout;

public class ParametriRicercaAnticipiFatturaForm extends PanjeaAbstractForm {

	public static final String FORM_ID = "parametriRicercaAnticipiFatturaForm";

	/**
	 * Costruttore.
	 * 
	 * @param parametriRicercaRate
	 *            parametriRicercaRate
	 */
	public ParametriRicercaAnticipiFatturaForm(final ParametriRicercaRate parametriRicercaRate) {
		super(PanjeaFormModelHelper.createFormModel(parametriRicercaRate, false, FORM_ID), FORM_ID);
	}

	@Override
	protected JComponent createFormControl() {
		final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
		FormLayout layout = new FormLayout("4dlu, left:default,4dlu,left:default", "default");
		FormLayoutFormBuilder builder = new FormLayoutFormBuilder(bf, layout);

		builder.nextRow();

		// periodo
		builder.addPropertyAndLabel("dataScadenza", 2);
		builder.nextRow();

		return builder.getPanel();
	}

}
