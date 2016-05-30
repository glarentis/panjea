package it.eurotn.panjea.tesoreria.rich.forms.ricercaeffetti;

import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.panjea.tesoreria.util.ParametriCreazioneInsoluti;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JComponent;
import javax.swing.JTextField;

import org.apache.log4j.Logger;
import org.springframework.richclient.form.builder.FormLayoutFormBuilder;

import com.jgoodies.forms.layout.FormLayout;

public class ParametriCreazioneInsolutiForm extends PanjeaAbstractForm {

	private class NumeroDocumentoRichiestoListener implements PropertyChangeListener {

		@Override
		public void propertyChange(PropertyChangeEvent arg0) {

			Boolean nDocRichiesto = ((Boolean) arg0.getNewValue());

			getFormModel().getFieldMetadata("numeroDocumento").setEnabled(nDocRichiesto);

		}

	}

	private static Logger logger = Logger.getLogger(ParametriCreazioneInsolutiForm.class);
	public static final String FORM_ID = "parametriCreazioneInsolutiForm";

	/**
	 * Costruttore.
	 */
	public ParametriCreazioneInsolutiForm() {
		super(PanjeaFormModelHelper.createFormModel(new ParametriCreazioneInsoluti(), false, FORM_ID), FORM_ID);
	}

	@Override
	protected JComponent createFormControl() {
		logger.debug("--> Creo controlli per il form");
		final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
		FormLayout layout = new FormLayout(
				"left:p, 4dlu, left:pref, 10dlu,left:pref, 4dlu, left:50dlu, 10dlu,left:pref, 4dlu, left:50dlu",
				"default");
		FormLayoutFormBuilder builder = new FormLayoutFormBuilder(bf, layout);

		builder.setLabelAttributes("colGrId=label colSpec=right:pref");
		builder.setLabelAttributes("r, c");

		builder.addPropertyAndLabel("dataDocumento", 1);

		builder.addPropertyAndLabel("numeroDocumento", 5);

		builder.addLabel("speseInsoluto", 9);
		JTextField textField = (JTextField) builder.addProperty("speseInsoluto", 11);
		textField.setColumns(10);

		getFormModel().getValueModel("numeroDocumentoRichiesto").addValueChangeListener(
				new NumeroDocumentoRichiestoListener());

		return builder.getPanel();
	}

}
