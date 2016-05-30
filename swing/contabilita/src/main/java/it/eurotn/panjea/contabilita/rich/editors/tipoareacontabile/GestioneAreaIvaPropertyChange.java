/**
 *
 */
package it.eurotn.panjea.contabilita.rich.editors.tipoareacontabile;

import it.eurotn.rich.form.FormModelPropertyChangeListeners;

import java.beans.PropertyChangeEvent;

import org.apache.log4j.Logger;
import org.springframework.binding.form.FormModel;

/**
 * Property change associato al valore di tipoDocumento.righeIvaEnable per visualizzare i dati dell'area iva.
 * 
 * @author Leonardo
 */
public class GestioneAreaIvaPropertyChange implements FormModelPropertyChangeListeners {

	private FormModel formModel = null;

	private static Logger logger = Logger.getLogger(GestioneAreaIvaPropertyChange.class);

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		logger.debug("--> Enter propertyChange");
		if (evt == null || (evt != null && evt.getNewValue() == null)) {
			logger.debug("--> Exit propertyChange");
			return;
		}

		boolean areaIvaAttiva = ((Boolean) formModel.getValueModel("tipoDocumento.righeIvaEnable").getValue())
				.booleanValue();
		logger.debug("--> area iva attiva " + areaIvaAttiva);
		formModel.getFieldMetadata("stampaRegistroIva").setEnabled(areaIvaAttiva);
		formModel.getFieldMetadata("registroIva").setEnabled(areaIvaAttiva);
		formModel.getFieldMetadata("registroIvaCollegato").setEnabled(areaIvaAttiva);
		formModel.getFieldMetadata("registroProtocollo").setEnabled(areaIvaAttiva);
		formModel.getFieldMetadata("registroProtocolloCollegato").setEnabled(areaIvaAttiva);
		formModel.getFieldMetadata("tipologiaCorrispettivo").setEnabled(areaIvaAttiva);
		formModel.getFieldMetadata("validazioneAreaIvaAutomatica").setEnabled(areaIvaAttiva);
		// il fieldMetadata di gestioneIva viene settato per ultimo poiche' e' quello a cui e' legato
		// il propertyChange del Form per nascondere i componenti
		formModel.getFieldMetadata("gestioneIva").setEnabled(areaIvaAttiva);
		logger.debug("--> Exit propertyChange");
	}

	@Override
	public void setFormModel(FormModel formModel) {
		// se arriva il form model null significa che deregistro il propertychange lo rimuovo quindi anche dal form
		// model
		if (formModel != null) {
			// HACK registra questo propertyChange anche al formModel
			formModel.addPropertyChangeListener(this);
		} else {
			// NPE Mail: non sono riuscito a riprodurre l'errore, eseguo semplicemente il controllo per null
			if (this.formModel != null) {
				this.formModel.removePropertyChangeListener(this);
			}
		}
		this.formModel = formModel;
	}

}
