/**
 *
 */
package it.eurotn.panjea.mrp.manager.work;

import it.eurotn.panjea.magazzino.domain.Giacenza;
import it.eurotn.panjea.mrp.domain.ArticoloAnagrafica;
import it.eurotn.panjea.mrp.domain.Bom;
import it.eurotn.panjea.mrp.domain.RigheCalcolo;
import it.eurotn.panjea.mrp.domain.RisultatoMRPArticoloBucket;
import it.eurotn.panjea.mrp.manager.interfaces.MrpCalcoloManager;
import it.eurotn.panjea.mrp.util.ArticoloConfigurazioneKey;
import it.eurotn.panjea.mrp.util.RisultatoPadre;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ObjectUtils;
import org.joda.time.DateTime;
import org.joda.time.Days;

/**
 * @author leonardo
 */
public class ArticoloCalcoloSottoScortaWork extends ArticoloCalcoloWork {

	private Date startDate;

	/**
	 * Costruttore.
	 *
	 * @param idDeposito
	 *            idDeposito
	 * @param idArticolo
	 *            idArticolo
	 * @param idConfigurazione
	 *            idConfigurazione
	 * @param giacenza
	 *            giacenza
	 * @param righeCalcolo
	 *            righeCalcolo
	 * @param s
	 *            s
	 * @param numBucket
	 *            numBucket
	 * @param articoloAnagrafica
	 *            articoloAnagrafica
	 * @param calendarWork
	 *            calendarWork
	 * @param ordinamento
	 *            ordinamento
	 * @param righeCalcolatePadre
	 *            righeCalcolatePadre
	 * @param startDate
	 *            startDate
	 */
	public ArticoloCalcoloSottoScortaWork(Date startDate, final int idDeposito, final int idArticolo,
			final Giacenza giacenza, RigheCalcolo[] ordinatoCliente, final int numBucket,
			final ArticoloAnagrafica articoloAnagrafica, final boolean[] calendarWork,
			final List<RisultatoPadre> righeCalcolatePadre, final Map<ArticoloConfigurazioneKey, Bom> boms) {
		super(idDeposito, idArticolo, giacenza, ordinatoCliente, numBucket, articoloAnagrafica, calendarWork,
				righeCalcolatePadre, boms, null);
		this.startDate = startDate;
	}

