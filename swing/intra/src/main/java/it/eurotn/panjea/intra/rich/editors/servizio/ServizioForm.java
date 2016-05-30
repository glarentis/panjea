package it.eurotn.panjea.intra.rich.editors.servizio;

import it.eurotn.panjea.intra.domain.Servizio;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;

import javax.swing.JComponent;

import org.springframework.richclient.form.builder.FormLayoutFormBuilder;

import com.jgoodies.forms.layout.FormLayout;

public class ServizioForm extends PanjeaAbstractForm {

	private static final String FORM_ID = "servizioForm";

	/**
	 * Costruttore.
	 * 
	 * @param servizio
	 *            la nomenclatura da gestire
	 */
	public ServizioForm(final Servizio servizio) {
		super(PanjeaFormModelHelper.createFormModel(servizio, false, FORM_ID + "Model"), FORM_ID);
	}

	@Override
	protected JComponent createFormControl() {
		final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
		FormLayout layout = new FormLayout("right:pref,4dlu,left:default, left:pref:grow", "");
		FormLayoutFormBuilder builder = new FormLayoutFormBuilder(bf, layout); // , new FormDebugPanel()
		builder.nextRow();

		builder.addPropertyAndLabel("codice");
		builder.nextRow();

		builder.addPropertyAndLabel("descrizione");
		builder.nextRow();

		return builder.getPanel();
	}
}
