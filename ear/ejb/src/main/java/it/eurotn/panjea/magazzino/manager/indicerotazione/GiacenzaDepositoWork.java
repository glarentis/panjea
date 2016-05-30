package it.eurotn.panjea.magazzino.manager.indicerotazione;

import it.eurotn.panjea.anagrafica.domain.Deposito;
import it.eurotn.panjea.magazzino.domain.ArticoloLite;
import it.eurotn.panjea.magazzino.manager.interfaces.GiacenzaManager;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import commonj.work.Work;

/**
 * @author leonardo
 */
public class GiacenzaDepositoWork implements Work {

	private static Logger logger = Logger.getLogger(GiacenzaDepositoWork.class);

	private GiacenzaManager giacenzaManager;
	private Deposito deposito;
	private Date startDate;

	private List<Integer> articoli;

	private Map<ArticoloLite, Double> result;

	/**
	 * Costruttore.
	 *
	 * @param deposito2
	 *            deposito
	 * @param startDate
	 *            data inizio per calcolo giacenza
	 * @param giacenzaManager
	 *            manager delal giacenza articolo
	 * @param articoli
	 *            articoli richiesti
	 */
	public GiacenzaDepositoWork(final Deposito deposito, final Date startDate,
			final GiacenzaManager giacenzaManager, final List<Integer> articoli) {
		super();
		this.deposito = deposito;
		this.giacenzaManager = giacenzaManager;
		this.startDate = startDate;
		this.articoli = articoli;
	}

	/**
	 * @return Returns the deposito.
	 */
	public Deposito getDeposito() {
		return deposito;
	}

	/**
	 * @return Returns the result.
	 */
	public Map<ArticoloLite, Double> getResult() {
		return result;
	}

	@Override
	public boolean isDaemon() {
		return false;
	}

	@Override
	public void release() {
		result = null;
	}

	@Override
	public void run() {
		try {
			logger.debug("DEBUG call calcola giacenza giacenzaDeposito");
			result = giacenzaManager.calcolaGiacenze(
					deposito.getDepositoLite(), articoli, startDate);
		} catch (Exception e) {
			logger.error("Errore nel calcolo della giacenza", e);
			throw new RuntimeException("Errore nel calcolo della giacenza", e);
		}
	}

}
