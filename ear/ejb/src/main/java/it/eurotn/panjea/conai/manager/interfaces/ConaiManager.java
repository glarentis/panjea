package it.eurotn.panjea.conai.manager.interfaces;

import it.eurotn.panjea.conai.domain.ConaiArticolo;
import it.eurotn.panjea.conai.domain.ConaiArticolo.ConaiMateriale;
import it.eurotn.panjea.conai.domain.ConaiComponente;
import it.eurotn.panjea.conai.domain.ConaiParametriCreazione;
import it.eurotn.panjea.conai.domain.RigaConaiArticolo;
import it.eurotn.panjea.conai.util.AnalisiConaiDTO;
import it.eurotn.panjea.conai.util.parametriricerca.ParametriRicercaAnalisi;
import it.eurotn.panjea.magazzino.domain.ArticoloLite;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino;
import it.eurotn.panjea.magazzino.domain.documento.TipoAreaMagazzino;

import java.util.List;

import javax.ejb.Local;

@Local
public interface ConaiManager {

	/**
	 * Cancella {@link ConaiArticolo}.
	 * 
	 * @param conaiArticolo
	 *            l'articolo conai da cancellare
	 */
	void cancellaArticoloConai(ConaiArticolo conaiArticolo);

	/**
	 * Cancella {@link ConaiComponente}.
	 * 
	 * @param conaiComponente
	 *            il componente conai da cancellare
	 */
	void cancellaComponenteConai(ConaiComponente conaiComponente);

	/**
	 * Carica l'analisi della situazione CONAI.
	 * 
	 * @param parametri
	 *            parametri di ricerca
	 * @return analisi caricata
	 */
	List<AnalisiConaiDTO> caricaAnalisiConali(ParametriRicercaAnalisi parametri);

	/**
	 * Carica la lista di {@link ConaiArticolo}; nel caso in cui non siano presenti, vengono generate tante righe quanti
	 * sono i materiali {@link ConaiMateriale}.<br>
	 * 
	 * @return List<ConaiArticolo>
	 */
	List<ConaiArticolo> caricaArticoliConai();

	/**
	 * Carica l'articolo conai e se non esiste ne prepara uno per il materiale specificato.
	 * 
	 * @param materiale
	 *            il materiale di cui caricare/creare la riga
	 * @return ConaiArticolo del materiale scelto
	 */
	ConaiArticolo caricaArticoloConai(ConaiMateriale materiale);

	/**
	 * Carica la lista di {@link ConaiComponente}.
	 * 
	 * @param articolo
	 *            l'articolo di cui caricare i componenti conai (tipo imballo con relativo peso)
	 * 
	 * @return List<ConaiComponente>
	 */
	List<ConaiComponente> caricaComponentiConai(ArticoloLite articolo);

	/**
	 * Carica le righe conai generate di una area magazzino.
	 * 
	 * @param areaMagazzino
	 *            l'area di cui caricare le righe conai
	 * @return List<RigaConaiArticolo>
	 */
	List<RigaConaiArticolo> caricaRigheConai(AreaMagazzino areaMagazzino);

	/**
	 * Carica tutti i tipi area magazzino che hanno la gestione conai abilitata.
	 * 
	 * @return {@link TipoAreaMagazzino} caricate
	 */
	List<TipoAreaMagazzino> caricaTipiAreaMagazzinoConGestioneConai();

	/**
	 * 
	 * @param conaiParametriCreazione
	 *            parametri per la creazione del file
	 * 
	 * @return pdf come array di byte
	 */
	byte[] generaModulo(ConaiParametriCreazione conaiParametriCreazione);

	/**
	 * Salva {@link ConaiArticolo}.
	 * 
	 * @param conaiArticolo
	 *            l'articolo conai da salvare
	 * @return {@link ConaiArticolo} salvato
	 */
	ConaiArticolo salvaArticoloConai(ConaiArticolo conaiArticolo);

	/**
	 * Salva {@link ConaiComponente}.
	 * 
	 * @param conaiComponente
	 *            il componente conai da salvare
	 * @return {@link ConaiComponente} salvato
	 */
	ConaiComponente salvaComponenteConai(ConaiComponente conaiComponente);

}
