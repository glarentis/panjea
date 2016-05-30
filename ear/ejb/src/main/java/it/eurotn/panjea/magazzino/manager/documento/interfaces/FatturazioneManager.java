package it.eurotn.panjea.magazzino.manager.documento.interfaces;

import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento;
import it.eurotn.panjea.magazzino.domain.DatiGenerazione;
import it.eurotn.panjea.magazzino.domain.SedeMagazzinoLite;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzinoLite;
import it.eurotn.panjea.magazzino.exception.RigaArticoloNonValidaException;
import it.eurotn.panjea.magazzino.exception.SedeNonAppartieneAdEntitaException;
import it.eurotn.panjea.magazzino.service.exception.SedePerRifatturazioneAssenteException;
import it.eurotn.panjea.magazzino.util.MovimentoFatturazioneDTO;
import it.eurotn.panjea.magazzino.util.parametriricerca.ParametriRicercaFatturazione;

import java.util.Date;
import java.util.List;

import javax.ejb.Local;

@Local
public interface FatturazioneManager {

	/**
	 * Cancella i movimenti in fatturazione creati da una fatturazione differita temporanea dell'utente specificato.
	 *
	 * @param utente
	 *            utente di riferimento
	 */
	void cancellaMovimentiInFatturazione(String utente);

	/**
	 * Carica tutte le aree magazzino per la fatturazione in base ai parametri di ricerca utilizzati.<br/>
	 * La ricerca esegue anche il controllo delle aree tramite le rules validation (
	 * {@link AbstractRigaMagazzinoRulesValidation}).
	 *
	 * @param parametriRicercaFatturazione
	 *            parametri per la ricerca
	 * @return lista di AreaMagazzinoLite con le righe non valide settate
	 */
	List<AreaMagazzinoLite> caricaAreeMagazzino(ParametriRicercaFatturazione parametriRicercaFatturazione);

	/**
	 * Carica la data dei movimenti che sono attualmente in fatturazione.
	 *
	 * @return <code>null</code> se non ci sono movimenti in fatturazione
	 */
	Date caricaDataMovimentiInFatturazione();

	/**
	 * Carica i dati generazione della fatturazione temporanea.
	 *
	 * @return generazione caricati
	 */
	List<DatiGenerazione> caricaDatiGenerazioneFatturazioneTemporanea();

	/**
	 * Carica tutte le fatturazioni eseguite nell'anno di riferimento.
	 *
	 * @param annoFatturazione
	 *            anno di riferimento
	 * @return lista delle fatturazioni caricate
	 */
	List<DatiGenerazione> caricaFatturazioni(int annoFatturazione);

	/**
	 * Carica tutti i movimenti generati dalla fatturazione della data di riferimento.
	 *
	 * @param dataCreazione
	 *            data di riferimento
	 * @return <code>List</code> di {@link AreaMagazzinoLite} caricate
	 */
	List<AreaMagazzinoLite> caricaMovimentiPerFatturazione(Date dataCreazione);

	/**
	 * Carica tutti i movimenti per la fatturazione con la data creazione richiesta. Se la data di creazione è nulla
	 * verranno caricati tutti i documenti in fatturazione temporanea.
	 *
	 * @param dataCreazione
	 *            data di riferimento
	 * @param utente
	 *            utente
	 * @return movimenti caricati
	 * */
	List<MovimentoFatturazioneDTO> caricaMovimentPerFatturazione(Date dataCreazione, String utente);

	/**
	 * Carica tutti i tipi documenti che possono essere selezionati per la fatturazione.
	 *
	 * @return lista di {@link TipoDocumento} caricati.
	 */
	List<TipoDocumento> caricaTipiDocumentoDestinazioneFatturazione();

	/**
	 * Carica tutti i tipi documento che devono essere fatturate nel tipo documento passato come parametro.
	 *
	 * @param tipoDocumentoDiFatturazione
	 *            tipo di documento utilizzato per la fatturazione
	 * @return tipi di documento che hanno come tipo documento di destinazione il tipo documento passato come parametro
	 *         tipoDocumentoDiFatturazione
	 */
	List<TipoDocumento> caricaTipiDocumentoPerFatturazione(TipoDocumento tipoDocumentoDiFatturazione);

	/**
	 * Conferma tutti i documenti in fatturazione.
	 *
	 * @param areeDaConfermare
	 *            lista documenti in fatturazione
	 * @return dati generazione creati
	 */
	DatiGenerazione confermaMovimentiInFatturazione(List<AreaMagazzino> areeDaConfermare);

	/**
	 * @see FatturazioneDifferitaGenerator#genera(List, TipoDocumento, Date, String)
	 * @param areeDaFatturare
	 *            areeDaFatturare
	 * @param tipoDocumentoDestinazione
	 *            tipoDocumentoDestinazione
	 * @param dataDocumentoDestinazione
	 *            dataDocumentoDestinazione
	 * @param noteFatturazione
	 *            noteFatturazione
	 * @param sedePerRifatturazione
	 *            sede per rifatturazione
	 * @param utente
	 *            utente di fatturazione
	 * @throws RigaArticoloNonValidaException
	 *             lanciata quando una riga non è valida
	 * @throws SedePerRifatturazioneAssenteException
	 *             lanciata quando almeno un'area non ha una sede per rifatturazione
	 * @throws SedeNonAppartieneAdEntitaException
	 *             SedeNonAppartieneAdEntitaException
	 */
	void genera(List<AreaMagazzinoLite> areeDaFatturare, TipoDocumento tipoDocumentoDestinazione,
			Date dataDocumentoDestinazione, String noteFatturazione, SedeMagazzinoLite sedePerRifatturazione,
			String utente) throws RigaArticoloNonValidaException, SedePerRifatturazioneAssenteException,
			SedeNonAppartieneAdEntitaException;

	/**
	 * Imposta le fatturazioni della data specificata a esportato.
	 *
	 * @param datiGenerazione
	 *            dati di fatturazione
	 */
	void impostaComeEsportato(DatiGenerazione datiGenerazione);

	/**
	 * Ordina il numero documento dei documenti in fatturazione per denominazione entita. L'ordinamento viene impostato
	 * sulla fatturazione temporanea e alla conferma viene tenuto lo stesso ordine.
	 *
	 * @param utente
	 *            utente dei documenti da ordinare
	 */
	void ordinaFatturazioneCorrente(String utente);

}
