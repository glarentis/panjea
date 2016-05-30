package it.eurotn.panjea.preventivi.manager.documento.interfaces;

import it.eurotn.panjea.anagrafica.documenti.domain.Documento;
import it.eurotn.panjea.magazzino.exception.TotaleDocumentoNonCoerenteException;
import it.eurotn.panjea.magazzino.manager.rigadocumento.variazioni.provvigioni.interfaces.RigaDocumentoVariazioneProvvigioneStrategy;
import it.eurotn.panjea.magazzino.manager.rigadocumento.variazioni.provvigioni.interfaces.TipoVariazioneProvvigioneStrategy;
import it.eurotn.panjea.magazzino.manager.rigadocumento.variazioni.sconti.interfaces.RigaDocumentoVariazioneScontoStrategy;
import it.eurotn.panjea.magazzino.manager.rigadocumento.variazioni.sconti.interfaces.TipoVariazioneScontoStrategy;
import it.eurotn.panjea.partite.domain.AreaPartite;
import it.eurotn.panjea.preventivi.domain.documento.AreaPreventivo;
import it.eurotn.panjea.preventivi.domain.documento.AreaPreventivo.StatoAreaPreventivo;
import it.eurotn.panjea.preventivi.service.exception.ClientePotenzialePresenteException;
import it.eurotn.panjea.preventivi.util.AreaPreventivoRicerca;
import it.eurotn.panjea.preventivi.util.parametriricerca.ParametriRicercaAreaPreventivo;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import javax.ejb.Local;

@Local
public interface AreaPreventivoManager {

	/**
	 * Aggiunge una variazione e modifica la percentuale di variazione a ogni riga articolo dell'area magazzino.
	 *
	 * @param idAreaPreventivo
	 *            area magazzino
	 * @param variazione
	 *            variazione
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
	void aggiungiVariazione(Integer idAreaPreventivo, BigDecimal variazione, BigDecimal percProvvigione,
			RigaDocumentoVariazioneScontoStrategy variazioneScontoStrategy,
			TipoVariazioneScontoStrategy tipoVariazioneScontoStrategy,
			RigaDocumentoVariazioneProvvigioneStrategy variazioneProvvigioneStrategy,
			TipoVariazioneProvvigioneStrategy tipoVariazioneProvvigioneStrategy);

	/**
	 * Blocca o sblocca l'area ordine.
	 *
	 * @param areaPreventivo
	 *            area Preventivo da bloccare/sbloccare
	 * @param blocca
	 *            true per bloccare l'area ordine, false per sbloccarla
	 * @return area ordine con il nuovo stato
	 */
	AreaPreventivo bloccaPreventivo(AreaPreventivo areaPreventivo, boolean blocca);

	/**
	 * Calcola la giacenza in data attuale su ogni riga dell'ordine.
	 *
	 * @param idAreaPreventivo
	 *            id dell'area ordine da calcolare
	 */
	void calcolaGiacenzaRighePreventivo(Integer idAreaPreventivo);

	/**
	 *
	 * @param areaPreventivo
	 *            areaPreventivo
	 * @param statoDaApplicare
	 *            statoDaApplicare
	 * @return areaPreventivo con statoModificato.
	 * @throws ClientePotenzialePresenteException
	 *             sollevata il preventivo fa riferimento ad un cliente potenziale all'accettazione
	 */
	AreaPreventivo cambiaStatoSePossibile(AreaPreventivo areaPreventivo, StatoAreaPreventivo statoDaApplicare)
			throws ClientePotenzialePresenteException;

	/**
	 * Carica una {@link AreaPreventivo}.
	 *
	 * @param areaPreventivo
	 *            area da caricare
	 * @return area caricata
	 */
	AreaPreventivo caricaAreaPreventivo(AreaPreventivo areaPreventivo);

	/**
	 * Carica l'area ordine legata al documento.
	 *
	 * @param documento
	 *            documento interessato
	 * @return areaPreventivo per il documento
	 */
	AreaPreventivo caricaAreaPreventivoByDocumento(Documento documento);

	/**
	 * Se in stato confermato o bloccato invalida l'area ordine portandola in stato provvisorio.
	 *
	 * @param areaPreventivo
	 *            l'area da verificare/invalidare
	 * @return l'area ordine passata o quella aggiornata con stato provvisorio
	 */
	AreaPreventivo checkInvalidaAreaPreventivo(AreaPreventivo areaPreventivo);

	/**
	 * Esegue una copia dell'ordine di riferimento (area ordine, documento, area rate e righe ordine ) e restituisce
	 * l'id del nuovo ordine creato.
	 *
	 * @param idAreaPreventivo
	 *            id dell'ordine da copiare
	 * @return id della copia effettuata
	 */
	AreaPreventivo copiaPreventivo(Integer idAreaPreventivo);

	/**
	 * Ricalcola i prezzi delle righe e dei totali dell'area ordine specificata.
	 *
	 * @param idAreaPreventivo
	 *            id dell'area ordine da ricalcolare
	 */
	void ricalcolaPrezziPreventivo(Integer idAreaPreventivo);

	/**
	 * metodo di ricerca per {@link AreaPreventivo}.
	 *
	 * @param parametriRicercaAreaPreventivo
	 *            parametri per la ricerca
	 *
	 * @return Collection di {@link AreaPreventivoRicerca} che soddisfano i criteri di ricerca
	 */
	List<AreaPreventivoRicerca> ricercaAreePreventivo(ParametriRicercaAreaPreventivo parametriRicercaAreaPreventivo);

	/**
	 * Salva una {@link AreaPreventivo}.
	 *
	 * @param areaPreventivo
	 *            area da salvare
	 * @return area salvata
	 */
	AreaPreventivo salvaAreaPreventivo(AreaPreventivo areaPreventivo);

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
	 * @param areaPreventivo
	 *            areaPreventivo da totalizzare
	 * @param areaPartite
	 *            area partite
	 * @return areaPreventivo totalizzata
	 */
	AreaPreventivo totalizzaDocumento(AreaPreventivo areaPreventivo, AreaPartite areaPartite);

	/**
	 * Valida le righe ordine.
	 *
	 * @param areaPreventivo
	 *            area ordine
	 * @param areaPartite
	 *            area partite
	 * @param cambioStato
	 *            indica se al termine della verifica si dovrà cambiare lo stato dell'area
	 * @return area ordine
	 * @throws TotaleDocumentoNonCoerenteException
	 */
	AreaPreventivo validaRighePreventivo(AreaPreventivo areaPreventivo, AreaPartite areaPartite, boolean cambioStato);
}
