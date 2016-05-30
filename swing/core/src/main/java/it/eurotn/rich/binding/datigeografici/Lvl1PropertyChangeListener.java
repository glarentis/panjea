package it.eurotn.rich.binding.datigeografici;

import it.eurotn.panjea.anagrafica.domain.datigeografici.LivelloAmministrativo1;
import it.eurotn.panjea.anagrafica.domain.datigeografici.Nazione;
import it.eurotn.rich.binding.DatiGeograficiBindingForm;

import java.beans.PropertyChangeEvent;

import org.springframework.binding.form.FormModel;

public class Lvl1PropertyChangeListener extends DatiGeograficiPropertyChangeListener {

	/**
	 * Costruttore.
	 * 
	 * @param formPropertyPath
	 *            formPropertyPath
	 * @param formModel
	 *            formModel
	 */
	public Lvl1PropertyChangeListener(final String formPropertyPath, final FormModel formModel) {
		super((formPropertyPath != null ? (formPropertyPath + ".") : ""), formModel);
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if (!formModel.isEnabled()) {
			return;
		}
		LivelloAmministrativo1 livelloAmministrativo1 = (LivelloAmministrativo1) evt.getNewValue();
		// se l'oggetto presente è uguale a quello nuovo non faccio nulla
		LivelloAmministrativo1 livelloAmministrativo1Presente = (LivelloAmministrativo1) evt.getOldValue();
		if (livelloAmministrativo1 != null && livelloAmministrativo1.getId() != null
				&& livelloAmministrativo1Presente != null && livelloAmministrativo1Presente.getId() != null
				&& livelloAmministrativo1Presente.getId().equals(livelloAmministrativo1.getId())) {
			return;
		}

		if (livelloAmministrativo1 != null && livelloAmministrativo1.getId() != null) {

			Nazione nazione = livelloAmministrativo1.getNazione();
			Nazione nazionePresente = (Nazione) formModel.getValueModel(
					formPropertyPath + DatiGeograficiBindingForm.NAZIONE_FORMPROPERTYPATH).getValue();
			if (nazionePresente == null || (nazionePresente != null && !nazione.equals(nazionePresente))) {
				formModel.getValueModel(formPropertyPath + DatiGeograficiBindingForm.NAZIONE_FORMPROPERTYPATH)
						.setValueSilently(nazione,
								listeners.get(formPropertyPath + DatiGeograficiBindingForm.NAZIONE_FORMPROPERTYPATH));
				suddivisioniAmministrativeControlController.updateLivelliVisibility(nazione);
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
		} else {
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

}