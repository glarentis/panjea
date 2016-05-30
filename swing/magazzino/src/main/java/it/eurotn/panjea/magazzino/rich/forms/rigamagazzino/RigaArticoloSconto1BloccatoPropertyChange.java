package it.eurotn.panjea.magazzino.rich.forms.rigamagazzino;

import it.eurotn.rich.form.FormModelPropertyChangeListeners;

import java.beans.PropertyChangeEvent;

import org.springframework.binding.form.FormModel;

public class RigaArticoloSconto1BloccatoPropertyChange implements FormModelPropertyChangeListeners {

	private FormModel formModel;

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getNewValue() != null && evt.getNewValue().equals(evt.getOldValue())) {
			return;
		}
		if (formModel == null) {
			return;
		}
		Boolean sconto1Bloccato = (Boolean) evt.getNewValue();

		formModel.getFieldMetadata("variazione1").setReadOnly(sconto1Bloccato);
	}

	@Override
	public void setFormModel(FormModel formModel) {
		this.formModel = formModel;
	}

}
