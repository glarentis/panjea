package it.eurotn.panjea.anagrafica.manager.interfaces;

import it.eurotn.dao.exception.DuplicateKeyObjectException;
import it.eurotn.dao.exception.StaleObjectStateException;
import it.eurotn.panjea.anagrafica.domain.Azienda;
import it.eurotn.panjea.anagrafica.domain.RapportoBancarioAzienda;
import it.eurotn.panjea.anagrafica.domain.SedeAzienda;
import it.eurotn.panjea.anagrafica.domain.TipoPagamento;
import it.eurotn.panjea.anagrafica.domain.TipoSedeEntita;
import it.eurotn.panjea.anagrafica.domain.lite.AziendaLite;
import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.anagrafica.domain.lite.SedeEntitaLite;
import it.eurotn.panjea.anagrafica.service.exception.AnagraficaServiceException;
import it.eurotn.panjea.anagrafica.service.exception.SedeEntitaPrincipaleAlreadyExistException;
import it.eurotn.panjea.anagrafica.util.AziendaAnagraficaDTO;

import java.util.List;

import javax.ejb.Local;

@Local
public interface AziendeManager {

	/**
	 * esegue la variazione della {@link SedeAzienda} principale.
	 *
	 * @param nuovaSedeAziendaPrincipaleAzienda
	 *            nuova sede principale
	 * @param tipoSedeSostitutivaEntita
	 *            tipo sede della sede principale sostituita
	 * @throws AnagraficaServiceException
	 *             eccezione generica
	 */
	void cambiaSedePrincipaleAzienda(SedeAzienda nuovaSedeAziendaPrincipaleAzienda,
			TipoSedeEntita tipoSedeSostitutivaEntita) throws AnagraficaServiceException;

	/**
	 *
	 * @param rapportoBancario
	 *            rapporto bancario azienda da cancellare
	 * @throws AnagraficaServiceException
	 *             eccezione generica
	 */
	void cancellaRapportoBancario(RapportoBancarioAzienda rapportoBancario) throws AnagraficaServiceException;

	/**
	 * Carica l'azienda correntemente loggata.
	 *
	 * @return azienda caricata
	 */
	AziendaLite caricaAzienda();

	/**
	 * Carica l'azienda correntemente loggata.
	 *
	 * @param loadSede
	 *            se <code>true</code> viene caricata la sede principale dell'azienda
	 * @return azienda caricata
	 */
	AziendaLite caricaAzienda(boolean loadSede);

	/**
	 * restituisce {@link AziendaLite} identificate da codiceAzienda .
	 *
	 * @param codiceAzienda
	 *            codice azienda
	 * @return azienda caricata
	 * @throws AnagraficaServiceException
	 *             eccezione generica
	 */
	AziendaLite caricaAzienda(String codiceAzienda) throws AnagraficaServiceException;

	/**
	 * restituisce {@link AziendaLite} identificate da codiceAzienda.
	 *
	 * @param codiceAzienda
	 *            codice azienda
	 * @param loadSede
	 *            se <code>true</code> carica la sede principale dell'azienda
	 * @return azienda caricata
	 * @throws AnagraficaServiceException
	 *             eccezione generica
	 */
	AziendaLite caricaAzienda(String codiceAzienda, boolean loadSede) throws AnagraficaServiceException;

	/**
	 * recupera {@link Azienda} e {@link SedeAzienda} principale e li restituisce all'interno di
	 * {@link AziendaAnagraficaDTO}.
	 *
	 * @param codice
	 *            codice azienda
	 * @return azienda anagrafica caricata
	 * @throws AnagraficaServiceException
	 *             eccezione generica
	 */
	AziendaAnagraficaDTO caricaAziendaAnagrafica(String codice) throws AnagraficaServiceException;

	/**
	 * recupera {@link Azienda} per codice.
	 *
	 * @param codice
	 *            codice azienda
	 * @return azienda caricata
	 * @throws AnagraficaServiceException
	 *             eccezione generica
	 */
	Azienda caricaAziendaByCodice(String codice) throws AnagraficaServiceException;

	/**
	 * restituisce {@link List} di {@link AziendaLite}.
	 *
	 * @return azienda caricate
	 * @throws AnagraficaServiceException
	 *             eccezione generica
	 */
	List<AziendaLite> caricaAziende() throws AnagraficaServiceException;

	/**
	 * Recupera {@link List} di {@link RapportoBancarioAzienda}.
	 *
	 * @param fieldSearch
	 *            campo da filtrare
	 * @param valueSearch
	 *            valore da cercare
	 * @return rapporti bancari caricati
	 * @throws AnagraficaServiceException
	 *             eccezione generica
	 */
	List<RapportoBancarioAzienda> caricaRapportiBancariAzienda(String fieldSearch, String valueSearch)
			throws AnagraficaServiceException;

	/**
	 * Recupera {@link RapportoBancarioAzienda} identificato da idRapportoBancario.
	 *
	 * @param idRapportoBancario
	 *            id del rapporto bancario
	 * @param initializeLazy
	 *            se <code>true</code> inizializza le proprietà lazy di {@link RapportoBancarioAzienda}
	 * @return rapporto bancario caricato
	 */
	RapportoBancarioAzienda caricaRapportoBancario(Integer idRapportoBancario, boolean initializeLazy);

	/**
	 * restituisce {@link RapportoBancarioAzienda} dando prioprità all'entità o a quello che ha il tipoPagamento di
	 * default per il tipo pagamento passato<br>
	 * . Se non esiste il tipo di default restituisce il primo.
	 *
	 * @param entita
	 *            entita
	 * @param sedeEntita
	 *            sede entita
	 * @param tipoPagamento
	 *            tipo pagamento
	 * @return rapporto caricato
	 */
	RapportoBancarioAzienda caricaRapportoBancarioAzienda(EntitaLite entita, SedeEntitaLite sedeEntita,
			TipoPagamento tipoPagamento);

	/**
	 * esegue il salvataggio di {@link Azienda}.
	 *
	 * @param azienda
	 *            azienda da salvare
	 * @return azienda salvata
	 * @throws AnagraficaServiceException
	 *             eccezione generica
	 */
	Azienda salvaAzienda(Azienda azienda) throws AnagraficaServiceException;

	/**
	 * Esegue il salvataggio di {@link Azienda} e {@link SedeAzienda}.
	 *
	 * @param aziendaAnagraficaDTO
	 *            azienda da salvare
	 * @return azienda salvata
	 * @throws AnagraficaServiceException
	 *             eccezione generica
	 * @throws DuplicateKeyObjectException
	 *             DuplicateKeyObjectException
	 * @throws StaleObjectStateException
	 *             StaleObjectStateException
	 * @throws SedeEntitaPrincipaleAlreadyExistException
	 *             SedeEntitaPrincipaleAlreadyExistException
	 */
	AziendaAnagraficaDTO salvaAziendaAnagrafica(AziendaAnagraficaDTO aziendaAnagraficaDTO)
			throws AnagraficaServiceException, DuplicateKeyObjectException, StaleObjectStateException,
			SedeEntitaPrincipaleAlreadyExistException;

	/**
	 *
	 * @param rapportoBancarioAzienda
	 *            rapporto bancario azienda da salvare
	 * @return rapporto bancario salvato
	 * @throws AnagraficaServiceException
	 *             eccezione generica
	 */
	RapportoBancarioAzienda salvaRapportoBancarioAzienda(RapportoBancarioAzienda rapportoBancarioAzienda)
			throws AnagraficaServiceException;

}
