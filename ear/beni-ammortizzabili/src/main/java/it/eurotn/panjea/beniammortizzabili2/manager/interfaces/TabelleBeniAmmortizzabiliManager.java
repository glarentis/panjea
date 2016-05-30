package it.eurotn.panjea.beniammortizzabili2.manager.interfaces;

import it.eurotn.panjea.beniammortizzabili.exception.BeniAmmortizzabiliException;
import it.eurotn.panjea.beniammortizzabili2.domain.Gruppo;
import it.eurotn.panjea.beniammortizzabili2.domain.SottoSpecie;
import it.eurotn.panjea.beniammortizzabili2.domain.Specie;
import it.eurotn.panjea.beniammortizzabili2.domain.TipologiaEliminazione;
import it.eurotn.panjea.beniammortizzabili2.domain.Ubicazione;

import java.util.List;

import javax.ejb.Local;

@Local
public interface TabelleBeniAmmortizzabiliManager {

	/**
	 * Cancella il {@link Gruppo}.
	 * 
	 * @param gruppo
	 *            gruppo da cancellare
	 */
	void cancellaGruppo(Gruppo gruppo);

	/**
	 * cancella {@link SottoSpecie}.
	 * 
	 * @param sottoSpecie
	 *            sottospecie da cancellare
	 */
	void cancellaSottoSpecie(SottoSpecie sottoSpecie);

	/**
	 * cancella {@link Specie}.
	 * 
	 * @param specie
	 *            specie da cancellare
	 * @throw BeniAmmortizzabiliException exception
	 */
	void cancellaSpecie(Specie specie);

	/**
	 * cancella {@link TipologiaEliminazione}.
	 * 
	 * @param tipologiaEliminazione
	 *            tipologia da eliminare
	 */
	void cancellaTipologiaEliminazione(TipologiaEliminazione tipologiaEliminazione);

	/**
	 * cancella {@link Ubicazione}.
	 * 
	 * @param ubicazione
	 *            Ubicazione da cancellare
	 */
	void cancellaUbicazione(Ubicazione ubicazione);

	/**
	 * carica {@link List} di {@link Gruppo}.
	 * 
	 * @param fieldSearch
	 *            .
	 * @param valueSearch
	 *            .
	 * @return Gruppi caricati
	 * @throws BeniAmmortizzabiliException
	 *             exception
	 */
	List<Gruppo> caricaGruppi(String fieldSearch, String valueSearch) throws BeniAmmortizzabiliException;

	/**
	 * carica {@link Gruppo} per l'azienda corrente.
	 * 
	 * @return gruppo azienda caricato
	 */
	Gruppo caricaGruppoAzienda();

	/**
	 * carica {@link Gruppo} per l'azienda corrente.
	 * 
	 * @param codiceGruppo
	 *            codcie del gruppo da caricare
	 * @return gruppo caricato
	 */
	Gruppo caricaGruppoByCodice(String codiceGruppo);

	/**
	 * carica {@link SottoSpecie}.
	 * 
	 * @param id
	 *            id della sottospecie da caricare
	 * @return sottospecie caricata
	 * @throws BeniAmmortizzabiliException
	 *             exception
	 */
	SottoSpecie caricaSottoSpecie(Integer id) throws BeniAmmortizzabiliException;

	/**
	 * carica {@link List} di {@link SottoSpecie}.
	 * 
	 * @param fieldSearch
	 *            .
	 * @param valueSearch
	 *            .
	 * @return sottospecie caricate
	 * @throws BeniAmmortizzabiliException
	 *             exception
	 */
	List<SottoSpecie> caricaSottoSpecie(String fieldSearch, String valueSearch) throws BeniAmmortizzabiliException;

	/**
	 * carica Specie per azienda corrente.
	 * 
	 * @param fieldSearch
	 *            .
	 * @param valueSearch
	 *            .
	 * @return speci caricate
	 * @throws BeniAmmortizzabiliException
	 *             exception
	 */
	List<Specie> caricaSpeci(String fieldSearch, String valueSearch) throws BeniAmmortizzabiliException;

