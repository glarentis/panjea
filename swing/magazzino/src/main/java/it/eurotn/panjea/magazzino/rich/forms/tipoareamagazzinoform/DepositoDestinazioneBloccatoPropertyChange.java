package it.eurotn.panjea.magazzino.rich.forms.tipoareamagazzinoform;

import it.eurotn.panjea.anagrafica.domain.lite.DepositoLite;
import it.eurotn.panjea.magazzino.rich.editors.tipoareamagazzino.TipoAreaMagazzinoPage;
import it.eurotn.rich.form.FormModelPropertyChangeListeners;

import java.beans.PropertyChangeEvent;

import org.springframework.binding.form.FormModel;

/**
 * Chiamato quando viene modificato il flag depositoDestinazioneBloccato: <li>True: abilito la combo per la scelta del
 * deposito di destinazione .Questo deve essere obbligatorio</li> <li>False: disabilito il deposito di destinazione e lo
 * setto a empty instance di {@link DepositoLite}</li>. <br>
 * 
 * @author giangi
 * @see {@link TipoAreaMagazzinoForm}, {@link TipoAreaMagazzinoPage}
 */
public class DepositoDestinazioneBloccatoPropertyChange implements FormModelPropertyChangeListeners {

	private FormModel formModel;

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		boolean abilitato = new Boolean(evt.getNewValue().toString()).booleanValue();
		if (!abilitato) {
			formModel.getValueModel("depositoDestinazione").setValue(null);
		}
		formModel.getFieldMetadata("depositoDestinazione").setEnabled(abilitato);
	}

	@Override
	public void setFormModel(FormModel formModel) {
		this.formModel = formModel;
	}

}
