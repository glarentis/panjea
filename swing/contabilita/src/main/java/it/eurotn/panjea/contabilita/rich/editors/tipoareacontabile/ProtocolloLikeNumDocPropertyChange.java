package it.eurotn.panjea.contabilita.rich.editors.tipoareacontabile;

import it.eurotn.rich.form.FormModelPropertyChangeListeners;

import java.beans.PropertyChangeEvent;

import org.apache.log4j.Logger;
import org.springframework.binding.form.FormModel;

public class ProtocolloLikeNumDocPropertyChange implements FormModelPropertyChangeListeners {

	static Logger logger = Logger.getLogger(ProtocolloLikeNumDocPropertyChange.class);

	private FormModel formModel = null;

	@Override
	public void propertyChange(PropertyChangeEvent evt) {

		if (evt == null || (evt != null && evt.getNewValue() == null)) {
			logger.debug("--> Exit propertyChange");
			return;
		}

		Boolean protocolloLikeNumDoc = (Boolean) this.formModel.getValueModel("protocolloLikeNumDoc").getValue();

		if (protocolloLikeNumDoc == true) {
			this.formModel.getValueModel("registroProtocollo").setValue(null);
		}

		this.formModel.getFieldMetadata("registroProtocollo").setEnabled(!protocolloLikeNumDoc);
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
