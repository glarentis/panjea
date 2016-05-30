package it.eurotn.panjea.auvend.rich.forms;

import it.eurotn.panjea.auvend.domain.TipoDocumentoBaseAuVend.TipoOperazione;
import it.eurotn.rich.form.FormModelPropertyChangeListeners;

import java.beans.PropertyChangeEvent;

import org.springframework.binding.form.FormModel;

public class TipoOperazionePropertyChange implements FormModelPropertyChangeListeners {

	private FormModel formModel;

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		TipoOperazione tipoOperazione = (TipoOperazione) evt.getNewValue();
		formModel.getFieldMetadata("depositoCaricatore").setEnabled(tipoOperazione == TipoOperazione.RECUPERO_GENERICO);
		formModel.getFieldMetadata("causaleAuvend").setEnabled(tipoOperazione == TipoOperazione.RECUPERO_GENERICO);
	}

	@Override
	public void setFormModel(FormModel formModel) {
		this.formModel = formModel;
	}

}
