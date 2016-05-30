package it.eurotn.panjea.magazzino.rich.forms.areamagazzino;

import it.eurotn.panjea.pagamenti.domain.CodicePagamento;
import it.eurotn.rich.form.FormModelPropertyChangeListeners;

import java.beans.PropertyChangeEvent;
import java.math.BigDecimal;

import org.springframework.binding.form.FormModel;

public class RaggruppamentoBolleProperyChange implements FormModelPropertyChangeListeners {

	private FormModel formModel;

	@Override
	public void propertyChange(PropertyChangeEvent evt) {

		if (evt.getNewValue().equals(evt.getOldValue())) {
			return;
		}

		Boolean raggrupBolle = (Boolean) formModel.getValueModel("areaMagazzino.raggruppamentoBolle").getValue();

		if (!raggrupBolle) {
			speseIncassoEnable(true);
			Boolean addSpese = (Boolean) formModel.getValueModel("areaMagazzino.addebitoSpeseIncasso").getValue();

			CodicePagamento codicePagamento = (CodicePagamento) formModel.getValueModel("areaRate.codicePagamento")
					.getValue();

			BigDecimal importoSpese = BigDecimal.ZERO;

			// se il codice pagamento non è nullo ed è richiesto l'addebito spese setto l'importo dell'addebito
			// indicato dal pagamento
			if (codicePagamento != null && codicePagamento.getId() != null && addSpese) {

				if (codicePagamento.getImportoSpese() != null) {
					importoSpese = codicePagamento.getImportoSpese();
				}
			} else {
				speseIncassoEnable(false);
			}

			if (!formModel.isReadOnly()) {
				formModel.getValueModel("areaRate.speseIncasso").setValue(importoSpese);
			}
		} else {
			speseIncassoEnable(false);
			if (!formModel.isReadOnly()) {
				formModel.getValueModel("areaRate.speseIncasso").setValue(BigDecimal.ZERO);
			}
		}
	}

	@Override
	public void setFormModel(FormModel formModel) {
		this.formModel = formModel;
	}

	/**
	 * Abilita o disabilita le spese di incasso.
	 * 
	 * @param enabled
	 *            <code>true</code> per abilitare le spese di incasso
	 */
	private void speseIncassoEnable(boolean enabled) {
		formModel.getFieldMetadata("areaRate.speseIncasso").setEnabled(enabled);
	}

}