/**
 * 
 */
package it.eurotn.panjea.magazzino.service.interfaces;

import it.eurotn.panjea.magazzino.util.ArticoloRicerca;
import it.eurotn.panjea.magazzino.util.ElaborazioneSchedaArticoloDTO;
import it.eurotn.panjea.magazzino.util.MovimentazioneArticolo;
import it.eurotn.panjea.magazzino.util.SituazioneSchedaArticoloDTO;
import it.eurotn.panjea.magazzino.util.parametriricerca.ParametriCreazioneSchedeArticoli;
import it.eurotn.panjea.magazzino.util.parametriricerca.ParametriRicercaElaborazioni;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;

import javax.ejb.Remote;

/**
 * @author fattazzo
 * 
 */
@Remote
public interface MagazzinoSchedeArticoloService {

	/**
	 * Carica tutti gli articoli non validi per la scheda del mese e anno passato come parametro.
	 * 
	 * @param anno
	 *            anno
	 * @param mese
	 *            mese
	 * @return articoli
	 */
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
	List<ArticoloRicerca> caricaArticoliStampati(Integer anno, Integer mese);

	/**
	 * Carica le elaborazioni per il mese e anno specificato.
	 * 
	 * @param parametri
	 *            parametri di ricerca
	 * @return elaborazioni
	 */
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
	 * @return file della scheda articolo
	 * @throws FileNotFoundException
	 *             sollevata se non viene trovato il file per la scheda
	 */
	byte[] caricaFileSchedaArticolo(Integer anno, Integer mese, Integer idArticolo) throws FileNotFoundException;

	/**
	 * Restituisce il numero di schede in elaborazione presenti sulla coda.
	 * 
	 * @return numero di schede
	 */
	int caricaNumeroSchedeArticoloInCodaDiElaborazione();

	/**
	 * Carica la scheda articolo pe rl'articolo e il periodo specificato.
	 * 
	 * @param params
	 *            parametri
	 * @return scheda
	 */
	List<MovimentazioneArticolo> caricaSchedaArticolo(Map<Object, Object> params);

	/**
	 * Carica la situazione delle schede articolo per il periodo indicato.
	 * 
	 * @param anno
	 *            anno
	 * @return situazione caricata
	 */
	List<SituazioneSchedaArticoloDTO> caricaSituazioneSchedeArticolo(Integer anno);

	/**
	 * Crea le schede articoli in base ai parametri di creazione.
	 * 
	 * @param parametriCreazioneSchedeArticoli
	 *            parametri di creazione
	 */
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
