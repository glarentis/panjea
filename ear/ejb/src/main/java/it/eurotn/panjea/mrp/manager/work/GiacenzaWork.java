/**
 *
 */
package it.eurotn.panjea.mrp.manager.work;

import it.eurotn.panjea.magazzino.domain.Giacenza;
import it.eurotn.panjea.mrp.manager.interfaces.MrpManager;
import it.eurotn.panjea.mrp.util.ArticoloDepositoKey;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import commonj.work.Work;
import commonj.work.WorkItem;
import commonj.work.WorkManager;

/**
 * @author leonardo
 */
public class GiacenzaWork implements Work {

	private static Logger logger = Logger.getLogger(GiacenzaWork.class);

	private MrpManager mrpManager;
	private WorkManager workManager;
	private Map<ArticoloDepositoKey, Giacenza> giacenze;
	private Date startDate;

	/**
	 * Costruttore.
	 *
	 * @param startDate
	 *            data inizio per calcolo giacenza
	 * @param mrpManager
	 *            mrpManager
	 * @param workManager
	 *            workManager per calcolo per deposito
	 */
	public GiacenzaWork(final Date startDate, final MrpManager mrpManager, final WorkManager workManager) {
		super();
		this.mrpManager = mrpManager;
		this.workManager = workManager;
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
	}

	@Override
	public void run() {
		long time = System.nanoTime();
		logger.debug("Start calcolo giacenze " + time);
		HashMap<ArticoloDepositoKey, Giacenza> giacenzeWorkResult = new HashMap<>();
		try {
			List<WorkItem> tasks = new ArrayList<>();
			List<Integer> depositi = mrpManager.getIdDepositi();
			for (Integer idDeposito : depositi) {
				GiacenzaDepositoWork giacenzaDepositoWork = new GiacenzaDepositoWork(idDeposito, startDate, mrpManager);
				WorkItem giacenzaDepositoWorkItem = workManager.schedule(giacenzaDepositoWork);
				tasks.add(giacenzaDepositoWorkItem);
			}
			workManager.waitForAll(tasks, 200000);
			for (WorkItem workItem : tasks) {
				GiacenzaDepositoWork giacenzaDepositoWork = (GiacenzaDepositoWork) workItem.getResult();
				giacenzeWorkResult.putAll(giacenzaDepositoWork.getGiacenze());
			}
			giacenze = Collections.unmodifiableMap(giacenzeWorkResult);
		} catch (Exception e) {
			logger.error("Errore nel calcolo delle giacenze", e);
			throw new RuntimeException("Errore nel calcolo delle giacenze", e);
		} finally {
			logger.debug("Fine calcolo giacenze in " + (System.currentTimeMillis() - time));
		}
	}

}
