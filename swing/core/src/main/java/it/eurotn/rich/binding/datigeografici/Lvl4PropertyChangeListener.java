package it.eurotn.rich.binding.datigeografici;

import it.eurotn.panjea.anagrafica.domain.datigeografici.LivelloAmministrativo1;
import it.eurotn.panjea.anagrafica.domain.datigeografici.LivelloAmministrativo2;
import it.eurotn.panjea.anagrafica.domain.datigeografici.LivelloAmministrativo3;
import it.eurotn.panjea.anagrafica.domain.datigeografici.LivelloAmministrativo4;
import it.eurotn.panjea.anagrafica.domain.datigeografici.Nazione;
import it.eurotn.rich.binding.DatiGeograficiBindingForm;

import java.beans.PropertyChangeEvent;

import org.springframework.binding.form.FormModel;

public class Lvl4PropertyChangeListener extends DatiGeograficiPropertyChangeListener {

	/**
	 * Costruttore.
	 * 
	 * @param formPropertyPath
	 *            formPropertyPath
	 * @param formModel
	 *            formModel
	 */
	public Lvl4PropertyChangeListener(final String formPropertyPath, final FormModel formModel) {
		super((formPropertyPath != null ? (formPropertyPath + ".") : ""), formModel);
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if (!formModel.isEnabled()) {
			return;
		}
		LivelloAmministrativo4 livelloAmministrativo4 = (LivelloAmministrativo4) evt.getNewValue();
		// se l'oggetto presente è uguale a quello nuovo non faccio nulla
		LivelloAmministrativo4 livelloAmministrativo4Presente = (LivelloAmministrativo4) evt.getOldValue();
		if (livelloAmministrativo4 != null && livelloAmministrativo4.getId() != null
				&& livelloAmministrativo4Presente != null && livelloAmministrativo4Presente.getId() != null
				&& livelloAmministrativo4Presente.getId().equals(livelloAmministrativo4.getId())) {
			return;
		}

		if (livelloAmministrativo4 != null && livelloAmministrativo4.getId() != null) {

			Nazione nazione = livelloAmministrativo4.getNazione();
			Nazione nazionePresente = (Nazione) formModel.getValueModel(
					formPropertyPath + DatiGeograficiBindingForm.NAZIONE_FORMPROPERTYPATH).getValue();
			if (nazionePresente == null || (nazionePresente != null && !nazione.equals(nazionePresente))) {
				formModel.getValueModel(formPropertyPath + DatiGeograficiBindingForm.NAZIONE_FORMPROPERTYPATH)
						.setValueSilently(nazione,
								listeners.get(formPropertyPath + DatiGeograficiBindingForm.NAZIONE_FORMPROPERTYPATH));
				suddivisioniAmministrativeControlController.updateLivelliVisibility(nazione);
			}
			LivelloAmministrativo3 livelloAmministrativo3 = livelloAmministrativo4 != null ? ((LivelloAmministrativo3) livelloAmministrativo4
					.getSuddivisioneAmministrativaPrecedente()) : null;
			if (livelloAmministrativo3 != null && livelloAmministrativo3.getId() != null
					&& formModel.hasValueModel(formPropertyPath + DatiGeograficiBindingForm.LVL3_FORMPROPERTYPATH)) {
				LivelloAmministrativo3 livelloAmministrativo3Presente = (LivelloAmministrativo3) formModel
						.getValueModel(formPropertyPath + DatiGeograficiBindingForm.LVL3_FORMPROPERTYPATH).getValue();
				if (livelloAmministrativo3Presente == null
						|| (livelloAmministrativo3Presente != null && !livelloAmministrativo3
								.equals(livelloAmministrativo3Presente))) {
					formModel.getValueModel(formPropertyPath + DatiGeograficiBindingForm.LVL3_FORMPROPERTYPATH)
							.setValueSilently(livelloAmministrativo3,
									listeners.get(formPropertyPath + DatiGeograficiBindingForm.LVL3_FORMPROPERTYPATH));
				}
			}

			LivelloAmministrativo2 livelloAmministrativo2 = livelloAmministrativo3 != null ? ((LivelloAmministrativo2) livelloAmministrativo3
					.getSuddivisioneAmministrativaPrecedente()) : null;
			if (livelloAmministrativo2 != null && livelloAmministrativo2.getId() != null
					&& formModel.hasValueModel(formPropertyPath + DatiGeograficiBindingForm.LVL2_FORMPROPERTYPATH)) {
				LivelloAmministrativo2 livelloAmministrativo2Presente = (LivelloAmministrativo2) formModel
						.getValueModel(formPropertyPath + DatiGeograficiBindingForm.LVL2_FORMPROPERTYPATH).getValue();
				if (livelloAmministrativo2Presente == null
						|| (livelloAmministrativo2Presente != null && !livelloAmministrativo2
								.equals(livelloAmministrativo2Presente))) {
					formModel.getValueModel(formPropertyPath + DatiGeograficiBindingForm.LVL2_FORMPROPERTYPATH)
							.setValueSilently(livelloAmministrativo2,
									listeners.get(formPropertyPath + DatiGeograficiBindingForm.LVL2_FORMPROPERTYPATH));
				}
			}

			LivelloAmministrativo1 livelloAmministrativo1 = livelloAmministrativo2 != null ? ((LivelloAmministrativo1) livelloAmministrativo2
					.getSuddivisioneAmministrativaPrecedente()) : null;
			if (livelloAmministrativo1 != null && livelloAmministrativo1.getId() != null
					&& formModel.hasValueModel(formPropertyPath + DatiGeograficiBindingForm.LVL1_FORMPROPERTYPATH)) {
				LivelloAmministrativo1 livelloAmministrativo1Presente = (LivelloAmministrativo1) formModel
						.getValueModel(formPropertyPath + DatiGeograficiBindingForm.LVL1_FORMPROPERTYPATH).getValue();
				if (livelloAmministrativo1Presente == null
						|| (livelloAmministrativo1Presente != null && !livelloAmministrativo1
								.equals(livelloAmministrativo1Presente))) {
					formModel.getValueModel(formPropertyPath + DatiGeograficiBindingForm.LVL1_FORMPROPERTYPATH)
							.setValueSilently(livelloAmministrativo1,
									listeners.get(formPropertyPath + DatiGeograficiBindingForm.LVL1_FORMPROPERTYPATH));
				}
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