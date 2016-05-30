package it.eurotn.panjea.tesoreria.manager.interfaces;

import it.eurotn.dao.exception.ObjectNotFoundException;
import it.eurotn.panjea.anagrafica.documenti.domain.Documento;
import it.eurotn.panjea.tesoreria.domain.AreaEffetti;
import it.eurotn.panjea.tesoreria.domain.AreaTesoreria;
import it.eurotn.panjea.tesoreria.util.ParametriRicercaAreeTesoreria;

import java.util.List;

public interface AreaTesoreriaManager {
	/**
	 * Cancella un'area tesoreria<br>
	 * L'implementazione del metodo sa su quale manager rigirare la chiamata.
	 * 
	 * @param areaTesoreria
	 *            da cancellare
	 * @param deleteAreeCollegate
	 *            se cancellare le aree collegate
	 */
	void cancellaAreaTesoreria(AreaTesoreria areaTesoreria, boolean deleteAreeCollegate);

	/**
	 * Cancella un'area tesoreria dal documento passato<br>
	 * L'implementazione del metodo sa su quale manager rigirare la chiamata.
	 * 
	 * @param documento
	 *            da cui caricare l'area tesoreria da cancellare
	 * @param deleteAreeCollegate
	 *            effettua la cancellazione delle sue aree collegate
	 */
	void cancellaAreaTesoreria(Documento documento, boolean deleteAreeCollegate);

	/**
	 * Cancella una lista di areeTesoreria.
	 * 
	 * @param listAree
	 *            aree da cancellare
	 * @param deleteAreeCollegate
	 *            effettua la cancellazione delle sue aree collegate
	 */
	void cancellaAreeTesorerie(List<AreaTesoreria> listAree, boolean deleteAreeCollegate);

	/**
	 * Metodo generico per caricare un'area tesoreria.<br>
	 * Nel manager il metodo e' implementato in base al tipo di area tesoreria del parametro.
	 * 
	 * @param areaTesoreria
	 *            del tipo richiesto
	 * @return l'area richiesta
	 * @throws ObjectNotFoundException
	 *             sollevata in caso l'area richiesta non esista
	 */
	AreaTesoreria caricaAreaTesoreria(AreaTesoreria areaTesoreria) throws ObjectNotFoundException;

	/**
	 * Carica l'{@link AreaTesoreria} del documento.
	 * 
	 * @param documento
	 *            Documento
	 * @return area tesoreria caricata
	 */
	AreaTesoreria caricaAreaTesoreria(Documento documento);

	/**
	 * Carica le aree collegate all'areaTesoreria.
	 * 
	 * @param areaTesoreria
	 *            area tesoreria di riferimento
	 * @return la lista di aree collegate
	 */
	List<AreaTesoreria> caricaAreeCollegate(AreaTesoreria areaTesoreria);

	/**
	 * Carica il numero delle aree che puntato alla {@link AreaEffetti} passata come parametro.
	 * 
	 * @param areaEffetti
	 *            area effetti
	 * @return numero di arre
	 */
	Long caricaNumeroAreeCollegate(AreaEffetti areaEffetti);

	/**
	 * @param documento
	 *            documento da controllare.
	 * @return area tesoreria trovata. Null se non esiste
	 */
	AreaTesoreria checkAreaTesoreria(Documento documento);

	/**
	 * Restituisce la lista delle aree emissione effetti relative all' {@link AreaTesoreria} di riferimento.
	 * 
	 * @param areaTesoreria
	 *            area tesoreria
	 * @return lista di aree emissione effetti
	 */
	List<AreaEffetti> getAreeEmissioneEffetti(AreaTesoreria areaTesoreria);

	/**
	 * Ricerca le aree tesorerie in base ai paramentri di ricerca.
	 * 
	 * @param parametriRicercaAreeTesoreria
	 *            parametri di ricerca
	 * @return lista di aree caricate
	 */
	List<AreaTesoreria> ricercaAreeTesorerie(ParametriRicercaAreeTesoreria parametriRicercaAreeTesoreria);

	/**
	 * Salva un {@link AreaTesoreria}.
	 * 
	 * @param areaTesoreria
	 *            area da salvare
	 * @return area salvata
	 */
	AreaTesoreria salvaAreaTesoreria(AreaTesoreria areaTesoreria);
}
