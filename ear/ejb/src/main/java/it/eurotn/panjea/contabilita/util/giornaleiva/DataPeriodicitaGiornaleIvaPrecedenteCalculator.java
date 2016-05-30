/**
 *
 */
package it.eurotn.panjea.contabilita.util.giornaleiva;

import java.util.Calendar;

/**
 * @author leonardo
 */
public abstract class DataPeriodicitaGiornaleIvaPrecedenteCalculator {

	/**
	 * Calculator per determinare la data del giornale iva precedente rispetto alla periodicità e ad altri parametri
	 * iniziali.
	 * 
	 * @param anno
	 *            l'anno di partenza da cui calcolare il precedente
	 * @param mese
	 *            il mese di partenza da cui calcolare il precedente
	 * @param riepilogoAnnuale
	 *            richiedo il riepilogo annuale e non il mese o il trimestre
	 * @return il calendar con anno e mese avvalorati con la data per la ricerca del giornale iva precedente
	 */
	public Calendar calculate(int anno, int mese, boolean riepilogoAnnuale) {
		Calendar calendar = Calendar.getInstance();
		// imposto l'anno corrente
		calendar.set(Calendar.YEAR, anno);

		calendar.set(Calendar.MILLISECOND, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.DAY_OF_MONTH, 1);

		// imposto il mese corrente, (mese - 1) dato che i mesi del calendar partono da 0
		calendar.set(Calendar.MONTH, mese - 1);

		calendar = getDataPrecedente(calendar, riepilogoAnnuale);
		return calendar;
	}

	/**
	 * Restituisce il calendar con impostato il mese e l'anno per la ricerca del giornale iva precedente a seconda di
	 * periodicità e richiesta di stampa annuale per il tipo periodicità di default restituisce la stessa data.
	 * 
	 * @param calendar
	 *            il calendar di partenza
	 * @param riepilogoAnnuale
	 *            richiedo il riepilogo annuale e non il mese o il trimestre
	 * @return il calendar che rappresenta la data per la ricerca del giornale iva precedente
	 */
	protected Calendar getDataPrecedente(Calendar calendar, boolean riepilogoAnnuale) {
		return calendar;
	}
}
