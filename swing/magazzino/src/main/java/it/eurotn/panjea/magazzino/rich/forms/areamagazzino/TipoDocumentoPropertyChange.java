package it.eurotn.panjea.magazzino.rich.forms.areamagazzino;

import java.beans.PropertyChangeEvent;
import java.math.BigDecimal;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.binding.form.FormModel;

import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento;
import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoDocumentoBD;
import it.eurotn.panjea.partite.domain.AreaPartite;
import it.eurotn.panjea.partite.domain.TipoAreaPartita;
import it.eurotn.rich.form.FormModelPropertyChangeListeners;

/**
 * PropertyChange legato alla property {@link TipoDocumento} incaricato di: <br>
 * <li>Entita':cliente o fornitore visualizza componenti di ricerca</li>.
 *
 * @author adriano
 * @version 1.0, 04/set/2008
 */
public class TipoDocumentoPropertyChange implements FormModelPropertyChangeListeners, InitializingBean {

	private static Logger logger = Logger.getLogger(TipoDocumentoPropertyChange.class);

	private FormModel formModel;

	private IMagazzinoDocumentoBD magazzinoDocumentoBD;

	private String tipoDocumentoPropertyPath;

	@Override
	public void afterPropertiesSet() throws Exception {
		org.springframework.util.Assert.notNull(magazzinoDocumentoBD, "magazzinoDocumentoBD non puo' essere null");
	}

	/**
	 * Verifica l'esistenza del {@link TipoAreaPartita} per il {@link TipoDocumento} corrente e rende
	 * visibili/invisibili i controlli legati a {@link AreaPartite}.
	 */
	private void controlloPresenzaAreaPartita() {
		logger.debug("--> Enter controlloPresenzaAreaPartita");
		// recupero tipoDocumento
		TipoDocumento tipoDocumento = (TipoDocumento) formModel.getValueModel(getTipoDocumentoPropertyPath())
				.getValue();
		boolean visible = false;

		if (formModel.isReadOnly()) {
			// verifica della presenza di AreaRate attraverso l'attributo
			// areaPartiteEnabled
			visible = (Boolean) formModel.getValueModel("areaRateEnabled").getValue();
		} else {
			if (tipoDocumento.getId() != null) {
				TipoAreaPartita tipoAreaPartita = magazzinoDocumentoBD
						.caricaTipoAreaPartitaPerTipoDocumento(tipoDocumento);
				// rende visibile i componenti se tipoAreaPartita associato a
				// tipoDocumento non esiste
				logger.debug("--> controllo esistenza di TipoAreaPartita "
						+ ((tipoAreaPartita != null) && (tipoAreaPartita.getId() != null)));
				visible = (tipoAreaPartita != null) && (tipoAreaPartita.getId() != null);
				logger.debug("--> aggiorno il valore di areaRateEnabled ");
				formModel.getValueModel("areaRateEnabled").setValue(visible);
			}
		}
		// enabled/disable fieldmetadata di AreaPartita
		logger.debug("--> abilitazione controlli AreaRate " + visible);
		formModel.getFieldMetadata("areaRate").setEnabled(visible);
		logger.debug("--> Exit controlloPresenzaAreaPartita");
	}

	/**
	 * @return the tipoDocumentoPropertyPath
	 */
	public String getTipoDocumentoPropertyPath() {
		return tipoDocumentoPropertyPath;
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		logger.debug("--> Enter propertyChange");
		if (evt.getNewValue() == null) {
			return;
		}
		controlloPresenzaAreaPartita();
		if (formModel.hasValueModel("importoRateAperte") && !formModel.isReadOnly()) {
			// setto le rateaperte a zero perchè il pannello del fido si resetti
			formModel.getValueModel("importoRateAperte").setValue(BigDecimal.ZERO);
		}
		logger.debug("--> Exit propertyChange");
	}

	@Override
	public void setFormModel(FormModel formModel) {
		this.formModel = formModel;
	}

	/**
	 * @param magazzinoDocumentoBD
	 *            The magazzinoDocumentoBD to set.
	 */
	public void setMagazzinoDocumentoBD(IMagazzinoDocumentoBD magazzinoDocumentoBD) {
		this.magazzinoDocumentoBD = magazzinoDocumentoBD;
	}

	/**
	 * @param tipoDocumentoPropertyPath
	 *            the tipoDocumentoPropertyPath to set
	 */
	public void setTipoDocumentoPropertyPath(String tipoDocumentoPropertyPath) {
		this.tipoDocumentoPropertyPath = tipoDocumentoPropertyPath;
	}

}
