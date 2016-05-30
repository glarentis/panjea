package it.eurotn.panjea.ordini.manager.documento.interfaces;

import it.eurotn.panjea.anagrafica.documenti.domain.Documento;
import it.eurotn.panjea.magazzino.exception.TotaleDocumentoNonCoerenteException;
import it.eurotn.panjea.magazzino.manager.rigadocumento.variazioni.provvigioni.interfaces.RigaDocumentoVariazioneProvvigioneStrategy;
import it.eurotn.panjea.magazzino.manager.rigadocumento.variazioni.provvigioni.interfaces.TipoVariazioneProvvigioneStrategy;
import it.eurotn.panjea.magazzino.manager.rigadocumento.variazioni.sconti.interfaces.RigaDocumentoVariazioneScontoStrategy;
import it.eurotn.panjea.magazzino.manager.rigadocumento.variazioni.sconti.interfaces.TipoVariazioneScontoStrategy;
import it.eurotn.panjea.ordini.domain.documento.AreaOrdine;
import it.eurotn.panjea.ordini.util.AreaOrdineRicerca;
import it.eurotn.panjea.ordini.util.parametriricerca.ParametriRicercaAreaOrdine;
import it.eurotn.panjea.partite.domain.AreaPartite;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import javax.ejb.Local;

@Local
public interface AreaOrdineManager {

	/**
	 * Aggiunge una variazione ad ogni riga del documento.
	 * 
	 * @param idAreaOrdine
	 *            area ordine da variare
	 * @param variazione
	 *            variazione da aggiungere
	 * @param percProvvigione
	 *            percentuale di provvigione
	 * @param variazioneScontoStrategy
	 *            strategia di variazione dello sconto
	 * @param tipoVariazioneScontoStrategy
	 *            tipo di variazione dello sconto
	 * @param variazioneProvvigioneStrategy
	 *            strategia di variazione della provvigione
	 * @param tipoVariazioneProvvigioneStrategy
	 *            tipo di variazione della provvigione
	 */
	void aggiungiVariazione(Integer idAreaOrdine, BigDecimal variazione, BigDecimal percProvvigione,
			RigaDocumentoVariazioneScontoStrategy variazioneScontoStrategy,
			TipoVariazioneScontoStrategy tipoVariazioneScontoStrategy,
			RigaDocumentoVariazioneProvvigioneStrategy variazioneProvvigioneStrategy,
			TipoVariazioneProvvigioneStrategy tipoVariazioneProvvigioneStrategy);

	/**
	 * Blocca o sblocca l'area ordine.
	 * 
	 * @param areaOrdine
	 *            area Ordine da bloccare/sbloccare
	 * @param blocca
	 *            true per bloccare l'area ordine, false per sbloccarla
	 * @return area ordine con il nuovo stato
	 */
	AreaOrdine bloccaOrdine(AreaOrdine areaOrdine, boolean blocca);

	/**
	 * Calcola la giacenza in data attuale su ogni riga dell'ordine.
	 * 
	 * @param idAreaOrdine
	 *            id dell'area ordine da calcolare
	 */
	void calcolaGiacenzaRigheOrdine(Integer idAreaOrdine);

	/**
	 * Carica una {@link AreaOrdine}.
	 * 
	 * @param areaOrdine
	 *            area da caricare
	 * @return area caricata
	 */
	AreaOrdine caricaAreaOrdine(AreaOrdine areaOrdine);

	/**
	 * Carica l'area ordine legata al documento.
	 * 
	 * @param documento
	 *            documento interessato
	 * @return areaOrdine per il documento
	 */
	AreaOrdine caricaAreaOrdineByDocumento(Documento documento);

	/**
	 * Se in stato confermato o bloccato invalida l'area ordine portandola in stato provvisorio.
	 * 
	 * @param areaOrdine
	 *            l'area da verificare/invalidare
	 * @return l'area ordine passata o quella aggiornata con stato provvisorio
	 */
	AreaOrdine checkInvalidaAreaMagazzino(AreaOrdine areaOrdine);

	/**
	 * Esegue una copia dell'ordine di riferimento (area ordine, documento, area rate e righe ordine ) e restituisce
	 * l'id del nuovo ordine creato.
	 * 
	 * @param idAreaOrdine
	 *            id dell'ordine da copiare
	 * @return id della copia effettuata
	 */
	Integer copiaOrdine(Integer idAreaOrdine);

	/**
	 * Ricalcola i prezzi delle righe e dei totali dell'area ordine specificata.
	 * 
	 * @param idAreaOrdine
	 *            id dell'area ordine da ricalcolare
	 */
	void ricalcolaPrezziOrdine(Integer idAreaOrdine);

	/**
	 * metodo di ricerca per {@link AreaOrdine}.
	 * 
	 * @param parametriRicercaAreaOrdine
	 *            parametri per la ricerca
	 * 
	 * @return Collection di {@link AreaOrdineRicerca} che soddisfano i criteri di ricerca
	 */
	List<AreaOrdineRicerca> ricercaAreeOrdine(ParametriRicercaAreaOrdine parametriRicercaAreaOrdine);

	/**
	 * Salva una {@link AreaOrdine}.
	 * 
	 * @param areaOrdine
	 *            area da salvare
	 * @return area salvata
	 */
	AreaOrdine salvaAreaOrdine(AreaOrdine areaOrdine);

	/**
	 * 
	 * @param righeDaSpostare
	 *            id delle righe da spostare all'interno del documento
	 * @param idDest
	 *            id della riga di riferimento per lo spostamento. Le righe verranno spostate sopra questa
	 */
	void spostaRighe(Set<Integer> righeDaSpostare, Integer idDest);

	/**
	 * Totalizza il documento<br/>
	 * . Calcola il totale del documento e i vari totalizzatori (spese, spese varie etc...).<br/>
	 * Non salva il documento lo totalizza solamente
	 * 
	 * @param areaOrdine
	 *            areaOrdine da totalizzare
	 * @param areaPartite
	 *            area partite
	 * @return areaOrdine totalizzata
	 */
	AreaOrdine totalizzaDocumento(AreaOrdine areaOrdine, AreaPartite areaPartite);

	/**
	 * Valida le righe ordine.
	 * 
	 * @param areaOrdine
	 *            area ordine
	 * @param areaPartite
	 *            area partite
	 * @param cambioStato
	 *            indica se al termine della verifica si dovrà cambiare lo stato dell'area
	 * @return area ordine
	 * @throws TotaleDocumentoNonCoerenteException
	 */
	AreaOrdine validaRigheOrdine(AreaOrdine areaOrdine, AreaPartite areaPartite, boolean cambioStato);
}
