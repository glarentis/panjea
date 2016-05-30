package it.eurotn.panjea.magazzino.rich.forms.verificaprezzo;

import it.eurotn.panjea.anagrafica.domain.Cliente;
import it.eurotn.panjea.anagrafica.domain.Fornitore;
import it.eurotn.panjea.anagrafica.domain.SedeEntita;
import it.eurotn.panjea.anagrafica.domain.lite.ClienteLite;
import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.anagrafica.domain.lite.FornitoreLite;
import it.eurotn.panjea.anagrafica.rich.bd.IAnagraficaBD;
import it.eurotn.rich.form.FormModelPropertyChangeListeners;

import java.beans.PropertyChangeEvent;

import org.springframework.binding.form.FormModel;

public class EntitaPropertyChange implements FormModelPropertyChangeListeners {

	private FormModel formModel;

	private IAnagraficaBD anagraficaBD;

	/**
	 * @return the anagraficaBD
	 */
	public IAnagraficaBD getAnagraficaBD() {
		return anagraficaBD;
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		SedeEntita sedeEntita = null;

		EntitaLite entitaLite = (EntitaLite) evt.getNewValue();

		if (entitaLite != null && entitaLite.getTipo().equals(ClienteLite.TIPO)) {
			Cliente cliente = new Cliente();
			cliente.setId(entitaLite.getId());
			if (entitaLite.getId() != null) {
				sedeEntita = anagraficaBD.caricaSedePrincipaleEntita(cliente);
			}
		} else if (entitaLite != null && entitaLite.getTipo().equals(FornitoreLite.TIPO)) {
			Fornitore fornitore = new Fornitore();
			fornitore.setId(entitaLite.getId());
			if (entitaLite.getId() != null) {
				sedeEntita = anagraficaBD.caricaSedePrincipaleEntita(fornitore);
			}
		}
		this.formModel.getValueModel("sedeEntita").setValue(sedeEntita);
	}

	/**
	 * @param anagraficaBD
	 *            the anagraficaBD to set
	 */
	public void setAnagraficaBD(IAnagraficaBD anagraficaBD) {
		this.anagraficaBD = anagraficaBD;
	}

	@Override
	public void setFormModel(FormModel formModel) {
		this.formModel = formModel;
	}

}
