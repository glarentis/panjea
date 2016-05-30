/**
 * 
 */
package it.eurotn.panjea.magazzino.manager.schedearticolo.interfaces;

import it.eurotn.panjea.magazzino.util.ElaborazioneSchedaArticoloDTO;
import it.eurotn.panjea.magazzino.util.parametriricerca.ParametriRicercaElaborazioni;

import java.util.List;

import javax.ejb.Local;

/**
 * @author fattazzo
 * 
 */
@Local
public interface MagazzinoElaborazioniSchedeArticoloManager {

	/**
	 * Carica le elaborazioni per il mese e anno specificato.
	 * 
	 * @param parametri
	 *            parametri di ricerca
	 * @return elaborazioni
	 */
	List<ElaborazioneSchedaArticoloDTO> caricaElaborazioniSchedeArticolo(ParametriRicercaElaborazioni parametri);

	/**
	 * Restituisce il numero di schede articolo in elaborazione presenti sulla coda.
	 * 
	 * @return numero di schede
	 */
	int caricaNumeroSchedeArticoloInCodaDiElaborazione();

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
