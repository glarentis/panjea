/**
 * 
 */
package it.eurotn.panjea.contabilita.rich.editors.tipoareacontabile;

import it.eurotn.panjea.contabilita.domain.TipoAreaContabile.GestioneIva;
import it.eurotn.rich.form.FormModelPropertyChangeListeners;

import java.beans.PropertyChangeEvent;

import org.apache.log4j.Logger;
import org.springframework.binding.form.FormModel;

/**
 * Property change associato al valore di gestione iva, se INTRA o ART17 devo visualizzare registro iva e protocollo
 * collegati, inoltre devo filtrare registro solo per tipoRegistro ACQUISTO e registro iva collegato per tipoRegistro
 * VENDITA. Per gestione iva NORMALE invece i parametri collegati non sono visibili e non c'e' filtro sul campo registro
 * iva.
 * 
 * @author Leonardo
 */
public class GestioneIvaPropertyChange implements FormModelPropertyChangeListeners {

	static Logger logger = Logger.getLogger(GestioneIvaPropertyChange.class);
	private FormModel formModel = null;

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		logger.debug("--> Enter propertyChange");
		if (evt == null || (evt != null && evt.getNewValue() == null)) {
			logger.debug("--> Exit propertyChange");
			return;
		}
		GestioneIva gestioneIva = (GestioneIva) formModel.getValueModel("gestioneIva").getValue();// evt.getNewValue();
		logger.debug("--> GestioneIva corrente " + gestioneIva);

		boolean enabled = false;
		if (!GestioneIva.NORMALE.equals(gestioneIva)) {
			enabled = true;
		}
		logger.debug("--> registroProtocolloCollegato enabled? "
				+ formModel.getFieldMetadata("registroProtocolloCollegato").isEnabled());
		// if (enabled == formModel.getFieldMetadata("registroProtocolloCollegato").isEnabled()) {
		// // HACK forzo il set di FieldMetadata con !enabled per forzarne nuovamente il propertyChange al successivo
		// set
		// formModel.getFieldMetadata("registroProtocolloCollegato").setEnabled(!enabled);
		// }
		formModel.getFieldMetadata("registroIvaCollegato").setEnabled(enabled);
		formModel.getFieldMetadata("registroProtocolloCollegato").setEnabled(enabled);

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
			this.formModel.removePropertyChangeListener(this);
		}
		this.formModel = formModel;
	}

}
