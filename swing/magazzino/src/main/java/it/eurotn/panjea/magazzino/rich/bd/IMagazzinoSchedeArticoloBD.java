/**
 * 
 */
package it.eurotn.panjea.magazzino.rich.bd;

import it.eurotn.panjea.magazzino.util.ArticoloRicerca;
import it.eurotn.panjea.magazzino.util.ElaborazioneSchedaArticoloDTO;
import it.eurotn.panjea.magazzino.util.SituazioneSchedaArticoloDTO;
import it.eurotn.panjea.magazzino.util.parametriricerca.ParametriCreazioneSchedeArticoli;
import it.eurotn.panjea.magazzino.util.parametriricerca.ParametriRicercaElaborazioni;
import it.eurotn.panjea.rich.bd.AsyncMethodInvocation;

import java.io.FileNotFoundException;
import java.util.List;

/**
 * @author fattazzo
 * 
 */
public interface IMagazzinoSchedeArticoloBD {

	/**
	 * Carica tutti gli articoli non validi per le schede del mese e anno passato come parametro.
	 * 
	 * @param anno
	 *            anno
	 * @param mese
	 *            mese
	 * @return articoli
	 */
	@AsyncMethodInvocation
	List<ArticoloRicerca> caricaArticoliNonValidi(Integer anno, Integer mese);

	/**
	 * Carica tutti gli articoli non presenti nelle schede del mese e anno passato come parametro.
	 * 
	 * @param anno
	 *            anno
	 * @param mese
	 *            mese
	 * @return articoli
	 */
	@AsyncMethodInvocation
	List<ArticoloRicerca> caricaArticoliRimanenti(Integer anno, Integer mese);

	/**
	 * Carica tutti gli articoli presente nelle schede del mese e anno passato come parametro.
	 * 
	 * @param anno
	 *            anno
	 * @param mese
	 *            mese
	 * @return articoli
	 */
	@AsyncMethodInvocation
	List<ArticoloRicerca> caricaArticoliStampati(Integer anno, Integer mese);

	/**
	 * Carica le elaborazioni per il mese e anno specificato.
	 * 
	 * @param parametri
	 *            parametri di ricerca
	 * @return elaborazioni
	 */
	@AsyncMethodInvocation
	List<ElaborazioneSchedaArticoloDTO> caricaElaborazioniSchedeArticolo(ParametriRicercaElaborazioni parametri);

	/**
	 * Restituisce il file della scheda per l'articolo e periodo specificato.
	 * 
	 * @param anno
	 *            anno
	 * @param mese
	 *            mese
	 * @param idArticolo
	 *            id articolo
	 * @return scheda
	 * @throws FileNotFoundException
	 *             sollevata se non viene trovato il file per la scheda
	 */
	@AsyncMethodInvocation
	byte[] caricaFileSchedaArticolo(Integer anno, Integer mese, Integer idArticolo) throws FileNotFoundException;

	/**
	 * Restituisce il numero di schede in elaborazione presenti sulla coda.
	 * 
	 * @return numero di schede
	 */
	int caricaNumeroSchedeArticoloInCodaDiElaborazione();

	/**
	 * Carica la situazione delle schede articolo per il periodo indicato.
	 * 
	 * @param anno
	 *            anno
	 * @return situazione caricata
	 */
	@AsyncMethodInvocation
	List<SituazioneSchedaArticoloDTO> caricaSituazioneSchedeArticolo(Integer anno);

	/**
	 * Crea le schede articoli in base ai parametri di creazione.
	 * 
	 * @param parametriCreazioneSchedeArticoli
	 *            parametri di creazione
	 */
	@AsyncMethodInvocation
	void creaSchedeArticolo(ParametriCreazioneSchedeArticoli parametriCreazioneSchedeArticoli);

	/**
	 * Modifica la descrizione dell'elaborazione da descrizioneOld a descrizioneNew.
	 * 
	 * @param descrizioneOld
	 *            vecchia descrizione
	 * @param descrizioneNew
	 *            nuova descrizione
	 */
	void modificaDescrizioneElaborazione(String descrizioneOld, String descrizioneNew);
}
