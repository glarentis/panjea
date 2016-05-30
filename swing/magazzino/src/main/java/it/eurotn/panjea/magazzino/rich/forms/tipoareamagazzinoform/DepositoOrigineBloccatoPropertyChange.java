package it.eurotn.panjea.magazzino.rich.forms.tipoareamagazzinoform;

import it.eurotn.panjea.anagrafica.domain.lite.DepositoLite;
import it.eurotn.panjea.magazzino.rich.editors.tipoareamagazzino.TipoAreaMagazzinoPage;
import it.eurotn.rich.form.FormModelPropertyChangeListeners;

import java.beans.PropertyChangeEvent;

import org.springframework.binding.form.FormModel;

/**
 * Chiamato quando viene modificato il flag depositoOrigineBloccato: <li>True: abilito la combo per la scelta del
 * deposito di origine .Questo deve essere obbligatorio</li> <li>False: disabilito il deposito di origine e lo setto a
 * empty instance di {@link DepositoLite}</li>. <br>
 * 
 * @author giangi
 * @see {@link TipoAreaMagazzinoForm}, {@link TipoAreaMagazzinoPage}
 */
public class DepositoOrigineBloccatoPropertyChange implements FormModelPropertyChangeListeners {

	private FormModel formModel;

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		// NPE su form model o value model di deposito origine, si presenta raramente quando creo un nuovo tipo
		// documento
		if (formModel != null && formModel.getValueModel("depositoOrigine") != null) {
			boolean abilitato = new Boolean(evt.getNewValue().toString()).booleanValue();
			if (!abilitato) {
				formModel.getValueModel("depositoOrigine").setValue(null);
			}
			formModel.getFieldMetadata("depositoOrigine").setEnabled(abilitato);
		}
	}

	@Override
	public void setFormModel(FormModel formModel) {
		this.formModel = formModel;
	}

}