	/**
	 * Calcolo por per le righe sottoscorta. Una importante differenza con le
	 * righe ordine cliente è l'utilizzo della data documento e della data
	 * consegna; la data documento è la data al momento della generazione degli
	 * ordini (today), mentre la data di consegna è la data documento più il
	 * lead time dell'articolo (today + bucketDataDocumento, tenente conto il
	 * calendario).
	 */
	@Override
	protected void calcola() {
		double[] s = calcolaS();
		ordinatoCliente = ObjectUtils.defaultIfNull(ordinatoCliente, new RigheCalcolo[numBucket]);
		RigheCalcolo[] risultato = new RigheCalcolo[numBucket];
		s = ObjectUtils.defaultIfNull(s, new double[numBucket]);
		giacenza = ObjectUtils.defaultIfNull(giacenza, new Giacenza(0.0, 0.0));

		Double disponibilita = giacenza.getGiacenza();
		Double disponibilitaPrimaDiUtilizzo = disponibilita;
		risultato = new RigheCalcolo[numBucket];

		DateTime startDateTime = new DateTime(startDate.getTime());
		DateTime today = new DateTime();
		int posizioneAdOggi = Days.daysBetween(startDateTime, today).getDays();
		posizioneAdOggi = posizioneAdOggi + MrpCalcoloManager.BUCKET_ZERO;

		RigheCalcolo risultatoGiorno = ObjectUtils.defaultIfNull(ordinatoCliente[posizioneAdOggi], new RigheCalcolo());
		if (!risultatoGiorno.getRisultatiMRPArticoloBucket().isEmpty()) {
			RisultatoMRPArticoloBucket risultatoMRPArticoloBucket = risultatoGiorno.getRisultatiMRPArticoloBucket()
					.get(0);

			// Se ho righe in risultatoGiorno calcolo i tempi con leadTime e
			// calendari.
			int leadTimeProgressivo = articoloAnagrafica.getLeadTime();
			Date dataDocumento = risultatoMRPArticoloBucket.getDataDocumento();
			Date dataProduzione = risultatoMRPArticoloBucket.getDataProduzione();

			// se non ho lead time o data produzione, imposto almeno 1 come
			// leadTime per le produzioni (distinte)
			if (leadTimeProgressivo == 0 && dataProduzione == null && articoloAnagrafica.isDistinta()) {
				leadTimeProgressivo = 1;
			}
			int bucketDataDocumento = 0;
			while (leadTimeProgressivo > 0) {
				bucketDataDocumento++;
				try {
					if (calendarWork[posizioneAdOggi + bucketDataDocumento]) {
						leadTimeProgressivo--;
					}
				} catch (Exception e) {
					System.out.println(e);
				}
			}

			disponibilita += s[posizioneAdOggi + bucketDataDocumento];
			disponibilitaPrimaDiUtilizzo = s[posizioneAdOggi + bucketDataDocumento];
			Double qtaRiordino = 0.0;
			Double minOrdinabile = risultatoMRPArticoloBucket.getMinOrdinabile();
			if (minOrdinabile != null && minOrdinabile > 0 && s[posizioneAdOggi + bucketDataDocumento] < minOrdinabile) {
				if (disponibilita <= minOrdinabile) {
					qtaRiordino = -minOrdinabile;
				}
			}
			Double lottoRiordino = risultatoMRPArticoloBucket.getLottoRiordino();
			if (lottoRiordino != null && lottoRiordino.compareTo(0.0) != 0) {
				if (Math.abs(disponibilita) <= lottoRiordino) {
					qtaRiordino = -lottoRiordino;
				} else {
					BigDecimal result = BigDecimal.valueOf(disponibilita).divide(BigDecimal.valueOf(lottoRiordino), 0,
							RoundingMode.UP);
					qtaRiordino = lottoRiordino * result.doubleValue();
				}
				if (qtaRiordino.compareTo(0.0) != 0 && s[posizioneAdOggi + bucketDataDocumento] < Math.abs(qtaRiordino)) {
					disponibilita = qtaRiordino;
				}
			}

			// Per ogni riga setto la dataDocumento appena calcolata e setto i
			// parametri dell'anagrafica
			// articolo
			Date dataConsegna = new DateTime(dataDocumento).plusDays(bucketDataDocumento).toDate();
			// disponibilita += s[posizioneAdOggi + bucketDataDocumento];
			// se ho impostata una data di produzione sull'ordine cliente,
			// allora uso quello invece di considerare
			// la data calcolata
			if (dataProduzione != null && articoloAnagrafica.isDistinta()) {
				dataDocumento = dataProduzione;
			}

			// Controllo le scorte. Se sono sotto scorta creo una riga per la
			// scorta.
			if (giacenza.isSottoScorta()) {
				risultatoMRPArticoloBucket.setDataDocumento(dataDocumento);
				risultatoMRPArticoloBucket.setDataConsegna(dataConsegna);
				risultatoMRPArticoloBucket.setIdDeposito(key.getIdDeposito());
				risultatoMRPArticoloBucket.setGiacenza(giacenza.getGiacenza());
				risultatoMRPArticoloBucket.setScorta(giacenza.getScorta());
				if (disponibilita >= 0) {
					risultatoMRPArticoloBucket.setQtaPor(0);
				} else {
					risultatoMRPArticoloBucket.setQtaPor((disponibilita * -1));
					disponibilita = 0.0;
				}
				risultatoMRPArticoloBucket.setDisponibilita(s[posizioneAdOggi + bucketDataDocumento]);
				risultatoMRPArticoloBucket.setDisponibilitaDopoUtilizzo(disponibilita);
				risultatoMRPArticoloBucket.setQtaInArrivo(disponibilitaPrimaDiUtilizzo);
				risultatoMRPArticoloBucket.setArticoloAnagrafica(articoloAnagrafica);
				risultatoMRPArticoloBucket.setOrdinamento(ordinamento);

				RigheCalcolo risultatoGiornoCalcolato = ObjectUtils.defaultIfNull(risultato[posizioneAdOggi],
						new RigheCalcolo());
				// risultatoGiornoCalcolato.addRisultatiMRPArticoloBucket(risultatoMRPArticoloBucket);

				risultato[posizioneAdOggi] = risultatoGiornoCalcolato;
			}
		}

	}

}
