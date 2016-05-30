/**
 *
 */
package it.eurotn.panjea.mrp.manager.work;

import java.util.Date;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.Days;

import commonj.work.Work;
import it.eurotn.panjea.magazzino.domain.Giacenza;
import it.eurotn.panjea.mrp.domain.ArticoloAnagrafica;
import it.eurotn.panjea.mrp.domain.RigheCalcolo;
import it.eurotn.panjea.mrp.domain.RigheOrdinatoCliente;
import it.eurotn.panjea.mrp.domain.RisultatoMRPArticoloBucket;
import it.eurotn.panjea.mrp.domain.RisultatoSottoScortaMRPArticoloBucket;
import it.eurotn.panjea.mrp.manager.interfaces.MrpCalcoloManager;
import it.eurotn.panjea.mrp.util.ArticoloDepositoKey;

/**
 * @author leonardo
 */
public class RigaOrdinatoSottoscortaWork implements Work {

	private static Logger logger = Logger.getLogger(RigaOrdinatoSottoscortaWork.class);

	private RigheOrdinatoCliente righeSottoScorta;

	private Map<ArticoloDepositoKey, Giacenza> giacenze;
	private int numTime;
	private Date startDate;
	private Map<Integer, ArticoloAnagrafica> articoliAnagrafica;

	/**
	 * Costruttore.
	 *
	 * @param giacenze
	 *            giacenze
	 * @param startDate
	 *            startDate
	 * @param numTime
	 *            numTime
	 * @param articoliAnagrafica
	 *            articoliAnagrafica
	 */
	public RigaOrdinatoSottoscortaWork(final Map<ArticoloDepositoKey, Giacenza> giacenze, final Date startDate,
			final int numTime, final Map<Integer, ArticoloAnagrafica> articoliAnagrafica) {
		super();
		this.giacenze = giacenze;
		this.numTime = numTime;
		this.startDate = startDate;
		this.articoliAnagrafica = articoliAnagrafica;
	}

	/**
	 * @return the righeSottoScorta to get
	 */
	public RigheOrdinatoCliente getRigheSottoScorta() {
		return righeSottoScorta;
	}

	@Override
	public boolean isDaemon() {
		return false;
	}

	@Override
	public void release() {
		righeSottoScorta = null;
	}

	@Override
	public void run() {
		try {
			righeSottoScorta = new RigheOrdinatoCliente();
			for (Entry<ArticoloDepositoKey, Giacenza> giacenza : giacenze.entrySet()) {
				if (giacenza.getValue().isSottoScorta()) {
					// Creo la riga per la sottoScorta
					RigheCalcolo[] righeCalcolo = new RigheCalcolo[numTime];
					RigheCalcolo rigaSottoScortaArticolo = new RigheCalcolo();

					Date today = new Date();
					DateTime startDateTime = new DateTime(startDate.getTime());
					DateTime todayDateTime = new DateTime(today.getTime());
					int posizioneAdOggi = Days.daysBetween(startDateTime, todayDateTime).getDays();
					posizioneAdOggi = posizioneAdOggi + MrpCalcoloManager.BUCKET_ZERO + 1000;

					double giacenzaValue = giacenza.getValue().getGiacenza();

					RisultatoMRPArticoloBucket rigaScorta = new RisultatoSottoScortaMRPArticoloBucket();
					rigaScorta.setDataDocumento(today);
					rigaScorta.setIdDeposito(giacenza.getKey().getIdDeposito());
					rigaScorta.setGiacenza(giacenzaValue);
					rigaScorta.setScorta(giacenza.getValue().getScorta());
					// il sottoscorta non ha una quantità richiesta da
					// un'ordine, ma semplicemente la quantità mancante
					// in giacenza
					rigaScorta.setQtaR(0);
					rigaScorta.setQtaPor(giacenza.getValue().getScorta());

					ArticoloAnagrafica articoloAnagrafica = articoliAnagrafica.get(giacenza.getKey().getIdArticolo());
					if (articoloAnagrafica != null) {
						rigaScorta.setArticoloAnagrafica(articoliAnagrafica.get(giacenza.getKey().getIdArticolo()));
					}
					rigaSottoScortaArticolo.addRisultatiMRPArticoloBucket(-1, -1, rigaScorta);

					// righeCalcolo[posizioneAdOggi] = rigaSottoScortaArticolo;

					// righeSottoScorta.put(new
					// ArticoloDepositoConfigurazioneKey(giacenza.getKey().getIdArticolo(),
					// giacenza.getKey().getIdDeposito(), null), righeCalcolo);
				}
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			logger.error("Errore nel caricare le anagrafiche articolo mrp", e);
			throw e;
		} catch (Exception e) {
			logger.error("Errore nel caricare le anagrafiche articolo mrp", e);
			throw new RuntimeException("Errore nel caricare le anagrafiche articolo mrp", e);
		}
	}

}
