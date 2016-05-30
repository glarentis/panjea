package it.eurotn.panjea.anagrafica.manager.interfaces;

import it.eurotn.panjea.anagrafica.domain.Contatto;
import it.eurotn.panjea.anagrafica.domain.ContattoSedeEntita;
import it.eurotn.panjea.anagrafica.domain.Entita;
import it.eurotn.panjea.anagrafica.domain.SedeEntita;
import it.eurotn.panjea.anagrafica.service.exception.AnagraficaServiceException;
import it.eurotn.panjea.anagrafica.service.exception.ContattoOrphanException;

import java.util.List;

import javax.ejb.Local;

@Local
public interface ContattiManager {

	/**
	 * Esegue la cancellazione di tutti i <code>ContattoSedeEntita</code> e di tutti i <code>Contatto</code>.
	 * 
	 * @param entita
	 *            entità di riferimento
	 */
	void cancellaContattiPerEntita(Entita entita);

	/**
	 * Esegue la cancellazione di tutti i <code>ContattoSedeEntita</code> e di tutti i <code>Contatto</code>.
	 * 
	 * @param sedeEntita
	 *            sede di riferimento
	 */
	void cancellaContattiPerSedeEntita(SedeEntita sedeEntita);

	/**
	 * esegue la cancellazione di <code>Contatto</code>.
	 * 
	 * @param contatto
	 *            contatto da cancellare
	 */
	void cancellaContatto(Contatto contatto);

	/**
	 * Esegue la cancellazione di <code>MansioneSede</code> e verifica in caso di deleteOrphan==true se il contatto
	 * risulta essere orfano ed eventualmente rilancia una <code>ContattoOrphanException</code>.
	 * 
	 * @param contattoSedeEntita
	 *            contattoSedeEntita da cancellare
	 * @throws AnagraficaServiceException
	 *             eccezione generica
	 * @throws ContattoOrphanException
	 *             {@link ContattoOrphanException}
	 */
	void cancellaContattoSedeEntita(ContattoSedeEntita contattoSedeEntita) throws AnagraficaServiceException,
			ContattoOrphanException;

	/**
	 * Carica indistintamente tutti i <code>Contatto</code> di <code>Entita</code>.
	 * 
	 * @param entita
	 *            entità di riferimento
	 * @return contatti
	 */
	List<Contatto> caricaContattiPerEntita(Entita entita);

	/**
	 * Restituisce tutte le mansioni di una data Entita.
	 * 
	 * @param entita
	 *            entità di riferimento
	 * @return mansioni
	 * @throws AnagraficaServiceException
	 *             eccezione generica
	 */
	List<ContattoSedeEntita> caricaContattiSedeEntitaPerEntita(Entita entita) throws AnagraficaServiceException;

	/**
	 * Restituisce tutte le mansioni di una data SedeEntita.
	 * 
	 * @param sedeEntita
	 *            sede di riferimento
	 * @return contatti
	 * @throws AnagraficaServiceException
	 *             eccezione generica
	 */
	List<ContattoSedeEntita> caricaContattiSedeEntitaPerSedeEntita(SedeEntita sedeEntita)
			throws AnagraficaServiceException;

	/**
	 * restituisce {@link Contatto} identificato da idContatto.
	 * 
	 * @param idContatto
	 *            id del contatto
	 * @return contatto caricato
	 * @throws AnagraficaServiceException
	 *             eccezione generica
	 */
	Contatto caricaContatto(java.lang.Integer idContatto) throws AnagraficaServiceException;

	/**
	 * Carica un {@link ContattoSedeEntita}.
	 * 
	 * @param idContatto
	 *            id del contatto
	 * @return contatto caricato
	 */
	ContattoSedeEntita caricaContattoSedeEntita(java.lang.Integer idContatto);

	/**
	 * Esegue la ricerca tra tutti i Contatto esistenti filtrando per i valori contenuti nella <code>Map</code>
	 * parametri.
	 * 
	 * @param parametri
	 *            parametri di ricerca
	 * @return contatti caricati
	 */
	List<Contatto> ricercaContatti(java.util.Map<String, Object> parametri);

	/**
	 * Esegue la ricerca tra tutti i Contatto di un <code>Entita</code> filtrando per i valori contenuti nella
	 * <code>Map</code> parametri.
	 * 
	 * @param entita
	 *            entita di riferimento
	 * @param parametri
	 *            parametri di ricerca
	 * @return contatti caricati
	 */
	List<Contatto> ricercaContattiPerEntita(Entita entita, java.util.Map<String, Object> parametri);

	/**
	 * Esegue il salvataggio di un istanza di <code>Contatto</code>.
	 * 
	 * @param contatto
	 *            da salvare
	 * @return contatto salvato
	 */
	Contatto salvaContatto(Contatto contatto);

	/**
	 * Esegue il salvataggio un {@link ContattoSedeEntita}.
	 * 
	 * @param contattoSedeEntita
	 *            contatto da salvare
	 * @return contatto salvato
	 */
	ContattoSedeEntita salvaContattoSedeEntita(ContattoSedeEntita contattoSedeEntita);

}
