/**
 * 
 */
package it.eurotn.panjea.magazzino.manager.schedearticolo.interfaces;

import it.eurotn.panjea.magazzino.domain.SchedaArticolo;
import it.eurotn.panjea.magazzino.util.ArticoloRicerca;
import it.eurotn.panjea.magazzino.util.MovimentazioneArticolo;
import it.eurotn.panjea.magazzino.util.SituazioneSchedaArticoloDTO;
import it.eurotn.panjea.magazzino.util.parametriricerca.ParametriCreazioneSchedeArticoli;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;

import javax.ejb.Local;

/**
 * @author fattazzo
 * 
 */
@Local
public interface MagazzinoSchedeArticoloManager {

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
	 * Carica la scheda richiesta.
	 * 
	 * @param anno
	 *            anno
	 * @param mese
	 *            mese
	 * @param idArticolo
	 *            id dell'articolo
	 * @return Scheda articolo caricata, se non esiste ne crea uno nuova
	 */
	SchedaArticolo caricaSchedaArticolo(Integer anno, Integer mese, Integer idArticolo);

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
	 * Salva una scheda articolo.
	 * 
	 * @param schedaArticolo
	 *            scheda da salvare
	 * @return scheda salvato
	 */
	SchedaArticolo salvaSchedaArticolo(SchedaArticolo schedaArticolo);

}