	/**
	 * carica {@link List} di {@link Specie} per {@link Gruppo}.
	 * 
	 * @param gruppo
	 *            gruppo di riferimento
	 * @return speci caricate
	 * @throws BeniAmmortizzabiliException
	 *             exception
	 */
	List<Specie> caricaSpecie(Gruppo gruppo) throws BeniAmmortizzabiliException;

	/**
	 * carica Specie.
	 * 
	 * @param id
	 *            id della specie da caricare
	 * @return Specie caricata
	 * @throws BeniAmmortizzabiliException
	 *             exception
	 */
	Specie caricaSpecie(Integer id) throws BeniAmmortizzabiliException;

	/**
	 * carica {@link TipologiaEliminazione}.
	 * 
	 * @param id
	 *            id della tipologia da caricare
	 * @return Tipologia caricata
	 * @throws BeniAmmortizzabiliException
	 *             exception
	 */
	TipologiaEliminazione caricaTipologiaEliminazione(Integer id) throws BeniAmmortizzabiliException;

	/**
	 * carica {@link List} di {@link TipologiaEliminazione}.
	 * 
	 * @param codice
	 *            codice da filtrare
	 * @return tipologie caricate
	 */
	List<TipologiaEliminazione> caricaTipologieEliminazione(String codice);

	/**
	 * carica {@link Ubicazione}.
	 * 
	 * @param id
	 *            id dell'ubicazione caricata
	 * @return Ubicazione caricata
	 * @throws BeniAmmortizzabiliException
	 *             exception
	 */
	Ubicazione caricaUbicazione(Integer id) throws BeniAmmortizzabiliException;

	/**
	 * Carica l'ubicazione filtrata per azienda e codice.
	 * 
	 * @param codiceUbiazione
	 *            codice della ubicazione
	 * @return Ubicazione caricata
	 */
	Ubicazione caricaUbicazioneByCodice(String codiceUbiazione);

	/**
	 * carica {@link List} di {@link Ubicazione}.
	 * 
	 * @param codice
	 *            codice da filtrare
	 * @return ubicazioni caricate
	 */
	List<Ubicazione> caricaUbicazioni(String codice);

	/**
	 * salva {@link Gruppo}.
	 * 
	 * @param gruppo
	 *            gruppo da salvare
	 * @return Gruppo gruppo salvato
	 */
	Gruppo salvaGruppo(Gruppo gruppo);

	/**
	 * salva {@link SottoSpecie}.
	 * 
	 * @param sottoSpecie
	 *            sottospecie da salvare
	 * @return sottospecie salvata
	 */
	SottoSpecie salvaSottoSpecie(SottoSpecie sottoSpecie);

	/**
	 * salva {@link Specie}.
	 * 
	 * @param specie
	 *            specie da salvare
	 * @return specie salvata
	 */
	Specie salvaSpecie(Specie specie);

	/**
	 * salva {@link TipologiaEliminazione}.
	 * 
	 * @param tipologiaEliminazione
	 *            tipologia da salvare
	 * @return tipologia salvata
	 */
	TipologiaEliminazione salvaTipologiaEliminazione(TipologiaEliminazione tipologiaEliminazione);

	/**
	 * salva {@link Ubicazione}.
	 * 
	 * @param ubicazione
	 *            ubicazione da salvare
	 * @return ubicazione salvata
	 */
	Ubicazione salvaUbicazione(Ubicazione ubicazione);

	/**
	 * Imposta il gruppo predefinito per l'azienda loggata togliendo l'eventuale associazione al gruppo precedente
	 * selezionato.
	 * 
	 * @param gruppo
	 *            Gruppo da associare all'azienda
	 * @return Gruppo associato all'azienda,
	 */
	Gruppo settaGruppoAzienda(Gruppo gruppo);

}
