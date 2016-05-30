package it.eurotn.panjea.magazzino.manager.interfaces;

import it.eurotn.panjea.anagrafica.domain.Entita;
import it.eurotn.panjea.anagrafica.domain.SedeEntita;
import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.magazzino.domain.CategoriaSedeMagazzino;
import it.eurotn.panjea.magazzino.domain.NoteAreaMagazzino;
import it.eurotn.panjea.magazzino.domain.SedeMagazzino;
import it.eurotn.panjea.magazzino.domain.SedeMagazzinoLite;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.ejb.Local;

@Local
public interface SediMagazzinoManager {

	/**
	 * Associa la sede per rifatturazione a tutte le sedi entita.
	 * 
	 * @param sediEntita
	 *            lista di sedi entita
	 * @param sedePerRifatturazione
	 *            sede per rifatturazione da associare
	 */
	void associaSediASedePerRifatturazione(List<SedeEntita> sediEntita, SedeMagazzinoLite sedePerRifatturazione);

	/**
	 * Associa le sedi magazzino ad una sede di rifatturazione.
	 * 
	 * @param sedeDiRifatturazione
	 *            sede di rifatturazione
	 * @param sediDaAssociare
	 *            sede da associare
	 */
	void associaSediMagazzinoPerRifatturazione(SedeMagazzinoLite sedeDiRifatturazione,
			List<SedeMagazzinoLite> sediDaAssociare);

	/**
	 * Cancella la categoria sede magazzino.
	 * 
	 * @param categoriaSedeMagazzino
	 *            {@link CategoriaSedeMagazzino} da cancellare
	 */
	void cancellaCategorieSediMagazzino(CategoriaSedeMagazzino categoriaSedeMagazzino);

	/**
	 * Cancella la sede magazzino associata alla sede entita.
	 * 
	 * @param sedeEntita
	 *            sede dell' entita
	 */
	void cancellaSedeMagazzino(SedeEntita sedeEntita);

	/**
	 * Cancella una sede magazzino.
	 * 
	 * @param sedeMagazzino
	 *            sede magazzino da cancellare
	 */
	void cancellaSedeMagazzino(SedeMagazzino sedeMagazzino);

	/**
	 * Carica una <code>CategoriaSedeMagazzino</code>.
	 * 
	 * @param sedeEntita
	 *            sede dell'entita
	 * @return <code>CategoriaSedeMagazzino</code> caricata
	 */
	CategoriaSedeMagazzino caricaCategoriaSedeMagazzinoBySede(SedeEntita sedeEntita);

	/**
	 * Carica tutte le categorie sedi magazzione per l'azienda loggata.
	 * 
	 * @param fieldSearch
	 *            .
	 * @param valueSearch
	 *            .
	 * @return <code>List</code> di {@link CategoriaSedeMagazzino}
	 */
	List<CategoriaSedeMagazzino> caricaCategorieSediMagazzino(String fieldSearch, String valueSearch);

	/**
	 * Carica tutta le <code>CategoriaSedeMagazzino</code> non associate da un contratto.
	 * 
	 * @return lista di categorie caricate
	 */
	List<CategoriaSedeMagazzino> caricaCategorieSediMagazzinoSenzaContratto();

	/**
	 * Carica le note blocco, le note sede, le note entita magazzino.
	 * 
	 * @param sedeEntita
	 *            la sede entita di cui caricare le note
	 * @return un array con le note blocco, sede e note entita magazzino
	 */
	NoteAreaMagazzino caricaNoteSede(SedeEntita sedeEntita);

	/**
	 * Carica la sede magazzino associata alla sede entità.
	 * 
	 * @param sedeEntita
	 *            sede dell'entita
	 * @param ignoraEreditaDatiCommerciali
	 *            ignora il flag eredita dati commerciali
	 * @return <code>SedeMagazzino</code> caricata
	 */
	SedeMagazzino caricaSedeMagazzinoBySedeEntita(SedeEntita sedeEntita, boolean ignoraEreditaDatiCommerciali);

	/**
	 * restituisce la {@link SedeMagazzino} associata alla {@link SedeEntita} principale dell'argomeno entita.
	 * 
	 * @param entita
	 *            entita
	 * @return {@link SedeMagazzino}
	 */
	SedeMagazzino caricaSedeMagazzinoPrincipale(Entita entita);

	/**
	 * Carica le sedi magazzino in base ai parametri forniti.
	 * 
	 * @param parametri
	 *            parametri
	 * @param textAsLike
	 *            textAsLike
	 * @return {@link Collection} con gli oggetti {@link SedeMagazzino} che rispondo ai criteri di ricerca
	 */
	List<SedeMagazzino> caricaSediMagazzino(Map<String, Object> parametri, boolean textAsLike);

	/**
	 * Carica le sedi magazzino dell'entità.
	 * 
	 * @param entita
	 *            entita
	 * @return <code>List</code> di <code>SedeMagazzinoLite</code> caricate. <br/>
	 *         Escludo le sedi di tipologia INDIRIZZO_SPEDIZIONE e SERVIZIO perchè la parte di magazzino non interessa
	 */
	List<SedeMagazzinoLite> caricaSediMagazzinoByEntita(Entita entita);

	/**
	 * Carica tutte le sedi magazzino che possono essere usate per la rifatturazione.
	 * 
	 * @return lista di sedi caricate
	 */
	List<SedeMagazzinoLite> caricaSediMagazzinoDiRifatturazione();

	/**
	 * Carica tutte le sedi magazzino che hanno una sede per rifatturazione.
	 * 
	 * 
	 * @return sedi caricate
	 */
	List<SedeMagazzinoLite> caricaSediRifatturazioneAssociate();

	/**
	 * Carica tutte le sedi magazzino dell'entità che non hanno un sede per rifatturazione.
	 * 
	 * @param entita
	 *            entita di riferimento
	 * @return sedi caricate
	 */
	List<SedeMagazzinoLite> caricaSediRifatturazioneNonAssociate(EntitaLite entita);

	/**
	 * Rimuove la sede di rifatturazione alla sede magazzino passata come parametro.
	 * 
	 * @param sedeMagazzino
	 *            sede a cui rimuovere la sede per rifatturazione
	 */
	void rimuoviSedePerRifatturazione(SedeMagazzinoLite sedeMagazzino);

	/**
	 * Salva una <code>CategoriaSedeMagazzino</code>.
	 * 
	 * @param categoriaSedeMagazzino
	 *            <code>CategoriaSedeMagazzino</code> da salvare
	 * @return <code>CategoriaSedeMagazzino</code> salvata
	 */
	CategoriaSedeMagazzino salvaCategoriaSedeMagazzino(CategoriaSedeMagazzino categoriaSedeMagazzino);

	/**
	 * Salva una <code>SedeMagazzino</code>.
	 * 
	 * @param sedeMagazzino
	 *            <code>SedeMagazzino</code> da salvare
	 * @return <code>SedeMagazzino</code> salvata
	 */
	SedeMagazzino salvaSedeMagazzino(SedeMagazzino sedeMagazzino);
}
