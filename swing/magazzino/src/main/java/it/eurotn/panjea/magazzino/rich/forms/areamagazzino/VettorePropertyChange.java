package it.eurotn.panjea.magazzino.rich.forms.areamagazzino;

import java.beans.PropertyChangeEvent;

import org.apache.log4j.Logger;
import org.springframework.binding.form.FormModel;
import org.springframework.binding.value.ValueModel;

import it.eurotn.panjea.anagrafica.domain.Entita;
import it.eurotn.panjea.anagrafica.domain.SedeEntita;
import it.eurotn.panjea.anagrafica.domain.Vettore;
import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.anagrafica.rich.bd.IAnagraficaBD;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino;
import it.eurotn.rich.form.FormModelPropertyChangeListeners;

/**
 * PropertyChange incaricato di gestire le azioni successive alla variazione del vettore dalla parte di
 * {@link AreaMagazzino}.
 *
 * @author fattazzo
 *
 */
public class VettorePropertyChange implements FormModelPropertyChangeListeners {

	private static Logger logger = Logger.getLogger(EntitaPropertyChange.class);

	private FormModel formModel;
	private final ValueModel sedeVettoreValueModel;

	private IAnagraficaBD anagraficaBD;

	/**
	 * Costruttore.
	 *
	 * @param sedeVettoreValueModel
	 *            value model
	 */
	public VettorePropertyChange(final ValueModel sedeVettoreValueModel) {
		super();
		this.sedeVettoreValueModel = sedeVettoreValueModel;
	}

	/**
	 * Assegna la sede di magazzino di default per l'argomento entita.
	 *
	 * @param entita
	 *            entita
	 */
	private void assegnaSedeEntita(EntitaLite entita) {
		logger.debug("--> Enter assegnaSedeEntita");

		// controllo della corrispondenza di Entita con la SedeMagazzino esistente
		SedeEntita sedeEntita = getSedeEntitaPrincipale(entita);
		sedeVettoreValueModel.setValueSilently(sedeEntita, this);
		logger.debug("--> Exit assegnaSedeEntita");
	}

	/**
	 * restituisce {@link SedeEntita} principale di Entita.
	 *
	 * @param entitaLite
	 *            entita di riferimento
	 * @return sede
	 */
	private SedeEntita getSedeEntitaPrincipale(EntitaLite entitaLite) {
		logger.debug("--> Enter getSedeEntitaPrincipale");
		Entita entita = new Vettore();
		entita.setId(entitaLite.getId());
		entita.setVersion(entitaLite.getVersion());

		SedeEntita sedeEntita = anagraficaBD.caricaSedePrincipaleEntita(entita);
		return sedeEntita;
	}

	@Override
	public void propertyChange(PropertyChangeEvent event) {
		logger.debug("--> Enter propertyChange");
		if (formModel == null || formModel.isReadOnly()) {
			return;
		}
		if (event.getNewValue() == null) {
			sedeVettoreValueModel.setValue(null);
			return;
		}
		if (event.getNewValue() == sedeVettoreValueModel.getValue()) {
			return;
		}

		EntitaLite entita = (EntitaLite) event.getNewValue();
		assegnaSedeEntita(entita);
		logger.debug("--> Exit propertyChange");
	}

	/**
	 * @param anagraficaBD
	 *            The anagraficaBD to set.
	 */
	public void setAnagraficaBD(IAnagraficaBD anagraficaBD) {
		this.anagraficaBD = anagraficaBD;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * it.eurotn.rich.form.FormModelPropertyChangeListeners#setFormModel(org.springframework.binding.form.FormModel)
	 */
	@Override
	public void setFormModel(FormModel formModel) {
		this.formModel = formModel;
	}
}
