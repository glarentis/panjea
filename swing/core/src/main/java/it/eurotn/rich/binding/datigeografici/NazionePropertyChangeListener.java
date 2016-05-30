package it.eurotn.rich.binding.datigeografici;

import it.eurotn.panjea.anagrafica.domain.datigeografici.Nazione;
import it.eurotn.rich.binding.DatiGeograficiBindingForm;

import java.beans.PropertyChangeEvent;

import org.springframework.binding.form.FormModel;

public class NazionePropertyChangeListener extends DatiGeograficiPropertyChangeListener {

	/**
	 * Costruttore.
	 * 
	 * @param formPropertyPath
	 *            formPropertyPath
	 * @param formModel
	 *            formModel
	 */
	public NazionePropertyChangeListener(final String formPropertyPath, final FormModel formModel) {
		super((formPropertyPath != null ? (formPropertyPath + ".") : ""), formModel);
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if (!formModel.isEnabled()) {
			return;
		}
		Nazione nazione = (Nazione) evt.getNewValue();

		// se l'oggetto presente è uguale a quello nuovo non faccio nulla
		Nazione nazionePresente = (Nazione) evt.getOldValue();
		if (nazione != null && nazione.getId() != null && nazionePresente != null && nazionePresente.getId() != null
				&& nazionePresente.getId().equals(nazione.getId())) {
			return;
		}

		suddivisioniAmministrativeControlController.showControls();

		suddivisioniAmministrativeControlController.updateLivelliVisibility(nazione);

		if (formModel.hasValueModel(formPropertyPath + DatiGeograficiBindingForm.LVL1_FORMPROPERTYPATH)) {
			formModel.getValueModel(formPropertyPath + DatiGeograficiBindingForm.LVL1_FORMPROPERTYPATH)
					.setValueSilently(null,
							listeners.get(formPropertyPath + DatiGeograficiBindingForm.LVL1_FORMPROPERTYPATH));
		}
		if (formModel.hasValueModel(formPropertyPath + DatiGeograficiBindingForm.LVL2_FORMPROPERTYPATH)) {
			formModel.getValueModel(formPropertyPath + DatiGeograficiBindingForm.LVL2_FORMPROPERTYPATH)
					.setValueSilently(null,
							listeners.get(formPropertyPath + DatiGeograficiBindingForm.LVL2_FORMPROPERTYPATH));
		}
		if (formModel.hasValueModel(formPropertyPath + DatiGeograficiBindingForm.LVL3_FORMPROPERTYPATH)) {
			formModel.getValueModel(formPropertyPath + DatiGeograficiBindingForm.LVL3_FORMPROPERTYPATH)
					.setValueSilently(null,
							listeners.get(formPropertyPath + DatiGeograficiBindingForm.LVL3_FORMPROPERTYPATH));
		}
		if (formModel.hasValueModel(formPropertyPath + DatiGeograficiBindingForm.LVL4_FORMPROPERTYPATH)) {
			formModel.getValueModel(formPropertyPath + DatiGeograficiBindingForm.LVL4_FORMPROPERTYPATH)
					.setValueSilently(null,
							listeners.get(formPropertyPath + DatiGeograficiBindingForm.LVL4_FORMPROPERTYPATH));
		}
		if (formModel.hasValueModel(formPropertyPath + DatiGeograficiBindingForm.LOCALITA_FORMPROPERTYPATH)) {
			formModel.getValueModel(formPropertyPath + DatiGeograficiBindingForm.LOCALITA_FORMPROPERTYPATH)
					.setValueSilently(null,
							listeners.get(formPropertyPath + DatiGeograficiBindingForm.LOCALITA_FORMPROPERTYPATH));
		}
		if (formModel.hasValueModel(formPropertyPath + DatiGeograficiBindingForm.CAP_FORMPROPERTYPATH)) {
			formModel.getValueModel(formPropertyPath + DatiGeograficiBindingForm.CAP_FORMPROPERTYPATH)
					.setValueSilently(null,
							listeners.get(formPropertyPath + DatiGeograficiBindingForm.CAP_FORMPROPERTYPATH));
		}
	}

}