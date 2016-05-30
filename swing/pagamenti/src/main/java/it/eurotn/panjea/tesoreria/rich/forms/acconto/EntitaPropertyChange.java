/**
 *
 */
package it.eurotn.panjea.tesoreria.rich.forms.acconto;

import it.eurotn.panjea.anagrafica.domain.Cliente;
import it.eurotn.panjea.anagrafica.domain.Entita;
import it.eurotn.panjea.anagrafica.domain.Fornitore;
import it.eurotn.panjea.anagrafica.domain.SedeEntita;
import it.eurotn.panjea.anagrafica.domain.lite.ClienteLite;
import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.anagrafica.domain.lite.FornitoreLite;
import it.eurotn.panjea.anagrafica.rich.bd.IAnagraficaBD;
import it.eurotn.rich.form.FormModelPropertyChangeListeners;

import java.beans.PropertyChangeEvent;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.binding.form.FormModel;

/**
 * @author Leonardo
 */
public class EntitaPropertyChange implements FormModelPropertyChangeListeners, InitializingBean {

	private static Logger logger = Logger.getLogger(EntitaPropertyChange.class);
	private FormModel formModel = null;

	private IAnagraficaBD anagraficaBD;

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	/**
	 * Assegna la sede di magazzino di default per l'argomento entita.
	 * 
	 * @param entita
	 *            entità alla quale assegnare la sede
	 */
	private void assegnaSedeEntita(EntitaLite entita) {
		logger.debug("--> Enter assegnaSedeEntita");
		SedeEntita sedeEntita = getSedeEntitaPrincipale(entita);
		formModel.getValueModel("documento.sedeEntita").setValue(sedeEntita);
		logger.debug("--> Exit assegnaSedeEntita");
	}

	/**
	 * @return Returns the anagraficaBD.
	 */
	public IAnagraficaBD getAnagraficaBD() {
		return anagraficaBD;
	}

	/**
	 * restituisce {@link SedeEntita} principale di Entita.
	 * 
	 * @param entitaLite
	 *            entita della sede
	 * @return sede principale dell'entità
	 */
	private SedeEntita getSedeEntitaPrincipale(EntitaLite entitaLite) {
		logger.debug("--> Enter getSedeEntitaPrincipale");
		Entita entita;
		if (entitaLite.getTipo().equals(ClienteLite.TIPO)) {
			entita = new Cliente();
			entita.setId(entitaLite.getId());
			entita.setVersion(entitaLite.getVersion());
		} else if (entitaLite.getTipo().equals(FornitoreLite.TIPO)) {
			entita = new Fornitore();
			entita.setId(entitaLite.getId());
			entita.setVersion(entitaLite.getVersion());
		} else {
			// se Entita non è ne cliente ne fornitore restituisco una istanza vuota di SedeEntita
			return null;
		}
		SedeEntita sedeEntita = anagraficaBD.caricaSedePrincipaleEntita(entita);
		return sedeEntita;
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		logger.debug("--> Enter propertyChange");
		if (formModel.isReadOnly()) {
			logger.debug("--> Exit propertyChange. FormModel in sola lettura");
			return;
		}
		final EntitaLite entitaNuova = (EntitaLite) evt.getNewValue();
		if (entitaNuova == null) {
			if (logger.isDebugEnabled()) {
				logger.debug("--> entita lite null, setto a null anche la sede entità");
			}
			formModel.getValueModel("documento.sedeEntita").setValue(null);
			return;
		}

		final EntitaLite oldEntita = (EntitaLite) evt.getOldValue();

		// se l'oggetto non cambia non faccio altro
		if (entitaNuova.equals(oldEntita)) {
			return;
		}

		assegnaSedeEntita(entitaNuova);
		logger.debug("--> Exit propertyChange");
	}

	/**
	 * @param anagraficaBD
	 *            The anagraficaBD to set.
	 */
	public void setAnagraficaBD(IAnagraficaBD anagraficaBD) {
		this.anagraficaBD = anagraficaBD;
	}

	@Override
	public void setFormModel(FormModel formModel) {
		this.formModel = formModel;
	}
}
