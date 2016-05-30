package it.eurotn.panjea.spedizioni.rich.generazione;

import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.panjea.spedizioni.domain.DatiSpedizioniDocumento.Consegne;
import it.eurotn.panjea.spedizioni.util.ParametriCreazioneEtichette;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.math.BigDecimal;

import javax.swing.JComponent;
import javax.swing.JTextField;

import org.springframework.richclient.form.builder.FormLayoutFormBuilder;

import com.jgoodies.forms.layout.FormLayout;

public class ParametriCreazioneEtichetteForm extends PanjeaAbstractForm {

	private class ImportoContrassegnoChangeListener implements PropertyChangeListener {

		@Override
		public void propertyChange(PropertyChangeEvent evt) {

			BigDecimal impotoNew = (BigDecimal) evt.getNewValue();

			Consegne consegne = (Consegne) getFormModel().getValueModel("consegna").getValue();

			if (impotoNew != null && impotoNew.compareTo(BigDecimal.ZERO) != 0 && consegne == null) {
				getFormModel().getValueModel("consegna").setValue(Consegne.PER_APPUNTAMENTO);
			}
		}

	}

	public static final String FORM_ID = "parametriCreazioneEtichetteForm";

	/**
	 * Costruttore.
	 * 
	 */
	public ParametriCreazioneEtichetteForm() {
		super(PanjeaFormModelHelper.createFormModel(new ParametriCreazioneEtichette(), false, FORM_ID), FORM_ID);
	}

	@Override
	protected JComponent createFormControl() {
		final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
		FormLayout layout = new FormLayout("right:pref,4dlu,70dlu,left:pref:grow", "2dlu,default");
		FormLayoutFormBuilder builder = new FormLayoutFormBuilder(bf, layout); // , new FormDebugPanel());
		builder.setLabelAttributes("r, c");
		builder.setRow(2);

		builder.addPropertyAndLabel("dataConsegna", 1);
		builder.nextRow();

		builder.addPropertyAndLabel("consegna", 1);
		builder.nextRow();

		((JTextField) builder.addPropertyAndLabel("numeroColli", 1)[1]).setHorizontalAlignment(JTextField.RIGHT);
		builder.nextRow();

		builder.addPropertyAndLabel("peso", 1);
		builder.nextRow();

		builder.addPropertyAndLabel("volume", 1);
		builder.nextRow();

		builder.addPropertyAndLabel("note", 1, 12, 2);
		builder.nextRow();

		builder.addPropertyAndLabel("importoContrassegno", 1);
		builder.nextRow();

		builder.addPropertyAndLabel("modalitaIncasso", 1, 16, 2);
		builder.nextRow();

		getFormModel().getValueModel("importoContrassegno").addValueChangeListener(
				new ImportoContrassegnoChangeListener());

		return builder.getPanel();
	}

}
