package it.eurotn.panjea.conai.rich.bd;

import it.eurotn.panjea.conai.domain.ConaiArticolo;
import it.eurotn.panjea.conai.domain.ConaiArticolo.ConaiMateriale;
import it.eurotn.panjea.conai.domain.ConaiComponente;
import it.eurotn.panjea.conai.domain.ConaiParametriCreazione;
import it.eurotn.panjea.conai.util.AnalisiConaiDTO;
import it.eurotn.panjea.conai.util.parametriricerca.ParametriRicercaAnalisi;
import it.eurotn.panjea.magazzino.domain.ArticoloLite;
import it.eurotn.panjea.magazzino.domain.documento.TipoAreaMagazzino;
import it.eurotn.panjea.rich.bd.AsyncMethodInvocation;

import java.util.List;

/**
 * Business delegate per il conai.
 * 
 * @author giangi
 * @version 1.0, 26/apr/2012
 * 
 */
public interface IConaiBD {

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
	@AsyncMethodInvocation
	List<AnalisiConaiDTO> caricaAnalisiConali(ParametriRicercaAnalisi parametri);

	/**
	 * Carica la lista di {@link ConaiArticolo}; nel caso in cui non siano presenti, vengono generate tante righe quanti
	 * sono i materiali {@link ConaiMateriale}.<br>
	 * 
	 * @return List<ConaiArticolo>
	 */
	@AsyncMethodInvocation
	List<ConaiArticolo> caricaArticoliConai();

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
	 * Carica tutti i tipi area magazzino che hanno la gestione conai abilitata.
	 * 
	 * @return {@link TipoAreaMagazzino} caricate
	 */
	List<TipoAreaMagazzino> caricaTipiAreaMagazzinoConGestioneConai();

	/**
	 * @param parametri
	 *            parametri per la creazione del file
	 * @return pdf come array di byte
	 */
	@AsyncMethodInvocation
	byte[] generaModulo(ConaiParametriCreazione parametri);

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
