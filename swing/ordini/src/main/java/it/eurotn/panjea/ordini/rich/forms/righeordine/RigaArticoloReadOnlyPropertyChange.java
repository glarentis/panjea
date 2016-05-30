package it.eurotn.panjea.ordini.rich.forms.righeordine;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.springframework.binding.form.FormModel;

import it.eurotn.panjea.magazzino.domain.omaggio.TipoOmaggio;

public class RigaArticoloReadOnlyPropertyChange implements PropertyChangeListener {

	private final FormModel formModel;

	/**
	 * Costruttore.
	 *
	 * @param formModel
	 *            formModel
	 */
	public RigaArticoloReadOnlyPropertyChange(final FormModel formModel) {
		super();
		this.formModel = formModel;
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		// proezzo netto e totale devono sempre rimanere disabilitate
		formModel.getFieldMetadata("prezzoNetto").setReadOnly(true);
		formModel.getFieldMetadata("prezzoTotale").setReadOnly(true);

		TipoOmaggio tipoOmaggio = (TipoOmaggio) formModel.getValueModel("tipoOmaggio").getValue();
		boolean isReadOnly = tipoOmaggio != null && !tipoOmaggio.equals(TipoOmaggio.NESSUNO) ? true : false;
		boolean sconto1Bloccato = (Boolean) formModel.getValueModel("sconto1Bloccato").getValue();

		formModel.getFieldMetadata("prezzoUnitarioReale").setReadOnly(isReadOnly);
		formModel.getFieldMetadata("variazione1").setReadOnly(isReadOnly || sconto1Bloccato);
		formModel.getFieldMetadata("variazione2").setReadOnly(isReadOnly);
		formModel.getFieldMetadata("variazione3").setReadOnly(isReadOnly);
		formModel.getFieldMetadata("variazione4").setReadOnly(isReadOnly);

		formModel.getFieldMetadata("qtaMagazzino")
				.setReadOnly(formModel.getValueModel("formulaTrasformazioneQtaMagazzino").getValue() != null);

		formModel.getFieldMetadata("qta")
				.setReadOnly(formModel.getValueModel("formulaTrasformazione").getValue() != null);

		boolean articoloLibero = ((Boolean) formModel.getValueModel("articoloLibero").getValue());
		formModel.getFieldMetadata("descrizione").setReadOnly(!articoloLibero);
		formModel.getFieldMetadata("descrizioneLingua").setReadOnly(!articoloLibero);
		formModel.getFieldMetadata("unitaMisura").setReadOnly(!articoloLibero);
	}

}
