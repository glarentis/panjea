/**
 *
 */
package it.eurotn.panjea.mrp.manager.work;

import it.eurotn.panjea.magazzino.domain.Giacenza;
import it.eurotn.panjea.mrp.manager.interfaces.MrpManager;
import it.eurotn.panjea.mrp.util.ArticoloDepositoKey;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import commonj.work.Work;

/**
 * @author leonardo
 */
public class GiacenzaDepositoWork implements Work {

	private static Logger logger = Logger.getLogger(GiacenzaDepositoWork.class);

	private MrpManager mrpManager;
	private Map<ArticoloDepositoKey, Giacenza> giacenze;
	private Integer idDeposito;
	private Date startDate;

	/**
	 * Costruttore.
	 *
	 * @param idDeposito
	 *            idDeposito
	 * @param startDate
	 *            data inizio per calcolo giacenza
	 * @param mrpManager
	 *            mrpManager
	 */
	public GiacenzaDepositoWork(final Integer idDeposito, final Date startDate, final MrpManager mrpManager) {
		super();
		this.idDeposito = idDeposito;
		this.mrpManager = mrpManager;
		this.startDate = startDate;
	}

	/**
	 * @return the giacenze to get
	 */
	public Map<ArticoloDepositoKey, Giacenza> getGiacenze() {
		return giacenze;
	}

	@Override
	public boolean isDaemon() {
		return false;
	}

	@Override
	public void release() {
		giacenze = null;
	}

	@Override
	public void run() {
		try {
			logger.debug("DEBUG call calcola giacenza giacenzaDeposito");
			giacenze = new HashMap<ArticoloDepositoKey, Giacenza>();

			long time = System.nanoTime();
			List<Giacenza> articoliGiacenza = mrpManager.calcolaGiacenzeFlat(idDeposito, startDate);

			time = System.nanoTime();

			for (Giacenza rigaGiacenza : articoliGiacenza) {
				giacenze.put(new ArticoloDepositoKey(rigaGiacenza.getIdArticolo(), idDeposito), rigaGiacenza);
			}
			if (logger.isDebugEnabled()) {
				logger.debug("--> Giacenza calcolata per deposito " + idDeposito + ". Num elementi " + giacenze.size()
						+ " in " + (System.nanoTime() - time) / 1000);
			}
		} catch (Exception e) {
			logger.error("Errore nel calcolo della giacenza", e);
			throw new RuntimeException("Errore nel calcolo della giacenza", e);
		}
	}

}
