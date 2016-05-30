package it.eurotn.panjea.mrp.manager.work;

import it.eurotn.panjea.mrp.domain.OrdiniFornitoreCalcolo;
import it.eurotn.panjea.mrp.manager.interfaces.MrpCalcoloManager;
import it.eurotn.panjea.mrp.manager.interfaces.MrpManager;
import it.eurotn.panjea.mrp.util.ArticoloDepositoKey;
import it.eurotn.util.KeyFromValueProvider;
import it.eurotn.util.PanjeaEJBUtil;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import commonj.work.Work;

public class OrdinatoFornitoreCalcoloWork implements Work {
	private static Logger logger = Logger.getLogger(OrdinatoFornitoreCalcoloWork.class);

	private Map<ArticoloDepositoKey, List<OrdiniFornitoreCalcolo>> ordinatiFornitore;
	private MrpManager mrpManager;
	private Date startDate;
	private int numTime;

	/**
	 * Costruttore.
	 *
	 * @param startDate
	 *            startDate
	 * @param numTime
	 *            numTime
	 * @param mrpManager
	 *            mrpManager
	 */
	public OrdinatoFornitoreCalcoloWork(final Date startDate, final int numTime, final MrpManager mrpManager) {
		super();
		this.mrpManager = mrpManager;
		this.numTime = numTime;
		this.startDate = startDate;
	}

	/**
	 * @return the ordinatiFornitoreProduzione to get
	 */
	public Map<ArticoloDepositoKey, List<OrdiniFornitoreCalcolo>> getOrdinatiFornitoreProduzione() {
		return ordinatiFornitore;
	}

	@Override
	public boolean isDaemon() {
		return false;
	}

	@Override
	public void release() {
		ordinatiFornitore = null;
	}

	@Override
	public void run() {
		logger.debug("--> Enter run OrdinatoOrdiniFornitore");
		try {
			Calendar endCalendar = new GregorianCalendar();
			endCalendar.setTime(startDate);
			endCalendar.add(Calendar.DAY_OF_WEEK, numTime - 1 - MrpCalcoloManager.BUCKET_ZERO);
			List<OrdiniFornitoreCalcolo> ordinatoFornitore = mrpManager.caricaOrdiniFornitoreCalcolo(startDate,
					endCalendar.getTime());
			ordinatiFornitore = PanjeaEJBUtil.listToMap(ordinatoFornitore,
					new KeyFromValueProvider<OrdiniFornitoreCalcolo, ArticoloDepositoKey>() {

						@Override
						public ArticoloDepositoKey keyFromValue(OrdiniFornitoreCalcolo elem) {
					return new ArticoloDepositoKey(elem.getIdArticolo(), elem.getIdDeposito());
						}
					});
			logger.debug("--> Exit run OrdinatoOrdiniFornitore");
		} catch (Exception e) {
			logger.error("Errore nel calcolo dell' ordinato fornitore e produzione", e);
			throw new RuntimeException("Errore nel calcolo dell' ordinato fornitore e produzione", e);
		}
	}

}
