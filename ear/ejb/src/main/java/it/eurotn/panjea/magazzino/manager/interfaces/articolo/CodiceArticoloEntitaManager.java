package it.eurotn.panjea.magazzino.manager.interfaces.articolo;

import it.eurotn.panjea.anagrafica.domain.Entita;
import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.magazzino.domain.CodiceArticoloEntita;
import it.eurotn.panjea.magazzino.exception.CodiceArticoloEntitaAbitualeEsistenteException;
import it.eurotn.panjea.magazzino.exception.CodiceArticoloEntitaContoTerziEsistenteException;

import java.util.List;

import javax.ejb.Local;

/**
 * Gestisce il CRUD di {@link CodiceArticoloEntita}.
 * 
 * @author Leonardo
 */
@Local
public interface CodiceArticoloEntitaManager {

	/**
	 * Cancella il {@link CodiceArticoloEntita} scelto.
	 * 
	 * @param codiceArticoloEntita
	 *            il {@link CodiceArticoloEntita} da cancellare
	 */
	void cancellaCodiceArticoloEntita(CodiceArticoloEntita codiceArticoloEntita);

	/**
	 * Carica il {@link CodiceArticoloEntita} dell'articolo ed entità scelti.
	 * 
	 * @param idArticolo
	 *            l'id dell'articolo di cui caricare il {@link CodiceArticoloEntita}
	 * @param idEntita
	 *            l'id dell'entita di cui caricare il {@link CodiceArticoloEntita}
	 * @return {@link CodiceArticoloEntita} caricato, <code>null</code> se non esiste
	 */
	CodiceArticoloEntita caricaCodiceArticoloEntita(Integer idArticolo, Integer idEntita);

	/**
	 * Carica la lista di {@link CodiceArticoloEntita} dell'entità scelta.
	 * 
	 * @param entita
	 *            entità di cui caricare i {@link CodiceArticoloEntita}
	 * @return List<CodiceArticoloEntita>
	 */
	List<CodiceArticoloEntita> caricaCodiciArticoloEntita(Entita entita);

	/**
	 * Carica la lista di {@link CodiceArticoloEntita} dell'articolo scelto.
	 * 
	 * @param idArticolo
	 *            l'id dell'articolo di cui caricare i {@link CodiceArticoloEntita}
	 * @return List<CodiceArticoloEntita>
	 */
	List<CodiceArticoloEntita> caricaCodiciArticoloEntita(Integer idArticolo);

	/**
	 * Carica le entità abituali per i codici articolo definiti.
	 * 
	 * @param tipoEntita
	 *            il filtro per tipo entità
	 * 
	 * @return List<EntitaLite>
	 */
	List<EntitaLite> caricaEntitaAbituali(String tipoEntita);

	/**
	 * Salva il {@link CodiceArticoloEntita} creato.
	 * 
	 * @param codiceArticoloEntita
	 *            il {@link CodiceArticoloEntita} da salvare
	 * @return {@link CodiceArticoloEntita} salvato
	 * @throws CodiceArticoloEntitaContoTerziEsistenteException
	 *             CodiceArticoloEntitaContoTerziEsistenteException
	 * @throws CodiceArticoloEntitaAbitualeEsistenteException
	 *             CodiceArticoloEntitaAbitualeEsistenteException
	 */
	CodiceArticoloEntita salvaCodiceArticoloEntita(CodiceArticoloEntita codiceArticoloEntita)
			throws CodiceArticoloEntitaContoTerziEsistenteException, CodiceArticoloEntitaAbitualeEsistenteException;

}
