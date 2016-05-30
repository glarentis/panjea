package it.eurotn.panjea.parametriricerca.domain;

import java.io.Serializable;

/**
 * Dato un {@link Periodo} ritorna lo stesso con le date di inizio e fine calcolate rispetto alla strategia del.
 * {@link Periodo}
 * 
 * @author giangi
 * @version 1.0, 07/lug/2011
 * 
 */
public interface PeriodoStrategyDateCalculator extends Serializable {
	/**
	 * 
	 * @param periodo
	 *            periodo contenente i dati per il calcolo
	 * @return Nuovo periodo con avvalorato solamente i campi data Inizio e data fine
	 */
	Periodo calcola(Periodo periodo);

	/**
	 * 
	 * @return true se posso avere la data iniziale a null. (ad esempio "ultimo mese" diventa "fino a ultimo mese"
	 */
	boolean isAllowedDataInizialeNull();

	/**
	 * 
	 * @return true se posso selezionare dei numeri giorni.
	 */
	boolean isAllowedNumGiorni();

	/**
	 * 
	 * @return true se posso selezionare le date, false se le date sono calcolate automaticamente.
	 */
	boolean isAllowedSelectDate();
}
