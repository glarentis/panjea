/**
 * 
 */
package it.eurotn.panjea.contabilita.rich.editors.tipoareacontabile;

import it.eurotn.panjea.contabilita.domain.RegistroIva;
import it.eurotn.panjea.contabilita.domain.RegistroIva.TipoRegistro;
import it.eurotn.rich.form.FormModelPropertyChangeListeners;

import java.beans.PropertyChangeEvent;

import org.apache.log4j.Logger;
import org.springframework.binding.form.FormModel;

/**
 * Form value changed che verifica il registro iva e a seconda del valore modifica la tipologia corrispettivo legata,
 * modifica inoltre la visibilita' del componente tipologia corrispettivo
 * 
 * @author Leonardo
 */
public class RegistroIvaPropertyChange implements FormModelPropertyChangeListeners {

	static Logger logger = Logger.getLogger(RegistroIvaPropertyChange.class);
	private FormModel formModel = null;

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		logger.debug("--> Enter propertyChange");
		logger.debug("--> registroIva value change " + evt.getPropertyName() + ":" + evt.getNewValue());
		// RegistroIva registroIva = (RegistroIva) evt.getNewValue();
		RegistroIva registroIva = (RegistroIva) formModel.getValueModel("registroIva").getValue();
		// controllo il registro iva se diverso da null (un nuovo oggetto che non ha id settato)
		if (registroIva != null && registroIva.getId() != null && registroIva.getId().intValue() != -1) {
			boolean enabled = false;
			if (registroIva != null && registroIva.getTipoRegistro() != null
					&& registroIva.getTipoRegistro().compareTo(TipoRegistro.CORRISPETTIVO) == 0) {
				enabled = true;
			}
			formModel.getFieldMetadata("tipologiaCorrispettivo").setEnabled(enabled);

			if (registroIva.getTipoRegistro().compareTo(TipoRegistro.CORRISPETTIVO) != 0) {
				// resetto la tipologia corrispettivo dato che cambio
				// corrispettivo quando il registro e' corrispettivo
				formModel.getValueModel("tipologiaCorrispettivo").setValue(null);
			}
		}
		logger.debug("--> Exit propertyChange");
	}

	@Override
	public void setFormModel(FormModel formModel) {
		this.formModel = formModel;
	}

}
