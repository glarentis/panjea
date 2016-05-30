package it.eurotn.panjea.magazzino.rich.forms.areamagazzino;

import it.eurotn.panjea.magazzino.domain.documento.TipoAreaMagazzino;
import it.eurotn.panjea.pagamenti.domain.CodicePagamento;
import it.eurotn.rich.form.FormModelPropertyChangeListeners;

import java.beans.PropertyChangeEvent;
import java.math.BigDecimal;

import org.springframework.binding.form.FormModel;

public class CodicePagamentoPropertyChange implements FormModelPropertyChangeListeners {

	private FormModel formModel;

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if (formModel.isReadOnly()
				|| (evt.getNewValue() != null && evt.getOldValue() != null && evt.getNewValue().equals(
						evt.getOldValue()))) {
			return;
		}
		// devo chiamare il set del campo spese incasso solo se l'area partite
		// e' presente per l'area magazzino corrente
		if ((Boolean) formModel.getValueModel("areaRateEnabled").getValue()) {
			CodicePagamento codicePagamento = (CodicePagamento) evt.getNewValue();
			BigDecimal percSconto = (codicePagamento == null) ? BigDecimal.ZERO : codicePagamento
					.getPercentualeSconto();
			Integer giorniLimite = (codicePagamento == null) ? null : codicePagamento.getGiorniLimite();
			BigDecimal impSpese = (codicePagamento == null) ? BigDecimal.ZERO : codicePagamento.getImportoSpese();
			formModel.getValueModel("areaRate.percentualeSconto").setValue(percSconto);
			formModel.getValueModel("areaRate.giorniLimite").setValue(giorniLimite);

			TipoAreaMagazzino tipoAreaMagazzino = (TipoAreaMagazzino) formModel.getValueModel(
					"areaMagazzino.tipoAreaMagazzino").getValue();
			Boolean addSpese = (Boolean) formModel.getValueModel("areaMagazzino.addebitoSpeseIncasso").getValue();

			formModel.getValueModel("areaRate.speseIncasso").setValue(null);
			if (tipoAreaMagazzino != null && tipoAreaMagazzino.getTipoDocumentoPerFatturazione() == null) {
				if (addSpese && codicePagamento != null && codicePagamento.getImportoSpese() != null) {
					formModel.getValueModel("areaRate.speseIncasso").setValue(impSpese);
				}
			}
		}

	}

	@Override
	public void setFormModel(FormModel formModel) {
		this.formModel = formModel;
	}

}
