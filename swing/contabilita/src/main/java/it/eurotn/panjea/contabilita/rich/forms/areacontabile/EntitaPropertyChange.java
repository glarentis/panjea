/**
 *
 */
package it.eurotn.panjea.contabilita.rich.forms.areacontabile;

import foxtrot.AsyncTask;
import foxtrot.AsyncWorker;
import it.eurotn.panjea.anagrafica.domain.Cliente;
import it.eurotn.panjea.anagrafica.domain.ContrattoSpesometro;
import it.eurotn.panjea.anagrafica.domain.Entita;
import it.eurotn.panjea.anagrafica.domain.Fornitore;
import it.eurotn.panjea.anagrafica.domain.SedeEntita;
import it.eurotn.panjea.anagrafica.domain.lite.ClienteLite;
import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.anagrafica.domain.lite.FornitoreLite;
import it.eurotn.panjea.anagrafica.rich.bd.IAnagraficaBD;
import it.eurotn.panjea.anagrafica.rich.bd.IAnagraficaTabelleBD;
import it.eurotn.rich.form.FormModelPropertyChangeListeners;

import java.beans.PropertyChangeEvent;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.binding.form.FormModel;

/**
 * @author Leonardo
 */
public class EntitaPropertyChange implements FormModelPropertyChangeListeners, InitializingBean {

	private class ContrattoLoaderTask extends AsyncTask {

		private EntitaLite entitaLite = null;

		@Override
		public void failure(Throwable e) {
			logger.error("--> errore in caricaSedePagamento in failure", e);
		}

		@Override
		public Object run() throws Exception {
			logger.debug("--> Enter run");
			List<ContrattoSpesometro> contratti = anagraficaTabelleBD.caricaContratti(entitaLite);
			ContrattoSpesometro contratto = new ContrattoSpesometro();
			if (contratti.size() == 1) {
				contratto = contratti.get(0);
			}
			return contratto;
		}

		/**
		 * @param entitaLite
		 *            the entitaLite to set
		 */
		public void setEntitaLite(EntitaLite entitaLite) {
			this.entitaLite = entitaLite;
		}

		@Override
		public void success(Object object) {
			logger.debug("--> Enter success");
			// Imposto il pagamento
			ContrattoSpesometro contratto = (ContrattoSpesometro) object;
			formModel.getValueModel("areaContabile.documento.contrattoSpesometro").setValue(contratto);
		}
	}

	private static Logger logger = Logger.getLogger(EntitaPropertyChange.class);
	private FormModel formModel = null;
	private IAnagraficaTabelleBD anagraficaTabelleBD = null;

	private IAnagraficaBD anagraficaBD;

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	/**
	 * Assegna il contratto se ne esiste uno, altrimenti ne prepara uno vuoto per il form.
	 * 
	 * @param entita
	 *            l'entità di cui cercare il contratto
	 */
	private void assegnaContratto(EntitaLite entita) {
		Boolean gestioneContratto = (Boolean) formModel.getValueModel(
				"areaContabile.documento.tipoDocumento.gestioneContratto").getValue();
		if (gestioneContratto == Boolean.TRUE) {
			ContrattoLoaderTask taskContratto = new ContrattoLoaderTask();
			taskContratto.setEntitaLite(entita);
			AsyncWorker.post(taskContratto);
		}
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
		formModel.getValueModel("areaContabile.documento.sedeEntita").setValue(sedeEntita);
		logger.debug("--> Exit assegnaSedeEntita");
	}

	/**
	 * @return Returns the anagraficaBD.
	 */
	public IAnagraficaBD getAnagraficaBD() {
		return anagraficaBD;
	}

	/**
	 * @return the anagraficaTabelleBD
	 */
	public IAnagraficaTabelleBD getAnagraficaTabelleBD() {
		return anagraficaTabelleBD;
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
		SedeEntita sedeEntita = anagraficaBD.caricaSedePredefinitaEntita(entita);
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
			formModel.getValueModel("areaContabile.documento.sedeEntita").setValue(null);
			return;
		}

		final EntitaLite oldEntita = (EntitaLite) evt.getOldValue();

		// se l'oggetto non cambia non faccio altro
		if (entitaNuova.equals(oldEntita)) {
			return;
		}

		assegnaSedeEntita(entitaNuova);
		assegnaContratto(entitaNuova);
		logger.debug("--> Exit propertyChange");
	}

	/**
	 * @param anagraficaBD
	 *            The anagraficaBD to set.
	 */
	public void setAnagraficaBD(IAnagraficaBD anagraficaBD) {
		this.anagraficaBD = anagraficaBD;
	}

	/**
	 * @param anagraficaTabelleBD
	 *            the anagraficaTabelleBD to set
	 */
	public void setAnagraficaTabelleBD(IAnagraficaTabelleBD anagraficaTabelleBD) {
		this.anagraficaTabelleBD = anagraficaTabelleBD;
	}

	@Override
	public void setFormModel(FormModel formModel) {
		this.formModel = formModel;
	}
}
