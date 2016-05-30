package it.eurotn.panjea.magazzino.rich.editors.faselavorazione;

import it.eurotn.panjea.anagrafica.domain.FaseLavorazione;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;

import javax.swing.JComponent;

import org.apache.log4j.Logger;
import org.springframework.richclient.form.builder.FormLayoutFormBuilder;

import com.jgoodies.forms.layout.FormLayout;

public class FaseLavorazioneForm extends PanjeaAbstractForm {

	private static Logger logger = Logger.getLogger(FaseLavorazioneForm.class);

	private static final String FORM_ID = "faseLavorazioneForm";

	/**
	 * Costruttore.
	 */
	public FaseLavorazioneForm() {
		super(PanjeaFormModelHelper.createFormModel(new FaseLavorazione(), false, FORM_ID), FORM_ID);
	}

	@Override
	protected JComponent createFormControl() {
		logger.debug("--> Enter createFormControl");
		final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
		FormLayout layout = new FormLayout("right:pref,4dlu,left:default,right:default:grow", "4dlu,default");
		FormLayoutFormBuilder builder = new FormLayoutFormBuilder(bf, layout);

		builder.setRow(2);
		builder.addPropertyAndLabel("codice");
		builder.nextRow();

		builder.addPropertyAndLabel("descrizione");
		builder.nextRow();

		builder.addPropertyAndLabel("ordinamento");
		builder.nextRow();

		builder.nextRow();
		builder.addBinding(bf.createTableBinding("fasiArticolo", 500, new ArticoliFasiLavorazioneTableModel(), null),
				1, 10, 4, 1);

		logger.debug("--> Exit createFormControl");
		return builder.getPanel();
	}

}
