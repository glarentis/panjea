/**
 * 
 */
package it.eurotn.panjea.partite.rich.editors.ricercarate;

import it.eurotn.panjea.partite.domain.TipoAreaPartita.TipoPartita;
import it.eurotn.panjea.partite.util.ParametriRicercaRate;
import it.eurotn.rich.form.FormModelPropertyChangeListeners;

import java.beans.PropertyChangeEvent;

import org.apache.log4j.Logger;
import org.springframework.binding.form.FormModel;

/**
 * Property change legato alla proprieta' tipoPartita del form model di {@link ParametriRicercaRateForm} che aggiorna il
 * valore del value model del campo entita' dato che e' il domain object {@link ParametriRicercaRate} che azzera il
 * campo secondo il tipo partita scelto.
 * 
 * @author Leonardo
 */
public class TipoPartitaPropertyChange implements FormModelPropertyChangeListeners {

	private static Logger logger = Logger.getLogger(TipoPartitaPropertyChange.class);
	private FormModel formModel = null;

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt == null || (evt != null && evt.getNewValue() == null)) {
			logger.debug("--> Exit propertyChange");
			return;
		}
		TipoPartita tipoPartita = (TipoPartita) evt.getNewValue();
		logger.debug("--> selezionato TipoPartita " + tipoPartita);
		// aggiorno il valore del campo entita'
		formModel.getValueModel("entita").setValue(formModel.getValueModel("entita").getValue());
	}

	@Override
	public void setFormModel(FormModel formModel) {
		this.formModel = formModel;
	}

}
