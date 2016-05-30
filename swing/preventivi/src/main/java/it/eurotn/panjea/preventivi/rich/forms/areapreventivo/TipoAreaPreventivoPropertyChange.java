package it.eurotn.panjea.preventivi.rich.forms.areapreventivo;

import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento.TipoEntita;
import it.eurotn.panjea.preventivi.domain.documento.TipoAreaPreventivo;
import it.eurotn.rich.form.FormModelPropertyChangeListeners;

import java.beans.PropertyChangeEvent;
import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.binding.form.FormModel;

public class TipoAreaPreventivoPropertyChange implements FormModelPropertyChangeListeners {

	private static Logger logger = Logger.getLogger(TipoAreaPreventivoPropertyChange.class);

	private FormModel formModel;

	/**
	 * metodo che esegue la copia del valore di dataRegistrazione in dataDocumento.
	 */
	private void copyDataRegistrazioneToDataDocumento() {
		logger.debug("--> copia data registrazione su data documento ");
		if (formModel.isReadOnly()) {
			return;
		}

		Date dataRegistrazione = (Date) formModel.getValueModel("areaPreventivo.dataRegistrazione").getValue();
		formModel.getValueModel("areaPreventivo.documento.dataDocumento").setValueSilently(dataRegistrazione, this);
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		logger.debug("--> Enter propertyChange");
		if (evt.getNewValue() == null) {
			// null value non esegue nulla
			logger.debug("--> Exit propertyChange newValue == null ");
			return;
		}
		TipoAreaPreventivo tipoAreaPreventivo = (TipoAreaPreventivo) formModel.getValueModel(
				"areaPreventivo.tipoAreaPreventivo").getValue();

		if (!formModel.isReadOnly()) {
			formModel.getValueModel("areaPreventivo.documento.entita").setValue(null);
		}

		TipoEntita tipoEntitaDocumento = tipoAreaPreventivo.getTipoDocumento().getTipoEntita();
		boolean entitaDaAbilitare = TipoEntita.CLIENTE == tipoEntitaDocumento
				|| TipoEntita.FORNITORE == tipoEntitaDocumento;
		formModel.getFieldMetadata("areaPreventivo.documento.entita").setEnabled(entitaDaAbilitare);
		logger.debug("--> abilita' entita' " + entitaDaAbilitare);

		if (tipoAreaPreventivo.isDataDocLikeDataReg()) {
			copyDataRegistrazioneToDataDocumento();
		}

		logger.debug("--> Exit propertyChange");
	}

	@Override
	public void setFormModel(FormModel formModel) {
		this.formModel = formModel;
	}

}
