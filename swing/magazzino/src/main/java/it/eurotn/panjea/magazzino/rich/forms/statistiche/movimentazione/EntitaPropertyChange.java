/**
 * 
 */
package it.eurotn.panjea.magazzino.rich.forms.statistiche.movimentazione;

import it.eurotn.panjea.agenti.domain.Agente;
import it.eurotn.panjea.agenti.domain.lite.AgenteLite;
import it.eurotn.panjea.anagrafica.domain.Cliente;
import it.eurotn.panjea.anagrafica.domain.Entita;
import it.eurotn.panjea.anagrafica.domain.Fornitore;
import it.eurotn.panjea.anagrafica.domain.SedeEntita;
import it.eurotn.panjea.anagrafica.domain.lite.ClienteLite;
import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.anagrafica.domain.lite.FornitoreLite;
import it.eurotn.panjea.anagrafica.rich.bd.AnagraficaBD;
import it.eurotn.panjea.anagrafica.rich.bd.IAnagraficaBD;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.apache.log4j.Logger;
import org.springframework.binding.form.FormModel;
import org.springframework.richclient.util.RcpSupport;

/**
 * PropertyChange incaricato di gestire le azioni successive alla variazione di {@link EntitaLite}.
 * 
 * @author adriano
 * @version 1.0, 08/set/2008
 * 
 */
public class EntitaPropertyChange implements PropertyChangeListener {

	private static Logger logger = Logger.getLogger(EntitaPropertyChange.class);

	private FormModel formModel;

	private IAnagraficaBD anagraficaBD;

	/**
	 * Costruttore.
	 * 
	 * @param formModel
	 *            form model
	 */
	public EntitaPropertyChange(final FormModel formModel) {
		super();
		this.formModel = formModel;
		this.anagraficaBD = RcpSupport.getBean(AnagraficaBD.BEAN_ID);
	}

	/**
	 * Assegna la sede di magazzino di default per l'argomento entita.
	 * 
	 * @param entita
	 *            entità alla quale assegnare la sede
	 */
	private void assegnaSedeEntita(EntitaLite entita) {
		logger.debug("--> Enter assegnaSedeEntita");
		if (entita == null) {
			formModel.getValueModel("sedeEntita").setValueSilently(new SedeEntita(), this);
		} else {
			// controllo della corrispondenza di Entita con la SedeMagazzino esistente
			SedeEntita sedeEntita = (SedeEntita) formModel.getValueModel("sedeEntita").getValue();
			if (sedeEntita == null || !entita.getId().equals(sedeEntita.getEntita().getId())) {
				sedeEntita = getSedeEntitaPrincipale(entita);
				formModel.getValueModel("sedeEntita").setValueSilently(sedeEntita, this);
			}

		}
		logger.debug("--> Exit assegnaSedeEntita");
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
		Entita entita = null;
		if (entitaLite != null) {
			if (entitaLite.getTipo().equals(ClienteLite.TIPO)) {
				entita = new Cliente();
			} else if (entitaLite.getTipo().equals(FornitoreLite.TIPO)) {
				entita = new Fornitore();
			} else if (entitaLite.getTipo().equals(AgenteLite.TIPO)) {
				entita = new Agente();
			}
			if (entita == null) {
				throw new UnsupportedOperationException("Entità non ha un tipo valido. Tipo dell'entità: "
						+ entitaLite.getTipo());
			}
			entita.setId(entitaLite.getId());
			entita.setVersion(entitaLite.getVersion());
		}

		SedeEntita sedeEntita = new SedeEntita();
		if (entita != null) {
			sedeEntita = anagraficaBD.caricaSedePrincipaleEntita(entita);
		}
		return sedeEntita;
	}

	@Override
	public void propertyChange(PropertyChangeEvent event) {
		logger.debug("--> Enter propertyChange");
		// verifica variazione solamente se sono in modifica
		// tramite mail è arrivata una npe, come se il formModel fosse null,
		// teoricamente non dovrebbe essere però faccio un controllo in +
		if (formModel == null) {
			return;
		}
		if (formModel.isReadOnly()) {
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

}
