package it.eurotn.panjea.magazzino.rich.forms.areamagazzino;

import it.eurotn.panjea.magazzino.domain.documento.TipoAreaMagazzino;
import it.eurotn.panjea.pagamenti.domain.CodicePagamento;
import it.eurotn.rich.form.FormModelPropertyChangeListeners;

import java.beans.PropertyChangeEvent;
import java.math.BigDecimal;

import org.apache.log4j.Logger;
import org.springframework.beans.NullValueInNestedPathException;
import org.springframework.binding.form.FormModel;

public class AddebitoSpesePropertyChange implements FormModelPropertyChangeListeners {

	private static Logger logger = Logger.getLogger(AddebitoSpesePropertyChange.class);
	private FormModel formModel;

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getNewValue() != null && evt.getOldValue() != null && evt.getNewValue().equals(evt.getOldValue())) {
			return;
		}

		Boolean addSpese = (Boolean) formModel.getValueModel("areaMagazzino.addebitoSpeseIncasso").getValue();

		CodicePagamento codicePagamento = null;
		try {
			codicePagamento = (CodicePagamento) formModel.getValueModel("areaRate.codicePagamento").getValue();
		} catch (NullValueInNestedPathException e) {
			// Da una mail è arrivato l'errore NullValueInNestedPathException.
			// Non si capisce come ricostruire l'errore. Cmq loggo e non faccio
			// vedere al cliente
			logger.error("-->EG.controllare  ", e);
		}

		TipoAreaMagazzino tipoAreaMagazzino = (TipoAreaMagazzino) formModel.getValueModel(
				"areaMagazzino.tipoAreaMagazzino").getValue();

		// nascondo o visualizzo il flag raggruppamento bolle in base
		// all'esistenza o meno del documento di
		// destinazione sul tipo area magazzino.
		boolean speseIncassoEnable = false;
		BigDecimal importoSpese = BigDecimal.ZERO;
		if (tipoAreaMagazzino != null) {
			if (tipoAreaMagazzino.getTipoDocumentoPerFatturazione() == null) {
				if (addSpese && codicePagamento != null) {
					speseIncassoEnable = true;
					if (codicePagamento.getImportoSpese() != null) {
						importoSpese = codicePagamento.getImportoSpese();
					}
				}
			} else {
				Boolean raggruppamentoBolle = (Boolean) formModel.getValueModel("areaMagazzino.raggruppamentoBolle")
						.getValue();

				if (!raggruppamentoBolle && addSpese && codicePagamento != null) {
					speseIncassoEnable = true;
					if (codicePagamento.getImportoSpese() != null) {
						importoSpese = codicePagamento.getImportoSpese();
					}
				}
			}
		}

		speseIncassoEnable(speseIncassoEnable);

		if (!formModel.isReadOnly()) {
			formModel.getValueModel("areaRate.speseIncasso").setValue(importoSpese);
		}

	}

	@Override
	public void setFormModel(FormModel formModel) {
		this.formModel = formModel;
	}

	/**
	 * abilita il filed delle spese incasso in base al parametro.
	 * 
	 * @param enabled
	 *            true per abilitare il filed
	 */
	private void speseIncassoEnable(boolean enabled) {
		if (!formModel.isReadOnly()) {
			formModel.getValueModel("areaRate.speseIncasso").setValue(BigDecimal.ZERO);
		}
		formModel.getFieldMetadata("areaRate.speseIncasso").setEnabled(enabled);
	}

}