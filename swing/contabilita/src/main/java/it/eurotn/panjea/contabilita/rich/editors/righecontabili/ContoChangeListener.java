package it.eurotn.panjea.contabilita.rich.editors.righecontabili;

import it.eurotn.panjea.contabilita.domain.RigaCentroCosto;
import it.eurotn.panjea.contabilita.domain.RigaContabile;
import it.eurotn.panjea.contabilita.domain.SottoConto;
import it.eurotn.rich.form.FormModelPropertyChangeListeners;

import java.beans.PropertyChangeEvent;
import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.binding.form.FormModel;

/**
 * Property change listener per rendere esclusiva la selezione dei campi dare ed avere<br/>
 * Cancellando il valore inserito in uno dei due entrambi i campi vengono abilitati nuovamente.
 * 
 * @author Leonardo
 * 
 */
public class ContoChangeListener implements FormModelPropertyChangeListeners {

	private static Logger logger = Logger.getLogger(ContoChangeListener.class);
	private FormModel formModel = null;

	/**
	 * viene richiamato sul cambio del conto avere o dare.
	 * 
	 * @param evt
	 *            evento del propertyChange
	 */
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		logger.debug("--> Enter propertyChange");
		if (evt == null) {
			logger.debug("--> Exit propertyChange");
			return;
		}
		if (formModel.isReadOnly()) {
			return;
		}

		SottoConto sottoconto = (SottoConto) evt.getNewValue();
		RigaContabile r = (RigaContabile) formModel.getFormObject();

		// Seleziono i centri di costo di default o cancello se il conto non li prevede
		if (sottoconto == null || (sottoconto != null && !sottoconto.isSoggettoCentroCosto())) {
			Set<RigaCentroCosto> righeCentroCosto = new HashSet<RigaCentroCosto>();
			formModel.getValueModel("righeCentroCosto").setValue(righeCentroCosto);
		}

		if (sottoconto != null && sottoconto.isSoggettoCentroCosto() && sottoconto.getCentroCosto() != null) {
			// Se ho il centro di costo di default creo la riga del centro di costo.
			Set<RigaCentroCosto> righeCentroCosto = new HashSet<RigaCentroCosto>();
			righeCentroCosto.clear();
			RigaCentroCosto riga = new RigaCentroCosto();
			riga.setRigaContabile(r);
			riga.setCentroCosto(r.getConto().getCentroCosto());
			riga.setImporto(r.getImporto());
			righeCentroCosto.add(riga);
			formModel.getValueModel("righeCentroCosto").setValue(righeCentroCosto);
		}

		logger.debug("--> Exit propertyChange");
	}

	@Override
	public void setFormModel(FormModel formModel) {
		this.formModel = formModel;
	}
}
