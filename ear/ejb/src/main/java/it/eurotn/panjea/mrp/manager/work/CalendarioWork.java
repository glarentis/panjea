/**
 *
 */
package it.eurotn.panjea.mrp.manager.work;

import it.eurotn.panjea.mrp.manager.interfaces.MrpCalcoloManager;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.apache.log4j.Logger;

import commonj.work.Work;

/**
 * @author leonardo
 */
public class CalendarioWork implements Work {

	private static Logger logger = Logger.getLogger(CalendarioWork.class);

	private boolean[] giorniLavorativi;
	private Date startDate;
	private int numTime;

	/**
	 * Costruttore.
	 * 
	 * @param startDate
	 *            startDate
	 * @param numTime
	 *            numTime
	 */
	public CalendarioWork(final Date startDate, final int numTime) {
		super();
		this.numTime = numTime;
		this.startDate = startDate;
	}

	/**
	 * @return the giorniLavorativi to get
	 */
	public boolean[] getGiorniLavorativi() {
		return giorniLavorativi;
	}

	@Override
	public boolean isDaemon() {
		return false;
	}

	@Override
	public void release() {
		giorniLavorativi = null;
	}

	@Override
	public void run() {
		try {
			logger.debug("--> Enter call calendar");
			long time = System.currentTimeMillis();
			giorniLavorativi = new boolean[numTime];
			Calendar calendar = new GregorianCalendar();
			calendar.setTime(startDate);
			calendar.add(Calendar.DAY_OF_MONTH, -MrpCalcoloManager.BUCKET_ZERO);
			for (int i = 0; i < giorniLavorativi.length; i++) {
				int day = calendar.get(Calendar.DAY_OF_WEEK);
				giorniLavorativi[i] = day != Calendar.SUNDAY && day != Calendar.SATURDAY;
				calendar.add(Calendar.DAY_OF_WEEK, 1);
			}
			logger.debug("--> Exit call calendar " + (System.currentTimeMillis() - time));
		} catch (Exception e) {
			logger.error("Errore nel calcolo del calendario", e);
			throw new RuntimeException("Errore nel calcolo del calendario", e);
		}
	}

}
