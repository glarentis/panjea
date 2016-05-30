package it.eurotn.panjea.anagrafica.manager.interfaces;

import it.eurotn.dao.exception.ObjectNotFoundException;
import it.eurotn.panjea.anagrafica.domain.Carica;
import it.eurotn.panjea.anagrafica.domain.FormaGiuridica;
import it.eurotn.panjea.anagrafica.domain.Mansione;
import it.eurotn.panjea.anagrafica.domain.TipoDeposito;
import it.eurotn.panjea.anagrafica.domain.TipoSedeEntita;
import it.eurotn.panjea.anagrafica.domain.TipoSedeEntita.TipoSede;
import it.eurotn.panjea.anagrafica.service.exception.AnagraficaServiceException;
import it.eurotn.panjea.anagrafica.service.exception.TipoSedeEntitaNonTrovataException;

import java.util.List;

import javax.ejb.Local;

@Local
public interface AnagraficheTipologieManager {

	/**
	 * Cancella una carica.
	 * 
	 * @param carica
	 *            la carica da cancellare
	 * @throws AnagraficaServiceException
	 *             eccezione generica
	 */
	void cancellaCarica(Carica carica) throws AnagraficaServiceException;

	/**
	 * Cancella una forma giuridica.
	 * 
	 * @param formaGiuridica
	 *            Value Object della forma giuridica da eliminare
	 * @throws AnagraficaServiceException
	 *             eccezione generica
	 * 
	 */
	void cancellaFormaGiuridica(FormaGiuridica formaGiuridica) throws AnagraficaServiceException;

	/**
	 * cancella {@link Mansione}.
	 * 
	 * @param mansione
	 *            mansione da cancellare
	 * @throws AnagraficaServiceException
	 *             eccezione generica
	 */
	void cancellaMansione(Mansione mansione) throws AnagraficaServiceException;

	/**
	 * 
	 * @param tipoDeposito
	 *            tipoDeposito da cancellare
	 * @throws AnagraficaServiceException
	 *             eccezione generica
	 */
	void cancellaTipoDeposito(TipoDeposito tipoDeposito) throws AnagraficaServiceException;

	/**
	 * Cancella {@link TipoSedeEntita}.
	 * 
	 * @param tipoSedeEntita
	 *            tipoSedeEntita da cancellare
	 * @throws AnagraficaServiceException
	 *             eccezione generica
	 */
	void cancellaTipoSedeEntita(TipoSedeEntita tipoSedeEntita) throws AnagraficaServiceException;

	/**
	 * Recupera la lista completa di Cariche.
	 * 
	 * @param fieldSearch
	 *            .
	 * @param valueSearch
	 *            .
	 * @return Lista di <code>CaricaVO</code>
	 * @throws AnagraficaServiceException
	 *             eccezione generica
	 */
	List<Carica> caricaCariche(String fieldSearch, String valueSearch) throws AnagraficaServiceException;

	/**
	 * Carica una forma giuridica.
	 * 
	 * @param idFormaGiuridica
	 *            Codice della forma giuridica da caricare
	 * @return Value Object della forma giuridica caricata
	 * @throws AnagraficaServiceException
	 *             eccezione generica
	 * @throws ObjectNotFoundException
	 *             sollevata nel caso in cui la forma giuridica da caricare non viene trovata
	 * 
	 */
	FormaGiuridica caricaFormaGiuridica(Integer idFormaGiuridica) throws AnagraficaServiceException,
			ObjectNotFoundException;

	/**
	 * Recupera la {@link List} di {@link FormaGiuridica}.
	 * 
	 * @param fieldSearch
	 *            .
	 * @param valueSearch
	 *            .
	 * @return {@link FormaGiuridica} caricate
	 * @throws AnagraficaServiceException
	 *             eccezione generica
	 */
	List<FormaGiuridica> caricaFormeGiuridiche(String fieldSearch, String valueSearch)
			throws AnagraficaServiceException;

	/**
	 * carica la {@link List} di {@link Mansione}.
	 * 
	 * @param descrizione
	 *            .
	 * @return mansioni caricate
	 * @throws it.eurotn.panjea.anagrafica.service.exception.AnagraficaServiceException
	 *             eccezione generica
	 */
	List<Mansione> caricaMansioni(String descrizione)
			throws it.eurotn.panjea.anagrafica.service.exception.AnagraficaServiceException;

	/**
	 * Carica tutti i tipi depositi.
	 * 
	 * @return tipi depositi caricati
	 * @throws AnagraficaServiceException
	 *             eccezione generica
	 */
	List<TipoDeposito> caricaTipiDepositi() throws AnagraficaServiceException;

	/**
	 * Carica {@link List} di {@link TipoSedeEntita}.
	 * 
	 * 
	 * @param codice
	 *            codice da filtrare
	 * 
	 * @return tipi sede caricati
	 * @throws AnagraficaServiceException
	 *             eccezione generica
	 */
	List<TipoSedeEntita> caricaTipiSede(String codice) throws AnagraficaServiceException;

	/**
	 * Carica la {@link List} di {@link TipoSedeEntita} con attributo sedePrincipale = false.
	 * 
	 * @return tipi sede entita caricati
	 * @throws AnagraficaServiceException
	 *             eccezione generica
	 */
	List<TipoSedeEntita> caricaTipiSedeSecondari() throws AnagraficaServiceException;

	/**
	 * Carica il tipoSedeEntita per tipoSede, se ne esiste piu' d'uno prendo il primo.
	 * 
	 * @param tipoSede
	 *            il {@link TipoSede}
	 * @return TipoSedeEntita
	 * @throws TipoSedeEntitaNonTrovataException
	 *             se non ho tipoSedeEntita per il parametro scelto.
	 */
	TipoSedeEntita caricaTipoSedeEntitaByTipoSede(TipoSede tipoSede) throws TipoSedeEntitaNonTrovataException;

	/**
	 * Recupera il {@link TipoSedeEntita} principale.
	 * 
	 * @return tipo sede entita caricato
	 * @throws AnagraficaServiceException
	 *             eccezione generica
	 */
	TipoSedeEntita caricaTipoSedeEntitaPrincipale() throws AnagraficaServiceException;

	/**
	 * Rende persistente l'istanza di <code>CaricaVO</code> passata per parametro.
	 * 
	 * @param carica
	 *            carica da salvare
	 * @return carica salvata
	 */
	Carica salvaCarica(Carica carica);

	/**
	 * Salva una forma giuridica.
	 * 
	 * @param formaGiuridica
	 *            forma giuridica da salvare
	 * @return forma giuridica salvata
	 */
	FormaGiuridica salvaFormaGiuridica(FormaGiuridica formaGiuridica);

	/**
	 * salva {@link Mansione}.
	 * 
	 * @param mansione
	 *            mansione da salvare
	 * @return mansione salvata
	 */
	Mansione salvaMansione(Mansione mansione);

	/**
	 * @param tipoDeposito
	 *            tipo deposito da salvare
	 * @return tipo deposito salvato
	 * @throws AnagraficaServiceException
	 *             eccezione generica
	 */
	TipoDeposito salvaTipoDeposito(TipoDeposito tipoDeposito) throws AnagraficaServiceException;

	/**
	 * Salva {@link TipoSedeEntita}.
	 * 
	 * @param tipoSedeEntita
	 *            tipo sede entita da salvare
	 * @return tipo sede entita salvato
	 * @throws AnagraficaServiceException
	 *             eccezione generica
	 */
	TipoSedeEntita salvaTipoSedeEntita(TipoSedeEntita tipoSedeEntita) throws AnagraficaServiceException;

}
