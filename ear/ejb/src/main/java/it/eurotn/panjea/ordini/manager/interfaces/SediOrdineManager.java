package it.eurotn.panjea.ordini.manager.interfaces;

import it.eurotn.panjea.anagrafica.domain.SedeEntita;
import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.ordini.domain.SedeOrdine;

import javax.ejb.Local;

@Local
public interface SediOrdineManager {

	/**
	 * Carica {@link SedeOrdine} collegata a {@link SedeEntita}.
	 * 
	 * @param sedeEntita
	 *            sedeEntita di riferimento
	 * @param ignoraEreditaDatiCommerciali
	 *            se true forza il caricamento della sede ordine ignorando {@link SedeEntita#isEreditaDatiCommerciali()}
	 * @return {@link SedeOrdine} della sedeEntita
	 */
	SedeOrdine caricaSedeOrdineBySedeEntita(SedeEntita sedeEntita, boolean ignoraEreditaDatiCommerciali);

	/**
	 * Carica la sede ordine principale per l'entità.
	 * 
	 * @param entitaLite
	 *            entità
	 * @return sede ordine caricata
	 */
	SedeOrdine caricaSedeOrdinePrincipaleEntita(EntitaLite entitaLite);

	/**
	 * Cancella la sede ordine della sede entità specificata.
	 * @param sedeEntita la sede entita di cui caricare la sede ordine da cancellare
	 */
	void cancellaSedeOrdine(SedeEntita sedeEntita);
	
	/**
	 * Cancella la sede ordine specificata.
	 * @param sedeOrdine la sede ordine da cancellare
	 */
	void cancellaSedeOrdine(SedeOrdine sedeOrdine);
	
	/**
	 * Rende persistente la {@link SedeOrdine} e la restituisce.
	 * 
	 * @param sedeOrdine
	 *            sede ordine da salvare
	 * @return {@link SedeOrdine} salvata
	 */
	SedeOrdine salvaSedeOrdine(SedeOrdine sedeOrdine);

}
