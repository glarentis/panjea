package it.eurotn.panjea.ordini.manager.interfaces;

import it.eurotn.panjea.ordini.domain.OrdineImportato;

import javax.ejb.Local;

@Local
public interface ImportazioneOrdineImportatoManger {

	/**
	 * Conferma l'ordine dal backorder.
	 * 
	 * @param ordineImportato
	 *            backorder
	 * @param codiceValuta
	 *            codice della valuta
	 * @param anno
	 *            anno di magazzino
	 * @param timeStampCreazione
	 *            timestamp per tutti gli ordini creati in questo use case
	 */
	void confermaOrdine(OrdineImportato ordineImportato, String codiceValuta, Integer anno, long timeStampCreazione);

}
