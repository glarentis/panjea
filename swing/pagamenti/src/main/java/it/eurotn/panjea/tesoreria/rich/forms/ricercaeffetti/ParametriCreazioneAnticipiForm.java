package it.eurotn.panjea.tesoreria.rich.forms.ricercaeffetti;

import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.panjea.tesoreria.util.ParametriCreazioneAnticipi;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JComponent;

import org.apache.log4j.Logger;
import org.springframework.richclient.form.builder.FormLayoutFormBuilder;

import com.jgoodies.forms.layout.FormLayout;

public class ParametriCreazioneAnticipiForm extends PanjeaAbstractForm {

	private class NumeroDocumentoRichiestoListener implements PropertyChangeListener {

		@Override
		public void propertyChange(PropertyChangeEvent arg0) {

			Boolean nDocRichiesto = ((Boolean) arg0.getNewValue());

			getFormModel().getFieldMetadata("numeroDocumento").setEnabled(nDocRichiesto);

		}

	}

	private static Logger logger = Logger.getLogger(ParametriCreazioneAnticipiForm.class);
	public static final String FORM_ID = "parametriCreazioneAnticipiForm";

	/**
	 * Costruttore.
	 */
	public ParametriCreazioneAnticipiForm() {
		super(PanjeaFormModelHelper.createFormModel(new ParametriCreazioneAnticipi(), false, FORM_ID), FORM_ID);
	}

	@Override
	protected JComponent createFormControl() {
		logger.debug("--> Creo controlli per il form");
		final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
		FormLayout layout = new FormLayout("left:p, 4dlu, left:pref, 10dlu,left:pref, 4dlu, left:50dlu", "default");
		FormLayoutFormBuilder builder = new FormLayoutFormBuilder(bf, layout);

		builder.setLabelAttributes("colGrId=label colSpec=right:pref");
		builder.setLabelAttributes("r, c");

		builder.addPropertyAndLabel("dataDocumento", 1);

		builder.addPropertyAndLabel("numeroDocumento", 5);
		getFormModel().getValueModel("numeroDocumentoRichiesto").addValueChangeListener(
				new NumeroDocumentoRichiestoListener());

		return builder.getPanel();
	}

}
