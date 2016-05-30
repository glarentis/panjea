package it.eurotn.panjea.magazzino.rich.forms.tipoareamagazzinoform;

import it.eurotn.rich.form.FormModelPropertyChangeListeners;

import java.beans.PropertyChangeEvent;

import org.springframework.binding.form.FormModel;

public class RichiestaMezzoTrasportoPropertyChange implements FormModelPropertyChangeListeners {

	private FormModel formModel;

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if (formModel != null) {
			formModel.getValueModel("richiestaMezzoTrasportoObbligatorio").setValue(
					formModel.getValueModel("richiestaMezzoTrasportoObbligatorio").getValue());
			formModel.getFieldMetadata("richiestaMezzoTrasportoObbligatorio").setEnabled(
					(boolean) formModel.getValueModel("richiestaMezzoTrasporto").getValue());
		}
	}

	@Override
	public void setFormModel(FormModel formModel) {
		this.formModel = formModel;
	}

}
