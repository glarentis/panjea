/**
 *
 */
package it.eurotn.panjea.mrp.manager.work;

import it.eurotn.panjea.mrp.domain.RigheOrdinatoCliente;
import it.eurotn.panjea.mrp.domain.RisultatoMRPArticoloBucket;
import it.eurotn.panjea.mrp.manager.interfaces.MrpCalcoloManager;
import it.eurotn.panjea.mrp.manager.interfaces.MrpManager;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import commonj.work.Work;

/**
 * @author leonardo
 */
public class RigaOrdinatoProduzioneEsistenteWork implements Work {

	private static Logger logger = Logger.getLogger(RigaOrdinatoProduzioneEsistenteWork.class);

	private RigheOrdinatoCliente righeOrdinatoCliente;
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
	public RigaOrdinatoProduzioneEsistenteWork(final Date startDate, final int numTime, final MrpManager mrpManager) {
		super();
		this.mrpManager = mrpManager;
		this.numTime = numTime;
		this.startDate = startDate;
	}

	/**
	 * @return the righeOrdinatoCliente to get
	 */
	public RigheOrdinatoCliente getRigheOrdinatoCliente() {
		return righeOrdinatoCliente;
	}

	@Override
	public boolean isDaemon() {
		return false;
	}

	@Override
	public void release() {
		righeOrdinatoCliente = null;
	}

	@Override
	public void run() {
		righeOrdinatoCliente = new RigheOrdinatoCliente();
		try {
			Calendar endCalendar = Calendar.getInstance();
			endCalendar.setTime(startDate);
			endCalendar.add(Calendar.DAY_OF_WEEK, numTime - 1 - MrpCalcoloManager.BUCKET_ZERO);
			List<RisultatoMRPArticoloBucket> righeOrdiniProduzioneEsistenti = mrpManager.caricaRigheProduzionePresenti(
					startDate, endCalendar.getTime());
			logger.debug("{" + righeOrdiniProduzioneEsistenti.size()
					+ "} righe ordini con parametri caricate...elaboro");
			righeOrdinatoCliente.aggiungiRigheOrdine(righeOrdiniProduzioneEsistenti, startDate, numTime);
		} catch (Exception e) {
			logger.error("Errore nel calcolo delle righe ordine produzione esistenti", e);
			throw new RuntimeException("Errore nel calcolo delle righe ordine produzione esistenti", e);
		}
	}

}
